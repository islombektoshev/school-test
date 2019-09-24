package uz.owl.schooltest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class AddSubjectPayload {
    @NotNull
    private String name;
    @NotNull
    private boolean primary;

    @JsonIgnore
    private String centername;
}
