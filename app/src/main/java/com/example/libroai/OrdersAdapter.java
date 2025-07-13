package com.example.libroai;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrderViewHolder> {

    private final List<OrderModel> orderList;
    private final boolean isLibrarian;

    public OrdersAdapter(List<OrderModel> orderList, boolean isLibrarian) {
        this.orderList = orderList;
        this.isLibrarian = isLibrarian;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                isLibrarian ? R.layout.item_librarian_order : R.layout.item_student_order,
                parent, false
        );
        return new OrderViewHolder(view, isLibrarian);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderModel order = orderList.get(position);

        holder.bookTitle.setText(order.getTitle());
        holder.bookAuthor.setText("by " + order.getAuthor());
        holder.orderType.setText(order.getOrderType());
        holder.orderStatus.setText("Status: " + order.getStatus());

        if ("Delivery".equalsIgnoreCase(order.getDeliveryType())) {
            holder.orderAddress.setText("Delivery Address: " + order.getAddress());
        } else {
            holder.orderAddress.setText("Pickup from library");
        }

        // ✅ Show Return Date or Delivery Date (whichever available)
        if ("Approved".equalsIgnoreCase(order.getStatus()) &&
                order.getDeliveryDate() != null &&
                !order.getDeliveryDate().isEmpty()) {

            holder.returnDate.setVisibility(View.VISIBLE);
            holder.returnDate.setText("Delivery by: " + order.getDeliveryDate());

        } else if (order.getReturnDate() != null && !order.getReturnDate().isEmpty()) {
            // For Rent orders, show return date
            holder.returnDate.setVisibility(View.VISIBLE);
            holder.returnDate.setText("Return by: " + order.getReturnDate());

        } else {
            holder.returnDate.setVisibility(View.GONE);
        }



        if (isLibrarian && holder.btnUpdateStatus != null) {
            if ("Completed".equalsIgnoreCase(order.getStatus())) {
                holder.btnUpdateStatus.setVisibility(View.GONE);
            } else {
                holder.btnUpdateStatus.setVisibility(View.VISIBLE);
                holder.btnUpdateStatus.setOnClickListener(v -> {

                    String studentUid = order.getStudentUid();
                    String docId = order.getDocId();
                    String type = order.getOrderType() != null ? order.getOrderType().toLowerCase() : "buy";

                    if (studentUid == null || studentUid.isEmpty() || docId == null || docId.isEmpty()) {
                        Toast.makeText(holder.itemView.getContext(),
                                "❌ Cannot update: missing studentUid or docId",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    FirebaseFirestore db = FirebaseFirestore.getInstance();

                    Calendar calendar = Calendar.getInstance();
                    calendar.add(Calendar.DAY_OF_YEAR, 10);
                    String deliveryDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(calendar.getTime());

                    db.collection("orders")
                            .document(studentUid)
                            .collection(type)
                            .document(docId)
                            .update(Map.of(
                                    "status", "Approved",
                                    "deliveryDate", deliveryDate
                            ))
                            .addOnSuccessListener(aVoid -> {
                                // 2️⃣ Update librarian-side order
                                db.collection("ordersForLibrarian")
                                        .document(type) // rent or buy
                                        .collection("requests")
                                        .document(docId)
                                        .update(Map.of(
                                                "status", "Approved",
                                                "deliveryDate", deliveryDate
                                        ))
                                        .addOnSuccessListener(unused -> {
                                            Toast.makeText(holder.itemView.getContext(),
                                                    "✅ Order Approved", Toast.LENGTH_SHORT).show();
                                            holder.orderStatus.setText("Status: Completed");
                                            holder.btnUpdateStatus.setVisibility(View.GONE);
                                        })
                                        .addOnFailureListener(e -> Toast.makeText(holder.itemView.getContext(),
                                                "⚠️ Librarian update failed: " + e.getMessage(), Toast.LENGTH_LONG).show());
                            })
                            .addOnFailureListener(e -> Toast.makeText(holder.itemView.getContext(),
                                    "❌ Student update failed: " + e.getMessage(), Toast.LENGTH_LONG).show());

                });

            }
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView bookTitle, bookAuthor, orderType, orderStatus, orderAddress, returnDate;
        Button btnUpdateStatus;

        public OrderViewHolder(@NonNull View itemView, boolean isLibrarian) {
            super(itemView);
            bookTitle = itemView.findViewById(R.id.book_title);
            bookAuthor = itemView.findViewById(R.id.book_author);
            orderType = itemView.findViewById(R.id.order_type);
            orderStatus = itemView.findViewById(R.id.order_status);
            orderAddress = itemView.findViewById(R.id.order_address);
            returnDate = itemView.findViewById(R.id.return_date);
            btnUpdateStatus = isLibrarian ? itemView.findViewById(R.id.btn_update_status) : null;
        }
    }
}
