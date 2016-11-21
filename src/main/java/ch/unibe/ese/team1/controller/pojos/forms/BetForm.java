package ch.unibe.ese.team1.controller.pojos.forms;

import javax.validation.constraints.NotNull;

import ch.unibe.ese.team1.model.Ad;
import ch.unibe.ese.team1.model.User;



/** This form is used when a user wants to bet to an auction */
public class BetForm {

	@NotNull(message = "Requires a price")
	private double price;

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
}
