package raisetech.student.management.controller.advice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import raisetech.student.management.exception.TestException;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(TestException.class)
    public ResponseEntity<String> handleTestException(TestException ex){
        return ResponseEntity.status(500).body("ControllerException:Test Error " + "Controller:" + ex.getMessage());
    }
}
