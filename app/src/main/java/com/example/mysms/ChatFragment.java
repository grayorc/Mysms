package com.example.mysms;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ChatFragment extends Fragment {

    private RecyclerView recyclerViewChat;
    private EditText editTextMessage;
    private ImageButton buttonSend;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> chatMessages;
    private DatabaseManager dbm;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    private static final String ARG_PARAM1 = "Id";
    private int Id;

    public ChatFragment() {

    }

    public static ChatFragment newInstance(int Id) {
        ChatFragment fragment = new ChatFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, Id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Id = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        recyclerViewChat = view.findViewById(R.id.recyclerViewChat);
        editTextMessage = view.findViewById(R.id.editTextMessage);
        buttonSend = view.findViewById(R.id.buttonSend);

        dbm = new DatabaseManager(getActivity());
        chatMessages = dbm.getChatHistory(Id);
        Contact contact = dbm.getContact(Id);
        editTextMessage.setHint("Send message to " +Id);
        chatAdapter = new ChatAdapter(chatMessages);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewChat.setAdapter(chatAdapter);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextMessage.getText().toString().trim();
                if (!message.isEmpty()) {
                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
                    } else {
                        String timestamp = String.valueOf(System.currentTimeMillis());
                        if(sendSMSMessage("1", message)) {
                            Toast.makeText(getActivity(), "SMS sent.", Toast.LENGTH_SHORT).show();
                            dbm.insertSentMessage(message, timestamp, String.valueOf(contact.getId()),"1");

                        }else {
                            Toast.makeText(getActivity(), "SMS failed to send.", Toast.LENGTH_SHORT).show();
                            dbm.insertSentMessage(message, timestamp,  String.valueOf(contact.getId()),"0");
                        }

                        chatMessages.add(new ChatMessage(message, timestamp, -1));
                        chatAdapter.notifyItemInserted(chatMessages.size() - 1);
                        editTextMessage.setText("");
                        recyclerViewChat.scrollToPosition(chatMessages.size() - 1);
                    }
                }
            }
        });

        return view;
    }

    private boolean sendSMSMessage(String phoneNumber, String message) {
//        try {
//            SmsManager smsManager = SmsManager.getDefault();
//            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
//            Toast.makeText(getActivity(), "SMS sent.", Toast.LENGTH_SHORT).show();
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
        return true;
    }

}
