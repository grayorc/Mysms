package com.example.mysms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            SmsMessage[] messages = new SmsMessage[pdus.length];
            for (int i = 0; i < pdus.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                String sender = messages[i].getDisplayOriginatingAddress();
                String content = messages[i].getMessageBody();
                String timestamp = String.valueOf(System.currentTimeMillis());
                Toast.makeText(context, sender, Toast.LENGTH_SHORT).show();
                //insert
                DatabaseManager dbm = new DatabaseManager(context);
                Contact Sender = dbm.getContactByPhone(sender);
                if(Sender != null){
                    dbm.insertReceivedMessage(content, timestamp, Sender.getId());
                    Log.i("message",content + "");
                }else {
                    //make new contact if received message from new number
                    Contact contact = new Contact(sender,sender);
                    Log.i("message",content);
                    dbm.insertContact(contact);
                    dbm.insertReceivedMessage(content, timestamp, contact.getId());
                    dbm.close();
                }
            }
        }
    }
}
