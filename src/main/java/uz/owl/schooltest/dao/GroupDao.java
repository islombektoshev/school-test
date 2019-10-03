package uz.owl.schooltest.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.owl.schooltest.entity.Guruh;
import uz.owl.schooltest.entity.SCenter;

import java.util.List;

@Repository
public interface GroupDao extends JpaRepository<Guruh, Long> {
    List<Guruh> findAllByScenter(SCenter sCenter);

    @Modifying
    @Query(value = "update student set guruh_id = null where guruh_id = ?1 ;" +
            "delete from guruh_block_tests where guruh_block_tests.guruhs_id = ?1 ;"+
            "delete from guruh where id = ?1 ;" ,
            nativeQuery = true)
    void deleteGuruh(Long goruhId);
}
