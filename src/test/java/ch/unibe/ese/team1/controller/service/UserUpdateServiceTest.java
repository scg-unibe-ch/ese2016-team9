package ch.unibe.ese.team1.controller.service;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team1.controller.pojos.forms.EditProfileForm;
import ch.unibe.ese.team1.model.Gender;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.UserRole;
import ch.unibe.ese.team1.model.dao.UserDao;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
public class UserUpdateServiceTest {

	@Autowired
	private UserUpdateService userUpdateService;
	
	@Autowired
	private UserDao userDao;

	@Test
	public void canChangeUsernameOfUser() throws ParseException {
		User user = this.createUser("blupp@email.ch", "password", "firstname", "lastname", Gender.MALE);
		this.userDao.save(user);
		
		EditProfileForm form = new EditProfileForm();
		form.setUsername("blupp1@email.ch");
		form.setLastName("lastname");
		form.setPassword("password");
		form.setFirstName("firstname");
		form.setId(user.getId());
		
		this.userUpdateService.updateFrom(form);
		
		User foundUser = this.userDao.findByUsername("blupp1@email.ch");
		assertNotNull(foundUser);
	}
	
	private User createUser(String email, String password, String firstName, String lastName, Gender gender) {
		User user = new User();
		user.setUsername(email);
		user.setPassword(password);
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEnabled(true);
		user.setGender(gender);
		Set<UserRole> userRoles = new HashSet<>();
		UserRole role = new UserRole();
		role.setRole("ROLE_USER");
		role.setUser(user);
		userRoles.add(role);
		user.setUserRoles(userRoles);
		return user;
	}
	
}
