package com.example.food__delivery.MainNavigationActivity;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.text.style.CharacterStyle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.food__delivery.Helper.FoodElement;
import com.example.food__delivery.Helper.Menu;
import com.example.food__delivery.Helper.OrderList;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
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
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends androidx.fragment.app.Fragment {

    public static ArrayList<Restaurant> restaurantList = new ArrayList<>();
    public static ArrayList<Menu> menuList = new ArrayList<>();
    public static ArrayList<OrderList> ordersList = new ArrayList<>();

    ProgressDialog loading;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager reLayoutManager;
    Adapter_Menu recyclerViewadapter;
    boolean done;
    boolean menuDone;
    boolean orderDone;
    private static ViewPager mPager;
    private static boolean[] filteredMenu = new boolean[5];

    private static List<Restaurant> filteredRestaurant = new ArrayList<>();
    private GetElements getElements = new GetElements();
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES = {R.drawable.discount1, R.drawable.discount2, R.drawable.discount3, R.drawable.discount4};
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
        TextInputEditText searchFilter = (TextInputEditText) view.findViewById(R.id.editText_search);
        //EditText searchFilter = (EditText) view.findViewById(R.id.editText_search);
        searchFilter.setTextColor(Color.WHITE);
        searchFilter.setHintTextColor(Color.WHITE);
        searchFilter.setHint("Food Search");
        searchFilter.setSingleLine();
        searchFilter.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        searchFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                onKeyFilter(s);
            }
        });

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        if (restaurantList.size() == 0) {
            getElements.execute();
        } else {
            recyclerViewadapter = new Adapter_Menu(getActivity(), restaurantList);
            recyclerView.setAdapter(recyclerViewadapter);
        }

        reLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext()) {
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
            loading.setMessage("After Dinner Rest a While; After Supper Walk a Mile");
            loading.setCancelable(false);
            loading.show();

        }

        @Override
        protected List<Restaurant> doInBackground(String... arg0) {
            done = false;
            menuDone = false;
            orderDone = false;
            DatabaseReference restaurantRef = mDatabase.child("restaurants");
            DatabaseReference menuRef = mDatabase.child("menu");
            FirebaseAuth auth = FirebaseAuth.getInstance();
            try {
                DatabaseReference orderRef = mDatabase.child("Orders").child(auth.getCurrentUser().getUid());
                orderRef.orderByKey().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ArrayList<FoodElement> foodElements = new ArrayList<>();
                        for (DataSnapshot order : dataSnapshot.getChildren()) {
                            OrderList orderList = new OrderList();
                            orderList.txnId = order.getKey();
                            orderList.txnTime = order.child("txnTime").getValue(String.class);
                            orderList.txnTime = orderList.txnTime.split("GMT")[0];
                            orderList.restaurantId = order.child("restaurantId").getValue(Integer.class);
                            orderList.amount = order.child("amount").getValue(String.class);
                            Log.d("TAG", "onDataChange: "+ orderList.amount);
                            orderList.status = order.child("status").getValue(String.class);
                            for (DataSnapshot food : order.child("foodList").getChildren()) {
                                FoodElement foodElement = new FoodElement();
                                foodElement.setPhoto(food.child("image").getValue(String.class));
                                foodElement.setName(food.child("name").getValue(String.class));
                                foodElement.setFoodType(food.child("category").getValue(String.class));
                                foodElement.setPrice(food.child("price").getValue(String.class));
                                foodElement.setQty(food.child("qty").getValue(Integer.class));
                                foodElement.setDescription(food.child("description").getValue(String.class));
                                foodElement.setRate(food.child("rate").getValue(Integer.class));
                                foodElements.add(foodElement);
                            }
                            orderList.foodList = (ArrayList<FoodElement>) foodElements.clone();
                            foodElements.clear();
                            ordersList.add(0, orderList);
                        }
                        orderDone = true;
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }catch(Exception e){
                orderDone = true;
                e.printStackTrace();
            }
            Log.d("Adding Ref", menuRef.toString());
            restaurantRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
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
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Menu menuItem = new Menu();
                        for (DataSnapshot category : child.getChildren()) {
                            nameOfKey = category.getKey();
                            Log.d("category", "Line 195 Home Fragment," + nameOfKey + " " + category.getRef().toString());
                            for (DataSnapshot food : category.getChildren()) {
                                FoodElement foodElement = new FoodElement();
                                foodElement.setPhoto(food.child("image").getValue(String.class));
                                foodElement.setName(food.child("name").getValue(String.class));
                                foodElement.setFoodType(food.child("category").getValue(String.class));
                                foodElement.setPrice(food.child("price").getValue(String.class));
                                foodElement.setDescription(food.child("description").getValue(String.class));
                                foodElement.setRate(food.child("rate").getValue(Integer.class));
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

            while (!done) ;
            while (!menuDone) ;
            while (!orderDone) ;
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

    private boolean[] setBoolArrayTo(boolean bool, int size) {
        boolean[] newBool = new boolean[size];
        for (int i = 0; i < size; i++) {
            newBool[i] = bool;
        }
        return newBool;
    }

    private void onKeyFilter(Editable v) {
        String filterText = v.toString();
        filteredMenu = setBoolArrayTo(false, filteredMenu.length);
        Log.d("Text", "Line 81 Home Fragement " + filterText);
        int iter = 0;
        if (filterText.equals("") || filterText.equals(" ")) {
            filteredMenu = setBoolArrayTo(true, filteredMenu.length);
        }
        for (Menu m : menuList) {
            filteredMenu[iter] = m.containsFood(filterText);
            iter++;
        }

        filteredRestaurant.clear();
        for (int i = 0; i < 5; i++) {
            if (filteredMenu[i]) {
                filteredRestaurant.add(restaurantList.get(i));
            }
        }

        recyclerViewadapter = new Adapter_Menu(getActivity(), filteredRestaurant);
        recyclerView.setAdapter(recyclerViewadapter);
    }
}
