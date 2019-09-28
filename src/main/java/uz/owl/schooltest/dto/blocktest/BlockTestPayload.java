package uz.owl.schooltest.dto.blocktest;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BlockTestPayload {
    @NotNull
    private String name;


    private Integer countOfQuestion;
}
