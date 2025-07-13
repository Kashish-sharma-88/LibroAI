package com.example.libroai;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private EditText nameEditText, emailEditText, phoneEditText, addressEditText;
    private Button saveButton, editButton;
    private ImageView profileImage;
    private TextView followersCountText, followingCountText, requestCountText;

    private boolean isEditMode = false;

    private FirebaseUser user;
    private FirebaseFirestore db;
    private DocumentReference userDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        nameEditText = findViewById(R.id.name_edit_text);
        emailEditText = findViewById(R.id.email_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        addressEditText = findViewById(R.id.address_edit_text);
        saveButton = findViewById(R.id.save_button);
        editButton = findViewById(R.id.edit_button);
        profileImage = findViewById(R.id.profile_image);

        // Follow stats
        followersCountText = findViewById(R.id.followers_count);
        followingCountText = findViewById(R.id.following_count);
        requestCountText = findViewById(R.id.request_count);

        user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "Not logged in", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        db = FirebaseFirestore.getInstance();
        userDoc = db.collection("users").document(user.getUid());

        loadProfileData();

        loadFollowStats(user.getUid());
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("follows").document(userId).collection("followers")
                .get().addOnSuccessListener(snap -> followersCountText.setText(snap.size() + " Followers"));

        db.collection("follows").document(userId).collection("following")
                .get().addOnSuccessListener(snap -> followingCountText.setText(snap.size() + " Following"));

        db.collection("users").document(userId).collection("followRequests")
                .get().addOnSuccessListener(snap -> requestCountText.setText(snap.size() + " Requests"));

        requestCountText.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, FollowRequestActivity.class));
        });

        followersCountText.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, FollowersListActivity.class));
        });

        followingCountText.setOnClickListener(v -> {
            startActivity(new Intent(ProfileActivity.this, FollowingListActivity.class));
        });


        editButton.setOnClickListener(v -> toggleEditMode());
        saveButton.setOnClickListener(v -> saveProfileData());
        profileImage.setOnClickListener(v ->
                Toast.makeText(this, "Change profile picture (not implemented)", Toast.LENGTH_SHORT).show());
    }

    private void loadProfileData() {
        userDoc.get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                nameEditText.setText(snapshot.getString("name"));
                emailEditText.setText(snapshot.getString("email"));
                phoneEditText.setText(snapshot.getString("phone"));
                addressEditText.setText(snapshot.getString("address"));
            } else {
                nameEditText.setText(user.getDisplayName());
                emailEditText.setText(user.getEmail());
            }
            setEditMode(false);
        }).addOnFailureListener(e ->
                Toast.makeText(this, "Failed to load profile", Toast.LENGTH_SHORT).show());
    }

    private void loadFollowStats(String userId) {
        db.collection("users").document(userId).collection("followers").get()
                .addOnSuccessListener(snapshot -> {
                    followersCountText.setText(snapshot.size() + " Followers");
                });

        db.collection("users").document(userId).collection("following").get()
                .addOnSuccessListener(snapshot -> {
                    followingCountText.setText(snapshot.size() + " Following");
                });

        db.collection("users").document(userId).collection("followRequests").get()
                .addOnSuccessListener(snapshot -> {
                    requestCountText.setText(snapshot.size() + " Requests");
                });
    }

    private void toggleEditMode() {
        isEditMode = !isEditMode;
        setEditMode(isEditMode);

        if (isEditMode) {
            editButton.setText("Cancel");
            saveButton.setVisibility(View.VISIBLE);
        } else {
            editButton.setText("Edit");
            saveButton.setVisibility(View.GONE);
            loadProfileData();  // reload original Firestore data
        }
    }

    private void setEditMode(boolean edit) {
        nameEditText.setEnabled(edit);
        emailEditText.setEnabled(edit);
        phoneEditText.setEnabled(edit);
        addressEditText.setEnabled(edit);
    }

    private void saveProfileData() {
        String name = nameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String address = addressEditText.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Name & Email are required", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", name);
        updates.put("email", email);
        updates.put("phone", phone);
        updates.put("address", address);

        if (!name.equals(user.getDisplayName())) {
            UserProfileChangeRequest req = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();
            user.updateProfile(req);
        }

        if (!email.equals(user.getEmail())) {
            user.updateEmail(email)
                    .addOnFailureListener(e ->
                            Toast.makeText(this, "Email update failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
        }

        userDoc.set(updates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();
                    toggleEditMode();
                    loadFollowStats(user.getUid()); // update counts after save
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Save failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
    }
}
