package uz.owl.schooltest.admin.service;

import javassist.NotFoundException;
import org.springframework.stereotype.Service;
import uz.owl.schooltest.admin.dto.user.UserDto;
import uz.owl.schooltest.admin.dto.user.UserPageDto;
import uz.owl.schooltest.admin.dto.user.UserPayload;
import uz.owl.schooltest.entity.AdminMessage;
import uz.owl.schooltest.entity.Role;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminService {
    UserDto getByUserId(Long id) throws NotFoundException;

    UserPageDto getUser(int start, int count);

    List<UserDto> getAllUsers();

    UserDto saveUser(UserPayload userPayload);

    void deleteById(Long id);

    UserDto block(Long id);

    UserDto unblock(Long id);

    List<AdminMessage> getAdminMessages(Long userId);

    AdminMessage addAdminMessage(Long userId, String message, String cause, String advice);

    void deleteAdminMessages(Long userId, Long megId);

    void deleteAllUserAdminMessages(Long  userId);

    UserDto setExpiredDate(Long id, LocalDateTime localDateTime);

    List<Role> getAllRoles();

    List<Role> getUserRoles(Long userId);

    List<Role> addRoleToUser(Long userId, Long roleId);

    List<Role> removeRoleToUser(Long userId, Long roleId);
}
