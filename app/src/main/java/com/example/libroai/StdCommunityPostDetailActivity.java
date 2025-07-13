package com.example.libroai;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StdCommunityPostDetailActivity extends AppCompatActivity {

    private TextView postTitleHeader, detailUserName, detailPostTime, detailPostTitle, detailPostContent, detailCategory, detailStatus;
    private RecyclerView repliesRecyclerView;
    private EditText replyEditText;
    private ImageView backButton, sendReplyButton, resolveButton;
    private StdCommunityReplyAdapter replyAdapter;
    private List<StdCommunityReply> repliesList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private String postId;
    private String postTitle;
    private StdCommunityPost currentPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_community_post_detail);

        // Get post ID from intent
        postId = getIntent().getStringExtra("postId");
        postTitle = getIntent().getStringExtra("postTitle");

        if (postId == null) {
            Toast.makeText(this, "Error: Post not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Initialize Firebase
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Initialize views
        initializeViews();

        // Setup RecyclerView
        repliesList = new ArrayList<>();
        replyAdapter = new StdCommunityReplyAdapter(this, repliesList, auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : "");
        repliesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        repliesRecyclerView.setAdapter(replyAdapter);

        // Setup click listeners
        backButton.setOnClickListener(v -> finish());
        sendReplyButton.setOnClickListener(v -> sendReply());
        resolveButton.setOnClickListener(v -> resolvePost());

        // Setup reply accepted listener
        replyAdapter.setOnReplyAcceptedListener(new StdCommunityReplyAdapter.OnReplyAcceptedListener() {
            @Override
            public void onReplyAccepted(StdCommunityReply reply) {
                acceptReply(reply);
            }
        });

        // Load post details and replies
        loadPostDetails();
        loadReplies();
    }

    private void initializeViews() {
        postTitleHeader = findViewById(R.id.post_title_header);
        detailUserName = findViewById(R.id.detail_user_name);
        detailPostTime = findViewById(R.id.detail_post_time);
        detailPostTitle = findViewById(R.id.detail_post_title);
        detailPostContent = findViewById(R.id.detail_post_content);
        detailCategory = findViewById(R.id.detail_category);
        detailStatus = findViewById(R.id.detail_status);
        repliesRecyclerView = findViewById(R.id.replies_recycler_view);
        replyEditText = findViewById(R.id.reply_edit_text);
        backButton = findViewById(R.id.back_button);
        sendReplyButton = findViewById(R.id.send_reply_button);
        resolveButton = findViewById(R.id.resolve_button);

        if (postTitle != null) {
            postTitleHeader.setText(postTitle);
        }
    }

    private void loadPostDetails() {
        db.collection("community_posts").document(postId).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().exists()) {
                            currentPost = task.getResult().toObject(StdCommunityPost.class);
                            if (currentPost != null) {
                                displayPostDetails();
                            }
                        } else {
                            Toast.makeText(StdCommunityPostDetailActivity.this, "Error loading post", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void displayPostDetails() {
        detailUserName.setText(currentPost.getUserName());
        detailPostTitle.setText(currentPost.getTitle());
        detailPostContent.setText(currentPost.getContent());
        detailCategory.setText(currentPost.getCategory());

        if (currentPost.getTimestamp() != null) {
            detailPostTime.setText(new java.text.SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", java.util.Locale.getDefault())
                    .format(currentPost.getTimestamp().toDate()));
        }

        if (currentPost.isResolved()) {
            detailStatus.setText("✓ Resolved");
            detailStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            resolveButton.setVisibility(View.GONE);
        } else {
            detailStatus.setText("● Open");
            detailStatus.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
            // Show resolve button only to post author
            FirebaseUser currentUser = auth.getCurrentUser();
            if (currentUser != null && currentUser.getUid().equals(currentPost.getUserId())) {
                resolveButton.setVisibility(View.VISIBLE);
            }
        }
    }

    private void loadReplies() {
        db.collection("community_posts").document(postId)
                .collection("replies")
                .orderBy("timestamp")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            repliesList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                StdCommunityReply reply = document.toObject(StdCommunityReply.class);
                                if (reply != null) {
                                    reply.setReplyId(document.getId());
                                    repliesList.add(reply);
                                }
                            }
                            replyAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void sendReply() {
        String replyContent = replyEditText.getText().toString().trim();

        if (replyContent.isEmpty()) {
            replyEditText.setError("Reply cannot be empty");
            return;
        }

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Please login to reply", Toast.LENGTH_SHORT).show();
            return;
        }

        String replyId = UUID.randomUUID().toString();
        StdCommunityReply reply = new StdCommunityReply(
                replyId,
                postId,
                currentUser.getUid(),
                currentUser.getDisplayName() != null ? currentUser.getDisplayName() : "Anonymous",
                replyContent
        );

        // Save reply to Firestore
        db.collection("community_posts").document(postId)
                .collection("replies").document(replyId).set(reply)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            replyEditText.setText("");
                            loadReplies();
                            updatePostReplyCount();
                            Toast.makeText(StdCommunityPostDetailActivity.this, "Reply sent!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(StdCommunityPostDetailActivity.this, "Error sending reply", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updatePostReplyCount() {
        if (currentPost != null) {
            currentPost.setReplyCount(currentPost.getReplyCount() + 1);
            db.collection("community_posts").document(postId).set(currentPost);
        }
    }

    private void acceptReply(StdCommunityReply reply) {
        // Only post author can accept replies
        if (currentPost != null && auth.getCurrentUser() != null && 
            auth.getCurrentUser().getUid().equals(currentPost.getUserId())) {
            
            reply.setAccepted(true);
            db.collection("community_posts").document(postId)
                    .collection("replies").document(reply.getReplyId()).set(reply)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                loadReplies();
                                Toast.makeText(StdCommunityPostDetailActivity.this, "Reply accepted!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void resolvePost() {
        if (currentPost != null) {
            currentPost.setResolved(true);
            db.collection("community_posts").document(postId).set(currentPost)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                displayPostDetails();
                                Toast.makeText(StdCommunityPostDetailActivity.this, "Post marked as resolved!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
} 