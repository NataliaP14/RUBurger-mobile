package com.example.ruburger.globaldata;

import com.example.ruburger.model.MenuItem;
import com.example.ruburger.model.Order;

import java.util.ArrayList;
import java.util.List;

public final class OrderSingleton {
    private static OrderSingleton instance;
    private List<MenuItem> currentOrder;
    private double orderTotal;
    private int currentOrderNumber = 0;
    private List<Order> placedOrders;

    private OrderSingleton() {
        currentOrder = new ArrayList<>();
        placedOrders = new ArrayList<>();
        orderTotal = 0.0;
    }

    public void setCurrentOrder(List<MenuItem> currentOrder) {
        this.currentOrder = currentOrder;
    }

    public List<MenuItem> getCurrentOrder() {
        return currentOrder;
    }

    public double getOrderTotal() {
        return orderTotal;
    }

    public void updateOrderTotal() {
        orderTotal = 0.0;
        for (MenuItem item : currentOrder) {
            orderTotal += item.price();
        }
    }

    public static synchronized OrderSingleton getInstance() {
        if (instance == null)
            instance = new OrderSingleton();
        return instance;
    }

    public void addItem(MenuItem item) {
        currentOrder.add(item);
        orderTotal += item.price();
    }

    public boolean removeItem(int index) {
        if (index >= 0 && index < currentOrder.size()) {
            MenuItem removedItem = currentOrder.remove(index);
            orderTotal -= removedItem.price();
            return true;
        }
        return false;
    }

    public boolean isEmpty() {
        return currentOrder.isEmpty();
    }

    public int getCurrentOrderNumber() {
        return currentOrderNumber;
    }

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

    public List<MenuItem> getPlacedOrder(int orderNumber) {
        for (Order order : placedOrders) {
            if (order.getOrderNumber() == orderNumber) {
                return order.getItems();
            }
        }
        return null;
    }

    public List<Integer> getAllOrderNumbers() {
        List<Integer> orderNumbers = new ArrayList<>();
        for (Order order : placedOrders) {
            orderNumbers.add(order.getOrderNumber());
        }
        return orderNumbers;
    }

    public void clearAllPlacedOrders() {
        placedOrders.clear();
    }

    public void resetCurrentOrderNumber(int startForm) {
        currentOrderNumber = startForm;
    }

    public void setCurrentOrderNumber(int orderNumber) {
        currentOrderNumber = orderNumber;
    }

    public double getTotalWithTax(int orderNumber) {
        for (Order order : placedOrders) {
            if (order.getOrderNumber() == orderNumber) {
                return order.getTotalAmount();
            }
        }
        return 0.0;
    }

    public double getOrderSubtotal(int orderNumber) {
        for (Order order : placedOrders) {
            if (order.getOrderNumber() == orderNumber) {
                return order.getSubTotal();
            }
        }
        return 0.0;
    }

    public double getOrderTax(int orderNumber) {
        for (Order order : placedOrders) {
            if (order.getOrderNumber() == orderNumber) {
                return order.getSalesTax();
            }
        }
        return 0.0;
    }
}