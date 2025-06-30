package com.example.libroai;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MessageAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int USER_TYPE = 0;
    private static final int BOT_TYPE  = 1;

    private final ArrayList<Message> messageList;
    private final Context context;

    public MessageAdapter(ArrayList<Message> messageList, Context context) {
        this.messageList = messageList;
        this.context     = context;
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).isUser()
                ? USER_TYPE
                : BOT_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder
    onCreateViewHolder(@NonNull ViewGroup parent,
                       int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        if (viewType == USER_TYPE) {
            View view = inflater.inflate(
                    R.layout.item_user_message, parent, false);
            return new UserViewHolder(view);
        } else {
            View view = inflater.inflate(
                    R.layout.item_bot_message, parent, false);
            return new BotViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,
                                 int position) {
        Message message = messageList.get(position);
        if (holder instanceof UserViewHolder) {
            ((UserViewHolder) holder)
                    .userMessage.setText(message.getText());
        } else {
            ((BotViewHolder) holder)
                    .botMessage.setText(message.getText());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView userMessage;
        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            userMessage = itemView.findViewById(R.id.userMessage);
        }
    }

    static class BotViewHolder extends RecyclerView.ViewHolder {
        TextView botMessage;
        BotViewHolder(@NonNull View itemView) {
            super(itemView);
            botMessage = itemView.findViewById(R.id.botMessage);
        }
    }
}
