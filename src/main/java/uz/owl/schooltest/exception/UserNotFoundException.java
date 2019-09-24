package uz.owl.schooltest.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UserNotFoundException extends RuntimeException {
    @Getter
    private String username;

    public UserNotFoundException(String msg) {
        super(msg);
    }
    public UserNotFoundException(String msg, String username) {
        super(msg);
        this.username = username;
    }
}
