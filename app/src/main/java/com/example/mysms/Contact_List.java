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

    private DatabaseManager dbm;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact__list, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);

        dbm = new DatabaseManager(getActivity());

        // Set up RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<Contact> contacts = (ArrayList<Contact>) dbm.getAllContacts();
        ContactListAdaptor adapter = new ContactListAdaptor(getActivity(), contacts);
        recyclerView.setAdapter(adapter);

        return view;
    }
}
