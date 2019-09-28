package uz.owl.schooltest.web.rest.proto;

import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import uz.owl.schooltest.dto.subject.SubjectDto;
import uz.owl.schooltest.dto.subject.SubjectPayload;
import uz.owl.schooltest.web.Message;

import java.security.Principal;
import java.util.List;

public interface SubjectProto {
    List<Resource<SubjectDto>> getAllSubjects(Principal principal, String centername);

    Resource<SubjectDto> getSingleSubject(Principal principal, String centername, String subjectname);

    ResponseEntity<Resource<Message>> deleteSubject(Principal principal, String centername, String subjectname);

    ResponseEntity<Resource<Message>> saveSubject(Principal principal, String centername, SubjectPayload payload);

    ResponseEntity<Resource<Message>> updateSubject(Principal principal, String centername, String subjectname, SubjectPayload payload);
}
