package ch.unibe.ese.team1.controller;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.hamcrest.Matchers.containsString;


import java.util.Calendar;
import java.util.Date;
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
import ch.unibe.ese.team1.controller.service.MessageService;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.Bet;
import ch.unibe.ese.team1.model.Message;
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
    private MessageService messageService;
    
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
		
		ResultActions resultAction = this.mockMvc.perform(
				post("/makeBet?id="+ad.getId()).with(csrf())
				.param("price", "500000")
				);
		
		resultAction.andExpect(status().is2xxSuccessful());
		// retrieve ad from db
		Ad OtherAd = adService.getAdById(ad.getId());
		assertEquals(1, OtherAd.getBets().size());
	}
	
	@Test
	public void getPlaceOnHomepageFormIfYouAreTheAuthor() throws Exception {
		this.login();
		User ese = userDao.findByUsername("ese@unibe.ch");
		Ad ad = this.generateAuctionAd(ese);
		this.mockMvc.perform(
				get("/ad/placeOnHomepage?id="+ad.getId())
				).andExpect(status().is2xxSuccessful());
	}
	
	@Test
	public void get403PlaceOnHomepageFormIfYouAreNotTheAuthor() throws Exception {
		this.login();
		User jane = userDao.findByUsername("jane@doe.com");
		Ad ad = this.generateAuctionAd(jane);
		this.mockMvc.perform(
				get("/ad/placeOnHomepage?id="+ad.getId())
				).andExpect(status().is4xxClientError());
		
	}
	
	@Test
	public void setAnAdToTheHomepage() throws Exception {
		this.login();
		User ese = userDao.findByUsername("ese@unibe.ch");
		Ad ad = this.generateAuctionAd(ese);
		this.mockMvc.perform(
				post("/ad/placeOnHomepage?id="+ad.getId())
				).andExpect(status().is3xxRedirection());
		// retrieve ad from db
		Ad OtherAd = adService.getAdById(ad.getId());
		assertTrue(OtherAd.isOnHomepage());
		
	}
	
	@Test
	public void cannotPlaceAnotherOnesAdOnHomepage() throws Exception {
		this.login();
		User jane = userDao.findByUsername("jane@doe.com");
		User ese = userDao.findByUsername("ese@unibe.ch");
		Ad ad = this.generateAuctionAd(jane);
		this.mockMvc.perform(
				post("/ad/placeOnHomepage?id="+ad.getId())
				).andExpect(status().is4xxClientError());
		// retrieve ad from db
		Ad OtherAd = adService.getAdById(ad.getId());
		assertFalse(OtherAd.isOnHomepage());
		
	}
	
	@Test
	public void canNotPlaceBetWhenIHaveAlreadyOne() throws Exception {
		this.login();
		User jane = userDao.findByUsername("jane@doe.com");
		User ese = userDao.findByUsername("ese@unibe.ch");
		Ad ad = this.generateAuctionAd(jane);

		Bet bet = new Bet();
		bet.setAd(ad);
		bet.setPrice(500000);
		bet.setUser(ese);
		bet.setCreationDate(new Date());
		
		betDao.save(bet);
		
		ResultActions resultAction = this.mockMvc.perform(
				post("/makeBet?id="+ad.getId()).with(csrf())
				.param("price", "600000")
				);
		
		resultAction.andExpect(status().is2xxSuccessful());
		// retrieve ad from db
		Ad OtherAd = adService.getAdById(ad.getId());
		assertEquals(1, OtherAd.getBets().size());
	}
	
	@Test
	public void canNotPlaceBetWhenAuctionIsFinished() throws Exception {
		this.login();
		User jane = userDao.findByUsername("jane@doe.com");
		User ese = userDao.findByUsername("ese@unibe.ch");
		Ad ad = this.generateAuctionAd(jane);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -7);
		ad.setAuctionEndingDate(cal.getTime());
		this.adDao.save(ad);

		ResultActions resultAction = this.mockMvc.perform(
				post("/makeBet?id="+ad.getId()).with(csrf())
				.param("price", "500000")
				);
		
		resultAction.andExpect(status().is2xxSuccessful());
		// retrieve ad from db
		Ad OtherAd = adService.getAdById(ad.getId());
		assertEquals(0, OtherAd.getBets().size());
	}
	
	@Test
	public void canNotSeeBookmarksWhenNotLoggedIn() throws Exception {
		ResultActions resultAction = this.mockMvc.perform(get("/profile/myBookmarks"));
		
		resultAction.andExpect(status().is3xxRedirection());
	}
	
	@Test
	public void canSeeBookmarksWhenLoggedIn() throws Exception {
		this.login();
		
		ResultActions resultAction = this.mockMvc.perform(get("/profile/myBookmarks"));
		
		resultAction.andExpect(status().is2xxSuccessful());
		resultAction.andExpect(model().attributeExists("bookmarkedAdvertisements"));
	}

	@Test
	public void canNotSeeOwnAdvertismentsWhenNotLoggedIn() throws Exception {
		ResultActions resultAction = this.mockMvc.perform(get("/profile/myHouses"));
		
		resultAction.andExpect(status().is3xxRedirection());
	}
	
	@Test
	public void canSeeOwnAdvertismentsWhenLoggedIn() throws Exception {
		this.login();
		
		ResultActions resultAction = this.mockMvc.perform(get("/profile/myHouses"));
		
		resultAction.andExpect(status().is2xxSuccessful());
		resultAction.andExpect(model().attributeExists("ownAdvertisements"));
	}
	
	@Test
	public void canSeeAd() throws Exception {
		this.login();
		

		User ese = userDao.findByUsername("ese@unibe.ch");
		Ad ad = this.generateAuctionAd(ese);
		
		ResultActions resultAction = this.mockMvc.perform(get("/ad?id="+ad.getId()));
		
		
		resultAction.andExpect(status().is2xxSuccessful());
		resultAction.andExpect(model().attributeExists("shownAd"));
		resultAction.andExpect(model().attribute("shownAd", ad));
		
	}
	
	@Test
	public void canSendMessageThroughTheAdPage() throws Exception {
		this.login();
		User jane = userDao.findByUsername("jane@doe.com");
		User ese = userDao.findByUsername("ese@unibe.ch");
		Ad ad = this.generateAuctionAd(jane);

		// retrieve current messages for jane
		Iterable<Message> msgs = this.messageService.getInboxForUser(jane);
		int count = 0;
		for (Message msg : msgs) {
			count++;
		}
		int totalOld = count;
		
		ResultActions resultAction = this.mockMvc.perform(
				post("/ad?id="+ad.getId()).with(csrf())
				.param("subject", "testSubject")
				.param("text", "testText")
				.param("recipient", "jane@doe.com")
				);
		
		resultAction.andExpect(status().is2xxSuccessful());
		// retrieve messages for jane
		Iterable<Message> msgsNew = this.messageService.getInboxForUser(jane);
		count = 0;
		for (Message msg : msgsNew) {
			count++;
		}
		assertEquals(totalOld + 1, count);
	}
	
	@Test
	public void getRateAuctionCannotBeMadeWhenAdIsNotAnAuction() throws Exception {
		this.login();
		User jane = userDao.findByUsername("jane@doe.com");
		Ad ad = this.generateAuctionAd(jane);
		ad.setAuctionEndingDate(null);
		this.adDao.save(ad);
		
		ResultActions resultAction = this.mockMvc.perform(get("/rateAd?id="+ad.getId()+"&rate="+jane.getId()).with(csrf()));
		
		resultAction.andExpect(status().is4xxClientError());
	}

	@Test
	public void getRateAuctionCannotBeMadeWhenAuctionIsNotFinished() throws Exception {
		this.login();
		User jane = userDao.findByUsername("jane@doe.com");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 7);
		Ad ad = this.generateAuctionAd(jane, cal.getTime());
		
		ResultActions resultAction = this.mockMvc.perform(get("/rateAd?id="+ad.getId()+"&rate="+jane.getId()).with(csrf()));
		
		resultAction.andExpect(status().is4xxClientError());
	}

	@Test
	public void getRateAuctionCannotBeMadeWhenUserIsNotAdvertiserAndNotBuyer() throws Exception {
		this.login();
		User jane = userDao.findByUsername("jane@doe.com");
		User bernerBaer = userDao.findByUsername("user@bern.com");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 7);
		Ad ad = this.generateAuctionAd(jane, cal.getTime());
		addBet(ad, bernerBaer, 50000, new Date());
		
		ResultActions resultAction = this.mockMvc.perform(get("/rateAd?id="+ad.getId()+"&rate="+jane.getId()).with(csrf()));
		
		resultAction.andExpect(status().is4xxClientError());
	}

	@Test
	public void getRateAuctionCanBeMadeIfAuctionHasFinishedAndUserIsBuyerOrSeller() throws Exception {
		this.login();
		User jane = userDao.findByUsername("jane@doe.com");
		User ese = userDao.findByUsername("ese@unibe.ch");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -7);
		Ad ad = this.generateAuctionAd(jane, cal.getTime());
		cal.add(Calendar.DATE, -12);
		
		addBet(ad, ese, 50000, cal.getTime());
		
		ResultActions resultAction = this.mockMvc.perform(get("/rateAd?id="+ad.getId()+"&rate="+jane.getId()).with(csrf()));
		
		resultAction.andExpect(status().is2xxSuccessful());
	}
	

	
	@Test
	public void postRateAuctionCannotBeMadeWhenAdIsNotAnAuction() throws Exception {
		this.login();
		User jane = userDao.findByUsername("jane@doe.com");
		Ad ad = this.generateAuctionAd(jane);
		ad.setAuctionEndingDate(null);
		this.adDao.save(ad);
		
		ResultActions resultAction = this.mockMvc.perform(post("/rateAd?id="+ad.getId()+"&rate="+jane.getId()).with(csrf()));
		
		resultAction.andExpect(status().is4xxClientError());
	}

	@Test
	public void postRateAuctionCannotBeMadeWhenAuctionIsNotFinished() throws Exception {
		this.login();
		User jane = userDao.findByUsername("jane@doe.com");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 7);
		Ad ad = this.generateAuctionAd(jane, cal.getTime());
		
		ResultActions resultAction = this.mockMvc.perform(post("/rateAd?id="+ad.getId()+"&rate="+jane.getId()).with(csrf()));
		
		resultAction.andExpect(status().is4xxClientError());
	}

	@Test
	public void postRateAuctionCannotBeMadeWhenUserIsNotAdvertiserAndNotBuyer() throws Exception {
		this.login();
		User jane = userDao.findByUsername("jane@doe.com");
		User bernerBaer = userDao.findByUsername("user@bern.com");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -7);
		Ad ad = this.generateAuctionAd(jane, cal.getTime());
		cal.add(Calendar.DATE, -7);
		addBet(ad, bernerBaer, 50000, cal.getTime());
		
		ResultActions resultAction = this.mockMvc.perform(post("/rateAd?id="+ad.getId()+"&rate="+jane.getId()).with(csrf()));
		
		resultAction.andExpect(status().is4xxClientError());
	}

	@Test
	public void postRateAuctionCanBeMadeIfAuctionHasFinishedAndUserIsBuyerOrSeller() throws Exception {
		this.login();
		User jane = userDao.findByUsername("jane@doe.com");
		User ese = userDao.findByUsername("ese@unibe.ch");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -7);
		Ad ad = this.generateAuctionAd(jane, cal.getTime());
		cal.add(Calendar.DATE, -12);
		
		addBet(ad, ese, 50000, cal.getTime());
		
		ResultActions resultAction = this.mockMvc.perform(post("/rateAd?id="+ad.getId()+"&rate="+jane.getId()).with(csrf()).param("rating", "1"));
		
		resultAction.andExpect(status().is3xxRedirection());
		
		//retrieve jane from db
		User jane1 = userDao.findByUsername("jane@doe.com");
		assertEquals(1, jane1.getLikes());
	}

	@Test
	public void bookmarksReturnZeroIfUserIsNull() throws Exception {
		User jane = userDao.findByUsername("jane@doe.com");
		Ad ad = this.generateAuctionAd(jane);
		
		ResultActions resultAction = this.mockMvc.perform(
				post("/bookmark").with(csrf())
				.param("id", ""+ad.getId())
				.param("screening", "1")
				.param("bookmarked", "0"));
		
		resultAction.andExpect(status().is2xxSuccessful());
		resultAction.andExpect(content().string(containsString("0")));
	}

	@Test
	public void bookmarksReturnOneIfUserIsFake() throws Exception {
		mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .defaultRequest(get("/").with(user("fake@fakerino.ch").roles("USER")))
                .addFilters(springSecurityFilterChain)
                .build();

		User jane = userDao.findByUsername("jane@doe.com");
		Ad ad = this.generateAuctionAd(jane);
		
		ResultActions resultAction = this.mockMvc.perform(
				post("/bookmark").with(csrf())
				.param("id", ""+ad.getId())
				.param("screening", "1")
				.param("bookmarked", "0"));
		
		resultAction.andExpect(status().is2xxSuccessful());
		resultAction.andExpect(content().string(containsString("1")));
	}
	
	@Test
	public void bookmarksReturnTwoIfUserHasntBookmarkedIt() throws Exception {
		this.login();
		User jane = userDao.findByUsername("jane@doe.com");
		Ad ad = this.generateAuctionAd(jane);
		
		ResultActions resultAction = this.mockMvc.perform(
				post("/bookmark").with(csrf())
				.param("id", ""+ad.getId())
				.param("screening", "1")
				.param("bookmarked", "0"));
		
		resultAction.andExpect(status().is2xxSuccessful());
		resultAction.andExpect(content().string(containsString("2")));
	}
	
	@Test
	public void bookmarksReturnThreeIfUserHasBookmarkedIt() throws Exception {
		this.login();
		User jane = userDao.findByUsername("jane@doe.com");
		User ese = userDao.findByUsername("ese@unibe.ch");
		Ad ad = this.generateAuctionAd(jane);
		Set<Ad> bAd = ese.getBookmarkedAds();
		bAd.add(ad);
		userDao.save(ese);
		
		ResultActions resultAction = this.mockMvc.perform(
				post("/bookmark").with(csrf())
				.param("id", ""+ad.getId())
				.param("screening", "1")
				.param("bookmarked", "0"));
		
		resultAction.andExpect(status().is2xxSuccessful());
		resultAction.andExpect(content().string(containsString("3")));
	}
	
	@Test
	public void bookmarksReturnFourIfOwnAd() throws Exception {
		this.login();
		User ese = userDao.findByUsername("ese@unibe.ch");
		Ad ad = this.generateAuctionAd(ese);
		
		ResultActions resultAction = this.mockMvc.perform(
				post("/bookmark").with(csrf())
				.param("id", ""+ad.getId())
				.param("screening", "1")
				.param("bookmarked", "0"));
		
		resultAction.andExpect(status().is2xxSuccessful());
		resultAction.andExpect(content().string(containsString("4")));
	}
	

	@Test
	public void willBookmarkAnAd() throws Exception {
		this.login();
		User jane = userDao.findByUsername("jane@doe.com");
		Ad ad = this.generateAuctionAd(jane);
		
		ResultActions resultAction = this.mockMvc.perform(
				post("/bookmark").with(csrf())
				.param("id", ""+ad.getId())
				.param("screening", "0")
				.param("bookmarked", "0"));
		
		resultAction.andExpect(status().is2xxSuccessful());
		resultAction.andExpect(content().string(containsString("3")));
	}
	
	private Bet addBet(Ad ad, User user, double price, Date date) {
		Bet bet = new Bet();
		bet.setAd(ad);
		bet.setPrice(price);
		bet.setUser(user);
		bet.setCreationDate(date);
		
		betDao.save(bet);
		return bet;
	}
	
	private Ad generateAuctionAd(User user, Date date) {
		Ad ad = this.generateAuctionAd(user);
		ad.setAuctionEndingDate(date);
		adDao.save(ad);
		return ad;
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