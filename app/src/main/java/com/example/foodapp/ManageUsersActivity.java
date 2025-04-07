package com.example.foodapp;

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

        userList = db.getAllUsers(); // bạn cần thêm hàm getAllUsers trong DatabaseHelper
        adapter = new UserAdapter(this, userList, new UserAdapter.OnUserActionListener() {
            @Override
            public void onEdit(User user) {
                Toast.makeText(ManageUsersActivity.this, "Sửa: " + user.getName(), Toast.LENGTH_SHORT).show();
                // TODO: mở dialog sửa user
            }

            @Override
            public void onDelete(User user) {
                boolean success = db.deleteUser(user.getId());
                if (success) {
                    Toast.makeText(ManageUsersActivity.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                    userList.remove(user);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(ManageUsersActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
            public void onChangeRole(User user) {
                // Gợi ý: Toggle role từ "user" -> "admin" hoặc ngược lại
                if (user.getRole().equals("user")) {
                    user.setRole("admin");
                } else {
                    user.setRole("user");
                }
                db.updateUserRole(user); // Cần hàm này trong DatabaseHelper
                adapter.notifyDataSetChanged();
                Toast.makeText(ManageUsersActivity.this, "Vai trò đã chuyển", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(adapter);
    }
}
