package uz.owl.schooltest.dto.student;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class AddSubjectToStudentPayload {
    @NotNull
    List<String> subjectName;
}
