package com.example.ruburger.model;

/**
 * This is an enum class containing a list of different types of protein that
 * a user can select when ordering a sandwich or burger. Each protein has its
 * associated price.
 * @author Natalia Peguero, Olivia Kamau
 */
public enum Protein {
	BEEF_PATTY(6.99),   // price for a single patty burger (default option)
	ROAST_BEEF(10.99),
	SALMON(9.99),
	CHICKEN(8.99);

	private double price;

	/**
	 * Constructs a protein object with its specific price.
	 * @param price		The price of the addon selected.
	 */
	Protein(double price) {
		this.price = price;
	}

	/**
	 * Gets the price of the addon selected.
	 * @return		The price of the specified addon.
	 */
	public double getPrice() {
		return price;
	}

}
