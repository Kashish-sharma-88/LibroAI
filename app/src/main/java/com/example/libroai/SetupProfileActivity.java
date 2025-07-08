package com.example.libroai;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SetupProfileActivity extends AppCompatActivity {

    EditText bioEditText;
    Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);

        bioEditText = findViewById(R.id.bioEditText);
        saveBtn = findViewById(R.id.saveProfileBtn);

        saveBtn.setOnClickListener(v -> {
            String bio = bioEditText.getText().toString().trim();

            if (bio.isEmpty()) {
                bioEditText.setError("Please enter your bio");
                return;
            }

            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            Map<String, Object> updates = new HashMap<>();
            updates.put("bio", bio);
            updates.put("followersCount", 0);
            updates.put("profileComplete", true); // ✅ Optional: Add flag

            FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(uid)
                    .update(updates)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Profile updated!", Toast.LENGTH_SHORT).show();

                        // ✅ Go to MainActivity2 (only here)
                        Intent intent = new Intent(this, MainActivity2.class);
                        startActivity(intent);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
