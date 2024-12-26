package com.example.mysms;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

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
                Log.i("sender",sender + "");
                if(Sender != null){
                    dbm.insertReceivedMessage(content, timestamp, Sender.getId());
                    Log.i("sender","found and saved" + "");
                }else {
                    //check without +98
                    String sender_without_cc = sender.replace("+98", "0");
                    Contact Sender_no_cc = dbm.getContactByPhone(sender_without_cc);
                    if(Sender_no_cc != null) {
                        Log.i("sender","found without cc and saved");
                        dbm.insertReceivedMessage(content, timestamp, Sender_no_cc.getId());
                    }else {
                        //make new contact if received message from new number
                        Contact contact = new Contact(sender,sender);
                        Log.i("sender","not found and saved");
                        Contact newContact = dbm.insertContact(contact);
                        dbm.insertReceivedMessage(content, timestamp, newContact.getId());
                    }
                }
                dbm.close();
                NotificationManager NfManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    NotificationChannel NfChannel = new NotificationChannel("MyChannel", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
                    NfChannel.setDescription("DSC of channel");
                    NfManager.createNotificationChannel(NfChannel);
                }

                NotificationCompat.Builder sNotifbuilder = new NotificationCompat.Builder(context, "MyChannel")
                        .setSmallIcon(R.drawable.chat_1_fill)
                        .setContentTitle(sender)
                        .setContentText(content)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                NfManager.notify(1, sNotifbuilder.build());
            }
        }
    }
}
