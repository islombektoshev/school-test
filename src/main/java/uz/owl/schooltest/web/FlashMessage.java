package uz.owl.schooltest.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlashMessage {
    private String message;
    private Status status;

    public static enum Status{
        SUCCESS,
        FAILURE
    }
}
