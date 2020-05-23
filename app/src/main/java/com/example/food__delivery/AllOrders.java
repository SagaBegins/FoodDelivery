package com.example.food__delivery;

import android.app.Fragment;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.food__delivery.Adapter.AdapterOrderDescription;
import com.example.food__delivery.Helper.OrderList;
import com.example.food__delivery.MainNavigationActivity.HomeFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllOrders extends androidx.fragment.app.Fragment {

    private final ArrayList<OrderList> orderlist = HomeFragment.ordersList;
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

    public AllOrders() {
        // Required empty public constructor
    }

    ViewPager mViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_orders, container, false);
        orderdetails = (RecyclerView) view.findViewById(R.id.orderslist);
        orderpopup = (CardView) view.findViewById(R.id.order_popup);
        restaurantName = (TextView) view.findViewById(R.id.restaurant_Name);
        txnId = (TextView) view.findViewById(R.id.txnId);
        txnDate = (TextView) view.findViewById(R.id.order_date);
        status = (TextView) view.findViewById(R.id.status);
        amount = (TextView) view.findViewById(R.id.amount);
        fullorderdetails = (RecyclerView) view.findViewById(R.id.order_food_list);
        exit = (ImageView) view.findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderpopup.setVisibility(View.GONE);
            }
        });
        AdapterOrderDescription adapter = new AdapterOrderDescription(orderlist, this);
        orderdetails.setAdapter(adapter);
        orderdetails.setLayoutManager(new LinearLayoutManager(getContext()));
        fullorderdetails.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }
}
