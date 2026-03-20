package raisetech.student.management.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.apache.ibatis.io.ResolverUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.exception.TestException;
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
     * @return 受講生詳細一覧（全件数）
     */
    @GetMapping("/studentList")
    public List<StudentDetail> getStudentList(){
        return service.searchStudentList();
    }

    /**
     * 受講生検索です。
     * idに紐づく受講生と受講コースの情報を取得します。
     * @param id 受講生ID
     * @return 受講生情報とそれに紐づく受講コース情報
     */
    @GetMapping("/student/{id}")
    public StudentDetail getStudent(@PathVariable @Size(min = 1,max = 3) @NotNull String id){
        return service.searchStudent(id);
    }

    /**
     * 受講生詳細の登録を行います。
     * 受講生と受講生コース情報を個別に登録し、受講生コース情報には受講生コースと紐づけるための値を設定
     * 受講生コース情報にあ紐づける値や開始日修了予定日などの日付情報を設定
     * @param studentDetail　受講生詳細
     * @return 実行結果
     */
    @PostMapping("/registerStudent")
    public ResponseEntity<StudentDetail> registerStudent(@RequestBody @Valid StudentDetail studentDetail) {
        service.registerStudent(studentDetail);
        return ResponseEntity.ok(studentDetail);
    }
    /**
     * 受講生詳細の更新を行う。キャンセルフラグの更新もここで行います（論理削除）
     * @param studentDetail 受講生詳細
     */
    @PutMapping("/updateStudent")
    public ResponseEntity<String> updateStudent(@RequestBody StudentDetail studentDetail){
        //更新処理
        service.updateStudent(studentDetail);
        return ResponseEntity.ok("更新処理が成功しました。");
    }

    /**
     * 例外処理をテストするためにエラーを送るメソッド
     */
    @GetMapping("testStudent")
    public void testStudent() throws TestException{
        throw new TestException("テストのエラーが発生しました。");
    }

    @ExceptionHandler(TestException.class)
    public ResponseEntity<String> handleTestException(TestException ex){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}

