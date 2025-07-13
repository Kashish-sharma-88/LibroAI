package com.example.libroai;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FollowersListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FollowersAdapter adapter;
    private List<FollowUserModel> followerList;
    private LinearLayout emptyStateLayout;
    private String currentUserId;
    private static final String TAG = "FollowersListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers_list);

        recyclerView = findViewById(R.id.recycler_view_followers);
        emptyStateLayout = findViewById(R.id.empty_state_layout);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        followerList = new ArrayList<>();
        adapter = new FollowersAdapter(followerList, this);
        recyclerView.setAdapter(adapter);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            currentUserId = auth.getCurrentUser().getUid();
            loadFollowers();
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadFollowers() {
        FirebaseFirestore.getInstance()
                .collection("follows")
                .document(currentUserId)
                .collection("followers")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    followerList.clear();
                    if (querySnapshot.isEmpty()) {
                        adapter.notifyDataSetChanged();
                        emptyStateLayout.setVisibility(View.VISIBLE);
                        return;
                    }

                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        // ✅ Directly get data from the "followers" document itself
                        FollowUserModel user = new FollowUserModel();
                        user.setUserId(doc.getId());
                        user.setUserName(doc.getString("userName"));
                        user.setUserEmail(doc.getString("userEmail"));
                        user.setUserType(doc.getString("userType"));
                        user.setUserBio(doc.getString("userBio"));
                        user.setLibraryName(doc.getString("libraryName"));

                        if (user.getUserName() != null && !user.getUserName().isEmpty()) {
                            followerList.add(user);
                        } else {
                            Log.e(TAG, "❌ Incomplete user data for: " + user.getUserId());
                        }
                    }

                    adapter.notifyDataSetChanged();

                    if (followerList.isEmpty()) {
                        emptyStateLayout.setVisibility(View.VISIBLE);
                    } else {
                        emptyStateLayout.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Failed to load followers: " + e.getMessage());
                    Toast.makeText(this, "Failed to load followers", Toast.LENGTH_SHORT).show();
                });
    }
}
