package ch.unibe.ese.team1.model;

import static org.junit.Assert.*;

import java.util.Date;

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
}
