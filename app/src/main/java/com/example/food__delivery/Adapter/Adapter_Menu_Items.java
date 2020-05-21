package com.example.food__delivery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.transition.Visibility;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.food__delivery.Activities.AfterMain;
import com.example.food__delivery.Helper.FoodElement;
import com.example.food__delivery.R;
import com.example.food__delivery.Testing.DatabaseEntry;


import java.util.List;


public class Adapter_Menu_Items extends RecyclerView.Adapter<Adapter_Menu_Items.ViewHolder> {

    private List<FoodElement> foodElements;
    Context context;
    boolean isFav = false;
    int id;
    RelativeLayout card;

    DatabaseEntry databaseEntry;

        public Adapter_Menu_Items(Context context, List<FoodElement> foodElements) {
        this.foodElements = foodElements;
        this.context = context;
        try{
            this.id = foodElements.get(0).getRate();
        }catch (Exception e){
            e.printStackTrace();
        }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_menu, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            databaseEntry = new DatabaseEntry(context);
            holder.description.setText(foodElements.get(position).getDescription());
            holder.image.setClickable(true);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch(holder.description.getVisibility()){
                        case View.GONE:
                            holder.description.setVisibility(View.VISIBLE);
                            holder.food_name.setVisibility(View.GONE);
                            holder.bottom.setVisibility(View.GONE);
                            break;
                        case View.VISIBLE:
                            holder.description.setVisibility(View.GONE);
                            holder.food_name.setVisibility(View.VISIBLE);
                            holder.bottom.setVisibility(View.VISIBLE);
                    }
                }
            });

            holder.food_name.setText(foodElements.get(position).getName());
            Glide.with(context.getApplicationContext())
                    .load(foodElements.get(position).getPhoto())
                    .into(holder.image);
            holder.price.setText("₹"+foodElements.get(position).getPrice());
            databaseEntry = new DatabaseEntry(context);
            if(databaseEntry.isFav(foodElements.get(position).getName()+"_"+id)) {
                holder.fav.setImageResource(R.drawable.ic_favorite_black_24dp);
                isFav = true;
            }
            databaseEntry.close();
            if(databaseEntry.totalQty(foodElements.get(position).getRate())<40) {
                holder.add.setEnabled(true);
                databaseEntry.close();
                holder.add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        databaseEntry = new DatabaseEntry(context);
                        databaseEntry.insertIntoCart(foodElements.get(position).getName()+"_"+id, foodElements.get(position).getPhoto(), foodElements.get(position).getPrice(), id, 1);
                        //Toast.makeText(context, "Food Added to Cart.", Toast.LENGTH_SHORT).show();
                        databaseEntry.close();
                        AfterMain.tv.setText(String.valueOf(databaseEntry.totalQty(foodElements.get(position).getRate())));
                    }
                });
            }else{
                databaseEntry.close();
                holder.add.setEnabled(false);

            }
            holder.fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!isFav) {
                        holder.fav.setImageResource(R.drawable.ic_favorite_black_24dp);
                        databaseEntry = new DatabaseEntry(context);
                        databaseEntry.insertIntoFav(foodElements.get(position).getName()+"_"+id, foodElements.get(position).getPhoto(), foodElements.get(position).getPrice(), id, 1);
                        databaseEntry.close();
                        Toast.makeText(context, "Food Added to Favourites.", Toast.LENGTH_SHORT);
                    }else{
                        databaseEntry = new DatabaseEntry(context);
                        databaseEntry.deleteARow(foodElements.get(position).getPhoto(),foodElements.get(position).getRate(), "favour_table");
                        databaseEntry.close();
                        holder.fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    }
                    isFav = !isFav;
                }
            });
        }
        @Override
        public int getItemCount() {
        return foodElements.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView food_name, price, description;
            private ImageView image;
            private ImageButton add, fav;
            private RelativeLayout bottom;

            public ViewHolder(View view) {
                super(view);
                food_name = (TextView) view.findViewById(R.id.menuItemName);
                image = (ImageView) view.findViewById(R.id.dishimage);
                price = (TextView)view.findViewById(R.id.priceofproduct);
                add = (ImageButton)view.findViewById(R.id.imageButton3);
                fav = (ImageButton)view.findViewById(R.id.imageButton4);
                description = (TextView) view.findViewById(R.id.food_description);
                bottom = (RelativeLayout) view.findViewById(R.id.bottom_section);
            }
        }
    }
