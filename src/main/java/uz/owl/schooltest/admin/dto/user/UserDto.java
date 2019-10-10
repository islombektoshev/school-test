package uz.owl.schooltest.admin.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Use for sending user data")
public class UserDto {
    @ApiModelProperty(name = "id", example = "0")
    private Long id;
    private String username;
    private String firstname;
    private String lastname;

    @ApiModelProperty(name = "enable", example = "true")
    private Boolean enable;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    @ApiModelProperty(name = "createdDate", example = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdDate;

    @ApiModelProperty(name = "paymentExpiredDate", example = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime paymentExpiredDate;

}
