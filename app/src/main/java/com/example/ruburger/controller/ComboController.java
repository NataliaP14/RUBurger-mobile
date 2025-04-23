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
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ruburger.R;
import com.example.ruburger.model.Beverage;
import com.example.ruburger.model.Side;
import com.example.ruburger.model.Flavor;

public class ComboController extends AppCompatActivity {
    private Spinner sideSpinner, drinkSpinner;
    private ImageView sideIcon, drinkIcon;
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

    }
}