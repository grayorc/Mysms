package com.example.mysms;

import androidx.annotation.Nullable;

public class Contact {
    private int Id;
    private String Name;
    private String PhoneNumber;

    public Contact(String name,String phoneNumber,int id) {
        Name = name;
        PhoneNumber = phoneNumber;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }
}
