package com.example.mysms;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.telephony.SmsManager;
import android.widget.Toast;

import android.content.Context;
import android.content.DialogInterface;

public class Helper {

    public interface DialogCallback {
        void onResult(boolean result);
    }

        public void showDialog(Context context, String message, DialogCallback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setPositiveButton("Yes", (dialog, id) -> {
                    callback.onResult(true);
                })
                .setNegativeButton("No", (dialog, id) -> {
                    callback.onResult(false);
                    dialog.dismiss();
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public boolean sendSMSMessage(Context context, String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(context, "SMS sent.", Toast.LENGTH_SHORT).show();
            return true;
        } catch (Exception e) {
            Toast.makeText(context, "SMS failed to send.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

}
