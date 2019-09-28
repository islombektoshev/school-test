package uz.owl.schooltest.service;

import org.omg.CosNaming.NamingContextExtPackage.StringNameHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.owl.schooltest.dao.GroupDao;
import uz.owl.schooltest.dto.blocktest.BlockTestDto;
import uz.owl.schooltest.dto.group.GroupDto;
import uz.owl.schooltest.dto.student.StudentDto;
import uz.owl.schooltest.entity.*;
import uz.owl.schooltest.exception.CenterNotFoundException;
import uz.owl.schooltest.exception.NotFoudException;

import javax.transaction.Transactional;
import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupService {
    @Autowired
    private  GroupDao groupDao;
    @Autowired
    private  UserService userService;
    @Autowired
    private  SCenterService sCenterService;
    @Autowired
    private  StudentService studentService;
    @Autowired
    private  BlockTestService blockTestService;

    public GroupService() {
    }

    public List<GroupDto> getAllByCenter(String username, String centername) {
        User user = userService.getUser(username);
        SCenter center = getCenter(user, centername);
        List<Guruh> allByScenter = groupDao.findAllByScenter(center);
        List<GroupDto> groupDtos = allByScenter.stream().map(this::convertToGroupDto).collect(Collectors.toList());
        return groupDtos;
    }

    @Transactional
    public GroupDto getSingle(String username, String centername, Long id) {
        User user = userService.getUser(username);
        SCenter center = getCenter(user, centername);
        Guruh guruhEntity = getGuruhEntity(center, id);
        if (guruhEntity == null) {
            throw new NotFoudException("Group not found");
        }
        return convertToGroupDto(guruhEntity);
    }

    Guruh getGuruhEntity(SCenter center, Long id) {
        Optional<Guruh> byId = groupDao.findById(id);
        Guruh guruh = null;
        try {
            guruh = byId.get();
            if (guruh.getScenter().getId() != center.getId()) return null;
        } catch (Exception ignored) {
        }
        return guruh;
    }

    @Transactional
    List<Student> getGuruhStudentEntities(SCenter sCenter, Long guruhId){
        Guruh guruhEntity = getGuruhEntity(sCenter, guruhId);
        return guruhEntity.getStudents();
    }

    public List<StudentDto> getGroupStudents(String username, String centername, Long guruhId){
        User user = userService.getUser(username);
        SCenter center = sCenterService.getCenter(user, centername);
        return getGuruhStudentEntities(center, guruhId).stream().map(studentService::convertToStudentDto).collect(Collectors.toList());
    }


    @Transactional
    List<BlockTest> getGuruhBlockTestEntities(SCenter sCenter, Long guruhId){
        return getGuruhEntity(sCenter, guruhId).getBlockTests();
    }

    public List<BlockTestDto> getGroupBlockTest(String username, String centername, Long guruhId){
        User user = userService.getUser(username);
        SCenter center = sCenterService.getCenter(user, centername);
        return getGuruhBlockTestEntities(center, guruhId).stream().map(blockTestService::convertToBlockTestDto).collect(Collectors.toList());
    }

    public GroupDto save(String username, String centername, String groupname) {
        User user = userService.getUser(username);
        SCenter center = getCenter(user, centername);
        Guruh guruh = new Guruh();
        guruh.setName(groupname);
        guruh.setScenter(center);
        Guruh newGroup = groupDao.save(guruh);
        System.out.println(newGroup);
        return convertToGroupDto(newGroup);
    }

    public GroupDto update(String username, String centername, String name, Long groupid) {
        User user = userService.getUser(username);
        SCenter center = getCenter(user, centername);
        Optional<Guruh> byId = groupDao.findById(groupid);
        Guruh guruh = null;
        try {
            guruh = byId.get();
            if (guruh.getScenter().getId() == center.getId()) {
                if (!guruh.getName().equals(name)) {
                    guruh.setName(name);
                    guruh = groupDao.save(guruh);
                }
                return convertToGroupDto(guruh);
            }
        } catch (Exception e) {
        }
        throw new NotFoudException("Group not found");
    }


    @Transactional
    public void deleteByCenter(String username, String centername, Long groupid) {
        User user = userService.getUser(username);
        SCenter center = getCenter(user, centername);
        Optional<Guruh> byId = groupDao.findById(groupid);
        byId.ifPresent(guruh -> {
            if (guruh.getScenter().getId().equals(center.getId())) {
                groupDao.delete(byId.get());
            }
        });
        byId.orElseThrow(() -> new NotFoudException("Group Not Found"));
    }


    private SCenter getCenter(User user, String centername) throws CenterNotFoundException {
        SCenter byAuthorEntityAndName = sCenterService.getByAuthorEntityAndName(user, centername);
        return byAuthorEntityAndName;
    }

    GroupDto convertToGroupDto(Guruh guruh) {
        if (guruh == null) return null;
        return GroupDto.builder().name(guruh.getName()).id(guruh.getId()).build();
    }
}
