package ch.unibe.ese.team1.controller;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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
import ch.unibe.ese.team1.controller.service.VisitService;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.Bet;
import ch.unibe.ese.team1.model.Gender;
import ch.unibe.ese.team1.model.Message;
import ch.unibe.ese.team1.model.MessageState;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.Visit;
import ch.unibe.ese.team1.model.dao.AdDao;
import ch.unibe.ese.team1.model.dao.BetDao;
import ch.unibe.ese.team1.model.dao.MessageDao;
import ch.unibe.ese.team1.model.dao.UserDao;
import ch.unibe.ese.team1.model.dao.VisitDao;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"file:src/main/webapp/WEB-INF/config/springMVC.xml",
		"file:src/main/webapp/WEB-INF/config/springData.xml",
		"file:src/main/webapp/WEB-INF/config/springSecurity.xml"})

@WebAppConfiguration
public class MessageControllerTest {
	private MockMvc mockMvc;
	
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;
    
    @Autowired
    private AdService adService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private MessageDao messageDao;
    
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
	public void canNotListMessagesWhenNotLoggedIn() throws Exception {
		this.mockMvc.perform(
				get("/profile/messages")			
				).andExpect(status().is3xxRedirection());
		
	}

	@Test
	public void canListMessagesWhenLoggedIn() throws Exception {
		this.login();
		this.mockMvc.perform(
				get("/profile/messages")			
				).andExpect(status().is2xxSuccessful());
		
	}

	@Test
	public void canGetInboxByInboxRequest() throws Exception {
		this.login();
		this.mockMvc.perform(
				post("/profile/message/inbox")			
				).andExpect(status().is2xxSuccessful());
		
	}

	@Test
	public void canGetSentBySentRequest() throws Exception {
		this.login();
		this.mockMvc.perform(
				post("/profile/message/sent")			
				).andExpect(status().is2xxSuccessful());
		
	}

	@Test
	public void getMessageById() throws Exception {
		this.login();
		User ese = userDao.findByUsername("ese@unibe.ch");
		User jane = userDao.findByUsername("jane@doe.com");
		
		Message msg = this.createNewMessage(jane, ese);
		
		this.mockMvc.perform(
				get("/profile/messages/getMessage")	
				.param("id", ""+msg.getId())
				).andExpect(status().is2xxSuccessful());
				
		
	}

	@Test
	public void getPostMessageList() throws Exception {
		this.login();
		
		this.mockMvc.perform(
				post("/profile/messages")	
				).andExpect(status().is2xxSuccessful());
		
	}
	
	@Test
	public void sendThroughPostMessageList() throws Exception {
		this.login();
		User jane = userDao.findByUsername("jane@doe.com");
		int total = 0;
		int count = 0;
		for (Message msg : this.messageDao.findByRecipient(jane)) {
			count++;
		}
		total = count;
		this.mockMvc.perform(
				post("/profile/messages")
				.param("recipient", jane.getUsername())
				.param("subject", "blupp")
				.param("text", "testtext")
				).andExpect(status().is2xxSuccessful());
		
		count = 0;
		for (Message msg : this.messageDao.findByRecipient(jane)) {
			count++;
		}
		assertEquals(total+1, count);
	}
	

	@Test
	public void readMessageWillMarkMessageAsRead() throws Exception {
		this.login();
		User ese = userDao.findByUsername("ese@unibe.ch");
		User jane = userDao.findByUsername("jane@doe.com");
		
		Message msg = this.createNewMessage(jane, ese);
		
		this.mockMvc.perform(
				get("/profile/readMessage")	
				.param("id", ""+msg.getId())
				).andExpect(status().is2xxSuccessful());
			
		Message msg2 = this.messageDao.findOne(msg.getId());
		assertEquals(MessageState.READ, msg2.getState());
		
	}
	

	@Test
	public void unreadMessageWillMarkMessageAsUnread() throws Exception {
		this.login();
		User ese = userDao.findByUsername("ese@unibe.ch");
		User jane = userDao.findByUsername("jane@doe.com");
		
		Message msg = this.createNewMessage(jane, ese);
		msg.setState(MessageState.READ);
		this.mockMvc.perform(
				get("/profile/unread")	
				.param("id", ""+msg.getId())
				).andExpect(status().is2xxSuccessful());
			
		Message msg2 = this.messageDao.findOne(msg.getId());
		assertEquals(MessageState.UNREAD, msg2.getState());
		
	}
	

	@Test
	public void validateEmailWontWorkWithNotAnEmail() throws Exception {
		this.login();
		
		this.mockMvc.perform(
				post("/profile/messages/validateEmail")	
				.param("email", "bluppbliblah")
				)
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().string(containsString("This user does not exist")));
		
	}
	

	@Test
	public void validateEmailWillWorkWithAnValidEmail() throws Exception {
		this.login();
		
		this.mockMvc.perform(
				post("/profile/messages/validateEmail")	
				.param("email", "ese@unibe.ch")
				)
			.andExpect(status().is2xxSuccessful())
			.andExpect(content().string(containsString("ese@unibe.ch")));
		
	}
	

	@Test
	public void sendThroughSendMethod() throws Exception {
		this.login();
		User jane = userDao.findByUsername("jane@doe.com");
		int total = 0;
		int count = 0;
		for (Message msg : this.messageDao.findByRecipient(jane)) {
			count++;
		}
		total = count;
		this.mockMvc.perform(
				post("/profile/messages/sendMessage")
				.param("recipientEmail", jane.getUsername())
				.param("subject", "blupp")
				.param("text", "testtext")
				).andExpect(status().is2xxSuccessful());
		
		count = 0;
		for (Message msg : this.messageDao.findByRecipient(jane)) {
			count++;
		}
		assertEquals(total+1, count);
	}
	
	private Message createNewMessage(User sender, User receiver) {
		Message msg = new Message();
		msg.setDateSent(new Date());
		msg.setRecipient(receiver);
		msg.setSender(sender);
		msg.setState(MessageState.UNREAD);
		msg.setSubject("Test");
		msg.setText("Test");
		
		messageDao.save(msg);
		return msg;
	}

}