package com.example.food__delivery.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.food__delivery.Helper.FoodElement;
import com.example.food__delivery.MainNavigationActivity.HomeFragment;
import com.example.food__delivery.MenuActivity.CategoryHandlerFragment;
import com.example.food__delivery.R;
import com.example.food__delivery.Testing.DatabaseEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class AfterMain extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    FirebaseAuth auth;
    public static TextView tv;
    private ArrayList<com.example.food__delivery.Helper.Menu> menuList = HomeFragment.menuList;
    public ViewPager mViewPager;
    public FloatingActionButton fab;
    public TextInputLayout foodsearchparent;
    public TextInputEditText foodsearch;
    public TabLayout tabLayout;

    private DatabaseEntry databaseEntry;
    private ArrayList<FoodElement> foodElements = new ArrayList<>();
    public static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    Bundle savedInstanceState;
    ImageView imageView;
    public int restaurantId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        Intent intent = getIntent();
        restaurantId = intent.getIntExtra("restaurantId" ,0);

        setContentView(R.layout.activity_after_main);
        foodsearch = findViewById(R.id.food_search);
        foodsearchparent = findViewById(R.id.food_search_parent);
        fab = findViewById(R.id.floatingActionButton);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        //mViewPager.setOffscreenPageLimit(9);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#5CA67C"));
        auth = FirebaseAuth.getInstance();
        mViewPager.addOnAdapterChangeListener(new ViewPager.OnAdapterChangeListener() {
            @Override
            public void onAdapterChanged(@NonNull ViewPager viewPager, @Nullable PagerAdapter oldAdapter, @Nullable PagerAdapter newAdapter) {
                newAdapter.startUpdate(viewPager);
            }
        });

        foodsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setupViewPager(mViewPager);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_after_main, menu);
        databaseEntry = new DatabaseEntry(this);
        MenuItem item = menu.findItem(R.id.action_cart);
        MenuItemCompat.setActionView(item, R.layout.badge_layout);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);
        tv = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
        imageView = (ImageView)notifCount.findViewById(R.id.imagenotif);
        tv.setText(String.valueOf(databaseEntry.totalQty(restaurantId)));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AfterMain.this, Cart_Favorite.class);
                intent.putExtra("ViewPager", 0);
                intent.putExtra("restaurantId", restaurantId);
                startActivity(intent);
            }
        });

        databaseEntry.close();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                finish();
                break;
            case R.id.action_cart: {
                Intent intent = new Intent(AfterMain.this, Cart_Favorite.class);
                intent.putExtra("ViewPager", 0);
                intent.putExtra("restaurantId", restaurantId);
                startActivity(intent);
                break;
            }
            case R.id.favorite: {
                Intent intent = new Intent(AfterMain.this, Cart_Favorite.class);
                intent.putExtra("ViewPager", 1);
                intent.putExtra("restaurantId", restaurantId);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        /*if(menuList.get(restaurantId).vegStarters.size() > 0)
            adapter.addFrag(new CategoryHandlerFragment(restaurantId, menuList.get(restaurantId).vegStarters), "Veg Starters");

        if(menuList.get(restaurantId).nonVegStarters.size() > 0)
            adapter.addFrag(new CategoryHandlerFragment(restaurantId, menuList.get(restaurantId).nonVegStarters), "Non-Veg Starters");

        if(menuList.get(restaurantId).vegMainCourse.size() > 0)
            adapter.addFrag(new CategoryHandlerFragment(restaurantId, menuList.get(restaurantId).vegMainCourse), "Veg Main Course");

        if(menuList.get(restaurantId).nonVegMainCourse.size() > 0)
            adapter.addFrag(new CategoryHandlerFragment(restaurantId, menuList.get(restaurantId).nonVegMainCourse), "Non-Veg Main Course");

        if(menuList.get(restaurantId).sweets.size() > 0)
            adapter.addFrag(new CategoryHandlerFragment(restaurantId, menuList.get(restaurantId).sweets), "Sweets");

        if(menuList.get(restaurantId).rolls.size() > 0)
            adapter.addFrag(new CategoryHandlerFragment(restaurantId, menuList.get(restaurantId).rolls), "Rolls");

        if(menuList.get(restaurantId).rice.size() > 0)
            adapter.addFrag(new CategoryHandlerFragment(restaurantId, menuList.get(restaurantId).rice), "Rice");

        if(menuList.get(restaurantId).breads.size() > 0)
            adapter.addFrag(new CategoryHandlerFragment(restaurantId, menuList.get(restaurantId).breads), "Breads");

        if(menuList.get(restaurantId).beverages.size() > 0)
            adapter.addFrag(new CategoryHandlerFragment(restaurantId, menuList.get(restaurantId).beverages), "Beverages");*/

        for(String category: com.example.food__delivery.Helper.Menu.categories){
            Log.d("TAG", "setupViewPager: "+foodsearch.getText().toString().toLowerCase());
            try {
                ArrayList<FoodElement> temp = filterFood(menuList.get(restaurantId).getIndex(category),foodsearch.getText().toString().toLowerCase());
                if (temp.size() > 0)
                    adapter.addFrag(new CategoryHandlerFragment(restaurantId, temp, this), category);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseEntry.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
        databaseEntry.close();
    }

    private ArrayList<FoodElement> filterFood(ArrayList<FoodElement> foodElements, String filter){
        ArrayList<FoodElement> filtered = new ArrayList<>();
        for(FoodElement food:foodElements){
            if(food.getName().toLowerCase().contains(filter))
                filtered.add(food);
        }
        return filtered;
    }

}
