package ch.unibe.ese.team1.model;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})
@WebAppConfiguration
public class AdTest {

	private Ad ad;
	
	@Before
	public void setUp() {
		this.ad = new Ad();
	}
	
	@Test
	public void checkIfThereIsAnAuctionPriceIsEqualToAuction() {
		this.ad.setAuctionStartingPrize(1000.50);
		assertFalse(this.ad.isAuction());
	}
	
	@Test
	public void checkIfThereIsAnAuctionEndDateIsEqualToAuction() {
		this.ad.setAuctionEndingDate(new Date());
		assertFalse(this.ad.isAuction());
	}
	
	@Test
	public void checkIfThereIsAnAuctionEndDateAndStartingPriceIsEqualToAuction() {
		this.ad.setAuctionEndingDate(new Date());
		this.ad.setAuctionStartingPrize(1000.50);
		assertTrue(this.ad.isAuction());
	}
	
	@Test
	public void getLastBetPriceWorks() {
		this.ad.setAuctionEndingDate(new Date());
		this.ad.setAuctionStartingPrize(500.00);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -2);
		
		Bet bet1 = new Bet();
		bet1.setPrice(1000);
		bet1.setCreationDate(cal.getTime());
		
		cal.add(Calendar.DATE, 1);
		
		Bet bet2 = new Bet();
		bet2.setPrice(2000);
		bet2.setCreationDate(cal.getTime());
		
		Set<Bet> bets = new HashSet<Bet>();
		bets.add(bet1);
		bets.add(bet2);
		
		this.ad.setBets(bets);

		assertTrue(this.ad.isAuction());
		assertEquals(2000, this.ad.getHighestBet(), 0.0);
	}
	
	@Test
	public void getLastBetPriceWorksNotIfNotAnAuction() {
		assertEquals(0, this.ad.getHighestBet(), 0.1);
	}
	
	@Test
	public void getLastBetPriceReturnsStartingPriceIfNoBetsAreMaken() {
		this.ad.setAuctionEndingDate(new Date());
		this.ad.setAuctionStartingPrize(500.00);
		
		assertEquals(500, this.ad.getHighestBet(), 0.1);
	}
	
	@Test
	public void getLastBiddingUser() {
		
		User user1 = new User();
		User user2 = new User();
		
		this.ad.setAuctionEndingDate(new Date());
		this.ad.setAuctionStartingPrize(500.00);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -2);
		
		Bet bet1 = new Bet();
		bet1.setPrice(1000);
		bet1.setCreationDate(cal.getTime());
		bet1.setUser(user1);
		
		cal.add(Calendar.DATE, 1);
		
		Bet bet2 = new Bet();
		bet2.setPrice(2000);
		bet2.setCreationDate(cal.getTime());
		bet2.setUser(user2);
		
		Set<Bet> bets = new HashSet<Bet>();
		bets.add(bet1);
		bets.add(bet2);
		
		this.ad.setBets(bets);
		
		assertEquals(user2, this.ad.getLastBiddingUser());
	}
	
	@Test
	public void isAuctionEnded() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -2);
		
		this.ad.setAuctionEndingDate(cal.getTime());
		assertTrue(this.ad.isAuctionEnded());
	}
	
	@Test
	public void isAuctionEndedReturnsFalseIfNot() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, +2);
		
		this.ad.setAuctionEndingDate(cal.getTime());
		assertFalse(this.ad.isAuctionEnded());
	}
}
