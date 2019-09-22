package uz.owl.schooltest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uz.owl.schooltest.dao.RoleDao;
import uz.owl.schooltest.dao.UserDao;
import uz.owl.schooltest.entity.Role;
import uz.owl.schooltest.entity.SCenter;
import uz.owl.schooltest.entity.User;
import uz.owl.schooltest.service.SCenterService;
import uz.owl.schooltest.service.UserService;

import javax.transaction.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SchoolTestApplicationTests {

	@Autowired
	UserDao userDao;

	@Autowired
	SCenterService sCenterService;

	@Autowired
	RoleDao roleDao;

	@Test
	@Transactional
	public void contextLoads() {
		Role role_user = roleDao.findByRolename("ROLE_USER");
		System.out.println("role_user.getUsers() = " + role_user.getUsers());
	}



}
