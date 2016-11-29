package ch.unibe.ese.team1.model.dao;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.team1.model.Bet;
import ch.unibe.ese.team1.model.User;

public interface BetDao extends CrudRepository<Bet, Long> {
	public Iterable<Bet> findByUser(User user);
}