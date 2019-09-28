package uz.owl.schooltest.web.rest;

import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.owl.schooltest.dto.subject.SubjectPayload;
import uz.owl.schooltest.dto.subject.SubjectDto;
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

//todo chalarari bor linlarni kodini yozish kerak
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
        return subjectByUsernameAnsCentername.stream().map(Resource<SubjectDto>::new).collect(Collectors.toList());
    }

    @Override
    @GetMapping(RESOURCE_URL + "/{subjectname}")
    public Resource<SubjectDto> getSingleSubject(Principal principal, @PathVariable String centername, @PathVariable String subjectname) throws UserNotFoundException, CenterNotFoundException {
        SubjectDto subjectDto = subjectService.getSubjectDtoByUsernameAndCenternameAndSubjectname(principal.getName(), centername, subjectname);
        return new Resource<>(subjectDto);
    }

    @Override
    @PostMapping(RESOURCE_URL)
    public ResponseEntity<Resource<Message>> saveSubject(Principal principal, @PathVariable String centername, @Valid @RequestBody SubjectPayload subjectPayload) throws UserNotFoundException, CenterNotFoundException, CoundtCreatedExeption {
        subjectPayload.setCentername(centername);
        subjectService.saveSubject(principal.getName(), subjectPayload);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Resource<>(new Message(201, "Subject created")));
    }

    @Override
    @PutMapping(RESOURCE_URL + "/{subjectname}")
    public ResponseEntity<Resource<Message>> updateSubject(Principal principal, @PathVariable String centername, @PathVariable String subjectname, @Valid @RequestBody SubjectPayload subjectPayload) throws UserNotFoundException, CenterNotFoundException {
        subjectPayload.setCentername(centername);
        String username = principal.getName();
        SubjectDto subjectDto = subjectService.updateSubject(username, subjectname, subjectPayload);
        return ResponseEntity.ok(new Resource<>(new Message(200, "Updated")));
    }

    @Override
    @DeleteMapping(RESOURCE_URL + "/{subjectname}")
    public ResponseEntity<Resource<Message>> deleteSubject(Principal principal, @PathVariable String centername, @PathVariable String subjectname) throws UserNotFoundException, CenterNotFoundException {
        String username = principal.getName();
        subjectService.deleteSubject(username, centername, subjectname);

        // TODO kodni yozib qoyish kerea
        return ResponseEntity.ok(new Resource<Message>(new Message(200, "Deleted")));
    }
}
