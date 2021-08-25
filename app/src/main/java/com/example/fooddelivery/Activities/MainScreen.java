package com.example.fooddelivery.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
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
import com.example.fooddelivery.Additional.DatabaseInstance;
import com.example.fooddelivery.Fragment.AllCartsFragment;
import com.example.fooddelivery.Fragment.AllFavouriteFragment;
import com.example.fooddelivery.Fragment.AllOrders;
import com.example.fooddelivery.Fragment.MainScreenFragment.AboutFragment;
import com.example.fooddelivery.Fragment.MainScreenFragment.HomeFragment;
import com.example.fooddelivery.Fragment.MainScreenFragment.RateFragment;
import com.example.fooddelivery.R;
import com.example.fooddelivery.Singleton;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Fragment fragment;
    DrawerLayout drawer;
    TextView nametext;
    public static FirebaseAuth auth;
    String facebookId, facebookName;
    ImageView photo;
    FirebaseUser user;
    Button sign;
    DatabaseInstance databaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment = new HomeFragment();
        setTitle("Home");

        final DatabaseReference databaseReference = Singleton.db.getReference();
        auth = Singleton.auth;
        user = Singleton.auth.getCurrentUser();

        databaseInstance = new DatabaseInstance(MainScreen.this);
        databaseInstance.createTable();
        databaseInstance.close();

        FacebookSdk.setApplicationId("581033482823166");
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main_screen);

        // Activity setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setItemTextColor(ColorStateList.valueOf(Color.BLACK));
        navigationView.setItemIconTintList(ColorStateList.valueOf(Color.BLACK));
        navigationView.setNavigationItemSelectedListener(this);

        View view = navigationView.getRootView();
        view.setBackgroundResource(R.drawable.home_back);

        nametext = navigationView.getHeaderView(0).findViewById(R.id.username);
        photo = navigationView.getHeaderView(0).findViewById(R.id.logo);
        sign = navigationView.getHeaderView(0).findViewById(R.id.buttonloginorsignup);

        // Handle Fragments
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment).commit();

        if (user != null) {
            navigationView.inflateMenu(R.menu.main_screen_drawer_login);
            databaseReference.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    facebookName = String.valueOf(dataSnapshot.child("name").getValue());
                    facebookId = String.valueOf(dataSnapshot.child("id").getValue());
                    nametext.setText(facebookName);
                    if (!facebookId.equalsIgnoreCase("null")) {
                        sign.setText("Log Out");
                        Glide.with(MainScreen.this).load("http://graph.facebook.com/" + facebookId + "/picture?type=normal").into(photo);
                        sign.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                auth.signOut();
                                LoginManager.getInstance().logOut();
                                Intent intent = new Intent(MainScreen.this, SigninActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        sign.setText("Log Out");
                        photo.setImageResource(R.drawable.male3);
                        sign.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                auth.signOut();
                                Intent intent = new Intent(MainScreen.this, SigninActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            navigationView.inflateMenu(R.menu.activity_main_screen_drawer);
            nametext.setText("Guest User");
            photo.setImageResource(R.drawable.male3);
            sign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainScreen.this, SigninActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finishAffinity();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home: {
                handleFragment("Home", new HomeFragment());
                break;
            }
            case R.id.nav_about: {
                handleFragment("About us", new AboutFragment());
                break;
            }
            case R.id.nav_inbox: {
                Intent i = new Intent(this, ChatActivity.class);
                startActivity(i);
                break;
            }
            case R.id.nav_call:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Do you want to call Food Delivery's helpline?");
                alertDialogBuilder.setPositiveButton("CALL",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                String tel = "370666666666";
                                callIntent.setData(Uri.parse("tel:" + tel));
                                callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                if (ContextCompat.checkSelfPermission(MainScreen.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    ActivityCompat.requestPermissions(MainScreen.this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                                } else {
                                    startActivity(callIntent);
                                }
                            }
                        });

                alertDialogBuilder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            case R.id.nav_rate: {
                handleFragment("Rate us", new RateFragment());
                break;
            }
            case R.id.order: {
                handleFragment("Order", new AllOrders());
                break;
            }
            case R.id.cartnav: {
                handleFragment("Your Carts", new AllCartsFragment());
                break;
            }
            case R.id.favourite: {
                handleFragment("Favourites", new AllFavouriteFragment());
                break;
            }
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void handleFragment(String title, Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        fm.popBackStack();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment).commit();
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

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}


