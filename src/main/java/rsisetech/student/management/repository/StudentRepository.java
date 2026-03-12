package rsisetech.student.management.repository;

import org.apache.ibatis.annotations.*;
import rsisetech.student.management.data.Student;
import rsisetech.student.management.data.StudentsCourses;
import rsisetech.student.management.domain.StudentDetail;

import java.util.List;

@Mapper
public interface StudentRepository {
    @Select("SELECT * FROM students")
    List<Student> search();

    @Select("SELECT * FROM students_courses")
    List<StudentsCourses> searchStudentsCourses();

    //IDの最大値を取得
    @Select("SELECT id FROM students ORDER BY id DESC LIMIT 1")
    String getMaxId();

    @Insert("""
            INSERT INTO students(name,kana_name,nickname,email,area,age,sex,remark,isDeleted)
            VALUES
            (#{name},#{kanaName},#{nickname},#{email},#{area},#{age},#{sex},#{remark},false)
            """)
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void registerStudent(Student student);

    @Insert("""
    INSERT INTO students_courses(student_id, course_name, course_start_at, course_end_at)
    VALUES(#{studentId}, #{courseName}, #{courseStartAt}, #{courseEndAt})
    """)
    void registerStudentsCourses(StudentsCourses studentsCourses);
//    @Insert("INSERT student values(#{name},#{age})")
//    void registerStudent(String name,int age);
//
//    @Update("UPDATE student SET age = #{age} WHERE name = #{name}")
//    void updateStudent(String name ,int age);
//
//    @Delete("DELETE FROM student WHERE name = #{name}")
//    void deleteStudent(String name);
}
