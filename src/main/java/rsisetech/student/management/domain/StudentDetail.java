package rsisetech.student.management.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rsisetech.student.management.data.Student;
import rsisetech.student.management.data.StudentCourse;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetail {
    private Student student;
    private List<StudentCourse> studentCourseList;
}
