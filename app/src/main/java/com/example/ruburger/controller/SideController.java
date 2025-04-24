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


/**
 * @author Natalia Peguero, Olivia Kamau
 * Controller class for the side item selection screen. Handles user interactions for selecting a side.
 */
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

    /**
     * Initializes the activity, sets up the UI and all the event listeners.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
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


    /**
     * Initializes the UI buttons and text components.
     */
    private void initializeButtons() {

        minusBtn = findViewById(R.id.minusBtn);
        plusBtn = findViewById(R.id.plusBtn);
        quantityText = findViewById(R.id.quantityText);
        priceText = findViewById(R.id.priceText);
        sizeSpinner = findViewById(R.id.sizeSpinner);
        sideSpinner = findViewById(R.id.sideSpinner);
        addToCartBtn = findViewById(R.id.addToCartBtn);

    }

    /**
     * Sets up all the listeners for user interaction (for the dropdown and the button clicks for quantity).
     */
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

    /**
     * Returns the selected side from the spinner.
     * @return  the selected side.
     */
    private Side getSide() {
        return Side.valueOf(sideSpinner.getSelectedItem().toString());
    }

    /**
     * Returns the selected size from the spinner.
     * @return  the chosen size.
     */
    private Size getSize() {
        return Size.valueOf(sizeSpinner.getSelectedItem().toString());
    }


    /**
     * Updates teh displayed price based on the side, size and quantity chosen.
     */
    private void updatePrice() {
        Side selectedSide = getSide();
        Size selectedSize = getSize();

        Sides sides = new Sides(quantity, selectedSide, selectedSize);
        double price = sides.price();
        priceText.setText(getString(R.string.price_placeholder, currencyFormat.format(price)));

    }

    /**
     * Adds the selected side item to the cart (the OrderSingleton) and shows a confirmation message.
     */
    private void addSideToCart() {
        Side selectedSide = getSide();
        Size selectedSize = getSize();

        Sides sides = new Sides(quantity, selectedSide, selectedSize);
        OrderSingleton.getInstance().addItem(sides);
        Toast.makeText(this, "Side added to your order!", Toast.LENGTH_SHORT).show();
    }

}

