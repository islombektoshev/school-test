package uz.owl.schooltest.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.owl.schooltest.dto.blocktest.BlockTestDto;
import uz.owl.schooltest.dto.blocktest.BlockTestPayload;
import uz.owl.schooltest.dto.blocktest.CreateBlockTestPayload;
import uz.owl.schooltest.dto.group.GroupDto;
import uz.owl.schooltest.dto.student.StudentDto;
import uz.owl.schooltest.dto.subject.SubjectDto;
import uz.owl.schooltest.service.BlockTestService;
import uz.owl.schooltest.web.Message;
import uz.owl.schooltest.web.rest.proto.BlockTestProto;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class BlockTestRestController implements BlockTestProto {


    private final String RESOURCE_URL = "/api/v1/centers/{centername}/blocktests";
    private final BlockTestService blockTestService;
    @Autowired
    SubjectRestController subjectRestController;

    public BlockTestRestController(BlockTestService blockTestService) {
        this.blockTestService = blockTestService;
    }

    @Override
    @GetMapping(RESOURCE_URL)
    public List<Resource<BlockTestDto>> getAllBlockTest(Principal principal, @PathVariable String centername) {
        return blockTestService.getAllBlockTest(principal.getName(), centername).stream().map(blockTestDto -> {
            Resource<BlockTestDto> resource = new Resource<>(blockTestDto);
            links(resource, principal, centername, blockTestDto.getId());
            return resource;
        }).collect(Collectors.toList());
    }

    @Override
    @GetMapping(RESOURCE_URL + "/{blockTestId}")
    public Resource<BlockTestDto> getBlockTest(Principal principal, @PathVariable String centername, @PathVariable Long blockTestId) {
        Resource<BlockTestDto> resource = new Resource<>(blockTestService.getBlockTest(principal.getName(), centername, blockTestId));
        links(resource, principal, centername, blockTestId);
        return resource;
    }

    @Override
    @PostMapping(RESOURCE_URL)
    public ResponseEntity<Resource<Message>> saveBlockTest(Principal principal, @PathVariable String centername, @Valid @RequestBody CreateBlockTestPayload payload) {
        BlockTestDto blockTest = blockTestService.createBlockTest(principal.getName(), centername, payload);
        Resource<Message> created = new Resource<>(new Message(200, "Created"));
        links(created, principal, centername, blockTest.getId());
        return ResponseEntity.ok(created);
    }

    @Override
//    @PutMapping(RESOURCE_URL + "/{blockTestId}")
    public ResponseEntity<Resource<Message>> updateUpdateBlockTest(Principal principal, @PathVariable String centername, @PathVariable Long blockTestId, @Valid @RequestBody BlockTestPayload payload) {
        return null; // TODO: 9/28/2019 its didn't work
    }

    @Override
    @DeleteMapping(RESOURCE_URL + "/{blockTestId}")
    public ResponseEntity<Resource<Message>> deleteBlockTest(Principal principal, @PathVariable String centername, @PathVariable Long blockTestId) {
        blockTestService.deleteBlockTest(principal.getName(), centername, blockTestId);
        return ResponseEntity.ok(new Resource<Message>(new Message(200, "Deleted")));
    }

//    @Override
//    @PostMapping(RESOURCE_URL + "/{blockTestId}/students/{studentId}")
//    public ResponseEntity<Resource<Message>> addStudentToBlockTest(Principal principal, @PathVariable String centername, @PathVariable Long blockTestId, @PathVariable Long studentId) {
//        return null;
//    }

    @Override
    @PostMapping(RESOURCE_URL + "/{blockTestId}/subjects/{subjectName}")
    public ResponseEntity<Resource<Message>> addSubject(Principal principal, @PathVariable String centername, @PathVariable Long blockTestId, @PathVariable String subjectName) {
        boolean b = blockTestService.addSubject(principal.getName(), centername, blockTestId, subjectName);
        Resource<Message> resource;
        if (b) {
            resource = new Resource<>(new Message(200, "Subject Already has"));
        } else {
            resource = new Resource<>(new Message(200, "Subject added"));
        }
        links(resource, principal, centername, blockTestId);
        return ResponseEntity.ok(resource);
    }

    @Override
    @PostMapping(RESOURCE_URL + "/{blockTestId}/groups/{groupId}")
    public ResponseEntity<Resource<Message>> addGroup(Principal principal, @PathVariable String centername, @PathVariable Long blockTestId, @PathVariable Long groupId) {
        boolean b = blockTestService.addGroup(principal.getName(), centername, blockTestId, groupId);
        Resource<Message> resource;
        if (b) {
            resource = new Resource<>(new Message(200, "Group Already has"));
        } else {
            resource = new Resource<>(new Message(200, "Group added"));
        }
        links(resource, principal, centername, blockTestId);
        return ResponseEntity.ok(resource);
    }

    @Override
    @DeleteMapping(RESOURCE_URL + "/{blockTestId}/students/{studentId}")
    public ResponseEntity<Resource<Message>> removeStudent(Principal principal, @PathVariable String centername, @PathVariable Long blockTestId, @PathVariable Long studentId) {
        boolean b = blockTestService.removeStudent(principal.getName(), centername, blockTestId, studentId);
        Resource<Message> resource;
        if (b) {
            resource = new Resource<>(new Message(200, "Student already removed"));
        } else {
            resource = new Resource<>(new Message(200, "Student removed"));
        }
        links(resource, principal, centername, blockTestId);
        return ResponseEntity.ok(resource);
    }

    @Override
    @DeleteMapping(RESOURCE_URL + "/{blockTestId}/subjects/{subjectName}")
    public ResponseEntity<Resource<Message>> removeSubject(Principal principal, @PathVariable String centername, @PathVariable Long blockTestId, @PathVariable String subjectName) {
        boolean b = blockTestService.removeSubject(principal.getName(), centername, blockTestId, subjectName);
        Resource<Message> resource;
        if (b) {
            resource = new Resource<>(new Message(200, "Subject already removed"));
        } else {
            resource = new Resource<>(new Message(200, "Subject removed"));
        }
        links(resource, principal, centername, blockTestId);
        return ResponseEntity.ok(resource);
    }

    @Override
    @DeleteMapping(RESOURCE_URL + "/{blockTestId}/groups/{groupId}")
    public ResponseEntity<Resource<Message>> removeGroup(Principal principal, @PathVariable String centername, @PathVariable Long blockTestId, @PathVariable Long groupId) {
        boolean b = blockTestService.removeGroup(principal.getName(), centername, blockTestId, groupId);
        Resource<Message> resource;
        if (b) {
            resource = new Resource<>(new Message(200, "Group already removed"));
        } else {
            resource = new Resource<>(new Message(200, "Group removed"));
        }
        links(resource, principal, centername, blockTestId);
        return ResponseEntity.ok(resource);
    }

    @Override
    @GetMapping(RESOURCE_URL + "/{blockTestId}/students")
    public List<Resource<StudentDto>> getStudents(Principal principal, @PathVariable String centername, @PathVariable Long blockTestId) {
        return blockTestService.getStudents(principal.getName(), centername, blockTestId)
                .stream().map(studentDto -> {
                    Resource<StudentDto> resource = new Resource<>(studentDto);
                    StudentRestController.links(resource, principal, centername, studentDto.getId());
                    return resource;
                }).collect(Collectors.toList());
    }

    @Override
    @GetMapping(RESOURCE_URL + "/{blockTestId}/groups")
    public List<Resource<GroupDto>> getGroups(Principal principal, @PathVariable String centername, @PathVariable Long blockTestId) {
        return blockTestService.getGroups(principal.getName(), centername, blockTestId).stream()
                .map(groupDto -> {
                    Resource<GroupDto> groupDtoResource = new Resource<>(groupDto);
                    GroupRestController.links(groupDtoResource, principal, centername, groupDto.getId());
                    return groupDtoResource;
                }).collect(Collectors.toList());
    }

    @Override
    @GetMapping(RESOURCE_URL + "/{blockTestId}/subjects")
    public List<Resource<SubjectDto>> getSubjects(Principal principal, @PathVariable String centername, @PathVariable Long blockTestId) {
        return blockTestService.getSubjects(principal.getName(), centername, blockTestId).stream()
                .map(subjectDto -> {
                    Resource<SubjectDto> subjectDtoResource = new Resource<>(subjectDto);
                    subjectRestController.links(subjectDtoResource, principal, centername, subjectDto.getName());
                    return subjectDtoResource;
                }).collect(Collectors.toList());
    }

    public static void links(Resource resource, Principal principal, String centername, Long blockTestId) {
        Link all_blocktest = linkTo(methodOn(BlockTestRestController.class).getAllBlockTest(principal, centername)).withRel("all_blocktest");
        Link self = linkTo(methodOn(BlockTestRestController.class).getBlockTest(principal, centername, blockTestId)).withSelfRel();
        Link subjects = linkTo(methodOn(BlockTestRestController.class).getSubjects(principal, centername, blockTestId)).withRel("subjects");
        Link groups = linkTo(methodOn(BlockTestRestController.class).getGroups(principal, centername, blockTestId)).withRel("groups");
        Link students = linkTo(methodOn(BlockTestRestController.class).getStudents(principal, centername, blockTestId)).withRel("students");

        resource.add(all_blocktest);
        resource.add(self);
        resource.add(subjects);
        resource.add(groups);
        resource.add(students);
    }
}
