package com.example.foodapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.text.Editable;
import android.text.TextWatcher;
import com.google.android.material.textfield.TextInputEditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ManageUsersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseHelper db;
    private List<User> userList;
    private UserAdapter adapter;
    private User loggedAdmin;
    private ImageButton rollBackButton, logoutBtn;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);
        rollBackButton = findViewById(R.id.rollBackButton);
        logoutBtn = findViewById(R.id.logoutButton);
        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loggedAdmin = getIntent().getParcelableExtra("loggedInAdmin");
        if (loggedAdmin == null) {
            Intent intent = new Intent(ManageUsersActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            TextView welcomeTextView = findViewById(R.id.adminWelcomeText);
            welcomeTextView.setText("Welcome, " + loggedAdmin.getName());
        }
        userList = db.getAllUsers();
        adapter = new UserAdapter(this, userList, new UserAdapter.OnUserActionListener() {

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDelete(User user) {
                new AlertDialog.Builder(ManageUsersActivity.this)
                        .setTitle("Confirm delete")
                        .setMessage("Are you sure you want to delete this user?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            boolean success = db.deleteUser(user.getId());
                            if (success) {
                                Toast.makeText(ManageUsersActivity.this, "Delete user successfully", Toast.LENGTH_SHORT).show();
                                userList.remove(user);
                                db.removeOrdersByUserId(user.getId());
                                db.removeCartByUserId(user.getId());
                                adapter.notifyDataSetChanged();
                            } else {
                                Toast.makeText(ManageUsersActivity.this, "Delete user failed", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }

            public void onChangeRole(User user) {
                if (user.getRole().equals("user")) {
                    user.setRole("admin");
                    db.removeOrdersByUserId(user.getId());
                    db.removeCartByUserId(user.getId());
                } else {
                    user.setRole("user");
                }
                db.updateUserRole(user);
                adapter.notifyDataSetChanged();
                Toast.makeText(ManageUsersActivity.this, "Role of user " + user.getName() + " is changed", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);

        TextInputEditText searchEditText = findViewById(R.id.emailSearchEditText);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterUsers(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        rollBackButton.setOnClickListener(View -> {
            Intent intent = new Intent(ManageUsersActivity.this, AdminHomeActivity.class);
            intent.putExtra("loggedInAdmin", loggedAdmin);
            startActivity(intent);
            finish();
        });
        logoutBtn.setOnClickListener(v -> {
            Intent intent = new Intent(ManageUsersActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }

    public void filterUsers(String keyword) {
        List<User> filtered = new ArrayList<>();
        for (User u : userList) {
            if (u.getEmail().toLowerCase().contains(keyword.toLowerCase())) {
                filtered.add(u);
            }
        }
        adapter.updateList(filtered);
    }
}
