package rsisetech.student.management;

import org.mybatis.spring.annotation.MapperScan;   // ★追加
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SpringBootApplication
@RestController
@MapperScan("rsisetech.student.management")        // ★追加
public class Application {
	@Autowired
	private StudentRepository repository;
	@Autowired
	private StudentCoursesRepository studentCoursesRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@GetMapping("/student")
	public List<Student> getStudentList(){
		return repository.search();
	}

	@GetMapping("/studentCourse")
	public List<StudentsCourses> getStudentCoursesList(){
		return studentCoursesRepository.search();
	}

}
