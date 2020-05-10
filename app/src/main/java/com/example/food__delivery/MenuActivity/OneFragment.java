package com.example.food__delivery.MenuActivity;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.food__delivery.Adapter.Adapter_Menu_Items;
import com.example.food__delivery.Helper.FoodElements;
import com.example.food__delivery.R;
import com.example.food__delivery.Testing.HttpHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class OneFragment extends androidx.fragment.app.Fragment {
    ProgressDialog loading;
    List<FoodElements> foodElements1;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager reLayoutManager;
    Adapter_Menu_Items recyclerViewadapter;
    String URL = "https://api.myjson.com/bins/137l8v";

    public OneFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_one, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler1);
        recyclerView.setHasFixedSize(true);
        new GetElements().execute();
        reLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 2);
        recyclerView.setLayoutManager(reLayoutManager);
        return view;
    }
    private class GetElements extends AsyncTask<String, String, List<FoodElements>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            loading = new ProgressDialog(getActivity());
            loading.setMessage("Please wait...");
            loading.setCancelable(false);
            loading.show();

        }

        @Override
        protected List<FoodElements> doInBackground(String... arg0) {
            HttpHandler sh = new HttpHandler();

            String jsonStr = sh.makeServiceCall(URL);
            foodElements1 = new ArrayList<>();
            if (jsonStr != null) {
                try {
                    JSONObject json = new JSONObject(jsonStr);
                    JSONArray array = json.getJSONArray("vegstarter");

                    JSONObject jObject=null;

                    for (int i = 0; i < array.length(); i++) {
                        FoodElements foodElements = new FoodElements();
                        try {
                            jObject = array.getJSONObject(i);
                            foodElements.setPhoto(jObject.getString("ImagePath"));
                            foodElements.setFoodType(jObject.getString("Name"));
                            foodElements.setPrice(jObject.getString("Price"));
                            foodElements.setRate(jObject.getInt("Rate"));
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                        foodElements1.add(foodElements);
                    }
                } catch (JSONException e) {
                }
            }
            return foodElements1;
        }

        @Override
        protected void onPostExecute(List<FoodElements> result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (loading.isShowing())
                loading.dismiss();
            recyclerViewadapter = new Adapter_Menu_Items(getActivity(), foodElements1);
            recyclerView.setAdapter(recyclerViewadapter);
        }
    }
}
