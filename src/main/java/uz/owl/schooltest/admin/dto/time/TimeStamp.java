package uz.owl.schooltest.admin.dto.time;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@ApiModel(value = "TimeStamp", description = "Use for sending time stamp")
public class TimeStamp {
    @ApiModelProperty(name = "time", example = "yyyy-MM-dd HH:mm")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime time;
}
