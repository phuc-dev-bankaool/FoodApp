package com.example.foodapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText, adminCodeEditText;
    private Button registerButton;
    private TextView signInTextView;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        db = new DatabaseHelper(this);

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        adminCodeEditText = findViewById(R.id.adminCodeEditText);
        registerButton = findViewById(R.id.registerButton);
        signInTextView = findViewById(R.id.loginTextView);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.registerLinearLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        signInTextView.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String pwd = passwordEditText.getText().toString().trim();
        String confirmPwd = confirmPasswordEditText.getText().toString().trim();
        String adminCode = adminCodeEditText.getText().toString().trim(); // mới thêm

        if (name.isEmpty() || email.isEmpty() || pwd.isEmpty() || confirmPwd.isEmpty()) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!pwd.equals(confirmPwd)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isAdded = db.addUser(name, email, pwd, adminCode);
        if (isAdded) {
            Toast.makeText(this, "Register successfully", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        } else {
            Toast.makeText(this, "Email already exists or registration failed", Toast.LENGTH_SHORT).show();
        }
    }
}