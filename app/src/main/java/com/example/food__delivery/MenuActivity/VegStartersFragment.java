package com.example.food__delivery.MenuActivity;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food__delivery.Adapter.Adapter_Menu_Items;
import com.example.food__delivery.Helper.FoodElement;
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
public class VegStartersFragment extends androidx.fragment.app.Fragment {
    ProgressDialog loading;
    private static List<FoodElement> foodElements = new ArrayList<>();
    RecyclerView recyclerView;
    GetElements getElements = new GetElements();
    RecyclerView.LayoutManager reLayoutManager;
    Adapter_Menu_Items recyclerViewadapter;
    boolean done;
    public final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private int restaurantId;

    public VegStartersFragment(int restaurantId) {
        this.restaurantId = restaurantId;
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_vegstarters, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler1);
        recyclerView.setHasFixedSize(true);
        Log.d("Size", foodElements.size()+"");
        if(foodElements.size() == 0 || foodElements.get(0).getRate() != restaurantId) {
            foodElements.clear();
            AsyncTask.Status temp = getElements.getStatus();
                        getElements.cancel(temp == AsyncTask.Status.RUNNING);
            while(!getElements.isCancelled());
            getElements = new GetElements();
            getElements.execute();
        }
        else{
            recyclerViewadapter = new Adapter_Menu_Items(getActivity(), foodElements);
            recyclerView.setAdapter(recyclerViewadapter);
        }
        reLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1);
        recyclerView.setLayoutManager(reLayoutManager);
        return view;
    }
    private class GetElements extends AsyncTask<String, String, List<FoodElement>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            loading = new ProgressDialog(getActivity());
            loading.setMessage("Please wait...");
            loading.setCancelable(true);
            loading.show();

        }

        @Override
        protected List<FoodElement> doInBackground(String... arg0) {
            done = false;
            DatabaseReference menuRef = mDatabase.child("menu").child(restaurantId+"").child("vegstarters");
            Log.d("ondatachange", "Line 91, Yup is being called atleast");
            menuRef.addValueEventListener(valueChanger);
            while(!done);
            menuRef.removeEventListener(valueChanger);
            return foodElements;
        }

        @Override
        protected void onPostExecute(List<FoodElement> result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (loading.isShowing())
                loading.dismiss();
            recyclerViewadapter = new Adapter_Menu_Items(getActivity(), foodElements);
            recyclerView.setAdapter(recyclerViewadapter);
        }
    }
    private ValueEventListener valueChanger = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Log.d("ondatachange", "Line 110, Yup is being called atleast");
            for(DataSnapshot child:dataSnapshot.getChildren()) {
                FoodElement foodElement = new FoodElement();
                foodElement.setPhoto(child.child("image").getValue(String.class));
                foodElement.setName(child.child("name").getValue(String.class));
                foodElement.setFoodType(child.child("category").getValue(String.class));
                foodElement.setPrice(child.child("price").getValue(String.class));
                foodElement.setRate(restaurantId);
                foodElements.add(foodElement);
            }
            done = true;
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            done = true;
            Log.d("setting done","is done in database error");
        }
    };
}
