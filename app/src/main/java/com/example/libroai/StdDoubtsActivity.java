package com.example.libroai;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class StdDoubtsActivity extends AppCompatActivity {

    private RecyclerView postsRecyclerView;
    private StdCommunityPostAdapter postAdapter;
    private List<StdCommunityPost> postsList;
    private FirebaseFirestore db;
    private FirebaseAuth auth;
    private EditText searchEditText;
    private Spinner categorySpinner;
    private LinearLayout emptyStateLayout;
    private ImageView backButton;
    private FloatingActionButton fabAskDoubt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_doubts);

        try {
            // Initialize Firebase
            db = FirebaseFirestore.getInstance();
            auth = FirebaseAuth.getInstance();

            // Check if Firebase is properly initialized
            if (db == null || auth == null) {
                Toast.makeText(this, "Firebase not initialized properly", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            // Initialize views
            postsRecyclerView = findViewById(R.id.posts_recycler_view);
            searchEditText = findViewById(R.id.search_edit_text);
            categorySpinner = findViewById(R.id.category_spinner);
            emptyStateLayout = findViewById(R.id.empty_state_layout);
            backButton = findViewById(R.id.back_button);
            fabAskDoubt = findViewById(R.id.fab_ask_doubt);

            // Check if views are properly initialized
            if (postsRecyclerView == null || searchEditText == null || categorySpinner == null || 
                emptyStateLayout == null || backButton == null || fabAskDoubt == null) {
                Toast.makeText(this, "Error initializing views", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }

            // Setup RecyclerView
            postsList = new ArrayList<>();
            postAdapter = new StdCommunityPostAdapter(this, postsList);
            if (postAdapter == null) {
                Toast.makeText(this, "Error initializing adapter", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            if (layoutManager == null) {
                Toast.makeText(this, "Error initializing layout manager", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            postsRecyclerView.setLayoutManager(layoutManager);
            postsRecyclerView.setAdapter(postAdapter);

            // Setup category spinner
            setupCategorySpinner();

            // Setup click listeners
            backButton.setOnClickListener(v -> finish());
            fabAskDoubt.setOnClickListener(v -> openAskDoubtActivity());
            
            // Setup search functionality
            searchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    filterPosts(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });

            // Load posts
            loadPosts();
            
        } catch (Exception e) {
            Toast.makeText(this, "Error initializing activity: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void setupCategorySpinner() {
        try {
            String[] categories = {"All Categories", "General", "Academic", "Technical", "Library", "Other"};
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(spinnerAdapter);

            categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    try {
                        String selectedCategory = categories[position];
                        if (position == 0) {
                            loadPosts(); // Load all posts
                        } else {
                            loadPostsByCategory(selectedCategory);
                        }
                    } catch (Exception e) {
                        Toast.makeText(StdDoubtsActivity.this, "Error selecting category: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {}
            });
        } catch (Exception e) {
            Toast.makeText(this, "Error setting up category spinner: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadPosts() {
        try {
            db.collection("community_posts")
                    .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            try {
                                if (task.isSuccessful()) {
                                    postsList.clear();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        StdCommunityPost post = document.toObject(StdCommunityPost.class);
                                        if (post != null) {
                                            post.setPostId(document.getId());
                                            postsList.add(post);
                                        }
                                    }
                                    postAdapter.notifyDataSetChanged();
                                    updateEmptyState();
                                } else {
                                    String errorMessage = "Error loading posts";
                                    if (task.getException() != null) {
                                        errorMessage += ": " + task.getException().getMessage();
                                    }
                                    Toast.makeText(StdDoubtsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(StdDoubtsActivity.this, "Error processing posts: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(this, "Error loading posts: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void loadPostsByCategory(String category) {
        try {
            db.collection("community_posts")
                    .whereEqualTo("category", category)
                    .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            try {
                                if (task.isSuccessful()) {
                                    postsList.clear();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        StdCommunityPost post = document.toObject(StdCommunityPost.class);
                                        if (post != null) {
                                            post.setPostId(document.getId());
                                            postsList.add(post);
                                        }
                                    }
                                    postAdapter.notifyDataSetChanged();
                                    updateEmptyState();
                                } else {
                                    String errorMessage = "Error loading posts";
                                    if (task.getException() != null) {
                                        errorMessage += ": " + task.getException().getMessage();
                                    }
                                    Toast.makeText(StdDoubtsActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                Toast.makeText(StdDoubtsActivity.this, "Error processing posts: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(this, "Error loading posts by category: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void filterPosts(String query) {
        try {
            List<StdCommunityPost> filteredList = new ArrayList<>();
            for (StdCommunityPost post : postsList) {
                if (post.getTitle().toLowerCase().contains(query.toLowerCase()) ||
                    post.getContent().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(post);
                }
            }
            postAdapter.updatePosts(filteredList);
            updateEmptyState();
        } catch (Exception e) {
            Toast.makeText(this, "Error filtering posts: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void updateEmptyState() {
        try {
            if (postsList.isEmpty()) {
                emptyStateLayout.setVisibility(View.VISIBLE);
                postsRecyclerView.setVisibility(View.GONE);
            } else {
                emptyStateLayout.setVisibility(View.GONE);
                postsRecyclerView.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error updating empty state: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void openAskDoubtActivity() {
        try {
            Intent intent = new Intent(this, StAskDoubtActivity.class);
            startActivityForResult(intent, 1001);
        } catch (Exception e) {
            Toast.makeText(this, "Error opening ask doubt activity: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            // Reload posts when returning to this activity
            loadPosts();
        } catch (Exception e) {
            Toast.makeText(this, "Error reloading posts: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            // Reload posts when a new doubt is posted
            loadPosts();
        }
    }
} 