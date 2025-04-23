package com.example.ruburger.controller;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ruburger.R;
import com.example.ruburger.model.AddOns;
import com.example.ruburger.model.Bread;
import com.example.ruburger.model.Sandwich;
import com.example.ruburger.model.Protein;
import com.example.ruburger.globaldata.OrderSingleton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SandwichController extends AppCompatActivity {

    private Spinner breadSpinner;
    private CheckBox lettuce, tomato, onion, avocado, cheese;
    private ImageButton minusBtn, plusBtn;
    private TextView quantityText, priceText;
    private Button addToCartBtn, makeItCombo;
    private Bread selectedBread;
    private Protein selectedProtein;
    private int quantity = 1;
    private ArrayList<AddOns> selectedAddOns = new ArrayList<>();
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();


    private Bread getSelectedBread() {
        return Bread.valueOf(breadSpinner.getSelectedItem().toString());
    }

    private Protein getSelectedProtein() {
        RadioGroup proteinGroup = findViewById(R.id.proteinGroup);
        int selectedId = proteinGroup.getCheckedRadioButtonId();

        if (selectedId == R.id.roastBeef) {
            return Protein.ROAST_BEEF;
        } else if (selectedId == R.id.salmon) {
            return Protein.SALMON;
        } else {
            return Protein.CHICKEN;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sandwich);

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
            Intent intent = new Intent(this, CurrentOrderController.class); // Replace with actual cart screen class
            startActivity(intent);
        });

        ordersButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, PlacedOrderController.class); // Replace with actual orders screen class
            startActivity(intent);
        });
    }


    private void initializeButtons() {

        breadSpinner = findViewById(R.id.breadSpinner);
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
        //I wanted to set the bread values up to see how it would look pls dont be mad

        Bread[] breads = Bread.values();
        List<String> breadList = new ArrayList<>();
        for (Bread b : breads) {
            breadList.add(b.name());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, breadList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        breadSpinner.setAdapter(adapter);
        breadSpinner.setSelection(Arrays.asList(breads).indexOf(Bread.BRIOCHE));


        breadSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updatePrice();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        RadioGroup proteinGroup = findViewById(R.id.proteinGroup);
        proteinGroup.setOnCheckedChangeListener((group, checkedId) -> updatePrice());


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
            addSandwichToCart();
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

        selectedBread = getSelectedBread();

        selectedProtein = getSelectedProtein();

        updateAddOns();

        Sandwich tempSandwich = new Sandwich(quantity, selectedBread, selectedProtein, selectedAddOns);

        double price = tempSandwich.price();
        priceText.setText(currencyFormat.format(price));
    }

    private void updateAddOns() {
        selectedAddOns.clear();

        if (lettuce.isChecked()) selectedAddOns.add(AddOns.LETTUCE);
        if (tomato.isChecked()) selectedAddOns.add(AddOns.TOMATOES);
        if (onion.isChecked()) selectedAddOns.add(AddOns.ONIONS);
        if (avocado.isChecked()) selectedAddOns.add(AddOns.AVOCADO);
        if (cheese.isChecked()) selectedAddOns.add(AddOns.CHEESE);
    }


    private void addSandwichToCart() {

        selectedBread = getSelectedBread();

        selectedProtein = getSelectedProtein();

        updateAddOns();

        Sandwich sandwich = new Sandwich(quantity, selectedBread, selectedProtein, selectedAddOns);
        OrderSingleton.getInstance().addItem(sandwich);
    }

}
