package com.example.foodapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private static final int REQUEST_CODE_PERMISSION = 100;
    private static final int REQUEST_CODE_PICK_IMAGE = 101;
    private Uri selectedImageUri;
    private ImageButton currentImageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_products);

        checkStoragePermission();

        rollBackButton = findViewById(R.id.rollBackButton);
        recyclerView = findViewById(R.id.recyclerViewFood);
        foodNameSearch = findViewById(R.id.foodNameSearch);
        emptyTextView = findViewById(R.id.emptyTextView);
        db = new DatabaseHelper(this);
        foodList = db.getAllFoods();
        logoutBtn = findViewById(R.id.logoutButton);
        addButton = findViewById(R.id.buttonAddFood);
        loggedAdmin = getIntent().getParcelableExtra("loggedInAdmin");

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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterFoods(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        addButton.setOnClickListener(v -> showAddDialog());
    }

    private void checkStoragePermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE_PERMISSION);
        }
    }

    // Hiển thị dialog thêm món ăn
    @SuppressLint("NotifyDataSetChanged")
    private void showAddDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_food, null);
        EditText nameInput = view.findViewById(R.id.editFoodName);
        EditText descInput = view.findViewById(R.id.editFoodDescription);
        EditText priceInput = view.findViewById(R.id.editFoodPrice);
        currentImageButton = view.findViewById(R.id.editFoodImage);

        currentImageButton.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE_PERMISSION);
            }
        });

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

                    String imageUriStr = selectedImageUri != null ? selectedImageUri.toString() : "";
                    float price = Float.parseFloat(priceStr);
                    Food food = new Food(0, name, desc, price, true, imageUriStr);
                    long id = db.addFood(food);
                    if (id != -1) {
                        food.setId((int) id);
                        foodList.add(food);
                        adapter.notifyDataSetChanged();
                        checkEmptyState(foodList);
                        Toast.makeText(this, "Food added", Toast.LENGTH_SHORT).show();
                        selectedImageUri = null;
                    } else {
                        Toast.makeText(this, "Error adding food", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void showEditDialog(Food food) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_food, null);
        EditText nameInput = view.findViewById(R.id.editFoodName);
        EditText descInput = view.findViewById(R.id.editFoodDescription);
        EditText priceInput = view.findViewById(R.id.editFoodPrice);
        currentImageButton = view.findViewById(R.id.editFoodImage);

        nameInput.setText(food.getName());
        descInput.setText(food.getDescription());
        priceInput.setText(String.valueOf(food.getPrice()));
        if (food.getImageUri() != null && !food.getImageUri().isEmpty()) {
            currentImageButton.setImageURI(Uri.parse(food.getImageUri()));
        }

        currentImageButton.setOnClickListener(v -> {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery();
            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_MEDIA_IMAGES}, REQUEST_CODE_PERMISSION);
            }
        });

        new AlertDialog.Builder(this)
                .setTitle("Edit Food")
                .setView(view)
                .setPositiveButton("Update", (dialog, which) -> {
                    String name = nameInput.getText().toString().trim();
                    String desc = descInput.getText().toString().trim();
                    String priceStr = priceInput.getText().toString().trim();

                    if (name.isEmpty() || desc.isEmpty() || priceStr.isEmpty()) {
                        Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    food.setName(name);
                    food.setDescription(desc);
                    food.setPrice(Float.parseFloat(priceStr));
                    food.setImageUri(selectedImageUri != null ? selectedImageUri.toString() : food.getImageUri());

                    db.updateFood(food);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(this, "Food updated", Toast.LENGTH_SHORT).show();
                    selectedImageUri = null;
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    // Mở gallery để chọn ảnh
    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri originalUri = data.getData();
            try {
                String fileName = "food_image_" + System.currentTimeMillis() + ".jpg";
                File imageFile = new File(getFilesDir(), fileName);
                InputStream inputStream = getContentResolver().openInputStream(originalUri);
                FileOutputStream outputStream = new FileOutputStream(imageFile);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                outputStream.close();

                selectedImageUri = Uri.fromFile(imageFile);
                if (currentImageButton != null) {
                    currentImageButton.setImageURI(selectedImageUri);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
                selectedImageUri = null;
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickImageFromGallery();
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_MEDIA_IMAGES)) {
                    Toast.makeText(this, "Please enable permission from settings", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Need permission to access gallery", Toast.LENGTH_SHORT).show();
                }
            }
        }
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
}