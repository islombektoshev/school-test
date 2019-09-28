package uz.owl.schooltest.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uz.owl.schooltest.web.Message;

import java.util.Date;

@RestController
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public void ifUserNotFound(UserNotFoundException e){
        System.out.println(e.getMessage());
    }

    @ExceptionHandler(CoundtCreatedExeption.class)
    public ResponseEntity<Message> ifCoundtCreated(CoundtCreatedExeption e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(400, e.getMessage()));
    }

    @ExceptionHandler(CoundtUpdatedException.class)
    public ResponseEntity<Message> ifCoundtUpdated(CoundtUpdatedException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(400, e.getMessage()));
    }

    @ExceptionHandler(CenterNotFoundException.class)
    public ResponseEntity<Message> ifCenterNotFound(CoundtUpdatedException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(400, e.getMessage()));
    }

    @ExceptionHandler(NotFoudException.class)
    public ResponseEntity<Message> notFoundEx(NotFoudException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Message(404, e.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Message> anyError(RuntimeException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(400, "Internal Server error"));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return ResponseEntity.badRequest().body(new ErrorMessage(new Date(),"Invalid argument", ex.getMessage()));
    }
}
