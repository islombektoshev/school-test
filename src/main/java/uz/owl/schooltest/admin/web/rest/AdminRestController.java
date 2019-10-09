package uz.owl.schooltest.admin.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.owl.schooltest.admin.dto.adminmessage.AdminMessagePayload;
import uz.owl.schooltest.admin.dto.signle.SingleId;
import uz.owl.schooltest.admin.dto.user.UserDto;
import uz.owl.schooltest.admin.dto.user.UserPageDto;
import uz.owl.schooltest.admin.service.AdminService;
import uz.owl.schooltest.entity.AdminMessage;
import uz.owl.schooltest.entity.Role;
import uz.owl.schooltest.service.UserService;
import uz.owl.schooltest.web.Message;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/admin")
public class AdminRestController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private UserService userService;

    @GetMapping("/")
    public uz.owl.schooltest.dto.user.UserDto home(Principal principal) {
        return userService.getUserDto(principal.getName());
    }

    @GetMapping("/users/all")
    public List<UserDto> getAllUsers() {
        return adminService.getAllUsers();
    }

    @GetMapping("/users")
    public UserPageDto getUsers(@RequestParam("start") int start, @RequestParam("count") int count) {
        return adminService.getUser(start, count);
    }

    @PostMapping("/users/{userId}/block")
    public ResponseEntity blockUser(@PathVariable Long userId) {
        UserDto block = adminService.block(userId);
        return ResponseEntity.ok(block);
    }

    @PostMapping("/users/{userId}/unblock")
    public ResponseEntity unblockUser(@PathVariable Long userId) {
        UserDto block = adminService.unblock(userId);
        return ResponseEntity.ok(block);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity deleteUser(@PathVariable Long userId) {
        adminService.deleteById(userId);
        return ResponseEntity.ok(new Message(200, "Deleted"));
    }

    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        return adminService.getAllRoles();
    }

    @GetMapping("/users/{userId}/roles")
    public List<Role> getUserRoles(@PathVariable Long userId) {
        return adminService.getUserRoles(userId);
    }

    @PostMapping("/users/{userId}/roles")
    public List<Role> addRoles(@PathVariable Long userId, @Valid @RequestBody SingleId roleId) {
        return adminService.addRoleToUser(userId, roleId.getId());
    }

    @DeleteMapping("/users/{userId}/roles")
    public List<Role> removeRoles(@PathVariable Long userId, @Valid @RequestBody SingleId roleId) {
        return adminService.removeRoleToUser(userId, roleId.getId());
    }

    @GetMapping("/users/{userId}/messages")
    public List<AdminMessage> getMessages(@PathVariable Long userId) {
        return adminService.getAdminMessages(userId);
    }

    @PostMapping("/users/{userId}/messages")
    public AdminMessage addMessages(@PathVariable Long userId, @Valid @RequestBody AdminMessagePayload payload) {
        return adminService.addAdminMessage(userId, payload.getMessage(), payload.getCause(), payload.getAdvice());
    }

    @DeleteMapping("/users/{userId}/messages")
    public ResponseEntity deleteMessage(@PathVariable Long userId, @Valid @RequestBody SingleId msgId) {
        adminService.deleteAdminMessages(userId, msgId.getId());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{userId}/messages/all")
    public ResponseEntity deleteMessage(@PathVariable Long userId) {
        adminService.deleteAllUserAdminMessages(userId);
        return ResponseEntity.ok().build();
    }

}
