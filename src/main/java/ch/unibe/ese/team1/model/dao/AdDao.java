package ch.unibe.ese.team1.model.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.User;

public interface AdDao extends CrudRepository<Ad, Long> {
	
	/** this will be used if both houses AND flats are searched */
	public Iterable<Ad> findByPriceLessThan (double price);

	/** this will be used if only houses or flats are searched */
	public Iterable<Ad> findByFlatAndPriceLessThan(boolean flat, double i);

	public Iterable<Ad> findByUser(User user);
	
	@Query("select ad from Ad ad where ad.price + ad.runningCosts < ?1")
	public Iterable<Ad> findByPriceIncludingRunningCostsLessThan (double price);
	
	@Query("select ad from Ad ad where ad.flat=?1 and ad.price + ad.runningCosts < ?2")
	public Iterable<Ad> findByFlatAndPriceIncludingRunningCostsLessThan(boolean flat, int i);
	
	public Iterable<Ad> findByAuctionStartingPriceNotNull();
	
	@Query("select ad from Ad ad where ad.isOnHomepage=true order by ad.wasOnHomepage")
	public Iterable<Ad> findByIsOnHomepageTrueOrderByWasOnHomepage();
	
}