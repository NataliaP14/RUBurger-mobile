package com.example.ruburger.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ruburger.R;
import com.example.ruburger.model.Size;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SideController extends AppCompatActivity {
    private Spinner sideSpinner;
    private Spinner sizeSpinner;
    private ImageButton minusBtn, plusBtn;
    private TextView quantityText, priceText;
    private Button addToCartBtn;
    private int quantity = 1;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_side);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeButtons();
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
            Intent intent = new Intent(this, CurrentOrderController.class); // Replace with actual cart screen class
            startActivity(intent);
        });

        ordersButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, PlacedOrderController.class); // Replace with actual orders screen class
            startActivity(intent);
        });
    }


    private void initializeButtons() {

        minusBtn = findViewById(R.id.minusBtn);
        plusBtn = findViewById(R.id.plusBtn);
        quantityText = findViewById(R.id.quantityText);
        priceText = findViewById(R.id.priceText);
        sizeSpinner = findViewById(R.id.sizeSpinner);
        sideSpinner = findViewById(R.id.sideSpinner);

    }

    private void setupListeners() {

        Size[] sizes = Size.values();
        List<String> sizeList = new ArrayList<>();
        for (Size size : sizes) {
            sizeList.add(size.name());
        }

        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sizeList);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);
        sizeSpinner.setSelection(Arrays.asList(sizes).indexOf(Size.MEDIUM));




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

    }

    private void updatePrice() {


    }


}

