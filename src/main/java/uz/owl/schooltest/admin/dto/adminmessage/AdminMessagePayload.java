package uz.owl.schooltest.admin.dto.adminmessage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminMessagePayload {
    @NotNull
    private String message;
    @NotNull
    private String cause;
    @NotNull
    private String advice;
}
