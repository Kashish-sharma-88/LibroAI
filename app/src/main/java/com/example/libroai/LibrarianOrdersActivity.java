package com.example.libroai;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.*;

import java.util.*;

public class LibrarianOrdersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrdersAdapter adapter;
    private List<OrderModel> orderList;
    private LinearLayout emptyState;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_librarian_orders);

        recyclerView = findViewById(R.id.recycler_view);
        emptyState = findViewById(R.id.empty_state);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderList = new ArrayList<>();
        adapter = new OrdersAdapter(orderList, true);
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadOrders();
    }

    private void loadOrders() {
        db.collection("ordersForLibrarian").document("buy")
                .collection("requests")
                .get().addOnSuccessListener(buySnapshot -> {
                    for (DocumentSnapshot doc : buySnapshot.getDocuments()) {
                        orderList.add(OrderModel.fromDoc(doc, "Buy"));
                    }
                    db.collection("ordersForLibrarian").document("rent")
                            .collection("requests")
                            .get().addOnSuccessListener(rentSnapshot -> {
                                for (DocumentSnapshot doc : rentSnapshot.getDocuments()) {
                                    orderList.add(OrderModel.fromDoc(doc, "Rent"));
                                }
                                updateUI();
                            });
                });
    }

    private void updateUI() {
        if (orderList.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            adapter.notifyDataSetChanged();
            recyclerView.setVisibility(View.VISIBLE);
            emptyState.setVisibility(View.GONE);
        }
    }
}
