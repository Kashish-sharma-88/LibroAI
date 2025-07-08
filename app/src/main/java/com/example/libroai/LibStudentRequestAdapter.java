package com.example.libroai;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class LibStudentRequestAdapter extends RecyclerView.Adapter<LibStudentRequestAdapter.ViewHolder> {

    private List<LibStudentRequestModel> requestList;

    public LibStudentRequestAdapter(List<LibStudentRequestModel> requestList) {
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lib_student_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LibStudentRequestModel request = requestList.get(position);
        
        holder.studentName.setText(request.getStudentName());
        holder.bookTitle.setText(request.getBookTitle());
        holder.studentEmail.setText(request.getStudentEmail());
        holder.requestMessage.setText(request.getRequestMessage());
        holder.requestDate.setText("Date: " + request.getRequestDate());
        holder.requestStatus.setText(request.getStatus());

        // Set status background color
        switch (request.getStatus().toLowerCase()) {
            case "pending":
                holder.requestStatus.setBackgroundResource(R.drawable.status_pending_bg);
                break;
            case "approved":
                holder.requestStatus.setBackgroundResource(R.drawable.button_approve_bg);
                break;
            case "rejected":
                holder.requestStatus.setBackgroundResource(R.drawable.button_reject_bg);
                break;
        }

        // Handle approve button click
        holder.btnApprove.setOnClickListener(v -> {
            updateRequestStatus(request.getRequestId(), "approved");
            Toast.makeText(v.getContext(), "Request approved!", Toast.LENGTH_SHORT).show();
            
            // Send notification to student (optional)
            sendNotificationToStudent(request.getStudentId(), "Your library request has been approved!", 
                                    "Request for " + request.getBookTitle() + " has been approved.");
        });

        // Handle reject button click
        holder.btnReject.setOnClickListener(v -> {
            updateRequestStatus(request.getRequestId(), "rejected");
            Toast.makeText(v.getContext(), "Request rejected!", Toast.LENGTH_SHORT).show();
            
            // Send notification to student (optional)
            sendNotificationToStudent(request.getStudentId(), "Your library request has been rejected.", 
                                    "Request for " + request.getBookTitle() + " has been rejected.");
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public void updateList(List<LibStudentRequestModel> newList) {
        this.requestList = newList;
        notifyDataSetChanged();
    }

    private void updateRequestStatus(String requestId, String newStatus) {
        FirebaseDatabase.getInstance().getReference("student_requests")
                .child(requestId)
                .child("status")
                .setValue(newStatus);
    }

    private void sendNotificationToStudent(String studentId, String title, String message) {
        // Create notification data
        DatabaseReference notificationRef = FirebaseDatabase.getInstance()
                .getReference("notifications")
                .child(studentId)
                .push();
        
        // Create notification data as Map
        java.util.Map<String, Object> notificationData = new java.util.HashMap<>();
        notificationData.put("title", title);
        notificationData.put("message", message);
        notificationData.put("timestamp", String.valueOf(System.currentTimeMillis()));
        notificationData.put("isRead", false);
        
        notificationRef.setValue(notificationData);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentName, bookTitle, studentEmail, requestMessage, requestDate, requestStatus;
        Button btnApprove, btnReject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.student_name);
            bookTitle = itemView.findViewById(R.id.book_title);
            studentEmail = itemView.findViewById(R.id.student_email);
            requestMessage = itemView.findViewById(R.id.request_message);
            requestDate = itemView.findViewById(R.id.request_date);
            requestStatus = itemView.findViewById(R.id.request_status);
            btnApprove = itemView.findViewById(R.id.btn_approve);
            btnReject = itemView.findViewById(R.id.btn_reject);
        }
    }
} 