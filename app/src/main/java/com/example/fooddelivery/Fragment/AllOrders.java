package com.example.fooddelivery.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.fooddelivery.Activities.AdminScreen;
import com.example.fooddelivery.Adapter.AdapterOrderDescription;
import com.example.fooddelivery.Fragment.MainScreenFragment.HomeFragment;
import com.example.fooddelivery.HelperModal.OrderList;
import com.example.fooddelivery.R;
import com.example.fooddelivery.Singleton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllOrders extends androidx.fragment.app.Fragment {

    public ArrayList<OrderList> orderlist = HomeFragment.ordersList;
    public ArrayList<OrderList> allorderslist = AdminScreen.adminordersList;

    FirebaseAuth auth;
    FirebaseDatabase database;
    boolean isAdmin;

    public RecyclerView orderdetails;
    public CardView orderpopup;

    public TextView restaurantName;
    public TextView txnId;
    public TextView txnDate;
    public TextView status;
    public TextView amount;
    public RecyclerView fullorderdetails;
    public ImageView exit;
    public Button accept;

    private View view;

    AdapterOrderDescription adapter;
    ViewPager mViewPager;

    public AllOrders(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public AllOrders() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        auth = Singleton.auth;
        database = Singleton.db;

        view = inflater.inflate(R.layout.fragment_all_orders, container, false);

        orderdetails = view.findViewById(R.id.orderslist);
        orderpopup = view.findViewById(R.id.order_popup);
        restaurantName = view.findViewById(R.id.restaurant_Name);
        txnId = view.findViewById(R.id.txnId);
        txnDate = view.findViewById(R.id.order_date);
        status = view.findViewById(R.id.status);
        amount = view.findViewById(R.id.amount);
        fullorderdetails = view.findViewById(R.id.order_food_list);
        exit = view.findViewById(R.id.exit);
        accept = view.findViewById(R.id.accept);

        DatabaseReference users = database.getReference().child("users").child(auth.getCurrentUser().getUid());

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderpopup.setVisibility(View.GONE);
            }
        });

        users.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                try {
                    isAdmin = dataSnapshot.child("admin").getValue(Boolean.class);
                } catch (Exception e) {
                    isAdmin = false;
                }
                if (!isAdmin) {
                    adapter = new AdapterOrderDescription(orderlist, AllOrders.this, isAdmin);
                } else {
                    adapter = new AdapterOrderDescription(allorderslist, AllOrders.this, isAdmin);
                }
                orderdetails.setAdapter(adapter);
                orderdetails.setLayoutManager(new LinearLayoutManager(getContext()));
                fullorderdetails.setLayoutManager(new LinearLayoutManager(getContext()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }
}
