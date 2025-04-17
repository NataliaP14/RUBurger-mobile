package com.example.ruburger.model;

/**
 * This class is an abtract class that represents the menu items
 * @author Natalia Peguero, Olivia Kamau
 */
public abstract class MenuItem {
	protected int quantity;


	/**
	 * Gets the price of the menu item.
	 * @return	The price of the menu item.
	 */
	public abstract double price();

	/**
	 *	Constructs a menu item with the specified quantity.
	 * @param quantity	The quantity of each menu item.
	 */
	public MenuItem(int quantity) {
		this.quantity = quantity;
	}

}
