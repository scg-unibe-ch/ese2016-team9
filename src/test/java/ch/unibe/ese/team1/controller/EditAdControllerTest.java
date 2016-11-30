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
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.dao.AdDao;
import ch.unibe.ese.team1.model.dao.UserDao;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})

@WebAppConfiguration
public class EditAdControllerTest {
	private MockMvc mockMvc;

	@Autowired
	private AdService adService;
	
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;
    
    @Autowired
    private AdDao adDao;
    
    @Autowired
    private UserDao userDao;
    
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
	public void editAdFormWillWorkEvenWithBiel() throws Exception {
		this.login();
		User ese = userDao.findByUsername("ese@unibe.ch");
		Ad ad = this.generateAuctionAd(ese);
		ResultActions resultActions = this.mockMvc.perform(
				post("/profile/editAd?adId=" + ad.getId()).with(csrf())
				.param("title", "Beautiful Flat in biel")
				.param("flat", "1")
				.param("street", "teststreet")
				.param("price", "500")
				.param("squareFootage", "50")
				.param("floor", "2")
				.param("room", "2")
				.param("houseDescription", "beatuiful")
				.param("city", "3000 - Biel;Bienne")				
				).andExpect(status().is3xxRedirection());
		resultActions.andExpect(redirectedUrl("/ad?id=" + ad.getId()));
		
		Ad otherAd = adDao.findOne(ad.getId());
		assertEquals("Biel;Bienne", otherAd.getCity());
		assertEquals(3000, otherAd.getZipcode());
	}
	

	@Test
	public void youShouldNotBeAllowedToEditSomeonesOtherAd() throws Exception {
		this.login();
		User jane = userDao.findByUsername("jane@doe.com");
		Ad ad = this.generateAuctionAd(jane);
		this.mockMvc.perform(
				post("/profile/editAd?adId=" + ad.getId()).with(csrf())
				.param("title", "Beautiful Flat in biel")
				.param("flat", "1")
				.param("street", "teststreet")
				.param("price", "500")
				.param("squareFootage", "50")
				.param("floor", "2")
				.param("room", "2")
				.param("houseDescription", "beatuiful")
				.param("city", "3012 - Bern")				
				).andExpect(status().is4xxClientError());
		
		Ad otherAd = adDao.findOne(ad.getId());
		assertEquals("Zürich", otherAd.getCity());
		assertEquals(5000, otherAd.getZipcode());
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
		adWithAuction.setCity("Zürich");
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


