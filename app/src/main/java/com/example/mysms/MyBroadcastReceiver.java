package com.example.mysms;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;


public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        final String str = "android.provider.Telephony.SMS_RECEIVED";

        if (intent.getAction().equals(str)) {
            Object[] pdus = (Object[]) intent.getExtras().get("pdus");
            SmsMessage[] messages = new SmsMessage[pdus.length];
            StringBuilder messageBody = new StringBuilder();
            String sender = "";
            for (int i = 0; i < pdus.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                if (i == 0) {
                    sender = messages[i].getOriginatingAddress();
                }
                messageBody.append(messages[i].getMessageBody());
            }
            NotificationManager NfManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel NfChannel = new NotificationChannel("MyChannel", "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
                NfChannel.setDescription("DSC of channel");
                NfManager.createNotificationChannel(NfChannel);
            }

            NotificationCompat.Builder sNotifbuilder = new NotificationCompat.Builder(context, "MyChannel")
                    .setSmallIcon(R.drawable.chat_1_fill)
                    .setContentTitle(sender)
                    .setContentText(messageBody.toString())
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

            NfManager.notify(1, sNotifbuilder.build());
        }
    }
}
