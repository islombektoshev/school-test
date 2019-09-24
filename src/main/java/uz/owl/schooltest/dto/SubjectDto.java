package uz.owl.schooltest.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubjectDto{
    private String name;
    private boolean primary;
}
