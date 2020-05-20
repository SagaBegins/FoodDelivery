package com.example.food__delivery.Activities;

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

import com.example.food__delivery.Fragment.Confirmation;
import com.example.food__delivery.Helper.FoodElement;
import com.example.food__delivery.Helper.OrderList;
import com.example.food__delivery.MainNavigationActivity.HomeFragment;
import com.example.food__delivery.R;
import com.example.food__delivery.Shipping;
import com.example.food__delivery.Testing.CustomViewPager;
import com.example.food__delivery.Testing.DatabaseEntry;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
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
        restaurantId = getIntent().getIntExtra("restaurantId", 0);
        setContentView(R.layout.activity_checkout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mViewPager = (CustomViewPager) findViewById(R.id.container1);
        setupViewPager(mViewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mViewPager.setPagingEnabled(false);
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
        adapter.addFrag(new Shipping(restaurantId), "Shipping");
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
                    intent = new Intent(this, ThankYouPage.class);
                    DatabaseEntry successOperations = new DatabaseEntry(this);
                    OrderList orderList = new OrderList();
                    orderList.foodList = (ArrayList<FoodElement>) successOperations.getDataFromDB("cart_table", restaurantId);
                    orderList.restaurantId = restaurantId;
                    String txnId = getIntent().getStringExtra("txnId");
                    orderList.txnTime = getIntent().getStringExtra("txnTime");
                    orderList.amount = getIntent().getStringExtra("amount");
                    orderList.status = "Will be delivered soon";
                    successOperations.deleteCart(restaurantId, "cart_table");
                    successOperations.close();

                    final ProgressDialog waitingToWrite = new ProgressDialog(this);
                    waitingToWrite.setMessage("Order Registering");
                    waitingToWrite.setCancelable(false);
                    waitingToWrite.show();

                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
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
            } /*else if (resultModel != null && resultModel.getError() != null) {
                Log.d(TAG, "Error response : " + resultModel.getError().getTransactionResponse());
            } else {
                Log.d(TAG, "Both objects are null!");
            }*/
        }
    }
}