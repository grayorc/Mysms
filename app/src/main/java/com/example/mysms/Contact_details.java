package com.example.mysms;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Contact_details extends Fragment {

    private static final String ARG_PARAM1 = "Id";
    private int Id;

    public Contact_details() {
        // Required empty public constructor
    }

    public static Contact_details newInstance(int Id) {
        Contact_details fragment = new Contact_details();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, Id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Log.i("tst", String.valueOf(getArguments().getInt(ARG_PARAM1)));
            Id = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_details, container, false);
        TextView idTextView = view.findViewById(R.id.Id);
        idTextView.setText(String.valueOf(Id));
        return view;
    }
}
