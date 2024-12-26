package com.example.mysms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Contact_List extends Fragment {

    private static final String ARG_IS_FORWARDING = "isForwarding";
    private static final String ARG_MESSAGE = "message";
    private DatabaseManager dbm;
    private RecyclerView recyclerView;
    private boolean isForwarding;
    private String message;

    public static Contact_List newInstance(boolean isForwarding, @Nullable String message) {
        Contact_List fragment = new Contact_List();
        Bundle args = new Bundle();
        args.putBoolean(ARG_IS_FORWARDING, isForwarding);
        args.putString(ARG_MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact__list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        if (getArguments() != null) {
            isForwarding = getArguments().getBoolean(ARG_IS_FORWARDING);
            message = getArguments().getString(ARG_MESSAGE);
        }

        dbm = new DatabaseManager(getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<Contact> contacts = (ArrayList<Contact>) dbm.getAllContacts();
        ContactListAdaptor adapter = new ContactListAdaptor(getActivity(), contacts, isForwarding, message);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
