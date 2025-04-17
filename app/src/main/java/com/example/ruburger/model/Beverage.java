package com.example.ruburger.model;

/** This class represents a beverage item in the menu.
 *  It handles the beverage flavor, size and quantity.
 * @author Natalia Peguero, Olivia Kamau
 */
public class Beverage extends MenuItem {
	private Size size;
	private Flavor flavor;

	private static final double BEVERAGE_SMALL_PRICE = 1.99;
	private static final double BEVERAGE_MEDIUM_PRICE = 2.49;
	private static final double BEVERAGE_LARGE_PRICE = 2.99;

	/**
	 *  Constructs a beverage object with the specified quantity, size and flavor.
	 * @param quantity	The number of beverages ordered.
	 * @param size 		The size of the beverage ordered (small, medium, large).
	 * @param flavor 	The flavor of the beverage ordered.
	 */
	public Beverage(int quantity, Size size, Flavor flavor) {
		super(quantity);
		this.size = size;
		this.flavor = flavor;
	}

	/**
	 *  Gets the flavor of the beverage.
	 * 	@return		The flavor of the beverage ordered.
	 */
	public Flavor getFlavor() { return flavor; }

	/**
	 *  Gets the size of the beverage.
	 * 	@return		The size of the beverage ordered.
	 */
	public Size getSize() { return size; }

	/**
	 *	Calculates the total price of the beverage based on its size and quantity.
	 * 	@return		The total price of the beverage order.
	 */
	@Override
	public double price() {

		double beveragePrice;

		switch(size) {
			case SMALL:
				beveragePrice = BEVERAGE_SMALL_PRICE;
				break;
			case MEDIUM:
				beveragePrice = BEVERAGE_MEDIUM_PRICE;
				break;
			case LARGE:
				beveragePrice = BEVERAGE_LARGE_PRICE;
				break;
			default:
				beveragePrice = BEVERAGE_MEDIUM_PRICE;;
		}
		return beveragePrice * quantity;
	}

	/**
	 * 	Returns a string representation of the beverage order.
	 * 	@return		A formatted string with the beverage order details.
	 */
	public String toString() {
		return String.format("%s, %s $%.2f [%d]", flavor.name(), size.name(), price(), quantity);
	}

}
