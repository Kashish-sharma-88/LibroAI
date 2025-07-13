package com.example.libroai;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.*;

import java.util.*;

public class StudentOrdersActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private OrdersAdapter adapter;
    private List<OrderModel> orderList;
    private LinearLayout emptyState;
    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_orders);

        recyclerView = findViewById(R.id.recycler_view);
        emptyState = findViewById(R.id.empty_state);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        orderList = new ArrayList<>();
        adapter = new OrdersAdapter(orderList, false);  // false = student
        recyclerView.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
        loadOrders();
    }

    private void loadOrders() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("orders").document(uid).collection("buy")
                .get().addOnSuccessListener(buySnapshot -> {
                    for (DocumentSnapshot doc : buySnapshot.getDocuments()) {
                        OrderModel model = OrderModel.fromDoc(doc, "Buy");

                        // ✅ Make sure to force-set correct UID & docId
                        model.setStudentUid(uid);       // 🛠️ force assign again
                        model.setDocId(doc.getId());    // 🛠️ force assign again

                        orderList.add(model);
                    }

                    db.collection("orders").document(uid).collection("rent")
                            .get().addOnSuccessListener(rentSnapshot -> {
                                for (DocumentSnapshot doc : rentSnapshot.getDocuments()) {
                                    OrderModel model = OrderModel.fromDoc(doc, "Rent");

                                    model.setStudentUid(uid);       // 🛠️ force assign again
                                    model.setDocId(doc.getId());    // 🛠️ force assign again

                                    orderList.add(model);
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
