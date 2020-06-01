package com.example.food__delivery.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food__delivery.AllOrders;
import com.example.food__delivery.Helper.FoodElement;
import com.example.food__delivery.R;

import java.util.ArrayList;

public class AdapterOrderContent extends RecyclerView.Adapter<AdapterOrderContent.ViewHolder> {
    private ArrayList<FoodElement> foodElements;
    private AllOrders parent;
    private Context context;
    private int restaurantId;

    public AdapterOrderContent(ArrayList<FoodElement> foodElements, AllOrders parent){
        this.foodElements = foodElements;
        this.parent = parent;
        this.context = parent.getContext();
        this.restaurantId = foodElements.get(0).getRate();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_order_details, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context.getApplicationContext())
                .load(foodElements.get(position).getPhoto())
                .into(holder.foodimage);
        Log.d("TAG", "onBindViewHolder: "+ foodElements.get(position).getPhoto());
        holder.food.setText(foodElements.get(position).getName().split("_")[0]);
        holder.quantity.setText(foodElements.get(position).getQty()+"");
        holder.price.setText(foodElements.get(position).getTotalPrice());
    }

    @Override
    public int getItemCount() {
        return foodElements.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView food, quantity, price;
        private ImageView foodimage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            food = itemView.findViewById(R.id.txtView_food);
            price = itemView.findViewById(R.id.txtView_price);
            quantity = itemView.findViewById(R.id.txtView_quantity);
            foodimage = itemView.findViewById(R.id.imgView_food);
        }
    }
}
