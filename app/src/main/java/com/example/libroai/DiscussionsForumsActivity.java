package com.example.libroai;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class DiscussionsForumsActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private ImageView backButton;
    private LinearLayout emptyState;
    private TextView emptyStateText, groupCountText;
    private androidx.cardview.widget.CardView browseGroupsCard, createGroupCard;
    
    private StdForumGroupAdapter adapter;
    private List<StdForumGroupModel> groupList;
    private FirebaseUser currentUser;
    private FirebaseFirestore firestore;
    private CollectionReference forumsRef;
    
    private static final int CREATE_GROUP_REQUEST = 1001;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion_forums);
        
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
        
        // Setup RecyclerView
        setupRecyclerView();
        
        // Setup click listeners
        setupClickListeners();
        
        // Load groups
        loadGroups();
    }
    
    private void initializeViews() {
        recyclerView = findViewById(R.id.recycler_view);
        browseGroupsCard = findViewById(R.id.browse_groups_card);
        createGroupCard = findViewById(R.id.create_group_card);
        backButton = findViewById(R.id.back_button);
        emptyState = findViewById(R.id.empty_state);
        emptyStateText = findViewById(R.id.empty_state_text);
        groupCountText = findViewById(R.id.group_count);
        
        groupList = new ArrayList<>();
    }
    
    private void setupRecyclerView() {
        adapter = new StdForumGroupAdapter(this, groupList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
    
    private void setupClickListeners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
        browseGroupsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Refresh the groups list
                loadGroups();
            }
        });
        
        createGroupCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DiscussionsForumsActivity.this, StdCreateGroupActivity.class);
                startActivityForResult(intent, CREATE_GROUP_REQUEST);
            }
        });
    }
    
    private void loadGroups() {
        forumsRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {
                if (error != null) {
                    Toast.makeText(DiscussionsForumsActivity.this, "Error loading groups: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                groupList.clear();
                for (DocumentSnapshot doc : value.getDocuments()) {
                    StdForumGroupModel group = doc.toObject(StdForumGroupModel.class);
                    if (group != null) {
                        group.setGroupId(doc.getId());
                        groupList.add(group);
                    }
                }
                // Update UI based on data
                groupCountText.setText(groupList.size() + " groups");
                if (groupList.isEmpty()) {
                    recyclerView.setVisibility(View.GONE);
                    emptyState.setVisibility(View.VISIBLE);
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    emptyState.setVisibility(View.GONE);
                    adapter.updateList(groupList);
                }
            }
        });
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == CREATE_GROUP_REQUEST && resultCode == RESULT_OK) {
            if (data != null && data.getBooleanExtra("groupCreated", false)) {
                Toast.makeText(this, "Group created successfully!", Toast.LENGTH_SHORT).show();
                // Groups will be automatically refreshed by the ValueEventListener
            }
        }
    }
} 