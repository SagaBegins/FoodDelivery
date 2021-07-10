package com.example.fooddelivery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.fooddelivery.Activities.MenuActivity;
import com.example.fooddelivery.Fragment.FavouriteFragment;
import com.example.fooddelivery.HelperModal.FoodElement;
import com.example.fooddelivery.R;
import com.example.fooddelivery.Additional.DatabaseInstance;

import java.util.List;


public class Adapter_Fav extends RecyclerView.Adapter<Adapter_Fav.ViewHolder>  {

    private List<FoodElement> foodElements;
    Context context;
    FavouriteFragment parent;
    DatabaseInstance databaseInstance;
    int id;

    public Adapter_Fav(List<FoodElement> foodElementsList, FavouriteFragment parent) {
        this.context = parent.getActivity();
        this.parent = parent;
        this.foodElements = foodElementsList;
        try {
            this.id = foodElements.get(0).getRate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_fav, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.food_name.setText(foodElements.get(position).getName().split("_")[0]);
        Glide.with(context.getApplicationContext())
                .load(foodElements.get(position).getPhoto()).transform(new CenterCrop(), new RoundedCorners(50))
                .into(holder.image);
        holder.price.setText(foodElements.get(position).getPrice());
        holder.addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseInstance = new DatabaseInstance(context);
                databaseInstance.insertIntoCart(foodElements.get(position).getName().split("_")[0]+"_"+id,foodElements.get(position).getPhoto(),foodElements.get(position).getPrice(),foodElements.get(position).getRate(), foodElements.get(position).getQty());
                MenuActivity.tv.setText(String.valueOf(databaseInstance.totalQty(id)));
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseInstance = new DatabaseInstance(context);
                databaseInstance.deleteARow(foodElements.get(position).getPhoto(), foodElements.get(position).getRate(),"favour_table");
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
        private Button addtocart, delete;


        public ViewHolder(View itemView) {
            super(itemView);
            food_name = (TextView) itemView.findViewById(R.id.nameproduct);
            price = (TextView)itemView.findViewById(R.id.priceproduct);
            image = (ImageView) itemView.findViewById(R.id.imagefav);
            addtocart = (Button) itemView.findViewById(R.id.addtocart);
            delete=(Button)itemView.findViewById(R.id.deletebutton);
        }
    }
}
