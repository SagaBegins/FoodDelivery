package com.example.food__delivery.Fragment;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food__delivery.Activities.Checkout;
import com.example.food__delivery.Adapter.Adapter_Cart;
import com.example.food__delivery.Helper.FoodElement;
import com.example.food__delivery.R;
import com.example.food__delivery.Testing.DatabaseEntry;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends androidx.fragment.app.Fragment {

    private static DatabaseEntry databaseEntrytotal;
    RecyclerView recyclerView;
    Adapter_Cart reAdapterFav;
    DatabaseEntry databaseEntry;
    List<FoodElement> foodElementsList;
    RecyclerView.LayoutManager layoutManager;
    int price=0;

    static TextView totalp;
    static TextView textget ;

    static RelativeLayout proceed;

    public CartFragment() {
        // Required empty public constructor
    }
    @SuppressLint("StaticFieldLeak")
    static View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler11);
        databaseEntry = new DatabaseEntry(getActivity());
        foodElementsList = new ArrayList<>();
        recyclerView.hasFixedSize();

        foodElementsList = databaseEntry.getDataFromDB("cart_table");
        databaseEntry.close();
        textget = (TextView)view.findViewById(R.id.textView15);
        proceed = (RelativeLayout)view.findViewById(R.id.relative_layout);
        reAdapterFav = new Adapter_Cart(foodElementsList,getActivity());
        totalp=(TextView)view.findViewById(R.id.textView14);
        recyclerView.setAdapter(reAdapterFav);
        int tot = calculateGrandTotal();
        String totalvalue = String.valueOf(tot);
        textget.setVisibility(View.GONE);
        SharedPreferences totalPrice;
        totalPrice = getActivity().getSharedPreferences("PRICE_TOTAL", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = totalPrice.edit();
        editor.putString("total", totalvalue);
        editor.commit();


        //Confirmation.priceTextview(totalvalue);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        if(!foodElementsList.isEmpty()){
            textget.setVisibility(View.GONE);
            proceed.setVisibility(View.VISIBLE);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Checkout.class);
                startActivity(intent);
            }
        });
        }else{
            textget.setVisibility(View.VISIBLE);
        }
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        databaseEntry.close();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        databaseEntry.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        databaseEntry.close();
    }

    @Override
    public void onResume() {
        super.onResume();
        //databaseEntry = ();
    }
    public static int calculateGrandTotal(){
        int total;
        Activity activity = (Activity)view.getContext();

        databaseEntrytotal = new DatabaseEntry(activity);
        total = databaseEntrytotal.total();
        databaseEntrytotal.close();

        totalp.setText("\u20B9 "+total);
        if(total == 0){
            textget.setVisibility(View.VISIBLE);
            proceed.setVisibility(View.GONE);
        }else{
            textget.setVisibility(View.GONE);
            proceed.setVisibility(View.VISIBLE);
        }
        return total;
    }
}
