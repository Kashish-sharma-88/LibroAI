package com.example.libroai;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import android.widget.Toast;
import android.widget.TextView;
import android.view.View;

public class libprofile extends AppCompatActivity {
    private EditText nameEditText, phoneEditText, emailEditText, addressEditText;
    private Button editButton, saveButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String userId;
    private TextView profileNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_libprofile);

        nameEditText = findViewById(R.id.name_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        addressEditText = findViewById(R.id.address_edit_text);
        editButton = findViewById(R.id.edit_button);
        saveButton = findViewById(R.id.save_button);
        profileNameText = findViewById(R.id.tv_name);

        // By default, Save button should be hidden
        saveButton.setVisibility(View.GONE);

        // Firebase initialization
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userId = mAuth.getCurrentUser() != null ? mAuth.getCurrentUser().getUid() : null;

        if (userId != null) {
            loadUserProfile();
        }

        editButton.setOnClickListener(v -> {
            setEditable(true);
            saveButton.setVisibility(View.VISIBLE); // Show Save button when editing
        });

        saveButton.setOnClickListener(v -> {
            setEditable(false);
            saveUserProfile();
            saveButton.setVisibility(View.GONE); // Hide Save button after saving
        });
    }

    private void setEditable(boolean editable) {
        nameEditText.setEnabled(editable);
        phoneEditText.setEnabled(editable);
        emailEditText.setEnabled(editable);
        addressEditText.setEnabled(editable);
    }

    private void loadUserProfile() {
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String name = documentSnapshot.getString("name");
                nameEditText.setText(name);
                if (profileNameText != null) {
                    profileNameText.setText(name);
                }
                emailEditText.setText(documentSnapshot.getString("email"));
                phoneEditText.setText(documentSnapshot.getString("phone"));
                addressEditText.setText(documentSnapshot.getString("address"));
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show();
        });
    }

    private void saveUserProfile() {
        DocumentReference userRef = db.collection("users").document(userId);
        userRef.update(
                "name", nameEditText.getText().toString(),
                "email", emailEditText.getText().toString(),
                "phone", phoneEditText.getText().toString(),
                "address", addressEditText.getText().toString()
        ).addOnSuccessListener(aVoid -> {
            if (profileNameText != null) {
                profileNameText.setText(nameEditText.getText().toString());
            }
            Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Failed to update profile: " + e.getMessage(), Toast.LENGTH_LONG).show();
        });
    }
} 