package com.example.ruburger.globaldata;


import com.example.ruburger.model.Sandwich;

/**
 * @author Natalia Peguero, Olivia Kamau
 * This combo singleton class handles the passing of data through the Sandiwch and Burger class into the Combo class
 */
public final class ComboSingleton {
    private static ComboSingleton instance;
    private Sandwich mainItem;

    /**
     * Empty constructor
     */
    public ComboSingleton() {}

    /**
     * Creates a instance of the combo singleton to use
     * @return returns the instance
     */
    public static synchronized ComboSingleton getInstance() {
        if (instance == null)
            instance = new ComboSingleton();
        return instance;
    }

    /**
     * Getter for the main item (Burger or Sandwich)
     * @return returns the main item
     */
    public Sandwich getMainItem() {
        return mainItem;
    }

    /**
     * Setter for the main item (Burger or Sandwich)
     * @param mainItem the main item to set
     */
    public void setMainItem(Sandwich mainItem) {
        this.mainItem = mainItem;
    }

}
