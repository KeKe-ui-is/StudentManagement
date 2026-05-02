package raisetech.student.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.StudentSearchCondition;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.exception.TestException;
import raisetech.student.management.exception.ViolationErrorResponse;
import raisetech.student.management.service.StudentService;

import java.util.List;

/**
 * 受講生の検索や登録、更新などを行うREST APIとして実行されるControllerです。
 */
@Validated
@RestController
public class StudentController {

    private StudentService service;

    /**
     * コンストラクタ
     */
    @Autowired
    public StudentController(StudentService service) {
        this.service = service;
    }

    /**
     * 受講生詳細一覧検索機能です。
     * 全件検索を行うので条件指定は不要です。
     * 以上の受付を行うControllerです。
     *
     * @return 受講生詳細一覧（全件数）
     */
    @Operation(
            summary = "一覧検索",
            description = "受講生の一覧を検索します",
            operationId = "getStudentList",
            tags = {"SearchStudent"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "検索成功"),
            @ApiResponse(responseCode = "500", description = "サーバー内部エラー")
    })
    @GetMapping("/studentList")
    public List<StudentDetail> getStudentList() {
        return service.searchStudentList();
    }

    /**
     * 受講生コース情報一覧検索機能です。
     * 全件検索を行うので条件指定は不要です。
     * 以上の受付を行うControllerです。
     * @return 受講生コース情報(全件数）
     */
    @Operation(
            summary = "一覧検索",
            description = "受講生コース情報の一覧を検索します",
            operationId = "getStudentCourseList",
            tags = {"SearchStudent"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "検索成功"),
            @ApiResponse(responseCode = "500", description = "サーバー内部エラー")
    })
    @GetMapping("/courseList")
    public List<StudentCourse> getStudentCourseList(){
        return service.searchStudentCourseList();
    }

    /**
     * 受講生検索です。
     * idに紐づく受講生と受講コースの情報を取得します。
     *
     * @param id 受講生ID
     * @return 受講生情報とそれに紐づく受講コース情報
     */
    @Operation(
            summary = "個別検索",
            description = "入力されたidに紐づく受講生を検索します",
            operationId = "getStudent",
            tags = {"SearchStudent"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "検索成功"),
            @ApiResponse(
                    responseCode = "400",
                    description = "入力チェックエラー",
                    content = @Content(schema = @Schema(implementation = ViolationErrorResponse.class))
            ),
            @ApiResponse(responseCode = "500", description = "サーバー内部エラー")
    })
    @GetMapping("/student/{id}")
    public StudentDetail getStudent(
            @Parameter(description = "受講生ID 1～3文字で入力", required = true)
            @PathVariable
            @Min(value = 0, message = "{student.id.size}")
            @Max(value = 999, message = "{student.id.size}")
            @NotNull
            Integer id) {
        return service.searchStudent(id);
    }
    //受講生の複数の条件を用いた検索
    @Operation(
            summary = "受講生複数条件検索",
            description = "名前や地域など複数条件を指定して検索します。",
            tags = {"SearchStudent"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "検索成功",
                    content = @Content(schema = @Schema(implementation = Student.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "入力値が不正です",
                    content = @Content(schema = @Schema(implementation = ViolationErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "サーバー内部エラー"
            )
    })
    /**
     * GETメソッドで検索条件を受け取り受講生の検索を行う
     * @param studentSearchCondition 受講生検索条件
     * @return 受講生
     */
    @PostMapping("/studentMultiple")
    public List<StudentDetail> searchStudentMultiple(@RequestBody @Valid StudentSearchCondition studentSearchCondition){
        return service.searchStudentMultipleCondition(studentSearchCondition);
    }

    /**
     * 受講生詳細の登録を行います。
     * 受講生と受講生コース情報を個別に登録し、受講生コース情報には受講生コースと紐づけるための値を設定
     * 受講生コース情報にあ紐づける値や開始日修了予定日などの日付情報を設定
     *
     * @param studentDetail 受講生詳細
     * @return 実行結果
     */
    @Operation(
            summary = "受講生情報登録",
            description = "受講生と受講コースの情報を登録します。",
            tags = {"UpdateStudentDB"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "登録成功",
                    content = @Content(schema = @Schema(implementation = StudentDetail.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "入力値が不正です",
                    content = @Content(schema = @Schema(implementation = ViolationErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "サーバー内部エラー"
            )
    })
    @PostMapping("/registerStudent")
    public ResponseEntity<StudentDetail> registerStudent(@RequestBody @Valid StudentDetail studentDetail) {
        service.registerStudent(studentDetail);
        return ResponseEntity.ok(studentDetail);
    }

    /**
     * 受講生詳細の更新を行う。キャンセルフラグの更新もここで行います（論理削除）
     *
     * @param studentDetail 受講生詳細
     */
    @Operation(
            summary = "受講生情報更新",
            description = "受講生と受講コースの情報を更新します。",
            tags = {"UpdateStudentDB"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "更新成功",
                    content = @Content(schema = @Schema(implementation = StudentDetail.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "入力値が不正です",
                    content = @Content(schema = @Schema(implementation = ViolationErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "サーバー内部エラー"
            )
    })
    @PutMapping("/updateStudent")
    public ResponseEntity<String> updateStudent(@RequestBody @Valid StudentDetail studentDetail) {
        //更新処理
        service.updateStudent(studentDetail);
        return ResponseEntity.ok("更新処理が成功しました。");
    }

    /**
     * 例外処理をテストするためにエラーを送るメソッド
     */
    @GetMapping("testStudent")
    public void testStudent() throws TestException {
        throw new TestException("テストのエラーが発生しました。");
    }

}

