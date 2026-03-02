package rsisetech.student.management;

import org.mybatis.spring.annotation.MapperScan;   // ★追加
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;
import rsisetech.student.management.data.Student;
import rsisetech.student.management.data.StudentsCourses;
import rsisetech.student.management.repository.StudentCoursesRepository;
import rsisetech.student.management.repository.StudentRepository;

import java.util.List;

@SpringBootApplication
@MapperScan("rsisetech.student.management")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
