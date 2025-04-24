package com.example.ruburger.globaldata;

import com.example.ruburger.model.MenuItem;
import com.example.ruburger.model.Order;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a singleton class that manages the current and placed orders.
 * It stores the current list of the menu items being ordered, the price and a list of the past orders.
 */
public final class OrderSingleton {
    private static OrderSingleton instance;
    private List<MenuItem> currentOrder;
    private double orderTotal;
    private int currentOrderNumber = 0;
    private List<Order> placedOrders;

    /**
     * Constructor for the singleton pattern that initializes the current order list and placed orders list.
     */
    private OrderSingleton() {
        currentOrder = new ArrayList<>();
        placedOrders = new ArrayList<>();
        orderTotal = 0.0;
    }

    /**
     * Sets the current order to the provided list of the MenuItem objects.
     * @param currentOrder  the new list of items in the current order.
     */
    public void setCurrentOrder(List<MenuItem> currentOrder) {
        this.currentOrder = currentOrder;
    }

    /**
     * Retrieves the current list of items in the order.
     * @return  the list of MenuItem objects in the current order.
     */
    public List<MenuItem> getCurrentOrder() {
        return currentOrder;
    }

    /**
     * Retrieves the current total price of the order.
     * @return  the total price of the current order.
     */
    public double getOrderTotal() {
        return orderTotal;
    }

    /**
     * Recalculates the total price of the current order if an item is removed.
     */
    public void updateOrderTotal() {
        orderTotal = 0.0;
        for (MenuItem item : currentOrder) {
            orderTotal += item.price();
        }
    }

    /**
     * Retrieves teh singleton instance of OrderSingleton and creates the instance if it doesn't already exist.
     * @return  the order singleton instance
     */
    public static synchronized OrderSingleton getInstance() {
        if (instance == null)
            instance = new OrderSingleton();
        return instance;
    }

    /**
     * Adds a menu item to the current order and updates the total.
     * @param item  the item being added to the current order.
     */
    public void addItem(MenuItem item) {
        currentOrder.add(item);
        orderTotal += item.price();
    }

    /**
     * Removes a menu item at a specified index from the current order and updates the price.
     * @param index     the index/position of the item to be removed.
     * @return          true if the item was successfully removed, false otherwise.
     */
    public boolean removeItem(int index) {
        if (index >= 0 && index < currentOrder.size()) {
            MenuItem removedItem = currentOrder.remove(index);
            orderTotal -= removedItem.price();
            return true;
        }
        return false;
    }

    /**
     * Checks if the current order is empty.
     * @return  true if the current order is empty, false otherwise.
     */
    public boolean isEmpty() {
        return currentOrder.isEmpty();
    }

    /**
     * Gets the current order number.
     * @return  the current order number.
     */
    public int getCurrentOrderNumber() {
        return currentOrderNumber;
    }

    /**
     * Finalizes the current order and stores it in the placed orders list. It also increments the
     * order number and clears the current order.
     * @return  the order number of the placed order, or -1 if the order is empty
     */
    public int placeOrder() {
        if (!currentOrder.isEmpty()) {
            Order order = new Order(currentOrderNumber);

            for (MenuItem item : currentOrder) {
                order.addItem(item);
            }

            placedOrders.add(order);

            int placedOrderNumber = currentOrderNumber;
            currentOrder.clear();
            orderTotal = 0.0;
            currentOrderNumber++;

            return placedOrderNumber;
        }
        return -1;
    }

    /**
     * Retrieves the list of items from a previously placed order.
     * @param orderNumber   the number of the order to retrieve.
     * @return  list of MenuItem objects from the placed order, or null if not found.
     */
    public List<MenuItem> getPlacedOrder(int orderNumber) {
        for (Order order : placedOrders) {
            if (order.getOrderNumber() == orderNumber) {
                return order.getItems();
            }
        }
        return null;
    }

    /**
     * Returns a list of all the placed order numbers.
     * @return  list of the order numbers.
     */
    public List<Integer> getAllOrderNumbers() {
        List<Integer> orderNumbers = new ArrayList<>();
        for (Order order : placedOrders) {
            orderNumbers.add(order.getOrderNumber());
        }
        return orderNumbers;
    }

    /**
     * Clears all the placed orders from memory.
     */
    public void clearAllPlacedOrders() {
        placedOrders.clear();
    }

    /**
     * Resets the current order number to a specific value.
     * @param startForm     the value to rest the order number to.
     */
    public void resetCurrentOrderNumber(int startForm) {
        currentOrderNumber = startForm;
    }

    /**
     *  Sets the current order number to a specific value.
     * @param orderNumber   the value to set as the current order number.
     */
    public void setCurrentOrderNumber(int orderNumber) {
        currentOrderNumber = orderNumber;
    }

    /**
     * Retrieves the total amount (including tax) for a placed order.
     * @param orderNumber   the number of the placed order.
     * @return  total amount with tax, or 0.0 if the order is not found.
     */
    public double getTotalWithTax(int orderNumber) {
        for (Order order : placedOrders) {
            if (order.getOrderNumber() == orderNumber) {
                return order.getTotalAmount();
            }
        }
        return 0.0;
    }

    /**
     * Retrieves the subtotal before tax for a placed order.
     * @param orderNumber   the number of the placed order.
     * @return  the subtotal.
     */
    public double getOrderSubtotal(int orderNumber) {
        for (Order order : placedOrders) {
            if (order.getOrderNumber() == orderNumber) {
                return order.getSubTotal();
            }
        }
        return 0.0;
    }

    /**
     * Retrieves the sales tax for a placed order.
     * @param orderNumber   the number of the placed order.
     * @return  the sales tax amount.
     */
    public double getOrderTax(int orderNumber) {
        for (Order order : placedOrders) {
            if (order.getOrderNumber() == orderNumber) {
                return order.getSalesTax();
            }
        }
        return 0.0;
    }
}