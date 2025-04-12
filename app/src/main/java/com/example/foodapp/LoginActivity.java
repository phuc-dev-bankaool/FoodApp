package com.example.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    private EditText emailEditText, passwordEditText;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        Button loginButton = findViewById(R.id.loginButton);
        TextView signUpTextView = findViewById(R.id.signUpTextView);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        db = new DatabaseHelper(this);
//        db.clearDatabase();
//        db.ensureTables();
        //set up edge to edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // move to home page
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        //set up behavior for clicking login button
        loginButton.setOnClickListener(View -> loginUser());
    }

    //check user by call checkUser method in DatabaseHelper
    private void loginUser() {
        String email = emailEditText.getText().toString().trim();
        String pwd = passwordEditText.getText().toString().trim();
        if (email.isEmpty() || pwd.isEmpty()) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }
        User user = db.getUserRole(email, pwd);
        if (user != null) {
            if (Objects.equals(user.getRole(), "user")) {
                Intent intent = new Intent(LoginActivity.this, UserHomeActivity.class);
                intent.putExtra("loggedInUser", user);
                startActivity(intent);
            } else if (Objects.equals(user.getRole(), "admin")) {
                Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
                intent.putExtra("loggedInAdmin", user);
                startActivity(intent);
            }
        }else{
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }
}