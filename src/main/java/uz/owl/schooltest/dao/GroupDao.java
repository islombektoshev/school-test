package uz.owl.schooltest.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.owl.schooltest.entity.Guruh;
import uz.owl.schooltest.entity.SCenter;

import java.util.List;

@Repository
public interface GroupDao extends CrudRepository<Guruh, Long> {
    List<Guruh> findAllByScenter(SCenter sCenter);
}
