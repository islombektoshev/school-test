package uz.owl.schooltest.admin.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.owl.schooltest.entity.AdminMessage;

@Repository
public interface AdminMessageDao extends CrudRepository<AdminMessage, Long> {
}
