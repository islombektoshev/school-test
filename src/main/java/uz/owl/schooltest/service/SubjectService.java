package uz.owl.schooltest.service;

import org.springframework.stereotype.Service;
import uz.owl.schooltest.dao.SubjectDao;
import uz.owl.schooltest.dto.AddSubjectPayload;
import uz.owl.schooltest.dto.SubjectDto;
import uz.owl.schooltest.entity.SCenter;
import uz.owl.schooltest.entity.Subject;
import uz.owl.schooltest.entity.User;
import uz.owl.schooltest.exception.CenterNotFoundException;
import uz.owl.schooltest.exception.CoundtCreatedExeption;
import uz.owl.schooltest.exception.UserNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubjectService {
    private final SubjectDao subjectDao;
    private final UserService userService;
    private final SCenterService sCenterService;

    public SubjectService(SubjectDao subjectDao, UserService userService, SCenterService sCenterService) {
        this.subjectDao = subjectDao;
        this.userService = userService;
        this.sCenterService = sCenterService;
    }

    public SubjectDto saveSubject(String username, AddSubjectPayload addSubjectPayload) throws UserNotFoundException, CenterNotFoundException, CoundtCreatedExeption {
        Subject subject = Subject.builder()
                .name(addSubjectPayload.getName())
                .primarySubject(addSubjectPayload.isPrimary())
                .scenter(sCenterService.getByAuthorAndName(username, addSubjectPayload.getCentername()))
                .build();
        System.out.println(subject);
        try {
            return convertToSubjectDto(subjectDao.save(subject));
        } catch (Exception e) {
            throw new CoundtCreatedExeption("Subject Coudnt save");
        }
    }

    public List<SubjectDto> getSubjectByUsernameAndCentername(String username, String centername) throws UserNotFoundException, CenterNotFoundException {
        User user = getUser(username);
        SCenter center = getCenter(user, centername);
        List<SubjectDto> subjectDtos = subjectDao.findAllByScenter(center).stream().map(this::convertToSubjectDto).collect(Collectors.toList());
        return subjectDtos;
    }

    public SubjectDto getSubjectDtoByUsernameAndCenternameAndSubjectname(String username, String centername, String subjectname) throws UserNotFoundException, CenterNotFoundException {
        User user = getUser(username);
        SCenter center = getCenter(user, centername);
        Subject byScenterAndName = subjectDao.findByScenterAndName(center, subjectname);
        return convertToSubjectDto(byScenterAndName);
    }

    /**
     * +
     * Uddate Subject
     *
     * @param username          user username
     * @param oldSubjectName    old subject which is will be update
     * @param addSubjectPayload data
     * @return
     * @throws UserNotFoundException
     * @throws CenterNotFoundException
     */
    public SubjectDto updateSubject(String username, String oldSubjectName, AddSubjectPayload addSubjectPayload) throws UserNotFoundException, CenterNotFoundException {
        User user = getUser(username);
        SCenter center = getCenter(user, addSubjectPayload.getCentername());
        Subject subject = subjectDao.findByScenterAndName(center, oldSubjectName); // getting subject with old name
        subject.setName(addSubjectPayload.getName());
        subject.setPrimarySubject(addSubjectPayload.isPrimary());
        Subject newSubject = subjectDao.save(subject);
        return convertToSubjectDto(newSubject);
    }

    public void deleteSubject(String username, String centername, String subjectname) throws UserNotFoundException, CenterNotFoundException {
        User user = getUser(username);
        SCenter center = getCenter(user, centername);
        subjectDao.deleteByScenterAndName(center, subjectname);
    }


    private User getUser(String username, String s) throws UserNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null) throw new UserNotFoundException(s, username);
        return user;
    }

    private User getUser(String username) throws UserNotFoundException {
        return getUser(username, "User not found");
    }

    SubjectDto convertToSubjectDto(Subject subject) {
        return SubjectDto.builder().name(subject.getName()).primary(subject.isPrimarySubject()).build();
    }

    private SCenter getCenter(User user, String centername) throws CenterNotFoundException {
        SCenter byAuthorEntityAndName = sCenterService.getByAuthorEntityAndName(user, centername);
        return byAuthorEntityAndName;
    }
}
