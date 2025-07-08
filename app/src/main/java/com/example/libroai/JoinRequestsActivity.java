package com.example.libroai;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JoinRequestsActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private JoinRequestsAdapter adapter;
    private List<JoinRequestModel> requestList;
    private LinearLayout emptyState;
    private TextView statsPending, statsApproved, statsRejected;
    private FirebaseFirestore db;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_requests);
        
        // Initialize Firestore
        db = FirebaseFirestore.getInstance();
        
        // Initialize views
        recyclerView = findViewById(R.id.recycler_view_requests);
        emptyState = findViewById(R.id.empty_state);
        statsPending = findViewById(R.id.stats_pending);
        statsApproved = findViewById(R.id.stats_approved);
        statsRejected = findViewById(R.id.stats_rejected);
        
        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("All Join Requests");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
        
        // Setup RecyclerView
        requestList = new ArrayList<>();
        adapter = new JoinRequestsAdapter(requestList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        
        // Fetch all join requests from Firestore
        fetchJoinRequests();
    }
    
    private void fetchJoinRequests() {
        db.collection("join_request")
                .orderBy("requestDate", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(this::onRequestsFetched)
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error fetching requests: " + e.getMessage(), 
                                 Toast.LENGTH_SHORT).show();
                });
    }
    
    private void onRequestsFetched(QuerySnapshot queryDocumentSnapshots) {
        requestList.clear();
        int pendingCount = 0, approvedCount = 0, rejectedCount = 0;
        
        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
            JoinRequestModel request = document.toObject(JoinRequestModel.class);
            if (request != null) {
                requestList.add(request);
                
                // Count by status
                switch (request.getStatus().toLowerCase()) {
                    case "pending":
                        pendingCount++;
                        break;
                    case "approved":
                        approvedCount++;
                        break;
                    case "rejected":
                        rejectedCount++;
                        break;
                }
            }
        }
        
        // Update stats
        statsPending.setText(String.valueOf(pendingCount));
        statsApproved.setText(String.valueOf(approvedCount));
        statsRejected.setText(String.valueOf(rejectedCount));
        
        // Update UI based on data
        if (requestList.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyState.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
            adapter.updateList(requestList);
        }
    }
    
    public void updateRequestStatus(String requestId, String newStatus, String responseMessage) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        
        // Update the request in Firestore
        db.collection("join_request").document(requestId)
                .update("status", newStatus,
                        "responseDate", Timestamp.now(),
                        "responseMessage", responseMessage)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(this, "Request " + newStatus + " successfully!", 
                                 Toast.LENGTH_SHORT).show();
                    // Refresh the list
                    fetchJoinRequests();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error updating request: " + e.getMessage(), 
                                 Toast.LENGTH_SHORT).show();
                });
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
} 