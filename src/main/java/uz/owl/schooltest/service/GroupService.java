package uz.owl.schooltest.service;

import org.springframework.stereotype.Service;
import uz.owl.schooltest.dao.GroupDao;
import uz.owl.schooltest.dto.GroupDto;
import uz.owl.schooltest.entity.Guruh;
import uz.owl.schooltest.entity.SCenter;
import uz.owl.schooltest.entity.User;
import uz.owl.schooltest.exception.CenterNotFoundException;
import uz.owl.schooltest.exception.NotFoudException;
import uz.owl.schooltest.exception.UserNotFoundException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GroupService {
    private final GroupDao groupDao;
    private final UserService userService;
    private final SCenterService sCenterService;

    public GroupService(GroupDao groupDao, UserService userService, SCenterService sCenterService) {
        this.groupDao = groupDao;
        this.userService = userService;
        this.sCenterService = sCenterService;
    }

    public List<GroupDto> getAllByCenter(String username, String centername) {
        User user = getUser(username);
        SCenter center = getCenter(user, centername);
        List<Guruh> allByScenter = groupDao.findAllByScenter(center);
        List<GroupDto> groupDtos = allByScenter.stream().map(this::convertToGroupDto).collect(Collectors.toList());
        return groupDtos;
    }

    @Transactional
    public GroupDto getSingle(String username, String centername, Long id) {
        User user = getUser(username);
        SCenter center = getCenter(user, centername);
        Optional<Guruh> byId = groupDao.findById(id);
        Guruh guruh = null;
        try {
            guruh = byId.get();
            if (guruh.getScenter().getId() == center.getId()) {
                return convertToGroupDto(guruh);
            }
        } catch (Exception e) {
        }
        throw new NotFoudException("Group not found");
    }

    public GroupDto save(String username, String centername, String groupname) {
        User user = getUser(username);
        SCenter center = getCenter(user, centername);
        Guruh guruh = new Guruh();
        guruh.setName(groupname);
        guruh.setScenter(center);
        Guruh newGroup = groupDao.save(guruh);
        System.out.println(newGroup);
        return convertToGroupDto(newGroup);
    }

    public void deleteByCenter(String username, String centername, Long groupid) {
        User user = getUser(username);
        SCenter center = getCenter(user, centername);
        Optional<Guruh> byId = groupDao.findById(groupid);
        byId.ifPresent(guruh -> {
            if (guruh.getScenter().getId().equals(center.getId())) {
                groupDao.delete(byId.get());
            }
        });
    }


    private User getUser(String username, String s) throws UserNotFoundException {
        User user = userService.findByUsername(username);
        if (user == null) throw new UserNotFoundException(s, username);
        return user;
    }

    private User getUser(String username) throws UserNotFoundException {
        return getUser(username, "User not found");
    }

    private SCenter getCenter(User user, String centername) throws CenterNotFoundException {
        SCenter byAuthorEntityAndName = sCenterService.getByAuthorEntityAndName(user, centername);
        return byAuthorEntityAndName;
    }

    private GroupDto convertToGroupDto(Guruh guruh) {
        return GroupDto.builder().name(guruh.getName()).id(guruh.getId()).build();
    }
}
