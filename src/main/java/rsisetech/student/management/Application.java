package rsisetech.student.management;

import org.mybatis.spring.annotation.MapperScan;   // ★追加
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@RestController
@MapperScan("rsisetech.student.management")        // ★追加
public class Application {
	@Autowired
	private StudentRepository repository;

	public Application(StudentRepository repository) { // ★おすすめ：コンストラクタ注入
		this.repository = repository;
	}

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@GetMapping("/student")
	public String getStudent(@RequestParam("name") String name){
		Student student = repository.searchByName(name);
		return student.getName() + " " + student.getAge() + "歳";
	}

	@PostMapping("/student")
	public void registerStudent(String name,int age){
		repository.registerStudent(name,age);
	}

	@PatchMapping("/student")
	public void updateStudent(String name ,int age){
		repository.updateStudent(name,age);
	}

	@DeleteMapping("/student")
	public void deleteStudent(@RequestParam String name){
		repository.deleteStudent(name);
	}

}
