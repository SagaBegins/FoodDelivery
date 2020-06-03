package com.example.food__delivery.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food__delivery.Activities.AdminScreen;
import com.example.food__delivery.Fragment.AllOrders;
import com.example.food__delivery.Fragment.AdminOrder;
import com.example.food__delivery.HelperModal.OrderList;
import com.example.food__delivery.HelperModal.Restaurant;
import com.example.food__delivery.Fragment.MainScreenFragment.HomeFragment;
import com.example.food__delivery.R;
import com.example.food__delivery.Singleton;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterOrderDescription extends RecyclerView.Adapter<AdapterOrderDescription.ViewHolder> {
    private ArrayList<OrderList> orderlist;
    private ArrayList<Restaurant> restaurants = Singleton.restaurantList;
    private AllOrders parent;
    private Context context;
    private AdminOrder adminparent;
    private boolean isAdmin;
    public AdapterOrderDescription(ArrayList<OrderList> ordersList, AllOrders parent, boolean isAdmin){
        this.orderlist = ordersList;
        this.parent = parent;
        this.context = parent.getContext();
        this.isAdmin = isAdmin;
    }

    public AdapterOrderDescription(ArrayList<OrderList> ordersList, AdminOrder parent, boolean isAdmin){
        this.orderlist = ordersList;
        this.adminparent = parent;
        this.context = parent.getContext();
        this.isAdmin = isAdmin;
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
        holder.txnid.setText(orderlist.get(position).txnId);
        holder.price.setText(orderlist.get(position).amount);

        if(isAdmin){
            holder.restaurantImage.setVisibility(View.GONE);
            holder.userId.setText(orderlist.get(position).userID);
            holder.userEmail.setText(orderlist.get(position).userEmail);
            holder.userId.setVisibility(View.VISIBLE);
            holder.userEmail.setVisibility(View.VISIBLE);
        }else{
            Glide.with(context.getApplicationContext())
                    .load(restaurants.get(orderlist.get(position).restaurantId).photo)
                    .into(holder.restaurantImage);
        }
        final int pos = position;
        try{
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
                if(isAdmin && orderlist.get(pos).status.toLowerCase().equals("pending"))
                    setUpAcceptButton(pos);
            }
        });}
        catch(Exception e){
            holder.viewDetails.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    adminparent.restaurantName.setText(restaurants.get(orderlist.get(pos).restaurantId).restaurantName);
                    adminparent.txnDate.setText(orderlist.get(pos).txnTime);
                    adminparent.txnId.setText(orderlist.get(pos).txnId);
                    adminparent.amount.setText(orderlist.get(pos).amount);
                    adminparent.status.setText(orderlist.get(pos).status);
                    adminparent.fullorderdetails.setAdapter(new AdapterOrderContent(orderlist.get(pos).foodList, parent));
                    adminparent.orderpopup.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return orderlist.size();
    }

    private void setUpAcceptButton(final int pos){
        parent.accept.setVisibility(View.VISIBLE);
        parent.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> hm = new HashMap<>();
                hm.put("from", "testadmin@gmail.com");
                hm.put("to", orderlist.get(pos).userEmail);
                hm.put("msg", "Your food will be delivered soon. Its being prepared. :)");

                DatabaseReference dbr = Singleton.db.getReference();
                dbr.child("Chat").push().setValue(hm);
                dbr.child("Orders").child(orderlist.get(pos).userID).child(orderlist.get(pos).txnId).child("status").setValue("Accepted");
                parent.accept.setVisibility(View.GONE);
                parent.orderpopup.setVisibility(View.GONE);
                Toast.makeText(parent.getContext(), "User "+ orderlist.get(pos).userEmail+ " has been notified", Toast.LENGTH_SHORT);
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView restaurantName, txnid, viewDetails, price, userId, userEmail;
        private ImageView restaurantImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            restaurantName = (TextView) itemView.findViewById(R.id.txtView_restaurantName);
            restaurantImage = (ImageView) itemView.findViewById(R.id.imgView_restaurant);
            txnid = (TextView) itemView.findViewById(R.id.txtView_txnId);
            viewDetails = (TextView) itemView.findViewById(R.id.txtView_details);
            price = (TextView) itemView.findViewById(R.id.priceoftransaction);
            userId = itemView.findViewById(R.id.userid);
            userEmail = itemView.findViewById(R.id.useremail);
        }
    }
}
