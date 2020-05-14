package com.example.food__delivery.MainNavigationActivity;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.food__delivery.Helper.FoodElement;
import com.example.food__delivery.Helper.Menu;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.example.food__delivery.Adapter.Adapter_Menu;
import com.example.food__delivery.Helper.Restaurant;
import com.example.food__delivery.R;
import com.example.food__delivery.Testing.SlidingImage_Adapter;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends androidx.fragment.app.Fragment {

    ProgressDialog loading;
    public static List<Restaurant> restaurantList  = new ArrayList<>();
    public static List<Menu> menuList = new ArrayList<>();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager reLayoutManager;
    Adapter_Menu recyclerViewadapter;
    boolean done;
    boolean menuDone;
    private static ViewPager mPager;
    private GetElements getElements = new GetElements();
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES = {R.drawable.paneer, R.drawable.parantha_1, R.drawable.kathiroll, R.drawable.rice, R.drawable.nonveg};
    private ArrayList<Integer> ImagesArray = new ArrayList<Integer>();
    public final static DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        if(restaurantList.size() == 0) {
            getElements.execute();
        }
        else{
            recyclerViewadapter = new Adapter_Menu(getActivity(), restaurantList);
            recyclerView.setAdapter(recyclerViewadapter);
        }

        reLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView.setLayoutManager(reLayoutManager);
        return view;
    }

    private void init(View view) {
        for (int i = 0; i < IMAGES.length; i++)
            ImagesArray.add(IMAGES[i]);
        mPager = (ViewPager) view.findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(getActivity(), ImagesArray));
        CirclePageIndicator indicator = (CirclePageIndicator)
                view.findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(3 * density);

        NUM_PAGES = IMAGES.length;

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 7500, 7500);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    private class GetElements extends AsyncTask<String, String, List<Restaurant>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            loading = new ProgressDialog(getActivity());
            loading.setMessage("One cannot think well, love well, sleep well, if one has not dined well! :)");
            loading.setCancelable(false);
            loading.show();

        }

        @Override
        protected List<Restaurant> doInBackground(String... arg0) {
            done = false;
            menuDone = false;
            DatabaseReference restaurantRef = mDatabase.child("restaurants");
            DatabaseReference menuRef = mDatabase.child("menu");
            Log.d("Adding Ref",menuRef.toString());
            restaurantRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot child:dataSnapshot.getChildren()) {
                        Restaurant Restaurant = new Restaurant();
                        Restaurant.photo = (child.child("Imagepath").getValue(String.class));
                        Restaurant.restaurantName = (child.child("Category").getValue(String.class));
                        restaurantList.add(Restaurant);
                    }
                    done = true;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            menuRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    ArrayList<FoodElement> foodElements = new ArrayList<>();
                    String nameOfKey;
                    for(DataSnapshot child:dataSnapshot.getChildren()) {
                        Menu menuItem = new Menu();
                        for(DataSnapshot category: child.getChildren()) {
                            nameOfKey = category.getKey();
                            Log.d("category", "Line 195 Home Fragment,"+nameOfKey+" "+ category.getRef().toString());
                            for(DataSnapshot childOfChild: category.getChildren()) {
                                FoodElement foodElement = new FoodElement();
                                foodElement.setPhoto(childOfChild.child("image").getValue(String.class));
                                foodElement.setName(childOfChild.child("name").getValue(String.class));
                                foodElement.setFoodType(childOfChild.child("category").getValue(String.class));
                                foodElement.setPrice(childOfChild.child("price").getValue(String.class));
                                foodElement.setRate(childOfChild.child("rate").getValue(Integer.class));
                                foodElements.add(foodElement);
                            }
                            menuItem.setIndex(nameOfKey, foodElements);
                            foodElements.clear();
                        }
                        menuList.add(menuItem);
                    }
                    menuDone = true;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            while(!done);
            while(!menuDone);
            return restaurantList;
        }

        @Override
        protected void onPostExecute(List<Restaurant> result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (loading.isShowing())
                loading.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            recyclerViewadapter = new Adapter_Menu(getActivity(), restaurantList);
            recyclerView.setAdapter(recyclerViewadapter);
        }
    }
}
