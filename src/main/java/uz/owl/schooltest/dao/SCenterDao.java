package uz.owl.schooltest.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.owl.schooltest.entity.SCenter;
import uz.owl.schooltest.entity.User;

@Repository
public interface SCenterDao extends CrudRepository<SCenter, Long> {

    void deleteByAuthorAndName(User author, String name);

    SCenter findByAuthorAndName(User author, String name);
}
