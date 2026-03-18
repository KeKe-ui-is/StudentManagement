package raisetech.student.management.data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class StudentCourse {
    private String id;
    private String studentId;
    @Size(min = 1,max = 50) @NotNull
    private String courseName;
    @PastOrPresent
    private LocalDateTime courseStartAt;
    private LocalDateTime courseEndAt;
}
