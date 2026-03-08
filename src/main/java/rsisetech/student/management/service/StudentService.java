package rsisetech.student.management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rsisetech.student.management.data.Student;
import rsisetech.student.management.data.StudentsCourses;
import rsisetech.student.management.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {
    private StudentRepository repository;

    @Autowired
    public StudentService(StudentRepository repository) {
        this.repository = repository;
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
}
