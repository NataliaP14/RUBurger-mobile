package com.example.ruburger.model;

import java.util.ArrayList;

/**
 * This class represents a Burger item in the menu.
 * It handles the burger bread, add-ons, quantity, patty selection and price.
 * @author Natalia Peguero, Olivia Kamau
 */
public class Burger extends Sandwich {
	private boolean doublePatty;
	public static final double DOUBLE_PATTY_PRICE = 2.50;

	/**
	 * Constructs a burger object with the user's choices.
	 * @param bread		The user's choice of bread.
	 * @param addOns	The add ons selected by the user.
	 * @param quantity	The number of burgers in the order.
	 * @param doublePatty	Returns true if the user selects a double patty,
	 *                         otherwise returns false (if the user selects a single patty).
	 */
	public Burger(Bread bread, ArrayList<AddOns> addOns, int quantity, boolean doublePatty) {
		super(quantity, bread, Protein.BEEF_PATTY, addOns);
		this.doublePatty = doublePatty;
	}

	/**
	 * Calculates the total price for the burger depending on the user's selections.
	 * @return   The total price for the burger.
	 */
	@Override
	public double price() {
		double totalPrice = super.price();

		if (doublePatty) {
			totalPrice += DOUBLE_PATTY_PRICE * quantity;
		}

		return totalPrice;


	}

	/**
	 *  Returns a string representation of the user's burger order.
	 *  @return   String with the user's burger details.
	 */
	@Override
	public String toString() {

		String addOnsString;

		if (addOns.isEmpty()) {
			addOnsString = "None";
		} else {
			addOnsString = "";
			for (int i = 0; i < addOns.size(); i++) {
				addOnsString += addOns.get(i).name();
				if (i < addOns.size() - 1) {
					addOnsString += ", ";
				}
			}
		}

		String pattyType = doublePatty ? "double" : "single";

		return String.format("Burger [%s] [%s] [%s] [%.2f] [%d]", pattyType, bread, addOnsString, price(), quantity);
	}

}
