package ch.unibe.ese.team1.controller.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.lang.Math;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team1.controller.pojos.forms.AlertForm;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.Alert;
import ch.unibe.ese.team1.model.Bet;
import ch.unibe.ese.team1.model.Location;
import ch.unibe.ese.team1.model.Message;
import ch.unibe.ese.team1.model.MessageState;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.dao.AlertDao;
import ch.unibe.ese.team1.model.dao.MessageDao;
import ch.unibe.ese.team1.model.dao.UserDao;

/**
 * Provides and handles persistence operations for adding, editing and deleting
 * alerts.
 */
@Service
public class AlertService {

	@Autowired
	UserDao userDao;

	@Autowired
	AlertDao alertDao;

	@Autowired
	MessageDao messageDao;

	@Autowired
	private GeoDataService geoDataService;

	/**
	 * Persists a new alert with the data from the alert form to the database.
	 * 
	 * @param alertForm
	 *            the form to take the data from
	 * @param user
	 *            the user to associate the new alert to
	 */
	@Transactional
	public void saveFrom(AlertForm alertForm, User user) {
		Alert alert = new Alert();

		String zip = alertForm.getCity().substring(0, 4);
		alert.setZipcode(Integer.parseInt(zip));
		alert.setCity(alertForm.getCity().substring(7));

		alert.setPrice(alertForm.getPrice());
		alert.setRadius(alertForm.getRadius());
		alert.setNumberOfRooms(alertForm.getNumberOfRooms());
		alert.setSquareFootage(alertForm.getSquareFootage());
		alert.setHouse(alertForm.getHouse());
		alert.setFlat(alertForm.getFlat());
		alert.setBothHouseAndFlat(alertForm.getBothHouseAndFlat());
		alert.setUser(user);
		alertDao.save(alert);
	}

	/**
	 * Returns all alerts that belong to the given user.
	 */
	@Transactional
	public Iterable<Alert> getAlertsByUser(User user) {
		return alertDao.findByUser(user);
	}

	/** Deletes the alert with the given id. */
	@Transactional
	public void deleteAlert(Long id) {
		alertDao.delete(id);
	}

	/** Triggers all alerts when someone bets to an ad */
	@Transactional
	public void triggerAlerts(Bet bet) {
		assert (bet.getAd() != null);
		
		Set<Bet> bets = bet.getAd().getBets();
		boolean chk = false;
		User notifiedUser = null;
		Bet notifiedBet = null;
		for (Bet localBet : bets) {
			if (chk) {
				continue;
			}
			if (!localBet.equals(bet)) {
				chk = true;
				notifiedUser = localBet.getUser();
				notifiedBet = localBet;
			}
		}

		if (notifiedUser != null && notifiedBet != null) {
			Date now = new Date();
			Message message = new Message();
			message.setSubject(":O");
			message.setText(getAlertText(notifiedBet));
			message.setSender(userDao.findByUsername("System"));
			message.setRecipient(notifiedUser);
			message.setState(MessageState.UNREAD);
			message.setDateSent(now);
			messageDao.save(message);
		}
		
	}
	
	/**
	 * Triggers all alerts that match the given ad. For every user, only one
	 * message is sent.
	 */
	@Transactional
	public void triggerAlerts(Ad ad) {
		int adPrice = ad.getPrize();
		Iterable<Alert> alerts = alertDao.findByPriceGreaterThan(adPrice - 1);

		// loop through all ads with matching city and price range, throw out
		// mismatches
		Iterator<Alert> alertIterator = alerts.iterator();
		while (alertIterator.hasNext()) {
			Alert alert = alertIterator.next();
			if (numberOfRoomsMismatchWith(ad, alert) || squareFootageMismatchWith(ad, alert) 
					|| typeMismatchWith(ad, alert) || radiusMismatchWith(ad, alert)
					|| ad.getUser().equals(alert.getUser()))
				alertIterator.remove();
		}

		// send only one message per user, no matter how many alerts were
		// triggered
		List<User> users = new ArrayList<User>();
		for (Alert alert : alerts) {
			User user = alert.getUser();
			if (!users.contains(user)) {
				users.add(user);
			}
		}

		// send messages to all users with matching alerts
		for (User user : users) {
			Date now = new Date();
			Message message = new Message();
			message.setSubject("It's a match!");
			message.setText(getAlertText(ad));
			message.setSender(userDao.findByUsername("System"));
			message.setRecipient(user);
			message.setState(MessageState.UNREAD);
			message.setDateSent(now);
			messageDao.save(message);
		}
	}

	private boolean numberOfRoomsMismatchWith(Ad ad, Alert alert) {
		boolean mismatch = false;
		if (alert.getNumberOfRooms() > ad.getNumberOfRooms())
			mismatch = true;
		return mismatch;
	}

	private boolean squareFootageMismatchWith(Ad ad, Alert alert) {
		boolean mismatch = false;
		if (alert.getSquareFootage() > ad.getSquareFootage())
			mismatch = true;
		return mismatch;
	}

	/**
	 * Returns the text for an alert message with the properties of the given
	 * ad.
	 */
	private String getAlertText(Ad ad) {
		return "Dear user,<br>good news. A new ad matching one of your alerts has been "
				+ "entered into our system. You can visit it here:<br><br>"
				+ "<a class=\"link\" href=/ad?id="
				+ ad.getId()
				+ ">"
				+ ad.getTitle()
				+ "</a><br><br>"
				+ "Good luck and enjoy,<br>"
				+ "Your a-Bec crew";
	}

	/**
	 * Returns the text for an alert message with the properties of the given
	 * bet.
	 */
	private String getAlertText(Bet bet) {
		return "Dear user,<br>You have been overbidden by " + bet.getUser().getUsername() + "<br>" 
				+ "<a class=\"link\" href=/ad?id="
				+ bet.getAd().getId()
				+ ">"
				+ bet.getAd().getTitle()
				+ "</a><br><br>"
				+ "Your a-Bec crew";
	}
	
	/** Checks if an ad is conforming to the criteria in an alert. */
	private boolean typeMismatchWith(Ad ad, Alert alert) {
		boolean mismatch = false;
		if (!alert.getBothHouseAndFlat()
				&& ad.getFlat() != alert.getFlat())
			mismatch = true;
		return mismatch;
	}

	/**
	 * Checks whether an ad is for a place too far away from the alert.
	 * 
	 * @param ad
	 *            the ad to compare to the alert
	 * @param alert
	 *            the alert to compare to the ad
	 * 
	 * @return true in case the alert does not match the ad (the ad is too far
	 *         away), false otherwise
	 */
	private boolean radiusMismatchWith(Ad ad, Alert alert) {
		if (geoDataService.getLocationsByCity(ad.getCity()).size() == 0) {
			return true;
		}
		final int earthRadiusKm = 6380;
		
		Location adLocation = geoDataService.getLocationsByCity(ad.getCity())
				.get(0);
		Location alertLocation = geoDataService.getLocationsByCity(
				alert.getCity()).get(0);

		double radSinLat = Math.sin(Math.toRadians(adLocation.getLatitude()));
		double radCosLat = Math.cos(Math.toRadians(adLocation.getLatitude()));
		double radLong = Math.toRadians(adLocation.getLongitude());
		double radLongitude = Math.toRadians(alertLocation.getLongitude());
		double radLatitude = Math.toRadians(alertLocation.getLatitude());
		double distance = Math.acos(radSinLat * Math.sin(radLatitude)
				+ radCosLat * Math.cos(radLatitude)
				* Math.cos(radLong - radLongitude))
				* earthRadiusKm;
		return (distance > alert.getRadius());
	}
	
	//for testing
	public boolean radiusMismatch(Ad ad, Alert alert) {
		return radiusMismatchWith(ad, alert);
	}
	
	//for testing
	public boolean typeMismatch(Ad ad, Alert alert) {
		return typeMismatchWith(ad, alert);
	}
}