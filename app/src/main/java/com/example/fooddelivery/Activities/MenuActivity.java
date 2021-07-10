package com.example.fooddelivery.Activities;

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

import com.example.fooddelivery.HelperModal.FoodElement;
import com.example.fooddelivery.Fragment.MainScreenFragment.HomeFragment;
import com.example.fooddelivery.Fragment.CategoryHandlerFragment;
import com.example.fooddelivery.R;
import com.example.fooddelivery.Additional.DatabaseInstance;
import com.example.fooddelivery.Singleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Character.compare;

public class MenuActivity extends AppCompatActivity {

    private final int EDIT_DISTANCE = 2;

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
    private ArrayList<com.example.fooddelivery.HelperModal.Menu> menuList = HomeFragment.menuList;
    public ViewPager mViewPager;
    public FloatingActionButton fab;
    public TextInputLayout foodsearchparent;
    public TextInputEditText foodsearch;
    public TabLayout tabLayout;

    private DatabaseInstance databaseInstance;
    private ArrayList<FoodElement> foodElements = new ArrayList<>();
    Bundle savedInstanceState;
    ImageView imageView;
    public int restaurantId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        Intent intent = getIntent();
        restaurantId = intent.getIntExtra("restaurantId" ,0);

        setContentView(R.layout.activity_menu);
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
        auth = Singleton.auth;
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
        databaseInstance = new DatabaseInstance(this);
        MenuItem item = menu.findItem(R.id.action_cart);
        MenuItemCompat.setActionView(item, R.layout.badge_layout);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);
        tv = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
        imageView = (ImageView)notifCount.findViewById(R.id.imagenotif);
        tv.setText(String.valueOf(databaseInstance.totalQty(restaurantId)));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, Cart_Favorite.class);
                intent.putExtra("ViewPager", 0);
                intent.putExtra("restaurantId", restaurantId);
                startActivity(intent);
            }
        });

        databaseInstance.close();
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
                Intent intent = new Intent(MenuActivity.this, Cart_Favorite.class);
                intent.putExtra("ViewPager", 0);
                intent.putExtra("restaurantId", restaurantId);
                startActivity(intent);
                break;
            }
            case R.id.favorite: {
                Intent intent = new Intent(MenuActivity.this, Cart_Favorite.class);
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

        for(String category: com.example.fooddelivery.HelperModal.Menu.categories){
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
        databaseInstance.close();
    }

    @Override
    protected void onPause() {
        super.onPause();
        databaseInstance.close();
    }

    private ArrayList<FoodElement> filterFood(ArrayList<FoodElement> foodElements, String filter){
        ArrayList<FoodElement> filtered = new ArrayList<>();
        if(filter == "")
            return foodElements;

        for(FoodElement food:foodElements){
            String lc = food.getName().toLowerCase();

            for(int i = 0; i < lc.length() - filter.length() + 1; i++){
                String temp = lc.substring(i, i + java.lang.Math.min(filter.length(), lc.length()));
                Log.d("TAG", editDistance( temp, filter) +" "+filter+" "+temp);
                if(lc.contains(filter) || editDistance(temp, filter) <= EDIT_DISTANCE){
                    filtered.add(food);
                    break;
                }
            }
        }
        return filtered;
    }

    private int editDistance(String left, String right) {
        int row_len = left.length() + 1;
        int col_len = right.length() + 1;

        int[][] editDistances = new int[row_len][col_len];

        for(int i = 0; i < row_len; i++){
            for(int j = 0; j < col_len; j++){
                if(i == 0) {
                    editDistances[i][j] = j;
                }
                else if( j == 0 ){
                    editDistances[i][j] = i;
                }else if (compare(left.charAt(i-1), right.charAt(j-1)) == 0){
                    editDistances[i][j] = java.lang.Math.min(editDistances[i-1][j], java.lang.Math.min(editDistances[i][j-1], editDistances[i-1][j-1]));
                }else {
                    editDistances[i][j] = 1 + java.lang.Math.min(editDistances[i-1][j], java.lang.Math.min(editDistances[i][j-1], editDistances[i-1][j-1]));
                }
            }
        }
        return editDistances[row_len-1][col_len-1];
    }
}
