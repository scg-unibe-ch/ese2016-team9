package ch.unibe.ese.team1.task;

import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ch.unibe.ese.team1.controller.service.AdService;
import ch.unibe.ese.team1.controller.service.AlertService;
import ch.unibe.ese.team1.controller.service.GMailerService;
import ch.unibe.ese.team1.controller.service.MessageService;
import ch.unibe.ese.team1.controller.service.UserService;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.Message;
import ch.unibe.ese.team1.model.User;

@Component
public class MessageScheduledTask {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private GMailerService mailerService;
	
    private static final Logger log = LoggerFactory.getLogger(MessageScheduledTask.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(cron="*/5 * * * * MON-FRI")
    public void processMessagesOnHold() {
        log.info("Started messagesOnHold processing at {}", dateFormat.format(new Date()));
        Iterable<User> users = this.userService.findUsersWhichHaveUnsentMessages();
    	
        for (User user : users) {
        	Iterable<Message> msgs = this.messageService.getMessagesToBeSent(user);
        	StringWriter outputWriter = new StringWriter();
        	int count = 0;
        	outputWriter.append("<ul>");
        	for (Message msg : msgs) {
        		count++;
            	outputWriter.append("<li>" + msg.getSubject() + "</li>");
            	this.messageService.setSendAsMail(msg);
        			
        	}
        	outputWriter.append("</ul>");
        	
        	String msg = "<h1>Daily Update</h1><br />Dear " + user.getFirstName() 
        		+ "<br /><br />"
        		+ "You have " + count + " new messages on the a-Bec platform!"
        		+ outputWriter.toString()
        		+ "<br />Have a nice day!<br /><br />You're a-Bec Team";
        	
        	try {
        		log.info("try to send an email to " + user.getEmail() + " {}", dateFormat.format(new Date()));
                this.mailerService.send(user.getEmail(), "Daily update", msg);
                
        	} catch(MessagingException e) {
        		log.error("failure to send: " + e.getMessage());
        	}
        }	

        log.info("Ending messagesOnHold processing at {}", dateFormat.format(new Date()));
    }
}