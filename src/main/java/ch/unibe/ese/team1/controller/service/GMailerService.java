package ch.unibe.ese.team1.controller.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Provides a simple access to the javamail api for sending mails
 */
@Service
public class GMailerService {

    private static final Logger log = LoggerFactory.getLogger(GMailerService.class);
    
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	private Session session;
	
	private String from;
	private String username;
	private String password;
	
	/**
	 * Generate a mailerservice object
	 * 
	 * @param smtpServer
	 * @param smtpPort
	 * @param username
	 * @param password
	 */
	public GMailerService() {
		super();
		this.from = "eseteam9@gmail.com";
		this.username = "eseteam9@gmail.com"; 
		this.password = "f-9Biv8}$rN)EW?q8P";
		
		Properties props = new Properties();

		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");

		session = Session.getInstance(props, null);
		this.session.setDebug(false);

	}
	
	/**
	 * send a message with this mailerservice
	 * 
	 * @param to
	 * @param subject
	 * @param message
	 * @throws MessagingException 
	 */
	public void send(String to, String subject, String message) throws MessagingException {
		this.log.info("Sending Mail with subject " + subject);
		try {
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(this.from));
			InternetAddress[] address = { new InternetAddress(to) };
			msg.setRecipients(Message.RecipientType.TO, address);
			msg.setSubject("New message on a-Bec: " + subject);
			msg.setSentDate(new Date());
	
			MimeBodyPart mbp = new MimeBodyPart();
			mbp.setText(message);
			
			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbp);
	
			msg.setContent(mp);
			
			Transport transport = this.session.getTransport("smtp");
			 
			transport.connect("smtp.gmail.com", this.username, this.password);
			transport.sendMessage(msg, msg.getAllRecipients());
			transport.close();
			
		} catch (MessagingException e) {
			log.error("Failed to send message at {}", dateFormat.format(new Date()));
			log.error("Message " + e.getMessage());
	        throw e;
		}
	}
}
