package uz.owl.schooltest.dao;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import uz.owl.schooltest.entity.BlockTest;
import uz.owl.schooltest.entity.SCenter;

import java.util.List;

@Repository
public interface BlockTestDao extends CrudRepository<BlockTest, Long> {
    BlockTest findByScenterAndId(SCenter sCenter, Long id);


    List<BlockTest> findAllByScenter(SCenter sCenter);

    @Modifying
    @Query(value =
            "insert into student_block_tests(students_id, block_tests_id)" +
                    "select student1.id as students_id, :blocktestId as block_tests_id " +
                    " from student student1 " +
                    " where student1.guruh_id in ( " +
                    "    select id  " + //-- blockTest ga qo'shilgan guruhlar
                    "    from guruh " +
                    "    where guruh.id in (  " + //-- guruhi bor bo'lsa tanlaydi
                    "        select guruh_block_tests.guruhs_id " +
                    "        from guruh_block_tests " +
                    "        where block_tests_id = :blocktestId ) " + //-- block test id
                    "      and not exists(  " + //-- ROW_COUNT ?????
                    "            select subject1.id  " + // -- B fanlari bor studentni fanlari
                    "            from subject subject1 " +
                    "            where subject1.id in ( " +
                    "                select ss2.subjects_id " +
                    "                from subject_students ss2 " +
                    "                where ss2.students_id = student1.id " + // -- berilgan studentni tekshiradi
                    "            ) " +
                    "                except " +  //-- minus
                    "            select subject2.id   " + //A blockTest ga qo'shilgan fanlar
                    "            from subject subject2 " +
                    "            where subject2.id in ( " +
                    "                select subjects_id " +
                    "                from subject_block_tests " +
                    "                where block_tests_id = :blocktestId  " + //-- blocktest id
                    "            )) " +
                    "      and exists(  " + //-- hech bo'lmasa bitta fani borligiga tekshiradi
                    "            select subject1.id " + //-- B fanlari bor studentni fanlari
                    "            from subject subject1 " +
                    "            where subject1.id in ( " +
                    "                select ss2.subjects_id " +
                    "                from subject_students ss2 " +
                    "                where ss2.students_id = student1.id " + //-- berilgan studentni tekshiradi
                    "            ) " +
                    "        )" +
                    ");", nativeQuery = true)
    void generateStudent(Long blocktestId);

    @Modifying
    @Query(value = "delete\n" +
            "from student_block_tests\n" +
            "where block_tests_id = :blocktestId\n" +
            "  and students_id = any (\n" +
            "    select student1.id as students_id\n" +
            "    from student student1\n" +
            "    where student1.guruh_id in (\n" +
            "        select id -- blockTest ga qo'shilgan guruhlar\n" +
            "        from guruh\n" +
            "        where guruh.id in ( -- guruhi bor bo'lsa tanlaydi\n" +
            "            select guruh_block_tests.guruhs_id\n" +
            "            from guruh_block_tests\n" +
            "            where block_tests_id = :blocktestId)-- block test id\n" +
            "          and not exists(\n" +
            "                select subject1.id -- B fanlari bor studentni fanlari\n" +
            "                from subject subject1\n" +
            "                where subject1.id in (\n" +
            "                    select ss2.subjects_id\n" +
            "                    from subject_students ss2\n" +
            "                    where ss2.students_id = student1.id -- berilgan studentni tekshiradi\n" +
            "                )\n" +
            "                    except -- minus\n" +
            "                select subject2.id -- A blockTest ga qo'shilgan fanlar\n" +
            "                from subject subject2\n" +
            "                where subject2.id in (\n" +
            "                    select subjects_id\n" +
            "                    from subject_block_tests\n" +
            "                    where block_tests_id = :blocktestId -- blocktest id\n" +
            "                ))\n" +
            "          and exists( -- hech bo'lmasa bitta fani borligiga tekshiradi\n" +
            "                select subject1.id -- B fanlari bor studentni fanlari\n" +
            "                from subject subject1\n" +
            "                where subject1.id in (\n" +
            "                    select ss2.subjects_id\n" +
            "                    from subject_students ss2\n" +
            "                    where ss2.students_id = student1.id -- berilgan studentni tekshiradi\n" +
            "                )\n" +
            "            )\n" +
            "    )\n" +
            ");", nativeQuery = true)
    void deleteGeneratableStudents(Long blocktestId);

    void deleteByScenterAndId(SCenter sCenter, Long id);

    @Modifying
    @Query(value =
            "delete from student_block_tests where block_tests_id = ?1  ;\n" +
                    "delete from subject_block_tests where block_tests_id = ?1  ;\n" +
                    "delete from guruh_block_tests where block_tests_id = ?1 ;\n" +
                    "delete from block_test where id = ?1 ;",
            nativeQuery = true)
    void deleteBlockTest(Long blocktestid);
}
