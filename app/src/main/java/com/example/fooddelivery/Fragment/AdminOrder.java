package com.example.fooddelivery.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.fooddelivery.Activities.AdminScreen;
import com.example.fooddelivery.Adapter.AdapterOrderDescription;
import com.example.fooddelivery.HelperModal.OrderList;
import com.example.fooddelivery.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminOrder extends androidx.fragment.app.Fragment {

    private final ArrayList<OrderList> orderlist = AdminScreen.adminordersList;
    public RecyclerView orderdetails;
    public CardView orderpopup;

    public TextView restaurantName;
    public TextView txnId;
    public TextView txnDate;
    public TextView status;
    public TextView amount;

    public RecyclerView fullorderdetails;
    public ImageView exit;
    private View view;

    ViewPager mViewPager;

    public AdminOrder() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderpopup.setVisibility(View.GONE);
            }
        });
        AdapterOrderDescription adapter = new AdapterOrderDescription(orderlist, this, true);
        orderdetails.setAdapter(adapter);
        orderdetails.setLayoutManager(new LinearLayoutManager(getContext()));
        fullorderdetails.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}
