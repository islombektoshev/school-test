package uz.owl.schooltest.web.rest;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.owl.schooltest.dto.blocktest.BlockTestDto;
import uz.owl.schooltest.dto.student.StudentDto;
import uz.owl.schooltest.dto.subject.SubjectDto;
import uz.owl.schooltest.dto.subject.SubjectPayload;
import uz.owl.schooltest.exception.CenterNotFoundException;
import uz.owl.schooltest.exception.CoundtCreatedExeption;
import uz.owl.schooltest.exception.UserNotFoundException;
import uz.owl.schooltest.service.SubjectService;
import uz.owl.schooltest.web.Message;
import uz.owl.schooltest.web.rest.proto.SubjectProto;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
public class SubjectRestController implements SubjectProto {
    private static final String RESOURCE_URL = "/api/v1/centers/{centername}/subjects";
    private final SubjectService subjectService;

    public SubjectRestController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Override
    @GetMapping(RESOURCE_URL)
    public List<Resource<SubjectDto>> getAllSubjects(Principal principal, @PathVariable String centername) throws UserNotFoundException, CenterNotFoundException {
        List<SubjectDto> subjectByUsernameAnsCentername = subjectService.getSubjectByUsernameAndCentername(principal.getName(), centername);
        return subjectByUsernameAnsCentername.stream().map(subject -> {
                    Resource<SubjectDto> resource = new Resource<>(subject);
                    links(resource, principal, centername, subject.getName());
                    return resource;
                }
        ).collect(Collectors.toList());
    }

    @Override
    @GetMapping(RESOURCE_URL + "/{subjectname}")
    public Resource<SubjectDto> getSingleSubject(Principal principal, @PathVariable String centername, @PathVariable String subjectname) throws UserNotFoundException, CenterNotFoundException {
        SubjectDto subjectDto = subjectService.getSubjectDtoByUsernameAndCenternameAndSubjectname(principal.getName(), centername, subjectname);
        Resource<SubjectDto> subjectDtoResource = new Resource<>(subjectDto);
        links(subjectDtoResource, principal, centername, subjectname);
        return subjectDtoResource;
    }

    @Override
    @PostMapping(RESOURCE_URL)
    public ResponseEntity<Resource<Message>> saveSubject(Principal principal, @PathVariable String centername, @Valid @RequestBody SubjectPayload subjectPayload) throws UserNotFoundException, CenterNotFoundException, CoundtCreatedExeption {
        subjectPayload.setCentername(centername);
        SubjectDto subjectDto = subjectService.saveSubject(principal.getName(), subjectPayload);
        Resource<Message> subject_created = new Resource<>(new Message(201, "Subject created"));
        links(subject_created, principal, centername, subjectDto.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(subject_created);
    }

    @Override
    @PutMapping(RESOURCE_URL + "/{subjectname}")
    public ResponseEntity<Resource<Message>> updateSubject(Principal principal, @PathVariable String centername, @PathVariable String subjectname, @Valid @RequestBody SubjectPayload subjectPayload) throws UserNotFoundException, CenterNotFoundException {
        subjectPayload.setCentername(centername);
        String username = principal.getName();
        SubjectDto subjectDto = subjectService.updateSubject(username, subjectname, subjectPayload);
        Resource<Message> updated = new Resource<>(new Message(200, "Updated"));
        links(updated, principal, centername, subjectDto.getName());
        return ResponseEntity.ok(updated);
    }

    @Override
    @GetMapping(RESOURCE_URL + "/{subjectname}/students")
    public List<Resource<StudentDto>> getStudents(Principal principal, @PathVariable String centername, @PathVariable String subjectname) {
        List<Resource<StudentDto>> collect = subjectService.getSubjectStudents(principal.getName(), centername, subjectname).stream().parallel()
                .map(studentDto -> {
                    Resource<StudentDto> resource = new Resource(studentDto);
                    StudentRestController.links(resource, principal, centername, studentDto.getId());
                    return resource;
                }).collect(Collectors.toList());
        return collect;
    }

    @Override
    @GetMapping(RESOURCE_URL + "/{subjectname}/groups")
    public List<Resource<BlockTestDto>> getBlockTests(Principal principal, @PathVariable String centername, @PathVariable String subjectname) {
        List<Resource<BlockTestDto>> collect = subjectService.getSubjectBlockTest(principal.getName(), centername, subjectname).stream().parallel()
                .map(blockTestDto -> {
                    Resource<BlockTestDto> resource = new Resource<>(blockTestDto);
                    BlockTestRestController.links(resource, principal, centername, blockTestDto.getId());
                    return resource;
                }).collect(Collectors.toList());
        return collect;
    }

    @Override
    @DeleteMapping(RESOURCE_URL + "/{subjectname}")
    public ResponseEntity<Resource<Message>> deleteSubject(Principal principal, @PathVariable String centername, @PathVariable String subjectname) throws UserNotFoundException, CenterNotFoundException {
        String username = principal.getName();
        subjectService.deleteSubject(username, centername, subjectname);
        return ResponseEntity.ok(new Resource<Message>(new Message(200, "Deleted")));
    }

    public static void links(Resource resource, Principal principal, String centername, String subjectName) {
        Link all_subjects = linkTo(methodOn(SubjectRestController.class).getAllSubjects(principal, centername)).withRel("all_subjects");
        Link self = linkTo(methodOn(SubjectRestController.class).getSingleSubject(principal, centername, subjectName)).withSelfRel();
        Link students = linkTo(methodOn(SubjectRestController.class).getStudents(principal, centername, subjectName)).withRel("students");
        Link blocktests = linkTo(methodOn(SubjectRestController.class).getBlockTests(principal, centername, subjectName)).withRel("blocktests");
        resource.add(blocktests);
        resource.add(students);
        resource.add(self);
        resource.add(all_subjects);
    }
}
