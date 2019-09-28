package uz.owl.schooltest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uz.owl.schooltest.dao.GroupDao;
import uz.owl.schooltest.dao.RoleDao;
import uz.owl.schooltest.dao.StudentDao;
import uz.owl.schooltest.dao.UserDao;
import uz.owl.schooltest.entity.Guruh;
import uz.owl.schooltest.entity.Student;
import uz.owl.schooltest.service.SCenterService;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
//@DataJpaTest
public class SchoolTestApplicationTests {

	@Autowired
	UserDao userDao;

	@Autowired
	SCenterService sCenterService;

	@Autowired
	StudentDao studentDao;

	@Autowired
	GroupDao groupDao;

	@Autowired
	RoleDao roleDao;

	@Test
	@Transactional
	public void contextLoads() {

	}



}
