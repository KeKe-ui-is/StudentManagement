package raisetech.student.management.controller.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StudentConverterTest {

    private StudentConverter converter;

    @BeforeEach
    void before(){
        converter = new StudentConverter();
    }

    @Test
    void リストの受講生情報とリストの受講生コース情報を渡すと受講生idで紐づいた受講詳細情報のリストが返ってくること(){
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
        StudentDetail studentDetail1 = new StudentDetail(student1,student1CourseList);
        //2人目の受講生詳細
        List<StudentCourse> student2CourseList = new ArrayList<>();
        student2CourseList.add(studentCourse2);
        StudentDetail studentDetail2 = new StudentDetail(student2,student2CourseList);
        //期待する値
        List<StudentDetail> expected = List.of(studentDetail1,studentDetail2);
        //実行
        List<StudentDetail> actual = converter.convertStudentDetails(studentList,studentCourseList);
        //検証
        assertEquals(expected.size(),actual.size());
        assertEquals(studentDetail1.getStudent(),actual.get(0).getStudent());
        assertEquals(studentDetail2.getStudent(),actual.get(1).getStudent());

        assertEquals(studentDetail1.getStudentCourseList(),actual.get(0).getStudentCourseList());
        assertEquals(studentDetail2.getStudentCourseList(),actual.get(1).getStudentCourseList());
    }

    @Test
    void 受講生情報とリストの受講生コース情報を渡すと受講生idで紐づいた受講詳細情報が返ってくること(){
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

        List<StudentCourse> expectedStudentCourseList = List.of(studentCourse1,studentCourse3);

        StudentDetail expected = new StudentDetail(student,expectedStudentCourseList);
        //実行
        StudentDetail actual = converter.convertStudentDetail(student,studentCourseList);
        //検証
        assertEquals(expected.getStudentCourseList().size(),actual.getStudentCourseList().size());
        assertEquals(expected.getStudent(),actual.getStudent());
        assertEquals(expected.getStudentCourseList(),actual.getStudentCourseList());
    }
}