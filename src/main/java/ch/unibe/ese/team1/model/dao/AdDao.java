package ch.unibe.ese.team1.model.dao;

import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.User;

public interface AdDao extends CrudRepository<Ad, Long> {
	
	/** this will be used if both houses AND flats are searched */
	public Iterable<Ad> findByPrizePerMonthLessThan (int prize);

	/** this will be used if only houses or flats are searched */
	public Iterable<Ad> findByFlatAndPrizePerMonthLessThan(boolean flat,
			int i);

	public Iterable<Ad> findByUser(User user);
}