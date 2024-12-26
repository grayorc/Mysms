package com.example.mysms;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EditContact#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditContact extends Fragment {

    private static final String ARG_PARAM1 = "Id";
    private int Id;

    public EditContact() {
    }

    public static EditContact newInstance(int Id) {
        EditContact fragment = new EditContact();
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_contact, container, false);

        EditText name = view.findViewById(R.id.name);
        EditText phone = view.findViewById(R.id.phoneNumber);
        Button saveBtn = view.findViewById(R.id.saveBtn);

        DatabaseManager dbm = new DatabaseManager(getActivity());
        Contact contact = dbm.getContact(Id);
        name.setText(contact.getName());
        phone.setText(contact.getPhoneNumber());

        saveBtn.setOnClickListener(v -> {
            contact.setName(name.getText().toString());
            contact.setPhoneNumber(phone.getText().toString());
            dbm.updateContact(contact);
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        return view;
    }
}