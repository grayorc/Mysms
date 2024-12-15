package com.example.mysms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper {
    private static String DBName = "sms.db";
    private static final String Received_Message_Table = "received_messages";
    private static final String[] Received_Message_fields = {"id", "sender", "message", "date", "sender"};
    private static final String Contact_Table = "contacts";
    private static final String[] Contact_fields = {"id","name", "phone"};

    final static int Version = 1;
    public DatabaseManager(@Nullable Context context) {
        super(context, DBName, null, Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IF NOT EXISTS " + Contact_Table + " ("
                + Contact_fields[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contact_fields[1] + " TEXT, "
                + Contact_fields[2] + " TEXT)";
        try {
            db.execSQL(query);
        }catch (Exception e){
            Log.i("DB_contacts", "Database not created");
        }

        Log.i("DB_contacts", "Database created");
    }

    public void InsertContact(Contact contact){
        SQLiteDatabase sld = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Contact_fields[1], contact.getName());
        cv.put(Contact_fields[2], contact.getPhoneNumber());

        sld.insert(Contact_Table, null, cv);

        sld.close();
    }

    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<>();
        SQLiteDatabase sld = this.getReadableDatabase();

        String selectQuery = "SELECT * FROM " + Contact_Table;
        Cursor cursor = sld.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(Contact_fields[0]));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(Contact_fields[1]));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(Contact_fields[2]));
                Contact contact = new Contact(name,phone,id);
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        cursor.close();
        sld.close();

        return contactList;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
