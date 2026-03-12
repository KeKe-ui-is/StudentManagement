package rsisetech.student.management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rsisetech.student.management.data.Student;
import rsisetech.student.management.data.StudentsCourses;
import rsisetech.student.management.domain.StudentDetail;
import rsisetech.student.management.repository.StudentCoursesRepository;
import rsisetech.student.management.repository.StudentRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StudentService {
    private StudentRepository repository;
    private StudentCoursesRepository studentsCoursesRepository;
    private Components components;
    @Autowired
    public StudentService(StudentRepository repository, StudentCoursesRepository studentCoursesRepository, Components components) {
        this.repository = repository;
        this.studentsCoursesRepository = studentCoursesRepository;
        this.components = components;
    }
    public List<Student> searchStudentList(){
        return repository.search();
    }

    public List<Student> searchStudentListMinAge30(){
        return repository.search().stream()
                .filter(student -> student.getAge() >=30)
                .toList();
    }

    public List<StudentsCourses> searchStudentsCoursesList(){
        return repository.searchStudentsCourses();
    }

    public List<StudentsCourses> searchStudentsCoursesNameJava(){
        return repository.searchStudentsCourses().stream()
                .filter(studentsCourses -> studentsCourses.getCourseName().equals("Java基礎コース"))
                .toList();
    }
    @Transactional
    public void registerStudent(StudentDetail studentDetail){
        repository.registerStudent(studentDetail.getStudent());
        for (StudentsCourses studentsCourses: studentDetail.getStudentsCourses()){
            studentsCourses.setStudentId(studentDetail.getStudent().getId());
            studentsCourses.setCourseStartAt(LocalDateTime.now());
            studentsCourses.setCourseEndAt(LocalDateTime.now().plusYears(1));
            repository.registerStudentsCourses(studentsCourses);
        }
    }

    public void addStudentCourses(String courseName,String id,String studentId){
        StudentsCourses studentsCourses = new StudentsCourses();
        studentsCourses.setCourseName(courseName);
        studentsCourses.setId(id);
        studentsCourses.setStudentId(studentId);
        studentsCoursesRepository.addStudentsCourses(studentsCourses);
    }

//    public void addStudentsAndCourses(StudentDetail studentDetail){
//        String studentId = components.createId(repository.getMaxId(),'s');
//        String courseId = components.createId(studentsCoursesRepository.getMaxId(),'c');
//
//        registerStudent(studentDetail.getStudent());
//        addStudentCourses(studentDetail.getCourseName() ,courseId,studentId);
//    }

}
