package com.example.food__delivery.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.food__delivery.Adapter.MyallcartRecyclerViewAdapter;
import com.example.food__delivery.Helper.FoodElement;
import com.example.food__delivery.Helper.OrderList;
import com.example.food__delivery.MainNavigationActivity.HomeFragment;
import com.example.food__delivery.R;
import com.example.food__delivery.Testing.DatabaseEntry;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AllCartsFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private View view;
    private ArrayList<CartFragment> adapterItems = new ArrayList<>();
    private ArrayList<OrderList> orderList = new ArrayList<>();
    static ViewGroup c;
    private ViewPager pager;
    ViewPagerAdapter adapter;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AllCartsFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static AllCartsFragment newInstance(int columnCount) {
        AllCartsFragment fragment = new AllCartsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_orders, container, false);
        Log.d("Create", "This is run Line 72 Allcarts");
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
        pager.removeAllViews();
        Log.d("TAG", "onDestroy: This is run " + pager.getAdapter().getCount());
        pager.removeAllViews();
        pager.removeAllViewsInLayout();
        for(int i =0; i < pager.getAdapter().getCount();i++)
            pager.getAdapter().destroyItem(c,i, adapterItems.get(i));
        adapterItems.clear();
        Log.d("TAG", "onDestroy: This is run " + pager.getAdapter().getCount());
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        Log.d("TAG", "setupViewPager: Line 106 This is being run");
        Log.d("TAG", "setupViewPager: "+adapter.getCount());
        DatabaseEntry db = new DatabaseEntry(getContext());
        for(int i=0;i< HomeFragment.menuList.size();i++){
            ArrayList<FoodElement> f;
            f = (ArrayList<FoodElement>) db.getDataFromDB("cart_table", i);
            if(f.size() == 0){
                continue;
            }
            f.clear();
            adapterItems.add(new CartFragment(i));
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
            FragmentManager manager = ((Fragment)object).getFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove((Fragment)object);
            trans.commit();
            super.destroyItem(container, position, object);
        }
    }
}
