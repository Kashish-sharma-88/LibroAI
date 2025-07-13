package com.example.libroai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StdGroupMembersAdapter extends RecyclerView.Adapter<StdGroupMembersAdapter.ViewHolder> {
    
    private List<String> membersList;
    private Context context;
    
    public StdGroupMembersAdapter(Context context, List<String> membersList) {
        this.context = context;
        this.membersList = membersList;
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_std_group_member, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String memberName = membersList.get(position);
        holder.memberName.setText(memberName);
        
        // Add member number
        holder.memberNumber.setText("#" + (position + 1));
    }
    
    @Override
    public int getItemCount() {
        return membersList.size();
    }
    
    public void updateList(List<String> newList) {
        this.membersList = newList;
        notifyDataSetChanged();
    }
    
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView memberName, memberNumber;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            memberName = itemView.findViewById(R.id.member_name);
            memberNumber = itemView.findViewById(R.id.member_number);
        }
    }
} 