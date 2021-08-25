package com.example.fooddelivery.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fooddelivery.Fragment.CartFragment;
import com.example.fooddelivery.Fragment.FavouriteFragment;
import com.example.fooddelivery.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Cart_Favorite extends AppCompatActivity {

    private int restaurantId;

    Fragment fragment;
    Toolbar toolbar;
    LinearLayout linearLayout;

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_favorite:
                    handleFragment("Favorite", new FavouriteFragment(restaurantId));
                    return true;
                case R.id.navigation_cart:
                    handleFragment("Cart", new CartFragment(restaurantId));
                    return true;
            }
            return false;
        }
    };

    private void handleFragment(String title, Fragment fragment) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        getSupportActionBar().setTitle(title);

        linearLayout = findViewById(R.id.container);
        linearLayout = findViewById(R.id.container);
        linearLayout.setBackgroundResource(R.drawable.fav_back);
        fragmentTransaction.replace(R.id.content, fragment).commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart__favorite);

        restaurantId = getIntent().getIntExtra("restaurantId", 0);

        Intent intent = getIntent();
        BottomNavigationView navigation = findViewById(R.id.navigation);
        toolbar = findViewById(R.id.toolbar8);
        linearLayout = findViewById(R.id.container);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(0xFFFFFFFF);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.navigation_cart);

        switch (intent.getIntExtra("ViewPager", 0)) {
            case 0:
                getSupportActionBar().setTitle("Cart");
                linearLayout.setBackgroundResource(R.drawable.cart_back);
                navigation.setSelectedItemId(R.id.navigation_cart);
                break;
            case 1:
                getSupportActionBar().setTitle("Favourite");
                linearLayout.setBackgroundResource(R.drawable.fav_back);
                navigation.setSelectedItemId(R.id.navigation_favorite);
                break;
        }
    }
}
