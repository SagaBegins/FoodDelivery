package com.example.fooddelivery.Fragment.MainScreenFragment;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.fooddelivery.Adapter.Adapter_Menu;
import com.example.fooddelivery.Additional.SlidingImage_Adapter;
import com.example.fooddelivery.HelperModal.ChatModel;
import com.example.fooddelivery.HelperModal.FoodElement;
import com.example.fooddelivery.HelperModal.Menu;
import com.example.fooddelivery.HelperModal.Offer;
import com.example.fooddelivery.HelperModal.OrderList;
import com.example.fooddelivery.HelperModal.Restaurant;
import com.example.fooddelivery.R;
import com.example.fooddelivery.Singleton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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

    public static ArrayList<Restaurant> restaurantList = Singleton.restaurantList;
    public static ArrayList<Menu> menuList = Singleton.menuList;
    public static ArrayList<OrderList> ordersList = new ArrayList<>();
    public static Float off = 0.15f;
    public static boolean msgSent = false;
    public static Float threshold = 2000f;

    boolean d;
    boolean done;
    boolean menuDone;
    boolean orderDone;

    public final static DatabaseReference mDatabase = Singleton.db.getReference();

    DatabaseReference mRef;
    ArrayList<String> mChats;
    ArrayList<String> mMsg;
    FirebaseUser mUser;

    ProgressDialog loading;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager reLayoutManager;
    Adapter_Menu recyclerViewadapter;

    private static ViewPager mPager;
    private static boolean[] filteredMenu = new boolean[5];
    private static final List<Restaurant> filteredRestaurant = new ArrayList<>();
    private final GetElements getElements = new GetElements();

    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private static final Integer[] IMAGES = {R.drawable.discount1, R.drawable.discount3, R.drawable.discount4};
    private final ArrayList<Integer> ImagesArray = new ArrayList<Integer>();

    TextWatcher tw;
    TextInputEditText searchFilter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mUser = Singleton.auth.getCurrentUser();
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        init(view);

        mChats = new ArrayList<>();
        mMsg = new ArrayList<>();

        searchFilter = view.findViewById(R.id.editText_search);
        searchFilter.setTextColor(Color.WHITE);
        searchFilter.setHintTextColor(Color.WHITE);
        searchFilter.setHint("Food Search");
        searchFilter.setSingleLine();
        searchFilter.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        tw = new TextWatcher() {
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
        };

        searchFilter.addTextChangedListener(tw);

        recyclerView = view.findViewById(R.id.restaurantsrecycler);
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

        mPager = view.findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(getActivity(), ImagesArray));

        CirclePageIndicator indicator = view.findViewById(R.id.indicator);
        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;
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
            loading = new ProgressDialog(getActivity());
            loading.setMessage("After Dinner Rest a While; After Supper Walk a Mile");
            loading.setCancelable(false);
            loading.show();
        }

        @Override
        protected List<Restaurant> doInBackground(String... arg0) {
            FirebaseAuth auth = Singleton.auth;

            done = false;
            menuDone = false;
            orderDone = false;

            DatabaseReference restaurantRef = mDatabase.child("restaurants");
            DatabaseReference menuRef = mDatabase.child("menu");
            DatabaseReference offerRef = mDatabase.child("offers");

            d = false;
            offerRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Singleton.offers = dataSnapshot.getValue(Offer.class);
                    d = true;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            while (!d) ;
            try {
                off = Singleton.offers.totalDiscount.off;
                threshold = Singleton.offers.totalDiscount.threshold;
            } catch (NullPointerException e) {
                off = 0.0f;
                threshold = 0.0f;
            }

            mRef = Singleton.db.getReference().child("Chat");
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mChats.clear();
                    mMsg.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            ChatModel cm = d.getValue(ChatModel.class);
                            //Getting all the chats that user sent
                            try {
                                if (cm.getFrom().equals(mUser.getEmail())) {
                                    mChats.add(cm.getTo());
                                    msgSent = true;
                                    mMsg.add(cm.getMsg());
                                }
                            } catch (Exception e) {
                                break;
                            }
                            //Getting all the chats sent to the user
                            if (cm.getTo().equals(mUser.getEmail())) {
                                mChats.add(cm.getFrom());
                                msgSent = false;
                                mMsg.add(cm.getMsg());
                            }

                        }
                    }

                    if (mChats.size() > 0)
                        if (!mChats.get(mChats.size() - 1).equals(Singleton.auth.getCurrentUser().getEmail()) && !msgSent) {
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "CHANNEL_ID")
                                    .setSmallIcon(R.drawable.logo1)
                                    .setContentTitle(mChats.get(mChats.size() - 1))
                                    .setContentText(mMsg.get(mMsg.size() - 1))
                                    .setStyle(new NotificationCompat.BigTextStyle().bigText(mMsg.get(mMsg.size() - 1)))
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setAutoCancel(true);

                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());
                            Log.d("TAG", "onDataChange: Before build");
                            notificationManager.notify(0, builder.build());
                        }

                    msgSent = false;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


            try {
                DatabaseReference orderRef = mDatabase.child("Orders").child(auth.getCurrentUser().getUid());
                orderRef.orderByKey().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ordersList.clear();
                        ArrayList<FoodElement> foodElements = new ArrayList<>();
                        for (DataSnapshot order : dataSnapshot.getChildren()) {
                            OrderList orderList = new OrderList();
                            orderList.txnId = order.getKey();
                            orderList.txnTime = order.child("txnTime").getValue(String.class);
                            orderList.txnTime = orderList.txnTime.split("GMT")[0];
                            orderList.restaurantId = order.child("restaurantId").getValue(Integer.class);
                            orderList.amount = order.child("amount").getValue(String.class);

                            Log.d("TAG", "onDataChange: " + orderList.amount);
                            orderList.status = order.child("status").getValue(String.class);

                            for (DataSnapshot food : order.child("foodList").getChildren()) {
                                FoodElement foodElement = new FoodElement();
                                foodElement.setPhoto(food.child("photo").getValue(String.class));
                                foodElement.setName(food.child("name").getValue(String.class));
                                foodElement.setFoodType(food.child("foodType").getValue(String.class));
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
            } catch (Exception e) {
                orderDone = true;
                e.printStackTrace();
            }

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

                                switch (foodElement.getFoodType()) {
                                    case Offer.vs:
                                        foodElement.off = Singleton.offers.vegstarters;
                                        break;
                                    case Offer.nvs:
                                        foodElement.off = Singleton.offers.nonvegstarters;
                                        break;
                                    case Offer.vm:
                                        foodElement.off = Singleton.offers.vegmaincourse;
                                        break;
                                    case Offer.nvm:
                                        foodElement.off = Singleton.offers.nonvegmaincourse;
                                        break;
                                    case Offer.b:
                                        foodElement.off = Singleton.offers.breads;
                                        break;
                                    case Offer.r:
                                        foodElement.off = Singleton.offers.rice;
                                        break;
                                    case Offer.ro:
                                        foodElement.off = Singleton.offers.rolls;
                                        break;
                                    case Offer.s:
                                        foodElement.off = Singleton.offers.sweets;
                                        break;
                                    case Offer.bev:
                                        foodElement.off = Singleton.offers.beverages;
                                        break;
                                }
                                switch (foodElement.getFoodType()) {
                                    case Offer.vs:
                                        foodElement.offqtyval = Singleton.offers.vegstartersqty;
                                        break;
                                    case Offer.nvs:
                                        foodElement.offqtyval = Singleton.offers.nonvegstartersqty;
                                        break;
                                    case Offer.vm:
                                        foodElement.offqtyval = Singleton.offers.vegmaincourseqty;
                                        break;
                                    case Offer.nvm:
                                        foodElement.offqtyval = Singleton.offers.nonvegmaincourseqty;
                                        break;
                                    case Offer.b:
                                        foodElement.offqtyval = Singleton.offers.breadsqty;
                                        break;
                                    case Offer.r:
                                        foodElement.offqtyval = Singleton.offers.riceqty;
                                        break;
                                    case Offer.ro:
                                        foodElement.offqtyval = Singleton.offers.rollsqty;
                                        break;
                                    case Offer.s:
                                        foodElement.offqtyval = Singleton.offers.sweetsqty;
                                        break;
                                    case Offer.bev:
                                        foodElement.offqtyval = Singleton.offers.beveragesqty;
                                        break;
                                }
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
            if (loading.isShowing())
                loading.dismiss();
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
