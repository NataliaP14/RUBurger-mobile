package com.example.ruburger.globaldata;

import com.example.ruburger.model.MenuItem;

import java.util.ArrayList;
import java.util.List;

public final class OrderSingleton {
    private static OrderSingleton instance;
    private List<MenuItem> currentOrder;
    private double orderTotal;
    private int currentOrderNumber = 0;

    private static class PlacedOrder {
        private int orderNumber;
        private List<MenuItem> items;

        public PlacedOrder(int orderNumber, List<MenuItem> items) {
            this.orderNumber = orderNumber;
            this.items = items;
        }

        public int getOrderNumber() {
            return orderNumber;
        }

        public List<MenuItem> getItems() {
            return items;
        }
    }

    private List<PlacedOrder> placedOrders;

    private OrderSingleton() {
        currentOrder = new ArrayList<>();
        placedOrders = new ArrayList<>();
        orderTotal = 0.0;
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
            List<MenuItem> orderCopy = new ArrayList<>(currentOrder);
            PlacedOrder placedOrder = new PlacedOrder(currentOrderNumber, orderCopy);
            placedOrders.add(placedOrder);

            int placedOrderNumber = currentOrderNumber;
            currentOrder.clear();
            orderTotal = 0.0;
            currentOrderNumber++;

            return placedOrderNumber;
        }
        return -1;
    }

    public List<MenuItem> getPlacedOrder(int orderNumber) {
        for (PlacedOrder order : placedOrders) {
            if (order.getOrderNumber() == orderNumber) {
                return order.getItems();
            }
        }
        return null;
    }

    public List<Integer> getAllOrderNumbers() {
        List<Integer> orderNumbers = new ArrayList<>();
        for (PlacedOrder order : placedOrders) {
            orderNumbers.add(order.getOrderNumber());
        }
        return orderNumbers;
    }
}