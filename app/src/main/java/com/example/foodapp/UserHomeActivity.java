package com.example.foodapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserHomeActivity extends AppCompatActivity {
    private ImageButton logoutBtn;
    private RecyclerView foodRecyclerView;
    private FoodAdapter foodAdapter;
    private DatabaseHelper databaseHelper;
    User loggedInUser;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Gọi phương thức onCreate của lớp cha
        super.onCreate(savedInstanceState);
        // Đặt layout cho hoạt động
        setContentView(R.layout.activity_home_user);

        // Lấy thông tin người dùng đã đăng nhập từ Intent
        loggedInUser = (User) getIntent().getParcelableExtra("loggedInUser");
        Log.d("UserHomeActivity", "Logged in user: " + loggedInUser);
        // Kiểm tra xem người dùng đã đăng nhập hay chưa, nếu chưa thì kết thúc hoạt động
        if (loggedInUser == null) {
            // Xử lý trường hợp người dùng chưa đăng nhập
            Intent intent = new Intent(UserHomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            // Hiển thị thông báo chào mừng cho người dùng đã đăng nhập
            TextView welcomeTextView = findViewById(R.id.userWelcomeText);
            welcomeTextView.setText("Welcome, " + loggedInUser.getName());
        }
        logoutBtn = findViewById(R.id.logoutButton);

        // Logout button
        logoutBtn.setOnClickListener(v -> {
            logoutUser();
        });

        // Hiển thị danh sách món ăn
        foodRecyclerView = findViewById(R.id.foodRecyclerView);

        // Sử dụng LinearLayoutManager để hiển thị danh sách theo chiều dọc
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo adapter và gán cho RecyclerView
        databaseHelper = new DatabaseHelper(this);

        List<Food> foodList = databaseHelper.getAllFoods();
        // Khởi tạo adapter
        foodAdapter = new FoodAdapter(this, foodList);
        // Gán adapter cho RecyclerView
        foodRecyclerView.setAdapter(foodAdapter);
    }
    // Xử lý sự kiện khi người dùng nhấn nút đăng xuất
    private void logoutUser() {
        Intent intent = new Intent(UserHomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
