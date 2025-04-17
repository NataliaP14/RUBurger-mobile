package com.example.ruburger.model;

/**
 * This is an enum class for the add-ons that the user can pick when
 * customizing their burger or sandwich. Each add-on has its associated price.
 * @author Natalia Peguero, Olivia Kamau
 */
public enum AddOns {
	LETTUCE (0.30),
	TOMATOES (0.30),
	ONIONS (0.30),
	AVOCADO (0.50),
	CHEESE (1.00);

	private double price;

	/**
	 * Constructs and add-on with its specified price.
	 * @param price		The price of the addon.
	 */
	AddOns(double price) {
		this.price = price;
	}

	/**
	 *Gets the price of the addon.
	 * @return		The price of the specified addon.
	 */
	public double getPrice() {
		return price;
	}
}
