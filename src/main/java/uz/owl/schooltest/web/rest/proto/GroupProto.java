package uz.owl.schooltest.web.rest.proto;

import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import uz.owl.schooltest.dto.blocktest.BlockTestDto;
import uz.owl.schooltest.dto.group.GroupDto;
import uz.owl.schooltest.dto.group.GroupPayload;
import uz.owl.schooltest.dto.student.StudentDto;
import uz.owl.schooltest.entity.BlockTest;
import uz.owl.schooltest.web.Message;

import java.security.Principal;
import java.util.List;

public interface GroupProto {

    List<Resource<GroupDto>> getAllGroups(Principal principal, String centername);

    Resource<GroupDto> getSingleGroup(Principal principal, String centername, Long groupId);

    ResponseEntity<Resource<Message>> deleteGroup(Principal principal, String centername, Long groupId);

    ResponseEntity<Resource<Message>> saveGroup(Principal principal, String centername, GroupPayload payload);

    ResponseEntity<Resource<Message>> updateGroup(Principal principal, String centername, Long groupid, GroupPayload payload);

    List<Resource<StudentDto>> getGroupStudents(Principal principal, String centername, Long groupId);

    List<Resource<BlockTestDto>> getGuruhBlockTests(Principal principal, String centernema, Long groupId);
}
