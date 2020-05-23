package com.example.food__delivery.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.food__delivery.Helper.FoodElement;
import com.example.food__delivery.Helper.OrderList;
import com.example.food__delivery.MainNavigationActivity.HomeFragment;
import com.example.food__delivery.R;
import com.example.food__delivery.Testing.DatabaseEntry;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AllFavouriteFragment extends Fragment {

    private View view;
    private ArrayList<FavouriteFragment> adapterItems = new ArrayList<>();
    private ArrayList<OrderList> orderList = new ArrayList<>();
    ViewGroup c;
    private ViewPager pager;
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
        c = container;
        pager = (ViewPager) view.findViewById(R.id.containerorder);
        setUpView();
        return view;
    }

    public void setUpView(){
        setupViewPager(pager);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabsorder);
        tabLayout.setupWithViewPager(pager);
        tabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#5CA67C"));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        for(int i =0; i < pager.getAdapter().getCount();i++)
            pager.getAdapter().destroyItem(c,i, adapterItems.get(i));
        adapterItems.clear();
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        DatabaseEntry db = new DatabaseEntry(getContext());
        for(int i = 0; i< HomeFragment.menuList.size(); i++){
            ArrayList<FoodElement> f;
            f = (ArrayList<FoodElement>) db.getDataFromDB("favour_table", i);
            if(f.size() == 0){
                continue;
            }
            f.clear();
            adapterItems.add(new FavouriteFragment(i));
            adapter.addFrag(adapterItems.get(adapter.getCount()) , HomeFragment.restaurantList.get(i).restaurantName);
        }
        db.close();
        Log.d("TAG", "setupViewPager: "+adapter.getCount());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
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

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            try {
                FragmentManager manager = ((Fragment) object).getFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.remove((Fragment) object);
                trans.commit();
                super.destroyItem(container, position, object);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
