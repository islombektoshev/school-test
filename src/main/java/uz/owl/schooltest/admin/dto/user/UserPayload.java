package uz.owl.schooltest.admin.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

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

    @ApiModelProperty(name = "enable", example = "true")
    @NotNull
    private boolean enable;

    @ApiModelProperty(name = "time", example = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime paymentExpiredDate;
}
