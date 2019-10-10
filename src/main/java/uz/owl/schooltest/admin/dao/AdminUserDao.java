package uz.owl.schooltest.admin.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import uz.owl.schooltest.entity.User;

import java.util.List;

@Repository
public interface AdminUserDao extends PagingAndSortingRepository<User, Long> {
    @Query(value = "select * from users\n" +
            "where payment_expired_date > current_timestamp", nativeQuery = true)
    List<User> getAllNotExpired();

    @Query(value = "select * from users\n" +
            "where payment_expired_date < current_timestamp", nativeQuery = true)
    List<User> getAllExpired();

    List<User> getAllByEnable(Boolean aBoolean);
}
