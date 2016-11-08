package ch.unibe.ese.team1.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import javax.servlet.Filter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})

@TestExecutionListeners({
	DependencyInjectionTestExecutionListener.class,
	DirtiesContextTestExecutionListener.class,
	TransactionalTestExecutionListener.class
})
@WebAppConfiguration
public class PlaceAdControllerTest {
	private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;
    
	@Before
	public void setup() throws Exception {
		mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
//                .defaultRequest(get("/").with(user("user").roles("ADMIN")))
                .addFilters(springSecurityFilterChain)
                .build();
	}
	
	@Test
	public void placeAdFormIsNotValidWhenNotLoggedIn() throws Exception {
		ResultActions resultActions = this.mockMvc.perform(
				post("/profile/placeAd"))
				.andExpect(redirectedUrl("http://localhost/login"));

		//resultActions.andDo(MockMvcResultHandlers.print());;
	}
	

	@Test
	public void placeAdFormWillSaveAdEvenInBiel() throws Exception {
		this.login();
		ResultActions resultActions = this.mockMvc.perform(
				post("/profile/placeAd")
				.param("title", "testAd")
				.param("flat", "1")
				.param("street", "teststreet")
				.param("prize", "500")
				.param("squareFootage", "50")
				.param("floor", "2")
				.param("room", "2")
				.param("houseDescription", "beatuiful")
				.param("city", "3000 - Biel;Bienne")
				).andExpect(status().is3xxRedirection());
		
		//resultActions.andDo(MockMvcResultHandlers.print());
	}

	private void login() {

		mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .defaultRequest(get("/").with(user("ese@unibe.ch").roles("USER")))
                .addFilters(springSecurityFilterChain)
                .build();
		
	}
}


