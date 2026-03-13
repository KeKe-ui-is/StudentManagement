package rsisetech.student.management.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
    //生徒単体の情報を表示
    @GetMapping("/showStudent")
    public String getStudent(@RequestParam int id, Model model){
        Student student = service.searchStudent(id);
        List<StudentsCourses> studentsCourses = service.searchStudentsCoursesList();
        StudentDetail studentDetail = converter.convertStudentDetail(student,studentsCourses);
        model.addAttribute("onlyStudent",studentDetail);
        return  "onlyStudent";
    }

    //受講生の更新



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
//受講生情報の新規登録
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
//    受講生情報の更新
    @PostMapping("/updateStudent")
    public String updateStudent(@ModelAttribute StudentDetail studentDetail ,BindingResult result){
        if (result.hasErrors()){
            return "updateStudent";
        }
        //更新処理
        service.updateStudent(studentDetail.getStudent());
        return "redirect:showStudent?id=" + studentDetail.getStudent().getId();
    }
}

