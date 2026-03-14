package rsisetech.student.management.data;

import lombok.Getter;
import lombok.Setter;

    @Getter
    @Setter
public class Student {
    private String name;
    private String id;
    private String kanaName;
    private String nickname;
    private String email;
    private String area;
    private int age;
    private String sex;
    //24課題下の2種類のフィールドに対応するレコードをStudentsテーブルに追加　追加はALTER TABLEを使う
    private String remark;
    private boolean deleted;
}
