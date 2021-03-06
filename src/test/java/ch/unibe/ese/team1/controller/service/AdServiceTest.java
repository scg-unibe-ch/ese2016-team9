package ch.unibe.ese.team1.controller.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import ch.unibe.ese.team1.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team1.controller.pojos.forms.SearchForm;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.Gender;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.UserRole;
import ch.unibe.ese.team1.model.dao.AdDao;
import ch.unibe.ese.team1.model.dao.UserDao;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
public class AdServiceTest {

	@Autowired
	private AdService adService;
	
	@Autowired
	private UserDao userDao;

	/**
	 * In order to test the saved ad, I need to get it back from the DB again, so these
	 * two methods need to be tested together, normally we want to test things isolated of
	 * course. Testing just the returned ad from saveFrom() wouldn't answer the question 
	 * whether the ad has been saved correctly to the db.
	 * @throws ParseException 
	 */
	@Test
	public void saveFromAndGetById() throws ParseException {
		//Preparation
		PlaceAdForm placeAdForm = new PlaceAdForm();
		placeAdForm.setCity("3018 - Bern");
		placeAdForm.setHouseDescription("Test House description");
		placeAdForm.setPrice(600.0);
		placeAdForm.setSquareFootage(50.0);
		placeAdForm.setTitle("title");
		placeAdForm.setStreet("Hauptstrasse 13");
		placeAdForm.setFlat(true);
		placeAdForm.setMoveInDate("27-02-2015");
		
		placeAdForm.setSmokers(true);
		placeAdForm.setAnimals(false);
		placeAdForm.setGarden(true);
		placeAdForm.setBalcony(false);
		placeAdForm.setCellar(true);
		placeAdForm.setFurnished(false);
		placeAdForm.setCable(false);
		placeAdForm.setGarage(true);
		
		ArrayList<String> filePaths = new ArrayList<>();
		filePaths.add("/img/test/ad1_1.jpg");
		
		User hans = createUser("hans@kanns.ch", "password", "Hans", "Kanns",
				Gender.MALE);
		hans.setAboutMe("Hansi Hinterseer");
		userDao.save(hans);
		
		adService.saveFrom(placeAdForm, filePaths, hans);
		
		Ad ad = new Ad();
		Iterable<Ad> ads = adService.getAllAds();
		Iterator<Ad> iterator = ads.iterator();
		
		while (iterator.hasNext()) {
			ad = iterator.next();
		}
		
		//Testing
		assertTrue(ad.getSmokers());
		assertFalse(ad.getAnimals());
		assertEquals("Bern", ad.getCity());
		assertEquals(3018, ad.getZipcode());
		assertEquals("Test House description", ad.getHouseDescription());
		assertEquals(600, ad.getPrice(), 0);
		assertEquals(50, ad.getSquareFootage(), 0);
		assertEquals("title", ad.getTitle());
		assertEquals("Hauptstrasse 13", ad.getStreet());
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    Date result =  df.parse("2015-02-27");
		
		assertEquals(0, result.compareTo(ad.getMoveInDate()));
	}

	/**
	 * Test our newly implemented SearchCriterias
	 * @throws ParseException 
	 */
	@Test
	public void newSearchCriterias() throws ParseException {
		//Preparation
		SearchForm searchForm = new SearchForm();
		
		searchForm.setRadius(10);
		searchForm.setPrice(1500.0);
		searchForm.setFlat(true);
		searchForm.setDistanceToNearestPublicTransport(5000);
		searchForm.setDistanceToNearestSchool(5000);
		searchForm.setDistanceToNearestSuperMarket(5000);
		searchForm.setIncludeRunningCosts(false);
		searchForm.setForSale(true);
		searchForm.setNumberOfRooms(1);
		searchForm.setCity("5000 - Aarau");
		
		Iterable<Ad> ads = adService.queryResults(searchForm);
		
		int count = 0;
		for (Ad ad : ads) {
			count++;
		}
		assertEquals(1, count);
	}
		
	@Test
	public void saveFromWithoutRestorationDatesWillNotEndInException() throws ParseException {
		//Preparation
		PlaceAdForm placeAdForm = new PlaceAdForm();
		placeAdForm.setCity("3018 - Bern");
		placeAdForm.setHouseDescription("Test House description");
		placeAdForm.setPrice(600);
		placeAdForm.setSquareFootage(50);
		placeAdForm.setTitle("title");
		placeAdForm.setStreet("Hauptstrasse 13");
		placeAdForm.setFlat(true);

		placeAdForm.setMoveInDate("27-02-2015");
		
		ArrayList<String> filePaths = new ArrayList<>();
		User hans = createUser("hans1@kanns.ch", "password", "Hans", "Kanns",
				Gender.MALE);
		hans.setAboutMe("Hansi Hinterseer");
		userDao.save(hans);
		
		adService.saveFrom(placeAdForm, filePaths, hans);
	}
	
	@Test
	public void saveFromWithoutMoveOutDatesWillNotEndInException() throws ParseException {
		//Preparation
		PlaceAdForm placeAdForm = new PlaceAdForm();
		placeAdForm.setCity("3018 - Bern");
		placeAdForm.setHouseDescription("Test House description");
		placeAdForm.setPrice(600);
		placeAdForm.setSquareFootage(50);
		placeAdForm.setTitle("title");
		placeAdForm.setStreet("Hauptstrasse 13");
		placeAdForm.setFlat(true);

		placeAdForm.setLastRenovation("27-02-2015");
		placeAdForm.setMoveInDate("27-04-2015");
		
		ArrayList<String> filePaths = new ArrayList<>();
		User hans = createUser("hans3@kanns.ch", "password", "Hans", "Kanns",
				Gender.MALE);
		hans.setAboutMe("Hansi Hinterseer");
		userDao.save(hans);
		
		adService.saveFrom(placeAdForm, filePaths, hans);
	}
	
	@Test
	public void getAllBetsBySpecificAd() {
		Iterable<Ad> ads = adService.getAllAds();
		
		Ad currentAd = null;
		for (Ad ad : ads) {
			if (currentAd == null && ad.isAuction()) {
				currentAd = ad;
			}
		}
		
		assertEquals(3, currentAd.getBets().size());
	}
	
	@Test
	public void getAllAdsOnHomepageGetsCorrectNumber() {
		Iterable<Ad> ads = adService.getHomepageAds(12);
		
		int count = 0;
		for (Ad ad : ads) {
			count++;
		}
		
		assertEquals(12, count);
	}
	
	@Test
	public void getAllAdsOnHomepageGetsBoth() {
		Iterable<Ad> ads = adService.getHomepageAds(12);
		
		boolean chkIs = false;
		boolean chkIsNot = false;
		
		for (Ad ad : ads) {
			if (ad.isOnHomepage()) {
				chkIs = true;
			} else {
				chkIsNot = true;
			}
		}
		
		assertTrue(chkIs);
		assertTrue(chkIsNot);
	}
	
	@Test
	public void getHomepageAds() {
		Iterable<Ad> ads = adService.getAllAds();
		
		Ad currentAd = null;
		for (Ad ad : ads) {
			if (currentAd == null && ad.isAuction()) {
				currentAd = ad;
			}
		}
		
		assertEquals(3, currentAd.getBets().size());
	}
	
	private User createUser(String email, String password, String firstName,
			String lastName, Gender gender) {
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
