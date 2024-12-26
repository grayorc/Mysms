package com.example.mysms;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {
    private List<ChatMessage> chatMessages;
    Context context;

    public ChatAdapter(List<ChatMessage> chatMessages, Context context) {
        this.chatMessages = chatMessages;
        this.context = context;
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
        /*
        * 1 for sent messages
        * 2 for failed messages
        * -1 for received messages
        */
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.linearLayoutMessage.getLayoutParams();
        if (chatMessage.isSent() == 1) {
            layoutParams.gravity = Gravity.END;
        } else if (chatMessage.isSent() == -1) {
            layoutParams.gravity = Gravity.START;
            holder.textViewMessage.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.white));
            holder.linearLayoutMessage.setBackgroundResource(R.drawable.received_message_background);
        } else if (chatMessage.isSent() == 0) {
            layoutParams.gravity = Gravity.END;
            holder.textViewMessage.setTextColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.my_dark_primary));
            holder.linearLayoutMessage.setBackgroundResource(R.drawable.err_message_background);
        }
        holder.linearLayoutMessage.setLayoutParams(layoutParams);
        //forward after long click
        holder.itemView.setOnLongClickListener(v -> {
            Helper hp = new Helper();
            hp.showDialog(context, "Do you want to forward this message?", result -> {
                if (result) {
                    FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
                    Contact_List fragment = Contact_List.newInstance(true, holder.textViewMessage.getText().toString());
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment)
                            .addToBackStack(null)
                            .commit();
                }
            });
            return true;
        });
        //show time stamp after click
        holder.itemView.setOnClickListener(v -> {
            long timestamp = Long.parseLong(chatMessage.getTimestamp());
            Log.d("ChatAdapter", "Timestamp: " + timestamp);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = new Date(timestamp);
            String formattedDate = sdf.format(date);
            Snackbar.make(v, formattedDate, Snackbar.LENGTH_LONG).setBackgroundTint(ContextCompat.getColor(context, R.color.black)).setTextColor(ContextCompat.getColor(context, R.color.purple)).show();
        });




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
