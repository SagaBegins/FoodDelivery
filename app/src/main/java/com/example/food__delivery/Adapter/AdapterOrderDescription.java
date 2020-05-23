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
import com.example.food__delivery.Helper.OrderList;
import com.example.food__delivery.Helper.Restaurant;
import com.example.food__delivery.MainNavigationActivity.HomeFragment;
import com.example.food__delivery.R;

import java.util.ArrayList;

public class AdapterOrderDescription extends RecyclerView.Adapter<AdapterOrderDescription.ViewHolder> {
    private ArrayList<OrderList> orderlist;
    private ArrayList<Restaurant> restaurants = HomeFragment.restaurantList;
    private AllOrders parent;
    private Context context;

    public AdapterOrderDescription(ArrayList<OrderList> ordersList, AllOrders parent){
        this.orderlist = ordersList;
        this.parent = parent;
        this.context = parent.getContext();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_order_description, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.restaurantName.setText(restaurants.get(orderlist.get(position).restaurantId).restaurantName);
        Glide.with(context.getApplicationContext())
                .load(restaurants.get(orderlist.get(position).restaurantId).photo)
                .into(holder.restaurantImage);
        holder.txnid.setText(orderlist.get(position).txnId);
        holder.price.setText(orderlist.get(position).amount);
        final int pos = position;
        holder.viewDetails.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                parent.restaurantName.setText(restaurants.get(orderlist.get(pos).restaurantId).restaurantName);
                parent.txnDate.setText(orderlist.get(pos).txnTime);
                parent.txnId.setText(orderlist.get(pos).txnId);
                parent.amount.setText(orderlist.get(pos).amount);
                parent.status.setText(orderlist.get(pos).status);
                parent.fullorderdetails.setAdapter(new AdapterOrderContent(orderlist.get(pos).foodList, parent));
                parent.orderpopup.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView restaurantName, txnid, viewDetails, price;
        private ImageView restaurantImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantName = (TextView) itemView.findViewById(R.id.txtView_restaurantName);
            restaurantImage = (ImageView) itemView.findViewById(R.id.imgView_restaurant);
            txnid = (TextView) itemView.findViewById(R.id.txtView_txnId);
            viewDetails = (TextView) itemView.findViewById(R.id.txtView_details);
            price = (TextView) itemView.findViewById(R.id.priceoftransaction);
        }
    }
}
