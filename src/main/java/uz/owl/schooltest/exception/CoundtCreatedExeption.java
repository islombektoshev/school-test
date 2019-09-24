package uz.owl.schooltest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CoundtCreatedExeption extends RuntimeException{
    public CoundtCreatedExeption(String message) {
        super(message);
    }
}
