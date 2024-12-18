package com.example.mysms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CreateContact extends Fragment {

    private DatabaseManager dbm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_contact, container, false);

        dbm = new DatabaseManager(getActivity());

        EditText nameInp = view.findViewById(R.id.name);
        EditText phoneNumberInp = view.findViewById(R.id.phoneNumber);
        Button saveBtn = view.findViewById(R.id.saveBtn);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String name = nameInp.getText().toString();
                String phoneNumber = phoneNumberInp.getText().toString();
                Contact contact = new Contact(name, phoneNumber);
                dbm.insertContact(contact);
                Toast.makeText(getActivity(), "Contact saved", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
