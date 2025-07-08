package com.example.libroai;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StdRequestStatusAdapter extends RecyclerView.Adapter<StdRequestStatusAdapter.ViewHolder> {

    private List<StdLibraryRequestModel> requestList;

    public StdRequestStatusAdapter(List<StdLibraryRequestModel> requestList) {
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_std_request_status, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StdLibraryRequestModel request = requestList.get(position);
        
        holder.libraryName.setText(request.getLibraryName());
        holder.requestDate.setText("Requested on: " + request.getRequestDate());
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
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public void updateList(List<StdLibraryRequestModel> newList) {
        this.requestList = newList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView libraryName, requestDate, requestStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            libraryName = itemView.findViewById(R.id.library_name);
            requestDate = itemView.findViewById(R.id.request_date);
            requestStatus = itemView.findViewById(R.id.request_status);
        }
    }
} 