package rsisetech.student.management;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Application {
	private String name = "Enami Kouji";
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@GetMapping("/name")
	public String getName() {
		return name;
	}
	}



