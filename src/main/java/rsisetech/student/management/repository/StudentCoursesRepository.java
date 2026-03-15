package rsisetech.student.management.repository;


import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import rsisetech.student.management.data.StudentCourse;

import java.util.List;
/**
*受講生情報を扱うリポジトリ
* 全件検索や単一条件での検索、コース情報の検索が行えるクラス
 */
@Mapper
public interface StudentCoursesRepository {
    /**
     *
     * @return 受講生情報を全件検索した一覧
     */
    @Select("SELECT * FROM students_courses")
    List<StudentCourse> search();

    @Select("SELECT id FROM students_courses ORDER BY id DESC LIMIT 1")
    String getMaxId();

    @Insert("""
            INSERT INTO students_courses(id,student_id,course_name,course_start_at,course_end_at)
            VALUES(#{id},#{studentId},#{courseName},#{courseStartAt},#{courseEndAt})
            """)
    void addStudentsCourses(StudentCourse studentCourse);
}
