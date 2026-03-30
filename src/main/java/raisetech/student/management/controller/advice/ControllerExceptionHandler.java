package raisetech.student.management.controller.advice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import raisetech.student.management.exception.ViolationErrorDetail;
import raisetech.student.management.exception.ViolationErrorResponse;
import raisetech.student.management.exception.TestException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@RestControllerAdvice
public class ControllerExceptionHandler {

    private Object c;

    @ExceptionHandler(TestException.class)
    public ResponseEntity<String> handleTestException(TestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ControllerException:Test Error " + "Controller:" + ex.getMessage());
    }

    /**
     * パラメータなどでvalidationで異常なデータをセットした際エラーになる。
     * そのエラーを処理してJson形式で返す例外処理
     *
     * @param ex      ConstraintViolationException
     * @param request HttpServletRequest
     * @return ResponseEntity<ViolationErrorResponse>
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ViolationErrorResponse> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
    // Json形式で返ってくる時間がUTCなのでそれに合わせて時間を生成ミリ秒まで対応
        Instant timestamp = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        //Httpレスポンスは400を返す
        int status = HttpStatus.BAD_REQUEST.value();
        //どのURLにリクエストしたかを値をとる
        String url = request.getRequestURI();
        //入力チェックでエラーが複数あることを想定してエラー情報を格納したオブジェクトをリストで受ける
        List<ViolationErrorDetail> errorList = new ArrayList<>();
        //エラーからValidationのエラーをSetで受け取る
        Set<ConstraintViolation<?>> violations = ex.getConstraintViolations();
        //forEachで1つずつ情報を受け取り情報をセットしていく
        violations.forEach((ConstraintViolation<?> c) -> {
            ViolationErrorDetail errorDetail = new ViolationErrorDetail();
/**
 *      errorDetail.setInvalid(String.valueOf(c.getInvalidValue()));はパスワードなど重要情報が漏洩するのを防ぐため今回のエラーでは渡さない
 */
            errorDetail.setMessage(c.getMessage());
            errorDetail.setPath(extractLastNodeName(c.getPropertyPath()));
            errorList.add(errorDetail);
        });
        //リストをPathとその次にメッセージのテンプレートを基準に並べ替え
        errorList.sort(Comparator.comparing(ViolationErrorDetail::getPath).thenComparing(ViolationErrorDetail::getMessage));
        //Jsonで組み立てるための情報が入ったエラーDTOオブジェクトを作成
        ViolationErrorResponse violationErrorResponse = new ViolationErrorResponse(timestamp, status, errorList, url);
//      //BadRequestとしてJsonで返す。
        return ResponseEntity.badRequest().body(violationErrorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ViolationErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,HttpServletRequest request) {
        Instant timestamp = Instant.now().truncatedTo(ChronoUnit.MILLIS);
        int status =  HttpStatus.BAD_REQUEST.value();
        String url = request.getRequestURI();

        List<ViolationErrorDetail> errorList = new ArrayList<>();

        List<FieldError> fieldErrorList = ex.getBindingResult().getFieldErrors();

        fieldErrorList.forEach(fieldError -> {
            ViolationErrorDetail violationErrorDetail = new ViolationErrorDetail();
            violationErrorDetail.setPath(fieldErrorSplit(fieldError.getField()));
            violationErrorDetail.setMessage(fieldError.getDefaultMessage());

            errorList.add(violationErrorDetail);
        });

        errorList.sort(Comparator.comparing(ViolationErrorDetail::getPath).thenComparing(ViolationErrorDetail::getMessage));

        ViolationErrorResponse violationErrorResponse = new ViolationErrorResponse(timestamp,status,errorList,url);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(violationErrorResponse);
    }

    /**
     * 最後のNodeを返す
     * @param propertyPath エラーの際受け取るPath
     * @return Path.Node
     */
    private String extractLastNodeName(Path propertyPath) {
        String lastNode = "";
        for (Path.Node node : propertyPath) {
            if (node.getName() != null) {
                lastNode = node.getName();
            }
        }
        if (lastNode.isEmpty()){
            lastNode =" ";
        }
        return lastNode;
    }

    private String fieldErrorSplit(String stringPath){
        String[] array = stringPath.split("\\.");
        return array[array.length-1];

    }


}
