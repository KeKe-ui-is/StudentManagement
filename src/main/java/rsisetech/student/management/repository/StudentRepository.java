package rsisetech.student.management.repository;

import org.apache.ibatis.annotations.*;
import rsisetech.student.management.data.Student;
import rsisetech.student.management.data.StudentCourse;

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
    @Select("SELECT * FROM students ")
    List<Student> search();

    @Select("SELECT * FROM students_courses")
    List<StudentCourse> searchStudentCourseList();

    /**
     * 受講生のidに紐づく情報単体を取得
     * @param id　受講生ID
     * @return　受講生情報(単体)
     */
    @Select("SELECT * FROM students WHERE id=#{id}")
    Student searchStudent(String id);

    /**
     * 受講生IDと紐づく受講生コース情報を取得
     * @param id 受講生ID
     * @return 受講生に紐づく受講生コース情報
     */
    @Select("SELECT * FROM students_courses WHERE student_id=#{id}")
    List<StudentCourse> searchStudentCourse(String id);

    /**
     * 新規で受講生を登録する際に使われる
     * IDは自動採番
     * @param student 受講生情報　
     */
    @Insert("""
            INSERT INTO students(name,kana_name,nickname,email,area,age,sex,remark,deleted)
            VALUES
            (#{name},#{kanaName},#{nickname},#{email},#{area},#{age},#{sex},#{remark},false)
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void registerStudent(Student student);

    /**
     * 受講生コース情報を新規で登録する際に使われる
     * IDは自動採番
     * @param studentCourse　受講生コース情報
     */
    @Insert("""
    INSERT INTO students_courses(student_id, course_name, course_start_at, course_end_at)
    VALUES(#{studentId}, #{courseName}, #{courseStartAt}, #{courseEndAt})
    """)
    @Options(useGeneratedKeys = true,keyProperty ="id")
    void registerStudentCourse(StudentCourse studentCourse);

    /**
     * 受講生情報を更新する際に使われる
     * キャンセルフラグの管理もここで行う（論理削除）
     * @param student　受講生情報
     */
    @Update("""
            UPDATE students
            SET name=#{name}, kana_name=#{kanaName},nickname=#{nickname}, email=#{email}, area=#{area}, age=#{age}, sex=#{sex}, remark=#{remark},deleted=#{deleted}
            WHERE id=#{id}
            """)
    void updateStudent(Student student);

    /**
     * 受講生コースを更新する際に使われる
     * @param studentCourse　受講生コース情報
     */
    @Update("""
            UPDATE students_courses
            SET course_name=#{courseName}
            WHERE student_id=#{studentId}
            AND id=#{id}
            """)
    void updateStudentCourse(StudentCourse studentCourse);
//    @Insert("INSERT student values(#{name},#{age})")
//    void registerStudent(String name,int age);
//
//    @Update("UPDATE student SET age = #{age} WHERE name = #{name}")
//    void updateStudent(String name ,int age);
//
//    @Delete("DELETE FROM student WHERE name = #{name}")
//    void deleteStudent(String name);
}
