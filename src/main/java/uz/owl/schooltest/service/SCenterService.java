package uz.owl.schooltest.service;

import org.springframework.stereotype.Service;
import uz.owl.schooltest.dao.SCenterDao;
import uz.owl.schooltest.dto.scenter.SCenterDto;
import uz.owl.schooltest.dto.scenter.SCenterPayload;
import uz.owl.schooltest.entity.SCenter;
import uz.owl.schooltest.entity.User;
import uz.owl.schooltest.exception.CenterNotFoundException;
import uz.owl.schooltest.exception.CoundtCreatedExeption;
import uz.owl.schooltest.exception.CoundtUpdatedException;
import uz.owl.schooltest.exception.UserNotFoundException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SCenterService {

    private final SCenterDao sCenterDao;
    private final UserService userService;

    public SCenterService(SCenterDao sCenterDao, UserService userService) {
        this.sCenterDao = sCenterDao;
        this.userService = userService;
    }

    public List<SCenterDto> findAllByUser(String username) throws UserNotFoundException {
        User user = userService.getUser(username, "User not found!");
        List<SCenter> sCenters = sCenterDao.findAllByAuthor(user);
        return sCenters.stream().map(this::convertToSCenterDto).collect(Collectors.toList());
    }

    public SCenterDto findByAuthorAndName(String username, String sCenterName) throws UserNotFoundException, CenterNotFoundException {
        User user = userService.getUser(username, "User not fount");
        SCenter byAuthorAndName = sCenterDao.findByAuthorAndName(user, sCenterName);
        if (byAuthorAndName == null){
            throw new CenterNotFoundException("Center Not Found");
        }
        return convertToSCenterDto(byAuthorAndName);
    }

        public SCenterDto saveForUser(String username, SCenterPayload sCenterPayload) throws CoundtCreatedExeption {
        User user = userService.findByUsername(username);

        SCenter center = SCenter.builder()
                .author(user)
                .name(sCenterPayload.getName())
                .caption(sCenterPayload.getCaption())
                .build();
        try {
            sCenterDao.save(center);
        } catch (Exception e) {
            throw new CoundtCreatedExeption("Center could't created");
        }
        return convertToSCenterDto(center);
    }

    public SCenterDto update(String username,String oldname, SCenterPayload sCenterPayload) throws UserNotFoundException, CoundtUpdatedException {
        User user = userService.getUser(username, "User not found!");
        SCenter center = sCenterDao.findByAuthorAndName(user, oldname);
        if (center == null) {
            throw new CoundtUpdatedException("Center not found");
        }
        center.setName(sCenterPayload.getName());
        center.setCaption(sCenterPayload.getCaption());
        SCenter updatedCenter = null;
        try {
            updatedCenter = sCenterDao.save(center);
        } catch (Exception e) {
            throw new CoundtUpdatedException("Center could't updated");
        }
        return convertToSCenterDto(updatedCenter);
    }


    @Transactional
    public void deleteByAuthorAndName(String author, String name) throws UserNotFoundException {
        User user = userService.getUser(author);
        sCenterDao.deleteByAuthorAndName(user, name);
    }

    SCenter getByAuthorEntityAndName(User user, String sCenterName) throws CenterNotFoundException {
        SCenter byAuthorAndName = sCenterDao.findByAuthorAndName(user, sCenterName);
        if (byAuthorAndName == null){
            throw new CenterNotFoundException("Center Not Found");
        }
        return byAuthorAndName;
    }

    SCenter getByAuthorAndName(String username, String sCenterName) throws CenterNotFoundException, UserNotFoundException {
        User user = userService.getUser(username, "User not fount");
        SCenter byAuthorAndName = sCenterDao.findByAuthorAndName(user, sCenterName);
        if (byAuthorAndName == null){
            throw new CenterNotFoundException("Center Not Found");
        }
        return byAuthorAndName;
    }

    SCenter getCenter(User user, String centername) throws CenterNotFoundException {
        SCenter byAuthorEntityAndName = getByAuthorEntityAndName(user, centername);
        return byAuthorEntityAndName;
    }

    private SCenterDto convertToSCenterDto(SCenter sCenter) {
        return SCenterDto.builder()
                .caption(sCenter.getCaption())
                .name(sCenter.getName())
                .id(sCenter.getId())
                .build();
    }

    public void getCenter(User user) {
    }
}
