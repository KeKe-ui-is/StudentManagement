package rsisetech.student.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import rsisetech.student.management.controller.converter.StudentConverter;
import rsisetech.student.management.data.Student;
import rsisetech.student.management.data.StudentsCourses;
import rsisetech.student.management.domain.StudentDetail;
import rsisetech.student.management.service.StudentService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class StudentController {

    private StudentService service;
    private  StudentConverter converter;
    @Autowired
    public StudentController(StudentService service, StudentConverter converter) {
        this.service = service;
        this.converter = converter;
    }

    @GetMapping("/studentList")
    public String getStudentList(Model model){
        List<Student> students = service.searchStudentList();
        List<StudentsCourses> studentsCourses = service.searchStudentsCoursesList();

        model.addAttribute("studentList", converter.convertStudentDetails(students, studentsCourses));
        return "studentList";
        //return のstudentListは返すhtmlファイルの名前 その前のはそのhtmlファイルの中に入っている${studentList}のこと
    }



    @GetMapping("/studentsCoursesList")
    public List<StudentsCourses> getStudentsCourses(){
        return service.searchStudentsCoursesList();
    }
}
