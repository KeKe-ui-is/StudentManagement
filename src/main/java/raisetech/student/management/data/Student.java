package raisetech.student.management.data;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
    /*
      受講生情報を扱うオブジェクト
     */
public class Student {
    private String id;
    @Size(min = 1, max = 50)
    @NotBlank(message = "{student.name.required}")
    private String name;
    @Size(min = 1, max = 50)
    @NotBlank(message = "{student.kanaName.required}")
    private String kanaName;
    @Size(min = 1, max = 50)
    @NotBlank
    private String nickname;
    @Size(min = 1, max = 50)
    @Email(message = "{student.email.invalid}")
    @Size(max = 50)
    @NotBlank(message = "{student.email.required}")
    private String email;
    @Size(min = 1, max = 50)
    private String area;
    @Min(value = 0, message = "{student.age.min}")
    @Max(value = 100, message = "{student.age.max}")
    private int age;
    @Size(min = 1, max = 10)
    private String sex;
    @Size(max = 255)
    private String remark;
    @NotNull
    private boolean deleted;
}
