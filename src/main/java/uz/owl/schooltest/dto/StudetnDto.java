package uz.owl.schooltest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudetnDto{
    private Long id;
    private String firstname;
    private String lastname;
}
