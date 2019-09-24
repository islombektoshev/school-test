package uz.owl.schooltest.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.owl.schooltest.entity.Student;

@Repository
public interface StudentDao extends CrudRepository<Student, Long> {
    
}
