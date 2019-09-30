package uz.owl.schooltest.web.rest;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uz.owl.schooltest.dto.blocktest.BlockTestDto;
import uz.owl.schooltest.dto.group.GroupDto;
import uz.owl.schooltest.dto.group.GroupPayload;
import uz.owl.schooltest.dto.student.StudentDto;
import uz.owl.schooltest.entity.BlockTest;
import uz.owl.schooltest.service.GroupService;
import uz.owl.schooltest.web.Message;
import uz.owl.schooltest.web.rest.proto.GroupProto;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class GroupRestController implements GroupProto {
    private final GroupService groupService;
    private final StudentRestController studentRestController;
    private final BlockTestRestController blockTestRestController;
    private static final String RESOURCE_URI = "/api/v1/centers/{centername}/groups";

    public GroupRestController(GroupService groupService, StudentRestController studentRestController, BlockTestRestController blockTestRestController) {
        this.groupService = groupService;
        this.studentRestController = studentRestController;
        this.blockTestRestController = blockTestRestController;
    }

    @Override
    @GetMapping(RESOURCE_URI)
    public List<Resource<GroupDto>> getAllGroups(Principal principal, @PathVariable String centername) {
        return groupService.getAllByCenter(principal.getName(), centername).stream().map(Resource<GroupDto>::new).collect(Collectors.toList());
    }

    @Override
    @GetMapping(RESOURCE_URI + "/{groupid}")
    public Resource<GroupDto> getSingleGroup(Principal principal, @PathVariable String centername, @PathVariable Long groupid) {
        Resource<GroupDto>  groupDtoResource = new Resource<>(groupService.getSingle(principal.getName(), centername, groupid));
        links(groupDtoResource, principal, centername, groupid);
        return groupDtoResource;
    }

    @Override
    @PostMapping(RESOURCE_URI)
    public ResponseEntity<Resource<Message>> saveGroup(Principal principal, @PathVariable String centername, @Valid @RequestBody GroupPayload groupPayload) {
        GroupDto groupDto = groupService.save(principal.getName(), centername, groupPayload.getName());
        Resource<Message> messageResource = new Resource<>(new Message(201, "Message Created"));
        links(messageResource, principal, centername, groupDto.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(messageResource);
    }

    @Override
    @PutMapping(RESOURCE_URI + "/{groupid}")
    public ResponseEntity<Resource<Message>> updateGroup(Principal principal, @PathVariable String centername, @PathVariable Long groupid,@RequestBody GroupPayload payload) {
        GroupDto updateGroup = groupService.update(principal.getName(), centername, payload.getName(), groupid);
        return ResponseEntity.ok(new Resource<Message>(new Message(200, "Updated"))); // TODO: 9/25/2019 linklarni tayyorlash kerak
    }

    @Override
    public List<Resource<StudentDto>> getGroupStudents(Principal principal, String centername, Long groupId) {
        List<StudentDto> guruhStudents = groupService.getGroupStudents(principal.getName(), centername, groupId);
        List<Resource<StudentDto>> collect = guruhStudents.stream().map(studentDto -> {
            Resource<StudentDto> resource = new Resource<>(studentDto);
            studentRestController.links(resource, principal, centername, studentDto.getId());
            return resource;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public List<Resource<BlockTestDto>> getGuruhBlockTests(Principal principal, String centernema, Long groupId) {
        List<BlockTestDto> guruhBlockTest = groupService.getGroupBlockTest(principal.getName(), centernema, groupId);
        List<Resource<BlockTestDto>> collect = guruhBlockTest.stream().map(blockTestDto -> {
            Resource<BlockTestDto> resource = new Resource<>(blockTestDto);
            blockTestRestController.links(resource, principal, centernema, blockTestDto.getId());
            return resource;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    @DeleteMapping(RESOURCE_URI + "/{groupid}")
    public ResponseEntity<Resource<Message>> deleteGroup(Principal principal, @PathVariable String centername, @PathVariable Long groupid){
        groupService.deleteByCenter(principal.getName(), centername, groupid);
        URI uri = linkTo(methodOn(getClass()).getAllGroups(principal, centername)).toUri();
        Message message = new Message(200, "Deleted").all(uri);
        return ResponseEntity.ok(new Resource(message));
    }

    public static void links(Resource resource, Principal principal, String centername, Long groupId){
        Link all_groups = linkTo(methodOn(GroupRestController.class).getAllGroups(principal, centername)).withRel("all_groups");
        Link self = linkTo(methodOn(GroupRestController.class).getSingleGroup(principal, centername, groupId)).withRel("self");
        resource.add(all_groups);
        resource.add(self); // TODO: 9/28/2019 qolgan linklarni qo'shish kerak
    }
}
