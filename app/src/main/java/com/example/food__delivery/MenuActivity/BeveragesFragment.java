package com.example.food__delivery.MenuActivity;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food__delivery.Adapter.Adapter_Menu_Items;
import com.example.food__delivery.Helper.FoodElement;
import com.example.food__delivery.MainNavigationActivity.HomeFragment;
import com.example.food__delivery.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class BeveragesFragment extends  androidx.fragment.app.Fragment {

    ProgressDialog loading;
    List<FoodElement> foodElements;
    RecyclerView recyclerView;

    RecyclerView.LayoutManager reLayoutManager;
    Adapter_Menu_Items recyclerViewadapter;
    boolean done;
    public final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private int restaurantId;

    public BeveragesFragment(int restaurantId) {
        this.restaurantId = restaurantId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_beverages, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler9);
        recyclerView.setHasFixedSize(false);
        foodElements = HomeFragment.menuList.get(restaurantId).beverages;
        if(foodElements.size() == 0) {
        }
        else{
            recyclerViewadapter = new Adapter_Menu_Items(getActivity(), foodElements);
            recyclerView.setAdapter(recyclerViewadapter);
        }
        reLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1);
        recyclerView.setLayoutManager(reLayoutManager);
        return view;
    }
}
