package com.example.ruburger.globaldata;


import com.example.ruburger.model.Sandwich;

public final class ComboSingleton {
    private static ComboSingleton instance;
    private Sandwich mainItem;

    public ComboSingleton() {}

    public static synchronized ComboSingleton getInstance() {
        if (instance == null)
            instance = new ComboSingleton();
        return instance;
    }

    public Sandwich getMainItem() {
        return mainItem;
    }

    public void setMainItem(Sandwich mainItem) {
        this.mainItem = mainItem;
    }

}
