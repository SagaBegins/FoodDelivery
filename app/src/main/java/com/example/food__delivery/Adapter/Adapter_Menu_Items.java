package com.example.food__delivery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food__delivery.Activities.AfterMain;
import com.example.food__delivery.Helper.FoodElement;
import com.example.food__delivery.R;
import com.example.food__delivery.Testing.DatabaseEntry;
import com.example.food__delivery.Testing.RoundedCornersTransformation;

import java.util.List;


public class Adapter_Menu_Items extends RecyclerView.Adapter<Adapter_Menu_Items.ViewHolder> {

    private List<FoodElement> foodElements;
    Context context;
    boolean isPressed = false;
    int rate = 0;
    DatabaseEntry databaseEntry;

        public Adapter_Menu_Items(Context context, List<FoodElement> foodElements) {
        this.foodElements = foodElements;
        this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_menu, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            databaseEntry = new DatabaseEntry(context);
            holder.food_name.setText(foodElements.get(position).getFoodType());
            Glide.with(context.getApplicationContext())
                    .load(foodElements.get(position).getPhoto())
                    .into(holder.image);
            holder.price.setText(foodElements.get(position).getPrice());
            if(databaseEntry.totalQty()<40) {
                holder.add.setEnabled(true);
                holder.add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        databaseEntry = new DatabaseEntry(context);
                        rate = foodElements.get(position).getRate();
                        databaseEntry.insertIntoCart(foodElements.get(position).getFoodType(), foodElements.get(position).getPhoto(), foodElements.get(position).getPrice(), rate, 1);
                        Toast.makeText(context, "Food Added to Cart.", Toast.LENGTH_SHORT);
                        AfterMain.tv.setText(String.valueOf(databaseEntry.totalQty()));
                    }
                });
            }else{
                holder.add.setEnabled(false);

            }
            holder.fav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if(!isPressed) {
                        holder.fav.setImageResource(R.drawable.ic_favorite_black_24dp);
                        databaseEntry = new DatabaseEntry(context);
                        databaseEntry.insertIntoFav(foodElements.get(position).getFoodType(), foodElements.get(position).getPhoto(), foodElements.get(position).getPrice(), rate, 1);
                        Toast.makeText(context, "Food Added to Favourites.", Toast.LENGTH_SHORT);
                    }else if(isPressed){
                        databaseEntry = new DatabaseEntry(context);
                        databaseEntry.deleteARow(foodElements.get(position).getPhoto(), "favour_table");
                        holder.fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    }
                    isPressed = !isPressed;
                }
            });
        }
        @Override
        public int getItemCount() {
        return foodElements.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView food_name, price;
            private ImageView image;
            private ImageButton add, fav;

            public ViewHolder(View view) {
                super(view);
                food_name = (TextView) view.findViewById(R.id.menuItemName);
                image = (ImageView) view.findViewById(R.id.dishimage);
                price = (TextView)view.findViewById(R.id.priceofproduct);
                add = (ImageButton)view.findViewById(R.id.imageButton3);
                fav = (ImageButton)view.findViewById(R.id.imageButton4);
            }
        }
    }
