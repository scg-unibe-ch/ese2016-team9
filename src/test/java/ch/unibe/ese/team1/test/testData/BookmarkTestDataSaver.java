package ch.unibe.ese.team1.test.testData;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team1.controller.service.AdService;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.dao.UserDao;

/**
 * This inserts some bookmark test data into the database.
 */
@Service
public class BookmarkTestDataSaver{

	@Autowired
	private UserDao userDao;
	@Autowired
	private AdService adService;

	@Transactional
	public void saveTestData() throws Exception {
		User ese = userDao.findByUsername("ese@unibe.ch");
		User jane = userDao.findByUsername("jane@doe.com");
		User bernerBaer = userDao.findByUsername("user@bern.com");
		User oprah = userDao.findByUsername("oprah@winfrey.com");

		// 5 bookmarks for Ese test-user
		Set<Ad> bookmarkedAds = new HashSet<>();
		bookmarkedAds.add(adService.getAdById(23));
		bookmarkedAds.add(adService.getAdById(31));
		bookmarkedAds.add(adService.getAdById(39));
		bookmarkedAds.add(adService.getAdById(65));
		bookmarkedAds.add(adService.getAdById(19));
		ese.setBookmarkedAds(bookmarkedAds);
		
		userDao.save(ese);

		// 4 bookmarks for Jane Doe
		bookmarkedAds = new HashSet<>();
		bookmarkedAds.add(adService.getAdById(39));
		bookmarkedAds.add(adService.getAdById(43));
		bookmarkedAds.add(adService.getAdById(47));
		bookmarkedAds.add(adService.getAdById(39));
		jane.setBookmarkedAds(bookmarkedAds);
		userDao.save(jane);

		// 5 bookmarks for user berner bear
		bookmarkedAds = new HashSet<>();
		bookmarkedAds.add(adService.getAdById(61));
		bookmarkedAds.add(adService.getAdById(57));
		bookmarkedAds.add(adService.getAdById(51));
		bookmarkedAds.add(adService.getAdById(31));
		bookmarkedAds.add(adService.getAdById(23));
		bernerBaer.setBookmarkedAds(bookmarkedAds);
		userDao.save(bernerBaer);

		// 4 bookmarks for Oprah
		bookmarkedAds = new HashSet<>();
		bookmarkedAds.add(adService.getAdById(19));
		bookmarkedAds.add(adService.getAdById(31));
		bookmarkedAds.add(adService.getAdById(61));
		bookmarkedAds.add(adService.getAdById(43));
		oprah.setBookmarkedAds(bookmarkedAds);
		userDao.save(oprah);
	}

}
