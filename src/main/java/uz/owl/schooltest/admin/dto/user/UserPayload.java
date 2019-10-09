package uz.owl.schooltest.admin.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPayload {
    @NotNull
    private String username;

    @NotNull
    private String password;

    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

    @NotNull
    private boolean enable;

    // TODO: 10/9/2019 add expired date into this class
}
