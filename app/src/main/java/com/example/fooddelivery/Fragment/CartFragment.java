package com.example.fooddelivery.Fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.Activities.Checkout;
import com.example.fooddelivery.Adapter.Adapter_Cart;
import com.example.fooddelivery.Additional.DatabaseInstance;
import com.example.fooddelivery.HelperModal.FoodElement;
import com.example.fooddelivery.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends androidx.fragment.app.Fragment {
    public Float saved;

    private DatabaseInstance databaseEntrytotal;

    RecyclerView recyclerView;
    Adapter_Cart reAdapterFav;
    DatabaseInstance databaseInstance;
    List<FoodElement> foodElementsList;
    RecyclerView.LayoutManager layoutManager;

    TextView totalp;
    TextView textget;

    RelativeLayout proceed;

    int price = 0;
    private final int restaurantId;

    public CartFragment(int rate) {
        this.restaurantId = rate;
        this.saved = 0.0f;
    }

    private View view;
    Context context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG", "onCreate: Runs this one");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_cart, container, false);
        Log.d("cartfrag", "This is running " + this.restaurantId);
        setUpView();
        return view;
    }

    @Override
    public void onDestroy() {
        Log.d("TAG", "onDestroy: CartFrag this is run too");
        super.onDestroy();
    }

    private void setUpView() {
        context = view.getContext();
        recyclerView = view.findViewById(R.id.recycler11);
        databaseInstance = new DatabaseInstance(getActivity());
        foodElementsList = new ArrayList<>();
        recyclerView.hasFixedSize();

        foodElementsList = databaseInstance.getDataFromDB("cart_table", restaurantId);
        databaseInstance.close();
        textget = view.findViewById(R.id.textView_cartEmpty);
        proceed = view.findViewById(R.id.relative_layout);
        reAdapterFav = new Adapter_Cart(foodElementsList, this);
        totalp = view.findViewById(R.id.textView_cartTotalPrice);
        recyclerView.setAdapter(reAdapterFav);
        double tot = calculateGrandTotal(restaurantId);
        String totalvalue = String.format("%.2f", tot);
        textget.setVisibility(View.GONE);
        SharedPreferences totalPrice;
        totalPrice = getActivity().getSharedPreferences("PRICE_TOTAL", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = totalPrice.edit();
        editor.putString("total", totalvalue);
        editor.apply();
        editor.putFloat("saved", saved);
        editor.apply();


        //Confirmation.priceTextview(totalvalue);
        layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        if (!foodElementsList.isEmpty()) {
            textget.setVisibility(View.GONE);
            proceed.setVisibility(View.VISIBLE);
            proceed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), Checkout.class);
                    intent.putExtra("restaurantId", restaurantId);
                    startActivity(intent);
                }
            });
        } else {
            textget.setVisibility(View.VISIBLE);
        }
    }


    public double calculateGrandTotal(int rate) {
        double total;
        Activity activity = (Activity) context;
        databaseEntrytotal = new DatabaseInstance(activity);
        total = databaseEntrytotal.total(rate);
        databaseEntrytotal.close();
        String tot = String.format("%.2f", total);
        totalp.setText("\u20B9 " + tot);
        if (total == 0) {
            textget.setVisibility(View.VISIBLE);
            proceed.setVisibility(View.GONE);
        } else {
            textget.setVisibility(View.GONE);
            proceed.setVisibility(View.VISIBLE);
        }
        return total;
    }

    public int getRestaurantId() {
        return this.restaurantId;
    }
}
