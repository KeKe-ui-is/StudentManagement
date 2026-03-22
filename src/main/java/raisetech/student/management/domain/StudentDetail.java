package raisetech.student.management.domain;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;

import java.util.List;

@Schema(description = "受講生詳細")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StudentDetail {
    @Schema(description = "受講生情報")
    @Valid
    private Student student;
    @ArraySchema(schema = @Schema(description = "１つから複数の受講生コース情報"))
    @Valid
    private List<StudentCourse> studentCourseList;
}
