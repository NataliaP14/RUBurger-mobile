package com.example.ruburger.controller;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.ruburger.R;
import com.example.ruburger.globaldata.OrderSingleton;
import com.example.ruburger.model.MenuItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


/**
 * This is a controller class for managing the Current Order screen.
 * It handles the display of current items in the order, removing specific order
 * items and placing the order.
 * @author  Natalia Peguero, Olivia Kamau
 */
public class CurrentOrderController extends AppCompatActivity {
    private ListView orderItemsList;
    private Button removeItemBtn, placeOrderBtn;
    private TextView subtotal, salesTax, totalAmount;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    private ArrayAdapter<String> orderAdapter;
    private List<String> orderDisplayList;
    private int selectedItemPosition = -1;
    private static final double NJ_TAX_RATE = 0.06625;


    /**
     * Sets up the view, initializes the UI components, listeners and updates the order display.
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_current_order);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.title), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom()
            );
            return insets;
        });

        initializeViews();
        setupListeners();
        updateOrderDisplay();

        LinearLayout menuButton = findViewById(R.id.menuButton);
        menuButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        LinearLayout cartButton = findViewById(R.id.cartButton);
        LinearLayout ordersButton = findViewById(R.id.ordersButton);

        cartButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, CurrentOrderController.class);
            startActivity(intent);
        });

        ordersButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, PlacedOrderController.class);
            startActivity(intent);
        });
    }


    /**
     * Refreshes the order display when the activity resumes.
     */
    @Override
    protected void onResume() {
        super.onResume();
        updateOrderDisplay();
    }

    /**
     * Initializes all view components and sets up the adapter for the order list.
     */
    private void initializeViews() {
        orderItemsList = findViewById(R.id.orderItemsList);
        removeItemBtn = findViewById(R.id.removeItemBtn);
        placeOrderBtn = findViewById(R.id.placeOrderBtn);
        subtotal = findViewById(R.id.subtotal);
        salesTax = findViewById(R.id.salesTax);
        totalAmount = findViewById(R.id.totalAmount);

        orderDisplayList = new ArrayList<>();
        orderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_activated_1, orderDisplayList);
        orderItemsList.setAdapter(orderAdapter);
        orderItemsList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    /**
     * Sets up listeners for the list item selection and button clicks.
     */
    private void setupListeners() {
        orderItemsList.setOnItemClickListener((parent, view, position, id) -> {
            selectedItemPosition = position;
        });

        removeItemBtn.setOnClickListener(v -> removeSelectedItem());

        placeOrderBtn.setOnClickListener(v -> placeOrder());

    }

    /**
     * Updates the list of order items displayed and recalculates the totals.
     */
    private void updateOrderDisplay() {

        OrderSingleton orderSingleton = OrderSingleton.getInstance();
        List<MenuItem> currentOrder = orderSingleton.getCurrentOrder();

        orderDisplayList.clear();

        if (currentOrder.isEmpty()) {
            removeItemBtn.setEnabled(false);
            placeOrderBtn.setEnabled(false);
        } else {
            removeItemBtn.setEnabled(true);
            placeOrderBtn.setEnabled(true);

            for (MenuItem item : currentOrder) {
                orderDisplayList.add(item.toString());
            }
        }

        orderAdapter.notifyDataSetChanged();
        updateOrderTotals();

    }

    /**
     * Calculates and updates the subtotal, tax and total amount for the current order
     */
    private void updateOrderTotals() {
        OrderSingleton orderSingleton = OrderSingleton.getInstance();
        double subtotalValue = orderSingleton.getOrderTotal();
        double taxValue = subtotalValue * NJ_TAX_RATE;
        double totalValue = subtotalValue + taxValue;

        subtotal.setText(getString(R.string.subtotal, currencyFormat.format(subtotalValue)));
        salesTax.setText(getString(R.string.sales_tax, currencyFormat.format(taxValue)));
        totalAmount.setText(getString(R.string.total, currencyFormat.format(totalValue)));
    }

    /**
     * Removes the selected item from the current order.
     */
    public void removeSelectedItem() {
        if (selectedItemPosition >= 0 && !OrderSingleton.getInstance().isEmpty()) {
            OrderSingleton orderSingleton = OrderSingleton.getInstance();
            if (orderSingleton.removeItem(selectedItemPosition)) {
                Toast.makeText(this, "Item removed", Toast.LENGTH_SHORT).show();
                selectedItemPosition = -1;
                updateOrderDisplay();
            }
        } else {
            Toast.makeText(this, "Please select an item to remove", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Places the current order if it's not empty, clears the current order and updates the totals.
     */
    private void placeOrder() {
        OrderSingleton orderSingleton = OrderSingleton.getInstance();

        if (!orderSingleton.isEmpty()) {

            // gonna add the part where we save the order to the array list (for the placed orders) here

            orderSingleton.placeOrder();
            orderSingleton.getCurrentOrder().clear();
            orderSingleton.updateOrderTotal();

            selectedItemPosition = -1;
            updateOrderDisplay();

            Toast.makeText(this, "Order #" + OrderSingleton.getInstance().getCurrentOrderNumber() + " placed successfully!", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
        }
    }

}
