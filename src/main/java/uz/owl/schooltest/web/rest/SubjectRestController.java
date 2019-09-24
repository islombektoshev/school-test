package uz.owl.schooltest.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.owl.schooltest.dto.AddSubjectPayload;
import uz.owl.schooltest.dto.SubjectDto;
import uz.owl.schooltest.exception.CenterNotFoundException;
import uz.owl.schooltest.exception.CoundtCreatedExeption;
import uz.owl.schooltest.exception.CoundtUpdatedException;
import uz.owl.schooltest.exception.UserNotFoundException;
import uz.owl.schooltest.service.SubjectService;
import uz.owl.schooltest.web.Message;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
public class SubjectRestController {
    private final SubjectService subjectService;

    public SubjectRestController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping("${api.v1}/centers/{centername}/subjects")
    public List<SubjectDto> getAllSubjects(Principal principal, @PathVariable String centername) throws UserNotFoundException, CenterNotFoundException {
        List<SubjectDto> subjectByUsernameAnsCentername = subjectService.getSubjectByUsernameAndCentername(principal.getName(), centername);
        return subjectByUsernameAnsCentername;
    }

    @GetMapping("${api.v1}/centers/{centername}/subjects/{subjectname}")
    public SubjectDto getAllSubjects(Principal principal, @PathVariable String centername, @PathVariable String subjectname) throws UserNotFoundException, CenterNotFoundException {
        SubjectDto subjectDto = subjectService.getSubjectDtoByUsernameAndCenternameAndSubjectname(principal.getName(), centername, subjectname);
        return subjectDto;
    }

    @PostMapping("${api.v1}/centers/{centername}/subjects")
    public ResponseEntity<Message> addSubject(Principal principal, @Valid @RequestBody AddSubjectPayload addSubjectPayload, @PathVariable String centername) throws UserNotFoundException, CenterNotFoundException, CoundtCreatedExeption {
        addSubjectPayload.setCentername(centername);
        subjectService.saveSubject(principal.getName(), addSubjectPayload);
        return ResponseEntity.status(HttpStatus.CREATED).body(new Message(201, "Subject created"));
    }

    @PutMapping("${api.v1}/centers/{centername}/subjects/{subjectname}")
    public ResponseEntity<Message> updateSuject(Principal principal,
                                                @Valid @RequestBody AddSubjectPayload addSubjectPayload,
                                                @PathVariable String centername,
                                                @PathVariable String subjectname) throws UserNotFoundException, CenterNotFoundException {
        addSubjectPayload.setCentername(centername);
        String username = principal.getName();
        SubjectDto subjectDto = subjectService.updateSubject(username, subjectname, addSubjectPayload);
        return ResponseEntity.ok(new Message(200, "Updated"));
    }

    @DeleteMapping("${api.v1}/centers/{centername}/subjects/{subjectname}")
    public ResponseEntity<Message> deleteSubject(Principal principal, @PathVariable String centername, @PathVariable String subjectname) throws UserNotFoundException, CenterNotFoundException {
        String username = principal.getName();
        subjectService.deleteSubject(username, centername, subjectname);

        // TODO
        return null;
    }


//
//    @ExceptionHandler(UserNotFoundException.class)
//    public void ifUserNotFound(UserNotFoundException e) {
//        System.out.println(e.getMessage());
//    }
//
//    @ExceptionHandler(CoundtCreatedExeption.class)
//    public ResponseEntity<Message> ifCoundtCreated(CoundtCreatedExeption e) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(400, e.getMessage()));
//    }
//
//    @ExceptionHandler(CoundtUpdatedException.class)
//    public ResponseEntity<Message> ifCoundtUpdated(CoundtUpdatedException e) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(400, e.getMessage()));
//    }
//
//    @ExceptionHandler(CenterNotFoundException.class)
//    public ResponseEntity<Message> ifCenterNotFound(CenterNotFoundException e) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(400, e.getMessage()));
//    }
//
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Message> anyError(Exception e) {
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Message(400, "Internal Server error"));
//    }
}
