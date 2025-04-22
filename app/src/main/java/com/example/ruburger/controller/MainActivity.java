package com.example.ruburger.controller;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ruburger.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        LinearLayout burgerBox = findViewById(R.id.burgerBox);
        LinearLayout sandwichBox = findViewById(R.id.sandwichBox);
        LinearLayout beverageBox = findViewById(R.id.beverageBox);
        LinearLayout sideBox = findViewById(R.id.sideBox);
        Button cartButton = findViewById(R.id.cartButton);
        Button ordersButton = findViewById(R.id.ordersButton);


        burgerBox.setOnClickListener(v -> startActivity(new Intent(this, BurgerController.class)));
        sandwichBox.setOnClickListener(v -> startActivity(new Intent(this, SandwichController.class)));
        beverageBox.setOnClickListener(v -> startActivity(new Intent(this, BeverageController.class)));
        sideBox.setOnClickListener(v -> startActivity(new Intent(this, SideController.class)));

        cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CurrentOrderController.class);
            startActivity(intent);
        });

        ordersButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, PlacedOrderController.class);
            startActivity(intent);
        });
    }
}