package uz.owl.schooltest.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.owl.schooltest.entity.Role;

@Repository
public interface RoleDao extends CrudRepository<Role, Long> {
    Role findByRolename(String s);
}
