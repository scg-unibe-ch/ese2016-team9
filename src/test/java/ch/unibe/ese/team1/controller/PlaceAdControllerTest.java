package ch.unibe.ese.team1.controller;

import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebClientBuilder;
import org.springframework.test.web.servlet.htmlunit.MockMvcWebConnection;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

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
public class PlaceAdControllerTest {
	private MockMvc mockMvc;

	@Autowired
	private AdService adService;
	
    @Autowired
    private WebApplicationContext context;

    @Autowired
    MockHttpSession httpSession;
    
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
	public void placeAdFormIsNotValidWhenNotLoggedIn() throws Exception {
		ResultActions resultActions = this.mockMvc.perform(
				post("/profile/placeAd").with(csrf())
				.param("title", "testAd - placeAdFormIsNotValidWhenNotLoggedIn")
				.param("flat", "1")
				.param("street", "teststreet")
				.param("price", "500")
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
				.param("price", "500")
				.param("squareFootage", "50")
				.param("floor", "2")
				.param("room", "2")
				.param("houseDescription", "beatuiful")
				.param("city", "3000 - Biel;Bienne")				
				).andExpect(status().is3xxRedirection());
		
		Ad ad = this.getAdFromUrl(resultActions.andReturn().getResponse().getRedirectedUrl());
		
		assertEquals("testAd - placeAdFormWillSaveAdEvenInBiel", ad.getTitle());	
	}

	@Test
	public void placeAdFormWillSaveAuctionData() throws Exception {
		this.login();
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, +7);
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		cal.set(Calendar.HOUR, 12);
		cal.set(Calendar.MINUTE, 30);
		ResultActions resultActions = this.mockMvc.perform(
				post("/profile/placeAd").with(csrf())
				.param("title", "testAd - placeAdFormWillSaveAuctionData")
				.param("flat", "1")
				.param("street", "teststreet")
				.param("price", "500000")
				.param("squareFootage", "50")
				.param("floor", "2")
				.param("room", "2")
				.param("houseDescription", "beatuiful")
				.param("city", "3012 - Biel;Bienne")	
				.param("auctionEndingDate", dateFormat.format(cal.getTime()))
				.param("auctionEndingHour", "12")
				.param("auctionEndingMinute", "30")
				.param("auctionStartingPrice", "150000.00")
				).andExpect(status().is3xxRedirection());

		Ad ad = this.getAdFromUrl(resultActions.andReturn().getResponse().getRedirectedUrl());
		
		assertEquals("testAd - placeAdFormWillSaveAuctionData", ad.getTitle());
		Calendar adCal = Calendar.getInstance();
		adCal.setTime(ad.getAuctionEndingDate());

		assertEquals(cal.get(Calendar.YEAR), adCal.get(Calendar.YEAR));
		assertEquals(cal.get(Calendar.MONTH), adCal.get(Calendar.MONTH));
		assertEquals(cal.get(Calendar.DATE), adCal.get(Calendar.DATE));
		assertEquals(cal.get(Calendar.HOUR), adCal.get(Calendar.HOUR));
		assertEquals(cal.get(Calendar.MINUTE), adCal.get(Calendar.MINUTE));
		
		assertEquals(150000.0, ad.getAuctionStartingPrice(),1);
		assertTrue(ad.isAuction());
		
	}
	
	@Test
	public void canSeePlaceAdForm() throws Exception {
		this.login();
		this.mockMvc.perform(
				get("/profile/placeAd")
				).andExpect(status().is2xxSuccessful())
				.andExpect(model().attributeDoesNotExist("isRentingAd"));
				
	}
	

	@Test
	public void canSeePlaceAdFormWithRentOption() throws Exception {
		this.login();
		this.mockMvc.perform(
				get("/profile/placeAd?rent=1")
				).andExpect(status().is2xxSuccessful())
				.andExpect(model().attributeExists("isRentingAd"));
	}
	

	@Test
	public void canUploadPicture() throws Exception {
		this.login();
		MockMultipartFile file = new MockMultipartFile("test.jpg", "test.jpg", "image/jpeg", new FileInputStream("src/test/files/test.jpg"));
        MockMultipartHttpServletRequestBuilder mockMultipartHttpServletRequestBuilder = (MockMultipartHttpServletRequestBuilder) fileUpload("/profile/placeAd/uploadPictures").accept(MediaType.ALL).session(httpSession);
        mockMultipartHttpServletRequestBuilder.file(file);
        mockMultipartHttpServletRequestBuilder.content("whatever");

        ResultActions resultActions = mockMvc.perform(mockMultipartHttpServletRequestBuilder);

        resultActions.andExpect(status().is2xxSuccessful());
	}
	

	@Test
	public void canUploadPicturesAndGetThemBack() throws Exception {
		this.login();
		MockMultipartFile file = new MockMultipartFile("test.jpg", "test.jpg", "image/jpeg", new FileInputStream("src/test/files/test.jpg"));
        MockMultipartHttpServletRequestBuilder mockMultipartHttpServletRequestBuilder = (MockMultipartHttpServletRequestBuilder) fileUpload("/profile/placeAd/uploadPictures").accept(MediaType.ALL).session(httpSession);
        mockMultipartHttpServletRequestBuilder.file(file);
        mockMultipartHttpServletRequestBuilder.content("whatever");

        ResultActions resultActions = mockMvc.perform(mockMultipartHttpServletRequestBuilder);

        resultActions.andExpect(status().is2xxSuccessful());

        MvcResult result = this.mockMvc.perform(
				post("/profile/placeAd/getUploadedPictures")
				).andExpect(status().is2xxSuccessful()).andReturn();
		
		assertTrue(result.getResponse().getContentAsString().contains("test.jpg"));
        
	}
	

	@Test
	public void canDeleteUploadedPictures() throws Exception {
		this.login();
		MockMultipartFile file = new MockMultipartFile("test.jpg", "test.jpg", "image/jpeg", new FileInputStream("src/test/files/test.jpg"));
        MockMultipartHttpServletRequestBuilder mockMultipartHttpServletRequestBuilder = (MockMultipartHttpServletRequestBuilder) fileUpload("/profile/placeAd/uploadPictures").accept(MediaType.ALL).session(httpSession);
        mockMultipartHttpServletRequestBuilder.file(file);
        mockMultipartHttpServletRequestBuilder.content("whatever");

        ResultActions resultActions = mockMvc.perform(mockMultipartHttpServletRequestBuilder);

        resultActions.andExpect(status().is2xxSuccessful());

        MvcResult result = this.mockMvc.perform(
				post("/profile/placeAd/getUploadedPictures")
				).andExpect(status().is2xxSuccessful()).andReturn();
		
        String in = result.getResponse().getContentAsString();
		
        Pattern p = Pattern.compile(".*\"url\":\"(.*)\".*");
        Matcher m = p.matcher(in);
        assertTrue(m.matches());
        
        String url = m.group(1);
        
        this.mockMvc.perform(
				post("/profile/placeAd/deletePicture")
				.param("url", url)
				).andExpect(status().is2xxSuccessful());
	}
	
	private Ad getAdFromUrl(String url) {
		Pattern p = Pattern.compile("\\/ad\\?id=(\\d+)");

		Matcher m = p.matcher(url);
		boolean b = m.matches();
		assertTrue(b);
		
		long id = Long.parseLong(m.group(1));
		
		return this.adService.getAdById(id);
	}
	
}


