package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "受講生コース情報の際申込状況情報が入るクラス")
@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor

public class StudentCourseStatus {
    @Schema(description = "受講状況ID", example = "1")
    private Integer id;
    @Schema(description = "受講生コース情報ID", example = "1")
    private Integer studentCourseId;
    @Size(min = 1,max = 10, message = "{studentCourseStatus.studentCourseStatus.size}")
    @NotNull
    @Schema(description = "申込状況の内容", example = "受講中")
    private String status;
}
