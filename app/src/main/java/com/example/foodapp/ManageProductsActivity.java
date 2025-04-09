package com.example.foodapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
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

import java.util.List;
public class ManageProductsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseHelper db;
    private List<Food> foodList;
    private FoodAdapter adapter;
    private ImageButton rollBackButton, logoutBtn;
    private User loggedAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_products);
        rollBackButton = findViewById(R.id.rollBackButton);
        recyclerView = findViewById(R.id.recyclerViewFood);
        db = new DatabaseHelper(this);
        foodList = db.getAllFoods();
        logoutBtn = findViewById(R.id.logoutButton);
        loggedAdmin = (User) getIntent().getParcelableExtra("loggedInAdmin");
        if (loggedAdmin == null) {
            Intent intent = new Intent(ManageProductsActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            TextView welcomeTextView = findViewById(R.id.adminWelcomeText);
            welcomeTextView.setText("Welcome, " + loggedAdmin.getName());
        }
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
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        rollBackButton.setOnClickListener(View -> {
            Intent intent = new Intent(ManageProductsActivity.this, AdminHomeActivity.class);
            intent.putExtra("loggedInAdmin", loggedAdmin);
            startActivity(intent);
            finish();
        });
        logoutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ManageProductsActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
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