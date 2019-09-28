package uz.owl.schooltest.dto.group;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GroupDto {
    private Long id;
    private String name;
}
