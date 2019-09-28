package uz.owl.schooltest.dto.user;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
public class UserPayload {

    @Pattern(regexp = "[\\w_.!@#$%^&*+-~]{3,255}")
    private String username;

    @Pattern(regexp = "[\\w_.!@#$%^&*+-~]{3,255}")
    private String passwrod;

    @NotNull
    private String firstname;

    @NotNull
    private String lastname;
}
