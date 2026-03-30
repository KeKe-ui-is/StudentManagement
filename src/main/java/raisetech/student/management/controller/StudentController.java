package raisetech.student.management.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.apache.ibatis.annotations.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
            @NotBlank(message = "{student.id.required}")
            @Size(min = 1, max = 3, message = "{student.id.size}")
            @Pattern(regexp = "^\\d+$", message = "{student.id.pattern}")
            @NotNull
            String id) {
        return service.searchStudent(id);
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

