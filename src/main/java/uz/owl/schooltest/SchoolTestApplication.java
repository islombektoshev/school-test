package uz.owl.schooltest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import uz.owl.schooltest.dao.GroupDao;
import uz.owl.schooltest.dao.RoleDao;
import uz.owl.schooltest.dao.StudentDao;
import uz.owl.schooltest.dao.UserDao;
import uz.owl.schooltest.dto.group.GroupDto;
import uz.owl.schooltest.dto.scenter.SCenterDto;
import uz.owl.schooltest.dto.student.AddStudentToGroupPayload;
import uz.owl.schooltest.dto.student.StudentDto;
import uz.owl.schooltest.entity.Guruh;
import uz.owl.schooltest.entity.Student;
import uz.owl.schooltest.entity.User;
import uz.owl.schooltest.service.GroupService;
import uz.owl.schooltest.service.SCenterService;
import uz.owl.schooltest.service.StudentService;
import uz.owl.schooltest.service.UserService;

import javax.transaction.Transactional;

@SpringBootApplication
@Service
public class SchoolTestApplication implements CommandLineRunner {

	@Autowired
	UserDao  userDao;
	public static void main(String[] args) {
		SpringApplication.run(SchoolTestApplication.class, args);
	}

	@Autowired
	SCenterService sCenterService;

	@Autowired
	StudentDao studentDao;

	@Autowired
	GroupDao groupDao;

	@Autowired
	RoleDao roleDao;

	@Autowired
	UserService userService;
	@Autowired
	StudentService studentService;
	@Autowired
	SCenterService getsCenterService;
	@Autowired
	GroupService groupService;

	@Override
	@Transactional
	public void run(String... args) throws Exception {

//		SCenterDto center = sCenterService.findByAuthorAndName("islom", "second");
//		System.out.println(center);
//		System.out.println("=======> " + center.getName());
//		StudentDto islom = studentService.getStudent("islom", center.getName(), 4L);
//		System.out.println("islom = " + islom);
//		AddStudentToGroupPayload addStudentToGroupPayload = new AddStudentToGroupPayload();
//		GroupDto guruh = groupService.getSingle("islom", center.getName(), 8L);
//		System.out.println("guruh = " + guruh);
//		addStudentToGroupPayload.setGroupId(guruh.getId());
//		GroupDto islom1 = studentService.addGroupToStudent("islom", center.getName(), 4L, addStudentToGroupPayload);
//		System.out.println(islom1);
	}
}
