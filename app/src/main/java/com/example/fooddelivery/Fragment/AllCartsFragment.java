package com.example.fooddelivery.Fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fooddelivery.HelperModal.FoodElement;
import com.example.fooddelivery.HelperModal.OrderList;
import com.example.fooddelivery.Fragment.MainScreenFragment.HomeFragment;
import com.example.fooddelivery.R;
import com.example.fooddelivery.Additional.DatabaseInstance;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class AllCartsFragment extends Fragment {

    private View view;
    private ArrayList<CartFragment> adapterItems = new ArrayList<>();
    private ArrayList<OrderList> orderList = new ArrayList<>();
    ViewGroup c;
    private ViewPager pager;
    TabLayout tabLayout;
    ViewPagerAdapter adapter;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AllCartsFragment() {
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
        tabLayout = (TabLayout) view.findViewById(R.id.tabsorder);
        setUpView();
        return view;
    }

    public void setUpView(){
        setupViewPager(pager);

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

       /* @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            try{
            FragmentManager manager = ((Fragment)object).getFragmentManager();
            FragmentTransaction trans = manager.beginTransaction();
            trans.remove((Fragment)object);
            trans.commit();
            super.destroyItem(container, position, object);
            }catch (Exception e){
                e.printStackTrace();
            }
        }*/
    }
}