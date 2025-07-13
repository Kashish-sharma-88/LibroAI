package com.example.libroai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.SetOptions;

public class StdJoinGroupActivity extends AppCompatActivity {
    
    private TextView groupName, groupDescription, adminName, memberCount, category;
    private Button joinButton;
    private ImageView backButton;
    
    private String groupId, currentUserId, currentUserName;
    private FirebaseUser currentUser;
    private DatabaseReference groupRef, userRef;
    private FirebaseFirestore firestore;
    private DocumentReference groupDocRef;
    private ListenerRegistration memberListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_join_group);

        // Initialize Firebase
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        currentUserId = currentUser.getUid();
        firestore = FirebaseFirestore.getInstance();

        // Get data from intent FIRST ✅
        Intent intent = getIntent();
        groupId = intent.getStringExtra("groupId"); // ✅ now groupId is not null
        String groupNameText = intent.getStringExtra("groupName");
        String groupDescText = intent.getStringExtra("groupDescription");
        String adminNameText = intent.getStringExtra("adminName");
        int memberCountText = intent.getIntExtra("memberCount", 0);
        String categoryText = intent.getStringExtra("category");

        // 🔐 Now safely create the docRef
        groupDocRef = firestore.collection("forums").document(groupId);

        // Initialize views
        initializeViews();

        // Set data to views
        groupName.setText(groupNameText);
        groupDescription.setText(groupDescText);
        adminName.setText("Admin: " + adminNameText);
        memberCount.setText(memberCountText + " members");
        category.setText(categoryText);

        // Setup click listeners
        setupClickListeners();

        // Check if user is already a member
        checkMembershipStatus();
    }

    
    private void initializeViews() {
        groupName = findViewById(R.id.group_name);
        groupDescription = findViewById(R.id.group_description);
        adminName = findViewById(R.id.admin_name);
        memberCount = findViewById(R.id.member_count);
        category = findViewById(R.id.category);
        joinButton = findViewById(R.id.join_button);
        backButton = findViewById(R.id.back_button);
    }
    
    private void setupClickListeners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                groupDocRef.get().addOnSuccessListener(snapshot -> {
                    if (snapshot.exists()) {
                        // Get current members list
                        java.util.List<String> members = (java.util.List<String>) snapshot.get("members");
                        String adminId = snapshot.getString("adminId");

                        boolean isMember = members != null && members.contains(currentUserId);
                        boolean isAdmin = currentUserId.equals(adminId);

                        if (isMember || isAdmin) {
                            // Directly open chat
                            Intent intent = new Intent(StdJoinGroupActivity.this, StdGroupChatActivity.class);
                            intent.putExtra("groupId", groupId);
                            startActivity(intent);
                            finish();
                        } else {
                            // Show join confirmation dialog
                            new androidx.appcompat.app.AlertDialog.Builder(StdJoinGroupActivity.this)
                                    .setTitle("Join Group")
                                    .setMessage("Do you want to join this group?")
                                    .setPositiveButton("Join", (dialog, which) -> joinGroup())
                                    .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                                    .show();
                        }
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(StdJoinGroupActivity.this, "Error fetching group info: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            }
        });


    }
    
    private void checkMembershipStatus() {
        memberListener = groupDocRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(DocumentSnapshot snapshot, FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(StdJoinGroupActivity.this, "Error checking membership: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (snapshot.exists()) {
                    // Assume members is an array of user IDs
                    java.util.List<String> members = (java.util.List<String>) snapshot.get("members");
                    if (members != null && members.contains(currentUserId)) {
                        joinButton.setText("Already Joined");
                        joinButton.setEnabled(false);
                        joinButton.setBackgroundResource(R.drawable.button_disabled_bg);
                    } else {
                        joinButton.setText("Join Group");
                        joinButton.setEnabled(true);
                        joinButton.setBackgroundResource(R.drawable.brown_button_bg);
                    }
                }
            }
        });
    }
    
    private void joinGroup() {
        String userName = currentUser.getDisplayName();
        if (userName == null || userName.isEmpty()) {
            userName = currentUser.getEmail();
        }
        // Add user to members array in Firestore
        groupDocRef.update("members", FieldValue.arrayUnion(currentUserId),
                "memberCount", FieldValue.increment(1))
            .addOnSuccessListener(aVoid -> {
                Toast.makeText(StdJoinGroupActivity.this, "Successfully joined the group!", Toast.LENGTH_SHORT).show();
                // Open group chat activity
                Intent intent = new Intent(StdJoinGroupActivity.this, StdGroupChatActivity.class);
                intent.putExtra("groupId", groupId);
                startActivity(intent);
                finish();
            })
            .addOnFailureListener(e -> {
                Toast.makeText(StdJoinGroupActivity.this, "Failed to join group: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (memberListener != null) memberListener.remove();
    }
} 