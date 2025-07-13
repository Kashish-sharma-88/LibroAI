package com.example.libroai;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.UUID;

public class StdCreateGroupActivity extends AppCompatActivity {
    
    private EditText groupNameInput, groupDescriptionInput;
    private Spinner categorySpinner;
    private Button createButton;
    private ImageView backButton;
    
    private FirebaseUser currentUser;
    private FirebaseFirestore firestore;
    private CollectionReference forumsRef;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_create_group);
        
        // Initialize Firebase
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        firestore = FirebaseFirestore.getInstance();
        forumsRef = firestore.collection("forums");
        
        // Initialize views
        initializeViews();
        
        // Setup category spinner
        setupCategorySpinner();
        
        // Setup click listeners
        setupClickListeners();
    }
    
    private void initializeViews() {
        groupNameInput = findViewById(R.id.group_name_input);
        groupDescriptionInput = findViewById(R.id.group_description_input);
        categorySpinner = findViewById(R.id.category_spinner);
        createButton = findViewById(R.id.create_button);
        backButton = findViewById(R.id.back_button);
    }
    
    private void setupCategorySpinner() {
        String[] categories = {
            "Academic",
            "Technology",
            "Literature",
            "Science",
            "Mathematics",
            "History",
            "Arts",
            "Sports",
            "Music",
            "Other"
        };
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
            this,
            android.R.layout.simple_spinner_item,
            categories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }
    
    private void setupClickListeners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGroup();
            }
        });
    }
    
    private void createGroup() {
        String groupName = groupNameInput.getText().toString().trim();
        String groupDescription = groupDescriptionInput.getText().toString().trim();
        String category = categorySpinner.getSelectedItem().toString();
        
        // Validate inputs
        if (TextUtils.isEmpty(groupName)) {
            groupNameInput.setError("Group name is required");
            groupNameInput.requestFocus();
            return;
        }
        
        if (TextUtils.isEmpty(groupDescription)) {
            groupDescriptionInput.setError("Group description is required");
            groupDescriptionInput.requestFocus();
            return;
        }
        
        if (groupName.length() < 3) {
            groupNameInput.setError("Group name must be at least 3 characters");
            groupNameInput.requestFocus();
            return;
        }
        
        if (groupDescription.length() < 10) {
            groupDescriptionInput.setError("Description must be at least 10 characters");
            groupDescriptionInput.requestFocus();
            return;
        }
        
        // Disable create button to prevent multiple submissions
        createButton.setEnabled(false);
        createButton.setText("Creating...");
        
        // Generate unique group ID (let Firestore handle it)
        String userName = currentUser.getDisplayName();
        if (userName == null || userName.isEmpty()) {
            userName = currentUser.getEmail();
        }
        StdForumGroupModel group = new StdForumGroupModel(
            null, // Firestore will set the ID
            groupName,
            groupDescription,
            currentUser.getUid(),
            userName,
            category
        );
        forumsRef.add(group)
            .addOnSuccessListener(documentReference -> {
                // Add creator as first member (as a field or subcollection)
                forumsRef.document(documentReference.getId())
                    .update(
                        "memberCount", 1,
                        "members", com.google.firebase.firestore.FieldValue.arrayUnion(currentUser.getUid())
                    )
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(StdCreateGroupActivity.this, "Group created successfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra("groupCreated", true);
                        setResult(RESULT_OK, intent);
                        finish();
                    });
            })
            .addOnFailureListener(e -> {
                Toast.makeText(StdCreateGroupActivity.this, "Error creating group: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                resetCreateButton();
            });
    }
    
    private void resetCreateButton() {
        createButton.setEnabled(true);
        createButton.setText("Create Group");
    }
} 