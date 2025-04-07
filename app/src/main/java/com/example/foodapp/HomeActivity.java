package com.example.foodapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeActivity extends AppCompatActivity {

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
        loggedInUser = (User) getIntent().getSerializableExtra("loggedInUser");

        // Kiểm tra xem người dùng đã đăng nhập hay chưa, nếu chưa thì kết thúc hoạt động
        if (loggedInUser == null) {
            // Xử lý trường hợp người dùng chưa đăng nhập
            finish();
            return;
        }else{
            // Hiển thị thông báo chào mừng cho người dùng đã đăng nhập
            TextView welcomeTextView = findViewById(R.id.welcomeTextView);
            welcomeTextView.setText("Welcome, " + loggedInUser.getName());
        }

        // Hiển thị danh sách món ăn
        foodRecyclerView = findViewById(R.id.foodRecyclerView);

        // Sử dụng LinearLayoutManager để hiển thị danh sách theo chiều dọc
        foodRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Khởi tạo adapter và gán cho RecyclerView
        databaseHelper = new DatabaseHelper(this);

        // Lấy danh sách món ăn từ cơ sở dữ liệu
        List<Food> foodList = databaseHelper.getAllFoods();

        // Khởi tạo adapter
        foodAdapter = new FoodAdapter(foodList);

        // Gán adapter cho RecyclerView
        foodRecyclerView.setAdapter(foodAdapter);
    }
}
