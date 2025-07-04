package com.example.libroai;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private EditText nameInput, emailInput, passInput, confirmInput;
    private Button signupBtn;
    private LinearLayout loginLinkLayout;
    private TextView forgotPasswordLink;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        String role = getIntent().getStringExtra("userType");

        nameInput = findViewById(R.id.nameInput);
        emailInput = findViewById(R.id.emailInput);
        passInput = findViewById(R.id.passInput);
        confirmInput = findViewById(R.id.ConfirmInput);
        signupBtn = findViewById(R.id.signupBtn);
        loginLinkLayout = findViewById(R.id.loginLinkLayout);
        forgotPasswordLink = findViewById(R.id.forgotPasswordLink);


        signupBtn.setOnClickListener(v -> {
            String name = nameInput.getText().toString().trim();
            String email = emailInput.getText().toString().trim();
            String pass = passInput.getText().toString().trim();
            String confirm = confirmInput.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                nameInput.setError("Full name required");
                return;
            }
            if (TextUtils.isEmpty(email)) {
                emailInput.setError("Email required");
                return;
            }
            if (TextUtils.isEmpty(pass) || pass.length() < 6) {
                passInput.setError("Password must be at least 6 characters");
                return;
            }
            if (!pass.equals(confirm)) {
                confirmInput.setError("Passwords do not match");
                return;
            }


            mAuth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            String uid = mAuth.getCurrentUser().getUid();

                            // Save data in Firestore
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("name", name);
                            userData.put("email", email);
                            userData.put("role", role);
                            userData.put("approved", role.equals("student")); // students are auto-approved, librarians not

                            db.collection("users").document(uid).set(userData)
                                    .addOnSuccessListener(unused -> {
                                        Toast.makeText(this, "Account created 🎉", Toast.LENGTH_SHORT).show();

                                        // Redirect based on role
                                        if ("student".equals(role)) {
                                            startActivity(new Intent(this, MainActivity2.class));
                                        } else if ("librarian".equals(role)) {
                                            startActivity(new Intent(this, Librarian_Dasboard.class));
                                        } else {
                                            Toast.makeText(this, "Unknown role", Toast.LENGTH_SHORT).show();
                                        }

                                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                        finish();
                                    })
                                    .addOnFailureListener(e ->
                                            Toast.makeText(this, "Failed to save user: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                                    );
                        } else {
                            Toast.makeText(this, "Signup failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });


        loginLinkLayout.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            intent.putExtra("userType", role);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });


        forgotPasswordLink.setOnClickListener(v -> {
            Intent intent = new Intent(SignUpActivity.this, ForgetPasswordActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });
    }
}
