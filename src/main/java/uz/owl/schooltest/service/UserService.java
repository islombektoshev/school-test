package uz.owl.schooltest.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import uz.owl.schooltest.dao.UserDao;
import uz.owl.schooltest.entity.User;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public void deleteByUsername(String username) {
        userDao.deleteByUsername(username);
    }

    public <S extends User> S save(S s) {
        return userDao.save(s);
    }

    public <S extends User> Iterable<S> saveAll(Iterable<S> iterable) {
        return userDao.saveAll(iterable);
    }

    public Optional<User> findById(Long aLong) {
        return userDao.findById(aLong);
    }

    public boolean existsById(Long aLong) {
        return userDao.existsById(aLong);
    }

    public Iterable<User> findAll() {
        return userDao.findAll();
    }

    public Iterable<User> findAllById(Iterable<Long> iterable) {
        return userDao.findAllById(iterable);
    }

    public long count() {
        return userDao.count();
    }

    public void deleteById(Long aLong) {
        userDao.deleteById(aLong);
    }

    public void delete(User user) {
        userDao.delete(user);
    }

    public void deleteAll(Iterable<? extends User> iterable) {
        userDao.deleteAll(iterable);
    }

    public void deleteAll() {
        userDao.deleteAll();
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
