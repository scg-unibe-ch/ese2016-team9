package ch.unibe.ese.team1.controller;

import static org.junit.Assert.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Calendar;
import java.util.Date;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ch.unibe.ese.team1.controller.service.AdService;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.Bet;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.dao.AdDao;
import ch.unibe.ese.team1.model.dao.BetDao;
import ch.unibe.ese.team1.model.dao.UserDao;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})

@WebAppConfiguration
public class AdControllerTest {
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
	public void canPlaceABetToAnAuction() throws Exception {
		this.login();
		User jane = userDao.findByUsername("jane@doe.com");
		Ad ad = this.generateAuctionAd(jane);

		this.mockMvc.perform(
				post("/makeBet?id="+ad.getId()).with(csrf())
				.param("price", "500000")
				).andExpect(status().is3xxRedirection());

		// retrieve ad from db
		Ad OtherAd = adService.getAdById(ad.getId());
		assertEquals(1, OtherAd.getBets().size());
	}

	@Test
	public void canNotPlaceBetWithPriceLessThanActualHighestBid() throws Exception {
		this.login();
		User jane = userDao.findByUsername("jane@doe.com");
		User bernerBaer = userDao.findByUsername("user@bern.com");
		Ad ad = this.generateAuctionAd(jane);

		Bet bet = new Bet();
		bet.setAd(ad);
		bet.setPrice(500000);
		bet.setUser(bernerBaer);
		bet.setCreationDate(new Date());
		
		betDao.save(bet);
		
		this.mockMvc.perform(
				post("/makeBet?id="+ad.getId()).with(csrf())
				.param("price", "500000")
				).andExpect(status().is2xxSuccessful());

		// retrieve ad from db
		Ad OtherAd = adService.getAdById(ad.getId());
		assertEquals(1, OtherAd.getBets().size());
	}
	
	

	private Ad generateAuctionAd(User user) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 7);
		Ad adWithAuction = new Ad();
		adWithAuction.setZipcode(5000);
		adWithAuction.setCreationDate(new Date());
		adWithAuction.setPrize(1000000);
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
		adWithAuction.setAuctionStartingPrize(250000);
		adDao.save(adWithAuction);

		return adWithAuction;
	}
	
}


