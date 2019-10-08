package uz.owl.schooltest.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.owl.schooltest.dao.RoleDao;
import uz.owl.schooltest.dao.UserDao;
import uz.owl.schooltest.dto.user.UserDto;
import uz.owl.schooltest.dto.user.UserPayload;
import uz.owl.schooltest.entity.Role;
import uz.owl.schooltest.entity.User;
import uz.owl.schooltest.exception.UserNotFoundException;

import javax.transaction.Transactional;

@Service
public class UserService implements UserDetailsService {

    private final UserDao userDao;
    private final RoleDao roleDao;

    public UserService(UserDao userDao, RoleDao roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    User findByUsername(String username) {
        return userDao.findByUsername(username);
    }


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User byUsername = userDao.findByUsername(s);
        if (byUsername == null) {
            throw new UsernameNotFoundException("User Not Found!");
        }
        return byUsername;
    }

    public UserDto getUserDto(String username) {
        User user = (User) loadUserByUsername(username);
        return converToUserPayload(user);
    }

    User getUser(String username, String s) throws UserNotFoundException {
        User user = findByUsername(username);
        if (user == null) throw new UserNotFoundException(s, username);
        return user;
    }

    User getUser(String username) throws UserNotFoundException {
        return getUser(username, "User not found");
    }


    public User creatUserEntity(String username, String password, String firstname, String lastname) {
        User user = User.builder()
                .username(username)
                .password(password)
                .firstname(firstname)
                .lastname(lastname)
                .build();
        Role role_user = roleDao.findByRolename("ROLE_USER");
        role_user.getUsers().add(user);
        user.getRoles().add(role_user);
        try {
            userDao.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return user;
    }

    public UserDto createUser(UserPayload p){
        User user = creatUserEntity(p.getUsername(), p.getPasswrod(), p.getFirstname(), p.getLastname());
        return converToUserPayload(user);
    }

    public UserDto converToUserPayload(User user) {
        if (user == null) return null;
        return UserDto.builder()
                .username(user.getUsername())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .build();
    }
}
