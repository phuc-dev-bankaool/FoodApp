package com.example.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class UserOrderActivity extends AppCompatActivity {
    private TextView orderIdText, orderDateText, orderTotalText;
    private Button backToHomeButton;
    private User loggedInUser;
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_user);

        loggedInUser = getIntent().getParcelableExtra("loggedInUser");
        order = getIntent().getParcelableExtra("order");

        if (loggedInUser == null || order == null) {
            finish();
            return;
        }

        orderIdText = findViewById(R.id.orderIdText);
        orderDateText = findViewById(R.id.orderDateText);
        orderTotalText = findViewById(R.id.orderTotalText);
        backToHomeButton = findViewById(R.id.backToHomeButton);

        // Hiển thị thông tin đơn hàng
        orderIdText.setText("Order ID: " + order.getId());
        orderDateText.setText("Date: " + order.getOrderDate());
        orderTotalText.setText(String.format("Total: %.0f $", order.getTotal()));

        // Nút quay lại UserHomeActivity
        backToHomeButton.setOnClickListener(v -> {
            Intent intent = new Intent(UserOrderActivity.this, UserHomeActivity.class);
            intent.putExtra("loggedInUser", loggedInUser);
            startActivity(intent);
            finish();
        });
    }
}