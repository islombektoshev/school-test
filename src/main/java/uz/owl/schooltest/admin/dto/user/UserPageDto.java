package uz.owl.schooltest.admin.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPageDto {
    private List<UserDto> users;
    long totalElement;
    int totalPages;
    int size;
    int number;
    int numberOfElement;
}
