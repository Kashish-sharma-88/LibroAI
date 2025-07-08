package com.example.libroai;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StdRequestStatusActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private StdRequestStatusAdapter adapter;
    private List<StdLibraryRequestModel> requestList;
    private LinearLayout emptyState;
    private FirebaseUser currentUser;
    private ValueEventListener requestListener;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_request_status);
        
        // Initialize Firebase user
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Please login first", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Initialize views
        recyclerView = findViewById(R.id.recycler_view_requests);
        emptyState = findViewById(R.id.empty_state);
        
        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("My Library Requests");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
        
        // Setup RecyclerView
        requestList = new ArrayList<>();
        adapter = new StdRequestStatusAdapter(requestList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        
        // Fetch student's requests from Firebase with real-time updates
        fetchStudentRequests();
    }
    
    private void fetchStudentRequests() {
        // Remove previous listener if exists
        if (requestListener != null) {
            FirebaseDatabase.getInstance().getReference("student_requests").removeEventListener(requestListener);
        }
        
        requestListener = FirebaseDatabase.getInstance().getReference("student_requests")
                .orderByChild("studentId")
                .equalTo(currentUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        requestList.clear();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            StdLibraryRequestModel request = snap.getValue(StdLibraryRequestModel.class);
                            if (request != null) {
                                requestList.add(request);
                            }
                        }
                        
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

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(StdRequestStatusActivity.this, 
                                "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove listener when activity is destroyed
        if (requestListener != null) {
            FirebaseDatabase.getInstance().getReference("student_requests").removeEventListener(requestListener);
        }
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
} 