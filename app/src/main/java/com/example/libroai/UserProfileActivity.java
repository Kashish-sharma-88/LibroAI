package com.example.libroai;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.FirebaseFirestore;

public class UserProfileActivity extends AppCompatActivity {
    TextView name, email, role, bio, library;
    ImageView avatar;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        name = findViewById(R.id.profile_name);
        email = findViewById(R.id.profile_email);
        role = findViewById(R.id.profile_role);
        bio = findViewById(R.id.profile_bio);
        library = findViewById(R.id.profile_library);
        avatar = findViewById(R.id.profile_avatar);

        String userId = getIntent().getStringExtra("userId");
        db = FirebaseFirestore.getInstance();

        db.collection("users").document(userId).get().addOnSuccessListener(doc -> {
            name.setText(doc.getString("name"));
            email.setText(doc.getString("email"));
            role.setText(doc.getString("role"));
            bio.setText(doc.getString("bio"));
            library.setText(doc.getString("libraryName"));
        });
    }
}
