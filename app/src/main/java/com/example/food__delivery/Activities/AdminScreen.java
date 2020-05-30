package com.example.food__delivery.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.food__delivery.InboxActivity;
import com.example.food__delivery.MainNavigationActivity.HomeFragment;
import com.example.food__delivery.R;
import com.example.food__delivery.Testing.DatabaseEntry;
import com.facebook.FacebookSdk;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminScreen extends AppCompatActivity {
    Fragment fragment;
    TextView nametext;
    FirebaseAuth auth;
    String idf, namef;
    ImageView photo;
    FirebaseUser user;
    Button sign;
    DatabaseEntry databaseEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
//        FacebookSdk.setApplicationId("581033482823166");
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        setContentView(R.layout.activity_admin_screen);
//
//        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//
//        //Add chat fragment or something
//        fragment = new HomeFragment();
//        setTitle("Home");
//        databaseEntry = new DatabaseEntry(AdminScreen.this);
//        databaseEntry.createTable();
//        databaseEntry.close();
//        auth = FirebaseAuth.getInstance();
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fm.beginTransaction();
//        fragmentTransaction.replace(R.id.content_frame_admin, fragment).commit();
//        user = FirebaseAuth.getInstance().getCurrentUser();

        Intent i = new Intent(this, InboxActivity.class);
        startActivity(i);
        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        auth.signOut();
    }

    public void exitByBackKey() {
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("You will be logged out when you leave")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        auth.signOut();
                        finishAffinity();
                        //close();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            //moveTaskToBack(false);
            auth.signOut();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}


