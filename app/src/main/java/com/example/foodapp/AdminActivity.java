package com.example.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminActivity extends AppCompatActivity {
    CardView manageUsersCard, manageCategoriesCard, manageProductsCard, manageOrdersCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_admin);

        // Ánh xạ view
        manageUsersCard = findViewById(R.id.manageUsersCard);
        manageCategoriesCard = findViewById(R.id.manageCategoriesCard);
        manageProductsCard = findViewById(R.id.manageProductsCard);
        manageOrdersCard = findViewById(R.id.manageOrdersCard);

        // Bắt sự kiện click
        manageUsersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, ManageUsersActivity.class));
            }
        });

        manageCategoriesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, ManageCategoriesActivity.class));
            }
        });

        manageProductsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, ManageProductsActivity.class));
            }
        });

        manageOrdersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, ManageOrdersActivity.class));
            }
        });
    }
}
