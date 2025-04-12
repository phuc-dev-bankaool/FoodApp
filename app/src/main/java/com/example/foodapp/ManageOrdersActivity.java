package com.example.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;
import java.util.List;

public class ManageOrdersActivity extends AppCompatActivity {
    private TextView adminWelcomeText;
    private ImageButton rollBackButton, logoutButton;
    private TextInputEditText orderIdSearch;
    private RecyclerView recyclerViewOrders;
    private OrderAdapter orderAdapter;
    private DatabaseHelper databaseHelper;
    private User loggedInUser;
    private List<Order> allOrders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_orders);

        loggedInUser = getIntent().getParcelableExtra("loggedInAdmin");
        if (loggedInUser == null || !loggedInUser.getRole().equals("admin")) {
            Toast.makeText(this, "Invalid admin user", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        adminWelcomeText = findViewById(R.id.adminWelcomeText);
        rollBackButton = findViewById(R.id.rollBackButton);
        logoutButton = findViewById(R.id.logoutButton);
        orderIdSearch = findViewById(R.id.orderIdSearch);
        recyclerViewOrders = findViewById(R.id.recyclerViewOrders);

        adminWelcomeText.setText("Welcome, " + loggedInUser.getName());
        databaseHelper = new DatabaseHelper(this);
        recyclerViewOrders.setLayoutManager(new LinearLayoutManager(this));

        loadOrders();

        rollBackButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, AdminHomeActivity.class);
            intent.putExtra("loggedInAdmin", loggedInUser);
            startActivity(intent);
            finish();
        });

        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        orderIdSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                filterOrders(s.toString());
            }
        });
    }

    private void loadOrders() {
        allOrders = databaseHelper.getAllOrders();
        orderAdapter = new OrderAdapter(this, allOrders);
        recyclerViewOrders.setAdapter(orderAdapter);
        updateEmptyState();
    }

    private void filterOrders(String query) {
        List<Order> filteredOrders = new ArrayList<>();
        if (query.isEmpty()) {
            filteredOrders.addAll(allOrders);
        } else {
            try {
                int orderId = Integer.parseInt(query);
                for (Order order : allOrders) {
                    if (order.getId() == orderId) {
                        filteredOrders.add(order);
                    }
                }
            } catch (NumberFormatException e) {
                // Ignore invalid input
            }
        }
        orderAdapter.updateOrders(filteredOrders);
        updateEmptyState();
    }

    private void updateEmptyState() {
        if (orderAdapter.getItemCount() == 0) {
            recyclerViewOrders.setVisibility(View.GONE);
            findViewById(R.id.emptyOrdersView).setVisibility(View.VISIBLE);
        } else {
            recyclerViewOrders.setVisibility(View.VISIBLE);
            findViewById(R.id.emptyOrdersView).setVisibility(View.GONE);
        }
    }
}