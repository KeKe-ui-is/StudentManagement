package raisetech.student.management.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.StudentCourseStatus;
import raisetech.student.management.data.StudentSearchCondition;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 受講生情報を取り扱うサービスです。
 * 検索登録更新などを行います。
 */
@Service
public class StudentService {

    private StudentRepository repository;
    private StudentConverter studentConverter;

    /**
     * コンストラクタ
     * @param repository データベースアクセス
     * @param studentConverter　コンバーター
     */
    @Autowired
    public StudentService(StudentRepository repository,StudentConverter studentConverter) {
        this.repository = repository;
        this.studentConverter = studentConverter;
    }

    /**
     * 受講生詳細の全件検索を行います。
     * 全件検索を行うので条件指定はありません。
     * @return 受講生情報一覧
     */
    public List<StudentDetail> searchStudentList(){
        List<Student> studentList = repository.search();
        List<StudentCourse> studentCourseList = repository.searchStudentCourseList();
        List<StudentCourseStatus> studentCourseStatusList = repository.searchStatusList();
        studentConverter.convertStudentCourseStatus(studentCourseList,studentCourseStatusList);
        return studentConverter.convertStudentDetails(studentList,studentCourseList);
    }
    /**
     * 受講生コース情報に申込状況をセットした受講生コース情報が返る
     * @return StudentCourse 受講生コース情報
     */
    public List<StudentCourse> searchStudentCourseList(){
        List<StudentCourse> studentCourseList = repository.searchStudentCourseList();
        List<StudentCourseStatus> studentCourseStatusList = repository.searchStatusList();
        return studentConverter.convertStudentCourseStatus(studentCourseList,studentCourseStatusList);
        }

    /**
     * 指定されたidに紐づく受講生の情報を返します。
     * またそれに関連する受講生コース情報・申込状況も含みます
     * @param id 受講生ID
     * @return 受講生情報
     */
    public StudentDetail searchStudent(Integer id){
        Student student= repository.searchStudent(id);
        List<StudentCourse> studentCourse = repository.searchStudentCourse(student.getId());
        studentCourse.forEach(course ->{
            course.setStudentCourseStatus(repository.searchStudentCourseStatus(course.getId()));
        });
        return new StudentDetail(student, studentCourse);
    }

    /**
     * 受講生情報の複数条件検索
     * 検索条件から受講生の情報とそれに紐づく受講コース情報や申込状況を取得
     * コース名や申込状況があれば取得した受講生にあてはまるものだけ残して行く
     * @param studentSearchCondition 検索条件が入っている
     * @return 受講生情報
     */
    public List<StudentDetail> searchStudentMultipleCondition(StudentSearchCondition studentSearchCondition){
        List<Student> studentList = repository.searchStudentMultipleCondition(studentSearchCondition);
        List<StudentCourse> studentCourseList = repository.searchStudentCourseList();
        List<StudentCourseStatus> studentCourseStatusList = repository.searchStatusList();
        studentConverter.convertStudentCourseStatus(studentCourseList,studentCourseStatusList);
        List<StudentDetail> studentDetails = studentConverter.convertStudentDetails(studentList, studentCourseList);
        studentDetails = studentConverter.filterByCourseName(studentDetails,studentSearchCondition.getCourseName());
        studentDetails = studentConverter.filterByStatus(studentDetails,studentSearchCondition.getStatus());
        return studentDetails;
    }

    /**
     * 受講生詳細を受け取る
     * 受講生情報をstudetnsテーブルに登録処理
     * 受講生コースをListで受け取っているのでfor文を回して１つずつ受講生IDと紐づけながら登録処理
     *  受講生と受講生コース情報を個別に登録し、受講生コース情報には受講生コースと紐づけるための値を設定
     *  受講生コース情報に紐づける値や開始日修了予定日などの日付情報を設定
     *  申込状況の登録は新規登録の場合はstatusは仮申込に設定
     *  申込状況のidは自動採番されsetStudentCourseStatusメソッドにより受講コース情報に申込状況がセットされる
     *  @param studentDetail　受講生情報詳細
     * @return 登録情報を付与した受講生詳細
     */
    @Transactional
    public StudentDetail registerStudent(StudentDetail studentDetail){
        String registerStatusText = "仮申込";
        Student student = studentDetail.getStudent();
        repository.registerStudent(student);
        studentDetail.getStudentCourseList().forEach(studentsCourses -> {
            initStudentsCourses(student, studentsCourses);
            repository.registerStudentCourse(studentsCourses);
            setStudentCourseStatus(studentsCourses,registerStatusText);
            repository.registerStudentCourseStatus(studentsCourses.getStudentCourseStatus());
        });
        return studentDetail;
    }

    /**
     * 受講生情報からIDを取り出し受講生コース情報の受講生IDにセットします。
     * 受講生コースにある受講開始日と修了予定日もセットします
     * @param student 受講生情報
     * @param studentCourse　受講生コース
     */
    private void initStudentsCourses(Student student, StudentCourse studentCourse) {
        LocalDateTime now = LocalDateTime.now();
        studentCourse.setStudentId(student.getId());
        studentCourse.setCourseStartAt(now);
        studentCourse.setCourseEndAt(now.plusYears(1));
    }

    /**
     * 受講コース情報を受け取りそこから必要なidを取り出し申込状況を作成するメソッド
     * @param studentCourse 申込状況
     * @param statusText 申込状況の内容に設定するメッセージ
     */
    private void setStudentCourseStatus(StudentCourse studentCourse, String statusText){
        StudentCourseStatus studentCourseStatus = new StudentCourseStatus();
        studentCourseStatus.setStudentCourseId(studentCourse.getId());
        studentCourseStatus.setStatus(statusText);
        studentCourse.setStudentCourseStatus(studentCourseStatus);
    }

    /**
     * 受講生詳細の更新
     * 受講生情報と受講生コース情報の更新を行う
     * @param studentDetail 受講生詳細
     */
    @Transactional
    public void updateStudent(StudentDetail studentDetail){
        repository.updateStudent(studentDetail.getStudent());
        studentDetail.getStudentCourseList()
                .forEach(studentCourse -> {
                    repository.updateStudentCourse(studentCourse);
                    repository.updateStudentCourseStatus(studentCourse.getStudentCourseStatus());
        });
    }

}
