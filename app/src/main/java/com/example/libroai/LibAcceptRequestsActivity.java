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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LibAcceptRequestsActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private LibStudentRequestAdapter adapter;
    private List<LibStudentRequestModel> requestList;
    private LinearLayout emptyState;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib_accept_requests);
        
        // Initialize views
        recyclerView = findViewById(R.id.recycler_view_requests);
        emptyState = findViewById(R.id.empty_state);
        
        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Student Requests");
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }
        
        // Setup RecyclerView
        requestList = new ArrayList<>();
        adapter = new LibStudentRequestAdapter(requestList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        
        // Fetch student requests from Firebase
        fetchStudentRequests();
    }
    
    private void fetchStudentRequests() {
        FirebaseDatabase.getInstance().getReference("student_requests")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        requestList.clear();
                        for (DataSnapshot snap : snapshot.getChildren()) {
                            LibStudentRequestModel request = snap.getValue(LibStudentRequestModel.class);
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
                        Toast.makeText(LibAcceptRequestsActivity.this, 
                                "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
} 