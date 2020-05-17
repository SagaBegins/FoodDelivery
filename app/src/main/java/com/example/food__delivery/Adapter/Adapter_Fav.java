package com.example.food__delivery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food__delivery.Activities.AfterMain;
import com.example.food__delivery.Fragment.CartFragment;
import com.example.food__delivery.Helper.FoodElement;
import com.example.food__delivery.R;
import com.example.food__delivery.Testing.DatabaseEntry;

import java.util.List;


public class Adapter_Fav extends RecyclerView.Adapter<Adapter_Fav.ViewHolder>  {

    private List<FoodElement> foodElements;
    Context context;
    DatabaseEntry databaseEntry;

    public Adapter_Fav(List<FoodElement> foodElementsList, Context context) {
        this.context = context;
        this.foodElements = foodElementsList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_fav, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.food_name.setText(foodElements.get(position).getFoodType());
        Glide.with(context.getApplicationContext())
                .load(foodElements.get(position).getPhoto())
                .into(holder.image);
        holder.price.setText(foodElements.get(position).getPrice());
        holder.movetocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseEntry = new DatabaseEntry(context);
                databaseEntry.insertIntoCart(foodElements.get(position).getFoodType(),foodElements.get(position).getPhoto(),foodElements.get(position).getPrice(),foodElements.get(position).getRate(), foodElements.get(position).getQty());
                databaseEntry.deleteARow(foodElements.get(position).getPhoto(),foodElements.get(position).getRate(),"favour_table");
                foodElements.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,foodElements.size());
                int id = foodElements.get(position).getRate();
                CartFragment.calculateGrandTotal(id);
                AfterMain.tv.setText(String.valueOf(databaseEntry.totalQty(id)));
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseEntry = new DatabaseEntry(context);
                databaseEntry.deleteARow(foodElements.get(position).getPhoto(),foodElements.get(position).getRate(),"favour_table");
                foodElements.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,foodElements.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodElements.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView food_name, price;
        private ImageView image;
        private Button movetocart, delete;


        public ViewHolder(View itemView) {
            super(itemView);
            food_name = (TextView) itemView.findViewById(R.id.nameproduct);
            price = (TextView)itemView.findViewById(R.id.priceproduct);
            image = (ImageView) itemView.findViewById(R.id.imagefav);
            movetocart = (Button) itemView.findViewById(R.id.button12);
            delete=(Button)itemView.findViewById(R.id.deletebutton);
        }
    }
}
