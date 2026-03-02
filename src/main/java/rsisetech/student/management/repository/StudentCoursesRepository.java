package rsisetech.student.management.repository;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import rsisetech.student.management.data.StudentsCourses;

import java.util.List;
/**
*受講生情報を扱うリポジトリ
* 全件検索や単一条件での検索、コース情報の検索が行えるクラス
 */
@Mapper
public interface StudentCoursesRepository {
    /**
     *
     * @return 受講生情報を全件検索した一覧
     */
    @Select("SELECT * FROM students_courses")
    List<StudentsCourses> search();
}
