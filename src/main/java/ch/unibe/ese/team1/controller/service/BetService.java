package ch.unibe.ese.team1.controller.service;

import java.util.Date;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team1.controller.pojos.forms.BetForm;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.Bet;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.dao.BetDao;

/** Handles all persistence operations concerning ad placement and retrieval. */
@Service
public class BetService {

	@Autowired
	private BetDao betDao;

	public static final int VALIDATE_OK = 0;
	public static final int VALIDATE_PRICE_TO_LOW = 1;
	public static final int VALIDATE_SAME_USER = 2;
	
	
	/**
	 * 
	 * @param betForm
	 *            the form to take the data from
	 * @param ad
	 * 			  the current ad of this bet
	 * @param user
	 * 			  the current user of this bet
	 * @return
	 */
	@Transactional
	public Bet saveFrom(BetForm betForm, Ad ad, User user) {
		Bet bet = new Bet();
		
		bet.setPrice(betForm.getPrice());
		bet.setCreationDate(new Date());
		bet.setUser(user);
		bet.setAd(ad);
		
		betDao.save(bet);

		return bet;
	}

	public int validateBet(BetForm betForm, Ad ad, User user) {
		Set<Bet> bets = ad.getBets();
		double maxBet = 0;
		for (Bet bet : bets) {
			if (maxBet < bet.getPrice()) {
				maxBet = bet.getPrice();
			}
		}
		if (betForm.getPrice() <= maxBet) {
			return BetService.VALIDATE_PRICE_TO_LOW;
		}
		
		return BetService.VALIDATE_OK;
	}
}