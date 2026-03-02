package rsisetech.student.management.repository;

import org.apache.ibatis.annotations.*;
import rsisetech.student.management.data.Student;
import rsisetech.student.management.data.StudentsCourses;

import java.util.List;

@Mapper
public interface StudentRepository {
    @Select("SELECT * FROM students")
    List<Student> search();

    @Select("SELECT * FROM students_courses")
    List<StudentsCourses> searchStudentsCourses();

//    @Insert("INSERT student values(#{name},#{age})")
//    void registerStudent(String name,int age);
//
//    @Update("UPDATE student SET age = #{age} WHERE name = #{name}")
//    void updateStudent(String name ,int age);
//
//    @Delete("DELETE FROM student WHERE name = #{name}")
//    void deleteStudent(String name);
}
