package com.example.libroai;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class StdFollowActivity extends AppCompatActivity {
    
    private RecyclerView recyclerView;
    private StdFollowAdapter adapter;
    private List<StdUserModel> userList;
    private LinearLayout emptyState;
    private TextView totalUsersCount;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_follow);
        
        Toast.makeText(this, "Follow Activity Opened!", Toast.LENGTH_SHORT).show();
        
        // Initialize views
        recyclerView = findViewById(R.id.recycler_view_users);
        emptyState = findViewById(R.id.empty_state);
        totalUsersCount = findViewById(R.id.total_users_count);
        
        // Setup RecyclerView
        userList = new ArrayList<>();
        adapter = new StdFollowAdapter(userList, "");
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        
        // Add dummy data
        addDummyData();
    }
    
    private void addDummyData() {
        // Add some dummy users for testing
        userList.add(new StdUserModel("1", "John Student", "john@email.com", "student", "", "I love reading books!", "", false, 5, 10));
        userList.add(new StdUserModel("2", "Sarah Librarian", "sarah@email.com", "librarian", "", "Library manager", "Central Library", false, 15, 8));
        userList.add(new StdUserModel("3", "Mike Reader", "mike@email.com", "student", "", "Book enthusiast", "", false, 3, 12));
        userList.add(new StdUserModel("4", "Emma Writer", "emma@email.com", "student", "", "Creative writer", "", false, 8, 15));
        userList.add(new StdUserModel("5", "David Library", "david@email.com", "librarian", "", "Senior librarian", "City Library", false, 25, 12));
        
        adapter.updateList(userList);
        
        if (totalUsersCount != null) {
            totalUsersCount.setText(userList.size() + " Users Available");
        }
        
        recyclerView.setVisibility(View.VISIBLE);
        emptyState.setVisibility(View.GONE);
    }
} 