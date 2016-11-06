package ch.unibe.ese.team1.controller.pojos.forms;

import javax.validation.constraints.AssertFalse;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;

import ch.unibe.ese.team1.model.User;

/** This form is used when a user wants to create a new alert. */
public class AlertForm {
	
	private User user;

	private boolean flat;
	private boolean house;

	@NotBlank(message = "Required")
	@Pattern(regexp = "^[0-9]{4} - [-\\w\\s\\u00C0-\\u00FF]*", message = "Please pick a city from the list")
	private String city;

	@NotNull(message = "Requires a number")
	@Min(value = 0, message = "Please enter a positive distance")
	private Integer radius;
	
	@NotNull(message = "Requires a number")
	@Min(value = 0, message = "In your dreams.")
	private Integer price;
	
	@NotNull(message = "Requires a number")
	private Integer numberOfRooms;
	
	@NotNull(message = "Requires a number")
	private Integer squareFootage;
	
	private int zipCode;

	@AssertFalse(message = "Please select either or both types")
	private boolean noHouseNoFlat;

	private boolean bothHouseAndFlat;

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public int getZipCode() {
		return zipCode;
	}
	public void setZipCode(int zip) {
		this.zipCode = zip;
	}

	public Integer getRadius() {
		return radius;
	}

	public void setRadius(Integer radius) {
		this.radius = radius;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public boolean getFlat() {
		return flat;
	}

	public void setFlat(boolean flat) {
		this.flat = flat;
	}

	public boolean getHouse() {
		return house;
	}

	public void setHouse(boolean house) {
		this.house = house;
	}

	public boolean getNoHouseNoFlat() {
		return noHouseNoFlat;
	}

	public void setNoHouseNoFlat(boolean noHouseNoFlat) {
		this.noHouseNoFlat = noHouseNoFlat;
	}

	public boolean getBothHouseAndFlat() {
		return bothHouseAndFlat;
	}

	public void setBothHouseAndFlat(boolean bothHouseAndFlat) {
		this.bothHouseAndFlat = bothHouseAndFlat;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public Integer getNumberOfRooms(){
		return numberOfRooms;
	}
	
	public void setNumberOfRooms(Integer numberOfRooms){
		this.numberOfRooms = numberOfRooms;
	}
	
	public Integer getSquareFootage(){
		return squareFootage;
	}
	
	public void setSquareFootage(Integer squareFootage){
		this.squareFootage = squareFootage;
	}
}
