package com.example.fooddelivery.Fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.Adapter.Adapter_Fav;
import com.example.fooddelivery.HelperModal.FoodElement;
import com.example.fooddelivery.R;
import com.example.fooddelivery.Additional.DatabaseInstance;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteFragment extends androidx.fragment.app.Fragment {

    RecyclerView recyclerView;
    DatabaseInstance databaseInstance;
    TextView quote;
    List<FoodElement> foodElementsList;
    RecyclerView.LayoutManager reLayoutManager;
    Adapter_Fav recyclerViewAdapter;
    int i=0;
    private int restaurantId;
    public FavouriteFragment(int rate) {
        this.restaurantId = rate;
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_favourite, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler10);
        databaseInstance = new DatabaseInstance(getActivity());
        foodElementsList  = new ArrayList<>();
        foodElementsList = databaseInstance.getDataFromDB("favour_table", restaurantId);
        databaseInstance.close();
        quote = (TextView)view.findViewById(R.id.emptyfav);
        quote.setVisibility(View.GONE);
        recyclerViewAdapter = new Adapter_Fav(foodElementsList, this);
        recyclerView.setAdapter(recyclerViewAdapter);
        reLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(reLayoutManager);
        if(foodElementsList.isEmpty()){
            quote.setVisibility(View.VISIBLE);
        }
        return view;
    }

}
