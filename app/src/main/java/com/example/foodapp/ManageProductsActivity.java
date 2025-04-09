package com.example.foodapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ManageProductsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseHelper db;
    private List<Food> foodList;
    private FoodAdapter adapter;
    private ImageButton rollBackButton, logoutBtn, addButton;
    private User loggedAdmin;
    private EditText foodNameSearch;
    private TextView emptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_products);
        rollBackButton = findViewById(R.id.rollBackButton);
        recyclerView = findViewById(R.id.recyclerViewFood);
        foodNameSearch = findViewById(R.id.foodNameSearch);
        emptyTextView = findViewById(R.id.emptyTextView);
        db = new DatabaseHelper(this);
        foodList = db.getAllFoods();
        logoutBtn = findViewById(R.id.logoutButton);
        addButton = findViewById(R.id.buttonAddFood);
        loggedAdmin = (User) getIntent().getParcelableExtra("loggedInAdmin");
        if (loggedAdmin == null) {
            Intent intent = new Intent(ManageProductsActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            TextView welcomeTextView = findViewById(R.id.adminWelcomeText);
            welcomeTextView.setText("Welcome, " + loggedAdmin.getName());
        }
        rollBackButton.setOnClickListener(v -> {
            Intent intent = new Intent(ManageProductsActivity.this, AdminHomeActivity.class);
            intent.putExtra("loggedInAdmin", loggedAdmin);
            startActivity(intent);
            finish();
        });
        logoutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ManageProductsActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
        adapter = new FoodAdapter(this, foodList, new FoodAdapter.OnFoodActionListener() {
            @Override
            public void onEdit(Food food) {
                showEditDialog(food);
            }

            @Override
            public void onDelete(Food food) {
                new AlertDialog.Builder(ManageProductsActivity.this)
                        .setTitle("Delete Food")
                        .setMessage("Are you sure you want to delete " + food.getName() + "?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            db.deleteFood(food.getId());
                            foodList.remove(food);
                            adapter.notifyDataSetChanged();
                            Toast.makeText(ManageProductsActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("No", null)
                        .show();
            }

            @Override
            public void onAdd(Food food) {
                showAddDialog();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        foodNameSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterFoods(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        addButton.setOnClickListener(v -> showAddDialog());
    }
    private void showAddDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_food, null);
        EditText nameInput = view.findViewById(R.id.editFoodName);
        EditText descInput = view.findViewById(R.id.editFoodDescription);
        EditText priceInput = view.findViewById(R.id.editFoodPrice);

        new AlertDialog.Builder(this)
                .setTitle("Add New Food")
                .setView(view)
                .setPositiveButton("Add", (dialog, which) -> {
                    String name = nameInput.getText().toString().trim();
                    String desc = descInput.getText().toString().trim();
                    String priceStr = priceInput.getText().toString().trim();

                    if (name.isEmpty() || desc.isEmpty() || priceStr.isEmpty()) {
                        Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    float price = Float.parseFloat(priceStr);
                    // Không sử dụng hình ảnh, chỉ cần tên, mô tả, giá và trạng thái
                    Food food = new Food(0, name, desc, price, true);
                    long id = db.addFood(food); // Trả về ID của món ăn mới
                    if (id != -1) {
                        food.setId((int) id);  // Cập nhật ID cho món ăn
                        foodList.add(food);
                        adapter.notifyDataSetChanged();
                        checkEmptyState(foodList);
                        Toast.makeText(this, "Food added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Error adding food", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    private void filterFoods(String keyword) {
        List<Food> filtered = new ArrayList<>();
        for (Food f : foodList) {
            if (f.getName().toLowerCase().contains(keyword.toLowerCase())) {
                filtered.add(f);
            }
        }
        adapter.updateList(filtered);
        checkEmptyState(filtered);
    }
    private void checkEmptyState(List<Food> filtered) {
        emptyTextView.setVisibility(filtered.isEmpty() ? View.VISIBLE : View.GONE);
    }
    private void showEditDialog(Food food) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_food, null);
        EditText nameInput = view.findViewById(R.id.editFoodName);
        EditText descInput = view.findViewById(R.id.editFoodDescription);
        EditText priceInput = view.findViewById(R.id.editFoodPrice);

        nameInput.setText(food.getName());
        descInput.setText(food.getDescription());
        priceInput.setText(String.valueOf(food.getPrice()));

        new AlertDialog.Builder(this)
                .setTitle("Edit Food")
                .setView(view)
                .setPositiveButton("Update", (dialog, which) -> {
                    food.setName(nameInput.getText().toString());
                    food.setDescription(descInput.getText().toString());
                    food.setPrice(Float.parseFloat(priceInput.getText().toString()));

                    db.updateFood(food);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Food updated", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}