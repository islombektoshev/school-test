package uz.owl.schooltest.service;

import org.springframework.stereotype.Service;
import uz.owl.schooltest.dao.BlockTestDao;
import uz.owl.schooltest.dto.blocktest.BlockTestDto;
import uz.owl.schooltest.dto.blocktest.BlockTestPayload;
import uz.owl.schooltest.dto.blocktest.CreateBlockTestPayload;
import uz.owl.schooltest.dto.group.GroupDto;
import uz.owl.schooltest.dto.student.StudentDto;
import uz.owl.schooltest.dto.subject.SubjectDto;
import uz.owl.schooltest.entity.*;
import uz.owl.schooltest.exception.NotFoudException;
import uz.owl.schooltest.web.rest.ControllerTool;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BlockTestService {
    private final UserService userService;
    private final SCenterService sCenterService;
    private final GroupService groupService;
    private final SubjectService subjectService;
    private final StudentService studentService;
    private final BlockTestDao blockTestDao;

    public BlockTestService(UserService userService, SCenterService sCenterService, GroupService groupService, SubjectService subjectService, StudentService studentService, BlockTestDao blockTestDao) {
        this.userService = userService;
        this.sCenterService = sCenterService;
        this.groupService = groupService;
        this.subjectService = subjectService;
        this.studentService = studentService;
        this.blockTestDao = blockTestDao;
    }

    BlockTest getBlockTestEntity(String username, String centername, Long bId) {
        User user = userService.getUser(username);
        SCenter center = sCenterService.getCenter(user, centername);
        BlockTest byScenterAndId = blockTestDao.findByScenterAndId(center, bId);
        return byScenterAndId;
    }

    List<BlockTest> getAllBlockTestEntity(String username, String centername) {
        User user = userService.getUser(username);
        SCenter center = sCenterService.getCenter(user, centername);
        return blockTestDao.findAllByScenter(center);
    }

    public List<BlockTestDto> getAllBlockTest(String username, String centername) {
        return getAllBlockTestEntity(username, centername).stream().map(BlockTestService::convertToBlockTestDto).collect(Collectors.toList());
    }

    public BlockTestDto getBlockTest(String username, String centername, Long id) {
        BlockTestDto blockTestDto = convertToBlockTestDto(getBlockTestEntity(username, centername, id));
        if (blockTestDto == null) {
            throw new NotFoudException("Block Test Not Found");
        }
        return blockTestDto;
    }

    @Deprecated() // use createBlockTest method
    public BlockTestDto saveBlockTest(String username, String centername, BlockTestPayload blockTestPayload) {
        User user = userService.getUser(username);
        SCenter center = sCenterService.getCenter(user, centername);
        BlockTest blockTest = BlockTest.builder()
                .countOfQuestion(blockTestPayload.getCountOfQuestion())
                .name(blockTestPayload.getName())
                .scenter(center)
                .build();
        center.addBlockTest(blockTest);
        blockTest.setScenter(center);
        blockTestDao.save(blockTest);
        return convertToBlockTestDto(blockTest);
    }

    @Transactional
    public BlockTestDto createBlockTest(String username, String centername, CreateBlockTestPayload payload) {
        User user = userService.getUser(username);
        SCenter center = sCenterService.getCenter(user, centername);
        BlockTest blockTest = BlockTest.builder()
                .countOfQuestion(payload.getCountOfQuestion())
                .name(payload.getName())
                .scenter(center)
                .build();
        blockTest = blockTestDao.save(blockTest);
        final BlockTest finalBlockTest = blockTest;
        List<Guruh> guruhs = payload.getGroups().stream().map(groupId -> {
            Guruh guruh = groupService.getGuruhEntity(center, groupId.longValue());
            if (guruh != null)
                guruh.addBlockTest(finalBlockTest);
            return guruh;
        })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        final BlockTest finalBlockTest1 = blockTest;
        List<Subject> subjects = payload.getSubjects().stream().map(subjectName -> {
            Subject subject = subjectService.getSubjectByCenterAndName(center, subjectName);
            if (subject != null)
                subject.addBlockTest(finalBlockTest1);
            return subject;
        }).filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (guruhs.isEmpty() || subjects.isEmpty()) return null;
        BlockTest savedBlockTest = blockTestDao.save(blockTest);
        System.out.println(savedBlockTest);
        Long blockTestId = savedBlockTest.getId();
        blockTestDao.generateStudent(blockTestId);

        return convertToBlockTestDto(savedBlockTest);
    }


    @Transactional
    public void deleteBlockTest(String username, String centername, Long blockId) {
        User user = userService.getUser(username);
        SCenter center = sCenterService.getCenter(user, centername);
        BlockTest blockTest = blockTestDao.findByScenterAndId(center, blockId);
        if (blockTest == null) return;
        blockTestDao.deleteBlockTest(blockTest.getId());
    }

    @Transactional
    public List<StudentDto> getStudents(String username, String centername, Long blockId) {
        BlockTest blockTestEntity = getBlockTestEntity(username, centername, blockId);
        return blockTestEntity.getStudents().stream().map(StudentService::convertToStudentDto).collect(Collectors.toList());
    }

    public List<GroupDto> getGroups(String username, String centername, Long blockId) {
        BlockTest blockTestEntity = getBlockTestEntity(username, centername, blockId);
        return blockTestEntity.getGuruhs().stream().map(groupService::convertToGroupDto).collect(Collectors.toList());
    }

    public List<SubjectDto> getSubjects(String username, String centername, Long blockId) {
        BlockTest blockTestEntity = getBlockTestEntity(username, centername, blockId);
        return blockTestEntity.getSubjects().stream().map(subjectService::convertToSubjectDto).collect(Collectors.toList());
    }

    @Transactional
    public boolean addStudent(String username, String centername, Long blockId, Long studentId) {
        User user = userService.getUser(username);
        SCenter center = sCenterService.getCenter(user, centername);
        BlockTest blockTest = blockTestDao.findByScenterAndId(center, blockId);
        Student studentEntity = studentService.getStudentEntity(center, studentId);

        if (blockTest.getStudents().contains(studentEntity)) return false;
        blockTest.addStudent(studentEntity);
        studentEntity.addBlockTest(blockTest);
        blockTestDao.save(blockTest);
        return true;
    }

    @Transactional
    public boolean removeStudent(String username, String centername, Long blockId, Long studentId) {
        User user = userService.getUser(username);
        SCenter center = sCenterService.getCenter(user, centername);
        BlockTest blockTest = blockTestDao.findByScenterAndId(center, blockId);
        Student studentEntity = studentService.getStudentEntity(center, studentId);

        if (blockTest.getStudents().contains(studentEntity)) return false;
        blockTest.addStudent(studentEntity);
        studentEntity.addBlockTest(blockTest);
        blockTestDao.save(blockTest);
        return true;
    }

    @Transactional
    public boolean addGroup(String username, String centername, Long blockId, Long groupId) {
        User user = userService.getUser(username);
        SCenter center = sCenterService.getCenter(user, centername);
        BlockTest blockTest = blockTestDao.findByScenterAndId(center, blockId);
        Guruh guruh = groupService.getGuruhEntity(center, groupId);

        ControllerTool.requireNotNull(blockTest, "BlockTest Not Found");
        ControllerTool.requireNotNull(guruh, "Student Not Found");
        if (blockTest.getGuruhs().contains(guruh)) return false;

        // clear generated students
        blockTestDao.deleteGeneratableStudents(blockTest.getId());
        blockTest.addGuruh(guruh);
        guruh.addBlockTest(blockTest);
        blockTestDao.save(blockTest);
        // generate new students
        blockTestDao.generateStudent(blockTest.getId());
        return true;
    }

    @Transactional
    public boolean removeGroup(String username, String centername, Long blockId, Long groupId) {
        User user = userService.getUser(username);
        SCenter center = sCenterService.getCenter(user, centername);
        BlockTest blockTest = blockTestDao.findByScenterAndId(center, blockId);
        Guruh guruh = groupService.getGuruhEntity(center, groupId);
        ControllerTool.requireNotNull(blockTest, "BlockTest Not Found");
        ControllerTool.requireNotNull(guruh, "Student Not Found");

        if (!blockTest.getGuruhs().contains(guruh)) return false;

        // clear generated students
        blockTestDao.deleteGeneratableStudents(blockTest.getId());
        blockTest.removeGuruh(guruh);
        guruh.removeBlockTest(blockTest);
        blockTestDao.save(blockTest);
        // generate new students

        blockTestDao.generateStudent(blockTest.getId());
        return true;
    }

    @Transactional
    public boolean addSubject(String username, String centername, Long blockId, String subjectName) {
        User user = userService.getUser(username);
        SCenter center = sCenterService.getCenter(user, centername);
        BlockTest blockTest = blockTestDao.findByScenterAndId(center, blockId);
        Subject subject = subjectService.getSubjectByCenterAndName(center, subjectName);
        ControllerTool.requireNotNull(blockTest, "BlockTest Not Found");
        ControllerTool.requireNotNull(subject, "Student Not Found");
        if (blockTest.getSubjects().contains(subject)) return false;

        // clear generated students
        blockTestDao.deleteGeneratableStudents(blockTest.getId());
        blockTest.addSubject(subject);
        subject.addBlockTest(blockTest);
        blockTestDao.save(blockTest);

        // generate new students
        blockTestDao.generateStudent(blockTest.getId());
        return true;
    }

    @Transactional
    public boolean removeSubject(String username, String centername, Long blockId, String subjectName) {
        User user = userService.getUser(username);
        SCenter center = sCenterService.getCenter(user, centername);
        BlockTest blockTest = blockTestDao.findByScenterAndId(center, blockId);
        Subject subject = subjectService.getSubjectByCenterAndName(center, subjectName);
        ControllerTool.requireNotNull(blockTest, "BlockTest Not Found");
        ControllerTool.requireNotNull(subject, "Student Not Found");
        if (!blockTest.getSubjects().contains(subject)) return false;

        // clear generated students
        blockTestDao.deleteGeneratableStudents(blockTest.getId());
        blockTest.removeSubject(subject);
        subject.removeBlockTest(blockTest);
        blockTestDao.save(blockTest);

        // generate new students
        blockTestDao.generateStudent(blockTest.getId());
        return true;
    }


    public static BlockTestDto convertToBlockTestDto(BlockTest blockTest) {
        if (blockTest == null) return null;
        return BlockTestDto.builder()
                .name(blockTest.getName())
                .countOfQuestion(blockTest.getCountOfQuestion())
                .id(blockTest.getId())
                .build();
    }
}
