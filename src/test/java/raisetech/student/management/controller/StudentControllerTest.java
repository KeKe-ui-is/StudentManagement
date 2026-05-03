package raisetech.student.management.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.StudentCourseStatus;
import raisetech.student.management.data.StudentSearchCondition;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;
import raisetech.student.management.service.StudentService;
import tools.jackson.databind.ObjectMapper;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    private StudentService service;

    //メッセージの外部化の関係でメッセージ解決をする
    @Autowired
    private MessageSource messageSource;

    //JavaオブジェクトをJsonに変換　逆も可能
    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private StudentRepository repository;

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    //@GetMapping("/studentList")
    @Test
    @DisplayName("受講生詳細の一覧検索の実行ができて空のリストが返ってくる")
    void studentList_searchStudentDetail_returnEmptyList() throws Exception {
        when(service.searchStudentList()).thenReturn(List.of(new StudentDetail()));

        mockMvc.perform(MockMvcRequestBuilders.get("/studentList"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"student\":null,\"studentCourseList\":null}]"));
        verify(service, times(1)).searchStudentList();
    }

    // Studentの入力チェック
    @Test
    @DisplayName("受講生詳細の受講生に適切な情報を入力した際に入力チェックにかからないこと")
    void validateStudent_setStudent_returnIsOk() {

        Student student = new Student();
        student.setId(1);
        student.setName("テスト太郎");
        student.setKanaName("テストタロウ");
        student.setNickname("テスタロウ");
        student.setEmail("test@example.jp");
        student.setAge(20);

        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertThat(violations.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("受講生詳細の受講生IDに3桁以上の数値を入力した際に入力チェックにかかること")
    void validateStudent_idHasMoreThanThreeDigits_returnValidationMessage() {
        Student student = new Student();
        student.setId(1111);
        student.setName("テスト太郎");
        student.setKanaName("テストタロウ");
        student.setNickname("テスタロウ");
        student.setEmail("test@example.jp");
        student.setAge(20);

        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        ConstraintViolation<Student> violation = violations.iterator().next();
        String message = resolveMessage(violation);

        assertThat(violations.size()).isEqualTo(1);
        assertThat("idの大きさは1桁以上3桁までです").isEqualTo(message);
    }

    @Test
    @DisplayName("受講生詳細の受講生の名前が空の値を入力した際に入力チェックにかかること")
    void validateStudent_nameIsEmpty_returnValidationMessage() {
        Student student = new Student();
        student.setId(1);
        student.setName(" ");
        student.setKanaName("テストタロウ");
        student.setNickname("テスタロウ");
        student.setEmail("test@example.jp");
        student.setAge(20);

        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        ConstraintViolation<Student> violation = violations.iterator().next();
        String message = resolveMessage(violation);

        assertThat(violations.size()).isEqualTo(1);
        assertThat("名前の入力が必要です").isEqualTo(message);
    }

    @Test
    @DisplayName("受講生詳細の受講生の名前に５０文字以上の値を入力した際に入力チェックにかかること")
    void validateStudent_nameSizeIsOver_returnValidationMessage() {
        Student student = new Student();
        student.setId(1);
        student.setName("これは50文字以上の名前ですこれは50文字以上の名前ですこれは50文字以上の名前ですこれは50文字以上の名前です");
        student.setKanaName("テストタロウ");
        student.setNickname("テスタロウ");
        student.setEmail("test@example.jp");
        student.setAge(20);

        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        ConstraintViolation<Student> violation = violations.iterator().next();
        String message = resolveMessage(violation);

        assertThat(violations.size()).isEqualTo(1);
        assertThat("名前は1文字以上50文字以内で入力してください").isEqualTo(message);
    }

    @Test
    @DisplayName("受講生詳細の受講生のカナ名が空の値を入力した際に入力チェックにかかること")
    void validateStudent_kanaNameIsEmpty_returnValidationMessage() {
        Student student = new Student();
        student.setId(1);
        student.setName("テスト太郎");
        student.setKanaName(" ");
        student.setNickname("テスタロウ");
        student.setEmail("test@example.jp");
        student.setAge(20);

        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        ConstraintViolation<Student> violation = violations.iterator().next();
        String message = resolveMessage(violation);

        assertThat(violations.size()).isEqualTo(1);
        assertThat("カナ名の入力が必要です").isEqualTo(message);
    }

    @Test
    @DisplayName("受講生詳細の受講生のカナ名に５０文字以上の値を入力した際に入力チェックにかかること")
    void validateStudent_kanaNameSizeIsOver_returnValidationMessage() {
        Student student = new Student();
        student.setId(1);
        student.setName("テスト太郎");
        student.setKanaName("コレハ50モジイジョウノカナメイデスコレハ50モジイジョウノカナメイデスコレハ50モジイジョウノカナメイデス");
        student.setNickname("テスタロウ");
        student.setEmail("test@example.jp");
        student.setAge(20);

        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        ConstraintViolation<Student> violation = violations.iterator().next();
        String message = resolveMessage(violation);

        assertThat(violations.size()).isEqualTo(1);
        assertThat("カナ名は1文字以上50文字以内で入力してください").isEqualTo(message);
    }

    @Test
    @DisplayName("受講生詳細の受講生のニックネームに空の値を入力した際に入力チェックにかかること")
    void validateStudent_nicknameIsEmpty_returnValidationMessage() {
        Student student = new Student();
        student.setId(1);
        student.setName("テスト太郎");
        student.setKanaName("テストタロウ");
        student.setNickname(" ");
        student.setEmail("test@example.jp");
        student.setAge(20);

        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        ConstraintViolation<Student> violation = violations.iterator().next();
        String message = resolveMessage(violation);

        assertThat(violations.size()).isEqualTo(1);
        assertThat("ニックネームの入力が必要です").isEqualTo(message);
    }

    @Test
    @DisplayName("受講生詳細の受講生のニックネームに５０文字以上の値を入力した際に入力チェックにかかること")
    void validateStudent_nicknameSizeIsOver_returnValidationMessage() {
        Student student = new Student();
        student.setId(1);
        student.setName("テスト太郎");
        student.setKanaName("テストタロウ");
        student.setNickname("５０文字以上のニックネームです５０文字以上のニックネームです５０文字以上のニックネームです５０文字以上のニックネームです");
        student.setEmail("test@example.jp");
        student.setAge(20);

        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        ConstraintViolation<Student> violation = violations.iterator().next();
        String message = resolveMessage(violation);

        assertThat(violations.size()).isEqualTo(1);
        assertThat("ニックネームは1文字以上50文字以内で入力してください").isEqualTo(message);
    }

    @Test
    @DisplayName("受講生詳細の受講生のニックネームに５０文字以上の値を入力した際に入力チェックにかかること")
    void validateStudent_emailIsEmpty_returnValidationMessage() {
        Student student = new Student();
        student.setId(1);
        student.setName("テスト太郎");
        student.setKanaName("テストタロウ");
        student.setNickname("テスタロウ");
        student.setEmail(" ");
        student.setAge(20);

        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        Iterator<ConstraintViolation<Student>> iteratorViolation = violations.iterator();

        ConstraintViolation<Student> violation1 = iteratorViolation.next();
        ConstraintViolation<Student> violation2 = iteratorViolation.next();

        String message1 = resolveMessage(violation1);
        String message2 = resolveMessage(violation2);
        List<String> messageList = List.of(message1, message2);

        assertThat(violations.size()).isEqualTo(2);
        assertThat(messageList).contains("メールアドレスの入力が必要です", "メールアドレスの形式が正しくありません");

    }

    @Test
    @DisplayName("受講生詳細の受講生のメールアドレスに５０文字以上の値を入力した際に入力チェックにかかること")
    void validateStudent_emailSizeIsOver_returnValidationMessage() {
        Student student = new Student();
        student.setId(1);
        student.setName("テスト太郎");
        student.setKanaName("テストタロウ");
        student.setNickname("テストタロウ");
        student.setEmail("test50文字以上test50文字以上test50文字以上test50文字以上test50文字以上@example.jp");
        student.setAge(20);

        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        ConstraintViolation<Student> violation = violations.iterator().next();
        String message = resolveMessage(violation);

        assertThat(violations.size()).isEqualTo(1);
        assertThat("メールアドレスは1文字以上50文字以内で入力してください").isEqualTo(message);
    }

    // StudentCourseの入力チェック
    @Test
    @DisplayName("受講生詳細情報の受講生コース情報のコース名に50文字以上の値を入力した際に入力チェックにかかること")
    void validateStudentCourse_courseNameSizeIsOver_returnValidationMessage() {
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setCourseName("JavaフルコースJavaフルコースJavaフルコースJavaフルコースJavaフルコースJavaフルコース");

        Set<ConstraintViolation<StudentCourse>> violations = validator.validate(studentCourse);
        ConstraintViolation<StudentCourse> violation = violations.iterator().next();
        String message = resolveMessage(violation);
        assertThat(violations.size()).isEqualTo(1);
        assertThat("コース名は1文字以上50文字以内で入力してください").isEqualTo(message);

    }

    /**
     * 受講生情報と受講コース情報が入る想定
     * 入力チェックで返されるメッセージを取り出す
     *
     * @param violation 入力チェックのエラーが入ったオブジェクト
     * @return String message
     */
    private String resolveMessage(ConstraintViolation<?> violation) {
        String messageTemplate = violation.getMessageTemplate();
        String resolveMessageTemplate = messageTemplate.replace("{", "").replace("}", "");
        return messageSource.getMessage(resolveMessageTemplate, null, Locale.JAPAN);
    }

    //@GetMapping("/student/{id}")
    @Test
    @DisplayName("受講生詳細情報の検索ができ空の受講生詳細が返ってくること")
    void getStudent_searchStudent_returnEmpty() throws Exception {
        /**
         * 元のコード
         *     @GetMapping("/student/{id}")
         *     public StudentDetail getStudent(
         *             @Parameter(description = "受講生ID 1～3文字で入力", required = true)
         *             @PathVariable
         *             @NotBlank(message = "{student.id.required}")
         *             @Size(min = 1, max = 3, message = "{student.id.size}")
         *             @Pattern(regexp = "^\\d+$", message = "{student.id.pattern}")
         *             @NotNull
         *             String id) {
         *         return service.searchStudent(id);
         *     }
         */
        Integer id = 999;
        when(service.searchStudent(id)).thenReturn(new StudentDetail());

        mockMvc.perform(MockMvcRequestBuilders.get("/student/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"student\":null,\"studentCourseList\":null}"));

        verify(service, times(1)).searchStudent(id);
    }

    @Test
    @DisplayName("受講生詳細情報の検索でidが入力されなかった時404エラーが返ってくること")
    void getStudent_idIsNull_returnIsNotFound() throws Exception {
        String id = null;
        mockMvc.perform(MockMvcRequestBuilders.get("/student/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("受講生詳細情報の検索でidが3桁以上の時エラーのJsonが返る")
    void getStudent_idHasMoreThanThreeDigits_returnBadRequest() throws Exception {
        Integer id = 1111;
        mockMvc.perform(MockMvcRequestBuilders.get("/student/{id}", id))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.length()").value(1))
                .andExpect(jsonPath("$.errors[0].path").value("id"))
                .andExpect(jsonPath("$.errors[0].message").value("idの大きさは1桁以上3桁までです"))
                .andExpect(jsonPath("$.url").value("/student/1111"));
    }

    @Test
    @DisplayName("受講生詳細情報の検索を複合条件で行う成功すると空の受講生詳細がリストで返る")
    void studentMultiple_isOk_returnEmptyList() throws Exception{
        /**
         *     @PostMapping("/studentMultiple")
         *     public List<StudentDetail> searchStudentMultiple(@RequestBody @Valid StudentSearchCondition studentSearchCondition){
         *         return service.searchStudentMultipleCondition(studentSearchCondition);
         *     }
         */
        StudentSearchCondition studentSearchCondition = new StudentSearchCondition();
        studentSearchCondition.setName("テストタロウ");
        studentSearchCondition.setArea("東京");
        studentSearchCondition.setCourseName("Javaフルコース");
        List<StudentDetail> studentDetailList = new ArrayList<>();

        when(service.searchStudentMultipleCondition(any(StudentSearchCondition.class))).thenReturn(studentDetailList);
        String studentConditionJson = objectMapper.writeValueAsString(studentSearchCondition);

        mockMvc.perform(MockMvcRequestBuilders.post("/studentMultiple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentConditionJson))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[]"))
                .andExpect(status().isOk());

        verify(service, times(1)).searchStudentMultipleCondition(any(StudentSearchCondition.class));
    }

    @Test
    @DisplayName("受講生詳細情報の検索を複合条件で行う成功すると受講生詳細がリストで返る")
    void studentMultiple_isOk_returnList() throws Exception{
        StudentSearchCondition studentSearchCondition = new StudentSearchCondition();
        studentSearchCondition.setName("テストタロウ");
        studentSearchCondition.setArea("東京");
        studentSearchCondition.setCourseName("Javaフルコース");

        Student student = createStudent();
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setCourseName("Javaフルコース");
        StudentCourseStatus studentCourseStatus = new StudentCourseStatus();
        studentCourseStatus.setStatus("仮申込");
        studentCourse.setStudentCourseStatus(studentCourseStatus);
        List<StudentCourse> studentCourseList = new ArrayList<>();
        studentCourseList.add(studentCourse);
        StudentDetail studentDetail = new StudentDetail(student, studentCourseList);
        List<StudentDetail> studentDetailList = new ArrayList<>();
        studentDetailList.add(studentDetail);

        when(service.searchStudentMultipleCondition(any(StudentSearchCondition.class))).thenReturn(studentDetailList);
        String studentConditionJson = objectMapper.writeValueAsString(studentSearchCondition);

        mockMvc.perform(MockMvcRequestBuilders.post("/studentMultiple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentConditionJson))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                                [
                                {
                                    "student": {
                                        "name": "テスト太郎",
                                        "nickname": "テスタロウ",
                                        "kanaName": "テストタロウ",
                                        "email": "test@example.jp",
                                        "age": 20,
                                        "sex": "男性",
                                        "deleted": false
                                    },
                                    "studentCourseList": [
                                        {
                                            "courseName": "Javaフルコース",
                                            "studentCourseStatus" :{
                                                "status" :"仮申込"
                                            }
                                        }
                                    ]
                                }
                                ]
                        """))
                .andExpect(status().isOk());

        verify(service, times(1)).searchStudentMultipleCondition(any(StudentSearchCondition.class));
    }

    @Test
    @DisplayName("受講生詳細情報の検索を複合条件で行う際名前のサイズを50文字以上にした時エラーのJsonが返ってくる")
    void studentMultiple_nameSizeIsOver_returnBadRequestJson() throws Exception{
        StudentSearchCondition studentSearchCondition = new StudentSearchCondition();
        studentSearchCondition.setName("テストタロウテストタロウテストタロウテストタロウテストタロウテストタロウテストタロウテストタロウテストタロウ");
        studentSearchCondition.setArea("東京");
        studentSearchCondition.setCourseName("Javaフルコース");

        String studentConditionJson = objectMapper.writeValueAsString(studentSearchCondition);

        mockMvc.perform(MockMvcRequestBuilders.post("/studentMultiple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentConditionJson))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.length()").value(1))
                .andExpect(jsonPath("$.errors[0].path").value("name"))
                .andExpect(jsonPath("$.errors[0].message").value("名前は50文字以内で入力してください"))
                .andExpect(jsonPath("$.url").value("/studentMultiple"));
        verify(service, never()).searchStudentMultipleCondition(any(StudentSearchCondition.class));
    }

    @Test
    @DisplayName("受講生詳細情報の検索を複合条件で行う際コース名のサイズを50文字以上にした時エラーのJsonが返ってくる")
    void studentMultiple_courseNameSizeIsOver_returnBadRequestJson() throws Exception{
        StudentSearchCondition studentSearchCondition = new StudentSearchCondition();
        studentSearchCondition.setName("テスト太郎");
        studentSearchCondition.setArea("東京");
        studentSearchCondition.setCourseName("JavaフルコースJavaフルコースJavaフルコースJavaフルコースJavaフルコースJavaフルコースJavaフルコース");

        String studentConditionJson = objectMapper.writeValueAsString(studentSearchCondition);

        mockMvc.perform(MockMvcRequestBuilders.post("/studentMultiple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentConditionJson))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.length()").value(1))
                .andExpect(jsonPath("$.errors[0].path").value("courseName"))
                .andExpect(jsonPath("$.errors[0].message").value("コース名は1文字以上50文字以内で入力してください"))
                .andExpect(jsonPath("$.url").value("/studentMultiple"));
        verify(service, never()).searchStudentMultipleCondition(any(StudentSearchCondition.class));
    }

    @Test
    @DisplayName("受講生詳細情報の検索を複合条件で行う際申込状況のサイズを10文字以上にした時エラーのJsonが返ってくる")
    void studentMultiple_statusSizeIsOver_returnBadRequestJson() throws Exception{
        StudentSearchCondition studentSearchCondition = new StudentSearchCondition();
        studentSearchCondition.setName("テスト太郎");
        studentSearchCondition.setArea("東京");
        studentSearchCondition.setCourseName("Javaフルコース");
        studentSearchCondition.setStatus("仮申込仮申込仮申込仮申込");

        String studentConditionJson = objectMapper.writeValueAsString(studentSearchCondition);

        mockMvc.perform(MockMvcRequestBuilders.post("/studentMultiple")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentConditionJson))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.length()").value(1))
                .andExpect(jsonPath("$.errors[0].path").value("status"))
                .andExpect(jsonPath("$.errors[0].message").value("申込状況は10文字以内で入力してください"))
                .andExpect(jsonPath("$.url").value("/studentMultiple"));
        verify(service, never()).searchStudentMultipleCondition(any(StudentSearchCondition.class));
    }

    //@PostMapping("/registerStudent")
    @Test
    @DisplayName("受講生詳細新規登録が実行され成功するとポストした受講生詳細が返ってくること")
    void registerStudent_isOk_returnStudentDetail() throws Exception {
        /**
         *     public ResponseEntity<StudentDetail> registerStudent(@RequestBody @Valid StudentDetail studentDetail) {
         *         service.registerStudent(studentDetail);
         *         return ResponseEntity.ok(studentDetail);
         *     }
         */
        Student student = createStudent();
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setCourseName("Javaフルコース");
        StudentCourseStatus studentCourseStatus = new StudentCourseStatus();
        studentCourseStatus.setStatus("仮申込");
        studentCourse.setStudentCourseStatus(studentCourseStatus);
        List<StudentCourse> studentCourseList = new ArrayList<>();
        studentCourseList.add(studentCourse);
        StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

        String studentDetailJson = objectMapper.writeValueAsString(studentDetail);

        mockMvc.perform(MockMvcRequestBuilders.post("/registerStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentDetailJson))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                {
                                    "student": {
                                        "name": "テスト太郎",
                                        "nickname": "テスタロウ",
                                        "kanaName": "テストタロウ",
                                        "email": "test@example.jp",
                                        "age": 20,
                                        "sex": "男性",
                                        "deleted": false
                                    },
                                    "studentCourseList": [
                                        {
                                            "courseName": "Javaフルコース",
                                            "studentCourseStatus" :{
                                                "status" :"仮申込"
                                            }
                                        }
                                    ]
                                }
                                """
                ));
        verify(service, times(1)).registerStudent(any(StudentDetail.class));
    }

    private static Student createStudent() {
        Student student = new Student();
        student.setName("テスト太郎");
        student.setKanaName("テストタロウ");
        student.setNickname("テスタロウ");
        student.setEmail("test@example.jp");
        student.setAge(20);
        student.setSex("男性");
        return student;
    }

    @Test
    @DisplayName("受講生詳細新規登録が実行時範囲外の年齢が入ったデータをポストするとエラーのJsonが返ってくること")
    void registerStudent_ageIsOutOfRange_returnBadRequestJson() throws Exception {
        /**
         *     public ResponseEntity<StudentDetail> registerStudent(@RequestBody @Valid StudentDetail studentDetail) {
         *         service.registerStudent(studentDetail);
         *         return ResponseEntity.ok(studentDetail);
         *     }
         */
        Student student = createStudent();
        student.setAge(-10);
        student.setSex("男性");
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setCourseName("Javaフルコース");
        List<StudentCourse> studentCourseList = new ArrayList<>();
        studentCourseList.add(studentCourse);
        StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

        String studentDetailJson = objectMapper.writeValueAsString(studentDetail);

        mockMvc.perform(MockMvcRequestBuilders.post("/registerStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentDetailJson))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.length()").value(1))
                .andExpect(jsonPath("$.errors[0].path").value("age"))
                .andExpect(jsonPath("$.errors[0].message").value("年齢は0以上で入力してください"))
                .andExpect(jsonPath("$.url").value("/registerStudent"));
        verify(service, never()).registerStudent(any(StudentDetail.class));
    }

    @Test
    @DisplayName("受講生詳細新規登録が実行時異常なデータをポストするとエラーのJsonが返ってくること")
    void registerStudent_multipleAbnormalities_returnBadRequestJson() throws Exception {
        Student student = createStudent();
        student.setName("");
        student.setKanaName("テストタロウテストタロウテストタロウテストタロウテストタロウテストタロウテストタロウテストタロウテストタロウテストタロウ");
        student.setEmail("testexample.jp");
        student.setAge(-10);
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setCourseName("Javaフルコース");
        List<StudentCourse> studentCourseList = new ArrayList<>();
        studentCourseList.add(studentCourse);
        StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

        String studentDetailJson = objectMapper.writeValueAsString(studentDetail);

        mockMvc.perform(MockMvcRequestBuilders.post("/registerStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentDetailJson))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.length()").value(5))
                .andExpect(jsonPath("$.errors[0].path").value("age"))
                .andExpect(jsonPath("$.errors[0].message").value("年齢は0以上で入力してください"))
                .andExpect(jsonPath("$.errors[1].path").value("email"))
                .andExpect(jsonPath("$.errors[1].message").value("メールアドレスの形式が正しくありません"))
                .andExpect(jsonPath("$.errors[2].path").value("kanaName"))
                .andExpect(jsonPath("$.errors[2].message").value("カナ名は1文字以上50文字以内で入力してください"))
                .andExpect(jsonPath("$.errors[3].path").value("name"))
                .andExpect(jsonPath("$.errors[3].message").value("名前の入力が必要です"))
                .andExpect(jsonPath("$.errors[4].path").value("name"))
                .andExpect(jsonPath("$.errors[4].message").value("名前は1文字以上50文字以内で入力してください"))
                .andExpect(jsonPath("$.url").value("/registerStudent"));
        verify(service, never()).registerStudent(any(StudentDetail.class));
    }

    //@PutMapping("/updateStudent")
    @Test
    @DisplayName("受講生詳細情報の更新が実行され成功すると成功メッセージが返ってくること")
    void updateStudent_isOk_returnSuccessMessage() throws Exception {
        /**
         *     public ResponseEntity<String> updateStudent(@RequestBody @Valid StudentDetail studentDetail) {
         *         //更新処理
         *         service.updateStudent(studentDetail);
         *         return ResponseEntity.ok("更新処理が成功しました。");
         *     }
         */
        Student student = createStudent();
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setId(student.getId());
        studentCourse.setStudentId(1);
        studentCourse.setCourseName("Javaフルコース");
        StudentCourseStatus studentCourseStatus = new StudentCourseStatus();
        studentCourseStatus.setId(1);
        studentCourseStatus.setStudentCourseId(studentCourse.getId());
        studentCourseStatus.setStatus("本申込");
        studentCourse.setStudentCourseStatus(studentCourseStatus);
        List<StudentCourse> studentCourseList = new ArrayList<>();
        studentCourseList.add(studentCourse);
        StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

        String studentDetailJson = objectMapper.writeValueAsString(studentDetail);

        mockMvc.perform(MockMvcRequestBuilders.put("/updateStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentDetailJson))
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(status().isOk())
                .andExpect(content().string("更新処理が成功しました。"));
        verify(service, times(1)).updateStudent(any(StudentDetail.class));
    }

    @Test
    @DisplayName("受講生詳細情報の更新実行時異常なデータをポストするとエラーのJsonが返ってくること")
    void updateStudent_multipleAbnormalities_returnBadRequestJson() throws Exception {
        Student student = createStudent();
        student.setName("");
        student.setKanaName("テストタロウテストタロウテストタロウテストタロウテストタロウテストタロウテストタロウテストタロウテストタロウテストタロウ");
        student.setEmail("testexample.jp");
        student.setAge(-10);
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setCourseName("Javaフルコース");
        List<StudentCourse> studentCourseList = new ArrayList<>();
        studentCourseList.add(studentCourse);
        StudentDetail studentDetail = new StudentDetail(student, studentCourseList);

        String studentDetailJson = objectMapper.writeValueAsString(studentDetail);

        mockMvc.perform(MockMvcRequestBuilders.put("/updateStudent")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(studentDetailJson))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.length()").value(5))
                .andExpect(jsonPath("$.errors[0].path").value("age"))
                .andExpect(jsonPath("$.errors[0].message").value("年齢は0以上で入力してください"))
                .andExpect(jsonPath("$.errors[1].path").value("email"))
                .andExpect(jsonPath("$.errors[1].message").value("メールアドレスの形式が正しくありません"))
                .andExpect(jsonPath("$.errors[2].path").value("kanaName"))
                .andExpect(jsonPath("$.errors[2].message").value("カナ名は1文字以上50文字以内で入力してください"))
                .andExpect(jsonPath("$.errors[3].path").value("name"))
                .andExpect(jsonPath("$.errors[3].message").value("名前の入力が必要です"))
                .andExpect(jsonPath("$.errors[4].path").value("name"))
                .andExpect(jsonPath("$.errors[4].message").value("名前は1文字以上50文字以内で入力してください"))
                .andExpect(jsonPath("$.url").value("/updateStudent"));
        verify(service, never()).updateStudent(any(StudentDetail.class));
    }


}