package com.example.libroai;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
//import com.google.firebase.firestore.SetOptions;
import java.util.ArrayList;
import java.util.List;

public class StdGroupChatActivity extends AppCompatActivity {
    
    private Toolbar toolbar;
    private RecyclerView chatRecyclerView;
    private EditText messageInput;
    private ImageView sendButton;
    private TextView groupNameToolbar, memberCountToolbar;
    
    private String groupId, currentUserId, currentUserName;
    private FirebaseUser currentUser;
    private DatabaseReference groupRef;
    private FirebaseFirestore firestore;
    private DocumentReference groupDocRef;
    private CollectionReference messagesRef;
    private ListenerRegistration messagesListener;
    private List<StdGroupMessageModel> messageList;
    private StdGroupChatAdapter chatAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        try {
            setContentView(R.layout.activity_std_group_chat);
            
            // Initialize Firebase
            currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser == null) {
                Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            currentUserId = currentUser.getUid();
            currentUserName = currentUser.getDisplayName();
            if (currentUserName == null || currentUserName.isEmpty()) {
                currentUserName = currentUser.getEmail();
            }
            
            // Get group ID from intent
            groupId = getIntent().getStringExtra("groupId");
            if (groupId == null) {
                Toast.makeText(this, "Group not found", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            
            groupRef = FirebaseDatabase.getInstance().getReference("forum_groups").child(groupId);
            firestore = FirebaseFirestore.getInstance();
            groupDocRef = firestore.collection("forums").document(groupId);
            messagesRef = groupDocRef.collection("messages");
            
            // Initialize views
            initializeViews();
            
            // Setup toolbar
            setupToolbar();
            
            // Setup RecyclerView
            setupChatRecyclerView();
            
            // Setup click listeners
            setupClickListeners();
            
            // Load group data
            loadGroupData();
            
            // Load messages
            loadMessages();
            
        } catch (Exception e) {
            Toast.makeText(this, "Error opening group chat: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }
    
    private void initializeViews() {
        try {
            toolbar = findViewById(R.id.toolbar);
            chatRecyclerView = findViewById(R.id.chat_recycler_view);
            messageInput = findViewById(R.id.message_input);
            sendButton = findViewById(R.id.send_button);
            groupNameToolbar = findViewById(R.id.group_name_toolbar);
            memberCountToolbar = findViewById(R.id.member_count_toolbar);
            
            messageList = new ArrayList<>();
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing views: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            throw e;
        }
    }
    
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }
    
    private void setupChatRecyclerView() {
        try {
            chatAdapter = new StdGroupChatAdapter(this, messageList, currentUserId);
            chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            chatRecyclerView.setAdapter(chatAdapter);
        } catch (Exception e) {
            Toast.makeText(this, "Error setting up chat: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void setupClickListeners() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });
    }
    
    private void loadGroupData() {
        try {
            groupDocRef.get().addOnSuccessListener(snapshot -> {
                if (!snapshot.exists()) {
                    Toast.makeText(this, "Group not found.", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                StdForumGroupModel group = snapshot.toObject(StdForumGroupModel.class);
                if (group != null) {
                    group.setGroupId(snapshot.getId()); // Always set from Firestore doc ID
                }
                // Defensive check (do NOT check groupId):
                if (group == null || group.getGroupName() == null || group.getAdminId() == null) {
                    Toast.makeText(this, "Group data is incomplete.", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                // Proceed to show group info in toolbar
                groupNameToolbar.setText(group.getGroupName());
                memberCountToolbar.setText(group.getMemberCount() + " members");
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Error loading group: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error setting up group listener: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void loadMessages() {
        try {
            messagesListener = messagesRef.orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot value, FirebaseFirestoreException error) {
                        if (error != null) {
                            Toast.makeText(StdGroupChatActivity.this, "Error loading messages: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            messageList.clear();
                            for (DocumentSnapshot doc : value.getDocuments()) {
                                StdGroupMessageModel message = doc.toObject(StdGroupMessageModel.class);
                                if (message != null) {
                                    messageList.add(message);
                                }
                            }
                            if (chatAdapter != null) {
                                chatAdapter.updateList(messageList);
                                if (messageList.size() > 0) {
                                    chatRecyclerView.smoothScrollToPosition(messageList.size() - 1);
                                }
                            }
                        } catch (Exception e) {
                            Toast.makeText(StdGroupChatActivity.this, "Error processing messages: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        } catch (Exception e) {
            Toast.makeText(this, "Error setting up messages listener: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
    
    private void sendMessage() {
        String messageText = messageInput.getText().toString().trim();
        
        if (TextUtils.isEmpty(messageText)) {
            return;
        }
        
        // Create message model
        StdGroupMessageModel message = new StdGroupMessageModel(
            currentUserId,
            currentUserName,
            messageText,
            System.currentTimeMillis()
        );
        
        // Save message to Firebase
        messagesRef.add(message)
            .addOnSuccessListener(documentReference -> {
                messageInput.setText("");
            })
            .addOnFailureListener(e -> {
                Toast.makeText(StdGroupChatActivity.this, "Failed to send message: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.group_chat_menu, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.action_group_info) {
            // Open group info activity
            Intent intent = new Intent(this, StdGroupViewActivity.class);
            intent.putExtra("groupId", groupId);
            startActivity(intent);
            return true;
        }
        // Removed action_members since StdGroupMembersActivity doesn't exist yet
        
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (messagesListener != null) messagesListener.remove();
    }
} 