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
import com.example.food__delivery.MainNavigationActivity.AboutFragment;
import com.example.food__delivery.MainNavigationActivity.HomeFragment;
import com.example.food__delivery.MainNavigationActivity.RateFragment;
import com.example.food__delivery.R;
import com.example.food__delivery.Testing.DatabaseEntry;

public class MainScreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    Fragment fragment;
    DrawerLayout drawer;
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
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View view = navigationView.getRootView();
        view.setBackgroundResource(R.drawable.home_back);
        fragment = new HomeFragment();
        setTitle("Home");
        databaseEntry = new DatabaseEntry(MainScreen.this);
        databaseEntry.createTable();
        databaseEntry.close();
        navigationView.setItemTextColor(ColorStateList.valueOf(Color.BLACK));
        navigationView.setItemIconTintList(ColorStateList.valueOf(Color.BLACK));
        nametext = (TextView)navigationView.getHeaderView(0).findViewById(R.id.textView2);
        photo = (ImageView)navigationView.getHeaderView(0).findViewById(R.id.imageView);
        sign = (Button)navigationView.getHeaderView(0).findViewById(R.id.button15);
        auth = FirebaseAuth.getInstance();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, fragment).commit();
        navigationView.setNavigationItemSelectedListener(this);
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            navigationView.inflateMenu(R.menu.main_screen_drawer_login);
            databaseReference.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot){
                        namef = String.valueOf(dataSnapshot.child("name").getValue());
                        idf= String.valueOf(dataSnapshot.child("id").getValue());
                        nametext.setText(namef);
                        if(!idf.equalsIgnoreCase("null")){
                            sign.setText("Log Out");
                            Glide.with(MainScreen.this).load("http://graph.facebook.com/" + idf + "/picture?type=normal").into(photo);
                            sign.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    auth.signOut();
                                    LoginManager.getInstance().logOut();
                                    Intent intent = new Intent(MainScreen.this, Login.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }else{
                            sign.setText("Log Out");
                            photo.setImageResource(R.drawable.male3);
                            sign.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    auth.signOut();
                                    Intent intent = new Intent(MainScreen.this, Login.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
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
        }else{
            navigationView.inflateMenu(R.menu.activity_main_screen_drawer);
            //sign.setText("com.example.fooddelivery.Signup or com.example.fooddelivery.Login");
            nametext.setText("Guest User");
            photo.setImageResource(R.drawable.male3);
            sign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainScreen.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else{
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
                fragment = new HomeFragment();
                setTitle("Home");
                FragmentManager fm = getSupportFragmentManager();
                fm.popBackStack();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment).commit();
                break;
            }
            case R.id.nav_dine: {
                fragment = new HomeFragment();
                setTitle("A La Carte");
                FragmentManager fm = getSupportFragmentManager();
                fm.popBackStack();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment).commit();
                break;
            }
            case R.id.nav_about: {
                fragment = new AboutFragment();
                setTitle("About Us");
                FragmentManager fm = getSupportFragmentManager();
                fm.popBackStack();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment).commit();
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
                fragment = new RateFragment();
                setTitle("Rate Us");
                FragmentManager fm = getSupportFragmentManager();
                fm.popBackStack();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment).commit();
                break;
            }
            case R.id.order: {
                fragment = new AllOrders();
                setTitle("Order");
                FragmentManager fm = getSupportFragmentManager();
                fm.popBackStack();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment).commit();
                break;
            }
            case R.id.cartnav: {
                fragment = new AllCartsFragment();
                setTitle("Your Carts");
                FragmentManager fm = getSupportFragmentManager();
                fm.popBackStack();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment).commit();
                break;
            }
            case R.id.favourite: {
                //fragment = new AllFavoriteFragemnt();
                fragment = new FavouriteFragment(0);
                setTitle("Favourite");
                FragmentManager fm = getSupportFragmentManager();
                fm.popBackStack();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment).commit();
                break;
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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


