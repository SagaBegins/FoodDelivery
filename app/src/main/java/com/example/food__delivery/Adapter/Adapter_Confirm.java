package com.example.food__delivery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food__delivery.Helper.FoodElement;
import com.example.food__delivery.R;

import java.util.List;


public class Adapter_Confirm extends RecyclerView.Adapter<Adapter_Confirm.ViewHolder> {

    private List<FoodElement> foodElements;
    Context context;

    public Adapter_Confirm(List<FoodElement> foodElements, Context context) {
        this.foodElements = foodElements;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.confirm_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.food_name.setText(foodElements.get(position).getName());
        Glide.with(context.getApplicationContext()).load(foodElements.get(position).getPhoto())
                .into(holder.image);
        holder.price.setText(foodElements.get(position).getPrice());
        holder.qty.setText(""+foodElements.get(position).getQty());
    }

    @Override
    public int getItemCount() {
        return foodElements.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView food_name, price, qty;
        private ImageView image;
        public ViewHolder(View itemView) {
            super(itemView);
            food_name = (TextView) itemView.findViewById(R.id.textView19);
            price = (TextView)itemView.findViewById(R.id.textView21);
            image = (ImageView) itemView.findViewById(R.id.imageconfirm);
            qty=(TextView) itemView.findViewById(R.id.textView26);
        }
    }
}
