package raisetech.student.management.service;

import org.apache.commons.lang3.builder.DiffExclude;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import raisetech.student.management.controller.converter.StudentConverter;
import raisetech.student.management.data.Student;
import raisetech.student.management.data.StudentCourse;
import raisetech.student.management.data.StudentCourseStatus;
import raisetech.student.management.data.StudentSearchCondition;
import raisetech.student.management.domain.StudentDetail;
import raisetech.student.management.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
    @DisplayName("受講生詳細の一覧検索_リポジトリとコンバーターの処理が適切に呼び出せていること")
    void searchStudentList_dataExists_callsRepositoryAndConverter() {
        /**
         *     public List<StudentDetail> searchStudentList(){
         *         List<Student> studentList = repository.search();
         *         List<StudentCourse> studentCourseList = repository.searchStudentCourseList();
         *         List<StudentCourseStatus> studentCourseStatusList = repository.searchStatusList();
         *         studentConverter.setStudentCourseStatus(studentCourseList,studentCourseStatusList);
         *         return studentConverter.convertStudentDetails(studentList,studentCourseList);
         *     }
         */
        List<Student> studentList = new ArrayList<>();
        Student student = createStudent();
        studentList.add(student);

        List<StudentCourseStatus> studentCourseStatusList = new ArrayList<>();
        StudentCourseStatus studentCourseStatus = createStatus();
        studentCourseStatusList.add(studentCourseStatus);

        List<StudentCourse> studentCourseList = new ArrayList<>();
        StudentCourse studentCourse = createStudentCourse();
        studentCourse.setStudentCourseStatus(studentCourseStatus);
        studentCourseList.add(studentCourse);

        StudentDetail studentDetail = new StudentDetail(student,studentCourseList);
        List<StudentDetail> expected = new ArrayList<>();
        expected.add(studentDetail);

        Mockito.when(repository.search()).thenReturn(studentList);
        Mockito.when(repository.searchStudentCourseList()).thenReturn(studentCourseList);
        Mockito.when(repository.searchStatusList()).thenReturn(studentCourseStatusList);
        Mockito.when(converter.convertStudentCourseStatus(studentCourseList,studentCourseStatusList)).thenReturn(studentCourseList);
        Mockito.when(converter.convertStudentDetails(studentList, studentCourseList)).thenReturn(expected);

        List<StudentDetail> actual = sut.searchStudentList();

        Assertions.assertEquals(expected, actual);
        Mockito.verify(repository, Mockito.times(1)).search();
        Mockito.verify(repository, Mockito.times(1)).searchStudentCourseList();
        Mockito.verify(repository, Mockito.times(1)).searchStatusList();
        Mockito.verify(converter, Mockito.times(1)).convertStudentCourseStatus(studentCourseList,studentCourseStatusList);
        Mockito.verify(converter, Mockito.times(1)).convertStudentDetails(studentList, studentCourseList);
    }

    @Test
    @DisplayName("受講生詳細の一覧検索_リストが空でもリポジトリとコンバーターの処理が適切に呼び出せていること")
    void searchStudentList_listIsEmpty_callsRepositoryAndConverter() {

        List<Student> studentList = new ArrayList<>();
        List<StudentCourse> studentCourseList = new ArrayList<>();
        List<StudentCourseStatus> studentCourseStatusList = new ArrayList<>();
        List<StudentDetail> expected = new ArrayList<>();

        Mockito.when(repository.search()).thenReturn(studentList);
        Mockito.when(repository.searchStudentCourseList()).thenReturn(studentCourseList);
        Mockito.when(repository.searchStatusList()).thenReturn(studentCourseStatusList);
        Mockito.when(converter.convertStudentCourseStatus(studentCourseList,studentCourseStatusList)).thenReturn(studentCourseList);
        Mockito.when(converter.convertStudentDetails(studentList, studentCourseList)).thenReturn(expected);

        List<StudentDetail> actual = sut.searchStudentList();

        Assertions.assertEquals(expected, actual);
        Mockito.verify(repository, Mockito.times(1)).search();
        Mockito.verify(repository, Mockito.times(1)).searchStudentCourseList();
        Mockito.verify(repository, Mockito.times(1)).searchStatusList();
        Mockito.verify(converter, Mockito.times(1)).convertStudentCourseStatus(studentCourseList,studentCourseStatusList);
        Mockito.verify(converter, Mockito.times(1)).convertStudentDetails(studentList, studentCourseList);
    }

    @Test
    @DisplayName("idを受け取り受講生詳細検索_リポジトリから受講生情報と受講生コース情報とそれに紐づく申込状況の検索ができていること")
    void searchStudent_IdIsExist_returnsStudentDetailWithCourseAndStatus() {
        /**
         *     元のコード
         *         public StudentDetail searchStudent(Integer id){
         *         Student student= repository.searchStudent(id);
         *         List<StudentCourse> studentCourse = repository.searchStudentCourse(student.getId());
         *         studentCourse.forEach(course ->{
         *             course.setStudentCourseStatus(repository.searchStudentCourseStatus(course.getId()));
         *         });
         *         return new StudentDetail(student, studentCourse);
         *     }
         */
//        事前準備
        Student student = createStudent();
        Integer id = student.getId();

        StudentCourseStatus studentCourseStatus = createStatus();

        List<StudentCourse> studentCourseList = new ArrayList<>();
        StudentCourse studentCourse = createStudentCourse();
        studentCourse.setStudentId(id);
        studentCourseList.add(studentCourse);

        StudentDetail expected = new StudentDetail(student, studentCourseList);

//        レポジトリの管理
        Mockito.when(repository.searchStudent(id)).thenReturn(student);
        Mockito.when(repository.searchStudentCourse(student.getId())).thenReturn(studentCourseList);
        Mockito.when(repository.searchStudentCourseStatus(studentCourse.getId())).thenReturn(studentCourseStatus);
//        実行
        StudentDetail actual = sut.searchStudent(id);
//        検証
        Assertions.assertEquals(id, actual.getStudent().getId());
        Assertions.assertEquals(student.getName(), actual.getStudent().getName());

        Assertions.assertEquals(1, actual.getStudentCourseList().size());
        Assertions.assertEquals(studentCourse.getStudentId(), actual.getStudentCourseList().get(0).getStudentId());
        Assertions.assertEquals(studentCourse.getCourseName(), actual.getStudentCourseList().get(0).getCourseName());

        Assertions.assertNotNull(actual.getStudentCourseList().get(0).getStudentCourseStatus());
        Assertions.assertEquals(studentCourseStatus.getStatus(),
                actual.getStudentCourseList().get(0).getStudentCourseStatus().getStatus());

        Mockito.verify(repository, Mockito.times(1)).searchStudent(id);
        Mockito.verify(repository, Mockito.times(1)).searchStudentCourse(student.getId());
        Mockito.verify(repository, Mockito.times(1)).searchStudentCourseStatus(studentCourse.getId());
    }

    //例外テスト 指定したidが存在しない場合　NullPointerExceptionが出る
    @Test
    @DisplayName("idを受け取り受講生詳細検索_存在しないidを指定したときNullPointerExceptionが返ること")
    void searchStudent_doesNotExistId_throwNullPointerException() {
//        事前準備
        Integer id = 999;
        Mockito.when(repository.searchStudent(id)).thenReturn(null);
//        実行
        NullPointerException e = assertThrows(NullPointerException.class, () -> {
            sut.searchStudent(id);
        });

//        検証
        Mockito.verify(repository, Mockito.times(1)).searchStudent(id);
        Mockito.verify(repository, Mockito.never()).searchStudentCourse(Mockito.any());

    }

    @Test
    @DisplayName("受講生の複数条件検索_複数条件を指定したとき検索ができていること")
    void searchStudentMultipleCondition_setMultipleCondition_callsRepositoryAndConverter(){
        /**
         * 元のコード
         *     public List<StudentDetail> searchStudentMultipleCondition(StudentSearchCondition studentSearchCondition){
         *         List<Student> studentList = repository.searchStudentMultipleCondition(studentSearchCondition);
         *         List<StudentCourse> studentCourseList = repository.searchStudentCourseList();
         *         List<StudentCourseStatus> studentCourseStatusList = repository.searchStatusList();
         *         studentConverter.convertStudentCourseStatus(studentCourseList,studentCourseStatusList);
         *         List<StudentDetail> studentDetails = studentConverter.convertStudentDetails(studentList, studentCourseList);
         *         studentDetails = studentConverter.filterByCourseName(studentDetails,studentSearchCondition.getCourseName());
         *         studentDetails = studentConverter.filterByStatus(studentDetails,studentSearchCondition.getStatus());
         *         return studentDetails;
         *     }
         */
        List<Student> studentList = new ArrayList<>();
        List<StudentCourse> studentCourseList = new ArrayList<>();
        List<StudentCourseStatus> studentCourseStatusList = new ArrayList<>();

        Student student = createStudent();
        StudentCourse studentCourse = createStudentCourse();
        StudentCourseStatus studentCourseStatus = createStatus();

        studentList.add(student);
        studentCourseList.add(studentCourse);
        studentCourseStatusList.add(studentCourseStatus);

        StudentSearchCondition studentSearchCondition = new StudentSearchCondition();
        studentSearchCondition.setId(999);
        studentSearchCondition.setName("テスト太郎");
        studentSearchCondition.setCourseName("テストコース");
        studentSearchCondition.setStatus("受講中");

        StudentDetail studentDetail = new StudentDetail(student,studentCourseList);
        studentDetail.getStudentCourseList().get(0).setStudentCourseStatus(studentCourseStatus);
        List<StudentDetail> studentDetailList = new ArrayList<>();
        studentDetailList.add(studentDetail);

        Mockito.when(repository.searchStudentMultipleCondition(studentSearchCondition)).thenReturn(studentList);
        Mockito.when(repository.searchStudentCourseList()).thenReturn(studentCourseList);
        Mockito.when(repository.searchStatusList()).thenReturn(studentCourseStatusList);
        Mockito.when(converter.convertStudentDetails(studentList,studentCourseList)).thenReturn(studentDetailList);
        Mockito.when(converter.convertStudentCourseStatus(studentCourseList, studentCourseStatusList)).thenReturn(studentCourseList);
        Mockito.when(converter.filterByCourseName(studentDetailList,studentSearchCondition.getCourseName())).thenReturn(studentDetailList);
        Mockito.when(converter.filterByStatus(studentDetailList,studentSearchCondition.getStatus())).thenReturn(studentDetailList);

        List<StudentDetail> actual = sut.searchStudentMultipleCondition(studentSearchCondition);

        Mockito.verify(repository, Mockito.times(1)).searchStudentMultipleCondition(studentSearchCondition);
        Mockito.verify(repository, Mockito.times(1)).searchStudentCourseList();
        Mockito.verify(repository, Mockito.times(1)).searchStatusList();

        Mockito.verify(converter, Mockito.times(1)).convertStudentCourseStatus(studentCourseList,studentCourseStatusList);
        Mockito.verify(converter, Mockito.times(1)).convertStudentDetails(studentList,studentCourseList);
        Mockito.verify(converter, Mockito.times(1)).filterByCourseName(studentDetailList,studentSearchCondition.getCourseName());
        Mockito.verify(converter, Mockito.times(1)).filterByStatus(studentDetailList,studentSearchCondition.getStatus());

        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(1, actual.get(0).getStudentCourseList().size());
        Assertions.assertEquals(student.getId(),actual.get(0).getStudent().getId());
        Assertions.assertEquals(student.getName(),actual.get(0).getStudent().getName());
        Assertions.assertEquals(studentCourse.getCourseName(),actual.get(0).getStudentCourseList().get(0).getCourseName());
        Assertions.assertEquals(studentCourseStatus.getStatus(),actual.get(0).getStudentCourseList().get(0).getStudentCourseStatus().getStatus());
    }

    @Test
    @DisplayName("受講生の複数条件検索_受講生のみ複数条件を指定したとき検索ができていること")
    void searchStudentMultipleCondition_setMultipleConditionOnlyStudent_callsRepositoryAndConverter(){
        List<Student> studentList = new ArrayList<>();
        List<StudentCourse> studentCourseList = new ArrayList<>();
        List<StudentCourseStatus> studentCourseStatusList = new ArrayList<>();

        Student student = createStudent();
        StudentCourse studentCourse = createStudentCourse();
        StudentCourseStatus studentCourseStatus = createStatus();

        studentList.add(student);
        studentCourseList.add(studentCourse);
        studentCourseStatusList.add(studentCourseStatus);

        StudentSearchCondition studentSearchCondition = new StudentSearchCondition();
        studentSearchCondition.setId(999);
        studentSearchCondition.setName("テスト太郎");
        studentSearchCondition.setCourseName("");
        studentSearchCondition.setStatus("");

        StudentDetail studentDetail = new StudentDetail(student,studentCourseList);
        studentDetail.getStudentCourseList().get(0).setStudentCourseStatus(studentCourseStatus);
        List<StudentDetail> studentDetailList = new ArrayList<>();
        studentDetailList.add(studentDetail);

        Mockito.when(repository.searchStudentMultipleCondition(studentSearchCondition)).thenReturn(studentList);
        Mockito.when(repository.searchStudentCourseList()).thenReturn(studentCourseList);
        Mockito.when(repository.searchStatusList()).thenReturn(studentCourseStatusList);
        Mockito.when(converter.convertStudentDetails(studentList,studentCourseList)).thenReturn(studentDetailList);
        Mockito.when(converter.convertStudentCourseStatus(studentCourseList, studentCourseStatusList)).thenReturn(studentCourseList);
        Mockito.when(converter.filterByCourseName(studentDetailList,studentSearchCondition.getCourseName())).thenReturn(studentDetailList);
        Mockito.when(converter.filterByStatus(studentDetailList,studentSearchCondition.getStatus())).thenReturn(studentDetailList);

        List<StudentDetail> actual = sut.searchStudentMultipleCondition(studentSearchCondition);

        Mockito.verify(repository, Mockito.times(1)).searchStudentMultipleCondition(studentSearchCondition);
        Mockito.verify(repository, Mockito.times(1)).searchStudentCourseList();
        Mockito.verify(repository, Mockito.times(1)).searchStatusList();

        Mockito.verify(converter, Mockito.times(1)).convertStudentCourseStatus(studentCourseList,studentCourseStatusList);
        Mockito.verify(converter, Mockito.times(1)).convertStudentDetails(studentList,studentCourseList);
        Mockito.verify(converter, Mockito.times(1)).filterByCourseName(studentDetailList,studentSearchCondition.getCourseName());
        Mockito.verify(converter, Mockito.times(1)).filterByStatus(studentDetailList,studentSearchCondition.getStatus());

        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(1, actual.get(0).getStudentCourseList().size());
        Assertions.assertEquals(student.getId(),actual.get(0).getStudent().getId());
        Assertions.assertEquals(student.getName(),actual.get(0).getStudent().getName());
        Assertions.assertEquals(studentCourse.getCourseName(),actual.get(0).getStudentCourseList().get(0).getCourseName());
        Assertions.assertEquals(studentCourseStatus.getStatus(),actual.get(0).getStudentCourseList().get(0).getStudentCourseStatus().getStatus());
    }

    @Test
    @DisplayName("受講生の複数条件検索_受講コース名のみ指定したとき検索ができていること")
    void searchStudentMultipleCondition_setOnlyCourseName_callsRepositoryAndConverter(){
        List<Student> studentList = new ArrayList<>();
        List<StudentCourse> studentCourseList = new ArrayList<>();
        List<StudentCourseStatus> studentCourseStatusList = new ArrayList<>();

        Student student = createStudent();
        StudentCourse studentCourse = createStudentCourse();
        StudentCourseStatus studentCourseStatus = createStatus();

        studentList.add(student);
        studentCourseList.add(studentCourse);
        studentCourseStatusList.add(studentCourseStatus);

        StudentSearchCondition studentSearchCondition = new StudentSearchCondition();
        studentSearchCondition.setId(null);
        studentSearchCondition.setName("");
        studentSearchCondition.setCourseName("テストコース");
        studentSearchCondition.setStatus("");

        StudentDetail studentDetail = new StudentDetail(student,studentCourseList);
        studentDetail.getStudentCourseList().get(0).setStudentCourseStatus(studentCourseStatus);
        List<StudentDetail> studentDetailList = new ArrayList<>();
        studentDetailList.add(studentDetail);

        Mockito.when(repository.searchStudentMultipleCondition(studentSearchCondition)).thenReturn(studentList);
        Mockito.when(repository.searchStudentCourseList()).thenReturn(studentCourseList);
        Mockito.when(repository.searchStatusList()).thenReturn(studentCourseStatusList);
        Mockito.when(converter.convertStudentDetails(studentList,studentCourseList)).thenReturn(studentDetailList);
        Mockito.when(converter.convertStudentCourseStatus(studentCourseList, studentCourseStatusList)).thenReturn(studentCourseList);
        Mockito.when(converter.filterByCourseName(studentDetailList,studentSearchCondition.getCourseName())).thenReturn(studentDetailList);
        Mockito.when(converter.filterByStatus(studentDetailList,studentSearchCondition.getStatus())).thenReturn(studentDetailList);

        List<StudentDetail> actual = sut.searchStudentMultipleCondition(studentSearchCondition);

        Mockito.verify(repository, Mockito.times(1)).searchStudentMultipleCondition(studentSearchCondition);
        Mockito.verify(repository, Mockito.times(1)).searchStudentCourseList();
        Mockito.verify(repository, Mockito.times(1)).searchStatusList();

        Mockito.verify(converter, Mockito.times(1)).convertStudentCourseStatus(studentCourseList,studentCourseStatusList);
        Mockito.verify(converter, Mockito.times(1)).convertStudentDetails(studentList,studentCourseList);
        Mockito.verify(converter, Mockito.times(1)).filterByCourseName(studentDetailList,studentSearchCondition.getCourseName());
        Mockito.verify(converter, Mockito.times(1)).filterByStatus(studentDetailList,studentSearchCondition.getStatus());

        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(1, actual.get(0).getStudentCourseList().size());
        Assertions.assertEquals(student.getId(),actual.get(0).getStudent().getId());
        Assertions.assertEquals(student.getName(),actual.get(0).getStudent().getName());
        Assertions.assertEquals(studentCourse.getCourseName(),actual.get(0).getStudentCourseList().get(0).getCourseName());
        Assertions.assertEquals(studentCourseStatus.getStatus(),actual.get(0).getStudentCourseList().get(0).getStudentCourseStatus().getStatus());
    }

    @Test
    @DisplayName("受講生の複数条件検索_申込状況のみ指定したとき検索ができていること")
    void searchStudentMultipleCondition_setOnlyStatus_callsRepositoryAndConverter(){
        List<Student> studentList = new ArrayList<>();
        List<StudentCourse> studentCourseList = new ArrayList<>();
        List<StudentCourseStatus> studentCourseStatusList = new ArrayList<>();

        Student student = createStudent();
        StudentCourse studentCourse = createStudentCourse();
        StudentCourseStatus studentCourseStatus = createStatus();

        studentList.add(student);
        studentCourseList.add(studentCourse);
        studentCourseStatusList.add(studentCourseStatus);

        StudentSearchCondition studentSearchCondition = new StudentSearchCondition();
        studentSearchCondition.setId(null);
        studentSearchCondition.setName("");
        studentSearchCondition.setCourseName("");
        studentSearchCondition.setStatus("受講中");

        StudentDetail studentDetail = new StudentDetail(student,studentCourseList);
        studentDetail.getStudentCourseList().get(0).setStudentCourseStatus(studentCourseStatus);
        List<StudentDetail> studentDetailList = new ArrayList<>();
        studentDetailList.add(studentDetail);

        Mockito.when(repository.searchStudentMultipleCondition(studentSearchCondition)).thenReturn(studentList);
        Mockito.when(repository.searchStudentCourseList()).thenReturn(studentCourseList);
        Mockito.when(repository.searchStatusList()).thenReturn(studentCourseStatusList);
        Mockito.when(converter.convertStudentDetails(studentList,studentCourseList)).thenReturn(studentDetailList);
        Mockito.when(converter.convertStudentCourseStatus(studentCourseList, studentCourseStatusList)).thenReturn(studentCourseList);
        Mockito.when(converter.filterByCourseName(studentDetailList,studentSearchCondition.getCourseName())).thenReturn(studentDetailList);
        Mockito.when(converter.filterByStatus(studentDetailList,studentSearchCondition.getStatus())).thenReturn(studentDetailList);

        List<StudentDetail> actual = sut.searchStudentMultipleCondition(studentSearchCondition);

        Mockito.verify(repository, Mockito.times(1)).searchStudentMultipleCondition(studentSearchCondition);
        Mockito.verify(repository, Mockito.times(1)).searchStudentCourseList();
        Mockito.verify(repository, Mockito.times(1)).searchStatusList();

        Mockito.verify(converter, Mockito.times(1)).convertStudentCourseStatus(studentCourseList,studentCourseStatusList);
        Mockito.verify(converter, Mockito.times(1)).convertStudentDetails(studentList,studentCourseList);
        Mockito.verify(converter, Mockito.times(1)).filterByCourseName(studentDetailList,studentSearchCondition.getCourseName());
        Mockito.verify(converter, Mockito.times(1)).filterByStatus(studentDetailList,studentSearchCondition.getStatus());

        Assertions.assertEquals(1, actual.size());
        Assertions.assertEquals(1, actual.get(0).getStudentCourseList().size());
        Assertions.assertEquals(student.getId(),actual.get(0).getStudent().getId());
        Assertions.assertEquals(student.getName(),actual.get(0).getStudent().getName());
        Assertions.assertEquals(studentCourse.getCourseName(),actual.get(0).getStudentCourseList().get(0).getCourseName());
        Assertions.assertEquals(studentCourseStatus.getStatus(),actual.get(0).getStudentCourseList().get(0).getStudentCourseStatus().getStatus());
    }

    @Test
    @DisplayName("受講生詳細情報の新規登録_受講生詳細情報が登録できておりIDや日付が入力されかつ申込状況が受講コースと紐づいているか")
    void registerStudent_courseListExists_setIdsDatesAndCourseStatus() {
        /**
         *     public StudentDetail registerStudent(StudentDetail studentDetail){
         *         String registerStatusText = "仮申込";
         *         Student student = studentDetail.getStudent();
         *         repository.registerStudent(student);
         *         studentDetail.getStudentCourseList().forEach(studentsCourses -> {
         *             initStudentsCourses(student, studentsCourses);
         *             repository.registerStudentCourse(studentsCourses);
         *             setStudentCourseStatus(studentsCourses,registerStatusText);
         *             repository.registerStudentCourseStatus(studentsCourses.getStudentCourseStatus());
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
            argStudent.setId(999);
            return null;
        }).when(repository).registerStudent(Mockito.any(Student.class));

        AtomicInteger courseId = new AtomicInteger(1);
        Mockito.doAnswer(invocation ->{
            StudentCourse argCourse = invocation.getArgument(0);
            argCourse.setId(courseId.getAndIncrement());
            return null;
        }).when(repository).registerStudentCourse(Mockito.any(StudentCourse.class));


//        実行
        StudentDetail actual = sut.registerStudent(expected);
//        確認する
        Assertions.assertSame(expected, actual);
        Assertions.assertEquals(999, actual.getStudent().getId());

        Mockito.verify(repository, Mockito.times(1)).registerStudent(student);
        Mockito.verify(repository, Mockito.times(2)).registerStudentCourse(Mockito.any(StudentCourse.class));
        Mockito.verify(repository, Mockito.times(2)).registerStudentCourseStatus(Mockito.any(StudentCourseStatus.class));

        Assertions.assertEquals(1, actual.getStudentCourseList().get(0).getId());
        Assertions.assertEquals(2, actual.getStudentCourseList().get(1).getId());
        Assertions.assertEquals(999, actual.getStudentCourseList().get(0).getStudentId());
        Assertions.assertEquals(999, actual.getStudentCourseList().get(1).getStudentId());
        Assertions.assertNotNull(st1.getCourseStartAt());
        Assertions.assertNotNull(st1.getCourseEndAt());
        Assertions.assertNotNull(st2.getCourseStartAt());
        Assertions.assertNotNull(st2.getCourseEndAt());

        Assertions.assertEquals(st1.getId(),actual.getStudentCourseList().get(0).getStudentCourseStatus().getStudentCourseId());
        Assertions.assertEquals(st2.getId(),actual.getStudentCourseList().get(1).getStudentCourseStatus().getStudentCourseId());
        Assertions.assertEquals("仮申込",actual.getStudentCourseList().get(0).getStudentCourseStatus().getStatus());
        Assertions.assertEquals("仮申込",actual.getStudentCourseList().get(1).getStudentCourseStatus().getStatus());
    }

    @Test
    @DisplayName("受講生の複数条件検索_存在しない複数条件を指定したとき空のリストが返って来ること")
    void searchStudentMultipleCondition_doesNotExistCondition_returnEmptyList(){

        List<StudentCourse> studentCourseList = new ArrayList<>();
        List<StudentCourseStatus> studentCourseStatusList = new ArrayList<>();

        StudentCourse studentCourse = createStudentCourse();
        StudentCourseStatus studentCourseStatus = createStatus();

        studentCourseList.add(studentCourse);
        studentCourseStatusList.add(studentCourseStatus);

        StudentSearchCondition studentSearchCondition = new StudentSearchCondition();
        studentSearchCondition.setId(1);
        studentSearchCondition.setName("テスト四郎");
        studentSearchCondition.setCourseName("ホンバンコース");
        studentSearchCondition.setStatus("仮申込");

        List<Student> emptyStudentList = new ArrayList<>();
        List<StudentDetail> emptyStudentDetailList = new ArrayList<>();

        Mockito.when(repository.searchStudentMultipleCondition(studentSearchCondition)).thenReturn(emptyStudentList);
        Mockito.when(repository.searchStudentCourseList()).thenReturn(studentCourseList);
        Mockito.when(repository.searchStatusList()).thenReturn(studentCourseStatusList);
        Mockito.when(converter.convertStudentDetails(emptyStudentList,studentCourseList)).thenReturn(emptyStudentDetailList);
        Mockito.when(converter.convertStudentCourseStatus(studentCourseList, studentCourseStatusList)).thenReturn(studentCourseList);
        Mockito.when(converter.filterByCourseName(emptyStudentDetailList,studentSearchCondition.getCourseName())).thenReturn(emptyStudentDetailList);
        Mockito.when(converter.filterByStatus(emptyStudentDetailList,studentSearchCondition.getStatus())).thenReturn(emptyStudentDetailList);

        List<StudentDetail> actual = sut.searchStudentMultipleCondition(studentSearchCondition);

        Mockito.verify(repository, Mockito.times(1)).searchStudentMultipleCondition(studentSearchCondition);
        Mockito.verify(repository, Mockito.times(1)).searchStudentCourseList();
        Mockito.verify(repository, Mockito.times(1)).searchStatusList();

        Mockito.verify(converter, Mockito.times(1)).convertStudentCourseStatus(studentCourseList,studentCourseStatusList);
        Mockito.verify(converter, Mockito.times(1)).convertStudentDetails(emptyStudentList,studentCourseList);
        Mockito.verify(converter, Mockito.times(1)).filterByCourseName(emptyStudentDetailList,studentSearchCondition.getCourseName());
        Mockito.verify(converter, Mockito.times(1)).filterByStatus(emptyStudentDetailList,studentSearchCondition.getStatus());

        Assertions.assertEquals(0, actual.size());
    }

    @Test
    @DisplayName("受講生詳細情報の新規登録_受講生詳細情報が登録できておりIDが入力されるか_受講生コース情報が0件の場合")
    void registerStudent_courseListIsEmpty_registerOnlyStudent() {
//        事前準備
        Integer id = 999;
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
        Assertions.assertEquals(999, student.getId());
        Assertions.assertSame(expected, actual);
        Mockito.verify(repository, Mockito.times(1)).registerStudent(student);
        Mockito.verify(repository, Mockito.times(0)).registerStudentCourse(Mockito.any(StudentCourse.class));
        Mockito.verify(repository, Mockito.times(0)).registerStudentCourseStatus(Mockito.any(StudentCourseStatus.class));
    }

    @Test
    @DisplayName("受講生詳細情報の更新処理_リポジトリが呼び出されているか")
    void updateStudent_courseListExists_updateStudentCourseAndStatus() {
        /**
         *     @Transactional
         *     public void updateStudent(StudentDetail studentDetail){
         *         repository.updateStudent(studentDetail.getStudent());
         *         studentDetail.getStudentCourseList()
         *                 .forEach(studentCourse -> {
         *                     repository.updateStudentCourse(studentCourse);
         *                     repository.updateStudentCourseStatus(studentCourse.getStudentCourseStatus());
         *         });
         *     }
         */
//        準備
        Student student = new Student();
        student.setName("テスト花子");
        student.setId(999);

        List<StudentCourse> studentCourseList = new ArrayList<>();
        StudentCourse st1 = new StudentCourse();
        st1.setCourseName("Javaフルコース");
        st1.setId(999);
        StudentCourseStatus st1Status = new StudentCourseStatus();
        st1Status.setStudentCourseId(st1.getId());
        st1Status.setStatus("本申込");
        st1.setStudentCourseStatus(st1Status);

        StudentCourse st2 = new StudentCourse();
        st2.setCourseName("AWSコース");
        st2.setId(998);
        StudentCourseStatus st2Status = new StudentCourseStatus();
        st2Status.setStudentCourseId(st2.getId());
        st2Status.setStatus("本申込");
        st2.setStudentCourseStatus(st2Status);
        studentCourseList.add(st1);
        studentCourseList.add(st2);


        StudentDetail studentDetail = new StudentDetail(student, studentCourseList);
//        実行
        sut.updateStudent(studentDetail);
//        検証
        Mockito.verify(repository, Mockito.times(1)).updateStudent(student);
        Mockito.verify(repository, Mockito.times(1)).updateStudentCourse(st1);
        Mockito.verify(repository, Mockito.times(1)).updateStudentCourse(st2);
        Mockito.verify(repository, Mockito.times(1)).updateStudentCourseStatus(st1Status);
        Mockito.verify(repository, Mockito.times(1)).updateStudentCourseStatus(st2Status);
    }

    @Test
    @DisplayName("受講生詳細情報の更新処理_受講コースが空でもリポジトリが呼び出されているか")
    void updateStudent_courseListIsEmpty_updateOnlyStudent() {
//        準備
        Student student = new Student();
        student.setName("テスト花子");
        student.setId(999);

        List<StudentCourse> studentCourseList = new ArrayList<>();

        StudentDetail studentDetail = new StudentDetail(student, studentCourseList);
//        実行
        sut.updateStudent(studentDetail);
//        検証
        Mockito.verify(repository, Mockito.times(1)).updateStudent(student);
        Mockito.verify(repository, Mockito.times(0)).updateStudentCourse(Mockito.any(StudentCourse.class));
        Mockito.verify(repository, Mockito.times(0)).updateStudentCourseStatus(Mockito.any(StudentCourseStatus.class));
    }

    private static StudentCourseStatus createStatus() {
        StudentCourseStatus studentCourseStatus = new StudentCourseStatus();
        studentCourseStatus.setId(999);
        studentCourseStatus.setStudentCourseId(998);
        studentCourseStatus.setStatus("受講中");
        return studentCourseStatus;
    }

    private static StudentCourse createStudentCourse() {
        StudentCourse studentCourse = new StudentCourse();
        studentCourse.setStudentId(999);
        studentCourse.setId(998);
        studentCourse.setCourseName("テストコース");
        return studentCourse;
    }

    private static Student createStudent() {
        Student student = new Student();
        student.setId(999);
        student.setName("テスト太郎");
        return student;
    }

}
