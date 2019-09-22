package uz.owl.schooltest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uz.owl.schooltest.dao.UserDao;
import uz.owl.schooltest.entity.User;

import javax.transaction.Transactional;

@SpringBootApplication
public class SchoolTestApplication implements CommandLineRunner {

	@Autowired
	UserDao  userDao;
	public static void main(String[] args) {
		SpringApplication.run(SchoolTestApplication.class, args);
	}

	@Override
//	@Transactional
	public void run(String... args) throws Exception {
//		userDao.deleteByUsername("islom");
	}
}
