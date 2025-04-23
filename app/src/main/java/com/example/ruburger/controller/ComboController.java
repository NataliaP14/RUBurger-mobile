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
import com.example.ruburger.model.Burger;
import com.example.ruburger.model.Side;
import com.example.ruburger.model.Flavor;
import com.example.ruburger.model.Combo;
import com.example.ruburger.globaldata.ComboSingleton;
import com.example.ruburger.globaldata.OrderSingleton;
import com.example.ruburger.model.Sandwich;
import com.example.ruburger.model.Size;

import java.text.NumberFormat;
import java.util.ArrayList;


public class ComboController extends AppCompatActivity {
    private Spinner sideSpinner, drinkSpinner;
    private ImageView sideIcon, drinkIcon;

    private ImageButton minusBtn, plusBtn;

    private TextView sandwichDetails, quantityText, priceText;
    private Button addToCartBtn;
    private int quantity = 1;

    private static final Size MEDIUM_DRINK = Size.MEDIUM;

    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        }

        initializeButtons();
        setupAdapters();
        setupListeners();
        updatePrice();


        Button menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        Button cartButton = findViewById(R.id.cartButton);
        Button ordersButton = findViewById(R.id.ordersButton);

        cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CurrentOrderController.class);
            startActivity(intent);
        });

        ordersButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, PlacedOrderController.class);
            startActivity(intent);
        });
    }


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

    private void setupListeners() {
        minusBtn.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                quantityText.setText(String.valueOf(quantity));
                updatePrice();
            }
        });

        plusBtn.setOnClickListener(v -> {
            quantity++;
            quantityText.setText(String.valueOf(quantity));
            updatePrice();
        });

        addToCartBtn.setOnClickListener(v -> {
            addComboToCart();
        });

        drinkSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Flavor flavor = (Flavor) parent.getItemAtPosition(position);

                switch (flavor) {
                    case COLA: drinkIcon.setImageResource(R.drawable.cola); break;
                    case JUICE: drinkIcon.setImageResource(R.drawable.juice); break;
                    case TEA: drinkIcon.setImageResource(R.drawable.tea); break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        sideSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Side side = (Side) parent.getItemAtPosition(position);

                switch (side) {
                    case CHIPS: sideIcon.setImageResource(R.drawable.chips); break;
                    case APPLE_SLICES: sideIcon.setImageResource(R.drawable.apple_slice); break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

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