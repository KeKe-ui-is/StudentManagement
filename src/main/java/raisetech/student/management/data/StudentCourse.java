package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Schema(description = "受講生コース情報")
@Getter
@Setter
public class StudentCourse {
    @Schema(description = "受講コースID", example = "1")
    private Integer id;
    @Schema(description = "受講生ID", example = "1")
    private Integer studentId;
    @Size(min = 1,max = 50, message = "{studentCourse.courseName.size}") @NotNull
    @Schema(description = "コース名", example = "Javaフルコース")
    private String courseName;
    @Schema(description = "受講開始日", example = "2026-04-01T10:00:00")
    @PastOrPresent
    private LocalDateTime courseStartAt;
    @Schema(description = "受講修了予定日", example = "2027-04-01T10:00:00")
    private LocalDateTime courseEndAt;
}
