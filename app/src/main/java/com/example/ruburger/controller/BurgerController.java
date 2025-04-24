package com.example.ruburger.controller;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ruburger.R;
import com.example.ruburger.model.Burger;
import com.example.ruburger.model.AddOns;
import com.example.ruburger.model.Bread;
import com.example.ruburger.globaldata.OrderSingleton;
import com.example.ruburger.globaldata.ComboSingleton;

import java.text.NumberFormat;
import java.util.ArrayList;

/**
 * @author Natalia Peguero, Olivia Kamau
 * This burger controller managages the logic for the burger selection
 */
public class BurgerController extends AppCompatActivity {
    private RadioGroup pattyGroup, breadGroup;
    private CheckBox lettuce, tomato, onion, avocado, cheese;
    private ImageButton minusBtn, plusBtn;
    private TextView quantityText, priceText;
    private Button addToCartBtn, makeItCombo;
    private int quantity = 1;
    private boolean isDoublePatty = false;
    private Bread selectedBread;
    private ArrayList<AddOns> selectedAddOns = new ArrayList<>();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();


    /**
     * Creates the view for the burger and handles the view switches for other buttons
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_burger);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.title), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom()
            );
            return insets;
        });

        initializeButtons();
        setupListeners();
        goToCombo();
        updatePrice();

        LinearLayout menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v -> { Intent intent = new Intent(this, MainActivity.class); startActivity(intent); finish(); });

        LinearLayout cartButton = findViewById(R.id.cartButton);
        LinearLayout ordersButton = findViewById(R.id.ordersButton);

        cartButton.setOnClickListener(v -> { Intent intent = new Intent(this, CurrentOrderController.class); startActivity(intent);});

        ordersButton.setOnClickListener(v -> { Intent intent = new Intent(this, PlacedOrderController.class); startActivity(intent); });
    }


    /**
     * initializes the buttons that a user can select
     */
    private void initializeButtons() {
        pattyGroup = findViewById(R.id.pattyGroup);
        breadGroup = findViewById(R.id.breadGroup);

        selectedBread = Bread.BRIOCHE;
        lettuce = findViewById(R.id.lettuce);
        tomato = findViewById(R.id.tomato);
        onion = findViewById(R.id.onion);
        avocado = findViewById(R.id.avocado);
        cheese = findViewById(R.id.cheese);

        minusBtn = findViewById(R.id.minusBtn);
        plusBtn = findViewById(R.id.plusBtn);
        quantityText = findViewById(R.id.quantityText);
        priceText = findViewById(R.id.priceText);

        addToCartBtn = findViewById(R.id.addToCart);
        makeItCombo = findViewById(R.id.makeItCombo);

        selectedBread = Bread.BRIOCHE;

    }

    /**
     * Sets ups the listeners for the buttons that a user can select
     */
    private void setupListeners() {
        pattyGroup.setOnCheckedChangeListener((group, checkedId) -> {
            isDoublePatty = (checkedId == R.id.doublePatty);
            updatePrice();
        });

        breadGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.brioche) {
                selectedBread = Bread.BRIOCHE;
            } else if (checkedId == R.id.wheat) {
                selectedBread = Bread.WHEAT_BREAD;
            } else if (checkedId == R.id.pretzel) {
                selectedBread = Bread.PRETZEL;
            }
        });

        CheckBox [] checkBoxes = {lettuce, tomato, onion, avocado, cheese};
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setOnCheckedChangeListener(((buttonView, isChecked) -> { updateAddOns(); updatePrice(); }));
        }

        minusBtn.setOnClickListener(v -> {
            if (quantity > 1) { quantity--; quantityText.setText(String.valueOf(quantity)); updatePrice(); }
        });

        plusBtn.setOnClickListener(v -> { quantity++; quantityText.setText(String.valueOf(quantity)); updatePrice(); });

        addToCartBtn.setOnClickListener(v -> { addBurgerToCart(); });

    }

    /**
     * Loads the combo view by creating a burger object and sending it to the combo singleton
     */
    private void loadComboView() {
        makeItCombo.setOnClickListener(v -> {
            updateAddOns();

            Burger burger = new Burger(selectedBread, new ArrayList<>(selectedAddOns), quantity, isDoublePatty);
            ComboSingleton.getInstance().setMainItem(burger);
            Intent intent = new Intent(this, ComboController.class);
            startActivity(intent);
        });
    }

    /**
     * Goes to the combo view
     */
    private void goToCombo() {
        loadComboView();
    }

    /**
     * Dynamically updates the price by creating a burger object
     */
    private void updatePrice() {
        Burger tempBurger = new Burger(selectedBread, selectedAddOns, quantity, isDoublePatty);
        double price = tempBurger.price();

        priceText.setText(getString(R.string.price_placeholder, currencyFormat.format(price)));
    }

    /**
     * updates the addons based off user selection
     */
    private void updateAddOns() {
        selectedAddOns.clear();

        if (lettuce.isChecked()) selectedAddOns.add(AddOns.LETTUCE);
        if (tomato.isChecked()) selectedAddOns.add(AddOns.TOMATOES);
        if (onion.isChecked()) selectedAddOns.add(AddOns.ONIONS);
        if (avocado.isChecked()) selectedAddOns.add(AddOns.AVOCADO);
        if (cheese.isChecked()) selectedAddOns.add(AddOns.CHEESE);
    }


    /**
     * Adds the burger object into the current order cart view and sends a toast message
     */
    private void addBurgerToCart() {
        Burger burger = new Burger(selectedBread, new ArrayList<>(selectedAddOns), quantity, isDoublePatty);

        OrderSingleton.getInstance().addItem(burger);

        Toast.makeText(this, "Burger added to your order!", Toast.LENGTH_SHORT).show();

    }

}
