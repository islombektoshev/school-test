package uz.owl.schooltest.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.owl.schooltest.dao.UserDao;
import uz.owl.schooltest.entity.User;

import javax.transaction.Transactional;

@Service
public class UserService implements UserDetailsService {

    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
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


}
