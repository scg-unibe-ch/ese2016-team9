package ch.unibe.ese.team1.model.dao;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.Bet;

public interface BetDao extends CrudRepository<Bet, Long> {
	//public Iterable<Bet> findByAd(Ad ad);
}