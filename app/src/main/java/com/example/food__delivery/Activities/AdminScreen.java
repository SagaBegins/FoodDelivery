package com.example.food__delivery.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.food__delivery.Fragment.AllOrders;
import com.example.food__delivery.Fragment.InboxFragment;
import com.example.food__delivery.Fragment.MainScreenFragment.HomeFragment;
import com.example.food__delivery.HelperModal.FoodElement;
import com.example.food__delivery.HelperModal.OrderList;
import com.example.food__delivery.HelperModal.Restaurant;
import com.example.food__delivery.R;
import com.example.food__delivery.Additional.DatabaseInstance;
import com.example.food__delivery.Singleton;
import com.facebook.FacebookSdk;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AdminScreen extends AppCompatActivity {
    public static ArrayList<OrderList> adminordersList;
    FirebaseAuth auth = Singleton.auth;
    DatabaseReference mDatabase;
    public static HashMap<String, String> useridEmail = new HashMap<>();
    boolean orderDone;
    ViewPager pager;
    TabLayout tablayout;
    ProgressDialog loading;

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
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFrag(new InboxFragment(), "Inbox");
        pagerAdapter.addFrag(new AllOrders(), "All Orders");

        pager.setAdapter(pagerAdapter);
        tablayout.setupWithViewPager(pager);

        orderDone = false;
        mDatabase = Singleton.db.getReference();
        loading = new ProgressDialog(this);
        loading.setCancelable(false);
        loading.show();
        try {
            DatabaseReference orderRef = mDatabase.child("Orders");
            orderRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    adminordersList.clear();
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
                    loading.dismiss();
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


