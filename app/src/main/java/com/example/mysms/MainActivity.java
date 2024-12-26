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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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
                else if(item.getItemId() == R.id.nav_notifications) {
                    selectedFragment = new CreateContact();
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                return true;
            }
        });
        String[] permissions = {
                Manifest.permission.SEND_SMS,
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.RECEIVE_SMS
        };

        List<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(permission);
            }
        }

        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(MainActivity.this, permissionsToRequest.toArray(new String[0]), 1);
        }

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
        unregisterReceiver(myBroadcastReceiver);
    }

}