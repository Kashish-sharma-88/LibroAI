package com.example.libroai;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StdGroupChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;
    
    private List<StdGroupMessageModel> messageList;
    private Context context;
    private String currentUserId;
    
    public StdGroupChatAdapter(Context context, List<StdGroupMessageModel> messageList, String currentUserId) {
        this.context = context;
        this.messageList = messageList;
        this.currentUserId = currentUserId;
    }
    
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        
        if (viewType == VIEW_TYPE_SENT) {
            view = LayoutInflater.from(context).inflate(R.layout.item_std_sent_message, parent, false);
            return new SentMessageViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_std_received_message, parent, false);
            return new ReceivedMessageViewHolder(view);
        }
    }
    
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        StdGroupMessageModel message = messageList.get(position);
        
        if (holder.getItemViewType() == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder) holder).bind(message);
        } else {
            ((ReceivedMessageViewHolder) holder).bind(message);
        }
    }
    
    @Override
    public int getItemCount() {
        return messageList.size();
    }
    
    @Override
    public int getItemViewType(int position) {
        StdGroupMessageModel message = messageList.get(position);
        
        if (message.getSenderId().equals(currentUserId)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }
    
    public void updateList(List<StdGroupMessageModel> newList) {
        this.messageList = newList;
        notifyDataSetChanged();
    }
    
    // Sent Message ViewHolder
    public static class SentMessageViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, messageTime;
        
        public SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message_text);
            messageTime = itemView.findViewById(R.id.message_time);
        }
        
        public void bind(StdGroupMessageModel message) {
            messageText.setText(message.getMessageText());
            messageTime.setText(formatTime(message.getTimestamp()));
        }
    }
    
    // Received Message ViewHolder
    public static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        TextView senderName, messageText, messageTime;
        
        public ReceivedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            senderName = itemView.findViewById(R.id.sender_name);
            messageText = itemView.findViewById(R.id.message_text);
            messageTime = itemView.findViewById(R.id.message_time);
        }
        
        public void bind(StdGroupMessageModel message) {
            senderName.setText(message.getSenderName());
            messageText.setText(message.getMessageText());
            messageTime.setText(formatTime(message.getTimestamp()));
        }
    }
    
    private static String formatTime(long timestamp) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(timestamp);
        
        // Format: "HH:mm" (24-hour format)
        return DateFormat.format("HH:mm", calendar).toString();
    }
} 