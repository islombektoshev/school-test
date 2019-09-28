package uz.owl.schooltest.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDto {
    private String username;

    private String lastname;

    private String firstname;

}
