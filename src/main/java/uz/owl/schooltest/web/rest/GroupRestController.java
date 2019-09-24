package uz.owl.schooltest.web.rest;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import uz.owl.schooltest.dto.GroupDto;
import uz.owl.schooltest.dto.GroupPayload;
import uz.owl.schooltest.service.GroupService;
import uz.owl.schooltest.web.Message;

import javax.validation.Valid;
import java.net.URI;
import java.security.Principal;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class GroupRestController {
    private final GroupService groupService;
    private static final String RESOURCE_URI = "${api.v1}centers/{centername}/groups";

    public GroupRestController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping(RESOURCE_URI)
    public List<GroupDto> getAllGroups(Principal principal, @PathVariable String centername) {
        return groupService.getAllByCenter(principal.getName(), centername);
    }

//    @GetMapping(RESOURCE_URI + "/{groupid}")
//    public ResponseEntity<GroupDto> getSingleGroup(Principal principal, @PathVariable String centername,
//                                   @PathVariable Long groupid) {
//        return ResponseEntity.ok(groupService.getSingle(principal.getName(), centername, groupid));
//    }

    @GetMapping(RESOURCE_URI + "/{groupid}")
    public Resource<GroupDto> getSingleGroup(Principal principal, @PathVariable String centername,
                                                   @PathVariable Long groupid) {
        Resource<GroupDto>  groupDtoResource = new Resource<>(groupService.getSingle(principal.getName(), centername, groupid));

        ControllerLinkBuilder link = linkTo(methodOn(this.getClass()).getAllGroups(principal, centername));

        groupDtoResource.add(link.withRel("all-groups"));

        return groupDtoResource;
    }

    @PostMapping(RESOURCE_URI)
    public ResponseEntity<Message> addGroup(Principal principal, @PathVariable String centername, @Valid @RequestBody GroupPayload groupPayload) {
        GroupDto groupDto = groupService.save(principal.getName(), centername, groupPayload.getName());
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{groupid}")
                .buildAndExpand(groupDto.getId()).toUri();
        Message created = new Message(201, "Created").self(location);
        System.out.println(created );
        return ResponseEntity.created(location).body(created);
    }

    @DeleteMapping(RESOURCE_URI + "/{groupid}")
    public ResponseEntity<Message> delete(Principal principal, @PathVariable String centername, @PathVariable Long groupid){
        groupService.deleteByCenter(principal.getName(),
                centername, groupid);
        URI uri = linkTo(methodOn(getClass()).getAllGroups(principal, centername)).toUri();
        Message message = new Message(200, "Deleted").all(uri);
        return ResponseEntity.ok(message);
    }

}
