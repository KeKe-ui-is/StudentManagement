package raisetech.student.management.repository;

import org.apache.ibatis.annotations.*;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

import java.util.List;

/**
 * 受講生テーブルと受講生コース情報テーブルと紐づくRepositoryです。
 */
@Mapper
public interface StudentRepository {
    /**
     * 受講生の全件検索を行います。
     * @return 受講生一覧（全件）
     */
    List<Student> search();

    /**
     * 受講生コース情報の全件検索を行います。
     * @return 受講生コース情報一覧
     */
    List<StudentCourse> searchStudentCourseList();
    /**
     * 受講生のidに紐づく情報単体を取得
     * @param id 受講生ID
     * @return 受講生情報(単体)
     */
    Student searchStudent(Integer id);
    /**
     * 受講生IDと紐づく受講生コース情報を取得
     * @param id 受講生ID
     * @return 受講生に紐づく受講生コース情報
     */
    List<StudentCourse> searchStudentCourse(Integer id);
    /**
     * 新規で受講生を登録する際に使われる
     * IDは自動採番
     * @param student 受講生情報　
     */
    void registerStudent(Student student);
    /**
     * 受講生コース情報を新規で登録する際に使われる
     * IDは自動採番
     * @param studentCourse　受講生コース情報
     */
    void registerStudentCourse(StudentCourse studentCourse);
    /**
     * 受講生情報を更新する際に使われる
     * キャンセルフラグの管理もここで行う（論理削除）
     * @param student　受講生情報
     */
    void updateStudent(Student student);
    /**
     * 受講生コースを更新する際に使われる
     * @param studentCourse　受講生コース情報
     */
    void updateStudentCourse(StudentCourse studentCourse);

}
