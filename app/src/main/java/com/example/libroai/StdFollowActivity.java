package com.example.libroai;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.util.ArrayList;
import java.util.List;

public class StdFollowActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StdFollowAdapter adapter;
    private List<StdUserModel> userList;
    private LinearLayout emptyState;
    private TextView totalUsersCount;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_std_follow);

        recyclerView = findViewById(R.id.recycler_view_users);
        emptyState = findViewById(R.id.empty_state);
        totalUsersCount = findViewById(R.id.total_users_count);

        userList = new ArrayList<>();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            currentUserId = user.getUid();
        } else {
            currentUserId = "";
        }

        adapter = new StdFollowAdapter(userList, currentUserId);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fetchStudentsFromFirestore();
    }

    private void fetchStudentsFromFirestore() {
        FirebaseFirestore.getInstance()
                .collection("users")
                .whereEqualTo("role", "student")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    userList.clear();
                    for (DocumentSnapshot doc : querySnapshot.getDocuments()) {
                        StdUserModel user = doc.toObject(StdUserModel.class);
                        if (user != null && !doc.getId().equals(currentUserId)) {
                            user.setUserId(doc.getId());
                            userList.add(user);
                        }
                    }

                    adapter.updateList(userList);
                    totalUsersCount.setText(userList.size() + " Users Available");

                    if (userList.isEmpty()) {
                        recyclerView.setVisibility(View.GONE);
                        emptyState.setVisibility(View.VISIBLE);
                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        emptyState.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "\u274C Failed to load students", Toast.LENGTH_SHORT).show();
                });
    }
}
