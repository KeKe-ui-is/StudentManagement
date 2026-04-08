package raisetech.student.management.controller.converter;

import org.junit.jupiter.api.BeforeEach;
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
    void 空の受講生一覧と空の受講生コース一覧なら空の受講詳細一覧を返す() {
        List<Student> studentList = new ArrayList<>();
        List<StudentCourse> studentCourseList = new ArrayList<>();
        List<StudentDetail> expected = new ArrayList<>();

        List<StudentDetail> actual = converter.convertStudentDetails(studentList, studentCourseList);

        assertThat(actual).isEmpty();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void 受講生idで紐づいた受講詳細情報のリストが返ってくること() {
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
        assertThat(actual.size()).isEqualTo(expected.size());
        assertThat(actual.get(0).getStudent()).isEqualTo(studentDetail1.getStudent());
        assertThat(actual.get(1).getStudent()).isEqualTo(studentDetail2.getStudent());

        assertThat(actual.get(0).getStudentCourseList()).isEqualTo(studentDetail1.getStudentCourseList());
        assertThat(actual.get(1).getStudentCourseList()).isEqualTo(studentDetail2.getStudentCourseList());

    }

    @Test
    void null要素を含む受講生コース一覧なら空の受講詳細一覧を返す() {
        //事前準備
        List<Student> studentList = new ArrayList<>();
        Student student1 = null;
        Student student2 = null;
        List<StudentCourse> studentCourseList = new ArrayList<>();
        StudentCourse studentCourse1 = null;
        studentCourseList.add(studentCourse1);
        StudentCourse studentCourse2 = null;
        studentCourseList.add(studentCourse2);
        StudentCourse studentCourse3 = null;
        studentCourseList.add(studentCourse3);

        //実行
        List<StudentDetail> actual = converter.convertStudentDetails(studentList, studentCourseList);
        //検証
        assertThat(actual.size()).isEqualTo(0);
        assertThat(actual).isEmpty();
    }

    @Test
    void 受講生コース一覧がnullの時NullPointerExceptionが発生すること() {
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
    void 受講生一覧がnullの時NullPointerExceptionが発生すること() {
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
    void 受講生一覧と受講コース一覧がnullの時NullPointerExceptionが発生すること() {
        //事前準備
        List<Student> studentList = null;
        List<StudentCourse> studentCourseList = null;

        //実行
        assertThatThrownBy(
                () -> converter.convertStudentDetails(studentList, studentCourseList)
        ).isInstanceOf(NullPointerException.class);
    }

    @Test
    void 空の受講生コース一覧を渡すと空の受講コース情報一覧が入った受講詳細情報のリストが返ってくること() {
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
        assertThat(actual.size()).isEqualTo(expected.size());
        assertThat(actual.get(0).getStudent()).isEqualTo(studentDetail1.getStudent());
        assertThat(actual.get(1).getStudent()).isEqualTo(studentDetail2.getStudent());

        assertThat(actual.get(0).getStudentCourseList()).isEmpty();
        assertThat(actual.get(1).getStudentCourseList()).isEmpty();

    }

    @Test
    void 紐づく受講コースがない受講生一覧なら空のコース一覧を持つ受講詳細一覧を返す() {
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
        assertThat(actual.size()).isEqualTo(expected.size());
        assertThat(actual.get(0).getStudent()).isEqualTo(studentDetail1.getStudent());
        assertThat(actual.get(1).getStudent()).isEqualTo(studentDetail2.getStudent());

        assertThat(actual.get(0).getStudentCourseList()).isEmpty();
        assertThat(actual.get(1).getStudentCourseList()).isEmpty();

    }

    @Test
    void 空の受講生と空の受講生コース一覧を渡すと空の受講詳細情報が返ってくること() {
        Student student = new Student();
        List<StudentCourse> studentCourseList = new ArrayList<>();
        StudentDetail expected = new StudentDetail(student, studentCourseList);

        StudentDetail actual = converter.convertStudentDetail(student, studentCourseList);

        assertThat(actual.getStudent()).isEqualTo(expected.getStudent());
        assertThat(actual.getStudentCourseList()).isEmpty();
    }

    @Test
    void 受講生idで紐づいた受講詳細情報単体が返ってくること() {
        //事前準備
        List<Student> studentList = new ArrayList<>();
        Student student = new Student();
        student.setId("1");
        student.setName("テスト太郎");
        studentList.add(student);

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
        assertThat(actual.getStudent()).isEqualTo(expected.getStudent());
        assertThat(actual.getStudentCourseList()).isEqualTo(expected.getStudentCourseList());
    }

    @Test
    void 受講生と受講生コース一覧がnullな時NullPointerExceptionが発生すること() {
        //事前準備
        Student student = null;
        List<StudentCourse> studentCourseList = null;

        //実行
        assertThatThrownBy(
                () -> converter.convertStudentDetail(student, studentCourseList)
        ).isInstanceOf(NullPointerException.class);

    }

    @Test
    void 受講生と空の受講生コース一覧を渡すと空の受講コース情報が入った受講詳細一覧が返ってくること() {
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
        assertThat(actual.getStudent()).isEqualTo(expected.getStudent());
        assertThat(actual.getStudentCourseList()).isEmpty();
    }

    @Test
    void 受講コース情報と紐づかない受講生IDな受講生を渡すと受講生だけが入った受講生詳細が返ってくること() {
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
        assertThat(actual.getStudent()).isEqualTo(expected.getStudent());
        assertThat(actual.getStudentCourseList()).isEmpty();
    }

}