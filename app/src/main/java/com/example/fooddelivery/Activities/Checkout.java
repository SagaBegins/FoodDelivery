package com.example.fooddelivery.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.fooddelivery.Additional.CustomViewPager;
import com.example.fooddelivery.Additional.DatabaseInstance;
import com.example.fooddelivery.Fragment.Confirmation;
import com.example.fooddelivery.Fragment.MainScreenFragment.HomeFragment;
import com.example.fooddelivery.Fragment.ShippingFragment;
import com.example.fooddelivery.HelperModal.FoodElement;
import com.example.fooddelivery.HelperModal.OrderList;
import com.example.fooddelivery.R;
import com.example.fooddelivery.Singleton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;

import java.util.ArrayList;
import java.util.List;

public class Checkout extends AppCompatActivity {

    private CustomViewPager mViewPager;
    private TabLayout tabLayout;
    private int restaurantId;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        restaurantId = getIntent().getIntExtra("restaurantId", 0);

        // Activity Setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mViewPager = findViewById(R.id.container1);
        mViewPager.setPagingEnabled(false);

        tabLayout = findViewById(R.id.tabs);
        setupViewPager(mViewPager);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#5CA67C"));

        LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new ShippingFragment(restaurantId), "ShippingFragment");
        adapter.addFrag(new Confirmation(restaurantId), "Confirmation");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("MainActivity", "request code " + requestCode + " resultcode " + resultCode);
        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {
            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE);

            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {

                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
                    //Success Transaction
                    Toast.makeText(this, "Transaction Success", Toast.LENGTH_LONG).show();

                    String txnId = getIntent().getStringExtra("txnId");
                    String uid = Singleton.auth.getCurrentUser().getUid();
                    DatabaseInstance successOperations = new DatabaseInstance(this);

                    intent = new Intent(this, ThankYouPage.class);

                    OrderList orderList = new OrderList();
                    orderList.userEmail = Singleton.auth.getCurrentUser().getEmail();
                    orderList.foodList = (ArrayList<FoodElement>) successOperations.getDataFromDB("cart_table", restaurantId);
                    orderList.restaurantId = restaurantId;
                    orderList.txnTime = getIntent().getStringExtra("txnTime");
                    orderList.amount = getIntent().getStringExtra("amount");
                    orderList.address = getIntent().getStringExtra("address");
                    orderList.status = "Pending";

                    successOperations.deleteCart(restaurantId, "cart_table");
                    successOperations.close();

                    // Process dialog
                    final ProgressDialog waitingToWrite = new ProgressDialog(this);
                    waitingToWrite.setMessage("Order Registering");
                    waitingToWrite.setCancelable(false);
                    waitingToWrite.show();

                    DatabaseReference currentOrder = HomeFragment.mDatabase.child("Orders").child(uid);
                    currentOrder.child(txnId).setValue(orderList).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            waitingToWrite.dismiss();
                            startActivity(intent);
                        }
                    });

                    Log.d("Succesful", "Line 109 Checkout");
                } else {
                    //Failure Transaction
                    Toast.makeText(this, "Transaction Failed", Toast.LENGTH_LONG).show();
                    Log.d("Failure", "Line 112 Checkout");
                }

                // Response from Payumoney
                String payuResponse = transactionResponse.getPayuResponse();

                // Response from SURl and FURL
                String merchantResponse = transactionResponse.getTransactionDetails();
            }
        }
    }
}