package com.example.ruburger.controller;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ImageButton;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ruburger.R;
import com.example.ruburger.model.Size;
import com.example.ruburger.model.Side;
import com.example.ruburger.model.Sides;
import com.example.ruburger.globaldata.OrderSingleton;

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
    private Side selectedSide;
    private Size selectedSize;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_side);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.title), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom()
            );
            return insets;
        });

        initializeButtons();
        setupListeners();
        updatePrice();

        LinearLayout menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class); startActivity(intent);
            finish();
        });

        LinearLayout cartButton = findViewById(R.id.cartButton);
        LinearLayout ordersButton = findViewById(R.id.ordersButton);

        cartButton.setOnClickListener(v -> { Intent intent = new Intent(this, CurrentOrderController.class);
            startActivity(intent);
        });

        ordersButton.setOnClickListener(v -> { Intent intent = new Intent(this, PlacedOrderController.class);
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
        addToCartBtn = findViewById(R.id.addToCartBtn);

    }

    private void setupListeners() {

        Side[] sides = Side.values();
        List<String> sideList = new ArrayList<>();
        for (Side side : sides) { sideList.add(side.name()); }

        ArrayAdapter<String> sideAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sideList);
        sideAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sideSpinner.setAdapter(sideAdapter);
        sideSpinner.setSelection(Arrays.asList(sides).indexOf(Side.CHIPS));

        sideSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { updatePrice(); }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        Size[] sizes = Size.values(); List<String> sizeList = new ArrayList<>(); for (Size size : sizes) { sizeList.add(size.name()); }

        ArrayAdapter<String> sizeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sizeList);
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);
        sizeSpinner.setSelection(Arrays.asList(sizes).indexOf(Size.MEDIUM));

        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { updatePrice(); }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        minusBtn.setOnClickListener(v -> { if (quantity > 1) { quantity--; quantityText.setText(String.valueOf(quantity)); updatePrice(); } });

        plusBtn.setOnClickListener(v -> { quantity++; quantityText.setText(String.valueOf(quantity)); updatePrice(); });
        addToCartBtn.setOnClickListener(v -> { addSideToCart(); });
    }

    private Side getSide() {
        return Side.valueOf(sideSpinner.getSelectedItem().toString());
    }

    private Size getSize() {
        return Size.valueOf(sizeSpinner.getSelectedItem().toString());
    }


    private void updatePrice() {
        Side selectedSide = getSide();
        Size selectedSize = getSize();

        Sides sides = new Sides(quantity, selectedSide, selectedSize);
        double price = sides.price();
        priceText.setText(getString(R.string.price_placeholder, currencyFormat.format(price)));

    }

    private void addSideToCart() {
        Side selectedSide = getSide();
        Size selectedSize = getSize();

        Sides sides = new Sides(quantity, selectedSide, selectedSize);
        OrderSingleton.getInstance().addItem(sides);
        Toast.makeText(this, "Side added to your order!", Toast.LENGTH_SHORT).show();
    }

}

