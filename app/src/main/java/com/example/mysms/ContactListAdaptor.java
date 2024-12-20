package com.example.mysms;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
//        holder.txtName.setText(m.getName());
        holder.txtPhone.setText(String.valueOf(m.getPhoneNumber()));
        try {
            holder.txtName.setText(String.valueOf(m.getId()));
        }catch (Exception e){
            holder.txtName.setText("err");
        }
        holder.itemView.setOnClickListener(v -> {
//            Log.d("ContactListAdaptor", "Item clicked: " + String.valueOf(m.getId()));
//            Toast.makeText(context, String.valueOf(m.getId()), Toast.LENGTH_SHORT).show();
            FragmentManager fragmentManager = ((AppCompatActivity) context).getSupportFragmentManager();
            EditContact fragment = EditContact.newInstance(m.getId());
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, fragment)
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
