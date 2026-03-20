package raisetech.student.management.data;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

/*
  受講生情報を扱うオブジェクト
*/
@Getter
@Setter
public class Student {

    private String id;
    // 名前
    @NotBlank(message = "{student.name.required}")
    @Size(min = 1, max = 50, message = "{student.name.size}")
    private String name;
    // カナ名
    @NotBlank(message = "{student.kanaName.required}")
    @Size(min = 1, max = 50, message = "{student.kanaName.size}")
    private String kanaName;
    // ニックネーム
    @NotBlank(message = "{student.nickname.required}")
    @Size(min = 1, max = 50, message = "{student.nickname.size}")
    private String nickname;
    // メール
    @NotBlank(message = "{student.email.required}")
    @Email(message = "{student.email.invalid}")
    @Size(max = 50, message = "{student.email.size}")
    private String email;
    // 地域
    @Size(min = 1, max = 50, message = "{student.area.size}")
    private String area;
    // 年齢
    @NotNull(message = "{student.age.required}")
    @Min(value = 0, message = "{student.age.min}")
    @Max(value = 100, message = "{student.age.max}")
    private Integer age;
    // 性別
    @Size(min = 1, max = 10, message = "{student.sex.size}")
    private String sex;
    // 備考
    @Size(max = 255, message = "{student.remark.size}")
    private String remark;
    // 削除フラグ
    private boolean deleted;
}