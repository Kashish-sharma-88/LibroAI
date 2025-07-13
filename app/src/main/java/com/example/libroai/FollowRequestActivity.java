package com.example.libroai;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FollowRequestActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FollowRequestAdapter adapter;
    private List<FollowUserModel> requestList;
    private LinearLayout emptyStateLayout;

    private FirebaseFirestore db;
    private String currentUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_request);

        recyclerView = findViewById(R.id.recycler_view_requests);
        emptyStateLayout = findViewById(R.id.empty_state_layout);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestList = new ArrayList<>();
        adapter = new FollowRequestAdapter(requestList, this);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        loadFollowRequests();
    }

    private void loadFollowRequests() {
        db.collection("users")
                .document(currentUserId)
                .collection("followRequests")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    requestList.clear();

                    if (querySnapshot.isEmpty()) {
                        emptyStateLayout.setVisibility(View.VISIBLE);
                        adapter.notifyDataSetChanged();
                        return;
                    }

                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        String senderId = doc.getString("senderId");
                        String senderName = doc.getString("senderName");
                        String senderEmail = doc.getString("senderEmail");

                        if (senderId == null || senderId.isEmpty()) continue;

                        FollowUserModel user = new FollowUserModel();
                        user.setUserId(senderId);
                        user.setUserName(senderName != null ? senderName : "Unknown");
                        user.setUserEmail(senderEmail != null ? senderEmail : "N/A");

                        requestList.add(user);
                    }

                    if (requestList.isEmpty()) {
                        emptyStateLayout.setVisibility(View.VISIBLE);
                    } else {
                        emptyStateLayout.setVisibility(View.GONE);
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load follow requests: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
