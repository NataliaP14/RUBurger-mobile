package com.example.ruburger.model;

/**
 * This class represents a combo meal on the menu.
 * It handles the drink, side and price of the combo meal.
 * @author Natalia Peguero, Olivia Kamau
 */
public class Combo extends MenuItem {
	private Sandwich sandwich;
	private Beverage drink;
	private Side side;

	private static final double COMBO_PRICE = 2.00;
	private static final Size COMBO_SIZE =Size.MEDIUM;

	/**
	 * Constructs a combo object with the sandwich/burger order, drink, side and quantity details.
	 * @param quantity	The number of combo orders.
	 * @param sandwich	The previously selected sandwich/burger order.
	 * @param drink		The combo drink order.
	 * @param side		The combo side order.
	 */
	public Combo(int quantity, Sandwich sandwich, Beverage drink, Side side) {
		super(quantity);
		this.sandwich = sandwich;
		this.drink = drink;
		this.side = side;
	}

	/**
	 * 	Gets the sandwich/burger order details.
	 * 	@return		The sandwich/burger ordered.
	 */
	public Sandwich getSandwich() { return sandwich; }

	/**
	 * 	Gets the combo drink details.
	 * 	@return		The drink ordered.
	 */
	public Beverage getDrink() { return drink; }

	/**
	 * 	Gets the combo side details.
	 * 	@return		The side ordered.
	 */
	public Side getSide() { return side; }

	/**
	 *  Sets the price for a combo meal (+$2 to the current price).
	 * 	@return		The price of the combo meal
	 */
	@Override
	public double price() {

		double totalPrice = sandwich.price();
		totalPrice += COMBO_PRICE;
		totalPrice *= quantity;
		return totalPrice;
	}

	/**
	 *  Gives the string representation of the combo meal details.
	 * 	@return		String with the details of the combo meal order.
	 */
	@Override
	public String toString() {

		String sandwichOrBurgerInfo = sandwich.toString();

		String drinkInfo = drink.getFlavor().name();
		String sideInfo = side.name();

		return String.format("Combo [%d] %s [%s, %s, MEDIUM] [%.2f]", quantity, sandwichOrBurgerInfo, drinkInfo, sideInfo, price());
	}
}
