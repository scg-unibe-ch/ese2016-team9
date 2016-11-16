package ch.unibe.ese.team1.controller;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import ch.unibe.ese.team1.model.dao.AdDao;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})

@WebAppConfiguration
public class PlaceAdControllerTest {
	private MockMvc mockMvc;

	@Autowired
	private AdService adService;
	
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
	
	@Test
	public void placeAdFormIsNotValidWhenNotLoggedIn() throws Exception {
		ResultActions resultActions = this.mockMvc.perform(
				post("/profile/placeAd").with(csrf())
				.param("title", "testAd - placeAdFormIsNotValidWhenNotLoggedIn")
				.param("flat", "1")
				.param("street", "teststreet")
				.param("prize", "500")
				.param("squareFootage", "50")
				.param("floor", "2")
				.param("room", "2")
				.param("houseDescription", "beatuiful")
				.param("city", "3000 - Biel;Bienne")				
				).andExpect(status().is3xxRedirection());

		resultActions.andExpect(redirectedUrl("http://localhost/login"));
		
		Iterable<Ad> ads = this.adService.getNewestAds(1);
		for (Ad ad : ads) {
			assertNotEquals("testAd - placeAdFormIsNotValidWhenNotLoggedIn", ad.getTitle());
		}
	}
	

	@Test
	public void placeAdFormWillSaveAdEvenInBiel() throws Exception {
		this.login();
		ResultActions resultActions = this.mockMvc.perform(
				post("/profile/placeAd").with(csrf())
				.param("title", "testAd - placeAdFormWillSaveAdEvenInBiel")
				.param("flat", "1")
				.param("street", "teststreet")
				.param("prize", "500")
				.param("squareFootage", "50")
				.param("floor", "2")
				.param("room", "2")
				.param("houseDescription", "beatuiful")
				.param("city", "3000 - Biel;Bienne")				
				).andExpect(status().is3xxRedirection());
		
		Iterable<Ad> ads = this.adService.getNewestAds(1);
		for (Ad ad : ads) {
			assertEquals("testAd - placeAdFormWillSaveAdEvenInBiel", ad.getTitle());
		}
		
	}

	private void login() {

		mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .defaultRequest(get("/").with(user("ese@unibe.ch").roles("USER")))
                .addFilters(springSecurityFilterChain)
                .build();
		
	}
}


