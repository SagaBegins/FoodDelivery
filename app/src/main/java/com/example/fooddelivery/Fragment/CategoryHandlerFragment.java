package com.example.fooddelivery.Fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.Activities.MenuActivity;
import com.example.fooddelivery.Adapter.Adapter_Menu_Items;
import com.example.fooddelivery.HelperModal.FoodElement;
import com.example.fooddelivery.R;
import com.example.fooddelivery.Singleton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Character.compare;

/**
 * A simple {@link Fragment} subclass.
 */
public class CategoryHandlerFragment extends androidx.fragment.app.Fragment {

    private final int EDIT_DISTANCE = 2;
    ProgressDialog loading;
    List<FoodElement> foodElements = new ArrayList<>();
    RecyclerView recyclerView;

    RecyclerView.LayoutManager reLayoutManager;
    Adapter_Menu_Items recyclerViewadapter;
    public ArrayList<FoodElement> filteredFoodElements = new ArrayList<>();
    boolean done;
    public final DatabaseReference mDatabase = Singleton.db.getReference();
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
                        foodsearch.bringToFront();
                        foodsearch.requestFocus();
                        foodsearchparent.setVisibility(View.VISIBLE);
                        //requestFocus(foodsearchparent);
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
                }else{
                    recyclerViewadapter = new Adapter_Menu_Items(getActivity(), foodElements);
                }

                recyclerView.setAdapter(recyclerViewadapter);
            }
        });

        reLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1);
        recyclerView.setLayoutManager(reLayoutManager);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        filteredFoodElements = filterFood((ArrayList<FoodElement>) foodElements, foodsearch.getText().toString().toLowerCase());
        recyclerViewadapter = new Adapter_Menu_Items(getActivity(), filteredFoodElements);
        recyclerView.setAdapter(recyclerViewadapter);
    }

    private ArrayList<FoodElement> filterFood(ArrayList<FoodElement> foodElements, String filter){
        ArrayList<FoodElement> filtered = new ArrayList<>();
        if(filter == "")
            return foodElements;

        for(FoodElement food:foodElements){
            String lc = food.getName().toLowerCase();

            for(int i = 0; i < lc.length() - filter.length() + 1; i++){
                String temp = lc.substring(i, i + filter.length());
                Log.d("TAG", editDistance( temp, filter) +" "+filter+" "+temp);
                if(lc.contains(filter) || editDistance(temp, filter) <= EDIT_DISTANCE){
                    filtered.add(food);
                    break;
                }
            }
        }
        return filtered;
    }

    private int editDistance(String left, String right) {
        int row_len = left.length() + 1;
        int col_len = right.length() + 1;

        int[][] editDistances = new int[row_len][col_len];

        for(int i = 0; i < row_len; i++){
            for(int j = 0; j < col_len; j++){
                if(i == 0) {
                    editDistances[i][j] = j;
                }
                else if( j == 0 ){
                    editDistances[i][j] = i;
                }else if (compare(left.charAt(i-1), right.charAt(j-1)) == 0){
                    editDistances[i][j] = java.lang.Math.min(editDistances[i-1][j], java.lang.Math.min(editDistances[i][j-1], editDistances[i-1][j-1]));
                }else {
                    editDistances[i][j] = 1 + java.lang.Math.min(editDistances[i-1][j], java.lang.Math.min(editDistances[i][j-1], editDistances[i-1][j-1]));
                }
            }
        }
        return editDistances[row_len-1][col_len-1];
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }
}
