package rsisetech.student.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import rsisetech.student.management.controller.converter.StudentConverter;
import rsisetech.student.management.data.Student;
import rsisetech.student.management.data.StudentsCourses;
import rsisetech.student.management.domain.StudentDetail;
import rsisetech.student.management.service.StudentService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class StudentController {

    private StudentService service;
    private StudentConverter converter;

    @Autowired
    public StudentController(StudentService service, StudentConverter converter) {
        this.service = service;
        this.converter = converter;
    }

    @GetMapping("/studentList")
    public String getStudentList(Model model) {
        List<Student> students = service.searchStudentList();
        List<StudentsCourses> studentsCourses = service.searchStudentsCoursesList();

        model.addAttribute("studentList", converter.convertStudentDetails(students, studentsCourses));
        return "studentList";
        //return のstudentListは返すhtmlファイルの名前 その前のはそのhtmlファイルの中に入っている${studentList}のこと
    }


    @GetMapping("/studentsCoursesList")
    public List<StudentsCourses> getStudentsCourses() {
        return service.searchStudentsCoursesList();
    }


    @GetMapping("/newStudent")
    public String newStudent(Model model) {
        StudentDetail studentDetail = new StudentDetail();
        studentDetail.setStudentsCourses(Arrays.asList(new StudentsCourses()));
        model.addAttribute("studentDetail", studentDetail);
        return "registerStudent";
    }

    @PostMapping("/registerStudent")
    public String registerStudent(@ModelAttribute StudentDetail studentDetail, BindingResult result) {
        if(result.hasErrors()){
            return "registerStudent";
        }
        //課題　新規受講生情報を登録する処理を実装
        //コース情報も一緒に登録できるように実装する　コースは単体で良い
        service.registerStudent(studentDetail);
        return "redirect:/studentList";
    }
}

