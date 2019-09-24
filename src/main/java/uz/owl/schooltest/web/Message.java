package uz.owl.schooltest.web;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message extends AbstractBaseMessage<Message>{
    private int status;
    private String message;

    {
        setC(this);
    }
    public Message() {
    }
}
