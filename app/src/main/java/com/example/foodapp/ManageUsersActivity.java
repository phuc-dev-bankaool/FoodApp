package com.example.foodapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ManageUsersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseHelper db;
    private List<User> userList;
    private UserAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);

        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        userList = db.getAllUsers();
        adapter = new UserAdapter(this, userList, new UserAdapter.OnUserActionListener() {
            @Override
            public void onEdit(User user) {
                Toast.makeText(ManageUsersActivity.this, "Sửa: " + user.getName(), Toast.LENGTH_SHORT).show();
                // TODO: mở dialog sửa user
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDelete(User user) {
                boolean success = db.deleteUser(user.getId());
                if (success) {
                    Toast.makeText(ManageUsersActivity.this, "Delete user successfully", Toast.LENGTH_SHORT).show();
                    userList.remove(user);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ManageUsersActivity.this, "Delete user failed", Toast.LENGTH_SHORT).show();
                }
            }
            public void onChangeRole(User user) {
                if (user.getRole().equals("user")) {
                    user.setRole("admin");
                } else {
                    user.setRole("user");
                }
                db.updateUserRole(user);
                adapter.notifyDataSetChanged();
                Toast.makeText(ManageUsersActivity.this, "Vai trò đã chuyển", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
