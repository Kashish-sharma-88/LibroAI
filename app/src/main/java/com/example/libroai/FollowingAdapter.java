package com.example.libroai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.ViewHolder> {

    private final List<FollowUserModel> followingList;
    private final Context context;

    public FollowingAdapter(List<FollowUserModel> followingList, Context context) {
        this.followingList = followingList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_profile_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FollowUserModel user = followingList.get(position);

        holder.name.setText(user.getUserName());
        holder.email.setText(user.getUserEmail());

        // Bio visibility
        String bio = user.getUserBio();
        if (bio != null && !bio.trim().isEmpty()) {
            holder.bio.setVisibility(View.VISIBLE);
            holder.bio.setText(bio);
        } else {
            holder.bio.setVisibility(View.GONE);
        }

        // Role label
        if ("librarian".equalsIgnoreCase(user.getUserType())) {
            holder.role.setText("📚 Librarian");
        } else {
            holder.role.setText("👤 Student");
        }
    }

    @Override
    public int getItemCount() {
        return followingList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, bio, role;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name  = itemView.findViewById(R.id.user_name);
            email = itemView.findViewById(R.id.user_email);
            bio   = itemView.findViewById(R.id.user_bio);
            role  = itemView.findViewById(R.id.user_role);
        }
    }
}
