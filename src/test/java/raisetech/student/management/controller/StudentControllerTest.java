package raisetech.student.management.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;
import raisetech.student.management.service.StudentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//これでテスト用のspring bootが起動する　全部動くわけではない
@WebMvcTest(StudentController.class)
class StudentControllerTest {
    //    MockMvcこれはspring bootが用意しているMockの仕組み Mockitではない　基本的には入れる
    @Autowired
    MockMvc mockMvc;

    //    Spring BootではMock化の仕組みが違う StudentControllerが@WebMvcTestに渡されると
//    インスタンス生成されてしまう。
//        @BeforeEach
//    void before() {
//        sut = new StudentService(repository, converter);
//    }  serviceでやってたこれはしない
    @MockitoBean
    private StudentService service;
    @MockitoBean
    private StudentRepository repository;

    //この時のValidatorはjakartaのものを使う 入力チェック時のテスト時に使用
    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


    @Test
    void 受講生詳細情報の一覧検索が実行できて空のリストが返ってくる() throws Exception {
        /**
         *  元のコード
         *     public List<StudentDetail> getStudentList() {
         *         return service.searchStudentList();
         *     }
         */
        when(service.searchStudentList()).thenReturn(List.of(new StudentDetail()));
        mockMvc.perform(get("/studentList"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"student\":null,\"studentCourseList\":null}]"));

        verify(service, times(1)).searchStudentList();
    }

    //    入力チェックのテスト
    @Test
    void 受講生詳細の受講生で適切な値を入力した時に入力チェックに異常が発生しないこと() {
        Student student = new Student();
        student.setId("1");
        student.setName("小林渓人");
        student.setKanaName("コバヤシケイト");
        student.setNickname("けい");
        student.setEmail("xiaolin@example.com");
        student.setAge(22);
//        入力チェックの結果 このviolationsに対していろいろ検証していく
        Set<ConstraintViolation<Student>> violations = validator.validate(student);
//        今回はIDのチェックをしているのでエラーが１つ起きているはず　
//    assertEquals(1,violations.size());　
//    基本的にAssertionsは統一する
//    assertThat assertjといわれるもの↓
        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    void 受講生詳細の受講生のIDに数値以外を用いた時に入力チェックにかかること() {
        Student student = new Student();
        student.setId("あああ");
        student.setName("小林渓人");
        student.setKanaName("コバヤシケイト");
        student.setNickname("けい");
        student.setEmail("xiaolin@example.com");
        student.setAge(22);

        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations).extracting("message").containsOnly("数字のみ入力してください");
    }

    @Test
    void 受講生詳細の受講生の年齢に範囲外の数字を用いた時に入力チェックにかかること() {
        Student student = new Student();
        student.setId("999");
        student.setName("小林渓人");
        student.setKanaName("コバヤシケイト");
        student.setNickname("けい");
        student.setEmail("xiaolin@example.com");
        student.setAge(-1);

        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertThat(violations.size()).isEqualTo(1);
        assertThat(violations)
                .extracting(ConstraintViolation::getMessage)
                .containsOnly("{student.age.min}");
    }


    @Test
    void 受講生詳細情報の検索が実行できて受講生詳細情報が返ってくる() throws Exception {
        /**
         *      @GetMapping("/student/{id}")
         *     public StudentDetail getStudent(
         *             @Parameter(description = "受講生ID 1～3文字で入力",required = true)
         *             @PathVariable
         *             @Size(min = 1, max = 3)
         *             @NotNull
         *             String id) {
         *         return service.searchStudent(id);
         *     }
         */
//        事前準備
        String id = "999";
        Student student = new Student();
        student.setId(id);
        student.setName("テスト太郎");

        List<StudentCourse> studentCourseList = new ArrayList<>();
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setStudentId(id);
        studentCourse.setCourseName("Javaフルコース");
        studentCourseList.add(studentCourse);

        StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

        when(service.searchStudent(id)).thenReturn(studentDetail);
        mockMvc.perform(get("/student/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json("{\n" +
                        "  \"student\": {\n" +
                        "    \"id\" : \"999\" , \n " +
                        "    \"name\": \"テスト太郎\",\n" +
                        "    \"kanaName\": null,\n" +
                        "    \"nickname\": null,\n" +
                        "    \"email\": null,\n" +
                        "    \"area\": null,\n" +
                        "    \"age\": null,\n" +
                        "    \"sex\": null,\n" +
                        "    \"remark\": null,\n" +
                        "    \"deleted\": false\n" +
                        "  },\n" +
                        "  \"studentCourseList\": [\n" +
                        "    {\n" +
                        "      \"courseName\": \"Javaフルコース\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}"));
        verify(service, times(1)).searchStudent(id);
    }

    @Test
    void 存在しないidで受講生詳細情報の検索を実行後エラーが返ってくる() throws Exception {
        /**
         *      @GetMapping("/student/{id}")
         *     public StudentDetail getStudent(
         *             @Parameter(description = "受講生ID 1～3文字で入力",required = true)
         *             @PathVariable
         *             @Size(min = 1, max = 3)
         *             @NotNull
         *             String id) {
         *         return service.searchStudent(id);
         *     }
         */
//        事前準備
        String id = "999";
        when(service.searchStudent(id)).thenThrow(new RuntimeException("System Error"));
        mockMvc.perform(get("/student/{id}", id))
                .andExpect(status().isInternalServerError());

        verify(service, times(1)).searchStudent(id);

    }

    @Test
    void 受講生詳細情報の登録が実行できレスポンスエンティティとして受講生詳細情報が返ってくること() throws Exception {
        /**
         *      元のコード
         *     @PostMapping("/registerStudent")
         *     public ResponseEntity<StudentDetail> registerStudent(@RequestBody @Valid StudentDetail studentDetail) {
         *         service.registerStudent(studentDetail);
         *         return ResponseEntity.ok(studentDetail);
         *     }
         */

//        doNothing().when(service).registerStudent(any(StudentDetail.class));
//        実行
        mockMvc.perform(MockMvcRequestBuilders.post("/registerStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "student" :{
                                        "id" : "999",
                                        "name" : "テスト太郎",
                                        "kanaName" : "テストタロウ",
                                        "nickname" : "てす",
                                        "email" : "test@example.jp",
                                        "age" : 20
                                    },
                                    "studentCourseList" : [
                                        {
                                            "studentId" : "999",
                                            "courseName" : "Javaフルコース"
                                        }
                                    ]
                                }
                                """))
                .andExpect(status().isOk());
        verify(service, times(1)).registerStudent(any());
    }

    @Test
    void 受講生詳細情報の登録時に不正な値を送ると500エラーが返ること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/registerStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "student" : {
                                    "id" : "999",
                                    "name" : "",
                                    "kanaName" : "テストタロウ",
                                    "nickname" : "てす",
                                    "email" : "test@example.jp",
                                    "age" : 20
                                },
                                "studentCourseList" : [
                                    {
                                        "studentId" : "999",
                                        "courseName" : "Javaフルコース"
                                    }
                                ]
                            }
                            """))
                .andExpect(status().is5xxServerError());

        verify(service, never()).registerStudent(any(StudentDetail.class));
    }

    @Test
    void 受講生更新処理が実行できレスポンスエンティティが返ってくるか() throws Exception {
        /**
         *     @PutMapping("/updateStudent")
         *     public ResponseEntity<String> updateStudent(@RequestBody @Valid StudentDetail studentDetail) {
         *         //更新処理
         *         service.updateStudent(studentDetail);
         *         return ResponseEntity.ok("更新処理が成功しました。");
         *     }
         */

        mockMvc.perform(MockMvcRequestBuilders.put("/updateStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "student" :{
                                        "id" : "999",
                                        "name" : "テスト太郎",
                                        "kanaName" : "テストタロウ",
                                        "nickname" : "てす",
                                        "email" : "test@example.jp",
                                        "age" : 20
                                    },
                                    "studentCourseList" : [
                                        {
                                            "studentId" : "999",
                                            "courseName" : "Javaフルコース"
                                        }
                                    ]
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(content().string("更新処理が成功しました。"));

        verify(service, times(1)).updateStudent(any(StudentDetail.class));
    }

    @Test
    void 受講生更新時に不正な値を送ると500エラーが返ること() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.put("/updateStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "student" : {
                                    "id" : "999",
                                    "name" : "テスト太郎",
                                    "kanaName" : "テストタロウ",
                                    "nickname" : "てす",
                                    "email" : "abc",
                                    "age" : 20
                                },
                                "studentCourseList" : [
                                    {
                                        "studentId" : "999",
                                        "courseName" : "Javaフルコース"
                                    }
                                ]
                            }
                            """))
                .andExpect(status().is5xxServerError());

        verify(service, never()).updateStudent(any(StudentDetail.class));
    }
}