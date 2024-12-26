package com.example.mysms;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper {
    private static String DBName = "sms.db";
    private static final String Contact_Table = "contacts";
    private static final String Contact_ID = "id";
    private static final String Contact_NAME = "name";
    private static final String Contact_PHONE = "phone";
    //received messages table
    private static final String Received_messages_Table = "received_messages";
    private static final String Received_messages_ID = "id";
    private static final String Received_messages_CONTENT = "content";
    private static final String Received_messages_TIMESTAMP = "timestamp";
    private static final String Received_messages_CONTACT_ID = "contact_id";
    //sent messages table
    private static final String Sent_messages_Table = "sent_messages";
    private static final String Sent_messages_ID = "id";
    private static final String Sent_messages_CONTENT = "content";
    private static final String Sent_messages_TIMESTAMP = "timestamp";
    private static final String Sent_messages_IS_SENT = "is_sent";
    private static final String Sent_messages_CONTACT_ID = "contact_id";

    final static int Version = 1;

    public DatabaseManager(@Nullable Context context) {
        super(context, DBName, null, Version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_contacts_table = "CREATE TABLE IF NOT EXISTS " + Contact_Table + " ("
                + Contact_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Contact_NAME + " TEXT, "
                + Contact_PHONE + " TEXT)";

        String create_received_messages_table = "CREATE TABLE IF NOT EXISTS " + Received_messages_Table + " ("
                + Received_messages_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Received_messages_CONTENT + " TEXT, "
                + Received_messages_TIMESTAMP + " TEXT, "
                + Received_messages_CONTACT_ID + " INTEGER, "
                + "FOREIGN KEY(" + Received_messages_CONTACT_ID + ") REFERENCES " + Contact_Table + "(" + Contact_ID + "))";

        String create_sent_messages_table = "CREATE TABLE IF NOT EXISTS " + Sent_messages_Table + " ("
                + Sent_messages_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + Sent_messages_CONTENT + " TEXT, "
                + Sent_messages_TIMESTAMP + " TEXT, "
                + Sent_messages_IS_SENT + " TEXT, "
                + Sent_messages_CONTACT_ID + " INTEGER, "
                + "FOREIGN KEY(" + Sent_messages_CONTACT_ID + ") REFERENCES " + Contact_Table + "(" + Contact_ID + "))";

        try {
            db.execSQL(create_contacts_table);
            db.execSQL(create_received_messages_table);
            db.execSQL(create_sent_messages_table);
        } catch (Exception e) {
            Log.i("DB", "Databases are not created: " + e.toString());
        }

        Log.i("DB", "Databases are created");
    }



    public Contact insertContact(Contact contact) {
        SQLiteDatabase sld = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Contact_NAME, contact.getName());
        cv.put(Contact_PHONE, contact.getPhoneNumber());
        long id = sld.insert(Contact_Table, null, cv);
        sld.close();
        return new Contact(contact.getName(), contact.getPhoneNumber(), (int)id);
    }

    public void deleteContact(Contact contact){
        SQLiteDatabase sld = this.getWritableDatabase();
        sld.delete(Contact_Table, "id=?", new String[]{String.valueOf(contact.getId())});
        sld.close();
    }

    public List<Contact> getAllContacts() {
        List<Contact> contactList = new ArrayList<>();
        SQLiteDatabase sld = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + Contact_Table;
        Cursor cursor = sld.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(Contact_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(Contact_NAME));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(Contact_PHONE));
                Contact contact = new Contact(name, phone, id);
                contactList.add(contact);
            } while (cursor.moveToNext());
        }

        cursor.close();
        sld.close();

        return contactList;
    }

    public Contact getContact(int id) {
        SQLiteDatabase sld = this.getReadableDatabase();
        Cursor cursor = sld.query(Contact_Table, null, "id=?", new String[]{String.valueOf(id)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            String name = cursor.getString(cursor.getColumnIndexOrThrow(Contact_NAME));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(Contact_PHONE));
            Contact contact = new Contact(name, phone, id);
            cursor.close();
            sld.close();
            return contact;
        } else {
            sld.close();
            return null;
        }
    }

    public void updateContact(Contact contact) {
        SQLiteDatabase sld = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Contact_NAME, contact.getName());
        cv.put(Contact_PHONE, contact.getPhoneNumber());
        try {
            sld.update(Contact_Table, cv, "id=?", new String[]{String.valueOf(contact.getId())});
        } catch (Exception e) {
            Log.i("DB_contacts", "failed to update");
        }
    }

    public List<ChatMessage> getChatHistory(int contactId) {
        List<ChatMessage> chatHistory = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + Received_messages_CONTENT +
                " , " + Received_messages_TIMESTAMP +
                " , '-1' as isSent, " +
                Received_messages_CONTACT_ID +
                " FROM " + Received_messages_Table +
                " WHERE " + Received_messages_CONTACT_ID + " = ?" +
                " UNION ALL " +
                "SELECT " + Sent_messages_CONTENT +
                " as content, " + Sent_messages_TIMESTAMP +
                " as timestamp, " + Sent_messages_IS_SENT +
                " as isSent, " + Sent_messages_CONTACT_ID + " as contact_id " +
                "FROM " + Sent_messages_Table +
                " WHERE " + Sent_messages_CONTACT_ID + " = ?" +
                " ORDER BY timestamp ASC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(contactId), String.valueOf(contactId)});
        if (cursor.moveToFirst()) {
            do {
                String message = cursor.getString(cursor.getColumnIndexOrThrow("content"));
                String timestamp = cursor.getString(cursor.getColumnIndexOrThrow("timestamp"));
                int isSent = cursor.getInt(cursor.getColumnIndexOrThrow("isSent"));
                int contact_id = cursor.getInt(cursor.getColumnIndexOrThrow("contact_id"));
                ChatMessage chatMessage = new ChatMessage(message, timestamp, isSent, contact_id);
                chatHistory.add(chatMessage);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return chatHistory;
    }



    public void insertSentMessage(String message, String timestamp, String ContactId,String isSent) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Sent_messages_CONTENT, message);
        cv.put(Sent_messages_TIMESTAMP, timestamp);
        cv.put(Sent_messages_IS_SENT, isSent);
        cv.put(Sent_messages_CONTACT_ID, ContactId);
        db.insert(Sent_messages_Table, null, cv);
        db.close();
    }

    public void insertReceivedMessage(String message, String timestamp, int ContactId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Received_messages_CONTENT, message);
        cv.put(Received_messages_TIMESTAMP, timestamp);
        cv.put(Received_messages_CONTACT_ID, ContactId);
        db.insert(Received_messages_Table, null, cv);
        db.close();
    }

    public Contact getContactByPhone(String phoneNumber) {
        SQLiteDatabase sld = this.getReadableDatabase();
        Cursor cursor = sld.query(Contact_Table, null, Contact_PHONE + "=?", new String[]{phoneNumber}, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(Contact_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(Contact_NAME));
                String phone = cursor.getString(cursor.getColumnIndexOrThrow(Contact_PHONE));
                Contact contact = new Contact(name, phone, id);
                cursor.close();
                sld.close();
                return contact;
            }
        }
        return null;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Contact_Table);
        db.execSQL("DROP TABLE IF EXISTS " + Sent_messages_Table);
        db.execSQL("DROP TABLE IF EXISTS " + Received_messages_Table);
        onCreate(db);
    }
}
