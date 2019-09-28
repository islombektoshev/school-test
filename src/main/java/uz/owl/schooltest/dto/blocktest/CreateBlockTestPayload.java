package uz.owl.schooltest.dto.blocktest;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateBlockTestPayload {
    @NotNull
    private String name;
    @NotNull
    private Integer countOfQuestion;

    @NotNull
    private List<String> subjects;

    @NotNull
    private List<Integer> groups;
}
