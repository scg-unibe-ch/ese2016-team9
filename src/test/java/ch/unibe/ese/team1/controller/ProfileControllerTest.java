package ch.unibe.ese.team1.controller;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ch.unibe.ese.team1.controller.service.AdService;
import ch.unibe.ese.team1.controller.service.VisitService;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.Bet;
import ch.unibe.ese.team1.model.Gender;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.Visit;
import ch.unibe.ese.team1.model.dao.AdDao;
import ch.unibe.ese.team1.model.dao.BetDao;
import ch.unibe.ese.team1.model.dao.UserDao;
import ch.unibe.ese.team1.model.dao.VisitDao;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})

@WebAppConfiguration
public class ProfileControllerTest {
	private MockMvc mockMvc;
	
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;
    
    @Autowired
    private AdDao adDao;
    
    @Autowired
    private AdService adService;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private BetDao betDao;
    
    @Autowired
    private VisitService visitService;
    
    @Autowired
    private VisitDao visitDao;
    
	@Before
	public void setup() throws Exception {
		mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(springSecurityFilterChain)
                .build();
		
	}
	
	private void login() {
		mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .defaultRequest(get("/").with(user("ese@unibe.ch").roles("USER")))
                .addFilters(springSecurityFilterChain)
                .build();
	}

	@Test
	public void canNotListBetsWhenNotLoggedIn() throws Exception {
		this.mockMvc.perform(
				get("/profile/listBets")			
				).andExpect(status().is3xxRedirection());
		
	}

	@Test
	public void canListBetsWhenLoggedIn() throws Exception {
		this.login();
		this.mockMvc.perform(
				get("/profile/listBets")			
				).andExpect(status().is2xxSuccessful());
		
	}
	
	@Test
	public void canSeeLoginPage() throws Exception {
		this.mockMvc.perform(
				get("/login")			
				).andExpect(status().is2xxSuccessful());
		
	}
	
	@Test
	public void googleSignupFormWillSetFlag() throws Exception {
		this.mockMvc.perform(
				post("/google-signup")			
				).andExpect(status().is2xxSuccessful());
	}
	
	@Test
	public void signUpFormWillDisplay() throws Exception {
		this.mockMvc.perform(
				get("/signup")			
				).andExpect(status().is2xxSuccessful());
		
	}
	
	@Test
	public void signUpANewUserWillWork() throws Exception {
		this.mockMvc.perform(
				post("/signup")	
				.param("email", "fake1@fake.com")
				.param("password", "password")
				.param("firstName", "Test")
				.param("lastName", "User")
				.param("gender", Gender.MALE.toString())
				).andExpect(status().is2xxSuccessful());
		User user = this.userDao.findByUsername("fake1@fake.com");
		assertNotNull(user);
		this.userDao.delete(user);
	}
	
	@Test
	public void ifFieldIsNotValidSignupWontWork() throws Exception {
		this.mockMvc.perform(
				post("/signup")	
				.param("email", "testfakefake.com")
				.param("password", "fake@fake.com")
				.param("firstName", "fake@fake.com")
				.param("lastName", "fake@fake.com")
				.param("gender", Gender.MALE.toString())
				).andExpect(status().is2xxSuccessful());
		User user = this.userDao.findByUsername("testfakefake.com");
		assertNull(user);
	}
	
	@Test
	public void usernameDoesNotExistsWillReturnZero() throws Exception {
		ResultActions resultAction = this.mockMvc.perform(
				post("/signup/doesEmailExist").with(csrf())
				.param("email", "fakeonetwothree@fake.com"));
		
		resultAction.andExpect(status().is2xxSuccessful());
		resultAction.andExpect(content().string(containsString("false")));
	}
	
	@Test
	public void usernameDoesExistsWillReturnOne() throws Exception {
		ResultActions resultAction = this.mockMvc.perform(
				post("/signup/doesEmailExist").with(csrf())
				.param("email", "ese@unibe.ch"));
		
		resultAction.andExpect(status().is2xxSuccessful());
		resultAction.andExpect(content().string(containsString("true")));
	}
	
	@Test
	public void canSeeEditProfilePageWhenLoggedIn() throws Exception {
		this.login();
		ResultActions resultAction = this.mockMvc.perform(
				get("/profile/editProfile"));
		
		resultAction.andExpect(status().is2xxSuccessful());
	}
	
	@Test
	public void canEditProfile() throws Exception {
		this.login();

		User user = this.userDao.findByUsername("ese@unibe.ch");
		
		ResultActions resultAction = this.mockMvc.perform(
				post("/profile/editProfile")
				.param("id", ""+user.getId())
				.param("username", user.getUsername())
				.param("password", "ese")
				.param("firstName", user.getFirstName())
				.param("lastName", user.getLastName())
				.param("aboutMe", "Huhu"));
		
		resultAction.andExpect(status().is3xxRedirection());
		User ese = this.userDao.findByUsername("ese@unibe.ch");
		assertEquals("Huhu", ese.getAboutMe());
	}
	

	@Test
	public void canSeeListOfVisistors() throws Exception {
		this.login();
		User ese = this.userDao.findByUsername("ese@unibe.ch");
		User jane = this.userDao.findByUsername("jane@doe.com");
		
		Iterable<Ad> randomAds = adService.getAdsByUser(ese);
		Ad ad = randomAds.iterator().next();
		
		List<User> searchers = new ArrayList<User>();
		searchers.add(jane);
		
		Visit visit = new Visit();
		visit.setAd(ad);
		visit.setEndTimestamp(new Date());
		visit.setStartTimestamp(new Date());
		visit.setSearchers(searchers);
		
		visitDao.save(visit);
		
		ResultActions resultAction = this.mockMvc.perform(
				get("/profile/visitors").param("visit", ""+visit.getId()));
		
		resultAction.andExpect(status().is2xxSuccessful());
	}

	@Test
	public void canGetPremiumForm() throws Exception {
		this.login();
		ResultActions resultAction = this.mockMvc.perform(
				get("/profile/getPremium"));
		resultAction.andExpect(status().is2xxSuccessful());
	}
	

	@Test
	public void canPostPremiumForm() throws Exception {
		this.login();
		ResultActions resultAction = this.mockMvc.perform(
				post("/profile/getPremium"));
		resultAction.andExpect(status().is3xxRedirection());
		User ese = this.userDao.findByUsername("ese@unibe.ch");
		assertTrue(ese.isPremium());
		ese.setPremium(false);
		userDao.save(ese);
		
	}
	

	@Test
	public void canSeeOwnProfile() throws Exception {
		this.login();
		User ese = this.userDao.findByUsername("ese@unibe.ch");
		ResultActions resultAction = this.mockMvc.perform(
				get("/user?id="+ese.getId()));
		resultAction.andExpect(status().is2xxSuccessful());
		
	}
	
	@Test
	public void canSeeAnotherProfile() throws Exception {
		this.login();
		User jane = this.userDao.findByUsername("jane@doe.com");
		ResultActions resultAction = this.mockMvc.perform(
				get("/user?id="+jane.getId()));
		resultAction.andExpect(status().is2xxSuccessful());
		
	}

	@Test
	public void canListAScheduleTimetable() throws Exception {
		this.login();
		User ese = this.userDao.findByUsername("ese@unibe.ch");
		User jane = this.userDao.findByUsername("jane@doe.com");
		
		Iterable<Ad> randomAds = adService.getAdsByUser(ese);
		Ad ad = randomAds.iterator().next();
		
		List<User> searchers = new ArrayList<User>();
		searchers.add(jane);
		
		Visit visit = new Visit();
		visit.setAd(ad);
		visit.setEndTimestamp(new Date());
		visit.setStartTimestamp(new Date());
		visit.setSearchers(searchers);
		
		visitDao.save(visit);
		
		ResultActions resultAction = this.mockMvc.perform(
				get("/profile/schedule"));
		
		resultAction.andExpect(status().is2xxSuccessful());
	}
}