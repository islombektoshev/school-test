package uz.owl.schooltest.web.rest.proto;

import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import uz.owl.schooltest.dto.blocktest.BlockTestDto;
import uz.owl.schooltest.dto.blocktest.BlockTestPayload;
import uz.owl.schooltest.dto.blocktest.CreateBlockTestPayload;
import uz.owl.schooltest.dto.group.GroupDto;
import uz.owl.schooltest.dto.student.StudentDto;
import uz.owl.schooltest.dto.subject.SubjectDto;
import uz.owl.schooltest.entity.Student;
import uz.owl.schooltest.web.Message;

import java.security.Principal;
import java.util.List;

public interface BlockTestProto {
    List<Resource<BlockTestDto>> getAllBlockTest(Principal principal, String centername);

    Resource<BlockTestDto> getBlockTest(Principal principal, String centername, Long blockTestId);

    ResponseEntity<Resource<Message>> saveBlockTest(Principal principal, String centername, CreateBlockTestPayload payload);

    ResponseEntity<Resource<Message>> updateUpdateBlockTest(Principal principal, String centername, Long blockTestId, BlockTestPayload payload);

    ResponseEntity<Resource<Message>> deleteBlockTest(Principal principal, String centername, Long blockTestId);

//    ResponseEntity<Resource<Message>> addStudentToBlockTest(Principal principal, String centername, Long blockTestId, Long studentId);

    ResponseEntity<Resource<Message>> addSubject(Principal principal, String centername, Long blockTestId, String subjectName);

    ResponseEntity<Resource<Message>> addGroup(Principal principal, String centername, Long blockTestId, Long groupId);

    ResponseEntity<Resource<Message>> removeStudent(Principal principal, String centername, Long blockTestId, Long studentId);

    ResponseEntity<Resource<Message>> removeSubject(Principal principal, String centername, Long blockTestId, String subjectName);

    ResponseEntity<Resource<Message>> removeGroup(Principal principal, String centername, Long blockTestId, Long groupId);

    List<StudentDto> getStudents(Principal principal, String centername, Long blockTestId);

    List<GroupDto> getGroups(Principal principal, String centername, Long blockTestId);

    List<SubjectDto> getSubjects(Principal principal, String centername, Long blockTestId);
}
