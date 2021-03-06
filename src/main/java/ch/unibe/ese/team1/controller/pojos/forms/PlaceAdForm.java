package ch.unibe.ese.team1.controller.pojos.forms;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

/** This form is used when a user wants to place a new ad. */
public class PlaceAdForm {
	
	@NotBlank(message = "Required")
	private String title;
	
	@NotBlank(message = "Required")
	private String street;
	
	@Pattern(regexp = "^[0-9]{4} - [-\\w\\s,;\\u00C0-\\u00FF]*", message = "Please pick a city from the list")
	private String city;
	
	private String moveInDate;
	
	@Min(value = 1, message = "Has to be equal to 1 or more")
	private double price;

	@Min(value = 1, message = "Has to be equal to 1 or more")
	private double squareFootage;

	@NotBlank(message = "Required")
	private String houseDescription;


	// optional for input
	private String houseFriends;
	
	//true if flat, false if house
	private boolean flat;
	private boolean forSale;
	
	private boolean smokers;
	private boolean animals;
	private boolean garden;
	private boolean balcony;
	private boolean cellar;
	private boolean furnished;
	private boolean cable;
	private boolean garage;
	
	private int floor;
	private double numberOfRooms;
	private double runningCosts;
	private String lastRenovation;
	private int distanceToNearestSuperMarket;
	private int distanceToNearestPublicTransport;
	private int distanceToNearestSchool;
	
	private String auctionEndingDate;
	private int auctionEndingHour;
	private int auctionEndingMinute;
	private double auctionStartingPrice;
	private double auctionStepPrice;
	

	private List<String> visits;

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public double getNumberOfRooms() {
		return numberOfRooms;
	}

	public void setNumberOfRooms(double numberOfRooms) {
		this.numberOfRooms = numberOfRooms;
	}

	public double getRunningCosts() {
		return runningCosts;
	}

	public void setRunningCosts(double runningCosts) {
		this.runningCosts = runningCosts;
	}

	public String getLastRenovation() {
		return lastRenovation;
	}

	public void setLastRenovation(String lastRenovation) {
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getHouseDescription() {
		return houseDescription;
	}

	public void setHouseDescription(String houseDescription) {
		this.houseDescription = houseDescription;
	}


	public double getSquareFootage() {
		return squareFootage;
	}

	public void setSquareFootage(double squareFootage) {
		this.squareFootage = squareFootage;
	}

	

	public boolean isSmokers() {
		return smokers;
	}

	public void setSmokers(boolean smoker) {
		this.smokers = smoker;
	}

	public boolean isAnimals() {
		return animals;
	}

	public void setAnimals(boolean animals) {
		this.animals = animals;
	}
	
	public boolean getGarden() {
		return garden;
	}

	public void setGarden(boolean garden) {
		this.garden = garden;
	}

	public boolean getBalcony() {
		return balcony;
	}

	public void setBalcony(boolean balcony) {
		this.balcony = balcony;
	}
	
	public boolean getCellar() {
		return cellar;
	}

	public void setCellar(boolean cellar) {
		this.cellar = cellar;
	}
	
	public boolean isFurnished() {
		return furnished;
	}

	public void setFurnished(boolean furnished) {
		this.furnished = furnished;
	}

	public boolean getCable() {
		return cable;
	}

	public void setCable(boolean cable) {
		this.cable = cable;
	}
	
	public boolean getGarage() {
		return garage;
	}

	public void setGarage(boolean garage) {
		this.garage = garage;
	}

	public String getMoveInDate() {
		return moveInDate;
	}

	public void setMoveInDate(String moveInDate) {
		this.moveInDate = moveInDate;
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

	public String getHouseFriends() {
		return houseFriends;
	}

	public void setHouseFriends(String houseFriends) {
		this.houseFriends = houseFriends;
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

	

	public List<String> getVisits() {
		return visits;
	}

	public void setVisits(List<String> visits) {
		this.visits = visits;
	}

	public String getAuctionEndingDate() {
		return auctionEndingDate;
	}

	public void setAuctionEndingDate(String auctionEndingDate) {
		this.auctionEndingDate = auctionEndingDate;
	}

	public double getAuctionStartingPrice() {
		return auctionStartingPrice;
	}

	public void setAuctionStartingPrice(double auctionStartingPrice) {
		this.auctionStartingPrice = auctionStartingPrice;
	}

	public int getAuctionEndingHour() {
		return auctionEndingHour;
	}

	public void setAuctionEndingHour(int auctionEndingHour) {
		this.auctionEndingHour = auctionEndingHour;
	}

	public int getAuctionEndingMinute() {
		return auctionEndingMinute;
	}

	public void setAuctionEndingMinute(int auctionEndingMinute) {
		this.auctionEndingMinute = auctionEndingMinute;
	}

	public double getAuctionStepPrice() {
		return auctionStepPrice;
	}

	public void setAuctionStepPrice(double auctionStepPrice) {
		this.auctionStepPrice = auctionStepPrice;
	}

}
