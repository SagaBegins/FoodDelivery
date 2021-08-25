package com.example.fooddelivery.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.fooddelivery.Additional.DatabaseInstance;
import com.example.fooddelivery.Fragment.MainScreenFragment.HomeFragment;
import com.example.fooddelivery.HelperModal.FoodElement;
import com.example.fooddelivery.HelperModal.OrderList;
import com.example.fooddelivery.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AllFavouriteFragment extends Fragment {

    private View view;
    private final ArrayList<FavouriteFragment> adapterItems = new ArrayList<>();
    private final ArrayList<OrderList> orderList = new ArrayList<>();
    private ViewPager pager;

    ViewGroup viewGroup;
    ViewPagerAdapter adapter;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AllFavouriteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_cart_fav, container, false);
        viewGroup = container;
        pager = view.findViewById(R.id.containerorder);
        setUpView();
        return view;
    }

    public void setUpView() {
        setupViewPager(pager);
        TabLayout tabLayout = view.findViewById(R.id.tabsorder);
        tabLayout.setupWithViewPager(pager);
        tabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#5CA67C"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        DatabaseInstance db = new DatabaseInstance(getContext());

        for (int i = 0; i < HomeFragment.menuList.size(); i++) {
            ArrayList<FoodElement> f;
            f = (ArrayList<FoodElement>) db.getDataFromDB("favour_table", i);
            if (f.size() == 0) {
                continue;
            }
            f.clear();
            adapterItems.add(new FavouriteFragment(i));
            adapter.addFrag(adapterItems.get(adapter.getCount()), HomeFragment.restaurantList.get(i).restaurantName);
        }
        db.close();

        Log.d("TAG", "setupViewPager: " + adapter.getCount());
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public androidx.fragment.app.Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(androidx.fragment.app.Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }
}
