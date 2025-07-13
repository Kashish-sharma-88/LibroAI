package com.example.libroai;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class StAskDoubtActivity extends AppCompatActivity {

    private EditText titleEditText;
    private EditText contentEditText;
    private Spinner categorySpinner;
    private Button submitButton;
    private ImageView backButton;
    private ProgressBar progressBar;
    
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String selectedCategory = "General";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_st_ask_doubt);

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize views
        titleEditText = findViewById(R.id.title_edit_text);
        contentEditText = findViewById(R.id.content_edit_text);
        categorySpinner = findViewById(R.id.category_spinner);
        submitButton = findViewById(R.id.submit_button);
        backButton = findViewById(R.id.back_button);
        progressBar = findViewById(R.id.progress_bar);

        // Setup category spinner
        setupCategorySpinner();

        // Setup click listeners
        backButton.setOnClickListener(v -> finish());
        
        submitButton.setOnClickListener(v -> submitDoubt());
    }

    private void setupCategorySpinner() {
        String[] categories = {"General", "Academic", "Technical", "Library", "Other"};
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_item, categories);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(spinnerAdapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = categories[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = "General";
            }
        });
    }

    private void submitDoubt() {
        String title = titleEditText.getText().toString().trim();
        String content = contentEditText.getText().toString().trim();

        // Validate input
        if (TextUtils.isEmpty(title)) {
            titleEditText.setError("Please enter a title");
            titleEditText.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(content)) {
            contentEditText.setError("Please enter your doubt");
            contentEditText.requestFocus();
            return;
        }

        if (title.length() < 5) {
            titleEditText.setError("Title must be at least 5 characters");
            titleEditText.requestFocus();
            return;
        }

        if (content.length() < 10) {
            contentEditText.setError("Doubt description must be at least 10 characters");
            contentEditText.requestFocus();
            return;
        }

        // Show progress
        progressBar.setVisibility(View.VISIBLE);
        submitButton.setEnabled(false);

        // Get current user
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Please login to ask a doubt", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            submitButton.setEnabled(true);
            return;
        }

        // Create post data
        Map<String, Object> postData = new HashMap<>();
        postData.put("userId", currentUser.getUid());
        postData.put("userName", currentUser.getDisplayName() != null ? 
            currentUser.getDisplayName() : "Anonymous");
        postData.put("userEmail", currentUser.getEmail());
        postData.put("title", title);
        postData.put("content", content);
        postData.put("category", selectedCategory);
        postData.put("timestamp", com.google.firebase.Timestamp.now());
        postData.put("replyCount", 0);
        postData.put("isResolved", false);
        postData.put("question", title);
        postData.put("reported", false);

        // Save to Firebase
        db.collection("community_posts")
            .add(postData)
            .addOnCompleteListener(new OnCompleteListener<com.google.firebase.firestore.DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<com.google.firebase.firestore.DocumentReference> task) {
                    progressBar.setVisibility(View.GONE);
                    submitButton.setEnabled(true);

                    if (task.isSuccessful()) {
                        Toast.makeText(StAskDoubtActivity.this, 
                            "Doubt posted successfully!", Toast.LENGTH_SHORT).show();
                        
                        // Return to doubts activity
                        Intent resultIntent = new Intent();
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    } else {
                        Toast.makeText(StAskDoubtActivity.this, 
                            "Failed to post doubt: " + task.getException().getMessage(), 
                            Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
} 