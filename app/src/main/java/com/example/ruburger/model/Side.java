package com.example.ruburger.model;

/**
 * This enum class represents the side options of the application
 * @author Natalia Peguero, Olivia Kamau
 */
public enum Side {
    CHIPS (1.99),
    FRIES (2.49),
    ONION_RINGS (3.29),
    APPLE_SLICES(1.29);

    private double price;

    /**
     *  Represents each side item with its price.
     * @param price     The price for the specified side.
     */
    Side(double price) {
        this.price = price;
    }

    /**
     * Gets the specified side and its price.
     * @return  The price for the specified side.
     */
    public double getPrice() {
        return price;
    }
}
