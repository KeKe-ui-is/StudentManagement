package raisetech.student.management.controller.converter;

import org.springframework.stereotype.Component;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 受講生情報と受講生コース情報を受講生詳細にもしくはその逆に変換するクラス
 * コンバーターです。
 */
@Component
public class StudentConverter {
    /**
     * 受講生に紐づく受講生コース情報をマッピングする
     * 受講生コース情報は受講生に対して複数存在するのでループを回して受講生詳細情報を組み立てる
     * @param studentList　受講生情報一覧
     * @param studentsCours　受講コース情報のリスト
     * @return 受講生詳細情報のリスト
     */
    public List<StudentDetail> convertStudentDetails(List<Student> studentList, List<StudentCourse> studentsCours) {
        List<StudentDetail> studentDetails = new ArrayList<>();

        studentList.forEach(student -> {
            StudentDetail studentDetail = new StudentDetail();
            studentDetail.setStudent(student);
            List<StudentCourse> convertStudentCourseList = studentsCours.stream()
                    .filter(studentCourse -> student.getId().equals(studentCourse.getStudentId()))
                    .collect(Collectors.toList());
            studentDetail.setStudentCourseList(convertStudentCourseList);
            studentDetails.add(studentDetail);
        });
        return studentDetails;
    }

    /**
     * convertStudentDetailsの受け取る受講生の数を１人分にしたもの
     * 受講生に紐づく受講生コース情報をマッピングする
     * 受講生コース情報は受講生に対して複数存在するのでループを回して受講生詳細情報を組み立てる
     * @param student　受講生情報
     * @param studentsCours　受講コース情報のリスト
     * @return　受講生詳細情報のリスト
     */
    public StudentDetail convertStudentDetail(Student student, List<StudentCourse> studentsCours) {
        StudentDetail studentDetail = new StudentDetail();
        studentDetail.setStudent(student);
        List<StudentCourse> convertStudentCourses = studentsCours.stream()
                    .filter(studentCourse -> student.getId().equals(studentCourse.getStudentId()))
                    .collect(Collectors.toList());
        studentDetail.setStudentCourseList(convertStudentCourses);
        return studentDetail;
    }
}