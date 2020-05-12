package com.example.food__delivery.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.food__delivery.Fragment.CartFragment;
import com.example.food__delivery.Fragment.FavouriteFragment;
import com.example.food__delivery.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Cart_Favorite extends AppCompatActivity {

    Fragment fragment;
    Toolbar toolbar;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_favorite:
                    getSupportActionBar().setTitle("Favourite");
                    fragment = new FavouriteFragment();
                    fragmentTransaction.replace(R.id.content, fragment).commit();
                    return true;
                case R.id.navigation_cart:
                    getSupportActionBar().setTitle("Cart");
                    fragment = new CartFragment();
                    fragmentTransaction.replace(R.id.content, fragment).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart__favorite);
        toolbar = (Toolbar) findViewById(R.id.toolbar8);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_cart);
        Intent intent = getIntent();
        switch (intent.getIntExtra("ViewPager", 0)) {
            case 0:
                getSupportActionBar().setTitle("Cart");
                navigation.setSelectedItemId(R.id.navigation_cart);
                break;
            case 1:
                getSupportActionBar().setTitle("Favourite");
                navigation.setSelectedItemId(R.id.navigation_favorite);
                break;
        }
    }
}
