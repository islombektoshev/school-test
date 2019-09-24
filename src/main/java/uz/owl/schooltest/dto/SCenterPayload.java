package uz.owl.schooltest.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SCenterPayload {
    @NotNull
    private String name;
    private String caption;
}
