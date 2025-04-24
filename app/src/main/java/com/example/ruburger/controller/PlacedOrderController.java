package com.example.ruburger.controller;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.appcompat.app.AlertDialog;

import com.example.ruburger.R;
import com.example.ruburger.globaldata.OrderSingleton;
import com.example.ruburger.model.MenuItem;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;


public class PlacedOrderController extends AppCompatActivity {

    private Spinner orderDropdown;
    private ListView orderItemsList;
    private Button cancelOrderBtn;
    private TextView totalAmount;
    private ArrayAdapter<String> orderAdapter;
    private List<String> orderDisplayList;
    private ArrayAdapter<String> dropdownAdapter;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    private int currentSelectedOrderIndex = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_placed_order);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.title), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(v.getPaddingLeft(), systemBars.top, v.getPaddingRight(), v.getPaddingBottom()
            );
            return insets;
        });


        initializeViews();
        setupListeners();
        populateOrderDropdown();

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


    @Override
    protected void onResume() {
        super.onResume();
        populateOrderDropdown();
    }

    private void initializeViews() {
        orderDropdown = findViewById(R.id.orderDropdown);
        orderItemsList = findViewById(R.id.orderItemsList);
        cancelOrderBtn = findViewById(R.id.cancelOrderBtn);
        totalAmount = findViewById(R.id.totalAmount);

        orderDisplayList = new ArrayList<>();
        orderAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, orderDisplayList);
        orderItemsList.setAdapter(orderAdapter);
    }

    private void setupListeners() {
        orderDropdown.setOnItemSelectedListener((new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentSelectedOrderIndex = position;
                displaySelectedOrder(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        }));

        cancelOrderBtn.setOnClickListener(v -> cancelCurrentOrder());

    }

    private void populateOrderDropdown() {
        List<Integer> orderNumbers = OrderSingleton.getInstance().getAllOrderNumbers();
        List<String> dropdownItems = new ArrayList<>();

        if (orderNumbers.isEmpty()) {
            cancelOrderBtn.setEnabled(false);
            orderDisplayList.clear();
            orderAdapter.notifyDataSetChanged();
            totalAmount.setText(getString(R.string.total, currencyFormat.format(0.0)));
        } else {
            for (Integer orderNumber : orderNumbers) {
                dropdownItems.add(String.valueOf(orderNumber + 1));
            }
            cancelOrderBtn.setEnabled(true);
        }

        dropdownAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, dropdownItems);
        dropdownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        orderDropdown.setAdapter(dropdownAdapter);

        if (!orderNumbers.isEmpty()) {
            orderDropdown.setSelection(0);
            displaySelectedOrder(0);
        }
    }

    private void displaySelectedOrder(int position) {
        OrderSingleton orderSingleton = OrderSingleton.getInstance();
        List<Integer> orderNumbers = orderSingleton.getAllOrderNumbers();

        if (orderNumbers.isEmpty() || position >= orderNumbers.size()) {
            return;
        }

        int orderNumber = orderNumbers.get(position);
        List<MenuItem> orderItems = orderSingleton.getPlacedOrder(orderNumber);

        orderDisplayList.clear();

        if (orderItems != null && !orderItems.isEmpty()) {
            for (MenuItem item : orderItems) {
                orderDisplayList.add(item.toString());
            }

            double subtotalValue = orderSingleton.getOrderSubtotal(orderNumber);
            double taxValue = orderSingleton.getOrderTax(orderNumber);
            double totalValue = orderSingleton.getTotalWithTax(orderNumber);

            totalAmount.setText(getString(R.string.total, currencyFormat.format(totalValue)));
        } else {
            totalAmount.setText(getString(R.string.total, currencyFormat.format(0.0)));
        }
        orderAdapter.notifyDataSetChanged();
    }

    private void cancelCurrentOrder() {
        OrderSingleton orderSingleton = OrderSingleton.getInstance();
        List<Integer> orderNumbers = orderSingleton.getAllOrderNumbers();

        if (orderNumbers.isEmpty() || currentSelectedOrderIndex >= orderNumbers.size()) {
            Toast.makeText(this, "No order to cancel", Toast.LENGTH_SHORT).show();
            return;
        }

        int orderNumber = orderNumbers.get(currentSelectedOrderIndex);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cancel Order");
        builder.setMessage("Are you sure you want to cancel Order #" + (orderNumber + 1) + "?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            boolean success = removeOrderFromSingleton(orderNumber);

            if (success) {
                Toast.makeText(this, "Order #" + (orderNumber + 1) + " cancelled", Toast.LENGTH_SHORT).show();
                populateOrderDropdown();

                if (!orderSingleton.getAllOrderNumbers().isEmpty()) {
                    orderDropdown.setSelection(0);
                    displaySelectedOrder(0);
                } else {
                    orderDisplayList.clear();
                    orderAdapter.notifyDataSetChanged();
                    totalAmount.setText(getString(R.string.total, currencyFormat.format(0.0)));
                }
            }
        });

        builder.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private boolean removeOrderFromSingleton(int orderNumber) {
        OrderSingleton orderSingleton = OrderSingleton.getInstance();
        List<Integer> allOrderNumbers = orderSingleton.getAllOrderNumbers();

        if (allOrderNumbers.size() == 1 && allOrderNumbers.get(0) == orderNumber) {
            orderSingleton.clearAllPlacedOrders();
            return true;
        }

        int originalCurrentOrderNumber = orderSingleton.getCurrentOrderNumber();

        List<List<MenuItem>> ordersToKeep = new ArrayList<>();
        List<Integer> orderNumbersToKeep = new ArrayList<>();

        for (Integer num : allOrderNumbers) {
            if (num != orderNumber) {
                List<MenuItem> items = orderSingleton.getPlacedOrder(num);
                if (items != null && !items.isEmpty()) {
                    ordersToKeep.add(new ArrayList<>(items));
                    orderNumbersToKeep.add(num);
                }
            }
        }

        orderSingleton.clearAllPlacedOrders();
        orderSingleton.resetCurrentOrderNumber(0);

        for (int i = 0; i < ordersToKeep.size(); i++) {
            orderSingleton.setCurrentOrderNumber(orderNumbersToKeep.get(i));
            orderSingleton.setCurrentOrder(ordersToKeep.get(i));
            orderSingleton.placeOrder();
        }

        orderSingleton.setCurrentOrderNumber(originalCurrentOrderNumber);
        orderSingleton.setCurrentOrder(new ArrayList<>());
        orderSingleton.updateOrderTotal();

        return true;
    }

}
