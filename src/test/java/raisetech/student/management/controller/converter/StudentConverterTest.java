package raisetech.student.management.controller.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.StudentCourseStatus;
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
        student1.setId(1);
        student1.setName("テスト太郎");
        studentList.add(student1);

        Student student2 = new Student();
        student2.setId(2);
        student2.setName("テスト次郎");
        studentList.add(student2);

        List<StudentCourse> studentCourseList = new ArrayList<>();

        StudentCourse studentCourse1 = new StudentCourse();
        studentCourse1.setStudentId(1);
        studentCourse1.setId(1);
        studentCourse1.setCourseName("テストコース");
        studentCourseList.add(studentCourse1);
        StudentCourse studentCourse2 = new StudentCourse();
        studentCourse2.setStudentId(2);
        studentCourse2.setId(2);
        studentCourse2.setCourseName("テストJavaコース");
        studentCourseList.add(studentCourse2);
        StudentCourse studentCourse3 = new StudentCourse();
        studentCourse3.setStudentId(1);
        studentCourse3.setId(3);
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
        student1.setId(1);
        student1.setName("テスト太郎");
        studentList.add(student1);

        Student student2 = new Student();
        student2.setId(2);
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
        student1.setId(1);
        student1.setName("テスト太郎");
        studentList.add(student1);

        Student student2 = new Student();
        student2.setId(2);
        student2.setName("テスト次郎");
        studentList.add(student2);

        List<StudentCourse> studentCourseList = new ArrayList<>();

        StudentCourse studentCourse1 = new StudentCourse();
        studentCourse1.setStudentId(1);
        studentCourse1.setId(1);
        studentCourse1.setCourseName("テストコース");
        studentCourseList.add(studentCourse1);
        StudentCourse studentCourse2 = null;
        studentCourseList.add(studentCourse2);
        StudentCourse studentCourse3 = new StudentCourse();
        studentCourse3.setStudentId(1);
        studentCourse3.setId(3);
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
        student1.setId(1);
        student1.setName("テスト太郎");
        studentList.add(student1);

        Student student2 = new Student();
        student2.setId(2);
        student2.setName("テスト次郎");
        studentList.add(student2);

        List<StudentCourse> studentCourseList = new ArrayList<>();

        StudentCourse studentCourse1 = new StudentCourse();
        studentCourse1.setStudentId(null);
        studentCourse1.setId(1);
        studentCourse1.setCourseName("テストコース");
        studentCourseList.add(studentCourse1);
        StudentCourse studentCourse2 = new StudentCourse();
        studentCourse2.setStudentId(2);
        studentCourse2.setId(2);
        studentCourse2.setCourseName("テストJavaコース");
        studentCourseList.add(studentCourse2);
        StudentCourse studentCourse3 = new StudentCourse();
        studentCourse3.setStudentId(1);
        studentCourse3.setId(3);
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
        student1.setId(1);
        student1.setName("テスト太郎");
        studentList.add(student1);

        Student student2 = new Student();
        student2.setId(2);
        student2.setName("テスト次郎");
        studentList.add(student2);

        List<StudentCourse> studentCourseList = new ArrayList<>();

        StudentCourse studentCourse1 = new StudentCourse();
        studentCourse1.setStudentId(1);
        studentCourse1.setId(null);
        studentCourse1.setCourseName("テストコース");
        studentCourseList.add(studentCourse1);
        StudentCourse studentCourse2 = new StudentCourse();
        studentCourse2.setStudentId(2);
        studentCourse2.setId(2);
        studentCourse2.setCourseName("テストJavaコース");
        studentCourseList.add(studentCourse2);
        StudentCourse studentCourse3 = new StudentCourse();
        studentCourse3.setStudentId(1);
        studentCourse3.setId(3);
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
        studentCourse1.setStudentId(1);
        studentCourse1.setId(1);
        studentCourse1.setCourseName("テストコース");
        studentCourseList.add(studentCourse1);
        StudentCourse studentCourse2 = new StudentCourse();
        studentCourse2.setStudentId(2);
        studentCourse2.setId(2);
        studentCourse2.setCourseName("テストJavaコース");
        studentCourseList.add(studentCourse2);
        StudentCourse studentCourse3 = new StudentCourse();
        studentCourse3.setStudentId(1);
        studentCourse3.setId(3);
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
        student1.setId(1);
        student1.setName("テスト太郎");
        studentList.add(student1);

        Student student2 = null;
        studentList.add(student2);

        List<StudentCourse> studentCourseList = new ArrayList<>();

        StudentCourse studentCourse1 = new StudentCourse();
        studentCourse1.setStudentId(1);
        studentCourse1.setId(1);
        studentCourse1.setCourseName("テストコース");
        studentCourseList.add(studentCourse1);
        StudentCourse studentCourse2 = new StudentCourse();
        studentCourse2.setStudentId(2);
        studentCourse2.setId(2);
        studentCourse2.setCourseName("テストJavaコース");
        studentCourseList.add(studentCourse2);
        StudentCourse studentCourse3 = new StudentCourse();
        studentCourse3.setStudentId(1);
        studentCourse3.setId(3);
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
        student2.setId(2);
        student2.setName("テスト次郎");
        studentList.add(student2);

        List<StudentCourse> studentCourseList = new ArrayList<>();

        StudentCourse studentCourse1 = new StudentCourse();
        studentCourse1.setStudentId(1);
        studentCourse1.setId(1);
        studentCourse1.setCourseName("テストコース");
        studentCourseList.add(studentCourse1);
        StudentCourse studentCourse2 = new StudentCourse();
        studentCourse2.setStudentId(2);
        studentCourse2.setId(2);
        studentCourse2.setCourseName("テストJavaコース");
        studentCourseList.add(studentCourse2);
        StudentCourse studentCourse3 = new StudentCourse();
        studentCourse3.setStudentId(1);
        studentCourse3.setId(3);
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
        student1.setId(1);
        student1.setName("テスト太郎");
        studentList.add(student1);

        Student student2 = new Student();
        student2.setId(2);
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
        student1.setId(999);
        student1.setName("テスト太郎");
        studentList.add(student1);

        Student student2 = new Student();
        student2.setId(998);
        student2.setName("テスト次郎");
        studentList.add(student2);

        List<StudentCourse> studentCourseList = new ArrayList<>();

        StudentCourse studentCourse1 = new StudentCourse();
        studentCourse1.setStudentId(1);
        studentCourse1.setId(1);
        studentCourse1.setCourseName("テストコース");
        studentCourseList.add(studentCourse1);
        StudentCourse studentCourse2 = new StudentCourse();
        studentCourse2.setStudentId(2);
        studentCourse2.setId(2);
        studentCourse2.setCourseName("テストJavaコース");
        studentCourseList.add(studentCourse2);
        StudentCourse studentCourse3 = new StudentCourse();
        studentCourse3.setStudentId(1);
        studentCourse3.setId(3);
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
        student.setId(1);
        student.setName("テスト太郎");

        List<StudentCourse> studentCourseList = new ArrayList<>();

        StudentCourse studentCourse1 = new StudentCourse();
        studentCourse1.setStudentId(1);
        studentCourse1.setId(1);
        studentCourse1.setCourseName("テストコース");
        studentCourseList.add(studentCourse1);
        StudentCourse studentCourse2 = new StudentCourse();
        studentCourse2.setStudentId(2);
        studentCourse2.setId(2);
        studentCourse2.setCourseName("テストJavaコース");
        studentCourseList.add(studentCourse2);
        StudentCourse studentCourse3 = new StudentCourse();
        studentCourse3.setStudentId(1);
        studentCourse3.setId(3);
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
        studentCourse1.setStudentId(1);
        studentCourse1.setId(1);
        studentCourse1.setCourseName("テストコース");
        studentCourseList.add(studentCourse1);
        StudentCourse studentCourse2 = new StudentCourse();
        studentCourse2.setStudentId(2);
        studentCourse2.setId(2);
        studentCourse2.setCourseName("テストJavaコース");
        studentCourseList.add(studentCourse2);
        StudentCourse studentCourse3 = new StudentCourse();
        studentCourse3.setStudentId(1);
        studentCourse3.setId(3);
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
        studentCourse1.setStudentId(1);
        studentCourse1.setId(1);
        studentCourse1.setCourseName("テストコース");
        studentCourseList.add(studentCourse1);
        StudentCourse studentCourse2 = new StudentCourse();
        studentCourse2.setStudentId(2);
        studentCourse2.setId(2);
        studentCourse2.setCourseName("テストJavaコース");
        studentCourseList.add(studentCourse2);
        StudentCourse studentCourse3 = new StudentCourse();
        studentCourse3.setStudentId(1);
        studentCourse3.setId(3);
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
        student.setId(1);
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
        student.setId(1);
        student.setName("テスト太郎");

        List<StudentCourse> studentCourseList = new ArrayList<>();

        StudentCourse studentCourse1 = new StudentCourse();
        studentCourse1.setStudentId(null);
        studentCourse1.setId(1);
        studentCourse1.setCourseName("テストコース");
        studentCourseList.add(studentCourse1);
        StudentCourse studentCourse2 = new StudentCourse();
        studentCourse2.setStudentId(2);
        studentCourse2.setId(2);
        studentCourse2.setCourseName("テストJavaコース");
        studentCourseList.add(studentCourse2);
        StudentCourse studentCourse3 = new StudentCourse();
        studentCourse3.setStudentId(1);
        studentCourse3.setId(3);
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
        student.setId(1);
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
        student.setId(999);
        student.setName("テスト太郎");

        List<StudentCourse> studentCourseList = new ArrayList<>();

        StudentCourse studentCourse1 = new StudentCourse();
        studentCourse1.setStudentId(1);
        studentCourse1.setId(1);
        studentCourse1.setCourseName("テストコース");
        studentCourseList.add(studentCourse1);
        StudentCourse studentCourse2 = new StudentCourse();
        studentCourse2.setStudentId(2);
        studentCourse2.setId(2);
        studentCourse2.setCourseName("テストJavaコース");
        studentCourseList.add(studentCourse2);
        StudentCourse studentCourse3 = new StudentCourse();
        studentCourse3.setStudentId(1);
        studentCourse3.setId(3);
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

    @Test
    @DisplayName("申込状況セット前の空の受講生コース一覧と空の申込状況一覧なら空の受講生コース一覧を返す")
    void convertStudentCourseStatus_emptyList_returnEmptyList(){
        /**
         * 元のコード
         *     public List<StudentCourse> convertStudentCourseStatus(List<StudentCourse> studentCourseList, List<StudentCourseStatus> studentCourseStatusList) {
         *             studentCourseList.forEach(studentCourse -> {
         *                 studentCourseStatusList.stream()
         *                         .filter(studentCourseStatus -> studentCourseStatus.getStudentCourseId().equals(studentCourse.getId()))
         *                         .findFirst()
         *                         .ifPresent(studentCourse::setStudentCourseStatus);
         *             });
         *         return studentCourseList;
         */
        List<StudentCourse> studentCourseList = new ArrayList<>();
        List<StudentCourseStatus> studentCourseStatusList = new ArrayList<>();

        List<StudentCourse> actual = converter.convertStudentCourseStatus(studentCourseList,studentCourseStatusList);

        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("受講生コース一覧と申込状況一覧なら申込状況が紐づいた受講生コース一覧を返す")
    void convertStudentCourseStatus_setCourseAndStatus_returnList(){
        List<StudentCourse> studentCourseList = createStudentCourseList();
        List<StudentCourseStatus> studentCourseStatusList = createStudentCourseStatusList();

        List<StudentCourse> expected = createStudentCourseList();
        List<StudentCourseStatus> expectedStatus = createStudentCourseStatusList();
        expected.get(0).setStudentCourseStatus(expectedStatus.get(0));
        expected.get(1).setStudentCourseStatus(expectedStatus.get(1));
        expected.get(2).setStudentCourseStatus(expectedStatus.get(2));


        List<StudentCourse> actual = converter.convertStudentCourseStatus(studentCourseList,studentCourseStatusList);

        assertThat(actual).hasSize(expected.size());
        assertThatStudentCourse(actual.get(0),expected.get(0));
        assertThatStudentCourse(actual.get(1),expected.get(1));
        assertThatStudentCourse(actual.get(2),expected.get(2));
    }

    @Test
    @DisplayName("受講生コース一覧と一部だけ紐づく申込状況一覧なら一致したものだけ申込状況がセットされた受講生コース一覧を返す")
    void convertStudentCourseStatus_someStatusesMatch_returnOnlyMatchedStatuses(){
        List<StudentCourse> studentCourseList = createStudentCourseList();
        List<StudentCourseStatus> studentCourseStatusList = createStudentCourseStatusList();
        studentCourseStatusList.get(1).setStudentCourseId(999);

        List<StudentCourse> expected = createStudentCourseList();
        List<StudentCourseStatus> expectedStatus = createStudentCourseStatusList();
        expected.get(0).setStudentCourseStatus(expectedStatus.get(0));
        expected.get(2).setStudentCourseStatus(expectedStatus.get(2));


        List<StudentCourse> actual = converter.convertStudentCourseStatus(studentCourseList,studentCourseStatusList);

        assertThat(actual).hasSize(expected.size());
        assertThatStudentCourse(actual.get(0),expected.get(0));
        assertThatStudentCourse(actual.get(1),expected.get(1));
        assertThatStudentCourse(actual.get(2),expected.get(2));
    }

    @Test
    @DisplayName("受講生コース一覧と空の申込状況一覧なら申込状況が紐づかない受講生コース一覧を返す")
    void convertStudentCourseStatus_statusIsEmpty_returnOnlyCourseList(){
        List<StudentCourse> studentCourseList = createStudentCourseList();
        List<StudentCourseStatus> studentCourseStatusList = new ArrayList<>();

        StudentCourse studentCourse1 = studentCourseList.get(0);
        StudentCourse studentCourse2 = studentCourseList.get(1);
        StudentCourse studentCourse3 = studentCourseList.get(2);

        List<StudentCourse> expected = new ArrayList<>();
        expected.add(studentCourse1);
        expected.add(studentCourse2);
        expected.add(studentCourse3);

        List<StudentCourse> actual = converter.convertStudentCourseStatus(studentCourseList,studentCourseStatusList);

        assertThat(actual).hasSize(expected.size());

        assertThatStudentCourse(actual.get(0),expected.get(0));
        assertThatStudentCourse(actual.get(1),expected.get(1));
        assertThatStudentCourse(actual.get(2),expected.get(2));
    }

    @Test
    @DisplayName("空の受講生コース一覧と申込状況一覧なら空の受講生コース一覧を返す")
    void convertStudentCourseStatus_courseIsEmpty_returnEmptyList(){
        List<StudentCourse> studentCourseList = new ArrayList<>();
        List<StudentCourseStatus> studentCourseStatusList = createStudentCourseStatusList();

        List<StudentCourse> actual = converter.convertStudentCourseStatus(studentCourseList,studentCourseStatusList);

        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("受講生詳細一覧からコース名の内容で検索できる")
    void filterByCourseName_searchCourse_returnList(){
        /**
         *     public List<StudentDetail> filterByCourseName(List<StudentDetail> studentDetailList,String courseName){
         *         if (courseName.isEmpty()){
         *             return studentDetailList;
         *         }
         *         List<StudentDetail> studentDetails = new ArrayList<>();
         *         studentDetailList.forEach(studentDetail -> {
         *             List<StudentCourse> studentCourseList = studentDetail.getStudentCourseList().stream()
         *                     .filter(studentCourse -> studentCourse.getCourseName().contains(courseName))
         *                     .toList();
         *             StudentDetail addDetail = new StudentDetail(studentDetail.getStudent(),studentCourseList);
         *             if (!studentCourseList.isEmpty()){
         *                 studentDetails.add(addDetail);
         *             }
         *         });
         *         return studentDetails;
         *     }
         */
        List<StudentDetail> studentDetails = createStudentDetailList();
        String searchCourseName = "テストコース";

        List<StudentDetail> actual = converter.filterByCourseName(studentDetails,searchCourseName);

        assertThat(actual).hasSize(1);
        assertThat(actual.get(0).getStudentCourseList()).hasSize(2);
        assertThat(actual.get(0).getStudent().getId()).isEqualTo(studentDetails.get(0).getStudent().getId());
        assertThat(actual.get(0).getStudent().getName()).isEqualTo(studentDetails.get(0).getStudent().getName());

        assertThatStudentCourse(actual.get(0).getStudentCourseList().get(0),studentDetails.get(0).getStudentCourseList().get(0));
        assertThatStudentCourse(actual.get(0).getStudentCourseList().get(1),studentDetails.get(0).getStudentCourseList().get(1));

    }

    @Test
    @DisplayName("受講生詳細一覧からコース名が空の時検索がされず受講生詳細一覧がそのまま返る")
    void filterByCourseName_courseIsEmpty_returnList(){
        List<StudentDetail> studentDetails = createStudentDetailList();
        String searchCourseName = "";

        List<StudentDetail> actual = converter.filterByCourseName(studentDetails,searchCourseName);

        assertThat(actual).hasSize(3);
    }

    @Test
    @DisplayName("受講生詳細一覧を検索するとき該当するコース名がないなら空の受講生詳細一覧がそのまま返る")
    void filterByCourseName_doNotExistCourse_returnEmpty(){
        List<StudentDetail> studentDetails = createStudentDetailList();
        String searchCourseName = "該当しないコース";

        List<StudentDetail> actual = converter.filterByCourseName(studentDetails,searchCourseName);

        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("受講生詳細一覧から申込状況で検索できる")
    void filterByStatus_searchStatus_returnList(){
        /**
         *     public List<StudentDetail> filterByStatus(List<StudentDetail> studentDetailList,String status){
         *         if (status.isEmpty()){
         *             return studentDetailList;
         *         }
         *         List<StudentDetail> studentDetails = new ArrayList<>();
         *         studentDetailList.forEach(studentDetail -> {
         *             List<StudentCourse> studentCourseList = studentDetail.getStudentCourseList().stream()
         *                     .filter(studentCourse -> studentCourse.getStudentCourseStatus().getStatus().equals(status))
         *                     .toList();
         *             StudentDetail addDetail = new StudentDetail(studentDetail.getStudent(),studentCourseList);
         *             if (!studentCourseList.isEmpty()){
         *                 studentDetails.add(addDetail);
         *             }
         *         });
         *         return studentDetails;
         *     }
         */
        List<StudentDetail> studentDetails = createStudentDetailList();
        String searchStatus = "仮申込";

        List<StudentDetail> actual = converter.filterByStatus(studentDetails,searchStatus);

        assertThat(actual).hasSize(1);
        assertThat(actual.get(0).getStudentCourseList()).hasSize(2);
        assertThat(actual.get(0).getStudent().getId()).isEqualTo(studentDetails.get(0).getStudent().getId());
        assertThat(actual.get(0).getStudent().getName()).isEqualTo(studentDetails.get(0).getStudent().getName());

        assertThatStudentCourse(actual.get(0).getStudentCourseList().get(0),studentDetails.get(0).getStudentCourseList().get(0));
        assertThatStudentCourse(actual.get(0).getStudentCourseList().get(1),studentDetails.get(0).getStudentCourseList().get(1));

    }

    @Test
    @DisplayName("受講生詳細一覧から申込状況が空の時検索がされず受講生詳細一覧がそのまま返る")
    void filterByStatus_statusIsEmpty_returnList(){
        List<StudentDetail> studentDetails = createStudentDetailList();
        String searchStatus = "";

        List<StudentDetail> actual = converter.filterByStatus(studentDetails,searchStatus);

        assertThat(actual).hasSize(3);
    }

    @Test
    @DisplayName("受講生詳細一覧を検索するとき該当する申込状況がないなら空の受講生詳細一覧が返る")
    void filterByStatus_doNotExistStatus_returnEmpty(){
        List<StudentDetail> studentDetails = createStudentDetailList();
        String searchStatus = "該当しないコース";

        List<StudentDetail> actual = converter.filterByStatus(studentDetails,searchStatus);

        assertThat(actual).isEmpty();
    }

    @Test
    @DisplayName("受講生詳細一覧に申込状況がnullの受講コースが含まれていても申込状況で検索できる")
    void filterByStatus_studentCourseStatusIsNull_returnList() {
        List<StudentDetail> studentDetails = createStudentDetailList();

        studentDetails.get(0).getStudentCourseList().get(0).setStudentCourseStatus(null);
        String searchStatus = "仮申込";

        List<StudentDetail> actual = converter.filterByStatus(studentDetails, searchStatus);

        assertThat(actual).hasSize(1);
        assertThat(actual.get(0).getStudent().getId()).isEqualTo(studentDetails.get(0).getStudent().getId());
        assertThat(actual.get(0).getStudentCourseList()).hasSize(1);
        assertThat(actual.get(0).getStudentCourseList().get(0).getCourseName()).isEqualTo("テストコース2");
    }


    /**
     * テストに使用する受講生を作成するメソッド
     * @return Student
     */
    private static List<Student> createStudentList(){
        List<Student> studentList = new ArrayList<>();
        Student student1 = new Student();
        student1.setId(1);
        student1.setName("テスト太郎");
        studentList.add(student1);

        Student student2 = new Student();
        student2.setId(2);
        student2.setName("テスト次郎");
        studentList.add(student2);

        Student student3 = new Student();
        student3.setId(3);
        student3.setName("テスト三郎");
        studentList.add(student3);

        return studentList;
    }
    /**
     * テストに使用する受講生コース一覧を返す
     * 受講生IDはcreateStudentと紐づいている
     * @return List<StudentCourse></StudentCourse>
     */
    private static List<StudentCourse> createStudentCourseList() {
        List<StudentCourse> studentCourseList = new ArrayList<>();
        StudentCourse studentCourse1 = new StudentCourse();
        studentCourse1.setStudentId(1);
        studentCourse1.setId(1);
        studentCourse1.setCourseName("テストコース");
        studentCourseList.add(studentCourse1);
        StudentCourse studentCourse2 = new StudentCourse();
        studentCourse2.setStudentId(2);
        studentCourse2.setId(2);
        studentCourse2.setCourseName("テストJavaコース");
        studentCourseList.add(studentCourse2);
        StudentCourse studentCourse3 = new StudentCourse();
        studentCourse3.setStudentId(1);
        studentCourse3.setId(3);
        studentCourse3.setCourseName("テストAIコース");
        studentCourseList.add(studentCourse3);
        return studentCourseList;
    }

    /**
     * テストに使用する申込状況のリストが返る
     * 受講生IDはcreateStudentCourseListと紐づいている
     * @return List<StudentCourseStatus>
     */
    private static List<StudentCourseStatus> createStudentCourseStatusList() {
        List<StudentCourseStatus> studentCourseStatusList = new ArrayList<>();
        StudentCourseStatus studentCourseStatus1 = new StudentCourseStatus(1,1,"仮申込");
        StudentCourseStatus studentCourseStatus2 = new StudentCourseStatus(2,2,"本申込");
        StudentCourseStatus studentCourseStatus3 = new StudentCourseStatus(3,3,"受講中");
        studentCourseStatusList.add(studentCourseStatus1);
        studentCourseStatusList.add(studentCourseStatus2);
        studentCourseStatusList.add(studentCourseStatus3);
        return studentCourseStatusList;
    }

    /**
     * テスト用の受講生詳細一覧を返す
     * 全ての受講生には最低１件は受講コースが入っており申込状況も紐づいている
     * student1だけ受講コースが２件ある
     * @return List<StudentDetail>
     */
    private static List<StudentDetail> createStudentDetailList(){
        Student student1 = new Student();
        student1.setId(1);
        student1.setName("テスト太郎");

        Student student2 = new Student();
        student2.setId(2);
        student2.setName("テスト次郎");

        Student student3 = new Student();
        student3.setId(3);
        student3.setName("テスト三郎");

        StudentCourseStatus studentCourseStatus1 = new StudentCourseStatus(1,1,"仮申込");
        StudentCourseStatus studentCourseStatus1_2 = new StudentCourseStatus(2,1,"仮申込");
        StudentCourseStatus studentCourseStatus2 = new StudentCourseStatus(3,2,"本申込");
        StudentCourseStatus studentCourseStatus3 = new StudentCourseStatus(4,3,"受講中");

        List<StudentCourse> studentCourseList1 = new ArrayList<>();
        StudentCourse studentCourse1 = new StudentCourse();
        studentCourse1.setStudentId(1);
        studentCourse1.setId(1);
        studentCourse1.setCourseName("テストコース");
        studentCourse1.setStudentCourseStatus(studentCourseStatus1);
        studentCourseList1.add(studentCourse1);
        StudentCourse studentCourse1_2 = new StudentCourse();
        studentCourse1_2.setStudentId(1);
        studentCourse1_2.setId(2);
        studentCourse1_2.setCourseName("テストコース2");
        studentCourse1_2.setStudentCourseStatus(studentCourseStatus1_2);
        studentCourseList1.add(studentCourse1_2);

        List<StudentCourse> studentCourseList2 = new ArrayList<>();
        StudentCourse studentCourse2 = new StudentCourse();
        studentCourse2.setStudentId(2);
        studentCourse2.setId(3);
        studentCourse2.setCourseName("テストJavaコース");
        studentCourse2.setStudentCourseStatus(studentCourseStatus2);
        studentCourseList2.add(studentCourse2);

        List<StudentCourse> studentCourseList3 = new ArrayList<>();
        StudentCourse studentCourse3 = new StudentCourse();
        studentCourse3.setStudentId(3);
        studentCourse3.setId(4);
        studentCourse3.setCourseName("テストAIコース");
        studentCourse3.setStudentCourseStatus(studentCourseStatus3);
        studentCourseList3.add(studentCourse3);

        StudentDetail studentDetail1 = new StudentDetail(student1,studentCourseList1);
        StudentDetail studentDetail2 = new StudentDetail(student2,studentCourseList2);
        StudentDetail studentDetail3 = new StudentDetail(student3,studentCourseList3);
        return List.of(studentDetail1,studentDetail2,studentDetail3);
    }

    /**
     * 受講生コースの比較
     * 受講生コース名と受講生IDがactualとexpectedで合致するか確認
     * 申込状況が入っていれば申込状況の受講コースIDと申込状況の確認を行う
     * @param actual 実際の値
     * @param expected 期待する値
     */
    private static void assertThatStudentCourse(StudentCourse actual,StudentCourse expected){
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getStudentId()).isEqualTo(expected.getStudentId());
        assertThat(actual.getCourseName()).isEqualTo(expected.getCourseName());
        assertThat(actual.getStudentCourseStatus() == null).isEqualTo(expected.getStudentCourseStatus() == null);
        if (actual.getStudentCourseStatus() != null && expected.getStudentCourseStatus() != null){
            assertThat(actual.getStudentCourseStatus().getStudentCourseId()).isEqualTo(expected.getStudentCourseStatus().getStudentCourseId());
            assertThat(actual.getStudentCourseStatus().getStatus()).isEqualTo(expected.getStudentCourseStatus().getStatus());
        }
    }


}