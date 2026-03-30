package raisetech.student.management.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Schema(description = "バリデーションエラー詳細")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ViolationErrorDetail {

    @Schema(description = "エラーが発生した項目名", example = "id")
    private String path;

    @Schema(description = "エラーメッセージ", example = "IDは1～3文字で入力してください")
    private String message;
}