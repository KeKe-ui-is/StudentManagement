package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*
  受講生情報を扱うオブジェクト
*/
@Schema(description = "受講生情報")
@Getter
@Setter
public class Student {
    @Schema(description = "受講生ID", example = "1")
    private String id;
    // 名前
    @Schema(description = "名前", example = "山田太郎")
    @NotBlank(message = "{student.name.required}")
    @Size(min = 1, max = 50, message = "{student.name.size}")
    private String name;
    // カナ名
    @Schema(description = "カナ名", example = "ヤマダタロウ")
    @NotBlank(message = "{student.kanaName.required}")
    @Size(min = 1, max = 50, message = "{student.kanaName.size}")
    private String kanaName;
    // ニックネーム
    @Schema(description = "ニックネーム", example = "太郎")
    @NotBlank(message = "{student.nickname.required}")
    @Size(min = 1, max = 50, message = "{student.nickname.size}")
    private String nickname;
    // メール
    @Schema(description = "メールアドレス", example = "example@gmail.com")
    @NotBlank(message = "{student.email.required}")
    @Email(message = "{student.email.invalid}")
    @Size(max = 50, message = "{student.email.size}")
    private String email;
    // 地域
    @Schema(description = "居住地域", example = "東京")
    @Size(min = 1, max = 50, message = "{student.area.size}")
    private String area;
    // 年齢
    @Schema(description = "年齢", example = "30")
    @Min(value = 0, message = "{student.age.min}")
    @Max(value = 100, message = "{student.age.max}")
    private int age;
    // 性別
    @Schema(description = "性別", example = "男性")
    @Size(min = 1, max = 10, message = "{student.sex.size}")
    private String sex;
    // 備考
    @Schema(description = "備考欄", example = "特になし")
    @Size(max = 255, message = "{student.remark.size}")
    private String remark;
    // 削除フラグ
    @Schema(description = "削除フラグ", example = "false")
    private boolean deleted;
}