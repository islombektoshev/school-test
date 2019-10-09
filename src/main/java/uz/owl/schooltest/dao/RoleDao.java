package uz.owl.schooltest.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.owl.schooltest.entity.Role;

import java.util.List;

@Repository
public interface RoleDao extends CrudRepository<Role, Long> {
    Role findByRolename(String s);

    List<Role> findAllByRolename(Iterable<String> roleNames);
}
