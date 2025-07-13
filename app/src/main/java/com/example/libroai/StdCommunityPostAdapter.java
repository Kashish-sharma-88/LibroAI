package com.example.libroai;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class StdCommunityPostAdapter extends RecyclerView.Adapter<StdCommunityPostAdapter.ViewHolder> {
    
    private List<StdCommunityPost> posts;
    private Context context;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());

    public StdCommunityPostAdapter(Context context, List<StdCommunityPost> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_std_community_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StdCommunityPost post = posts.get(position);
        
        holder.titleText.setText(post.getTitle());
        // Show content, or question, or title as fallback
        String content = post.getContent();
        if (content == null || content.trim().isEmpty()) {
            content = post.getQuestion();
            if (content == null || content.trim().isEmpty()) {
                content = post.getTitle();
            }
        }
        holder.contentText.setText(content);
        holder.userNameText.setText(post.getUserName());
        holder.categoryText.setText(post.getCategory());
        holder.replyCountText.setText(post.getReplyCount() + " replies");
        
        if (post.getTimestamp() != null) {
            holder.timeText.setText(dateFormat.format(post.getTimestamp().toDate()));
        }
        
        if (post.isResolved()) {
            holder.statusText.setText("✓ Resolved");
            holder.statusText.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else {
            holder.statusText.setText("● Open");
            holder.statusText.setTextColor(context.getResources().getColor(android.R.color.holo_orange_dark));
        }

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(context, StdCommunityPostDetailActivity.class);
            intent.putExtra("postId", post.getPostId());
            intent.putExtra("postTitle", post.getTitle());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    public void updatePosts(List<StdCommunityPost> newPosts) {
        this.posts = newPosts;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView titleText, contentText, userNameText, categoryText, timeText, replyCountText, statusText;

        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.post_card);
            titleText = itemView.findViewById(R.id.post_title);
            contentText = itemView.findViewById(R.id.post_content);
            userNameText = itemView.findViewById(R.id.post_user_name);
            categoryText = itemView.findViewById(R.id.post_category);
            timeText = itemView.findViewById(R.id.post_time);
            replyCountText = itemView.findViewById(R.id.post_reply_count);
            statusText = itemView.findViewById(R.id.post_status);
        }
    }
} 