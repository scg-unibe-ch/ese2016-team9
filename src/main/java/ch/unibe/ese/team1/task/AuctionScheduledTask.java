package ch.unibe.ese.team1.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import ch.unibe.ese.team1.controller.service.AdService;
import ch.unibe.ese.team1.controller.service.AlertService;
import ch.unibe.ese.team1.model.Ad;

@Component
public class AuctionScheduledTask {

	@Autowired
	private AdService adService;
	
	@Autowired
	private AlertService alertService;
	
    private static final Logger log = LoggerFactory.getLogger(AuctionScheduledTask.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void processFinishedAuctions() {
        log.info("Started auction processing at {}", dateFormat.format(new Date()));
        Iterable<Ad> ads = this.adService.getAuctionsWhichShouldBeProcessed();
        
        for (Ad ad : ads) {
        	this.alertService.triggerAuction(ad);
        	this.adService.markProcessed(ad);
        }	

        log.info("Finished auction processing at {}", dateFormat.format(new Date()));
    }
}