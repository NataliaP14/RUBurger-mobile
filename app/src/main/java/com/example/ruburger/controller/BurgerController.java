package com.example.ruburger.controller;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
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

import java.text.NumberFormat;
import java.util.ArrayList;


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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        }

        initializeButtons();
        setupListeners();
        goToCombo();
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

    }

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
            checkBox.setOnCheckedChangeListener(((buttonView, isChecked) -> {
                updateAddOns();
                updatePrice();
            }));
        }

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
            addBurgerToCart();
        });

    }


    private void loadComboView() {
        makeItCombo.setOnClickListener(v -> {
            Intent intent = new Intent(this, ComboController.class);
            startActivity(intent);
        });
    }

    private void goToCombo() {

        loadComboView();
    }

    private void updatePrice() {
        Burger tempBurger = new Burger(selectedBread, selectedAddOns, quantity, isDoublePatty);
        double price = tempBurger.price();

        priceText.setText(getString(R.string.price_placeholder, currencyFormat.format(price)));
    }

    private void updateAddOns() {
        selectedAddOns.clear();

        if (lettuce.isChecked()) selectedAddOns.add(AddOns.LETTUCE);
        if (tomato.isChecked()) selectedAddOns.add(AddOns.TOMATOES);
        if (onion.isChecked()) selectedAddOns.add(AddOns.ONIONS);
        if (avocado.isChecked()) selectedAddOns.add(AddOns.AVOCADO);
        if (cheese.isChecked()) selectedAddOns.add(AddOns.CHEESE);
    }


    private void addBurgerToCart() {
        Burger burger = new Burger(selectedBread, new ArrayList<>(selectedAddOns), quantity, isDoublePatty);

        OrderSingleton.getInstance().addItem(burger);

        Toast.makeText(this, "Burger added to your order!", Toast.LENGTH_SHORT).show();

    }




}
