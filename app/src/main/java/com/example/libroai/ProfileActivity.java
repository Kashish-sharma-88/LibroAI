package com.example.libroai;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, phoneEditText, addressEditText;
    private Button   saveButton,  editButton;
    private ImageView profileImage;

    private boolean isEditMode = false;

    private FirebaseUser      user;
    private FirebaseFirestore db;
    private DocumentReference userDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        /* ── View refs ── */
        nameEditText    = findViewById(R.id.name_edit_text);
        emailEditText   = findViewById(R.id.email_edit_text);
        phoneEditText   = findViewById(R.id.phone_edit_text);
        addressEditText = findViewById(R.id.address_edit_text);
        saveButton      = findViewById(R.id.save_button);
        editButton      = findViewById(R.id.edit_button);
        profileImage    = findViewById(R.id.profile_image);

        /* ── Firebase ── */
        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        db = FirebaseFirestore.getInstance();
        userDoc = db.collection("users").document(user.getUid());

        /* ── Load profile data ── */
        loadProfileData();

        /* ── Clicks ── */
        editButton.setOnClickListener(v -> toggleEditMode());
        saveButton.setOnClickListener(v -> saveProfileData());
        profileImage.setOnClickListener(v ->
                Toast.makeText(this, "Change profile picture (not implemented)", Toast.LENGTH_SHORT).show());
    }

    /* ------------------------------------------ */
    /*         Load Firestore user document       */
    /* ------------------------------------------ */
    private void loadProfileData() {
        userDoc.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                nameEditText.setText(snapshot.getString("name"));
                emailEditText.setText(snapshot.getString("email"));
                phoneEditText.setText(snapshot.getString("phone"));
                addressEditText.setText(snapshot.getString("address"));
            } else {
                // First time: pre‑fill from auth
                nameEditText.setText(user.getDisplayName());
                emailEditText.setText(user.getEmail());
            }
            setEditMode(false);   // disable edits initially
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show());
    }

    /* ------------------------------------------ */
    /*         Edit / View mode toggling          */
    /* ------------------------------------------ */
    private void toggleEditMode() {
        isEditMode = !isEditMode;
        setEditMode(isEditMode);

        if (isEditMode) {
            editButton.setText("Cancel");
            saveButton.setVisibility(View.VISIBLE);
        } else {
            editButton.setText("Edit");
            saveButton.setVisibility(View.GONE);
            loadProfileData();        // reload original Firestore data
        }
    }
    private void setEditMode(boolean edit) {
        nameEditText.setEnabled(edit);
        emailEditText.setEnabled(edit);
        phoneEditText.setEnabled(edit);
        addressEditText.setEnabled(edit);
    }

    /* ------------------------------------------ */
    /*         Save changes to Firestore          */
    /* ------------------------------------------ */
    private void saveProfileData() {
        String name    = nameEditText.getText().toString().trim();
        String email   = emailEditText.getText().toString().trim();
        String phone   = phoneEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Name & Email are required", Toast.LENGTH_SHORT).show();
            return;
        }

        /* Collect fields for Firestore update */
        Map<String,Object> updates = new HashMap<>();
        updates.put("name",    name);
        updates.put("email",   email);
        updates.put("phone",   phone);
        updates.put("address", address);

        /* 1️⃣  Update display name if changed */
        if (!name.equals(user.getDisplayName())) {
            UserProfileChangeRequest req = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();
            user.updateProfile(req);
        }

        /* 2️⃣  Update email if changed */
        if (!email.equals(user.getEmail())) {
            // For critical change, re‑auth may be required.
            // Here we try directly; handle failure if any.
            user.updateEmail(email)
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Email update failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
        }

        /* 3️⃣  Save to Firestore */
        userDoc.set(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
                    toggleEditMode();
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Save failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }
}
