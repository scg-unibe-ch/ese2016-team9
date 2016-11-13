package ch.unibe.ese.team1.controller.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

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



	/**
	 * Handles persisting a new bet to the database.
	 * 
	 * @param BetForm
	 *            the form to take the data from
	 * @param User
	 * 			  the current user of this bet
	 */
	@Transactional
	public Bet saveFrom(BetForm betForm, User user) {
		Bet bet = new Bet();
		
		bet.setPrice(betForm.getPrice());
		bet.setCreationDate(new Date());
		bet.setAd(betForm.getAd());
		bet.setUser(user);
		
		betDao.save(bet);

		return bet;
	}
}