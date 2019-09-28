package uz.owl.schooltest.web.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.owl.schooltest.dto.blocktest.BlockTestDto;
import uz.owl.schooltest.dto.group.GroupDto;
import uz.owl.schooltest.dto.student.AddStudentToGroupPayload;
import uz.owl.schooltest.dto.student.AddSubjectToStudentPayload;
import uz.owl.schooltest.dto.student.StudentPayload;
import uz.owl.schooltest.dto.student.StudentDto;
import uz.owl.schooltest.dto.subject.SubjectDto;
import uz.owl.schooltest.service.StudentService;
import uz.owl.schooltest.web.Message;
import uz.owl.schooltest.web.rest.proto.StudentProto;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

// TODO: 9/25/2019 yana birmarta tekshirib chiqish kerak
@RestController
public class StudentRestController implements StudentProto {

    private static final String RESURCE_URL = "/api/v1/centers/{centername}/students";
    private final StudentService studentService;
    @Autowired
    private BlockTestRestController blockTestRestController;

    public StudentRestController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    @GetMapping(RESURCE_URL)
    public List<Resource<StudentDto>> getAllStudents(Principal principal, @PathVariable String centername) {
        List<StudentDto> allStudents = studentService.getAllStudents(principal.getName(), centername);
        List<Resource<StudentDto>> collect = allStudents.stream().map(studentDto -> {
            Resource<StudentDto> resource = new Resource<>(studentDto);
            links(resource, principal, centername, studentDto.getId());
            return resource;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    @GetMapping(RESURCE_URL + "/{studentId}")
    public ResponseEntity<Resource<StudentDto>> getStudent(Principal principal, @PathVariable String centername, @PathVariable Long studentId) {
        StudentDto student = studentService.getStudent(principal.getName(), centername, studentId);
        Resource<StudentDto> body = new Resource<>(student);
        links(body, principal, centername, studentId);
        return ResponseEntity.ok(body);
    }

    @Override
    @PostMapping(RESURCE_URL)
    public ResponseEntity<Resource<Message>> saveStudent(Principal principal, @PathVariable String centername, @Valid @RequestBody StudentPayload payload) {
        StudentDto studentDto = studentService.saveStudent(principal.getName(), centername, payload);
        Resource<Message> resource = new Resource<>(new Message(201, "Created"));
        links(resource, principal, centername, studentDto.getId());
        return ResponseEntity.ok(resource);
    }

    @Override
    @PutMapping(RESURCE_URL + "/{studentId}")
    public ResponseEntity<Resource<Message>> updateStudent(Principal principal, @PathVariable String centername, @PathVariable Long studentId, @Valid  @RequestBody StudentPayload payload) {
        StudentDto studentDto = studentService.updateStudent(principal.getName(), centername, studentId, payload);
        Resource<Message> message = new Resource<>(new Message(200, "Updated"));
        links(message, principal, centername, studentDto.getId());
        return ResponseEntity.ok(message);
    }

    @Override
    @DeleteMapping(RESURCE_URL + "/{studentId}")
    public ResponseEntity<Resource<Message>> deleteStudent(Principal principal, @PathVariable String centername, @PathVariable Long studentId) {
        studentService.delete(principal.getName(), centername, studentId);
        Resource<Message> message = new Resource<>(new Message(200, "Deleted"));
        links(message, principal, centername, studentId);
        return ResponseEntity.ok(message);
    }

    @Override
    @PostMapping(RESURCE_URL + "/{studentId}/groups")
    public ResponseEntity<Resource<Message>> addGroup(Principal principal, @PathVariable String centername, @PathVariable Long studentId, @Valid  @RequestBody AddStudentToGroupPayload payload) {
        GroupDto groupDto = studentService.addGroupToStudent(principal.getName(), centername, studentId, payload);
        Resource<Message> message = new Resource<>(new Message(200, String.valueOf(groupDto)));
        links(message, principal, centername, studentId);
        return ResponseEntity.ok(message);
    }

    @Override
    @PostMapping(RESURCE_URL + "/{studentId}/subjects")
    public ResponseEntity<Resource<Message>> addSubject(Principal principal, @PathVariable String centername, @PathVariable Long studentId,@Valid  @RequestBody AddSubjectToStudentPayload payload) {
        StudentDto studentDto = studentService.addSubjectToStudent(principal.getName(), centername, studentId, payload);
        Resource<Message> resource = new Resource<>(new Message(201, "Subjects added"));
        links(resource, principal, centername, studentDto.getId());
        return ResponseEntity.ok(resource);
    }

    @Override
    @GetMapping(RESURCE_URL + "/{studentId}/groups")
    public ResponseEntity<Object> getGroup(Principal principal, @PathVariable String centername, @PathVariable Long studentId) {
        GroupDto studentGroup = studentService.getStudentGroup(principal.getName(), centername, studentId);
        if (studentGroup == null) {
            Resource<Message> messageResource = new Resource<>(new Message(404, "There is no group for this student"));
            links(messageResource, principal, centername, studentId);
            return ResponseEntity.status(404).body(messageResource);
        }
        Link students = linkTo(this.getClass()).withRel("students");
        Resource<GroupDto> body = new Resource<>(studentGroup);
        body.add(students);
        return ResponseEntity.ok(body);// TODO: 9/26/2019 test
    }

    @Override
    @GetMapping(RESURCE_URL + "/{studentId}/subjects")
    public List<Resource<SubjectDto>> getSubjects(Principal principal, @PathVariable String centername, @PathVariable Long studentId) {
        List<SubjectDto> subjects = studentService.getSubjects(principal.getName(), centername, studentId);
        return subjects.stream().map(Resource<SubjectDto>::new).collect(Collectors.toList());
    }

    @Override
    @DeleteMapping(RESURCE_URL + "/{studentId}/groups")
    public ResponseEntity<?> removeGroup(Principal principal,@PathVariable String centername, @PathVariable Long studentId) {
        studentService.removeGroupFromStudent(principal.getName(), centername, studentId);
        return ResponseEntity.ok().build();
    }

    @Override
    @DeleteMapping(RESURCE_URL + "/{studentId}/subjects")
    public ResponseEntity<?> removeSubject(Principal principal,@PathVariable String centername, @PathVariable Long studentId, @Valid  @RequestBody AddSubjectToStudentPayload payload) {
        payload.getSubjectName().forEach(subjectName ->{
            studentService.removeSubjectFromStudent(principal.getName(), centername, studentId, subjectName);
        });
        return ResponseEntity.ok().build();
    }

    @Override
    @GetMapping(RESURCE_URL + "/{studentId}/blocktests")
    public ResponseEntity<?> getBlockTest(Principal principal, String centername, @PathVariable Long studentId) {
        List<BlockTestDto> blockTest = studentService.getBlockTest(principal.getName(), centername, studentId);
        List<Resource<BlockTestDto>> collect = blockTest.stream().map(b -> {
            Resource<BlockTestDto> messageResource = new Resource<>(b);
            blockTestRestController.links(messageResource, principal, centername, b.getId());
            return messageResource;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(collect);
    }

    public void links(Resource resource, Principal principal, String centername, Long studentId) {
//        _links:{
//            self:?
//            subjects:
//            groups:
//            center:
//            results:{
//                System.out.println("kcmslkd");
//            }
//        }

        Link all_student = linkTo(methodOn(getClass()).getAllStudents(principal, centername)).withRel("all_student");
        Link student = linkTo(methodOn(getClass()).getStudent(principal, centername, studentId)).withRel("student");
        Link subjects = linkTo(methodOn(getClass()).getSubjects(principal, centername, studentId)).withRel("subjects");
        Link groups = linkTo(methodOn(getClass()).getGroup(principal, centername, studentId)).withRel("groups");
        Link center = linkTo(methodOn(SCenterRestController.class).getSingleCenter(principal, centername)).withRel("center");
        // todo result link don't add to resourse
        resource.add(student);
        resource.add(center);
        resource.add(all_student);
        resource.add(subjects);
        resource.add(groups);
    }
}
