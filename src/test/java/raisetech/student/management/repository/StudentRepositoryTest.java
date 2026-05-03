package raisetech.student.management.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.StudentCourseStatus;
import raisetech.student.management.data.StudentSearchCondition;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

@MybatisTest
class StudentRepositoryTest {
    @Autowired
    StudentRepository sut;

    @Test
    @DisplayName("受講生の全件検索が行えること")
    void search_studentList(){
        List<Student> actual = sut.search();
        assertThat(actual).hasSize(5);
    }

    @Test
    @DisplayName("受講生コース情報の全件検索が行えること")
    void searchStudentCourseList_studentCourseList(){
        List<StudentCourse> actual = sut.searchStudentCourseList();
        assertThat(actual).hasSize(8);
    }

    @Test
    @DisplayName("申込状況の全件検索が行えること")
    void searchStatusList_studentCourseStatusList(){
        List<StudentCourseStatus> actual = sut.searchStatusList();
        assertThat(actual).hasSize(8);
    }

    @Test
    @DisplayName("指定したidに合致する受講生の検索が行えること")
    void searchStudent_student(){
        Integer id = 1;
        Student expected = new Student();
        expected.setId(1);
        expected.setName("中村悠斗");
        expected.setKanaName("ナカムラユウト");
        expected.setNickname("ユウト");
        expected.setEmail("yuto.nakamura@example.com");
        expected.setArea("神奈川");
        expected.setAge(26);
        expected.setSex("男性");
        expected.setRemark("Java Silver取得を目指して学習中");
        expected.setDeleted(false);

        Student actual = sut.searchStudent(id);

        assertStudent(actual,expected);
    }

    @Test
    @DisplayName("存在しないidを指定して受講生の検索をすると受講生情報がNullで返ること")
    void searchStudent_notFound_returnEmpty(){
        Student actual = sut.searchStudent(999);

        assertThat(actual).isNull();
    }

    @Test
    @DisplayName("指定した受講生idに合致する受講生コース情報の検索が行えること")
    void searchStudentCourse_studentCourseList() {
        StudentCourse studentCourse1 = new StudentCourse();
        studentCourse1.setId(2);
        studentCourse1.setStudentId(2);
        studentCourse1.setCourseName("Web開発コース");
        studentCourse1.setCourseStartAt(stringToLocalDateTime("2026-04-21 10:00:00"));
        studentCourse1.setCourseEndAt(stringToLocalDateTime("2026-10-21 18:00:00"));
        StudentCourse studentCourse2 = new StudentCourse();
        studentCourse2.setId(8);
        studentCourse2.setStudentId(2);
        studentCourse2.setCourseName("Python基礎コース");
        studentCourse2.setCourseStartAt(stringToLocalDateTime("2026-04-26 10:00:00"));
        studentCourse2.setCourseEndAt(stringToLocalDateTime("2026-10-26 18:00:00"));

        List<StudentCourse> expected = new ArrayList<>();
        expected.add(studentCourse1);
        expected.add(studentCourse2);

        List<StudentCourse> actual = sut.searchStudentCourse(2);

        assertThat(actual).hasSize(2);

        assertStudentCourse(actual.get(0),expected.get(0));
        assertStudentCourse(actual.get(1),expected.get(1));
    }

    @Test
    @DisplayName("存在しない受講生idを指定すると受講生コース情報が空で返ること")
    void searchStudentCourse_notFound_returnEmpty() {
        List<StudentCourse> actual = sut.searchStudentCourse(999);

        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("指定した受講コースidに合致する申込状況の検索が行えること")
    void searchStudentCourseStatus_returnList(){
        Integer id = 1;
        StudentCourseStatus expected = new StudentCourseStatus();
        expected.setId(1);
        expected.setStudentCourseId(1);
        expected.setStatus("仮申込");

        StudentCourseStatus actual = sut.searchStudentCourseStatus(id);

        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getStudentCourseId()).isEqualTo(expected.getStudentCourseId());
        assertThat(actual.getStatus()).isEqualTo(expected.getStatus());
    }

    @Test
    @DisplayName("存在しない受講コースidを指定すると申込状況が空で返ること")
    void searchStudentCourseStatus_notFound_returnEmpty() {
        StudentCourseStatus actual = sut.searchStudentCourseStatus(999);

        assertThat(actual).isNull();
    }

    @Test
    @DisplayName("受講生の登録が行えること")
    void registerStudent_test(){
        Student student = createStudent();
        sut.registerStudent(student);
        List<Student> actual = sut.search();
        assertThat(actual).hasSize(6);
    }

    @Test
    @DisplayName("受講コース情報の登録が行えること")
    void registerStudentCourse_test(){
        StudentCourse studentCourse = createStudentCourse();
        sut.registerStudentCourse(studentCourse);
        List<StudentCourse> actual = sut.searchStudentCourseList();
        assertThat(actual).hasSize(9);
    }

    @Test
    @DisplayName("申込状況の登録が行えること")
    void registerStudentCourseStatus_test(){
        StudentCourseStatus studentCourseStatus = createStudentCourseStatus();
        sut.registerStudentCourseStatus(studentCourseStatus);
        List<StudentCourseStatus> actual = sut.searchStatusList();
        assertThat(actual).hasSize(9);
    }

    @Test
    @DisplayName("受講生の更新が行えること")
    void updateStudent_test(){
        Integer id = 1;
        Student expected = createStudent();
        expected.setId(id);
        sut.updateStudent(expected);

        Student actual = sut.searchStudent(id);
        assertStudent(actual,expected);
    }

    @Test
    @DisplayName("受講生コース情報の更新が行えること")
    void updateStudentCourse_test(){
        Integer id =1;
        Integer studentId = 1;
        StudentCourse expected = createStudentCourse();
        expected.setId(id);
        expected.setStudentId(studentId);

        sut.updateStudentCourse(expected);
        List<StudentCourse> actual = sut.searchStudentCourse(studentId);

        assertThat(actual.get(0).getCourseName()).isEqualTo(expected.getCourseName());
        assertThat(actual).hasSize(3);
    }

    @Test
    @DisplayName("申込状況の更新が行えること")
    void updateStudentCourseStatus_test(){
        StudentCourseStatus expected = new StudentCourseStatus(1,1,"本申込");
        sut.updateStudentCourseStatus(expected);
        StudentCourseStatus actual = sut.searchStudentCourseStatus(expected.getStudentCourseId());

        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getStudentCourseId()).isEqualTo(expected.getStudentCourseId());
        assertThat(actual.getStatus()).isEqualTo(expected.getStatus());

    }

    @Test
    @DisplayName("受講生の複数条件で検索する際何も指定しなかった時受講生一覧が返る")
    void searchStudentMultipleCondition_conditionIsEmpty_returnList(){
        StudentSearchCondition studentSearchCondition = new StudentSearchCondition();
        List<Student> actual = sut.searchStudentMultipleCondition(studentSearchCondition);

        assertThat(actual).hasSize(5);
    }

    @Test
    @DisplayName("受講生の複数条件を指定して検索ができる")
    void searchStudentMultipleCondition_setMultipleCondition_returnList(){
        StudentSearchCondition studentSearchCondition = new StudentSearchCondition();
        studentSearchCondition.setName("中村悠斗");
        studentSearchCondition.setKanaName("ナカムラユウト");
        studentSearchCondition.setNickname("ユウト");
        studentSearchCondition.setEmail("yuto.nakamura@example.com");
        studentSearchCondition.setArea("神奈川");
        studentSearchCondition.setAge(26);
        studentSearchCondition.setSex("男性");

        List<Student> actual = sut.searchStudentMultipleCondition(studentSearchCondition);

        assertThat(actual).hasSize(1);
        Student actualStudent = actual.get(0);
        assertThat(actualStudent.getId()).isEqualTo(1);
        assertThat(actualStudent.getName()).isEqualTo("中村悠斗");
        assertThat(actualStudent.getKanaName()).isEqualTo("ナカムラユウト");
        assertThat(actualStudent.getNickname()).isEqualTo("ユウト");
        assertThat(actualStudent.getEmail()).isEqualTo("yuto.nakamura@example.com");
        assertThat(actualStudent.getArea()).isEqualTo("神奈川");
        assertThat(actualStudent.getAge()).isEqualTo(26);
        assertThat(actualStudent.getSex()).isEqualTo("男性");
    }

    @Test
    @DisplayName("受講生の複数条件を指定して検索する際部分検索ができる")
    void searchStudentMultipleCondition_partialSearch_returnList(){
        StudentSearchCondition studentSearchCondition = new StudentSearchCondition();
        studentSearchCondition.setName("中村");
        studentSearchCondition.setKanaName("ユウト");
        studentSearchCondition.setNickname("ユ");
        studentSearchCondition.setEmail("nakamura@example.com");
        studentSearchCondition.setArea("奈川");

        List<Student> actual = sut.searchStudentMultipleCondition(studentSearchCondition);

        assertThat(actual).hasSize(1);
        Student actualStudent = actual.get(0);
        assertThat(actualStudent.getId()).isEqualTo(1);
        assertThat(actualStudent.getName()).isEqualTo("中村悠斗");
        assertThat(actualStudent.getKanaName()).isEqualTo("ナカムラユウト");
        assertThat(actualStudent.getNickname()).isEqualTo("ユウト");
        assertThat(actualStudent.getEmail()).isEqualTo("yuto.nakamura@example.com");
        assertThat(actualStudent.getArea()).isEqualTo("神奈川");
        assertThat(actualStudent.getAge()).isEqualTo(26);
        assertThat(actualStudent.getSex()).isEqualTo("男性");
    }

    @Test
    @DisplayName("存在しない受講生の複数条件を指定して検索ができる")
    void searchStudentMultipleCondition_doesNotExist_returnList(){
        StudentSearchCondition studentSearchCondition = new StudentSearchCondition();
        studentSearchCondition.setName("テスト太郎");
        studentSearchCondition.setKanaName("テストタロウ");
        studentSearchCondition.setNickname("テスタロウ");
        studentSearchCondition.setEmail("test@example.com");
        studentSearchCondition.setArea("テスト");
        studentSearchCondition.setAge(20);
        studentSearchCondition.setSex("男性");

        List<Student> actual = sut.searchStudentMultipleCondition(studentSearchCondition);

        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("受講生を年齢で範囲を指定して検索ができる")
    void searchStudentMultipleCondition_setMaxAgeAndMinAge_returnList(){
        StudentSearchCondition studentSearchCondition = new StudentSearchCondition();
        studentSearchCondition.setMaxAge(30);
        studentSearchCondition.setMinAge(20);

        List<Student> actual = sut.searchStudentMultipleCondition(studentSearchCondition);

        assertThat(actual).hasSize(4);
    }


    /**
     * レポジトリのテストで使用する受講生情報を生成するメソッド
     * @return Student
     */
    private static Student createStudent(){
        Student student = new Student();
        student.setName("テスト太郎");
        student.setKanaName("テストタロウ");
        student.setNickname("テスタロウ");
        student.setEmail("test@example.jp");
        student.setArea("テスト地区");
        student.setAge(20);
        student.setSex("男性");
        student.setRemark("テストテストテスト");
        student.setDeleted(false);
        return student;
    }

    /**
     * レポジトリのテストに使用する受講コース情報を生成するメソッド
     * @return StudentCourse
     */
    private static StudentCourse createStudentCourse(){
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setStudentId(1);
        studentCourse.setCourseName("テストコース");
        studentCourse.setCourseStartAt(stringToLocalDateTime("2026-04-01 10:00:00"));
        studentCourse.setCourseEndAt(stringToLocalDateTime("2026-10-01 18:00:00"));
        return studentCourse;
    }

    /**
     * テストに使用する申込状況を生成するメソッド
     * @return StudentCourseStatus
     */
    private static StudentCourseStatus createStudentCourseStatus(){
        StudentCourseStatus studentCourseStatus = new StudentCourseStatus();
        studentCourseStatus.setStudentCourseId(9);
        studentCourseStatus.setStatus("仮申込");
        return studentCourseStatus;
    }

    /**
     * ２つの受講生情報をassertThatを使用して比較するメソッド
     * @param actual 実際にRepositoryから取得した受講生情報
     * @param expected 想定されている受講生情報
     */
    private static void assertStudent(Student actual,Student expected){
        assertThat(expected.getId()).isEqualTo(actual.getId());
        assertThat(expected.getName()).isEqualTo(actual.getName());
        assertThat(expected.getKanaName()).isEqualTo(actual.getKanaName());
        assertThat(expected.getNickname()).isEqualTo(actual.getNickname());
        assertThat(expected.getEmail()).isEqualTo(actual.getEmail());
        assertThat(expected.getArea()).isEqualTo(actual.getArea());
        assertThat(expected.getAge()).isEqualTo(actual.getAge());
        assertThat(expected.getSex()).isEqualTo(actual.getSex());
        assertThat(expected.getRemark()).isEqualTo(actual.getRemark());
        assertThat(expected.isDeleted()).isEqualTo(actual.isDeleted());
    }

    /**
     * 受講コース情報をassertThatで比較するメソッド
     * @param actual 実際にRepositoryから取得した受講生コース情報
     * @param expected 想定されている受講生コース情報
     */
    private static void assertStudentCourse(StudentCourse actual, StudentCourse expected){
        assertThat(expected.getId()).isEqualTo(actual.getId());
        assertThat(expected.getStudentId()).isEqualTo(actual.getStudentId());
        assertThat(expected.getCourseName()).isEqualTo(actual.getCourseName());
        assertThat(expected.getCourseStartAt()).isEqualTo(actual.getCourseStartAt());
        assertThat(expected.getCourseEndAt()).isEqualTo(actual.getCourseEndAt());
    }

    /**
     * 文字列の時間をLocalDatetimeに変換するメソッド
     * @param time yyyy-MM-dd HH:mm:ssがフォーマットな時間が指定された文字列
     * @return LocalDateTime
     */
    private static LocalDateTime stringToLocalDateTime(String time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(time,formatter);
    }

}