package ch.unibe.ese.team1.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.validator.constraints.Range;

/** Describes an advertisement that users can place and search for. */
@Entity
public class Ad {

	@Id
	@GeneratedValue
	private long id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String street;

	@Column(nullable = false)
	private int zipcode;

	@Column(nullable = false)
	private String city;
	
	@Column(nullable = false)
	@Temporal(TemporalType.DATE)
	private Date creationDate;

	@Temporal(TemporalType.DATE)
	private Date moveInDate;

	@Column(nullable = false)
	private double price;

	@Column(nullable = false)
	private double squareFootage;

	@Column(nullable = false)
	@Lob
	private String houseDescription;

	@Column(nullable = false)
	private boolean smokers;

	@Column(nullable = false)
	private boolean animals;

	@Column(nullable = false)
	private boolean garden;

	@Column(nullable = false)
	private boolean balcony;

	@Column(nullable = false)
	private boolean cellar;

	@Column(nullable = false)
	private boolean furnished;

	@Column(nullable = false)
	private boolean cable;

	@Column(nullable = false)
	private boolean garage;

	// true if flat, false if house
	@Column(nullable = false)
	private boolean flat;
	
	//true if for sale, false if for rent 
	@Column(nullable = false)
	private boolean forSale;

	@Column(nullable = false)
	private double numberOfRooms;
	
	@Column(nullable = false)
	private double runningCosts;
	
	@Column(nullable = false)
	@Range(min=-1, max=100)
	private int floor;
	
	@Column
	@Temporal(TemporalType.DATE)
	private Date lastRenovation;
	
	@Column(nullable = false)
	private int distanceToNearestSuperMarket;

	@Column(nullable = false)
	private int distanceToNearestPublicTransport;
	
	@Column(nullable = false)
	private int distanceToNearestSchool;
	
	@Column(nullable = false)
	private boolean isOnHomepage;

	@Fetch(FetchMode.SELECT)
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<AdPicture> pictures;

	@ManyToOne(optional = false)
	private User user;
	
	@OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Visit> visits;
	
	@OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@OrderBy("creationDate DESC")
	private Set<Bet> bets;

	@Column
	@Temporal(TemporalType.TIMESTAMP)
	private Date auctionEndingDate;
	
	@Column(nullable = false)
	private int wasOnHomepage = 0;
	
	@Column
	private double auctionStartingPrice;
	
	@Column
	private double auctionStepPrice;
	
	@Column(nullable = false)
	private boolean auctionProcessed;
	
	@Column(nullable = false)
	private boolean advertiserRated;
	
	@Column(nullable = false)
	private boolean userRated;
	
	public static final double DEFAULT_AUCTION_STEP_PRICE = 10;
	
	public Ad () {
		this.isOnHomepage = false;
		this.auctionProcessed = false;
		this.userRated = false;
		this.advertiserRated = false;
	}
	
	public Date getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public boolean getFlat() {
		return flat;
	}

	public boolean getForSale() {
		return forSale;
	}

	public void setForSale(boolean forSale) {
		this.forSale = forSale;
	}

	public void setFlat(boolean flat) {
		this.flat = flat;
	}

	public boolean getSmokers() {
		return smokers;
	}

	public void setSmokers(boolean allowsSmokers) {
		this.smokers = allowsSmokers;
	}

	public boolean getAnimals() {
		return animals;
	}

	public void setAnimals(boolean allowsAnimals) {
		this.animals = allowsAnimals;
	}

	public boolean getGarden() {
		return garden;
	}

	public void setGarden(boolean hasGarden) {
		this.garden = hasGarden;
	}

	public boolean getBalcony() {
		return balcony;
	}

	public void setBalcony(boolean hasBalcony) {
		this.balcony = hasBalcony;
	}

	public boolean getCellar() {
		return cellar;
	}

	public void setCellar(boolean hasCellar) {
		this.cellar = hasCellar;
	}

	public boolean getFurnished() {
		return furnished;
	}

	public void setFurnished(boolean furnished) {
		this.furnished = furnished;
	}

	public boolean getCable() {
		return cable;
	}

	public void setCable(boolean hasCable) {
		this.cable = hasCable;
	}

	public boolean getGarage() {
		return garage;
	}

	public void setGarage(boolean garage) {
		this.garage = garage;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getZipcode() {
		return zipcode;
	}

	public void setZipcode(int zipcode) {
		this.zipcode = zipcode;
	}

	public Date getMoveInDate() {
		return moveInDate;
	}

	public void setMoveInDate(Date moveInDate) {
		this.moveInDate = moveInDate;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double pricePerMonth) {
		this.price = pricePerMonth;
	}

	public double getSquareFootage() {
		return squareFootage;
	}

	public void setSquareFootage(double squareFootage) {
		this.squareFootage = squareFootage;
	}

	public String getHouseDescription() {
		return houseDescription;
	}

	public void setHouseDescription(String houseDescription) {
		this.houseDescription = houseDescription;
	}

	public List<AdPicture> getPictures() {
		return pictures;
	}

	public void setPictures(List<AdPicture> pictures) {
		this.pictures = pictures;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Set<Visit> getVisits() {
		return visits;
	}

	public void setVisits(Set<Visit> visits) {
		this.visits = visits;
	}

	
	public double getRunningCosts() {
		return runningCosts;
	}

	public void setRunningCosts(double runningCosts) {
		this.runningCosts = runningCosts;
	}

	public double getNumberOfRooms() {
		return numberOfRooms;
	}

	public void setNumberOfRooms(double numberOfRooms) {
		this.numberOfRooms = numberOfRooms;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public Date getLastRenovation() {
		return lastRenovation;
	}

	public void setLastRenovation(Date lastRenovation) {
		this.lastRenovation = lastRenovation;
	}

	public int getDistanceToNearestSuperMarket() {
		return distanceToNearestSuperMarket;
	}

	public void setDistanceToNearestSuperMarket(int distanceToNearestSuperMarket) {
		this.distanceToNearestSuperMarket = distanceToNearestSuperMarket;
	}

	public int getDistanceToNearestPublicTransport() {
		return distanceToNearestPublicTransport;
	}

	public void setDistanceToNearestPublicTransport(int distanceToNearestPublicTransport) {
		this.distanceToNearestPublicTransport = distanceToNearestPublicTransport;
	}

	public int getDistanceToNearestSchool() {
		return distanceToNearestSchool;
	}

	public void setDistanceToNearestSchool(int distanceToNearestSchool) {
		this.distanceToNearestSchool = distanceToNearestSchool;
	}

	public Set<Bet> getBets() {
		return this.bets;
	}
	
	public void setBets(Set<Bet> bets) {
		this.bets = bets;
	}

	public Date getAuctionEndingDate() {
		return auctionEndingDate;
	}
	
	public long getSecondsUntilAuctionEnding() {
		if (this.auctionEndingDate == null) {
			return 0;
		}
		if (this.isAuctionEnded()) {
			return 0;
		}
		Date date = new Date();
		return this.auctionEndingDate.getTime() - date.getTime();
	}

	public void setAuctionEndingDate(Date auctionEndingDate) {
		this.auctionEndingDate = auctionEndingDate;
	}

	public double getAuctionStartingPrice() {
		return auctionStartingPrice;
	}

	public void setAuctionStartingPrice(double auctionStartingPrice) {
		this.auctionStartingPrice = auctionStartingPrice;
	}
	
	public boolean isAuction() {
		return this.auctionStartingPrice != 0 && this.auctionEndingDate != null; 
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	// equals method is defined to check for id only
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ad other = (Ad) obj;
		if (id != other.id)
			return false;
		return true;
	}

	public double getHighestBet() {
		if (this.getLastBet() == null) {
			if (this.isAuction()) {
				return this.getAuctionStartingPrice();
			} else {
				return 0;
			}
		}
		return this.getLastBet().getPrice();
	}

	public User getLastBiddingUser() {
		if (this.getLastBet() == null) {
			return null;
		}
		return this.getLastBet().getUser();
	}
	
	/**
	 * @return the auctionStepPrice
	 */
	public double getAuctionStepPrice() {
		if (this.auctionStepPrice == 0.0) {
			return DEFAULT_AUCTION_STEP_PRICE;
		}
		return auctionStepPrice;
	}

	/**
	 * @param auctionStepPrice the auctionStepPrice to set
	 */
	public void setAuctionStepPrice(double auctionStepPrice) {
		this.auctionStepPrice = auctionStepPrice;
	}

	private Bet getLastBet() {
		Date lastDate = new Date();
		lastDate.setTime(0);
		Bet lastBet = null;
		if (this.isAuction()) {
			if (this.bets != null) {
				for (Bet bet : this.bets) {
					if (lastDate.before(bet.getCreationDate())) {
						lastBet = bet;
						lastDate = lastBet.getCreationDate();
					}
				}
			}
		}
		return lastBet;
	}

	public boolean isAuctionEnded() {
		Date date = new Date();
		return date.after(this.auctionEndingDate);
	}
	
	public boolean isOnHomepage() {
		return this.isOnHomepage;
	}
	
	public void setOnHomepage(boolean value) {
		this.isOnHomepage = value;
	}
	
	public void wasOnHomepage() {
		this.wasOnHomepage++;
	}

	public boolean isAuctionProcessed() {
		return auctionProcessed;
	}

	public void setAuctionProcessed(boolean auctionProcessed) {
		this.auctionProcessed = auctionProcessed;
	}

	public boolean isAdvertiserRated() {
		return advertiserRated;
	}

	public void setAdvertiserRated(boolean advertiserRated) {
		this.advertiserRated = advertiserRated;
	}

	public boolean isUserRated() {
		return userRated;
	}

	public void setUserRated(boolean userRated) {
		this.userRated = userRated;
	}
	
	
}