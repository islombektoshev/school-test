package uz.owl.schooltest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SCenterDto {
    private Long id;
    private String name;
    private String caption;
}
