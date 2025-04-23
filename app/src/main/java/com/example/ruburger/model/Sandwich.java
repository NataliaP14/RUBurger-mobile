package com.example.ruburger.model;

import java.util.ArrayList;

/**
 * This class represents a sandwich object on the menu.
 * It handles the sandwich bread, protein, add-ons, quantity and price.
 * @author Natalia Peguero, Olivia Kamau
 */
public class Sandwich extends MenuItem {
	protected Bread bread;
	protected Protein protein;
	protected ArrayList<AddOns> addOns;


	/**
	 *Constructs a sandwich object with the user's bread, protein, addon and
	 * quantity selections.
	 * @param quantity	The number of sandwiches ordered by the user.
	 * @param bread		The type of bread chosen by the user.
	 * @param protein	The type of protein selected.
	 * @param addOns	The addons chosen by the user.
	 */
	public Sandwich(int quantity, Bread bread, Protein protein, ArrayList<AddOns> addOns) {
		super(quantity);
		this.bread = bread;
		this.protein = protein;
		this.addOns = addOns;
	}

	/**
	 *  Calculates the total price for the sandwich with the user's selections.
	 *  @return 	The total price of the sandwich.
	 */
	@Override
	public double price() {

		double totalPrice = protein.getPrice();
		for(AddOns addOn : addOns) { totalPrice += addOn.getPrice(); }
		totalPrice *= quantity;

		return totalPrice;
	}

	/**
	 * Gets the type of bread chosen by the user.
	 * @return	The specific type of bread selected by the user.
	 */
	public Bread getBread() {
		return bread;
	}

	/**
	 * Gets the protein choice of the user.
	 * @return	The choice of protein selected by the user.
	 */
	public Protein getProtein() {
		return protein;
	}

	/**
	 * Gets the list of add-ons selected by the user.
	 * @return	A list of add-ons chosen by the user.
	 */
	public ArrayList<AddOns> getAddOns() {
		return addOns;
	}

	/**
	 *  Returns a string representation of the user's sandwich order.
	 *  @return   String with the user's sandwich details.
	 */
	public String toString() {
		String addOnsString;

		if(addOns.isEmpty()) {
			addOnsString = "None";
		} else {
			addOnsString = "";
			for(int i = 0; i < addOns.size(); i++) {
				addOnsString += addOns.get(i).name();
				if(i < addOns.size() -1) {
					addOnsString += ", ";
				}
			}
		}

		return String.format("Sandwich [%s] [%s] [%s] [%.2f] [%d]", bread, protein, addOnsString, price(), quantity);
	}

}
