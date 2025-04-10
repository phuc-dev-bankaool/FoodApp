package com.example.foodapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserHomeActivity extends AppCompatActivity {
    private ImageButton logoutBtn, cartButton;
    private RecyclerView foodRecyclerView;
    private FoodAdapter foodAdapter;
    private DatabaseHelper databaseHelper;
    private User loggedInUser;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);

        loggedInUser = (User) getIntent().getParcelableExtra("loggedInUser");
        Log.d("UserHomeActivity", "Logged in user: " + loggedInUser);
        if (loggedInUser == null) {
            Intent intent = new Intent(UserHomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        TextView welcomeTextView = findViewById(R.id.userWelcomeText);
        welcomeTextView.setText("Welcome, " + loggedInUser.getName());

        logoutBtn = findViewById(R.id.logoutButton);
        cartButton = findViewById(R.id.cartButton);

        logoutBtn.setOnClickListener(v -> logoutUser());
        cartButton.setOnClickListener(v -> {
            // TODO: Chuyển sang activity giỏ hàng (tạo sau nếu cần)
            Toast.makeText(this, "Cart clicked (to be implemented)", Toast.LENGTH_SHORT).show();
        });

        foodRecyclerView = findViewById(R.id.foodRecyclerView);
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);
        List<Food> foodList = databaseHelper.getAllFoods();

        foodAdapter = new FoodAdapter(this, foodList, new FoodAdapter.OnAddToCartListener() {
            @Override
            public void onAddToCart(Food food) {
                boolean success = databaseHelper.addToCart(loggedInUser.getId(), food.getId(), 1);
                if (success) {
                    Toast.makeText(UserHomeActivity.this, food.getName() + " added to cart", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserHomeActivity.this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
                }
            }
        });
        foodRecyclerView.setAdapter(foodAdapter);
    }

    private void logoutUser() {
        Intent intent = new Intent(UserHomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}