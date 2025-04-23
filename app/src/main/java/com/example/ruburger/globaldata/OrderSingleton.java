package com.example.ruburger.globaldata;

import com.example.ruburger.model.MenuItem;

import java.util.List;


public final class OrderSingleton {
    private static OrderSingleton instance;
    private List<MenuItem> currentOrder;
    private double orderTotal;

    private OrderSingleton() {

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


}
