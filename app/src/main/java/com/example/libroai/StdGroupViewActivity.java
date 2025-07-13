package com.example.libroai;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class StdGroupViewActivity extends AppCompatActivity {
    
    private TextView groupName, groupDescription, adminName, memberCount, category;
    private ImageView backButton;
    private RecyclerView membersRecyclerView;
    private TextView membersTitle;
    
    private String groupId, currentUserId;
    private FirebaseUser currentUser;
    private DatabaseReference groupRef;
    private List<String> membersList;
    private StdGroupMembersAdapter membersAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_group_view);
        
        // Initialize Firebase
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        currentUserId = currentUser.getUid();
        
        // Get group ID from intent
        groupId = getIntent().getStringExtra("groupId");
        if (groupId == null) {
            Toast.makeText(this, "Group not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        groupRef = FirebaseDatabase.getInstance().getReference("forum_groups").child(groupId);
        
        // Initialize views
        initializeViews();
        
        // Setup RecyclerView for members
        setupMembersRecyclerView();
        
        // Setup click listeners
        setupClickListeners();
        
        // Load group data
        loadGroupData();
    }
    
    private void initializeViews() {
        groupName = findViewById(R.id.group_name);
        groupDescription = findViewById(R.id.group_description);
        adminName = findViewById(R.id.admin_name);
        memberCount = findViewById(R.id.member_count);
        category = findViewById(R.id.category);
        backButton = findViewById(R.id.back_button);
        membersRecyclerView = findViewById(R.id.members_recycler_view);
        membersTitle = findViewById(R.id.members_title);
        
        membersList = new ArrayList<>();
    }
    
    private void setupMembersRecyclerView() {
        membersAdapter = new StdGroupMembersAdapter(this, membersList);
        membersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        membersRecyclerView.setAdapter(membersAdapter);
    }
    
    private void setupClickListeners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    
    private void loadGroupData() {
        groupRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    StdForumGroupModel group = snapshot.getValue(StdForumGroupModel.class);
                    if (group != null) {
                        // Update group details
                        groupName.setText(group.getGroupName());
                        groupDescription.setText(group.getGroupDescription());
                        adminName.setText("Admin: " + group.getAdminName());
                        memberCount.setText(group.getMemberCount() + " members");
                        category.setText(group.getCategory());
                        
                        // Load members
                        loadMembers();
                    }
                } else {
                    Toast.makeText(StdGroupViewActivity.this, "Group not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
            
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(StdGroupViewActivity.this, 
                        "Error loading group: " + error.getMessage(), 
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void loadMembers() {
        groupRef.child("members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                membersList.clear();
                for (DataSnapshot memberSnapshot : snapshot.getChildren()) {
                    String memberName = memberSnapshot.getValue(String.class);
                    if (memberName != null) {
                        membersList.add(memberName);
                    }
                }
                
                // Update members title
                membersTitle.setText("Members (" + membersList.size() + ")");
                
                // Update adapter
                membersAdapter.updateList(membersList);
            }
            
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(StdGroupViewActivity.this, 
                        "Error loading members: " + error.getMessage(), 
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
} 