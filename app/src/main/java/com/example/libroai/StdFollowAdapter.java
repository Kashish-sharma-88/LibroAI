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

public class StdFollowAdapter extends RecyclerView.Adapter<StdFollowAdapter.ViewHolder> {

    private List<StdUserModel> userList;
    private String currentUserId;

    public StdFollowAdapter(List<StdUserModel> userList, String currentUserId) {
        this.userList = userList;
        this.currentUserId = currentUserId != null ? currentUserId : "";
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_std_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StdUserModel user = userList.get(position);
        
        holder.userName.setText(user.getUserName());
        holder.userEmail.setText(user.getUserEmail());
        
        // Set user type and library info
        if ("librarian".equals(user.getUserType())) {
            holder.userType.setText("📚 Librarian");
            if (user.getLibraryName() != null && !user.getLibraryName().isEmpty()) {
                holder.libraryInfo.setText("🏛️ " + user.getLibraryName());
                holder.libraryInfo.setVisibility(View.VISIBLE);
            } else {
                holder.libraryInfo.setVisibility(View.GONE);
            }
        } else {
            holder.userType.setText("👤 Student");
            holder.libraryInfo.setVisibility(View.GONE);
        }
        
        // Set bio
        if (user.getUserBio() != null && !user.getUserBio().isEmpty()) {
            holder.userBio.setText(user.getUserBio());
            holder.userBio.setVisibility(View.VISIBLE);
        } else {
            holder.userBio.setVisibility(View.GONE);
        }
        
        // Set followers count
        holder.followersCount.setText(user.getFollowersCount() + " followers");
        
        // Set follow button state
        if (currentUserId.isEmpty()) {
            // User not logged in - disable follow button
            holder.followButton.setText("Login to Follow");
            holder.followButton.setBackgroundResource(R.drawable.following_button_bg);
            holder.followButton.setEnabled(false);
        } else {
            updateFollowButton(holder.followButton, user.isFollowing());
            
            // Handle follow button click
            holder.followButton.setOnClickListener(v -> {
                if (user.isFollowing()) {
                    unfollowUser(user, holder.followButton);
                } else {
                    followUser(user, holder.followButton);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public void updateList(List<StdUserModel> newList) {
        this.userList = newList;
        notifyDataSetChanged();
    }

    private void updateFollowButton(Button button, boolean isFollowing) {
        if (isFollowing) {
            button.setText("Following ✓");
            button.setBackgroundResource(R.drawable.following_button_bg);
        } else {
            button.setText("Follow +");
            button.setBackgroundResource(R.drawable.follow_button_gradient);
        }
    }

    private void followUser(StdUserModel user, Button button) {
        if (currentUserId.isEmpty()) {
            Toast.makeText(button.getContext(), "Please login to follow users", Toast.LENGTH_SHORT).show();
            return;
        }
        
        DatabaseReference followRef = FirebaseDatabase.getInstance()
                .getReference("follows")
                .child(currentUserId)
                .child(user.getUserId());
        
        followRef.setValue(true)
                .addOnSuccessListener(aVoid -> {
                    user.setFollowing(true);
                    user.setFollowersCount(user.getFollowersCount() + 1);
                    
                    // Update user's followers count in Firebase
                    FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(user.getUserId())
                            .child("followersCount")
                            .setValue(user.getFollowersCount());
                    
                    notifyDataSetChanged();
                    Toast.makeText(button.getContext(), 
                            "Started following " + user.getUserName(), Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(button.getContext(), 
                            "Failed to follow: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void unfollowUser(StdUserModel user, Button button) {
        if (currentUserId.isEmpty()) {
            Toast.makeText(button.getContext(), "Please login to unfollow users", Toast.LENGTH_SHORT).show();
            return;
        }
        
        DatabaseReference followRef = FirebaseDatabase.getInstance()
                .getReference("follows")
                .child(currentUserId)
                .child(user.getUserId());
        
        followRef.removeValue()
                .addOnSuccessListener(aVoid -> {
                    user.setFollowing(false);
                    user.setFollowersCount(Math.max(0, user.getFollowersCount() - 1));
                    
                    // Update user's followers count in Firebase
                    FirebaseDatabase.getInstance()
                            .getReference("users")
                            .child(user.getUserId())
                            .child("followersCount")
                            .setValue(user.getFollowersCount());
                    
                    notifyDataSetChanged();
                    Toast.makeText(button.getContext(), 
                            "Unfollowed " + user.getUserName(), Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(button.getContext(), 
                            "Failed to unfollow: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName, userEmail, userType, libraryInfo, userBio, followersCount;
        Button followButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.user_name);
            userEmail = itemView.findViewById(R.id.user_email);
            userType = itemView.findViewById(R.id.user_type);
            libraryInfo = itemView.findViewById(R.id.library_info);
            userBio = itemView.findViewById(R.id.user_bio);
            followersCount = itemView.findViewById(R.id.followers_count);
            followButton = itemView.findViewById(R.id.follow_button);
        }
    }
} 