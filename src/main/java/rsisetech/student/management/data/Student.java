package rsisetech.student.management.data;

import lombok.Getter;
import lombok.Setter;

    @Getter
    @Setter
    /*
      受講生情報を扱うオブジェクト
     */
    public class Student {
    private String name;
    private String id;
    private String kanaName;
    private String nickname;
    private String email;
    private String area;
    private int age;
    private String sex;
    private String remark;
    private boolean deleted;
}
