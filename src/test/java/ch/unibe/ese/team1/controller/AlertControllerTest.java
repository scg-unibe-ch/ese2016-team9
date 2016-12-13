package ch.unibe.ese.team1.controller;

import static org.junit.Assert.*;
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
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import ch.unibe.ese.team1.controller.service.AdService;
import ch.unibe.ese.team1.controller.service.AlertService;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.Alert;
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
public class AlertControllerTest {
	private MockMvc mockMvc;
	
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;
    
    @Autowired
    private AlertService alertService;
    
    @Autowired
    private UserDao userDao;
    
    
	@Before
	public void setup() throws Exception {
		mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .defaultRequest(get("/").with(user("ese@unibe.ch").roles("USER")))
                .addFilters(springSecurityFilterChain)
                .build();
	}
	
	@Test
	public void canSeeAlertPage() throws Exception {
		this.mockMvc.perform(
				get("/profile/alerts")			
				).andExpect(status().is2xxSuccessful());
	}
		
	@Test
	public void canMakeAnAlert() throws Exception {
		User ese = userDao.findByUsername("ese@unibe.ch");
		int count = 0;
		for (Alert alert : alertService.getAlertsByUser(ese)) {
			count++;
		}
		int total = count;
		
		this.mockMvc.perform(
				post("/profile/alerts")
				.param("radius", "1000")
				.param("price", "100000")
				.param("city", "3000 - Biel;Bienne")
				.param("numberOfRooms", "3")
				.param("squareFootage", "40")
				.param("bothHouseAndFlat", "1")
				.param("noHouseNoFlat", "0")
				.param("noSellNoRent", "0")							
				).andExpect(status().is2xxSuccessful());
		count = 0;
		for (Alert alert : alertService.getAlertsByUser(ese)) {
			count++;
		}
		assertEquals(total + 1, count);
	}
	
	@Test
	public void deleteAlertWorks() throws Exception {
		User ese = userDao.findByUsername("ese@unibe.ch");
		int count = 0;
		long lastId = 0;
		for (Alert alert : alertService.getAlertsByUser(ese)) {
			count++;
			lastId = alert.getId();
		}
		int total = count;
		
		this.mockMvc.perform(
				get("/profile/alerts/deleteAlert")
				.param("id", ""+lastId)							
				).andExpect(status().is2xxSuccessful());
		
		count = 0;
		for (Alert alert : alertService.getAlertsByUser(ese)) {
			count++;
		}
		assertEquals(total - 1, count);
	}
	
	@Test
	public void canSeeDisclamerPage() throws Exception {
		this.mockMvc.perform(
				get("/disclaimer")			
				).andExpect(status().is2xxSuccessful());
	}	
}