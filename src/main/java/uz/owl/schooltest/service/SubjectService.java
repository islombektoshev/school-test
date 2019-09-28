package uz.owl.schooltest.service;

import org.springframework.stereotype.Service;
import uz.owl.schooltest.dao.SubjectDao;
import uz.owl.schooltest.dto.subject.SubjectPayload;
import uz.owl.schooltest.dto.subject.SubjectDto;
import uz.owl.schooltest.entity.SCenter;
import uz.owl.schooltest.entity.Student;
import uz.owl.schooltest.entity.Subject;
import uz.owl.schooltest.entity.User;
import uz.owl.schooltest.exception.CenterNotFoundException;
import uz.owl.schooltest.exception.CoundtCreatedExeption;
import uz.owl.schooltest.exception.NotFoudException;
import uz.owl.schooltest.exception.UserNotFoundException;

import javax.transaction.Transactional;
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

    public SubjectDto saveSubject(String username, SubjectPayload subjectPayload) throws UserNotFoundException, CenterNotFoundException, CoundtCreatedExeption {
        Subject subject = Subject.builder()
                .name(subjectPayload.getName())
                .primarySubject(subjectPayload.isPrimary())
                .scenter(sCenterService.getByAuthorAndName(username, subjectPayload.getCentername()))
                .build();
        System.out.println(subject);
        try {
            return convertToSubjectDto(subjectDao.save(subject));
        } catch (Exception e) {
            throw new CoundtCreatedExeption("Subject Coudnt save");
        }
    }

    public List<SubjectDto> getSubjectByUsernameAndCentername(String username, String centername) throws UserNotFoundException, CenterNotFoundException {
        User user = userService.getUser(username);
        SCenter center = sCenterService.getCenter(user, centername);
        List<SubjectDto> subjectDtos = subjectDao.findAllByScenter(center).stream().map(this::convertToSubjectDto).collect(Collectors.toList());
        return subjectDtos;
    }

    public SubjectDto getSubjectDtoByUsernameAndCenternameAndSubjectname(String username, String centername, String subjectname) throws UserNotFoundException, CenterNotFoundException {
        User user = userService.getUser(username);
        SCenter center = sCenterService.getCenter(user, centername);
        Subject byScenterAndName = subjectDao.findByScenterAndName(center, subjectname);
        if (byScenterAndName == null) {
            throw new NotFoudException("Subject Not Found");
        }
        return convertToSubjectDto(byScenterAndName);
    }

    /**
     * Uddate Subject
     *
     * @param username          user username
     * @param oldSubjectName    old subject which is will be update
     * @param subjectPayload data
     * @return
     * @throws UserNotFoundException
     * @throws CenterNotFoundException
     */
    public SubjectDto updateSubject(String username, String oldSubjectName, SubjectPayload subjectPayload) throws UserNotFoundException, CenterNotFoundException {
        User user = userService.getUser(username);
        SCenter center = sCenterService.getCenter(user, subjectPayload.getCentername());
        Subject subject = subjectDao.findByScenterAndName(center, oldSubjectName); // getting subject with old name
        if (subject == null) {
            throw new NotFoudException("Subject Not Found");
        }
        subject.setName(subjectPayload.getName());
        subject.setPrimarySubject(subjectPayload.isPrimary());
        Subject newSubject = subjectDao.save(subject);
        return convertToSubjectDto(newSubject);
    }

    @Transactional
    public void deleteSubject(String username, String centername, String subjectname) throws UserNotFoundException, CenterNotFoundException {
        User user = userService.getUser(username);
        SCenter center = sCenterService.getCenter(user, centername);
        Subject byScenterAndName = subjectDao.findByScenterAndName(center, subjectname);
        if (byScenterAndName == null) {
            throw new NotFoudException("Subject Not Found");
        }
        subjectDao.deleteByScenterAndName(center, subjectname);
    }

    Subject getSubjectByCenterAndName(SCenter sCenter, String name){
        return subjectDao.findByScenterAndName(sCenter, name);
    }

    List<Subject> getPrimarySubject(User user, SCenter center){
        return subjectDao.findAllByScenterAndPrimarySubject(center, true);
    }

    SubjectDto convertToSubjectDto(Subject subject) {
        return SubjectDto.builder().name(subject.getName()).primary(subject.isPrimarySubject()).build();
    }
}
