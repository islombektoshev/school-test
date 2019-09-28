package uz.owl.schooltest.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.owl.schooltest.entity.SCenter;
import uz.owl.schooltest.entity.Student;

import java.util.List;

@Repository
public interface StudentDao extends CrudRepository<Student, Long> {
    Student findByScenterAndId(SCenter sCenter, Long id);
    List<Student> findAllByScenter(SCenter sCenter);
}
