package raisetech.student.management.data;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Schema(description = "受講生検索の際検索条件を入れておくクラス")
@Getter
@Setter

public class StudentSearchCondition {
    @Schema(description = "受講生ID", example = "1")
    @Min(value = 0, message = "{studentSearchCondition.id.size}")
    @Max(value = 999, message = "{studentSearchCondition.id.size}")
    private Integer id;
    // 名前
    @Schema(description = "名前", example = "山田太郎")
    @Size(max = 50, message = "{studentSearchCondition.name.size}")
    private String name;
    // カナ名
    @Schema(description = "カナ名", example = "ヤマダタロウ")
    @Size(max = 50, message = "{studentSearchCondition.kanaName.size}")
    private String kanaName;
    // ニックネーム
    @Schema(description = "ニックネーム", example = "太郎")
    @Size(max = 50, message = "{studentSearchCondition.nickname.size}")
    private String nickname;
    // メール
    @Schema(description = "メールアドレス", example = "example@gmail.com")
    @Email(message = "{studentSearchCondition.email.invalid}")
    @Size(max = 50, message = "{studentSearchCondition.email.size}")
    private String email;
    // 地域
    @Schema(description = "居住地域", example = "東京")
    @Size(max = 50, message = "{studentSearchCondition.area.size}")
    private String area;
    // 年齢
    @Schema(description = "年齢", example = "30")
    @Min(value = 0, message = "{studentSearchCondition.age.min}")
    @Max(value = 100, message = "{studentSearchCondition.age.max}")
    private Integer age;
    //年齢検索するときの上限値
    @Schema(description = "年齢で検索する際上限の値を設定するときに使用", example = "30")
    @Min(value = 0, message = "{studentSearchCondition.age.min}")
    @Max(value = 100, message = "{studentSearchCondition.age.max}")
    private Integer maxAge;
    //年齢検索するときの上限値
    @Schema(description = "年齢で検索する際下限の値を設定するときに使用", example = "30")
    @Min(value = 0, message = "{studentSearchCondition.age.min}")
    @Max(value = 100, message = "{studentSearchCondition.age.max}")
    private Integer minAge;
    // 性別
    @Schema(description = "性別", example = "男性")
    @Size(max = 10, message = "{studentSearchCondition.sex.size}")
    private String sex;
    //コース名
    @Size(max = 50, message = "{studentCourse.courseName.size}")
    @Schema(description = "コース名", example = "Javaフルコース")
    private String courseName;
    // 申込状況
    @Size(max = 10, message = "{studentCourseStatus.status.size}")
    @Schema(description = "申込状況の内容", example = "受講中")
    private String status;
}
