package uz.owl.schooltest.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.owl.schooltest.entity.SCenter;
import uz.owl.schooltest.entity.Subject;

import java.util.List;

@Repository
public interface SubjectDao extends CrudRepository<Subject, Long> {

    List<Subject> findAllByScenter(SCenter sCenter);

    Subject findByScenterAndName(SCenter sCenter, String  subjectname);

    void deleteByScenterAndName(SCenter sCenter, String  subjectname);

    List<Subject> findAllByScenterAndPrimarySubject(SCenter sCenter, boolean b);
}
