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
        @Size(min = 1,max = 50)
        private String name;
        @Size(min = 1,max = 50) @NotNull
        private String kanaName;
        @Size(min = 1,max = 50) @NotNull
        private String nickname;
        @Size(min = 1,max = 50) @Email
        private String email;
        @Size(min = 1,max = 50)
        private String area;
        @Min(value = 0) @Max(value = 100)
        private int age;
        @Size(min = 1,max = 10)
        private String sex;
        @Size(max = 255)
        private String remark;
        @NotNull
        private boolean deleted;
}
