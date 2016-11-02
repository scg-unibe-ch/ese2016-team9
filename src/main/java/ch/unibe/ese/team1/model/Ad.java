package ch.unibe.ese.team1.model;

import java.util.Date;
import java.util.List;

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

	@Temporal(TemporalType.DATE)
	@Column(nullable = true)
	private Date moveOutDate;

	@Column(nullable = false)
	private int prize;

	@Column(nullable = false)
	private int squareFootage;

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
	
	@Column(nullable = false)
	private boolean forSale;

	@Column(nullable = false)
	private int numberOfRooms;
	
	@Column(nullable = false)
	private int runningCosts;
	
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

	@Fetch(FetchMode.SELECT)
	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<AdPicture> pictures;

	@ManyToOne(optional = false)
	private User user;
	
	@OneToMany(mappedBy = "ad", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Visit> visits;

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

	public void setMoveOutDate(Date moveOutDate) {
		this.moveOutDate = moveOutDate;
	}

	public int getPrize() {
		return prize;
	}

	public void setPrize(int prizePerMonth) {
		this.prize = prizePerMonth;
	}

	public int getSquareFootage() {
		return squareFootage;
	}

	public void setSquareFootage(int squareFootage) {
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

	public Date getMoveOutDate() {
		return moveOutDate;
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

	public Date getDate(boolean date) {
		if (date)
			return moveInDate;
		else
			return moveOutDate;
	}

	public List<Visit> getVisits() {
		return visits;
	}

	public void setVisits(List<Visit> visits) {
		this.visits = visits;
	}

	
	public int getRunningCosts() {
		return runningCosts;
	}

	public void setRunningCosts(int runningCosts) {
		this.runningCosts = runningCosts;
	}

	public int getNumberOfRooms() {
		return numberOfRooms;
	}

	public void setNumberOfRooms(int numberOfRooms) {
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
}