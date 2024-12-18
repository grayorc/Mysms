package com.example.mysms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerViewChat;
    private EditText editTextMessage;
    private Button buttonSend;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;
    private DatabaseManager dbm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerViewChat = view.findViewById(R.id.recyclerViewChat);
        editTextMessage = view.findViewById(R.id.editTextMessage);
        buttonSend = view.findViewById(R.id.buttonSend);

        dbm = new DatabaseManager(getActivity());

        // Retrieve chat history
        chatMessages = dbm.getChatHistory();

        // Set up the RecyclerView and Adapter
        chatAdapter = new ChatAdapter(chatMessages);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewChat.setAdapter(chatAdapter);


        // Handle sending messages
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextMessage.getText().toString().trim();
                if (!message.isEmpty()) {
                    // Save the message to the database and update the UI
                    String timestamp = String.valueOf(System.currentTimeMillis());
                    dbm.insertSentMessage(message, timestamp, "receiver_id_here");
                    chatMessages.add(new ChatMessage(message, timestamp, true));
                    chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                    editTextMessage.setText("");
                    recyclerViewChat.scrollToPosition(chatMessages.size() - 1);
                }
            }
        });

        return view;
    }
}
