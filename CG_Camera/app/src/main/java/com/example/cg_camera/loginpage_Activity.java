package com.example.cg_camera;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class loginpage_Activity extends AppCompatActivity {

    private EditText emailInput, passwordInput;
    private Button loginButton;
    private TextView signUpText;

    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        // Initialize Firebase
        FirebaseApp.initializeApp(this);
        firestore = FirebaseFirestore.getInstance();

        // Initialize views
        emailInput = findViewById(R.id.email_1);
        passwordInput = findViewById(R.id.password_1);
        loginButton = findViewById(R.id.login_1);
        signUpText = findViewById(R.id.signup_1);

        // Login button click
        loginButton.setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            String password = passwordInput.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(loginpage_Activity.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(email, password);
            }
        });

        // Sign up text click
        signUpText.setOnClickListener(v -> {
            Intent intent = new Intent(loginpage_Activity.this, signup_page.class);
            startActivity(intent);
        });
    }

    private void loginUser(String email, String password) {
        firestore.collection("users")
                .whereEqualTo("email", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(loginpage_Activity.this, "User not found", Toast.LENGTH_SHORT).show();
                    } else {
                        boolean matched = false;
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            String storedPassword = doc.getString("password");
                            if (storedPassword != null && storedPassword.equals(password)) {
                                matched = true;
                                break;
                            }
                        }
                        if (matched) {
                            Toast.makeText(loginpage_Activity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(loginpage_Activity.this, home_page.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(loginpage_Activity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(loginpage_Activity.this, "Login failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }
}
