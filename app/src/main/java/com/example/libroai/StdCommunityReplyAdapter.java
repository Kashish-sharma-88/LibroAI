package com.example.libroai;

import android.content.Context;
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

public class StdCommunityReplyAdapter extends RecyclerView.Adapter<StdCommunityReplyAdapter.ViewHolder> {
    
    private List<StdCommunityReply> replies;
    private Context context;
    private String currentUserId;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy 'at' HH:mm", Locale.getDefault());

    public StdCommunityReplyAdapter(Context context, List<StdCommunityReply> replies, String currentUserId) {
        this.context = context;
        this.replies = replies;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_std_community_reply, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StdCommunityReply reply = replies.get(position);
        
        holder.contentText.setText(reply.getContent());
        holder.userNameText.setText(reply.getUserName());
        
        if (reply.getTimestamp() != null) {
            holder.timeText.setText(dateFormat.format(reply.getTimestamp().toDate()));
        }
        
        if (reply.isAccepted()) {
            holder.acceptedText.setVisibility(View.VISIBLE);
            holder.acceptedText.setText("✓ Accepted Answer");
            holder.acceptedText.setTextColor(context.getResources().getColor(android.R.color.holo_green_dark));
        } else {
            holder.acceptedText.setVisibility(View.GONE);
        }

        // Show accept button only if this is not the current user's reply
        if (!reply.getUserId().equals(currentUserId)) {
            holder.acceptButton.setVisibility(View.VISIBLE);
            holder.acceptButton.setOnClickListener(v -> {
                // Handle accept reply logic
                if (onReplyAcceptedListener != null) {
                    onReplyAcceptedListener.onReplyAccepted(reply);
                }
            });
        } else {
            holder.acceptButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return replies.size();
    }

    public void updateReplies(List<StdCommunityReply> newReplies) {
        this.replies = newReplies;
        notifyDataSetChanged();
    }

    // Interface for handling reply acceptance
    public interface OnReplyAcceptedListener {
        void onReplyAccepted(StdCommunityReply reply);
    }

    private OnReplyAcceptedListener onReplyAcceptedListener;

    public void setOnReplyAcceptedListener(OnReplyAcceptedListener listener) {
        this.onReplyAcceptedListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView contentText, userNameText, timeText, acceptedText, acceptButton;

        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.reply_card);
            contentText = itemView.findViewById(R.id.reply_content);
            userNameText = itemView.findViewById(R.id.reply_user_name);
            timeText = itemView.findViewById(R.id.reply_time);
            acceptedText = itemView.findViewById(R.id.reply_accepted);
            acceptButton = itemView.findViewById(R.id.reply_accept_button);
        }
    }
} 