package com.example.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UserOrderHistoryActivity extends AppCompatActivity {
    private RecyclerView orderRecyclerView;
    private OrderAdapter orderAdapter;
    private DatabaseHelper databaseHelper;
    private User loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history_user);

        loggedInUser = getIntent().getParcelableExtra("loggedInUser");
        if (loggedInUser == null) {
            Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        Button backToHomeButton = findViewById(R.id.backToHomeButton);
        orderRecyclerView = findViewById(R.id.orderRecyclerView);
        orderRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);
        loadOrderHistory();
        backToHomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserOrderHistoryActivity.this, UserHomeActivity.class);
            intent.putExtra("loggedInUser", loggedInUser);
            startActivity(intent);
            finish();
        });
    }

    private void loadOrderHistory() {
        List<Order> orderList = databaseHelper.getUserOrders(loggedInUser.getId());
        if (orderList.isEmpty()) {
            Toast.makeText(this, "No orders found", Toast.LENGTH_SHORT).show();
        }
        orderAdapter = new OrderAdapter(this, orderList);
        orderRecyclerView.setAdapter(orderAdapter);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, UserHomeActivity.class);
        intent.putExtra("loggedInUser", loggedInUser);
        startActivity(intent);
        finish();
    }
}