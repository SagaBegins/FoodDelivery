package com.example.fooddelivery.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.fooddelivery.Fragment.AllOrders;
import com.example.fooddelivery.Fragment.InboxFragment;
import com.example.fooddelivery.HelperModal.FoodElement;
import com.example.fooddelivery.HelperModal.OrderList;
import com.example.fooddelivery.HelperModal.Restaurant;
import com.example.fooddelivery.R;
import com.example.fooddelivery.Singleton;
import com.facebook.FacebookSdk;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminScreen extends AppCompatActivity {
    public static ArrayList<OrderList> adminordersList;
    FirebaseAuth auth = Singleton.auth;
    DatabaseReference mDatabase;
    public static HashMap<String, String> useridEmail = new HashMap<>();
    public boolean orderDone;
    ViewPager pager;
    TabLayout tablayout;
    public ProgressDialog loading;
    public ViewPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FacebookSdk.setApplicationId("581033482823166");
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_admin_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("Admin Home");
        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);
        final DatabaseReference databaseReference = Singleton.db.getReference();
        pager = findViewById(R.id.adminviewpager);
        tablayout = findViewById(R.id.admintablayout);
        adminordersList = new ArrayList<>();

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        orderDone = false;
        mDatabase = Singleton.db.getReference();

        loading = new ProgressDialog(this);
        loading.setCancelable(false);
        loading.setTitle("Please be patient");
        loading.setMessage("Fetching Messages and Orders");
        loading.show();
        try {
            ThreadRunner loading = new ThreadRunner();
            if(adminordersList.isEmpty())
                loading.execute();

            DatabaseReference orderRef = mDatabase.child("Orders");
            orderRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    adminordersList.clear();
                    orderDone = false;
                    ArrayList<FoodElement> foodElements = new ArrayList<>();
                    for(DataSnapshot user: dataSnapshot.getChildren()) {
                        String userId = user.getKey();
                        for (DataSnapshot order :user.getChildren()) {
                            OrderList orderList = new OrderList();
                            orderList.userID = userId;
                            orderList.txnId = order.getKey();
                            //change this here later
                            orderList.userEmail = order.child("userEmail").getValue(String.class);
                            orderList.userEmail = "test@gmail.com";
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
                            adminordersList.add(0, orderList);
                        }
                    }
                    Log.d("TAG", "onDataChange: SHOULD BE SET TO TRUE");
                    orderDone = true;
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            DatabaseReference restaurantRef = mDatabase.child("restaurants");
            restaurantRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Restaurant Restaurant = new Restaurant();
                        Restaurant.photo = (child.child("Imagepath").getValue(String.class));
                        Restaurant.restaurantName = (child.child("Category").getValue(String.class));
                        Singleton.restaurantList.add(Restaurant);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            DatabaseReference usersRef = mDatabase.child("users");
            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot user:dataSnapshot.getChildren()){
                        String useremail = user.child("name").getValue(String.class);
                        useridEmail.put(user.getKey(), useremail);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch(Exception e){
            orderDone = true;
            e.printStackTrace();
        }


    }

    public class ThreadRunner extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] objects) {
            while (!orderDone) {}
            Log.d("TAG", "doInBackground: SHOULD BE DONE");
            return null;
        }
        @Override
        protected void onPostExecute(Object object) {

            pagerAdapter.addFrag(new InboxFragment(), "Inbox");
            pagerAdapter.addFrag(new AllOrders(), "All Orders");

            pager.setAdapter(pagerAdapter);
            tablayout.setupWithViewPager(pager);

            loading.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        auth.signOut();
        super.onDestroy();
    }

    public void exitByBackKey() {
        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("You will be logged out when you leave")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                        auth.signOut();
                        finishAffinity();
                        //close();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitByBackKey();
            //moveTaskToBack(false);
            auth.signOut();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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


