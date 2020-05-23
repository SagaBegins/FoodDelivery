package com.example.food__delivery.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.food__delivery.Fragment.AllCartsFragment;
import com.example.food__delivery.Fragment.AllFavouriteFragment;
import com.example.food__delivery.MainNavigationActivity.HomeFragment;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.food__delivery.AllOrders;
import com.example.food__delivery.Fragment.CartFragment;
import com.example.food__delivery.Fragment.FavouriteFragment;
import com.example.food__delivery.Login;
import com.example.food__delivery.R;
import com.example.food__delivery.Testing.DatabaseEntry;

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
        FacebookSdk.setApplicationId("581033482823166");
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_admin_screen);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

        //Add chat fragment or something
        fragment = new HomeFragment();
        setTitle("Home");
        databaseEntry = new DatabaseEntry(AdminScreen.this);
        databaseEntry.createTable();
        databaseEntry.close();
        auth = FirebaseAuth.getInstance();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame_admin, fragment).commit();
        user = FirebaseAuth.getInstance().getCurrentUser();
        }


    public void exitByBackKey() {
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
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

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}


