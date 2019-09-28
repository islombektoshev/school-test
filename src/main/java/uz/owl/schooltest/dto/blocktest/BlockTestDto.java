package uz.owl.schooltest.dto.blocktest;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BlockTestDto {
    private Long id;
    private String name;
    private int countOfQuestion;
}
