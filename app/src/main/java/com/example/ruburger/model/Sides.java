package com.example.ruburger.model;

/**
 * This class represents the side option on the menu. It allows the user to select a side,
 * its size, the quantity and gets the price of the user's selection.
 * @author Natalia Peguero, Olivia Kamau
 */
public class Sides extends MenuItem {

    private Side side;
    private Size size;

    private static final double SIDE_MEDIUM_PRICE = 0.50;
    private static final double SIDE_LARGE_PRICE = 1.00;

    /**
     *  Constructs a side object with the specified quantity, side and size.
     *  @param quantity     The number of sides ordered.
     *  @param side         The type of side ordered.
     *  @param size         The size of the side ordered.
     */
    public Sides(int quantity, Side side, Size size) {
        super(quantity);
        this.side = side;
        this.size = size;
    }

    /**
     * Gets the side that the customer ordered.
     * @return      The type of side item ordered.
     */
    public Side getSide() { return side; }

    /**
     * Gets the selected size of the side item.
     * @return      The size of the side ordered.
     */
    public Size getSize() { return size; }

    /**
     *  Calculates the total price of the size based on size and quantity.
     *  @return     The total price of the side ordered.
     */
    @Override
    public double price() {
        double basePrice = side.getPrice();

        switch(size) {
            case MEDIUM:
                basePrice += SIDE_MEDIUM_PRICE;
                break;
            case LARGE:
                basePrice += SIDE_LARGE_PRICE;
                break;
            default:    // case SMALL
                break;
        }

        return basePrice * quantity;
    }

    /**
     *  Returns a string representation of the side item.
     *  @return     A formatted string with the side order details.
     */
    @Override
    public String toString() {
        return String.format("%s, %s [%d] $%.2f", side.name(), size.name(), quantity, price());
    }
}
