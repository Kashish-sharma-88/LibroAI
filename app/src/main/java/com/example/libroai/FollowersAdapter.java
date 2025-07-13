package com.example.libroai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.ViewHolder> {

    private final List<FollowUserModel> followersList;
    private final Context context;

    public FollowersAdapter(List<FollowUserModel> followersList, Context context) {
        this.followersList = followersList;
        this.context = context;
    }

    @NonNull
    @Override
    public FollowersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_profile_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FollowersAdapter.ViewHolder holder, int position) {
        FollowUserModel user = followersList.get(position);

        holder.name.setText(user.getUserName() != null ? user.getUserName() : "Unnamed");
        holder.email.setText(user.getUserEmail() != null ? user.getUserEmail() : "No Email");

        if (user.getUserBio() != null && !user.getUserBio().isEmpty()) {
            holder.bio.setVisibility(View.VISIBLE);
            holder.bio.setText(user.getUserBio());
        } else {
            holder.bio.setVisibility(View.GONE);
        }

        if ("librarian".equalsIgnoreCase(user.getUserType())) {
            holder.role.setText("📚 Librarian");
        } else {
            holder.role.setText("👤 Student");
        }
    }

    @Override
    public int getItemCount() {
        return followersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, bio, role;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.user_name);
            email = itemView.findViewById(R.id.user_email);
            bio = itemView.findViewById(R.id.user_bio);
            role = itemView.findViewById(R.id.user_role);
        }
    }
}
