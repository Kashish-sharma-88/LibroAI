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

import java.util.List;

public class LibStudentRecordsAdapter extends RecyclerView.Adapter<LibStudentRecordsAdapter.ViewHolder> {

    private List<LibStudentRecordModel> recordsList;
    private Context context;
    private OnRecordClickListener listener;

    public interface OnRecordClickListener {
        void onViewDetailsClick(LibStudentRecordModel record);
    }

    public LibStudentRecordsAdapter(Context context, List<LibStudentRecordModel> recordsList, OnRecordClickListener listener) {
        this.context = context;
        this.recordsList = recordsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_student_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LibStudentRecordModel record = recordsList.get(position);
        
        // Set student info
        holder.studentNameText.setText(record.getStudentName());
        holder.studentEmailText.setText(record.getStudentEmail());
        
        // Set book info
        holder.bookTitleText.setText(record.getBookTitle());
        holder.bookAuthorText.setText(record.getBookAuthor());
        holder.bookCategoryText.setText(record.getBookCategory());
        
        // Set dates
        holder.issueDateText.setText(record.getIssueDate());
        holder.returnDateText.setText(record.getReturnDate());
        
        // Set status
        holder.statusText.setText(record.getStatus());
        if (record.getStatus().equals("ISSUED")) {
            holder.statusText.setBackgroundResource(R.drawable.rounded_button_green);
        } else if (record.getStatus().equals("OVERDUE")) {
            holder.statusText.setBackgroundResource(R.drawable.rounded_button_red);
        } else if (record.getStatus().equals("RETURNED")) {
            holder.statusText.setBackgroundResource(R.drawable.rounded_button_grey);
        }
        
        // Set days remaining and fine
        holder.daysRemainingText.setText(record.getDaysRemaining() + " days");
        holder.fineAmountText.setText("₹" + record.getFineAmount());
        
        // Set click listener
        holder.viewDetailsButton.setOnClickListener(v -> {
            if (listener != null) {
                listener.onViewDetailsClick(record);
            }
        });
    }

    @Override
    public int getItemCount() {
        return recordsList.size();
    }

    public void updateData(List<LibStudentRecordModel> newRecords) {
        this.recordsList = newRecords;
        notifyDataSetChanged();
    }

    public void filterData(List<LibStudentRecordModel> filteredRecords) {
        this.recordsList = filteredRecords;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView studentNameText, studentEmailText, bookTitleText, bookAuthorText, bookCategoryText;
        TextView issueDateText, returnDateText, statusText, daysRemainingText, fineAmountText;
        Button viewDetailsButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            
            studentNameText = itemView.findViewById(R.id.studentNameText);
            studentEmailText = itemView.findViewById(R.id.studentEmailText);
            bookTitleText = itemView.findViewById(R.id.bookTitleText);
            bookAuthorText = itemView.findViewById(R.id.bookAuthorText);
            bookCategoryText = itemView.findViewById(R.id.bookCategoryText);
            issueDateText = itemView.findViewById(R.id.issueDateText);
            returnDateText = itemView.findViewById(R.id.returnDateText);
            statusText = itemView.findViewById(R.id.statusText);
            daysRemainingText = itemView.findViewById(R.id.daysRemainingText);
            fineAmountText = itemView.findViewById(R.id.fineAmountText);
            viewDetailsButton = itemView.findViewById(R.id.viewDetailsButton);
        }
    }
} 