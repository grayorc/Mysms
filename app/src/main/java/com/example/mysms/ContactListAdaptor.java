package com.example.mysms;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ContactListAdaptor extends RecyclerView.Adapter<ContactListAdaptor.MyViewHolder> {
    Context context;
    ArrayList<Contact> arr;

    public ContactListAdaptor(Context context, ArrayList<Contact> arr) {
        this.context = context;
        this.arr = arr;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Contact m = arr.get(position);
        holder.txtName.setText(m.getName());
        holder.txtPhone.setText(String.valueOf(m.getPhoneNumber()));

        holder.itemView.setOnClickListener(v -> {
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            Contact_details fragment = Contact_details.newInstance(m.getId());
            fragmentManager.beginTransaction()
                    .replace(R.id.contact_details, fragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtPhone;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtPhone = itemView.findViewById(R.id.txtPhone);
        }
    }
}
