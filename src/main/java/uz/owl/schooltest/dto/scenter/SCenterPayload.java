package uz.owl.schooltest.dto.scenter;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SCenterPayload {
    @NotNull
    private String name;
    @NotNull
    private String caption;
}
