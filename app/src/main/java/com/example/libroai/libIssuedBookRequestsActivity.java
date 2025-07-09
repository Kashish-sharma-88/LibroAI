package com.example.libroai;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

public class libIssuedBookRequestsActivity extends AppCompatActivity {
    private RecyclerView recyclerRequests;
    private IssuedBookRequestsAdapter adapter;
    private List<IssuedBookRequestModel> requestList = new ArrayList<>();
    private FirebaseFirestore db;
    private ListenerRegistration listenerRegistration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lib_issued_book_requests);

        recyclerRequests = findViewById(R.id.recycler_requests);
        recyclerRequests.setLayoutManager(new LinearLayoutManager(this));
        adapter = new IssuedBookRequestsAdapter(this, requestList);
        recyclerRequests.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        listenToRequests();
    }

    private void listenToRequests() {
        if (listenerRegistration != null) listenerRegistration.remove();
        listenerRegistration = db.collection("Issued Books")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .addSnapshotListener((snapshots, error) -> {
                if (error != null || snapshots == null) return;
                List<IssuedBookRequestModel> newList = new ArrayList<>();
                for (QueryDocumentSnapshot doc : snapshots) {
                    IssuedBookRequestModel req = doc.toObject(IssuedBookRequestModel.class);
                    req.setRequestId(doc.getId());
                    newList.add(req);
                }
                adapter.setRequestList(newList);
            });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (listenerRegistration != null) listenerRegistration.remove();
    }
} 