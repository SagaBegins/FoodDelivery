package com.example.food__delivery.Fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food__delivery.Activities.MenuActivity;
import com.example.food__delivery.Adapter.Adapter_Menu_Items;
import com.example.food__delivery.HelperModal.FoodElement;
import com.example.food__delivery.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryHandlerFragment extends androidx.fragment.app.Fragment {

    ProgressDialog loading;
    List<FoodElement> foodElements = new ArrayList<>();
    RecyclerView recyclerView;

    RecyclerView.LayoutManager reLayoutManager;
    Adapter_Menu_Items recyclerViewadapter;
    public ArrayList<FoodElement> filteredFoodElements = new ArrayList<>();
    boolean done;
    public final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private int restaurantId;
    private FloatingActionButton fab;
    private TextInputEditText foodsearch;
    private TextInputLayout foodsearchparent;
    private MenuActivity parent;

    public CategoryHandlerFragment(int restaurantId, ArrayList<FoodElement> foodElements, MenuActivity parent) {
        this.restaurantId = restaurantId;
        this.foodElements = foodElements;
        this.parent = parent;
        this.foodsearch = parent.foodsearch;
        this.foodsearchparent = parent.foodsearchparent;
        this.fab = parent.fab;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categoryhandler, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.categoryRecycler);
        recyclerView.setHasFixedSize(true);
        //foodsearch.setHint(category+" Search");

        if(foodElements.size() == 0 || foodElements.get(0).getRate() != restaurantId) {
        }
        else{
            recyclerViewadapter = new Adapter_Menu_Items(getActivity(), foodElements);
            recyclerView.setAdapter(recyclerViewadapter);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (foodsearchparent.getVisibility()) {
                    case View.GONE:
                        //foodsearch.bringToFront();
                        foodsearchparent.setVisibility(View.VISIBLE);
                        break;
                    case View.VISIBLE:
                        foodsearch.setText("");
                        foodsearchparent.setVisibility(View.GONE);
                }
            }
        });


        foodsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s != null) {
                    filteredFoodElements = filterFood((ArrayList<FoodElement>) foodElements, s.toString().toLowerCase());
                    recyclerViewadapter = new Adapter_Menu_Items(getActivity(), filteredFoodElements);
                    recyclerView.setAdapter(recyclerViewadapter);
                }else{
                    recyclerViewadapter = new Adapter_Menu_Items(getActivity(), foodElements);
                    recyclerView.setAdapter(recyclerViewadapter);
                }
            }
        });

        reLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1);
        recyclerView.setLayoutManager(reLayoutManager);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        filteredFoodElements = filterFood((ArrayList<FoodElement>) foodElements, foodsearch.getText().toString());
        recyclerViewadapter = new Adapter_Menu_Items(getActivity(), filteredFoodElements);
        recyclerView.setAdapter(recyclerViewadapter);
    }

    private ArrayList<FoodElement> filterFood(ArrayList<FoodElement> foodElements, String filter){
        ArrayList<FoodElement> filtered = new ArrayList<>();
        for(FoodElement food:foodElements){
            if(food.getName().toLowerCase().contains(filter))
                filtered.add(food);
        }
        return filtered;
    }
}
