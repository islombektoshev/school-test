package uz.owl.schooltest.admin.dto.signle;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SingleId {
    @NotNull
    private Long id;
}
