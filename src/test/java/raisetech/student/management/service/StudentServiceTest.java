package raisetech.student.management.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private StudentRepository repository;
    @Mock
    private StudentConverter converter;

    private StudentService sut;

    @BeforeEach
    void before() {
        sut = new StudentService(repository, converter);
    }

    @Test
    void 受講生詳細の一覧検索＿リポジトリとコンバーターの処理が適切に呼び出せていること() {
        /**
         *     public List<StudentDetail> searchStudentList(){
         *         List<Student> studentList = repository.search();
         *         List<StudentCourse> studentCourseList = repository.searchStudentCourseList();
         *         return studentConverter.convertStudentDetails(studentList, studentCourseList);
         *     }
         */
        List<Student> studentList = new ArrayList<>();
        Student student = new Student();
        student.setId("999");
        student.setName("テスト太郎");
        studentList.add(student);
        List<StudentCourse> studentCourseList = new ArrayList<>();
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setStudentId("999");
        studentCourse.setId("998");
        studentCourse.setCourseName("テストコース");
        studentCourseList.add(studentCourse);

        StudentDetail studentDetail = new StudentDetail(student,studentCourseList);
        List<StudentDetail> expected = new ArrayList<>();
        expected.add(studentDetail);

        Mockito.when(repository.search()).thenReturn(studentList);
        Mockito.when(repository.searchStudentCourseList()).thenReturn(studentCourseList);
        Mockito.when(converter.convertStudentDetails(studentList, studentCourseList)).thenReturn(expected);

        List<StudentDetail> actual = sut.searchStudentList();

        Assertions.assertEquals(expected, actual);
        Mockito.verify(repository, Mockito.times(1)).search();
        Mockito.verify(repository, Mockito.times(1)).searchStudentCourseList();
        Mockito.verify(converter, Mockito.times(1)).convertStudentDetails(studentList, studentCourseList);
    }

    @Test
    void 受講生詳細の一覧検索＿リストが空でもリポジトリとコンバーターの処理が適切に呼び出せていること() {
        /**
         *     public List<StudentDetail> searchStudentList(){
         *         List<Student> studentList = repository.search();
         *         List<StudentCourse> studentCourseList = repository.searchStudentCourseList();
         *         return studentConverter.convertStudentDetails(studentList, studentCourseList);
         *     }
         */
        List<Student> studentList = new ArrayList<>();
        List<StudentCourse> studentCourseList = new ArrayList<>();
        List<StudentDetail> expected = new ArrayList<>();

        Mockito.when(repository.search()).thenReturn(studentList);
        Mockito.when(repository.searchStudentCourseList()).thenReturn(studentCourseList);
        Mockito.when(converter.convertStudentDetails(studentList, studentCourseList)).thenReturn(expected);

        List<StudentDetail> actual = sut.searchStudentList();

        Assertions.assertEquals(expected, actual);
        Mockito.verify(repository, Mockito.times(1)).search();
        Mockito.verify(repository, Mockito.times(1)).searchStudentCourseList();
        Mockito.verify(converter, Mockito.times(1)).convertStudentDetails(studentList, studentCourseList);
    }


    @Test
    void idを受け取り受講生詳細検索_リポジトリから受講生情報と受講生コース情報の検索ができていること() {
        /**
         *     元のコード
         *     public StudentDetail searchStudent(String id){
         *         Student student= repository.searchStudent(id);
         *         List<StudentCourse> studentCourse = repository.searchStudentCourse(student.getId());
         *         return new StudentDetail(student, studentCourse);
         *     }
         */
//        事前準備
        String id = "999";
        Student student = new Student();
        student.setId("999");
        student.setName("テスト三郎");

        List<StudentCourse> studentCourseList = new ArrayList<>();
        StudentCourse st1 = new StudentCourse();
        st1.setCourseName("Javaフルコース");
        st1.setStudentId(id);
        StudentCourse st2 = new StudentCourse();
        st2.setCourseName("AWSコース");
        st2.setStudentId(id);
        studentCourseList.add(st1);
        studentCourseList.add(st2);

        StudentDetail expected = new StudentDetail(student, studentCourseList);

//        レポジトリの管理
        Mockito.when(repository.searchStudent(id)).thenReturn(student);
        Mockito.when(repository.searchStudentCourse(student.getId())).thenReturn(studentCourseList);
//        実行
        StudentDetail actual = sut.searchStudent(id);
//        検証
        Assertions.assertEquals(expected.getStudent().getId(), actual.getStudent().getId());
        Assertions.assertEquals(expected.getStudent().getName(), actual.getStudent().getName());
        Assertions.assertEquals("999", actual.getStudent().getId());
        Assertions.assertEquals("999", actual.getStudentCourseList().get(0).getStudentId());
        Assertions.assertEquals("999", actual.getStudentCourseList().get(1).getStudentId());

        Mockito.verify(repository, Mockito.times(1)).searchStudent(id);
        Mockito.verify(repository, Mockito.times(1)).searchStudentCourse(student.getId());
    }

    //例外テスト 指定したidが存在しない場合　NullPointerExceptionが出る
    @Test
    void idを受け取り受講生詳細検索_存在しないidを指定したときエラーが返ること() {
        /**
         *     元のコード
         *     public StudentDetail searchStudent(String id){
         *         Student student= repository.searchStudent(id);
         *         List<StudentCourse> studentCourse = repository.searchStudentCourse(student.getId());
         *         return new StudentDetail(student, studentCourse);
         *     }
         */
//        事前準備
        String id = "999";
        Mockito.when(repository.searchStudent(id)).thenReturn(null);
//        実行
        NullPointerException e = assertThrows(NullPointerException.class, () -> {
            sut.searchStudent(id);
        });

//        検証
        Mockito.verify(repository, Mockito.times(1)).searchStudent(id);
        Mockito.verify(repository, Mockito.never()).searchStudentCourse(Mockito.any());


        System.out.println("例外クラス: " + e.getClass().getName());
        System.out.println("メッセージ: " + e.getMessage());
    }


    @Test
    void 受講生詳細情報の新規登録_受講生詳細情報が登録できておりIDや日付が入力されているか() {
        /**
         *      元のコード
         *     @Transactional
         *     public StudentDetail registerStudent(StudentDetail studentDetail){
         *         Student student = studentDetail.getStudent();
         *         repository.registerStudent(student);
         *         studentDetail.getStudentCourseList().forEach(studentsCourses -> {
         *             initStudentsCourses(student, studentsCourses);
         *             repository.registerStudentCourse(studentsCourses);
         *         });
         *         return studentDetail;
         *     }
         */

//        事前準備
        Student student = new Student();
        student.setName("テスト太郎");

        List<StudentCourse> studentCourseList = new ArrayList<>();
        StudentCourse st1 = new StudentCourse();
        st1.setCourseName("Javaフルコース");
        StudentCourse st2 = new StudentCourse();
        st2.setCourseName("AWSコース");
        studentCourseList.add(st1);
        studentCourseList.add(st2);

        StudentDetail expected = new StudentDetail(student, studentCourseList);

        Mockito.doAnswer(invocation -> {
            Student argStudent = invocation.getArgument(0);
            argStudent.setId("999");
            return null;
        }).when(repository).registerStudent(Mockito.any(Student.class));

//        実行
        StudentDetail actual = sut.registerStudent(expected);
//        確認する
        Assertions.assertSame(expected, actual);
        Mockito.verify(repository, Mockito.times(1)).registerStudent(student);
        Mockito.verify(repository, Mockito.times(2)).registerStudentCourse(Mockito.any(StudentCourse.class));
//        StudentCourseにidが入っているか
        Assertions.assertEquals("999", st1.getStudentId());
        Assertions.assertEquals("999", st2.getStudentId());
//        日時が入っているか
        Assertions.assertNotNull(st1.getCourseStartAt());
        Assertions.assertNotNull(st1.getCourseEndAt());
        Assertions.assertNotNull(st2.getCourseStartAt());
        Assertions.assertNotNull(st2.getCourseEndAt());
    }

    @Test
    void 受講生詳細情報の新規登録_受講生詳細情報が登録できておりIDが入力されるか_受講生コース情報が0件の場合() {
//        事前準備
        String id = "999";
        Student student = new Student();
        student.setName("テスト花子");

        List<StudentCourse> studentCourseList = new ArrayList<>();
        StudentDetail studentDetail = new StudentDetail(student, studentCourseList);
        StudentDetail expected = studentDetail;

        Mockito.doAnswer(invocation -> {
            Student argStudent = invocation.getArgument(0);
            argStudent.setId(id);
            return null;
        }).when(repository).registerStudent(Mockito.any(Student.class));
//        実行
        StudentDetail actual = sut.registerStudent(studentDetail);
//        確認
        Assertions.assertEquals("999", student.getId());
        Assertions.assertEquals(expected, actual);
        Mockito.verify(repository, Mockito.times(1)).registerStudent(student);
        Mockito.verify(repository, Mockito.times(0)).registerStudentCourse(Mockito.any(StudentCourse.class));
    }

    @Test
    void 受講生詳細情報の更新処理_リポジトリが呼び出されているか() {
        /**
         *     @Transactional
         *     public void updateStudent(StudentDetail studentDetail){
         *         repository.updateStudent(studentDetail.getStudent());
         *         studentDetail.getStudentCourseList()
         *                 .forEach(studentCourse -> {repository.updateStudentCourse(studentCourse);
         *         });
         *     }
         */
//        準備
        Student student = new Student();
        student.setName("テスト花子");
        student.setId("999");

        List<StudentCourse> studentCourseList = new ArrayList<>();
        StudentCourse st1 = new StudentCourse();
        st1.setCourseName("Javaフルコース");
        st1.setId("999");
        StudentCourse st2 = new StudentCourse();
        st2.setCourseName("AWSコース");
        st2.setId("998");
        studentCourseList.add(st1);
        studentCourseList.add(st2);

        StudentDetail studentDetail = new StudentDetail(student, studentCourseList);
//        実行
        sut.updateStudent(studentDetail);
//        検証
        Mockito.verify(repository, Mockito.times(1)).updateStudent(student);
        Mockito.verify(repository, Mockito.times(2)).updateStudentCourse(Mockito.any(StudentCourse.class));

    }

//    @Test
//    void 受講生コース情報の初期化_id_開始日_修了予定日の処理が呼び出されているか() {
//        /**
//         *      元のコード
//         *     public void initStudentsCourses(Student student, StudentCourse studentCourse) {
//         *         LocalDateTime now = LocalDateTime.now();
//         *         studentCourse.setStudentId(student.getId());
//         *         studentCourse.setCourseStartAt(now);
//         *         studentCourse.setCourseEndAt(now.plusYears(1));
//         *     }
//         */
////        事前準備
//        Student student = new Student();
//        student.setId("999");
//
//        LocalDateTime now = LocalDateTime.now();
//        StudentCourse studentCourse = new StudentCourse();
//        studentCourse.setCourseStartAt(now);
//        studentCourse.setCourseEndAt(now);
////        実行
//        sut.initStudentsCourses(student,studentCourse);
////        検証
//        Assertions.assertEquals(student.getId(),studentCourse.getStudentId());
//
//        Assertions.assertEquals(now.getYear(),studentCourse.getCourseStartAt().getYear());
//        Assertions.assertEquals(now.getMonth(),studentCourse.getCourseStartAt().getMonth());
//        Assertions.assertEquals(now.getDayOfMonth(),studentCourse.getCourseStartAt().getDayOfMonth());
//
//        Assertions.assertEquals(now.plusYears(1).getYear(),studentCourse.getCourseEndAt().getYear());
//        Assertions.assertEquals(now.getMonth(),studentCourse.getCourseEndAt().getMonth());
//        Assertions.assertEquals(now.getDayOfMonth(),studentCourse.getCourseEndAt().getDayOfMonth());
//
//    }
}
