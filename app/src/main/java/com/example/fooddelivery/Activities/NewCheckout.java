package com.example.fooddelivery.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.fooddelivery.Fragment.Confirmation;
import com.example.fooddelivery.HelperModal.FoodElement;
import com.example.fooddelivery.HelperModal.OrderList;
import com.example.fooddelivery.Fragment.MainScreenFragment.HomeFragment;
import com.example.fooddelivery.R;
import com.example.fooddelivery.Fragment.ShippingFragment;
import com.example.fooddelivery.Additional.CustomViewPager;
import com.example.fooddelivery.Additional.DatabaseInstance;
import com.example.fooddelivery.Singleton;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;
import com.payumoney.core.entity.TransactionResponse;
import com.payumoney.sdkui.ui.utils.PayUmoneyFlowManager;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class NewCheckout extends AppCompatActivity {

//    private CustomViewPager mViewPager;
//    private TabLayout tabLayout;
//    private int restaurantId;
//    Intent intent;
//
//    @NotNull
//    private static JSONObject getBaseRequest() throws JSONException {
//        return new JSONObject().put("apiVersion", 2).put("apiVersionMinor", 0);
//    }
//
//    private static JSONArray getAllowedCardNetworks() {
//        return new JSONArray()
//                .put("AMEX")
//                .put("DISCOVER")
//                .put("INTERAC")
//                .put("JCB")
//                .put("MASTERCARD")
//                .put("VISA");
//    }
//
//    //TODO get credentials from ajith
//    private static JSONObject getGatewayTokenizationSpecification() throws JSONException {
//        return new JSONObject() {{
//            put("type", "PAYMENT_GATEWAY");
//            put("parameters", new JSONObject() {{
//                put("gateway", "example");
//                put("gatewayMerchantId", "exampleGatewayMerchantId");
//            }});
//        }};
//    }
//
//    private static JSONObject getCardPaymentMethod() throws JSONException {
//        JSONObject cardPaymentMethod = getBaseCardPaymentMethod();
//        cardPaymentMethod.put("tokenizationSpecification", getGatewayTokenizationSpecification());
//
//        return cardPaymentMethod;
//    }
//
//    public static PaymentsClient createPaymentsClient(Activity activity) {
//        Wallet.WalletOptions walletOptions =
//                new Wallet.WalletOptions.Builder().setEnvironment(Constants.PAYMENTS_ENVIRONMENT).build();
//        return Wallet.getPaymentsClient(activity, walletOptions);
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.N)
//    public static Optional<JSONObject> getIsReadyToPayRequest() {
//        try {
//            JSONObject isReadyToPayRequest = getBaseRequest();
//            isReadyToPayRequest.put(
//                    "allowedPaymentMethods", new JSONArray().put(getBaseCardPaymentMethod()));
//
//            return Optional.of(isReadyToPayRequest);
//
//        } catch (JSONException e) {
//            return Optional.empty();
//        }
//    }
//
//    private static JSONArray getAllowedCardAuthMethods() {
//        return new JSONArray()
//                .put("PAN_ONLY")
//                .put("CRYPTOGRAM_3DS");
//    }
//
//    private static JSONObject getBaseCardPaymentMethod() throws JSONException {
//        JSONObject cardPaymentMethod = new JSONObject();
//        cardPaymentMethod.put("type", "CARD");
//
//        JSONObject parameters = new JSONObject();
//        parameters.put("allowedAuthMethods", getAllowedCardAuthMethods());
//        parameters.put("allowedCardNetworks", getAllowedCardNetworks());
//        // Optionally, you can add billing address/phone number associated with a CARD payment method.
//        parameters.put("billingAddressRequired", true);
//
//        JSONObject billingAddressParameters = new JSONObject();
//        billingAddressParameters.put("format", "FULL");
//
//        parameters.put("billingAddressParameters", billingAddressParameters);
//
//        cardPaymentMethod.put("parameters", parameters);
//
//        return cardPaymentMethod;
//    }
//
//
//    private void possiblyShowGooglePayButton() {
//
//        final Optional<JSONObject> isReadyToPayJson = PaymentsUtil.getIsReadyToPayRequest();
//        if (!isReadyToPayJson.isPresent()) {
//            return;
//        }
//
//        // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
//        // OnCompleteListener to be triggered when the result of the call is known.
//        IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(isReadyToPayJson.get().toString());
//        Task<Boolean> task = paymentsClient.isReadyToPay(request);
//        task.addOnCompleteListener(this,
//                new OnCompleteListener<Boolean>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Boolean> task) {
//                        if (task.isSuccessful()) {
//                            setGooglePayAvailable(task.getResult());
//                        } else {
//                            Log.w("isReadyToPay failed", task.getException());
//                        }
//                    }
//                });
//    }
//
//    private static JSONObject getTransactionInfo(String price) throws JSONException {
//        JSONObject transactionInfo = new JSONObject();
//        transactionInfo.put("totalPrice", price);
//        transactionInfo.put("totalPriceStatus", "FINAL");
//        transactionInfo.put("countryCode", Constants.COUNTRY_CODE);
//        transactionInfo.put("currencyCode", Constants.CURRENCY_CODE);
//        transactionInfo.put("checkoutOption", "COMPLETE_IMMEDIATE_PURCHASE");
//
//        return transactionInfo;
//    }
//
//    private static JSONObject getMerchantInfo() throws JSONException {
//        return new JSONObject().put("merchantName", "Example Merchant");
//    }
//
//
//    public static Optional<JSONObject> getPaymentDataRequest(long priceCents) {
//
//        final String price = PaymentsUtil.centsToString(priceCents);
//
//        try {
//            JSONObject paymentDataRequest = PaymentsUtil.getBaseRequest();
//            paymentDataRequest.put(
//                    "allowedPaymentMethods", new JSONArray().put(PaymentsUtil.getCardPaymentMethod()));
//            paymentDataRequest.put("transactionInfo", PaymentsUtil.getTransactionInfo(price));
//            paymentDataRequest.put("merchantInfo", PaymentsUtil.getMerchantInfo());
//
//      /* An optional shipping address requirement is a top-level property of the PaymentDataRequest
//      JSON object. */
//            paymentDataRequest.put("shippingAddressRequired", true);
//
//            JSONObject shippingAddressParameters = new JSONObject();
//            shippingAddressParameters.put("phoneNumberRequired", false);
//
//            JSONArray allowedCountryCodes = new JSONArray(Constants.SHIPPING_SUPPORTED_COUNTRIES);
//
//            shippingAddressParameters.put("allowedCountryCodes", allowedCountryCodes);
//            paymentDataRequest.put("shippingAddressParameters", shippingAddressParameters);
//            return Optional.of(paymentDataRequest);
//
//        } catch (JSONException e) {
//            return Optional.empty();
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        restaurantId = getIntent().getIntExtra("restaurantId", 0);
//        setContentView(R.layout.activity_checkout);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        toolbar.setTitleTextColor(0xFFFFFFFF);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        mViewPager = (CustomViewPager) findViewById(R.id.container1);
//        setupViewPager(mViewPager);
//        tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(mViewPager);
//        mViewPager.setPagingEnabled(false);
//        tabLayout.setTabTextColors(Color.parseColor("#FFFFFF"), Color.parseColor("#5CA67C"));
//        LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
//        for (int i = 0; i < tabStrip.getChildCount(); i++) {
//            tabStrip.getChildAt(i).setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    return true;
//                }
//            });
//        }
//    }
//
//    private void setupViewPager(ViewPager viewPager) {
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFrag(new ShippingFragment(restaurantId), "ShippingFragment");
//        adapter.addFrag(new Confirmation(restaurantId), "Confirmation");
//        viewPager.setAdapter(adapter);
//    }
//
//    class ViewPagerAdapter extends FragmentPagerAdapter {
//        private final List<Fragment> mFragmentList = new ArrayList<>();
//        private final List<String> mFragmentTitleList = new ArrayList<>();
//
//        public ViewPagerAdapter(FragmentManager manager) {
//            super(manager);
//        }
//
//        @Override
//        public Fragment getItem(int position) {
//            return mFragmentList.get(position);
//        }
//
//        @Override
//        public int getCount() {
//            return mFragmentList.size();
//        }
//
//        public void addFrag(Fragment fragment, String title) {
//            mFragmentList.add(fragment);
//            mFragmentTitleList.add(title);
//        }
//
//        @Override
//        public CharSequence getPageTitle(int position) {
//            return mFragmentTitleList.get(position);
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d("MainActivity", "request code " + requestCode + " resultcode " + resultCode);
//        if (requestCode == PayUmoneyFlowManager.REQUEST_CODE_PAYMENT && resultCode == RESULT_OK && data != null) {
//            TransactionResponse transactionResponse = data.getParcelableExtra(PayUmoneyFlowManager.INTENT_EXTRA_TRANSACTION_RESPONSE);
//
//            if (transactionResponse != null && transactionResponse.getPayuResponse() != null) {
//
//                if (transactionResponse.getTransactionStatus().equals(TransactionResponse.TransactionStatus.SUCCESSFUL)) {
//                    //Success Transaction
//                    Toast.makeText(this, "Transaction Success", Toast.LENGTH_LONG).show();
//                    intent = new Intent(this, ThankYouPage.class);
//                    DatabaseInstance successOperations = new DatabaseInstance(this);
//                    OrderList orderList = new OrderList();
//                    orderList.userEmail = Singleton.auth.getCurrentUser().getEmail();
//                    orderList.foodList = (ArrayList<FoodElement>) successOperations.getDataFromDB("cart_table", restaurantId);
//                    orderList.restaurantId = restaurantId;
//                    String txnId = getIntent().getStringExtra("txnId");
//                    orderList.txnTime = getIntent().getStringExtra("txnTime");
//                    orderList.amount = getIntent().getStringExtra("amount");
//                    orderList.address = getIntent().getStringExtra("address");
//                    orderList.status = "Pending";
//                    successOperations.deleteCart(restaurantId, "cart_table");
//                    successOperations.close();
//
//                    final ProgressDialog waitingToWrite = new ProgressDialog(this);
//                    waitingToWrite.setMessage("Order Registering");
//                    waitingToWrite.setCancelable(false);
//                    waitingToWrite.show();
//
//                    String uid = Singleton.auth.getCurrentUser().getUid();
//                    DatabaseReference currentOrder = HomeFragment.mDatabase.child("Orders").child(uid);
//                    currentOrder.child(txnId).setValue(orderList).addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            waitingToWrite.dismiss();
//                            startActivity(intent);
//                        }
//                    });
//
//                    Log.d("Succesful", "Line 109 Checkout");
//                } else {
//                    //Failure Transaction
//                    Toast.makeText(this, "Transaction Failed", Toast.LENGTH_LONG).show();
//                    Log.d("Failure", "Line 112 Checkout");
//                }
//
//                // Response from Payumoney
//                String payuResponse = transactionResponse.getPayuResponse();
//
//                // Response from SURl and FURL
//                String merchantResponse = transactionResponse.getTransactionDetails();
//            } /*else if (resultModel != null && resultModel.getError() != null) {
//                Log.d(TAG, "Error response : " + resultModel.getError().getTransactionResponse());
//            } else {
//                Log.d(TAG, "Both objects are null!");
//            }*/
//        }
//    }
}