package com.example.ruburger.model;

import java.util.ArrayList;


/**
 * This class represents an order made my the customer. It gets the order number
 * for each order, starts a new order, adds/removes items from the order, updates the
 * dynamic price and calculates the total price of the order using the subtotal and
 * NJ sales tax.
 * @author Natalia Peguero, Olivia Kamau
 */
public class Order {
	private int number;		// unique # for each order
	private ArrayList<MenuItem> items;	// the list of food items in the order
	private double subPrice; 	// the total price of the order (without tax)

	private static final double NJ_TAX_RATE = 0.06625;
	private static int orderCount = 0;		// static global variable to update the number

	/**
	 *  Constructs an order object with a unique order number.
	 * @param number	The order number.
	 */

	public Order (int number) {
		this.number = number;
		this.items = new ArrayList<>();
		this.subPrice = 0.0;
	}

	/**
	 * Gets the unique order number for each order placed.
	 * @return	The unique order number.
	 */
	public int getOrderNumber() {
		return number;
	}

	/**
	 * Gets the list of items in the order.
	 * @return	the array list containing the menu items.
	 */
	public ArrayList<MenuItem> getItems() {
		return items;
	}

	/**
	 * Finalizes the current order and starts a new one.
	 * @return	A new order instance with the next order number.
	 */
	public Order startNewOrder() {
		orderCount++;
		return new Order(orderCount);
	}

	/**
	 * Updates the subtotal price after a user adds or removes an item from
	 * their order.
	 */
	private void updatePrice() {
		subPrice = 0;
		for(MenuItem item : items) {
			subPrice += item.price();
		}
	}

	/**
	 * Adds the menu item to the order.
	 * @param item		The item to be added to the order.
	 */
	public void addItem(MenuItem item) {
		items.add(item);
		updatePrice();
	}

	/**
	 * Removes a menu item from the order by its index.
	 * @param index		The index of the item to be removed.
	 */
	public void removeItem(int index) {
		if(index >= 0 && index < items.size()) {
			items.remove(index);
			updatePrice();
		}
	}

	/**
	 * Gets the total price of the order (without tax)
	 * @return	Total price of all items in the order (without tax)
	 */
	public double getSubTotal() {
		return subPrice;
	}

	/**
	 * Gets the sales tax for the order based on the total price.
	 * @return	The sales tax amount for the order.
	 */
	public double getSalesTax() {
		return subPrice * NJ_TAX_RATE;
	}

	/**
	 * Calculates the final total price of the order (including sales tax)
	 * @return	Final total price of the order (+ sales tax)
	 */
	public double getTotalAmount() {
		return subPrice + getSalesTax();
	}

}
