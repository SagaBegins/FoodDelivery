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
import com.example.fooddelivery.Additional.DatabaseInstance;
import com.example.fooddelivery.HelperModal.FoodElement;
import com.example.fooddelivery.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteFragment extends androidx.fragment.app.Fragment {

    RecyclerView recyclerView;
    DatabaseInstance databaseInstance;
    TextView quote;
    RecyclerView.LayoutManager reLayoutManager;
    Adapter_Fav recyclerViewAdapter;

    List<FoodElement> foodElementsList;

    int i = 0;
    private final int restaurantId;

    public FavouriteFragment(int rate) {
        this.restaurantId = rate;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);
        recyclerView = view.findViewById(R.id.recycler10);

        databaseInstance = new DatabaseInstance(getActivity());
        foodElementsList = new ArrayList<>();
        foodElementsList = databaseInstance.getDataFromDB("favour_table", restaurantId);
        databaseInstance.close();

        quote = view.findViewById(R.id.emptyfav);
        quote.setVisibility(View.GONE);

        recyclerViewAdapter = new Adapter_Fav(foodElementsList, this);
        recyclerView.setAdapter(recyclerViewAdapter);

        reLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(reLayoutManager);

        if (foodElementsList.isEmpty()) {
            quote.setVisibility(View.VISIBLE);
        }
        return view;
    }

}
