package ch.unibe.ese.team1.controller;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebConnection;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;



import ch.unibe.ese.team1.controller.service.AdService;
import ch.unibe.ese.team1.controller.service.EnquiryService;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.Rating;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.Visit;
import ch.unibe.ese.team1.model.VisitEnquiry;
import ch.unibe.ese.team1.model.VisitEnquiryState;
import ch.unibe.ese.team1.model.dao.AdDao;
import ch.unibe.ese.team1.model.dao.UserDao;
import ch.unibe.ese.team1.model.dao.VisitDao;
import ch.unibe.ese.team1.model.dao.VisitEnquiryDao;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})

@WebAppConfiguration
public class EnquiryControllerTest {
	private MockMvc mockMvc;

	@Autowired
	private AdService adService;
    
    @Autowired
    private UserDao userDao;
    
    @Autowired
    private AdDao adDao;
    
    @Autowired
    private VisitDao visitDao;
    
    @Autowired
    private VisitEnquiryDao visitEnquiryDao;
    
    @Autowired
    private EnquiryService enService;
	
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;
    
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
	public void getEnquiriesIsForbiddenForAnonymousUser() throws Exception {
		ResultActions resultActions = this.mockMvc.perform(
				get("/profile/enquiries")
				).andExpect(status().is3xxRedirection());

		resultActions.andExpect(redirectedUrl("http://localhost/login"));
	}
	
	@Test
	public void getEnquiriesWillListEnquiriesForUser() throws Exception {
		this.login();
		this.mockMvc.perform(
				get("/profile/enquiries")
				).andExpect(status().is2xxSuccessful());
		
	}
	
	@Test
	public void canSendAnEnquiryForVisit() throws Exception {
		this.login();
		User jane = userDao.findByUsername("jane@doe.com");
		
		Ad ad = this.generateAuctionAd(jane);
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 7);
		Visit visit = new Visit();
		visit.setAd(ad);
		visit.setEndTimestamp(cal.getTime());
		visit.setStartTimestamp(new Date());
		visitDao.save(visit);
		int count = 0;
		int total;
		for (VisitEnquiry en : this.enService.getEnquiriesByRecipient(jane)) {
			count++;
		}
		total = count;
		
		this.mockMvc.perform(
				get("/profile/enquiries/sendEnquiryForVisit")
				.param("id", ""+visit.getId())
				).andExpect(status().is2xxSuccessful());
		count = 0;

		for (VisitEnquiry en : this.enService.getEnquiriesByRecipient(jane)) {
			count++;
		}
		assertEquals(total+1, count);
		
	}

	@Test
	public void canAcceptAnEnquiry() throws Exception {
		this.login();
		User ese = userDao.findByUsername("ese@unibe.ch");
		
		VisitEnquiry en = this.enService.getEnquiriesByRecipient(ese).iterator().next();
		
		this.mockMvc.perform(
				get("/profile/enquiries/acceptEnquiry")
				.param("id", ""+en.getId())
				).andExpect(status().is2xxSuccessful());
		
		VisitEnquiry en2 = this.visitEnquiryDao.findOne(en.getId());
		assertEquals(VisitEnquiryState.ACCEPTED, en2.getState());
		en2.setState(VisitEnquiryState.OPEN);
		this.visitEnquiryDao.save(en2);
	}
	
	@Test
	public void canDeclineAnEnquiry() throws Exception {
		this.login();
		User ese = userDao.findByUsername("ese@unibe.ch");
		
		VisitEnquiry en = this.enService.getEnquiriesByRecipient(ese).iterator().next();
		
		this.mockMvc.perform(
				get("/profile/enquiries/declineEnquiry")
				.param("id", ""+en.getId())
				).andExpect(status().is2xxSuccessful());
		
		VisitEnquiry en2 = this.visitEnquiryDao.findOne(en.getId());
		assertEquals(VisitEnquiryState.DECLINED, en2.getState());
		en2.setState(VisitEnquiryState.OPEN);
		this.visitEnquiryDao.save(en2);
	}

	@Test
	public void canReopenAnEnquiry() throws Exception {
		this.login();
		User ese = userDao.findByUsername("ese@unibe.ch");
		
		VisitEnquiry en = this.enService.getEnquiriesByRecipient(ese).iterator().next();
		en.setState(VisitEnquiryState.DECLINED);
		this.visitEnquiryDao.save(en);
		
		this.mockMvc.perform(
				get("/profile/enquiries/reopenEnquiry")
				.param("id", ""+en.getId())
				).andExpect(status().is2xxSuccessful());
		
		VisitEnquiry en2 = this.visitEnquiryDao.findOne(en.getId());
		assertEquals(VisitEnquiryState.OPEN, en2.getState());
	}

	@Test
	public void rateUserWillWork() throws Exception {
		this.login();
		User ese = userDao.findByUsername("ese@unibe.ch");
		User jane = userDao.findByUsername("jane@doe.com");
		
		this.mockMvc.perform(
				get("/profile/rateUser")
				.param("rate", ""+jane.getId())
				.param("stars", "5")
				).andExpect(status().is2xxSuccessful());
		
		Rating rating = enService.getRatingByRaterAndRatee(ese, jane);
		assertEquals(5, rating.getRating());
	}

	@Test
	public void getRating() throws Exception {
		this.login();
		User jane = userDao.findByUsername("jane@doe.com");
		
		this.mockMvc.perform(
				get("/profile/ratingFor")
				.param("user", ""+jane.getId())
				).andExpect(status().is2xxSuccessful());
	}
	
	
	private Ad generateAuctionAd(User user) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 7);
		Ad adWithAuction = new Ad();
		adWithAuction.setZipcode(5000);
		adWithAuction.setCreationDate(new Date());
		adWithAuction.setPrice(1000000);
		adWithAuction.setSquareFootage(60);
		adWithAuction.setNumberOfRooms(3);
		adWithAuction.setFlat(false);
		adWithAuction.setSmokers(false);
		adWithAuction.setAnimals(false);
		adWithAuction.setHouseDescription("testdescription");
		adWithAuction.setUser(user);
		adWithAuction.setTitle("Sweet House for Sale");
		adWithAuction.setStreet("Schwanenplace 61B");
		adWithAuction.setCity("Aarau");
		adWithAuction.setGarden(false);
		adWithAuction.setBalcony(false);
		adWithAuction.setCellar(false);
		adWithAuction.setFurnished(false);
		adWithAuction.setCable(false);
		adWithAuction.setGarage(false);
		adWithAuction.setForSale(true);
		adWithAuction.setRunningCosts(300);
		adWithAuction.setDistanceToNearestPublicTransport(1);
		adWithAuction.setDistanceToNearestSchool(1);
		adWithAuction.setDistanceToNearestSuperMarket(2);
		adWithAuction.setAuctionEndingDate(cal.getTime());
		adWithAuction.setAuctionStartingPrice(250000);
		adDao.save(adWithAuction);

		return adWithAuction;
	}	
}


