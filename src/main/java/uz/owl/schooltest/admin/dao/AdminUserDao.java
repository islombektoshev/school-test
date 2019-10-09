package uz.owl.schooltest.admin.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import uz.owl.schooltest.entity.User;

@Repository
public interface AdminUserDao extends PagingAndSortingRepository<User, Long> {
}
