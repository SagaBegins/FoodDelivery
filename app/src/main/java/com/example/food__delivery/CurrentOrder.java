package com.example.food__delivery;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food__delivery.Adapter.Adapter_Order;
import com.example.food__delivery.Helper.OrderElements;
import com.example.food__delivery.Testing.DatabaseEntry;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CurrentOrder extends androidx.fragment.app.Fragment {

    static RecyclerView recyclerViewOrder;
    DatabaseEntry databaseEntry;
    RecyclerView.LayoutManager layoutManager;
    List<OrderElements> foodElementsList;
    Adapter_Order reAdapterOrder;
    static TextView order;

    public CurrentOrder() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_current_order, container, false);
        recyclerViewOrder = (RecyclerView)view.findViewById(R.id.recycler_order);
        databaseEntry = new DatabaseEntry(getActivity());
        foodElementsList = new ArrayList<OrderElements>();
        foodElementsList = databaseEntry.getDataFromOrder();
        Log.e("name", ""+foodElementsList.size());
        order = (TextView)view.findViewById(R.id.orderno);
            checkData(foodElementsList);

            reAdapterOrder = new Adapter_Order(foodElementsList,getActivity());
            recyclerViewOrder.setAdapter(reAdapterOrder);
            layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
            recyclerViewOrder.setLayoutManager(layoutManager);

        return view;
    }
    public static void checkData(List<OrderElements> foodElementsList){
        if(foodElementsList.isEmpty()){
            recyclerViewOrder.setVisibility(View.GONE);
            order.setVisibility(View.VISIBLE);
        }
    }
}