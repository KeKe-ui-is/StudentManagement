package raisetech.student.management.exception;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Schema(description = "バリデーションエラーレスポンス")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ViolationErrorResponse {

    @Schema(description = "エラー発生時刻", example = "2026-03-28T03:15:30.123Z")
    private Instant timestamp;

    @Schema(description = "HTTPステータス", example = "400")
    private int status;

    @ArraySchema(schema = @Schema(implementation = ViolationErrorDetail.class))
    private List<ViolationErrorDetail> errors;

    @Schema(description = "リクエストURL", example = "/student/aaaa")
    private String url;
}