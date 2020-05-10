package com.example.food__delivery.Fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food__delivery.Adapter.Adapter_Fav;
import com.example.food__delivery.Helper.FoodElements;
import com.example.food__delivery.R;
import com.example.food__delivery.Testing.DatabaseEntry;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavouriteFragment extends androidx.fragment.app.Fragment {

    RecyclerView recyclerView;
    DatabaseEntry databaseEntry;
    TextView quote;
    List<FoodElements> foodElementsList;
    RecyclerView.LayoutManager reLayoutManager;
    Adapter_Fav recyclerViewAdapter;
    int i=0;
    public FavouriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_favourite, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler10);
        databaseEntry = new DatabaseEntry(getActivity());
        foodElementsList  = new ArrayList<>();
        foodElementsList = databaseEntry.getDataFromDB("favour_table");
        quote = (TextView)view.findViewById(R.id.textView16);
        quote.setVisibility(View.GONE);
        recyclerViewAdapter = new Adapter_Fav(foodElementsList, getActivity().getApplicationContext());
        recyclerView.setAdapter(recyclerViewAdapter);
        reLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(reLayoutManager);
        if(foodElementsList.isEmpty()){
            quote.setVisibility(View.VISIBLE);
        }
        return view;
    }

}
