package uz.owl.schooltest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import uz.owl.schooltest.dao.StudentDao;
import uz.owl.schooltest.dto.blocktest.BlockTestDto;
import uz.owl.schooltest.dto.group.GroupDto;
import uz.owl.schooltest.dto.student.AddStudentToGroupPayload;
import uz.owl.schooltest.dto.student.AddSubjectToStudentPayload;
import uz.owl.schooltest.dto.student.StudentDto;
import uz.owl.schooltest.dto.student.StudentPayload;
import uz.owl.schooltest.dto.subject.SubjectDto;
import uz.owl.schooltest.entity.*;
import uz.owl.schooltest.exception.NotFoudException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static uz.owl.schooltest.web.rest.ControllerTool.requireNotNull;

@Service
public class StudentService {
    private final StudentDao studentDao;
    private final SCenterService sCenterService;
    private final UserService userService;
    private final SubjectService subjectService;
    @Autowired // o'chirma buni kerak!!!!
    private  GroupService groupService;
    @Autowired
    private BlockTestService blockTestService;

    public StudentService(StudentDao studentDao, SCenterService sCenterService, UserService userService, SubjectService subjectService) {
        this.studentDao = studentDao;
        this.sCenterService = sCenterService;
        this.userService = userService;
        this.subjectService = subjectService;
    }

    public List<StudentDto> getAllStudents(String username, String centername) {
        User user = userService.getUser(username);
        SCenter center = sCenterService.getCenter(user, centername);
        return studentDao.findAllByScenter(center).stream().map(this::convertToStudentDto).collect(Collectors.toList());
    }

    public StudentDto getStudent(String username, String centername, Long studentid) {
        User user = userService.getUser(username);
        SCenter center = sCenterService.getCenter(user, centername);
        Student student = getStudentEntity(center, studentid);
        requireNotNull(student);
        return convertToStudentDto(student);
    }

    Student getStudentEntity(SCenter center, Long studentid) {

        return studentDao.findByScenterAndId(center, studentid);
    }

    @Transactional // TODO: !9/27/2019 update didn't work check this method!
    public StudentDto saveStudent(String username, String centername, StudentPayload payload) {
        User user = userService.getUser(username);
        SCenter center = sCenterService.getCenter(user, centername);
        Student student = Student.builder()
                .firstname(payload.getFirstname())
                .lastname(payload.getLastname())
                .scenter(center)
                .build();
        List<Subject> primarySubjects = subjectService.getPrimarySubject(user, center);
        primarySubjects.forEach(System.out::println); // TODO: 9/26/2019 delete this line
        primarySubjects.forEach(subject -> {
            student.addSubject(subject);
            subject.addStudent(student);
        });
        Student newStudent = studentDao.save(student); // todo may i need to update mapped side
        return convertToStudentDto(newStudent);
    }

    public StudentDto updateStudent(String username, String centername, Long studentId, StudentPayload payload) {
        User user = userService.getUser(username);
        SCenter center = sCenterService.getCenter(user, centername);
        Student studentEntity = getStudentEntity(center, studentId);
        requireNotNull(studentEntity);
        studentEntity.setFirstname(payload.getFirstname());
        studentEntity.setLastname(payload.getLastname());
        System.out.println("studentEntity = " + studentEntity);
        studentDao.save(studentEntity);
        return convertToStudentDto(studentEntity);
    }

    public void delete(String username, String centername, Long studentId) {
        User user = userService.getUser(username);
        SCenter center = sCenterService.getCenter(user, centername);
        Student studentEntity = getStudentEntity(center, studentId);
        requireNotNull(studentEntity);
        studentDao.delete(studentEntity);
    }


    @Transactional // TODO: 9/26/2019 transactional?? am I need it ?
    public GroupDto addGroupToStudent(String username, String centername, Long studentId, AddStudentToGroupPayload payload) {
        User user = userService.getUser(username);
        SCenter center = sCenterService.getCenter(user, centername);
        Student student = getStudentEntity(center, studentId);
        requireNotNull(student, "Student not found");
        Guruh guruhEntity = groupService.getGuruhEntity(center, payload.getGroupId());
        requireNotNull(guruhEntity, "Group Not Found");
        student.setGuruh(guruhEntity);
        System.out.println(student.getGuruh());
        studentDao.save(student);
        return groupService.convertToGroupDto(guruhEntity);
    }

    @Transactional
    public void removeGroupFromStudent(String username, String centername, Long studentId) {
        User user = userService.getUser(username);
        SCenter center = sCenterService.getCenter(user, centername);
        Student student = studentDao.findByScenterAndId(center, studentId);
        Guruh guruh = student.getGuruh();
        requireNotNull(student, "Student not found");
        requireNotNull(guruh, "Student has no group");
        student.setGuruh(null); // todo check this line
        guruh.removeStudent(student);
        studentDao.save(student);
    }

    @Transactional
    public void removeSubjectFromStudent(String username, String centername, Long studentId, String subjectName){
        User user = userService.getUser(username);
        SCenter center = sCenterService.getCenter(user, centername);
        Student student = studentDao.findByScenterAndId(center, studentId);
        Subject subject = subjectService.getSubjectByCenterAndName(center, subjectName);
        requireNotNull(student, "Student not found");
        requireNotNull(subject, "Subject not found");
        if(!student.getSubjects().contains(subject)) return;
        student.removeSubject(subject);
        subject.removeStudent(student);
        studentDao.save(student);
    }

    @Transactional
    public StudentDto addSubjectToStudent(String username, String centername, Long studentId, AddSubjectToStudentPayload payload) {
        User user = userService.getUser(username);
        SCenter center = sCenterService.getCenter(user, centername);
        Student student = studentDao.findByScenterAndId(center, studentId);
        requireNotNull(student, "Student not found");
        List<Subject> subjects = payload.getSubjectName().stream().map(studentname -> {
            return subjectService.getSubjectByCenterAndName(center, studentname);
        }).filter(Objects::nonNull).collect(Collectors.toList());

        List<Subject> studentSubjects = student.getSubjects();

        subjects.forEach(subject -> {
            if (studentSubjects.contains(subject)) return;
            student.addSubject(subject);
            subject.addStudent(student);
        });
        Student savedStudent = studentDao.save(student);
        return convertToStudentDto(savedStudent);
    }

    @Transactional
    public List<SubjectDto> getSubjects(String username, String centername, Long studentId) {
        User user = userService.getUser(username);
        SCenter center = sCenterService.getCenter(user, centername);
        Student studentEntity = getStudentEntity(center, studentId);
        requireNotNull(studentEntity, "Student not found");
        return studentEntity
                .getSubjects()
                .stream()
                .map(subjectService::convertToSubjectDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public GroupDto getStudentGroup(String username, String centername, Long studentId) {
        User user = userService.getUser(username);
        SCenter center = sCenterService.getCenter(user, centername);
        Student studentEntity = getStudentEntity(center, studentId);
        requireNotNull(studentEntity, "Student not found");
        Guruh guruh = studentEntity.getGuruh();
        return groupService.convertToGroupDto(guruh);
    }

    @Transactional
    public List<BlockTestDto> getBlockTest(String username, String centername, Long studentId){
        User user = userService.getUser(username);
        SCenter center = sCenterService.getCenter(user, centername);
        Student studentEntity = getStudentEntity(center, studentId);
        requireNotNull(studentEntity, "Student not found");
        return studentEntity.getBlockTests().stream().map(blockTestService::convertToBlockTestDto).collect(Collectors.toList());

    }

    public StudentDto convertToStudentDto(Student student) {
        if (student == null) return null;
        return StudentDto.builder()
                .firstname(student.getFirstname())
                .lastname(student.getLastname())
                .id(student.getId())
                .build();
    }

}
