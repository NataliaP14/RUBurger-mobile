package com.example.ruburger.controller;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
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
import com.example.ruburger.globaldata.OrderSingleton;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

/**
 * @author Natalia Peguero, Olivia Kamau
 * This beverage controller manages the logic for the beverages
 */
public class BeverageController extends AppCompatActivity {
    private TextView priceText;
    private Button addToCartBtn;
    private BeverageAdapter adapter;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

    /**
     * Sets up the view for beverage and the other view switches
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_beverage);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.title), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom()
            );
            return insets;
        });

        RecyclerView recyclerView = findViewById(R.id.beverageRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<Flavor> allFlavors = Arrays.asList(Flavor.values());

        adapter = new BeverageAdapter(this, allFlavors, this::showBeverageDialog, () -> {});
        recyclerView.setAdapter(adapter);

        findViewById(R.id.menuButton).setOnClickListener(v -> { startActivity(new Intent(this, MainActivity.class)); finish(); });

        findViewById(R.id.cartButton).setOnClickListener(v -> { startActivity(new Intent(this, CurrentOrderController.class)); });

        findViewById(R.id.ordersButton).setOnClickListener(v -> { startActivity(new Intent(this, PlacedOrderController.class)); });
    }

    /**
     * This is the popup for the beverage dialog
     * @param flavor the flavor chosen
     * @param quantity quantity
     * @param size size
     */
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
        updatePrice(qty[0], selectedSize[0], flavor, priceText);

        sizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            /**
             * For item selection, for the size of beverage
             * @param parent The AdapterView where the selection happened
             * @param view The view within the AdapterView that was clicked
             * @param pos The position of the view in the adapter
             * @param id The row id of the item that is selected
             */
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) { selectedSize[0] = Size.values()[pos]; updatePrice(qty[0], selectedSize[0], flavor, priceText); }

            /**
             * Contains no selected item
             * @param parent The AdapterView that now contains no selected item.
             */
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        plus.setOnClickListener(v -> { qty[0]++; quantityText.setText(String.valueOf(qty[0])); updatePrice(qty[0], selectedSize[0], flavor, priceText);
        });
        minus.setOnClickListener(v -> {
            if (qty[0] > 1) {qty[0]--; quantityText.setText(String.valueOf(qty[0])); updatePrice(qty[0], selectedSize[0], flavor, priceText); }
        });
        dialog.show();
        addToCartBtn.setOnClickListener(v -> { Beverage beverage = new Beverage(qty[0], selectedSize[0], flavor); OrderSingleton.getInstance().addItem(beverage); Toast.makeText(this, "Beverage added to your order!", Toast.LENGTH_SHORT).show(); dialog.dismiss();
        });
    }

    /**
     * Updates the price of beverage dynamically
     * @param quantity the quantity
     * @param size the size
     * @param flavor the flavor
     * @param priceText the price text
     */
    private void updatePrice(int quantity, Size size, Flavor flavor, TextView priceText) {
        Beverage beverage = new Beverage(quantity, size, flavor);
        double price = beverage.price();
        priceText.setText(getString(R.string.price_placeholder, currencyFormat.format(price)));
    }
}