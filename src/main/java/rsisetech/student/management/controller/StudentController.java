package rsisetech.student.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import rsisetech.student.management.data.Student;
import rsisetech.student.management.data.StudentsCourses;
import rsisetech.student.management.service.StudentService;

import java.util.List;

@RestController
public class StudentController {

    private StudentService service;
    @Autowired
    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping("/student")
    public List<Student> getStudentList(){
        return service.searchStudentList();
    }

    @GetMapping("/studentAge30")
    public List<Student> getStudentAge30(){
        return service.searchStudentListMinAge30();
    }

    @GetMapping("/studentsCoursesList")
    public List<StudentsCourses> getStudentsCourses(){
        return service.searchStudentsCoursesList();
    }

    @GetMapping("/studentsCoursesNameJava")
    public  List<StudentsCourses> getStudentsCoursesNameJava(){
        return service.searchStudentsCoursesNameJava();
    }

    //課題３０代以上の人のみ抽出する
    //抽出したリストをコントローラーに返す

    //Javaフルコースの情報のみを抽出する
    //抽出したリストを返す
}
