package com.example.mysms;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
import com.example.mysms.R;
import android.view.MenuItem;



public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_NOTIFICATION = 101;
    private static final int REQUEST_CODE_RECEIVE_SMS = 102;
    private static final int REQUEST_CODE_SEND_SMS = 103;
    private static final int REQUEST_CODE_READ_PHONE_STATE = 105;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        //database
        DatabaseManager dbm = new DatabaseManager(this);
        SQLiteDatabase sld = dbm.getReadableDatabase();
        if (sld.isOpen()){
            Log.i("DB_contacts", "Database opened");
            sld.close();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;
                if(item.getItemId() == R.id.nav_home){
                    selectedFragment = new Contact_List();
                }
                else if(item.getItemId() == R.id.nav_dashboard){
                    selectedFragment = new ChatFragment();
                }
                else if(item.getItemId() == R.id.nav_notifications) {
                    selectedFragment = new CreateContact();
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            }
        });

        // Load the default fragment
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
//        }
        //create contact
//        EditText nameInp = findViewById(R.id.name);
//        EditText phoneNumberInp = findViewById(R.id.phoneNumber);
//        Button saveBtn = findViewById(R.id.saveBtn);
//        saveBtn.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                String name = nameInp.getText().toString();
//                String phoneNumber = phoneNumberInp.getText().toString();
//                Contact contact = new Contact(name, phoneNumber,0);
//                dbm.InsertContact(contact);
//                Toast.makeText(MainActivity.this, "saved", Toast.LENGTH_SHORT).show();
//            }
//        });
//        TextView tx = findViewById(R.id.title);
        //list contact
//        RecyclerView rv = findViewById(R.id.recyclerView);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//        rv.setLayoutManager(linearLayoutManager);
//        ArrayList<Contact> im = (ArrayList<Contact>) dbm.getAllContacts();
//        ContactListAdaptor adp = new ContactListAdaptor(this, im);
//        rv.setAdapter(adp);


        // Request permissions for notifications
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_NOTIFICATION);
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECEIVE_SMS}, REQUEST_CODE_RECEIVE_SMS);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_CODE_SEND_SMS);
        }

        // Register the broadcast receiver
        registerReceiver(myBroadcastReceiver, new IntentFilter(Intent.ACTION_USER_UNLOCKED));
    }

    MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();

    public void RegisterReceiverSMS(MyBroadcastReceiver MyBroadcastReceiver) {
        IntentFilter SMS_RECEIVED_ACTION = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(MyBroadcastReceiver, SMS_RECEIVED_ACTION);
    }
    public void requestPermission(String permission, int REQUEST_CODE) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, REQUEST_CODE);
    }
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_RECEIVE_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "App needs permission to receive and send SMS", Toast.LENGTH_LONG).show();
            }
        }

        if (requestCode == REQUEST_CODE_NOTIFICATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "App needs permission to send Notification", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the receiver to prevent memory leaks
        unregisterReceiver(myBroadcastReceiver);
    }

}