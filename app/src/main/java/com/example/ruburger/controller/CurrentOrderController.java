package com.example.ruburger.controller;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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

public class CurrentOrderController extends AppCompatActivity {

    private ListView orderItemsList;
    private Button removeItemBtn, placeOrderBtn;
    private TextView subtotal, salesTax, totalAmount;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    private ArrayAdapter<String> orderAdapter;
    private List<String> orderDisplayList;
    private int selectedItemPosition = -1;
    private static final double NJ_TAX_RATE = 0.06625;




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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        }

        initializeViews();
        setupListeners();
        updateOrderDisplay();

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


    @Override
    protected void onResume() {
        super.onResume();
        updateOrderDisplay();
    }

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


    private void setupListeners() {
        orderItemsList.setOnItemClickListener((parent, view, position, id) -> {
            selectedItemPosition = position;
        });

        removeItemBtn.setOnClickListener(v -> removeSelectedItem());

        placeOrderBtn.setOnClickListener(v -> placeOrder());

    }

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

    private void updateOrderTotals() {
        OrderSingleton orderSingleton = OrderSingleton.getInstance();
        double subtotalValue = orderSingleton.getOrderTotal();
        double taxValue = subtotalValue * NJ_TAX_RATE;
        double totalValue = subtotalValue + taxValue;

        subtotal.setText(getString(R.string.subtotal, currencyFormat.format(subtotalValue)));
        salesTax.setText(getString(R.string.sales_tax, currencyFormat.format(taxValue)));
        totalAmount.setText(getString(R.string.total, currencyFormat.format(totalValue)));
    }

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
