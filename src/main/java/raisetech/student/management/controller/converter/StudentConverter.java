package raisetech.student.management.controller.converter;

import org.springframework.stereotype.Component;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.StudentCourseStatus;
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
     * @param studentsCourseList　受講コース情報のリスト
     * @return 受講生詳細情報のリスト
     */
    public List<StudentDetail> convertStudentDetails(List<Student> studentList, List<StudentCourse> studentsCourseList) {
        List<StudentDetail> studentDetails = new ArrayList<>();

        studentList.forEach(student -> {
            StudentDetail studentDetail = new StudentDetail();
            studentDetail.setStudent(student);
            List<StudentCourse> convertStudentCourseList = studentsCourseList.stream()
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
     * @param studentCourseList　受講コース情報のリスト
     * @return　受講生詳細情報のリスト
     */
    public StudentDetail convertStudentDetail(Student student, List<StudentCourse> studentCourseList) {
        StudentDetail studentDetail = new StudentDetail();
        studentDetail.setStudent(student);
        List<StudentCourse> convertStudentCourses = studentCourseList.stream()
                    .filter(studentCourse -> student.getId().equals(studentCourse.getStudentId()))
                    .collect(Collectors.toList());
        studentDetail.setStudentCourseList(convertStudentCourses);
        return studentDetail;
    }
    /**
     * 受講生コース情報と受講状況のリストを受け取り受講生コースIDで紐づく受講状況をセットする
     * @param studentCourseList 受講コース情報一覧
     * @param studentCourseStatusList 受講状況一覧
     * @return 受講コース情報一覧(受講状況セット済み）
     */
    public List<StudentCourse> convertStudentCourseStatus(List<StudentCourse> studentCourseList, List<StudentCourseStatus> studentCourseStatusList) {
            studentCourseList.forEach(studentCourse -> {
                studentCourseStatusList.stream()
                        .filter(studentCourseStatus -> studentCourseStatus.getStudentCourseId().equals(studentCourse.getId()))
                        .findFirst()
                        .ifPresent(studentCourse::setStudentCourseStatus);
            });
        return studentCourseList;
    }

    /**
     * 受講生情報一覧から受講コース名があるものだけ取り出して受講生情報一覧を返すメソッド
     * courseNameが空文字やnullなら処理は行わない
     * 検索時は部分一致
     * @param studentDetailList 受講生詳細一覧
     * @param courseName フィルターにかける受講コース名
     * @return 受講生詳細一覧
     */
    public List<StudentDetail> filterByCourseName(List<StudentDetail> studentDetailList,String courseName){
        if (courseName.isEmpty()){
            return studentDetailList;
        }
        List<StudentDetail> studentDetails = new ArrayList<>();
        studentDetailList.forEach(studentDetail -> {
            List<StudentCourse> studentCourseList = studentDetail.getStudentCourseList().stream()
                    .filter(studentCourse -> studentCourse.getCourseName().contains(courseName))
                    .toList();
            StudentDetail addDetail = new StudentDetail(studentDetail.getStudent(),studentCourseList);
            if (!studentCourseList.isEmpty()){
                studentDetails.add(addDetail);
            }
        });
        return studentDetails;
    }
    /**
     * 受講生情報一覧から申込状況があるものだけ取り出して受講生情報一覧を返すメソッド
     * 申込状況が空文字やnullなら処理は行わない
     * 検索時は完全一致
     * @param studentDetailList 受講生詳細一覧
     * @param status 検索に用いる申込状況
     * @return 受講生詳細一覧
     */
    public List<StudentDetail> filterByStatus(List<StudentDetail> studentDetailList,String status){
        if (status.isEmpty()){
            return studentDetailList;
        }
        List<StudentDetail> studentDetails = new ArrayList<>();
        studentDetailList.forEach(studentDetail -> {
            List<StudentCourse> studentCourseList = studentDetail.getStudentCourseList().stream()
                    .filter(studentCourse -> studentCourse.getStudentCourseStatus().getStatus().equals(status))
                    .toList();
            StudentDetail addDetail = new StudentDetail(studentDetail.getStudent(),studentCourseList);
            if (!studentCourseList.isEmpty()){
                studentDetails.add(addDetail);
            }
        });
        return studentDetails;
    }
}