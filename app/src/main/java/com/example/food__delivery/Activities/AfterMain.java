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

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.example.food__delivery.MenuActivity.BreadsFragment;
import com.example.food__delivery.MenuActivity.PlattersFragment;
import com.example.food__delivery.MenuActivity.NonVegMainCourseFragment;
import com.example.food__delivery.MenuActivity.BeveragesFragment;
import com.example.food__delivery.MenuActivity.VegStartersFragment;
import com.example.food__delivery.MenuActivity.RiceFragment;
import com.example.food__delivery.MenuActivity.RollsFragment;
import com.example.food__delivery.MenuActivity.VegMainCoursefragment;
import com.example.food__delivery.MenuActivity.NonVegStartersFragment;
import com.example.food__delivery.R;
import com.example.food__delivery.Testing.DatabaseEntry;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    private ViewPager mViewPager;
    private DatabaseEntry databaseEntry;
    public static DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        Intent intent = getIntent();

        int restaurantId = intent.getIntExtra("restaurantId" ,0);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mViewPager = (ViewPager) findViewById(R.id.container);
        setupViewPager(mViewPager);
        //mViewPager.setOffscreenPageLimit(2);
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
        tv.setText(String.valueOf(databaseEntry.totalQty()));

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AfterMain.this, Cart_Favorite.class);
                intent.putExtra("ViewPager", 0);
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
                startActivity(intent);
                break;
            }
            case R.id.favorite: {
                Intent intent = new Intent(AfterMain.this, Cart_Favorite.class);
                intent.putExtra("ViewPager", 1);
                startActivity(intent);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new VegStartersFragment(), "Veg Starters");
        adapter.addFrag(new NonVegStartersFragment(), "Non-Veg Starters");
        adapter.addFrag(new VegMainCoursefragment(), "Veg Main Course");
        adapter.addFrag(new NonVegMainCourseFragment(), "Non-Veg Main Course");
        adapter.addFrag(new PlattersFragment(), "Platters");
        adapter.addFrag(new RollsFragment(), "Rolls");
        adapter.addFrag(new RiceFragment(), "Rice");
        adapter.addFrag(new BreadsFragment(), "Breads");
        adapter.addFrag(new BeveragesFragment(), "Beverages");
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
