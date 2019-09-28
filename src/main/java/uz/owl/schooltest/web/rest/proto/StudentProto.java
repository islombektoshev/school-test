package uz.owl.schooltest.web.rest.proto;

import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import uz.owl.schooltest.dto.group.GroupDto;
import uz.owl.schooltest.dto.student.AddStudentToGroupPayload;
import uz.owl.schooltest.dto.student.AddSubjectToStudentPayload;
import uz.owl.schooltest.dto.student.StudentPayload;
import uz.owl.schooltest.dto.student.StudentDto;
import uz.owl.schooltest.dto.subject.SubjectDto;
import uz.owl.schooltest.web.Message;

import java.security.Principal;
import java.util.List;

public interface StudentProto {
    List<Resource<StudentDto>> getAllStudents(Principal principal, String centername);

    ResponseEntity<Resource<StudentDto>> getStudent(Principal principal, String centername, Long studentId);

    ResponseEntity<Resource<Message>> saveStudent(Principal principal, String centername, StudentPayload payload);

    ResponseEntity<Resource<Message>> updateStudent(Principal principal, String centername, Long studentId, StudentPayload payload);

    ResponseEntity<Resource<Message>> deleteStudent(Principal principal, String centername, Long studentId);

    ResponseEntity<Resource<Message>> addGroup(Principal principal, String centername,Long studentId, AddStudentToGroupPayload payload);

    ResponseEntity<Resource<Message>> addSubject(Principal principal, String centername,Long studentId, AddSubjectToStudentPayload payload);

    ResponseEntity<Object> getGroup(Principal principal, String centername, Long studentId);

    List<Resource<SubjectDto>> getSubjects(Principal principal, String centername, Long studentId);

    ResponseEntity<?> removeGroup(Principal principal, String centername, Long studentId);

    ResponseEntity<?> removeSubject(Principal principal, String centername, Long studentId, AddSubjectToStudentPayload payload);

    ResponseEntity<?> getBlockTest(Principal principal, String centername, Long studentId);
}
