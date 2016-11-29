package ch.unibe.ese.team1.controller.pojos.forms;


import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;

import ch.unibe.ese.team1.controller.service.GeoDataService;
import ch.unibe.ese.team1.model.Location;

/** This form is used for searching for an ad. */
public class SearchForm {
	
	private boolean filtered;

	// flat: true, house: false
	private boolean flat;
	// sale: true, rent: false
	private boolean forSale = false;

	@NotBlank(message = "Required")
	@Pattern(regexp = "^[0-9]{4} - [-\\w\\s,;\\u00C0-\\u00FF]*", message = "Please pick a city from the list")
	private String city;
	
	@NotNull(message = "Requires a number")
	@Min(value = 0, message = "Please enter a positive distance")
	private Integer radius;
	
	@NotNull(message = "Requires a number")
	@Min(value = 0, message = "In your dreams.")
	private double prize;
	
	private boolean includeRunningCosts;

	private boolean bothHouseAndFlat = true;
	
	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getRadius() {
		return radius;
	}

	public void setRadius(Integer radius) {
		this.radius = radius;
	}

	public double getPrize() {
		return prize;
	}

	public void setPrize(double prize) {
		this.prize = prize;
	}
	
	public boolean getIncludeRunningCosts() {
		return includeRunningCosts;
	}

	public void setIncludeRunningCosts(boolean includeRunningCosts) {
		this.includeRunningCosts = includeRunningCosts;
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

	public boolean getBothHouseAndFlat() {
		return bothHouseAndFlat;
	}

	public void setBothHouseAndFlat(boolean bothHouseAndFlat) {
		this.bothHouseAndFlat = bothHouseAndFlat;
	}

	
	// //////////////////
	// Filtered results//
	// //////////////////

	public boolean getFiltered() {
		return filtered;
	}

	public void setFiltered(boolean filtered) {
		this.filtered = filtered;
	}

	private String earliestMoveInDate;
	private String latestMoveInDate;

	private boolean smokers;
	private boolean animals;
	private boolean garden;
	private boolean balcony;
	private boolean cellar;
	private boolean furnished;
	private boolean cable;
	private boolean garage;
	private boolean groundFloor;
	
	private int squareFootage;
	private int numberOfRooms;
	private String lastRenovation;
	private int distanceToNearestSuperMarket;
	private int distanceToNearestPublicTransport;
	private int distanceToNearestSchool;

	private boolean houseHelper;

	// the ugly stuff
	private boolean flatHelper;
	
	private double latitude;
	private double longitude;

	public boolean getSmokers() {
		return smokers;
	}

	public void setSmokers(boolean smokers) {
		this.smokers = smokers;
	}

	public boolean getAnimals() {
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

	public boolean getFurnished() {
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

	public String getEarliestMoveInDate() {
		return earliestMoveInDate;
	}

	public void setEarliestMoveInDate(String earliestMoveInDate) {
		this.earliestMoveInDate = earliestMoveInDate;
	}

	public String getLatestMoveInDate() {
		return latestMoveInDate;
	}

	public boolean getGroundFloor() {
		return groundFloor;
	}

	public void setGroundFloor(boolean groundFloor) {
		this.groundFloor = groundFloor;
	}

	public int getSquareFootage() {
		return squareFootage;
	}

	public void setSquareFootage(int squareFootage) {
		this.squareFootage = squareFootage;
	}

	public int getNumberOfRooms() {
		return numberOfRooms;
	}

	public void setNumberOfRooms(int numberOfRooms) {
		this.numberOfRooms = numberOfRooms;
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

	public void setLatestMoveInDate(String latestMoveInDate) {
		this.latestMoveInDate = latestMoveInDate;
	}

	public boolean getFlatHelper() {
		return flatHelper;
	}

	public void setFlatHelper(boolean helper) {
		this.flatHelper = helper;
	}

	public boolean getHouseHelper() {
		return houseHelper;
	}

	public void setHouseHelper(boolean helper) {
		this.houseHelper = helper;
	}
	
	public void setLatitude(double latitude){
		this.latitude = latitude;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public void setLongitude(double longitude){
		this.longitude = longitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
}