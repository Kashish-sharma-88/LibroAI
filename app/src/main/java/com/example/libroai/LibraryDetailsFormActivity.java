package com.example.libroai;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class LibraryDetailsFormActivity extends AppCompatActivity {

    EditText libraryNameInput, cityInput, areaInput;
    Button submitBtn;
    FirebaseFirestore db;
    String uid, email;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_details_form);

        libraryNameInput = findViewById(R.id.libraryNameInput);
        cityInput = findViewById(R.id.cityInput);
        areaInput = findViewById(R.id.areaInput);
        submitBtn = findViewById(R.id.submitBtn);

        db = FirebaseFirestore.getInstance();
        uid = getIntent().getStringExtra("uid");
        email = getIntent().getStringExtra("email");

        submitBtn.setOnClickListener(v -> {
            String libName = libraryNameInput.getText().toString().trim();
            String city = cityInput.getText().toString().trim();
            String area = areaInput.getText().toString().trim();

            if (TextUtils.isEmpty(libName) || TextUtils.isEmpty(city) || TextUtils.isEmpty(area)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> data = new HashMap<>();
            data.put("libraryName", libName);
            data.put("city", city);
            data.put("area", area);
            data.put("uid", uid);
            data.put("email", email);
            data.put("status", "pending");

            db.collection("library_requests")
                    .document(uid)
                    .set(data)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(this, "Submitted. Wait for admin approval.\nYou'll be notified via email.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(this, LoginActivity.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to submit: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        });
    }
}
