package uz.owl.schooltest.dto.student;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StudentDto {
    private Long id;
    private String firstname;
    private String lastname;
}
