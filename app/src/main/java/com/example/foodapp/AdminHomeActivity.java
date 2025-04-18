package com.example.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class AdminHomeActivity extends AppCompatActivity {
    CardView manageUsersCard,manageProductsCard, manageOrdersCard;
    ImageButton logoutBtn;
    User loggedAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);
        loggedAdmin = getIntent().getParcelableExtra("loggedInAdmin");
        Log.d("DEBUG", "loggedAdmin: " + loggedAdmin);
        if (loggedAdmin == null) {
            // Xử lý trường hợp người dùng chưa đăng nhập
            Intent intent = new Intent(AdminHomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Hiển thị thông báo chào mừng cho người dùng đã đăng nhập
            TextView welcomeTextView = findViewById(R.id.adminWelcomeText);
            welcomeTextView.setText("Welcome, " + loggedAdmin.getName());
        }
        // Ánh xạ view
        manageUsersCard = findViewById(R.id.manageUsersCard);
        manageProductsCard = findViewById(R.id.manageProductsCard);
        manageOrdersCard = findViewById(R.id.manageOrdersCard);
        logoutBtn = findViewById(R.id.logoutButton);
        logoutBtn.setOnClickListener(v -> {
            logoutUser();
        });
        // Bắt sự kiện click
        manageUsersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, ManageUsersActivity.class);
                intent.putExtra("loggedInAdmin", loggedAdmin);
                startActivity(intent);
                finish();
            }
        });

        manageProductsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, ManageProductsActivity.class);
                intent.putExtra("loggedInAdmin", loggedAdmin);
                startActivity(intent);
                finish();
            }
        });

        manageOrdersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminHomeActivity.this, ManageOrdersActivity.class);
                intent.putExtra("loggedInAdmin", loggedAdmin);
                startActivity(intent);
                finish();
            }
        });
    }

    private void logoutUser() {
        Intent intent = new Intent(AdminHomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
