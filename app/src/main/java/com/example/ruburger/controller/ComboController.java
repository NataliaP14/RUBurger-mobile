package com.example.ruburger.controller;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ruburger.R;
import com.example.ruburger.model.Beverage;
import com.example.ruburger.model.Side;
import com.example.ruburger.model.Flavor;
import com.example.ruburger.model.Combo;
import com.example.ruburger.globaldata.ComboSingleton;
import com.example.ruburger.globaldata.OrderSingleton;
import com.example.ruburger.model.Sandwich;
import com.example.ruburger.model.Size;
import java.text.NumberFormat;

/**
 * @author Natalia Peguero, Olivia Kamau
 * This combo controller manages the logic for the combo based off Burger or Sandwich
 */
public class ComboController extends AppCompatActivity {
    private Spinner sideSpinner, drinkSpinner;
    private ImageView sideIcon, drinkIcon;

    private ImageButton minusBtn, plusBtn;

    private TextView sandwichDetails, quantityText, priceText;
    private Button addToCartBtn;
    private int quantity = 1;
    private static final Size MEDIUM_DRINK = Size.MEDIUM;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    /**
     * Manages the view for the combo, and handles the switching of views based off the buttons
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_combo);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.title), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom()
            );
            return insets;
        });

        initializeButtons();
        setupAdapters();
        setupListeners();
        updatePrice();

        LinearLayout menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        LinearLayout cartButton = findViewById(R.id.cartButton);
        LinearLayout ordersButton = findViewById(R.id.ordersButton);

        cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CurrentOrderController.class);
            startActivity(intent);
        });
        ordersButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, PlacedOrderController.class);
            startActivity(intent);
        });
    }

    /**
     * Initializing all of the buttons for user selection
     */
    private void initializeButtons() {
        sideSpinner = findViewById(R.id.sideSpinner);
        sideIcon = findViewById(R.id.sideIcon);

        drinkSpinner = findViewById(R.id.drinkSpinner);
        drinkIcon = findViewById(R.id.drinkIcon);

        minusBtn = findViewById(R.id.minusBtn);
        plusBtn = findViewById(R.id.plusBtn);
        quantityText = findViewById(R.id.quantityText);

        sandwichDetails = findViewById(R.id.sandwichDetails);
        priceText = findViewById(R.id.priceText);
        addToCartBtn = findViewById(R.id.addToCart);
    }

    /**
     * Sets up the adapters for the sides and the drinks
     */
    private void setupAdapters() {

        Side[] sides = new Side[] {
                Side.CHIPS,
                Side.APPLE_SLICES
        };
        ArrayAdapter<Side> sideAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sides);
        sideAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sideSpinner.setAdapter(sideAdapter);
        sideSpinner.setSelection(0);

        Flavor[] drinks = new Flavor[] {
                Flavor.COLA,
                Flavor.JUICE,
                Flavor.TEA
        };
        ArrayAdapter<Flavor> drinkAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, drinks);
        drinkAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drinkSpinner.setAdapter(drinkAdapter);
        drinkSpinner.setSelection(0);


    }


    /**
     * Sets up the listeners for the minus and plus button, and dynamically changes the image for the drink and side option for the combo
     */
    private void setupListeners() {
        minusBtn.setOnClickListener(v -> {
            if (quantity > 1) { quantity--; quantityText.setText(String.valueOf(quantity)); updatePrice(); } });

        plusBtn.setOnClickListener(v -> { quantity++; quantityText.setText(String.valueOf(quantity)); updatePrice(); });

        addToCartBtn.setOnClickListener(v -> { addComboToCart(); });

        drinkSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * Dynamically changes the the drink image based off user selection
             * @param parent The AdapterView where the selection happened
             * @param view The view within the AdapterView that was clicked
             * @param position The position of the view in the adapter
             * @param id The row id of the item that is selected
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Flavor flavor = (Flavor) parent.getItemAtPosition(position);

                switch (flavor) {
                    case COLA: drinkIcon.setImageResource(R.drawable.cola); break;
                    case JUICE: drinkIcon.setImageResource(R.drawable.juice); break;
                    case TEA: drinkIcon.setImageResource(R.drawable.tea); break;
                }
            }

            /**
             * Method that contains no selected items, for when u select nothing
             * @param parent The AdapterView that now contains no selected item.
             */
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        sideSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             *
             * @param parent The AdapterView where the selection happened
             * @param view The view within the AdapterView that was clicked
             * @param position The position of the view in the adapter
             * @param id The row id of the item that is selected
             */
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Side side = (Side) parent.getItemAtPosition(position);
                switch (side) {
                    case CHIPS: sideIcon.setImageResource(R.drawable.chips); break;
                    case APPLE_SLICES: sideIcon.setImageResource(R.drawable.apple_slice); break;
                }
            }

            /**
             * Method that for when u select nothing
             * @param parent The AdapterView that now contains no selected item.
             */
            @Override
            public void onNothingSelected(AdapterView<?> parent) { } });
    }

    /**
     * Dynamically updates the price of the combo, based off user selection
     */
    private void updatePrice() {
        Sandwich mainItem = ComboSingleton.getInstance().getMainItem();
        Flavor flavor = (Flavor) drinkSpinner.getSelectedItem();
        Side side = (Side) sideSpinner.getSelectedItem();

        Beverage drink = new Beverage(1, MEDIUM_DRINK, flavor);
        Combo combo = new Combo(quantity, mainItem, drink, side);

        double price = combo.price();

        sandwichDetails.setText(mainItem.toString());
        priceText.setText(getString(R.string.price_placeholder, currencyFormat.format(price)));

    }

    /**
     * Logic for adding the combo to the cart by creating a combo object
     */
    private void addComboToCart() {
        Sandwich mainItem = ComboSingleton.getInstance().getMainItem();
        Flavor flavor = (Flavor) drinkSpinner.getSelectedItem();
        Side side = (Side) sideSpinner.getSelectedItem();

        Beverage drink = new Beverage(1, MEDIUM_DRINK, flavor);
        Combo combo = new Combo(quantity, mainItem, drink, side);

        OrderSingleton.getInstance().addItem(combo);

        Toast.makeText(this, "Combo added to your order!", Toast.LENGTH_SHORT).show();

    }
}