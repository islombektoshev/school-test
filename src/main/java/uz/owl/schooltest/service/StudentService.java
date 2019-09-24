package uz.owl.schooltest.service;

import org.springframework.stereotype.Service;
import uz.owl.schooltest.dao.StudentDao;
import uz.owl.schooltest.dto.StudetnDto;
import uz.owl.schooltest.entity.SCenter;
import uz.owl.schooltest.entity.Student;
import uz.owl.schooltest.entity.User;
import uz.owl.schooltest.exception.CenterNotFoundException;
import uz.owl.schooltest.exception.UserNotFoundException;

import java.util.Optional;

@Service
public class StudentService  {
    private final StudentDao studentDao;
    private final SCenterService sCenterService;
    private final UserService userService;

    public StudentService(StudentDao studentDao, SCenterService sCenterService, UserService userService) {
        this.studentDao = studentDao;
        this.sCenterService = sCenterService;
        this.userService = userService;
    }

    public StudetnDto getStudent(String username, String centername, Long studentid){


        return null;
    }


    public Optional<Student> findById(Long aLong) {
        return studentDao.findById(aLong);
    }
    public <S extends Student> S save(S s) {
        return studentDao.save(s);
    }
    public void deleteById(Long aLong) {
        studentDao.deleteById(aLong);
    }
    public void delete(Student student) {
        studentDao.delete(student);
    }
    private User getUser(String username, String s) throws UserNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null) throw new UserNotFoundException(s, username);
        return user;
    }
    private User getUser(String username) throws UserNotFoundException {
        return getUser(username, "User not found");
    }
    StudetnDto convertToStudentDto(Student student) {
        return StudetnDto.builder().
                id(student.getId()).
                firstname(student.getFirstname()).
                lastname(student.getLastname()).build();
    }
    private SCenter getCenter(User user, String centername) throws CenterNotFoundException {
        SCenter byAuthorEntityAndName = sCenterService.getByAuthorEntityAndName(user, centername);
        return byAuthorEntityAndName;
    }
}
