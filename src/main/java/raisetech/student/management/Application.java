package raisetech.student.management;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.mybatis.spring.annotation.MapperScan;   // ★追加
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@OpenAPIDefinition(
		info = @Info(
				title = "受講生管理システム",
				version = "1.0.0",
				description = "受講生情報および受講生コース情報を管理するアプリです",
				contact = @Contact(
						name = "kobayashi keito",
						email = "xiaolinxiren@gmail.com"
				),
				license = @License(
						name = "Apache 2.0"
				)
		),
		tags = {
				@Tag(name="StudentAPI",description = "受講生および受講生コース情報を管理する受講生管理システムのAPI")
		}
)
@SpringBootApplication
@MapperScan("raisetech.student.management")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
