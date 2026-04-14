package raisetech.student.management.controller.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StudentConverterTest {

    private StudentConverter converter;

    @BeforeEach
    void before() {
        converter = new StudentConverter();
    }

    @Test
    @DisplayName("空の受講生一覧と空の受講生コース一覧なら空の受講詳細一覧を返す")
    void converterStudentDetails_bothListsEmpty_returnEmpty() {
        List<Student> studentList = new ArrayList<>();
        List<StudentCourse> studentCourseList = new ArrayList<>();

        List<StudentDetail> actual = converter.convertStudentDetails(studentList, studentCourseList);

        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("受講生idで紐づいた受講詳細情報のリストが返ってくること")
    void converterStudentDetails_matchId_returnList() {
        //事前準備
        List<Student> studentList = new ArrayList<>();
        Student student1 = new Student();
        student1.setId("1");
        student1.setName("テスト太郎");
        studentList.add(student1);

        Student student2 = new Student();
        student2.setId("2");
        student2.setName("テスト次郎");
        studentList.add(student2);

        List<StudentCourse> studentCourseList = new ArrayList<>();

        StudentCourse studentCourse1 = new StudentCourse();
        studentCourse1.setStudentId("1");
        studentCourse1.setId("1");
        studentCourse1.setCourseName("テストコース");
        studentCourseList.add(studentCourse1);
        StudentCourse studentCourse2 = new StudentCourse();
        studentCourse2.setStudentId("2");
        studentCourse2.setId("2");
        studentCourse2.setCourseName("テストJavaコース");
        studentCourseList.add(studentCourse2);
        StudentCourse studentCourse3 = new StudentCourse();
        studentCourse3.setStudentId("1");
        studentCourse3.setId("3");
        studentCourse3.setCourseName("テストAIコース");
        studentCourseList.add(studentCourse3);
        //1人目の受講生詳細
        List<StudentCourse> student1CourseList = new ArrayList<>();
        student1CourseList.add(studentCourse1);
        student1CourseList.add(studentCourse3);
        StudentDetail studentDetail1 = new StudentDetail(student1, student1CourseList);
        //2人目の受講生詳細
        List<StudentCourse> student2CourseList = new ArrayList<>();
        student2CourseList.add(studentCourse2);
        StudentDetail studentDetail2 = new StudentDetail(student2, student2CourseList);
        //期待する値
        List<StudentDetail> expected = List.of(studentDetail1, studentDetail2);
        //実行
        List<StudentDetail> actual = converter.convertStudentDetails(studentList, studentCourseList);
        //検証
        assertThat(actual).hasSize(expected.size());
        //1件目の検証
        assertThat(actual.get(0).getStudent().getId()).isEqualTo(studentDetail1.getStudent().getId());
        assertThat(actual.get(0).getStudent().getName()).isEqualTo(studentDetail1.getStudent().getName());
        assertThat(actual.get(0).getStudentCourseList().size()).isEqualTo(studentDetail1.getStudentCourseList().size());
        assertThat(actual.get(0).getStudentCourseList().get(0).getId()).isEqualTo(studentDetail1.getStudentCourseList().get(0).getId());
        assertThat(actual.get(0).getStudentCourseList().get(0).getStudentId()).isEqualTo(studentDetail1.getStudentCourseList().get(0).getStudentId());
        assertThat(actual.get(0).getStudentCourseList().get(0).getCourseName()).isEqualTo(studentDetail1.getStudentCourseList().get(0).getCourseName());
        assertThat(actual.get(0).getStudentCourseList().get(1).getId()).isEqualTo(studentDetail1.getStudentCourseList().get(1).getId());
        assertThat(actual.get(0).getStudentCourseList().get(1).getStudentId()).isEqualTo(studentDetail1.getStudentCourseList().get(1).getStudentId());
        assertThat(actual.get(0).getStudentCourseList().get(1).getCourseName()).isEqualTo(studentDetail1.getStudentCourseList().get(1).getCourseName());
        //2件目の検証
        assertThat(actual.get(1).getStudent().getId()).isEqualTo(studentDetail2.getStudent().getId());
        assertThat(actual.get(1).getStudent().getName()).isEqualTo(studentDetail2.getStudent().getName());
        assertThat(actual.get(1).getStudentCourseList().size()).isEqualTo(studentDetail2.getStudentCourseList().size());
        assertThat(actual.get(1).getStudentCourseList().get(0).getId()).isEqualTo(studentDetail2.getStudentCourseList().get(0).getId());
        assertThat(actual.get(1).getStudentCourseList().get(0).getStudentId()).isEqualTo(studentDetail2.getStudentCourseList().get(0).getStudentId());
        assertThat(actual.get(1).getStudentCourseList().get(0).getCourseName()).isEqualTo(studentDetail2.getStudentCourseList().get(0).getCourseName());
    }

    @Test
    @DisplayName("受講生コース一覧がnullの時NullPointerExceptionが発生すること")
    void converterStudentDetails_courseNull_throwException() {
        //事前準備
        List<Student> studentList = new ArrayList<>();
        Student student1 = new Student();
        student1.setId("1");
        student1.setName("テスト太郎");
        studentList.add(student1);

        Student student2 = new Student();
        student2.setId("2");
        student2.setName("テスト次郎");
        studentList.add(student2);

        List<StudentCourse> studentCourseList = null;

        //実行
        assertThatThrownBy(
                () -> converter.convertStudentDetails(studentList, studentCourseList)
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Nullな受講コースが混在している時NullPointerExceptionが発生すること")
    void converterStudentDetails_mixNullCourse_throwException() {
        //事前準備
        List<Student> studentList = new ArrayList<>();
        Student student1 = new Student();
        student1.setId("1");
        student1.setName("テスト太郎");
        studentList.add(student1);

        Student student2 = new Student();
        student2.setId("2");
        student2.setName("テスト次郎");
        studentList.add(student2);

        List<StudentCourse> studentCourseList = new ArrayList<>();

        StudentCourse studentCourse1 = new StudentCourse();
        studentCourse1.setStudentId("1");
        studentCourse1.setId("1");
        studentCourse1.setCourseName("テストコース");
        studentCourseList.add(studentCourse1);
        StudentCourse studentCourse2 = null;
        studentCourseList.add(studentCourse2);
        StudentCourse studentCourse3 = new StudentCourse();
        studentCourse3.setStudentId("1");
        studentCourse3.setId("3");
        studentCourse3.setCourseName("テストAIコース");
        studentCourseList.add(studentCourse3);
        //検証
        assertThatThrownBy(
                () -> converter.convertStudentDetails(studentList, studentCourseList)
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("受講コース情報のstudentIdフィールドの一部がNullでも正常に動作すること")
    void converterStudentDetails_mixCourseStudentIdNull_returnList() {
        //事前準備
        List<Student> studentList = new ArrayList<>();
        Student student1 = new Student();
        student1.setId("1");
        student1.setName("テスト太郎");
        studentList.add(student1);

        Student student2 = new Student();
        student2.setId("2");
        student2.setName("テスト次郎");
        studentList.add(student2);

        List<StudentCourse> studentCourseList = new ArrayList<>();

        StudentCourse studentCourse1 = new StudentCourse();
        studentCourse1.setStudentId(null);
        studentCourse1.setId("1");
        studentCourse1.setCourseName("テストコース");
        studentCourseList.add(studentCourse1);
        StudentCourse studentCourse2 = new StudentCourse();
        studentCourse2.setStudentId("2");
        studentCourse2.setId("2");
        studentCourse2.setCourseName("テストJavaコース");
        studentCourseList.add(studentCourse2);
        StudentCourse studentCourse3 = new StudentCourse();
        studentCourse3.setStudentId("1");
        studentCourse3.setId("3");
        studentCourse3.setCourseName("テストAIコース");
        studentCourseList.add(studentCourse3);
        //1人目の受講生詳細
        List<StudentCourse> student1CourseList = new ArrayList<>();
        student1CourseList.add(studentCourse3);
        StudentDetail studentDetail1 = new StudentDetail(student1, student1CourseList);
        //2人目の受講生詳細
        List<StudentCourse> student2CourseList = new ArrayList<>();
        student2CourseList.add(studentCourse2);
        StudentDetail studentDetail2 = new StudentDetail(student2, student2CourseList);
        //期待する値
        List<StudentDetail> expected = List.of(studentDetail1, studentDetail2);
        //実行
        List<StudentDetail> actual = converter.convertStudentDetails(studentList, studentCourseList);
        //検証
        assertThat(actual).hasSize(expected.size());
        //1件目の検証
        assertThat(actual.get(0).getStudent().getId()).isEqualTo(studentDetail1.getStudent().getId());
        assertThat(actual.get(0).getStudent().getName()).isEqualTo(studentDetail1.getStudent().getName());
        assertThat(actual.get(0).getStudentCourseList().size()).isEqualTo(studentDetail1.getStudentCourseList().size());
        assertThat(actual.get(0).getStudentCourseList().get(0).getId()).isEqualTo(studentDetail1.getStudentCourseList().get(0).getId());
        assertThat(actual.get(0).getStudentCourseList().get(0).getStudentId()).isEqualTo(studentDetail1.getStudentCourseList().get(0).getStudentId());
        assertThat(actual.get(0).getStudentCourseList().get(0).getCourseName()).isEqualTo(studentDetail1.getStudentCourseList().get(0).getCourseName());
        //2件目の検証
        assertThat(actual.get(1).getStudent().getId()).isEqualTo(studentDetail2.getStudent().getId());
        assertThat(actual.get(1).getStudent().getName()).isEqualTo(studentDetail2.getStudent().getName());
        assertThat(actual.get(1).getStudentCourseList().size()).isEqualTo(studentDetail2.getStudentCourseList().size());
        assertThat(actual.get(1).getStudentCourseList().get(0).getId()).isEqualTo(studentDetail2.getStudentCourseList().get(0).getId());
        assertThat(actual.get(1).getStudentCourseList().get(0).getStudentId()).isEqualTo(studentDetail2.getStudentCourseList().get(0).getStudentId());
        assertThat(actual.get(1).getStudentCourseList().get(0).getCourseName()).isEqualTo(studentDetail2.getStudentCourseList().get(0).getCourseName());
    }

    @Test
    @DisplayName("受講コース情報のidフィールドの一部がNullでも正常に動作すること")
    void converterStudentDetails_mixCourseIdNull_returnList() {
        //事前準備
        List<Student> studentList = new ArrayList<>();
        Student student1 = new Student();
        student1.setId("1");
        student1.setName("テスト太郎");
        studentList.add(student1);

        Student student2 = new Student();
        student2.setId("2");
        student2.setName("テスト次郎");
        studentList.add(student2);

        List<StudentCourse> studentCourseList = new ArrayList<>();

        StudentCourse studentCourse1 = new StudentCourse();
        studentCourse1.setStudentId("1");
        studentCourse1.setId(null);
        studentCourse1.setCourseName("テストコース");
        studentCourseList.add(studentCourse1);
        StudentCourse studentCourse2 = new StudentCourse();
        studentCourse2.setStudentId("2");
        studentCourse2.setId("2");
        studentCourse2.setCourseName("テストJavaコース");
        studentCourseList.add(studentCourse2);
        StudentCourse studentCourse3 = new StudentCourse();
        studentCourse3.setStudentId("1");
        studentCourse3.setId("3");
        studentCourse3.setCourseName("テストAIコース");
        studentCourseList.add(studentCourse3);
        //1人目の受講生詳細
        List<StudentCourse> student1CourseList = new ArrayList<>();
        student1CourseList.add(studentCourse1);
        student1CourseList.add(studentCourse3);
        StudentDetail studentDetail1 = new StudentDetail(student1, student1CourseList);
        //2人目の受講生詳細
        List<StudentCourse> student2CourseList = new ArrayList<>();
        student2CourseList.add(studentCourse2);
        StudentDetail studentDetail2 = new StudentDetail(student2, student2CourseList);
        //期待する値
        List<StudentDetail> expected = List.of(studentDetail1, studentDetail2);
        //実行
        List<StudentDetail> actual = converter.convertStudentDetails(studentList, studentCourseList);
        //検証
        assertThat(actual).hasSize(expected.size());
        //1件目の検証
        assertThat(actual.get(0).getStudent().getId()).isEqualTo(studentDetail1.getStudent().getId());
        assertThat(actual.get(0).getStudent().getName()).isEqualTo(studentDetail1.getStudent().getName());
        assertThat(actual.get(0).getStudentCourseList().size()).isEqualTo(studentDetail1.getStudentCourseList().size());
        assertThat(actual.get(0).getStudentCourseList().get(0).getId()).isEqualTo(studentDetail1.getStudentCourseList().get(0).getId());
        assertThat(actual.get(0).getStudentCourseList().get(0).getStudentId()).isEqualTo(studentDetail1.getStudentCourseList().get(0).getStudentId());
        assertThat(actual.get(0).getStudentCourseList().get(0).getCourseName()).isEqualTo(studentDetail1.getStudentCourseList().get(0).getCourseName());
        //2件目の検証
        assertThat(actual.get(1).getStudent().getId()).isEqualTo(studentDetail2.getStudent().getId());
        assertThat(actual.get(1).getStudent().getName()).isEqualTo(studentDetail2.getStudent().getName());
        assertThat(actual.get(1).getStudentCourseList().size()).isEqualTo(studentDetail2.getStudentCourseList().size());
        assertThat(actual.get(1).getStudentCourseList().get(0).getId()).isEqualTo(studentDetail2.getStudentCourseList().get(0).getId());
        assertThat(actual.get(1).getStudentCourseList().get(0).getStudentId()).isEqualTo(studentDetail2.getStudentCourseList().get(0).getStudentId());
        assertThat(actual.get(1).getStudentCourseList().get(0).getCourseName()).isEqualTo(studentDetail2.getStudentCourseList().get(0).getCourseName());
    }

    @Test
    @DisplayName("受講生一覧がnullの時NullPointerExceptionが発生すること")
    void converterStudentDetails_studentListNull_throwException() {
        //事前準備
        List<Student> studentList = null;

        List<StudentCourse> studentCourseList = new ArrayList<>();

        StudentCourse studentCourse1 = new StudentCourse();
        studentCourse1.setStudentId("1");
        studentCourse1.setId("1");
        studentCourse1.setCourseName("テストコース");
        studentCourseList.add(studentCourse1);
        StudentCourse studentCourse2 = new StudentCourse();
        studentCourse2.setStudentId("2");
        studentCourse2.setId("2");
        studentCourse2.setCourseName("テストJavaコース");
        studentCourseList.add(studentCourse2);
        StudentCourse studentCourse3 = new StudentCourse();
        studentCourse3.setStudentId("1");
        studentCourse3.setId("3");
        studentCourse3.setCourseName("テストAIコース");
        studentCourseList.add(studentCourse3);

        //実行
        assertThatThrownBy(
                () -> converter.convertStudentDetails(studentList, studentCourseList)
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Nullな受講生が混在している時NullPointerExceptionが発生すること")
    void converterStudentDetails_mixNullStudent_throwException() {
        //事前準備
        List<Student> studentList = new ArrayList<>();
        Student student1 = new Student();
        student1.setId("1");
        student1.setName("テスト太郎");
        studentList.add(student1);

        Student student2 = null;
        studentList.add(student2);

        List<StudentCourse> studentCourseList = new ArrayList<>();

        StudentCourse studentCourse1 = new StudentCourse();
        studentCourse1.setStudentId("1");
        studentCourse1.setId("1");
        studentCourse1.setCourseName("テストコース");
        studentCourseList.add(studentCourse1);
        StudentCourse studentCourse2 = new StudentCourse();
        studentCourse2.setStudentId("2");
        studentCourse2.setId("2");
        studentCourse2.setCourseName("テストJavaコース");
        studentCourseList.add(studentCourse2);
        StudentCourse studentCourse3 = new StudentCourse();
        studentCourse3.setStudentId("1");
        studentCourse3.setId("3");
        studentCourse3.setCourseName("テストAIコース");
        studentCourseList.add(studentCourse3);
        //検証
        assertThatThrownBy(
                () -> converter.convertStudentDetails(studentList, studentCourseList)
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("受講生のidフィールドの一部がNullの時NullPointerExceptionが発生すること")
    void converterStudentDetails_mixStudentIdNull_throwException() {
        //事前準備
        List<Student> studentList = new ArrayList<>();
        Student student1 = new Student();
        student1.setId(null);
        student1.setName("テスト太郎");
        studentList.add(student1);

        Student student2 = new Student();
        student2.setId("2");
        student2.setName("テスト次郎");
        studentList.add(student2);

        List<StudentCourse> studentCourseList = new ArrayList<>();

        StudentCourse studentCourse1 = new StudentCourse();
        studentCourse1.setStudentId("1");
        studentCourse1.setId("1");
        studentCourse1.setCourseName("テストコース");
        studentCourseList.add(studentCourse1);
        StudentCourse studentCourse2 = new StudentCourse();
        studentCourse2.setStudentId("2");
        studentCourse2.setId("2");
        studentCourse2.setCourseName("テストJavaコース");
        studentCourseList.add(studentCourse2);
        StudentCourse studentCourse3 = new StudentCourse();
        studentCourse3.setStudentId("1");
        studentCourse3.setId("3");
        studentCourse3.setCourseName("テストAIコース");
        studentCourseList.add(studentCourse3);
        //検証
        assertThatThrownBy(
                () -> converter.convertStudentDetails(studentList, studentCourseList)
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("受講生一覧と受講コース一覧がnullの時NullPointerExceptionが発生すること")
    void converterStudentDetails_bothListsNull_throwException() {
        //事前準備
        List<Student> studentList = null;
        List<StudentCourse> studentCourseList = null;

        //実行
        assertThatThrownBy(
                () -> converter.convertStudentDetails(studentList, studentCourseList)
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("空の受講コース一覧を渡すと空の受講コース情報一覧が入った受講詳細情報のリストが返ってくること")
    void converterStudentDetails_courseIsEmpty_returnListEmptyCourse() {
        //事前準備
        List<Student> studentList = new ArrayList<>();
        Student student1 = new Student();
        student1.setId("1");
        student1.setName("テスト太郎");
        studentList.add(student1);

        Student student2 = new Student();
        student2.setId("2");
        student2.setName("テスト次郎");
        studentList.add(student2);

        List<StudentCourse> studentCourseList = new ArrayList<>();

        //1人目の受講生詳細
        List<StudentCourse> student1CourseList = new ArrayList<>();
        StudentDetail studentDetail1 = new StudentDetail(student1, student1CourseList);
        //2人目の受講生詳細
        List<StudentCourse> student2CourseList = new ArrayList<>();
        StudentDetail studentDetail2 = new StudentDetail(student2, student2CourseList);
        //期待する値
        List<StudentDetail> expected = List.of(studentDetail1, studentDetail2);
        //実行
        List<StudentDetail> actual = converter.convertStudentDetails(studentList, studentCourseList);
        //検証
        assertThat(actual).hasSize(expected.size());
        assertThat(actual.get(0).getStudent().getId()).isEqualTo(expected.get(0).getStudent().getId());
        assertThat(actual.get(0).getStudent().getName()).isEqualTo(expected.get(0).getStudent().getName());
        assertThat(actual.get(1).getStudent().getId()).isEqualTo(expected.get(1).getStudent().getId());
        assertThat(actual.get(1).getStudent().getName()).isEqualTo(expected.get(1).getStudent().getName());

        assertThat(actual.get(0).getStudentCourseList()).isEmpty();
        assertThat(actual.get(1).getStudentCourseList()).isEmpty();

    }

    @Test
    @DisplayName("紐づく受講コースがない受講生一覧なら空のコース一覧を持つ受講詳細一覧を返す")
    void converterStudentDetails_noMatchCourse_returnListEmptyCourse() {
        //事前準備
        List<Student> studentList = new ArrayList<>();
        Student student1 = new Student();
        student1.setId("999");
        student1.setName("テスト太郎");
        studentList.add(student1);

        Student student2 = new Student();
        student2.setId("998");
        student2.setName("テスト次郎");
        studentList.add(student2);

        List<StudentCourse> studentCourseList = new ArrayList<>();

        StudentCourse studentCourse1 = new StudentCourse();
        studentCourse1.setStudentId("1");
        studentCourse1.setId("1");
        studentCourse1.setCourseName("テストコース");
        studentCourseList.add(studentCourse1);
        StudentCourse studentCourse2 = new StudentCourse();
        studentCourse2.setStudentId("2");
        studentCourse2.setId("2");
        studentCourse2.setCourseName("テストJavaコース");
        studentCourseList.add(studentCourse2);
        StudentCourse studentCourse3 = new StudentCourse();
        studentCourse3.setStudentId("1");
        studentCourse3.setId("3");
        studentCourse3.setCourseName("テストAIコース");
        studentCourseList.add(studentCourse3);
        //1人目の受講生詳細
        List<StudentCourse> student1CourseList = new ArrayList<>();
        StudentDetail studentDetail1 = new StudentDetail(student1, student1CourseList);
        //2人目の受講生詳細
        List<StudentCourse> student2CourseList = new ArrayList<>();
        StudentDetail studentDetail2 = new StudentDetail(student2, student2CourseList);
        //期待する値
        List<StudentDetail> expected = List.of(studentDetail1, studentDetail2);
        //実行
        List<StudentDetail> actual = converter.convertStudentDetails(studentList, studentCourseList);
        //検証
        assertThat(actual).hasSize(expected.size());
        assertThat(actual.get(0).getStudent().getId()).isEqualTo(expected.get(0).getStudent().getId());
        assertThat(actual.get(0).getStudent().getName()).isEqualTo(expected.get(0).getStudent().getName());
        assertThat(actual.get(1).getStudent().getId()).isEqualTo(expected.get(1).getStudent().getId());
        assertThat(actual.get(1).getStudent().getName()).isEqualTo(expected.get(1).getStudent().getName());
        assertThat(actual.get(0).getStudentCourseList()).isEmpty();
        assertThat(actual.get(1).getStudentCourseList()).isEmpty();

    }

    @Test
    @DisplayName("空の受講生と空の受講生コース一覧を渡すと空の受講詳細情報が返ってくること")
    void converterStudentDetail_bothEmpty_returnEmpty() {
        Student student = new Student();
        List<StudentCourse> studentCourseList = new ArrayList<>();

        StudentDetail actual = converter.convertStudentDetail(student, studentCourseList);

        assertThat(actual).isNotNull();
        assertThat(actual.getStudent().getId()).isNull();
        assertThat(actual.getStudent().getName()).isNull();
        assertThat(actual.getStudentCourseList()).isEmpty();
    }

    @Test
    @DisplayName("受講生idで紐づいた受講詳細情報単体が返ってくること")
    void converterStudentDetail_match_returnDetail() {
        //事前準備
        Student student = new Student();
        student.setId("1");
        student.setName("テスト太郎");

        List<StudentCourse> studentCourseList = new ArrayList<>();

        StudentCourse studentCourse1 = new StudentCourse();
        studentCourse1.setStudentId("1");
        studentCourse1.setId("1");
        studentCourse1.setCourseName("テストコース");
        studentCourseList.add(studentCourse1);
        StudentCourse studentCourse2 = new StudentCourse();
        studentCourse2.setStudentId("2");
        studentCourse2.setId("2");
        studentCourse2.setCourseName("テストJavaコース");
        studentCourseList.add(studentCourse2);
        StudentCourse studentCourse3 = new StudentCourse();
        studentCourse3.setStudentId("1");
        studentCourse3.setId("3");
        studentCourse3.setCourseName("テストAIコース");
        studentCourseList.add(studentCourse3);

        List<StudentCourse> expectedStudentCourseList = List.of(studentCourse1, studentCourse3);

        StudentDetail expected = new StudentDetail(student, expectedStudentCourseList);
        //実行
        StudentDetail actual = converter.convertStudentDetail(student, studentCourseList);
        //検証
        assertThat(actual.getStudentCourseList().size()).isEqualTo(expected.getStudentCourseList().size());
        assertThat(actual.getStudent().getId()).isEqualTo(expected.getStudent().getId());
        assertThat(actual.getStudent().getName()).isEqualTo(expected.getStudent().getName());
        assertThat(actual.getStudentCourseList().get(0).getId()).isEqualTo(expected.getStudentCourseList().get(0).getId());
        assertThat(actual.getStudentCourseList().get(0).getStudentId()).isEqualTo(expected.getStudentCourseList().get(0).getStudentId());
        assertThat(actual.getStudentCourseList().get(0).getCourseName()).isEqualTo(expected.getStudentCourseList().get(0).getCourseName());
        assertThat(actual.getStudentCourseList().get(1).getId()).isEqualTo(expected.getStudentCourseList().get(1).getId());
        assertThat(actual.getStudentCourseList().get(1).getStudentId()).isEqualTo(expected.getStudentCourseList().get(1).getStudentId());
        assertThat(actual.getStudentCourseList().get(1).getCourseName()).isEqualTo(expected.getStudentCourseList().get(1).getCourseName());
    }

    @Test
    @DisplayName("受講生がnullな時NullPointerExceptionが発生すること")
    void converterStudentDetail_studentNull_throwException() {
        //事前準備
        Student student = null;

        List<StudentCourse> studentCourseList = new ArrayList<>();

        StudentCourse studentCourse1 = new StudentCourse();
        studentCourse1.setStudentId("1");
        studentCourse1.setId("1");
        studentCourse1.setCourseName("テストコース");
        studentCourseList.add(studentCourse1);
        StudentCourse studentCourse2 = new StudentCourse();
        studentCourse2.setStudentId("2");
        studentCourse2.setId("2");
        studentCourse2.setCourseName("テストJavaコース");
        studentCourseList.add(studentCourse2);
        StudentCourse studentCourse3 = new StudentCourse();
        studentCourse3.setStudentId("1");
        studentCourse3.setId("3");
        studentCourse3.setCourseName("テストAIコース");
        studentCourseList.add(studentCourse3);

        assertThatThrownBy(
                () -> converter.convertStudentDetail(student,studentCourseList)
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("受講生のidがnullな時NullPointerExceptionが発生すること")
    void converterStudentDetail_studentIdNull_throwException() {
        //事前準備
        Student student = new Student();
        student.setId(null);
        student.setName("テスト太郎");

        List<StudentCourse> studentCourseList = new ArrayList<>();

        StudentCourse studentCourse1 = new StudentCourse();
        studentCourse1.setStudentId("1");
        studentCourse1.setId("1");
        studentCourse1.setCourseName("テストコース");
        studentCourseList.add(studentCourse1);
        StudentCourse studentCourse2 = new StudentCourse();
        studentCourse2.setStudentId("2");
        studentCourse2.setId("2");
        studentCourse2.setCourseName("テストJavaコース");
        studentCourseList.add(studentCourse2);
        StudentCourse studentCourse3 = new StudentCourse();
        studentCourse3.setStudentId("1");
        studentCourse3.setId("3");
        studentCourse3.setCourseName("テストAIコース");
        studentCourseList.add(studentCourse3);

        assertThatThrownBy(
                () -> converter.convertStudentDetail(student,studentCourseList)
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("受講コース情報一覧がnullな時NullPointerExceptionが発生すること")
    void converterStudentDetail_courseListNull_throwException() {
        //事前準備
        Student student = new Student();
        student.setId("1");
        student.setName("テスト太郎");

        List<StudentCourse> studentCourseList = null;

        assertThatThrownBy(
                () -> converter.convertStudentDetail(student,studentCourseList)
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("一部の受講コース情報のstudentIdがnullな時正常に動作する")
    void converterStudentDetail_mixCourseStudentIdNull_returnDetail() {
        //事前準備
        Student student = new Student();
        student.setId("1");
        student.setName("テスト太郎");

        List<StudentCourse> studentCourseList = new ArrayList<>();

        StudentCourse studentCourse1 = new StudentCourse();
        studentCourse1.setStudentId(null);
        studentCourse1.setId("1");
        studentCourse1.setCourseName("テストコース");
        studentCourseList.add(studentCourse1);
        StudentCourse studentCourse2 = new StudentCourse();
        studentCourse2.setStudentId("2");
        studentCourse2.setId("2");
        studentCourse2.setCourseName("テストJavaコース");
        studentCourseList.add(studentCourse2);
        StudentCourse studentCourse3 = new StudentCourse();
        studentCourse3.setStudentId("1");
        studentCourse3.setId("3");
        studentCourse3.setCourseName("テストAIコース");
        studentCourseList.add(studentCourse3);

        List<StudentCourse> expectedStudentCourseList = List.of(studentCourse3);

        StudentDetail expected = new StudentDetail(student, expectedStudentCourseList);
        //実行
        StudentDetail actual = converter.convertStudentDetail(student, studentCourseList);
        //検証
        assertThat(actual.getStudentCourseList().size()).isEqualTo(expected.getStudentCourseList().size());
        assertThat(actual.getStudent().getId()).isEqualTo(expected.getStudent().getId());
        assertThat(actual.getStudent().getName()).isEqualTo(expected.getStudent().getName());
        assertThat(actual.getStudentCourseList().get(0).getId()).isEqualTo(expected.getStudentCourseList().get(0).getId());
        assertThat(actual.getStudentCourseList().get(0).getStudentId()).isEqualTo(expected.getStudentCourseList().get(0).getStudentId());
        assertThat(actual.getStudentCourseList().get(0).getCourseName()).isEqualTo(expected.getStudentCourseList().get(0).getCourseName());
    }

    @Test
    @DisplayName("受講生と受講生コース一覧がnullな時NullPointerExceptionが発生すること")
    void converterStudentDetail_bothNull_throwException() {
        //事前準備
        Student student = null;
        List<StudentCourse> studentCourseList = null;

        //実行
        assertThatThrownBy(
                () -> converter.convertStudentDetail(student, studentCourseList)
        ).isInstanceOf(NullPointerException.class);

    }

    @Test
    @DisplayName("受講生と空の受講生コース一覧を渡すと空の受講コース情報が入った受講詳細一覧が返ってくること")
    void converterStudentDetail_courseListEmpty_returnDetailEmptyCourse() {
        //事前準備
        Student student = new Student();
        student.setId("1");
        student.setName("テスト太郎");

        List<StudentCourse> studentCourseList = new ArrayList<>();

        //期待する値
        StudentDetail expected = new StudentDetail(student, studentCourseList);
        //実行
        StudentDetail actual = converter.convertStudentDetail(student, studentCourseList);
        //検証
        assertThat(actual.getStudent().getId()).isEqualTo(expected.getStudent().getId());
        assertThat(actual.getStudent().getName()).isEqualTo(expected.getStudent().getName());
        assertThat(actual.getStudentCourseList()).isEmpty();
    }

    @Test
    @DisplayName("受講コース情報と紐づかない受講生IDがある受講生を渡すと空の受講コース情報が入った受講生詳細が返ってくること")
    void converterStudentDetail_noMatch_returnDetailEmptyCourse() {
        //事前準備
        Student student = new Student();
        student.setId("999");
        student.setName("テスト太郎");

        List<StudentCourse> studentCourseList = new ArrayList<>();

        StudentCourse studentCourse1 = new StudentCourse();
        studentCourse1.setStudentId("1");
        studentCourse1.setId("1");
        studentCourse1.setCourseName("テストコース");
        studentCourseList.add(studentCourse1);
        StudentCourse studentCourse2 = new StudentCourse();
        studentCourse2.setStudentId("2");
        studentCourse2.setId("2");
        studentCourse2.setCourseName("テストJavaコース");
        studentCourseList.add(studentCourse2);
        StudentCourse studentCourse3 = new StudentCourse();
        studentCourse3.setStudentId("1");
        studentCourse3.setId("3");
        studentCourse3.setCourseName("テストAIコース");
        studentCourseList.add(studentCourse3);

        List<StudentCourse> expectedStudentCourseList = new ArrayList<>();
        //期待する値
        StudentDetail expected = new StudentDetail(student, expectedStudentCourseList);
        //実行
        StudentDetail actual = converter.convertStudentDetail(student, studentCourseList);
        //検証
        assertThat(actual.getStudent().getId()).isEqualTo(expected.getStudent().getId());
        assertThat(actual.getStudent().getName()).isEqualTo(expected.getStudent().getName());
        assertThat(actual.getStudentCourseList()).isEmpty();
    }

}