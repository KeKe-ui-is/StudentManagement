package rsisetech.student.management.domain;

import lombok.Getter;
import lombok.Setter;
import rsisetech.student.management.data.Student;
import rsisetech.student.management.data.StudentsCourses;

import java.util.List;
@Getter
@Setter
public class StudentDetail {
    private Student student;
    private List<StudentsCourses> studentsCourses;
    private String courseName;
}
