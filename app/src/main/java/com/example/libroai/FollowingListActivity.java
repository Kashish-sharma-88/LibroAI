package com.example.libroai;

import android.os.Bundle;
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

public class FollowingListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FollowingAdapter adapter;
    private List<FollowUserModel> followingList;
    private LinearLayout emptyStateLayout;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_following_list);

        recyclerView = findViewById(R.id.recycler_view_following);
        emptyStateLayout = findViewById(R.id.empty_state_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        followingList = new ArrayList<>();
        adapter = new FollowingAdapter(followingList, this); // ✅ Correct adapter now used
        recyclerView.setAdapter(adapter);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        loadFollowing();
    }

    private void loadFollowing() {
        FirebaseFirestore.getInstance()
                .collection("follows")
                .document(currentUserId)
                .collection("following")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    followingList.clear();

                    if (querySnapshot.isEmpty()) {
                        emptyStateLayout.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                        return;
                    }

                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        String followedUserId = doc.getId();

                        FirebaseFirestore.getInstance()
                                .collection("users")
                                .document(followedUserId)
                                .get()
                                .addOnSuccessListener(userDoc -> {
                                    FollowUserModel user = userDoc.toObject(FollowUserModel.class);
                                    if (user != null) {
                                        user.setUserId(followedUserId);
                                        followingList.add(user);
                                        adapter.notifyDataSetChanged();
                                        emptyStateLayout.setVisibility(View.GONE);
                                    }
                                });
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Failed to load following", Toast.LENGTH_SHORT).show());
    }
}
