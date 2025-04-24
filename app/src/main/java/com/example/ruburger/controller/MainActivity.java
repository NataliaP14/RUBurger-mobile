package com.example.ruburger.controller;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ruburger.R;

/**
 * @author Natalia Peguero, Olivia Kamau
 * This main activity class sets up the main menu view for the application, this is the root point where you actually run the application
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Handles the creating of the view for the main menu class, and sets up the switching of the view based off the bottom nav buttons
     * and the main select buttons such as Burger, Sandwich, Beverage, and Side
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.title), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom()
            );
            return insets;
        });

        LinearLayout burgerBox = findViewById(R.id.burgerBox);
        LinearLayout sandwichBox = findViewById(R.id.sandwichBox);
        LinearLayout beverageBox = findViewById(R.id.beverageBox);
        LinearLayout sideBox = findViewById(R.id.sideBox);
        LinearLayout cartButton = findViewById(R.id.cartButton);
        LinearLayout ordersButton = findViewById(R.id.ordersButton);

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