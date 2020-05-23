package com.example.food__delivery.MenuActivity;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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
public class CategoryHandlerFragment extends androidx.fragment.app.Fragment {

    ProgressDialog loading;
    List<FoodElement> foodElements = new ArrayList<>();
    RecyclerView recyclerView;

    RecyclerView.LayoutManager reLayoutManager;
    Adapter_Menu_Items recyclerViewadapter;
    boolean done;
    public final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private int restaurantId;
    private FloatingActionButton fab;
    private TextInputEditText foodsearch;
    private TextInputLayout foodsearchparent;
    private String category;

    public CategoryHandlerFragment(int restaurantId, ArrayList<FoodElement> foodElements, String category) {
        this.restaurantId = restaurantId;
        this.foodElements = foodElements;
        this.category = category;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categoryhandler, container, false);
        fab = view.findViewById(R.id.floatingActionButton);
        foodsearch = view.findViewById(R.id.food_search);
        foodsearchparent = view.findViewById(R.id.food_search_parent);
        recyclerView = (RecyclerView) view.findViewById(R.id.categoryRecycler);
        recyclerView.setHasFixedSize(true);
        foodsearch.setHint(category+" Search");

        if(foodElements.size() == 0 || foodElements.get(0).getRate() != restaurantId) {
        }
        else{
            recyclerViewadapter = new Adapter_Menu_Items(getActivity(), foodElements, "");
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
                    recyclerViewadapter = new Adapter_Menu_Items(getActivity(), foodElements,s.toString());
                    recyclerView.setAdapter(recyclerViewadapter);
                }else{
                    recyclerViewadapter = new Adapter_Menu_Items(getActivity(), foodElements,"");
                    recyclerView.setAdapter(recyclerViewadapter);
                }
            }
        });

        reLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1);
        recyclerView.setLayoutManager(reLayoutManager);
        return view;
    }
}
