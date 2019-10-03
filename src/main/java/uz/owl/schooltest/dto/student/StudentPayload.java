package uz.owl.schooltest.dto.student;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class StudentPayload {
    @NotNull
    private String firstname;
    @NotNull
    private String lastname;
}
