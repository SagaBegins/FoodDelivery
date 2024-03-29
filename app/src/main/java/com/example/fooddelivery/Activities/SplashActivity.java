package com.example.fooddelivery.Activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddelivery.R;
import com.example.fooddelivery.Singleton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Method;

public class SplashActivity extends AppCompatActivity {
    RelativeLayout splash;

    FirebaseAuth auth;
    FirebaseUser user;
    DatabaseReference firebaseDatabase;

    boolean isAdmin;
    private Singleton init;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init = new Singleton();

        auth = Singleton.auth;
        user = Singleton.auth.getCurrentUser();

        FirebaseDatabase database = Singleton.db;
        firebaseDatabase = database.getReference();

        Log.d("TAG", "onCreate: " + database.getReference().toString());

        setContentView(R.layout.activity_splash);
        splash = findViewById(R.id.splash);
        splash.postDelayed(() -> {

            if (isMobileDataEnabled()) {
                //Mobile data is enabled and do whatever you want here
                if (user == null) {
                    Intent i = new Intent(SplashActivity.this, OpenScreen.class);
                    startActivity(i);
                    finish();
                } else {
                    firebaseDatabase.child("users").child(auth.getUid()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            try {
                                isAdmin = dataSnapshot.child("admin").getValue(Boolean.class);
                            } catch (Exception e) {
                                isAdmin = false;
                            }
                            Log.d("TAG", "onDataChange: " + isAdmin);
                            Intent intent;

                            if (isAdmin) {
                                intent = new Intent(SplashActivity.this, AdminScreen.class);
                            } else {
                                intent = new Intent(SplashActivity.this, MainScreen.class);
                            }
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            } else {
                //Mobile data is disabled here
                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                builder.setTitle("Error");
                builder.setIcon(R.drawable.ic_warning_black_24dp);
                builder.setMessage("No mobile data connection detected.\nPlease check network connection and visit again.");
                builder.setCancelable(false);

                builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }, 4000);
    }

    private boolean isMobileDataEnabled() {
        boolean mobileDataEnabled = false;
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        try {
            Class cmClass = Class.forName(cm.getClass().getName());
            Method method = cmClass.getDeclaredMethod("getMobileDataEnabled");
            method.setAccessible(true);

            mobileDataEnabled = (Boolean) method.invoke(cm) || wm.isWifiEnabled();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mobileDataEnabled;
    }
}
