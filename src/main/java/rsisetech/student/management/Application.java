package rsisetech.student.management;

import org.mybatis.spring.annotation.MapperScan;   // ★追加
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("rsisetech.student.management")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
