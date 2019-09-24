package uz.owl.schooltest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CoundtUpdatedException extends RuntimeException{
    public CoundtUpdatedException(String message) {
        super(message);
    }
}
