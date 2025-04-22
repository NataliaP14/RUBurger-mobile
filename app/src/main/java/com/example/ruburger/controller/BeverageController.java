package com.example.ruburger.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ruburger.R;
import com.example.ruburger.adapter.BeverageAdapter;
import com.example.ruburger.model.Beverage;
import com.example.ruburger.model.Flavor;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

public class BeverageController extends AppCompatActivity {

    private TextView priceText;
    private Button addToCartBtn;
    private BeverageAdapter adapter;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_beverage);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        priceText = findViewById(R.id.priceText);
        addToCartBtn = findViewById(R.id.addToCartBtn);

        RecyclerView recyclerView = findViewById(R.id.beverageRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Flavor> allFlavors = Arrays.asList(Flavor.values());

        adapter = new BeverageAdapter(this, allFlavors, this::updatePrice);
        recyclerView.setAdapter(adapter);


        findViewById(R.id.menuButton).setOnClickListener(v -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });

        findViewById(R.id.cartButton).setOnClickListener(v -> {
            startActivity(new Intent(this, CurrentOrderController.class));
        });

        findViewById(R.id.ordersButton).setOnClickListener(v -> {
            startActivity(new Intent(this, PlacedOrderController.class));
        });

   
    }

    private void updatePrice() {
    }
}
