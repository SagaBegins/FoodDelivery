package com.example.food__delivery.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
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
    private ViewPager mViewPager;
    public FloatingActionButton fab;
    public TextInputLayout foodsearchparent;
    public TextInputEditText foodsearch;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        //mViewPager.setOffscreenPageLimit(9);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#5CA67C"));
        auth = FirebaseAuth.getInstance();
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
            try {
                ArrayList<FoodElement> temp = menuList.get(restaurantId).getIndex(category);
                if (temp.size() > 0)
                    adapter.addFrag(new CategoryHandlerFragment(restaurantId, temp, this), category);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
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

}
