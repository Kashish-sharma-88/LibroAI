package com.example.libroai;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class JoinRequestsAdapter extends RecyclerView.Adapter<JoinRequestsAdapter.ViewHolder> {

    private List<JoinRequestModel> requestList;
    private Context context;
    private JoinRequestsActivity activity;

    public JoinRequestsAdapter(List<JoinRequestModel> requestList, JoinRequestsActivity activity) {
        this.requestList = requestList;
        this.activity = activity;
        this.context = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_join_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JoinRequestModel request = requestList.get(position);
        
        holder.studentName.setText(request.getStudentName());
        holder.studentEmail.setText(request.getStudentEmail());
        holder.libraryName.setText(request.getLibraryName());
        holder.requestMessage.setText(request.getRequestMessage());
        holder.requestStatus.setText(request.getStatus().toUpperCase());
        
        // Format and display date
        if (request.getRequestDate() != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
            String dateStr = sdf.format(request.getRequestDate().toDate());
            holder.requestDate.setText("Requested: " + dateStr);
        }
        
        // Set status background color
        switch (request.getStatus().toLowerCase()) {
            case "pending":
                holder.requestStatus.setBackgroundResource(R.drawable.status_pending_bg);
                holder.actionButtons.setVisibility(View.VISIBLE);
                break;
            case "approved":
                holder.requestStatus.setBackgroundResource(R.drawable.button_approve_bg);
                holder.actionButtons.setVisibility(View.GONE);
                break;
            case "rejected":
                holder.requestStatus.setBackgroundResource(R.drawable.button_reject_bg);
                holder.actionButtons.setVisibility(View.GONE);
                break;
        }
        
        // Show response info if available
        if (request.getResponseDate() != null && request.getResponseMessage() != null) {
            holder.responseInfo.setVisibility(View.VISIBLE);
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault());
            String responseDateStr = sdf.format(request.getResponseDate().toDate());
            holder.responseDate.setText("Responded: " + responseDateStr);
            holder.responseMessage.setText(request.getResponseMessage());
        } else {
            holder.responseInfo.setVisibility(View.GONE);
        }

        // Handle approve button click
        holder.btnApprove.setOnClickListener(v -> {
            showResponseDialog(request.getRequestId(), "approved", "Approve Request");
        });

        // Handle reject button click
        holder.btnReject.setOnClickListener(v -> {
            showResponseDialog(request.getRequestId(), "rejected", "Reject Request");
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public void updateList(List<JoinRequestModel> newList) {
        this.requestList = newList;
        notifyDataSetChanged();
    }

    private void showResponseDialog(String requestId, String status, String title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        // Set up the input
        final EditText input = new EditText(context);
        input.setHint("Add a response message (optional)");
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton(status.equals("approved") ? "Approve" : "Reject", (dialog, which) -> {
            String responseMessage = input.getText().toString().trim();
            if (responseMessage.isEmpty()) {
                responseMessage = status.equals("approved") ? "Request approved" : "Request rejected";
            }
            activity.updateRequestStatus(requestId, status, responseMessage);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentName, studentEmail, libraryName, requestMessage, requestDate, requestStatus;
        TextView responseDate, responseMessage;
        LinearLayout actionButtons, responseInfo;
        Button btnApprove, btnReject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            studentName = itemView.findViewById(R.id.student_name);
            studentEmail = itemView.findViewById(R.id.student_email);
            libraryName = itemView.findViewById(R.id.library_name);
            requestMessage = itemView.findViewById(R.id.request_message);
            requestDate = itemView.findViewById(R.id.request_date);
            requestStatus = itemView.findViewById(R.id.request_status);
            responseDate = itemView.findViewById(R.id.response_date);
            responseMessage = itemView.findViewById(R.id.response_message);
            actionButtons = itemView.findViewById(R.id.action_buttons);
            responseInfo = itemView.findViewById(R.id.response_info);
            btnApprove = itemView.findViewById(R.id.btn_approve);
            btnReject = itemView.findViewById(R.id.btn_reject);
        }
    }
} 