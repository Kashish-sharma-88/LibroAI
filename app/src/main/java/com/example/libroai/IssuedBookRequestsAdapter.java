package com.example.libroai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.List;

public class IssuedBookRequestsAdapter extends RecyclerView.Adapter<IssuedBookRequestsAdapter.RequestViewHolder> {
    private List<IssuedBookRequestModel> requestList;
    private Context context;

    public IssuedBookRequestsAdapter(Context context, List<IssuedBookRequestModel> requestList) {
        this.context = context;
        this.requestList = requestList;
    }

    @NonNull
    @Override
    public RequestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_issued_book_request, parent, false);
        return new RequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestViewHolder holder, int position) {
        IssuedBookRequestModel request = requestList.get(position);
        holder.tvBookTitle.setText(request.getBookTitle());
        holder.tvBookAuthor.setText(request.getBookAuthor());
        holder.tvStudentId.setText(request.getStudentId());
        Glide.with(context).load(request.getBookImageUrl()).placeholder(R.drawable.img_2).into(holder.imgBook);

        String status = request.getStatus();
        if ("pending".equals(status)) {
            holder.btnApprove.setVisibility(View.VISIBLE);
            holder.btnReject.setVisibility(View.VISIBLE);
            holder.btnApprove.setEnabled(true);
            holder.btnReject.setEnabled(true);
            holder.btnApprove.setText("Approve");
            holder.btnReject.setText("Reject");
            holder.btnApprove.setBackgroundResource(R.drawable.rounded_button_brown);
            holder.btnApprove.setTextColor(context.getResources().getColor(R.color.white));
            holder.btnApprove.setBackgroundTintList(null);

            holder.btnReject.setBackgroundResource(R.drawable.rounded_button_brown);
            holder.btnReject.setTextColor(context.getResources().getColor(R.color.white));
            holder.btnReject.setBackgroundTintList(null);
        } else if ("approved".equals(status)) {
            holder.btnApprove.setVisibility(View.VISIBLE);
            holder.btnReject.setVisibility(View.GONE);
            holder.btnApprove.setEnabled(false);
            holder.btnApprove.setText("Approved");
            holder.btnApprove.setBackgroundResource(R.color.brown_primary);
            holder.btnApprove.setTextColor(context.getResources().getColor(R.color.white));
        } else if ("rejected".equals(status)) {
            holder.btnApprove.setVisibility(View.GONE);
            holder.btnReject.setVisibility(View.VISIBLE);
            holder.btnReject.setEnabled(false);
            holder.btnReject.setText("Rejected");
            holder.btnReject.setBackgroundResource(android.R.color.holo_red_dark);
            holder.btnReject.setTextColor(context.getResources().getColor(R.color.white));
        }

        holder.btnApprove.setOnClickListener(v -> updateStatus(request, "approved"));
        holder.btnReject.setOnClickListener(v -> updateStatus(request, "rejected"));
    }

    private void updateStatus(IssuedBookRequestModel request, String status) {
        FirebaseFirestore.getInstance()
            .collection("Issued Books")
            .document(request.getRequestId())
            .update("status", status);
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public void setRequestList(List<IssuedBookRequestModel> newList) {
        this.requestList = newList;
        notifyDataSetChanged();
    }

    static class RequestViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBook;
        TextView tvBookTitle, tvBookAuthor, tvStudentId;
        Button btnApprove, btnReject;
        public RequestViewHolder(@NonNull View itemView) {
            super(itemView);
            imgBook = itemView.findViewById(R.id.img_book);
            tvBookTitle = itemView.findViewById(R.id.tv_book_title);
            tvBookAuthor = itemView.findViewById(R.id.tv_book_author);
            tvStudentId = itemView.findViewById(R.id.tv_student_id);
            btnApprove = itemView.findViewById(R.id.btn_approve);
            btnReject = itemView.findViewById(R.id.btn_reject);
        }
    }
} 