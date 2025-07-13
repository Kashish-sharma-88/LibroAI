package com.example.libroai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class FollowRequestAdapter extends RecyclerView.Adapter<FollowRequestAdapter.RequestViewHolder> {

    private List<FollowUserModel> requestList;
    private Context context;

    public FollowRequestAdapter(List<FollowUserModel> requestList, Context context) {
        this.requestList = requestList;
        this.context = context;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_follow_requests, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        FollowUserModel requester = requestList.get(position);

        holder.name.setText(requester.getUserName());
        holder.email.setText(requester.getUserEmail());

        holder.acceptBtn.setOnClickListener(v -> acceptRequest(requester, position));
        holder.rejectBtn.setOnClickListener(v -> rejectRequest(requester, position));
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    private void acceptRequest(FollowUserModel requester, int position) {
        String requesterId = requester.getUserId();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Step 1: Add to followers (store full data of requester)
        db.collection("follows")
                .document(currentUserId)
                .collection("followers")
                .document(requesterId)
                .set(new java.util.HashMap<String, Object>() {{
                    put("userName", requester.getUserName());
                    put("userEmail", requester.getUserEmail());
                    put("userType", requester.getUserType());
                    put("userBio", requester.getUserBio());
                    put("libraryName", requester.getLibraryName());
                }});

        // Step 2: Add to following (you can just track timestamp or keep empty)
        db.collection("follows")
                .document(requesterId)
                .collection("following")
                .document(currentUserId)
                .set(new java.util.HashMap<String, Object>() {{
                    put("timestamp", System.currentTimeMillis());
                }});

        // Step 3: Remove the follow request
        db.collection("users")
                .document(currentUserId)
                .collection("followRequests")
                .document(requesterId)
                .delete();

        // Step 4: Send Notification
        NotificationHelper.sendNotification(
                requesterId,
                "Your follow request was accepted by " + requester.getUserName(),
                currentUserId,
                "request_accepted"
        );

        // Step 5: UI update
        Toast.makeText(context, "Request Accepted", Toast.LENGTH_SHORT).show();
        requestList.remove(position);
        notifyItemRemoved(position);
    }


    private void rejectRequest(FollowUserModel requester, int position) {
        String requesterId = requester.getUserId();
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users")
                .document(currentUserId)
                .collection("followRequests")
                .document(requesterId)
                .delete()
                .addOnSuccessListener(unused -> {
                    db.collection("users")
                            .document(currentUserId)
                            .get()
                            .addOnSuccessListener(currentUserDoc -> {
                                String currentUserName = currentUserDoc.getString("userName");

                                NotificationHelper.sendNotification(
                                        requesterId,
                                        "Your follow request was rejected by " + currentUserName,
                                        currentUserId,
                                        "request_rejected"
                                );
                            });

                    Toast.makeText(context, "Request Rejected", Toast.LENGTH_SHORT).show();
                    requestList.remove(position);
                    notifyItemRemoved(position);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(context, "Failed to reject request", Toast.LENGTH_SHORT).show());
    }

    public static class RequestViewHolder extends RecyclerView.ViewHolder {
        TextView name, email;
        Button acceptBtn, rejectBtn;

        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.request_name);
            email = itemView.findViewById(R.id.request_email);
            acceptBtn = itemView.findViewById(R.id.btn_accept);
            rejectBtn = itemView.findViewById(R.id.btn_reject);
        }
    }
}
