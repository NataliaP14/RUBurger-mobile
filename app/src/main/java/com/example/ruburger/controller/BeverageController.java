package com.example.ruburger.controller;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ruburger.R;
import com.example.ruburger.adapter.BeverageAdapter;
import com.example.ruburger.model.Beverage;
import com.example.ruburger.model.Flavor;
import com.example.ruburger.model.Size;

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



        RecyclerView recyclerView = findViewById(R.id.beverageRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Flavor> allFlavors = Arrays.asList(Flavor.values());

        adapter = new BeverageAdapter(this, allFlavors, this::showBeverageDialog, () -> {});
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

    private void showBeverageDialog(Flavor flavor, int quantity, Size size) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_beverage_popup);

        TextView flavorTitle = dialog.findViewById(R.id.dialogFlavorTitle);
        Spinner sizeSpinner = dialog.findViewById(R.id.dialogSizeSpinner);
        TextView quantityText = dialog.findViewById(R.id.dialogQuantity);
        ImageButton plus = dialog.findViewById(R.id.dialogPlus);
        ImageButton minus = dialog.findViewById(R.id.dialogMinus);
        TextView priceText = dialog.findViewById(R.id.priceText);
        Button addToCartBtn = dialog.findViewById(R.id.dialogAddBtn);

        flavorTitle.setText(flavor.name());
        quantityText.setText(String.valueOf(quantity));

        ArrayAdapter<Size> sizeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Size.values());
        sizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sizeSpinner.setAdapter(sizeAdapter);
        sizeSpinner.setSelection(size.ordinal());

        final int[] qty = { quantity };
        final Size[] selectedSize = { size };

        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedSize[0] = Size.values()[pos];
            }
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        plus.setOnClickListener(v -> {
            qty[0]++;
            quantityText.setText(String.valueOf(qty[0]));
        });

        minus.setOnClickListener(v -> {
            if (qty[0] > 1) {
                qty[0]--;
                quantityText.setText(String.valueOf(qty[0]));

            }
        });

        dialog.show();
    }

    private void updatePrice(Flavor flavor, int i, Size size) {
    }
}
