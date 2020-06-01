package com.example.food__delivery.Activities;

import android.app.AlertDialog;
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

import com.example.food__delivery.AllOrders;
import com.example.food__delivery.Fragment.InboxFragment;
import com.example.food__delivery.Helper.FoodElement;
import com.example.food__delivery.Helper.OrderList;
import com.example.food__delivery.R;
import com.example.food__delivery.Additional.DatabaseInstance;
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
import java.util.List;

public class AdminScreen extends AppCompatActivity {
    public static ArrayList<OrderList> adminordersList;
    Fragment fragment;
    TextView nametext;
    FirebaseAuth auth;
    DatabaseReference mDatabase;
    String idf, namef;
    ImageView photo;
    FirebaseUser user;
    Button sign;
    boolean orderDone;
    DatabaseInstance databaseInstance;
    ViewPager pager;
    TabLayout tablayout;

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
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        pager = findViewById(R.id.adminviewpager);
        tablayout = findViewById(R.id.admintablayout);
        adminordersList = new ArrayList<>();
        
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFrag(new InboxFragment(), "Inbox");
        pagerAdapter.addFrag(new AllOrders(), "All Orders");

        pager.setAdapter(pagerAdapter);
        tablayout.setupWithViewPager(pager);

        //Add chat fragment or something
        /*fragment = new InboxFragment();
        setTitle("Admin Home");
        databaseEntry = new DatabaseEntry(AdminScreen.this);
        databaseEntry.createTable();
        databaseEntry.close();
        auth = FirebaseAuth.getInstance();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame_admin, fragment).commit();
        user = FirebaseAuth.getInstance().getCurrentUser();*/
        orderDone = false;
        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
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


        }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        auth.signOut();
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


