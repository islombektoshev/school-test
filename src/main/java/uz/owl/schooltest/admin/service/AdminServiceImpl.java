package uz.owl.schooltest.admin.service;

import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import uz.owl.schooltest.admin.dao.AdminMessageDao;
import uz.owl.schooltest.admin.dao.AdminUserDao;
import uz.owl.schooltest.admin.dto.user.UserDto;
import uz.owl.schooltest.admin.dto.user.UserPageDto;
import uz.owl.schooltest.admin.dto.user.UserPayload;
import uz.owl.schooltest.dao.RoleDao;
import uz.owl.schooltest.entity.AdminMessage;
import uz.owl.schooltest.entity.Role;
import uz.owl.schooltest.entity.User;
import uz.owl.schooltest.exception.NotFoudException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminUserDao adminUserDao;

    @Autowired
    private AdminMessageDao adminMessageDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    DateTimeFormatter dateFormatter;

    @Override
    public UserDto getByUserId(Long id) throws NotFoundException {
        User user = getUserEntity(id);
        return convert(user);
    }

    @Override
    public UserPageDto getUser(int start, int count) {
        Page<User> all = adminUserDao.findAll(PageRequest.of(start, count));
        long totalElements = all.getTotalElements();
        int totalPages = all.getTotalPages();
        int size = all.getSize();
        int number = all.getNumber();
        int numberOfElements = all.getNumberOfElements();
        List<UserDto> collect = all.map(this::convert).get().collect(Collectors.toList());
        return UserPageDto.builder()
                .totalElement(totalElements)
                .totalPages(totalPages)
                .size(size)
                .number(number)
                .numberOfElement(numberOfElements)
                .users(collect)
                .build();
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<User> users = new ArrayList<>();
        adminUserDao.findAll().forEach(users::add);
        return users.stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public UserDto saveUser(UserPayload userPayload) {
        User user = User.builder()
                .firstname(userPayload.getFirstname())
                .lastname(userPayload.getLastname())
                .password(userPayload.getPassword())
                .username(userPayload.getUsername())
                .enable(userPayload.isEnable())
                .paymentExpiredDate(userPayload.getPaymentExpiredDate())
                .build();
        User savedUser = adminUserDao.save(user);
        return convert(user);
    }

    @Override
    public void deleteById(Long id) {
        adminUserDao.deleteById(id);
    }

    @Override
    public UserDto block(Long id) {
        User user = getUserEntity(id);
        user.setEnable(false);
        User save = adminUserDao.save(user);
        return convert(save);
    }

    @Override
    public UserDto unblock(Long id) {
        User user = getUserEntity(id);
        user.setEnable(true);
        User save = adminUserDao.save(user);
        return convert(save);
    }

    @Override
    @Transactional
    public List<AdminMessage> getAdminMessages(Long userId) {
        User userEntity = getUserEntity(userId);
        return userEntity.getAdminMessages();
    }

    @Override
    public AdminMessage addAdminMessage(Long userId, String message, String cause, String advice) {
        User userEntity = getUserEntity(userId);
        AdminMessage adminMessage = AdminMessage
                .builder()
                .message(message)
                .cause(cause)
                .advice(advice)
                .build();
        userEntity.getAdminMessages().add(adminMessage);
        adminMessage.setUser(userEntity);
        AdminMessage save = adminMessageDao.save(adminMessage);
        adminUserDao.save(userEntity);
        return adminMessage;
    }

    @Override
    @Transactional
    public void deleteAdminMessages(Long userId, Long msgId) {
        AdminMessage adminMessage = adminMessageDao.findById(msgId).orElseThrow(() -> new NotFoudException("Message not found (" + msgId + ")"));
        User user = adminMessage.getUser();
        if (user.getId() != userId) throw new NotFoudException("Message not found (" + msgId + ")");
        adminMessageDao.delete(adminMessage);
    }

    @Override
    @Transactional
    public void deleteAllUserAdminMessages(Long userId) {
        User userEntity = getUserEntity(userId);
        adminMessageDao.deleteAll(userEntity.getAdminMessages());
        userEntity.getAdminMessages().clear();
        adminUserDao.save(userEntity);
    }

    @Override
    @Deprecated
    public UserDto setExpiredDate(Long id, LocalDateTime localDateTime) {
        User userEntity = getUserEntity(id);
        userEntity.setPaymentExpiredDate(localDateTime);
        adminUserDao.save(userEntity);
        return convert(userEntity);
    }

    @Override
    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
        roleDao.findAll().forEach(roles::add);
        return roles;
    }

    @Override
    @Transactional
    public List<Role> getUserRoles(Long userId) {
        User userEntity = getUserEntity(userId);
        List<Role> roles = userEntity.getRoles();
        return roles;
    }

    @Override
    public List<Role> addRoleToUser(Long userId, Long roleId) {
        User userEntity = getUserEntity(userId);
        List<Role> roles = userEntity.getRoles();
        Role role = getRole(roleId);
        if (!roles.contains(role)) {
            roles.add(role);
            adminUserDao.save(userEntity);
        }
        return roles;
    }

    @Override
    @Transactional
    public List<Role> removeRoleToUser(Long userId, Long roleId) {
        User userEntity = getUserEntity(userId);
        List<Role> roles = userEntity.getRoles();
        Role role = getRole(roleId);
        if (roles.contains(role)) {
            roles.remove(role);
            adminUserDao.save(userEntity);
        }
        return roles;
    }

    @Override
    public List<UserDto> getPaymentExpiredUsers() {
        return adminUserDao.getAllExpired().stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getPaymentNotExpiredUsers() {
        return adminUserDao.getAllNotExpired().stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getBlockedUsers() {
        return adminUserDao.getAllByEnable(false).stream().map(this::convert).collect(Collectors.toList());
    }

    @Override
    public List<UserDto> getNotBlockedUsers() {
        return adminUserDao.getAllByEnable(true).stream().map(this::convert).collect(Collectors.toList());
    }

    Role getRole(Long roleId) {
        return roleDao.findById(roleId).orElseThrow(() -> new NotFoudException("Role not found"));
    }

    User getUserEntity(Long id) {
        return adminUserDao.findById(id).orElseThrow(() -> new NotFoudException("User not found (" + id + ")"));
    }

    UserDto convert(User user) {
        if (user == null) return null;
        return UserDto.builder()
                .username(user.getUsername())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .createdDate(user.getCreatedDate())
                .enable(user.isEnable())
                .paymentExpiredDate(user.getPaymentExpiredDate())
                .id(user.getId())
                .build();
    }
}
