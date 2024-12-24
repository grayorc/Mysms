package com.example.mysms;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<ChatMessage> chatMessages;

    public ChatAdapter(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);
        holder.textViewMessage.setText(chatMessage.getMessage());

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.linearLayoutMessage.getLayoutParams();
        if (chatMessage.isSent() == 1) {
            layoutParams.gravity = Gravity.END;
        } else if (chatMessage.isSent() == -1) {
            layoutParams.gravity = Gravity.START;
        } else if (chatMessage.isSent() == 0) {
            layoutParams.gravity = Gravity.START;
        }
        holder.linearLayoutMessage.setLayoutParams(layoutParams);
    }




    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewMessage;
        public LinearLayout linearLayoutMessage;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            linearLayoutMessage = itemView.findViewById(R.id.linearLayoutMessage);
        }
    }

}
