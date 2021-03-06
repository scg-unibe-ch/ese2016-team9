package ch.unibe.ese.team1.controller.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ch.unibe.ese.team1.controller.pojos.Location;
import ch.unibe.ese.team1.controller.pojos.forms.PlaceAdForm;
import ch.unibe.ese.team1.controller.pojos.forms.SearchForm;
import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.AdPicture;
import ch.unibe.ese.team1.model.Bet;
import ch.unibe.ese.team1.model.User;
import ch.unibe.ese.team1.model.Visit;
import ch.unibe.ese.team1.model.dao.AdDao;
import ch.unibe.ese.team1.model.dao.AlertDao;
import ch.unibe.ese.team1.model.dao.BetDao;
import ch.unibe.ese.team1.model.dao.MessageDao;
import ch.unibe.ese.team1.model.dao.UserDao;

/** Handles all persistence operations concerning ad placement and retrieval. */
@Service
public class AdService {

	@Autowired
	private AdDao adDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private BetDao betDao;

	@Autowired
	private AlertDao alertDao;

	@Autowired
	private MessageDao messageDao;

	@Autowired
	private UserService userService;

	@Autowired
	private GeoDataService geoDataService;

	/**
	 * Handles persisting a new ad to the database.
	 * 
	 * @param placeAdForm
	 *            the form to take the data from
	 * @param a
	 *            list of the file paths the pictures are saved under
	 * @param the
	 *            currently logged in user
	 */
	@Transactional
	public Ad saveFrom(PlaceAdForm placeAdForm, List<String> filePaths,
			User user) {
		
		Ad ad = new Ad();

		Date now = new Date();
		ad.setCreationDate(now);

		ad.setTitle(placeAdForm.getTitle());

		ad.setStreet(placeAdForm.getStreet());

		ad.setFlat(placeAdForm.getFlat());

		// take the zipcode - first four digits
		String zip = placeAdForm.getCity().substring(0, 4);
		ad.setZipcode(Integer.parseInt(zip));
		ad.setCity(placeAdForm.getCity().substring(7));
		
		Calendar calendar = Calendar.getInstance();
		// java.util.Calendar uses a month range of 0-11 instead of the
		// XMLGregorianCalendar which uses 1-12
		try {
			if (placeAdForm.getMoveInDate() != null && placeAdForm.getMoveInDate().length() >= 1) {
				int dayMoveIn = Integer.parseInt(placeAdForm.getMoveInDate()
						.substring(0, 2));
				int monthMoveIn = Integer.parseInt(placeAdForm.getMoveInDate()
						.substring(3, 5));
				int yearMoveIn = Integer.parseInt(placeAdForm.getMoveInDate()
						.substring(6, 10));
				calendar.set(yearMoveIn, monthMoveIn - 1, dayMoveIn);
				ad.setMoveInDate(calendar.getTime());
			}

			if (placeAdForm.getLastRenovation() != null && placeAdForm.getLastRenovation().length() >= 1) {
				int dayRenovate = Integer.parseInt(placeAdForm.getLastRenovation()
						.substring(0, 2));
				int monthRenovate = Integer.parseInt(placeAdForm.getLastRenovation()
						.substring(3, 5));
				int yearRenovate = Integer.parseInt(placeAdForm.getLastRenovation()
						.substring(6, 10));
				calendar.set(yearRenovate, monthRenovate - 1, dayRenovate);
				ad.setLastRenovation(calendar.getTime());
			}
		} catch (NumberFormatException e) {
		}

		ad.setPrice(placeAdForm.getPrice());
		ad.setSquareFootage(placeAdForm.getSquareFootage());
		ad.setRunningCosts(placeAdForm.getRunningCosts());
		
		ad.setHouseDescription(placeAdForm.getHouseDescription());

		// ad description values
		ad.setSmokers(placeAdForm.isSmokers());
		ad.setAnimals(placeAdForm.isAnimals());
		ad.setGarden(placeAdForm.getGarden());
		ad.setBalcony(placeAdForm.getBalcony());
		ad.setCellar(placeAdForm.getCellar());
		ad.setFurnished(placeAdForm.isFurnished());
		ad.setCable(placeAdForm.getCable());
		ad.setGarage(placeAdForm.getGarage());
		
		// distance values
		ad.setFloor(placeAdForm.getFloor());
		ad.setNumberOfRooms(placeAdForm.getNumberOfRooms());
		ad.setDistanceToNearestPublicTransport(placeAdForm.getDistanceToNearestPublicTransport());
		ad.setDistanceToNearestSchool(placeAdForm.getDistanceToNearestSchool());
		ad.setDistanceToNearestSuperMarket(placeAdForm.getDistanceToNearestSuperMarket());
		
		/*
		 * Save the paths to the picture files, the pictures are assumed to be
		 * uploaded at this point!
		 */
		List<AdPicture> pictures = new ArrayList<>();
		for (String filePath : filePaths) {
			AdPicture picture = new AdPicture();
			picture.setFilePath(filePath);
			pictures.add(picture);
		}
		ad.setPictures(pictures);


		// visits
		Set<Visit> visits = new HashSet<>();
		List<String> visitStrings = placeAdForm.getVisits();
		if (visitStrings != null) {
			for (String visitString : visitStrings) {
				Visit visit = new Visit();
				// format is 28-02-2014;10:02;13:14
				DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
				String[] parts = visitString.split(";");
				String startTime = parts[0] + " " + parts[1];
				String endTime = parts[0] + " " + parts[2];
				Date startDate = null;
				Date endDate = null;
				try {
					startDate = dateFormat.parse(startTime);
					endDate = dateFormat.parse(endTime);
				} catch (ParseException ex) {
					ex.printStackTrace();
				}

				visit.setStartTimestamp(startDate);
				visit.setEndTimestamp(endDate);
				visit.setAd(ad);
				visits.add(visit);
			}
			ad.setVisits(visits);
		}

		// Save auction information
		if (placeAdForm.getAuctionEndingDate() != null && 
				placeAdForm.getAuctionEndingDate().length() >= 1) {
			int dayEndAuction = Integer.parseInt(placeAdForm.getAuctionEndingDate()
					.substring(0, 2));
			int monthEndAuction = Integer.parseInt(placeAdForm.getAuctionEndingDate()
					.substring(3, 5));
			int yearEndAuction = Integer.parseInt(placeAdForm.getAuctionEndingDate()
					.substring(6, 10));
			
			
			calendar.set(
					yearEndAuction, 
					monthEndAuction - 1, 
					dayEndAuction, 
					placeAdForm.getAuctionEndingHour(), 
					placeAdForm.getAuctionEndingMinute()
			);
			ad.setAuctionEndingDate(calendar.getTime());
			ad.setAuctionStartingPrice(placeAdForm.getAuctionStartingPrice());
			if (placeAdForm.getAuctionStepPrice() != 0.0) {
				ad.setAuctionStepPrice(placeAdForm.getAuctionStepPrice());
			}		
		}
		
		
		ad.setUser(user);
		
		adDao.save(ad);

		return ad;
	}

	/**
	 * Gets the ad that has the given id.
	 * 
	 * @param id
	 *            the id that should be searched for
	 * @return the found ad or null, if no ad with this id exists
	 */
	@Transactional
	public Ad getAdById(long id) {
		return adDao.findOne(id);
	}

	/** Returns all ads in the database */
	@Transactional
	public Iterable<Ad> getAllAds() {
		return adDao.findAll();
	}

	/**
	 * Returns the newest ads in the database. Parameter 'newest' says how many.
	 */
	@Transactional
	public Iterable<Ad> getNewestAds(int newest) {
		Iterable<Ad> allAds = adDao.findAll();
		List<Ad> ads = new ArrayList<Ad>();
		for (Ad ad : allAds)
			ads.add(ad);
		Collections.sort(ads, new Comparator<Ad>() {
			@Override
			public int compare(Ad ad1, Ad ad2) {
				return ad2.getCreationDate().compareTo(ad1.getCreationDate());
			}
		});
		List<Ad> fourNewest = new ArrayList<Ad>();
		for (int i = 0; i < newest; i++)
			fourNewest.add(ads.get(i));
		return fourNewest;
	}

	/**
	 * Returns all ads that match the parameters given by the form. This list
	 * can possibly be empty.
	 * 
	 * @param searchForm
	 *            the form to take the search parameters from
	 * @return an Iterable of all search results
	 */
	@Transactional
	public Iterable<Ad> queryResults(SearchForm searchForm) {
		Iterable<Ad> results = null;
		
		// we use this method if we are looking for houses AND flats
		if (searchForm.getBothHouseAndFlat()) {
			results = adDao
					.findByPriceLessThan(searchForm.getPrice() + 1);
		}
		// we use this method if we are looking EITHER for houses OR for flats
		else {
			results = adDao.findByFlatAndPriceLessThan(
					searchForm.getFlat(), searchForm.getPrice() + 1);
			
		}
		
		//removes finished auctions 
		Iterator<Ad> auctionIterator = results.iterator();
		while(auctionIterator.hasNext()) {
			Ad ad = auctionIterator.next();
			if(ad.isAuction() && ad.isAuctionEnded())
				auctionIterator.remove();
		}
		
		//includes the running costs. 
		if(searchForm.getIncludeRunningCosts()){
			Iterator<Ad> iterator = results.iterator();
			while(iterator.hasNext()) {
				Ad ad = iterator.next();
				if(ad.getPrice()+ad.getRunningCosts() > searchForm.getPrice())
					iterator.remove();
			}
		}
		
		//filters between rent and buy if needed
		Iterator<Ad> iteratorRent = results.iterator();
		while(iteratorRent.hasNext()){
			Ad ad = iteratorRent.next();
			if(ad.getForSale() != searchForm.getForSale())
				iteratorRent.remove();
		}

		// filter out zipcode
		String city = searchForm.getCity().substring(7);

		// get the location that the user searched for and take the one with the
		// lowest zip code
		Location searchedLocation = geoDataService.getLocationsByCity(city)
				.get(0);
		
		//set latitude and longitude for map
		searchForm.setLongitude(searchedLocation.getLongitude());
		searchForm.setLatitude(searchedLocation.getLatitude());

		// create a list of the results and of their locations
		List<Ad> locatedResults = new ArrayList<>();
		for (Ad ad : results) {
			locatedResults.add(ad);
		}

		final int earthRadiusKm = 6380;
		List<Location> locations = geoDataService.getAllLocations();
		double radSinLat = Math.sin(Math.toRadians(searchedLocation
				.getLatitude()));
		double radCosLat = Math.cos(Math.toRadians(searchedLocation
				.getLatitude()));
		double radLong = Math.toRadians(searchedLocation.getLongitude());

		/*
		 * calculate the distances (Java 8) and collect all matching zipcodes.
		 * The distance is calculated using the law of cosines.
		 * http://www.movable-type.co.uk/scripts/latlong.html
		 */
		List<Integer> zipcodes = locations
				.parallelStream()
				.filter(location -> {
					double radLongitude = Math.toRadians(location
							.getLongitude());
					double radLatitude = Math.toRadians(location.getLatitude());
					double distance = Math.acos(radSinLat
							* Math.sin(radLatitude) + radCosLat
							* Math.cos(radLatitude)
							* Math.cos(radLong - radLongitude))
							* earthRadiusKm;
					return distance < searchForm.getRadius();
				}).map(location -> location.getZip())
				.collect(Collectors.toList());

		locatedResults = locatedResults.stream()
				.filter(ad -> zipcodes.contains(ad.getZipcode()))
				.collect(Collectors.toList());

		// filter for additional criteria
		if (searchForm.getFiltered()) {
			// prepare date filtering - by far the most difficult filter
			Date earliestInDate = null;
			Date latestInDate = null;
			Date earliestOutDate = null;
			Date latestOutDate = null;
			Date lastRenovationDate = null;

			// parse move-in and move-out dates
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			try {
				earliestInDate = formatter.parse(searchForm
						.getEarliestMoveInDate());
			} catch (Exception e) {
			}
			try {
				latestInDate = formatter
						.parse(searchForm.getLatestMoveInDate());
			} catch (Exception e) {
			}
			try {
				lastRenovationDate = formatter.parse(searchForm.getLastRenovation());
			} catch (Exception e) {
				
			}

			// filtering by dates
			locatedResults = validateDate(locatedResults, earliestInDate,
					latestInDate, lastRenovationDate);

			// filtering for the rest
			// smokers
			if (searchForm.getSmokers()) {
				Iterator<Ad> iterator = locatedResults.iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if (!ad.getSmokers())
						iterator.remove();
				}
			}

			// animals
			if (searchForm.getAnimals()) {
				Iterator<Ad> iterator = locatedResults.iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if (!ad.getAnimals())
						iterator.remove();
				}
			}

			// garden
			if (searchForm.getGarden()) {
				Iterator<Ad> iterator = locatedResults.iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if (!ad.getGarden())
						iterator.remove();
				}
			}

			// balcony
			if (searchForm.getBalcony()) {
				Iterator<Ad> iterator = locatedResults.iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if (!ad.getBalcony())
						iterator.remove();
				}
			}

			// cellar
			if (searchForm.getCellar()) {
				Iterator<Ad> iterator = locatedResults.iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if (!ad.getCellar())
						iterator.remove();
				}
			}

			// furnished
			if (searchForm.getFurnished()) {
				Iterator<Ad> iterator = locatedResults.iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if (!ad.getFurnished())
						iterator.remove();
				}
			}

			// cable
			if (searchForm.getCable()) {
				Iterator<Ad> iterator = locatedResults.iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if (!ad.getCable())
						iterator.remove();
				}
			}

			// garage
			if (searchForm.getGarage()) {
				Iterator<Ad> iterator = locatedResults.iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if (!ad.getGarage())
						iterator.remove();
				}
			}

			// groundFloor
			if (searchForm.getGroundFloor()) {
				Iterator<Ad> iterator = locatedResults.iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if (ad.getFloor() >= 1)
						iterator.remove();
				}
			}
			
			//number of rooms
			if(searchForm.getNumberOfRooms()>0){
				Iterator<Ad> iterator = locatedResults.iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if(ad.getNumberOfRooms() < searchForm.getNumberOfRooms())
						iterator.remove();
				}
			}
			
			// distances. Keep in mind that 5100 is per default the biggest distance. It means 5km or more.
			if (searchForm.getDistanceToNearestPublicTransport() < 5100) {
				Iterator<Ad> iterator = locatedResults.iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if (ad.getDistanceToNearestPublicTransport() > searchForm.getDistanceToNearestPublicTransport())
							iterator.remove();
				}
			}
			
			if (searchForm.getDistanceToNearestSchool() <5100) {
				Iterator<Ad> iterator = locatedResults.iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if (ad.getDistanceToNearestSchool() > searchForm.getDistanceToNearestSchool())
							iterator.remove();
				}
			}
			
			if (searchForm.getDistanceToNearestSuperMarket() < 5100) {
				Iterator<Ad> iterator = locatedResults.iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if (ad.getDistanceToNearestSuperMarket() > searchForm.getDistanceToNearestSuperMarket())
						iterator.remove();
				}
			}
			
			
			// square footage
			if (searchForm.getSquareFootage() > 0) {
				Iterator<Ad> iterator = locatedResults.iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if (ad.getSquareFootage() < searchForm.getSquareFootage())
						iterator.remove();
				}
			}
			
		}
		return locatedResults;
	}

	private List<Ad> validateDate(List<Ad> ads, Date earliestDate, Date latestDate, Date latestRenovation) {
		if (ads.size() > 0) {
			// Move-in dates
			// Both an earliest AND a latest date to compare to
			if (earliestDate != null) {
				if (latestDate != null) {
					Iterator<Ad> iterator = ads.iterator();
					while (iterator.hasNext()) {
						Ad ad = iterator.next();
						if (ad.getMoveInDate().compareTo(earliestDate) < 0
								|| ad.getMoveInDate().compareTo(latestDate) > 0) {
							iterator.remove();
						}
					}
				}
				// only an earliest date
				else {
					Iterator<Ad> iterator = ads.iterator();
					while (iterator.hasNext()) {
						Ad ad = iterator.next();
						if (ad.getMoveInDate().compareTo(earliestDate) < 0)
							iterator.remove();
					}
				}
			}
			// only a latest date
			else if (latestDate != null && earliestDate == null) {
				Iterator<Ad> iterator = ads.iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if (ad.getMoveInDate().compareTo(latestDate) > 0)
						iterator.remove();
				}
			} 
			// a latest renovation date
			else if (latestRenovation != null) {
				Iterator<Ad> iterator = ads.iterator();
				while (iterator.hasNext()) {
					Ad ad = iterator.next();
					if (ad.getLastRenovation().compareTo(latestRenovation) > 0)
						iterator.remove();
				}
			}
			else {
			}
		}
		return ads;
	}

	/** Returns all ads that were placed by the given user. */
	public Iterable<Ad> getAdsByUser(User user) {
		return adDao.findByUser(user);
	}

	/**
	 * Checks if the email of a user is already contained in the given string.
	 * 
	 * @param email
	 *            the email string to search for
	 * @param alreadyAdded
	 *            the string of already added emails, which should be searched
	 *            in
	 * 
	 * @return true if the email has been added already, false otherwise
	 */
	public Boolean checkIfAlreadyAdded(String email, String alreadyAdded) {
		email = email.toLowerCase();
		alreadyAdded = alreadyAdded.replaceAll("\\s+", "").toLowerCase();
		String delimiter = "[:;]+";
		String[] toBeTested = alreadyAdded.split(delimiter);
		for (int i = 0; i < toBeTested.length; i++) {
			if (email.equals(toBeTested[i])) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * set an ad to the homepage
	 * @param ad
	 * @param value
	 */
	public void placeOnHomepage(Ad ad, boolean value) {
		ad.setOnHomepage(value);
		this.adDao.save(ad);
	}
	

	/**
	 * Returns ads which are bought to the homepage
	 * If it has more than the parameter, it will return randomly other ads
	 * 
	 * @param count
	 */
	@Transactional
	public Iterable<Ad> getHomepageAds(int count) {
		Iterable<Ad> homepageAds = adDao.findByIsOnHomepageTrueOrderByWasOnHomepage();
		ArrayList<Ad> ads = new ArrayList<Ad>();
		
		for (Ad homepageAd : homepageAds) {
			homepageAd.wasOnHomepage();
			this.adDao.save(homepageAd);
			if(!homepageAd.isAuction() || (homepageAd.isAuction() && !homepageAd.isAuctionEnded()))
				ads.add(homepageAd);
		}
		
		if (ads.size() < count) {
			Iterable<Ad> lowAds = adDao.findAll();
			ArrayList<Ad> listLowAds = new ArrayList<Ad>();
			for (Ad lowAd : lowAds) {
				if (lowAd.isOnHomepage() || (lowAd.isAuction() && lowAd.isAuctionEnded())) {
					continue;
				}
				listLowAds.add(lowAd);
			}
			Collections.shuffle(listLowAds);
			
			for(Ad lowAdShuffled : listLowAds) {
				if (ads.size() < count) {
					ads.add(lowAdShuffled);
				}
			}
		}
		
		return ads;
	}
	
	/**
	 * Returns ads which a user has bets on
	 * 
	 * @param User user
	 */
	@Transactional
	public Iterable<Ad> getAdsByBetsForUser(User user) {
		Iterable<Bet> bets = this.betDao.findByUser(user);
		HashMap<Ad, Integer> ads = new HashMap<Ad, Integer>();
		for(Bet bet : bets) {
			ads.put(bet.getAd(), (int) bet.getAd().getId());
		}
		
		return ads.keySet();
	}
	
	/**
	 * Returns ads which a user has bets on
	 * 
	 * @param User user
	 */
	@Transactional
	public Iterable<Ad> getAuctionsWhichShouldBeProcessed() {
		Iterable<Ad> ads = this.adDao.findAll();
		HashMap<Ad, Integer> returnAds = new HashMap<Ad, Integer>();

		for(Ad ad : ads) {
			if (ad.isAuction() && ad.isAuctionEnded() && !ad.isAuctionProcessed()) {
				returnAds.put(ad, (int) ad.getId());
			}
		}

		return returnAds.keySet();
	}
	
	/**
	 * Mark an auction as processed
	 * 
	 * @param Ad ad
	 */
	@Transactional
	public void markProcessed(Ad ad) {
		ad.setAuctionProcessed(true);
		this.adDao.save(ad);
	}

	@Transactional
	public void setUserRated(Ad ad) {
		ad.setUserRated(true);
		this.adDao.save(ad);
	}
	
	@Transactional
	public void setAdvertiserRated(Ad ad) {
		ad.setAdvertiserRated(true);
		this.adDao.save(ad);
	}
}