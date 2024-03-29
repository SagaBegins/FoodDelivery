package com.example.fooddelivery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.fooddelivery.Activities.MenuActivity;
import com.example.fooddelivery.Additional.DatabaseInstance;
import com.example.fooddelivery.HelperModal.FoodElement;
import com.example.fooddelivery.R;

import java.util.List;


public class Adapter_Menu_Items extends RecyclerView.Adapter<Adapter_Menu_Items.ViewHolder> {

    private final List<FoodElement> foodElements;
    Context context;
    boolean isFav = false;
    int id;
    RelativeLayout card;
    DatabaseInstance databaseInstance;

    public Adapter_Menu_Items(Context context, List<FoodElement> foodElements) {
        this.foodElements = foodElements;
        this.context = context;

        try {
            this.id = foodElements.get(0).getRate();
        } catch (Exception e) {
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
        databaseInstance = new DatabaseInstance(context);
        holder.description.setText(foodElements.get(position).getDescription());
        holder.image.setClickable(true);
        holder.food_name.setText(foodElements.get(position).getName());
        holder.price.setText("₹" + foodElements.get(position).getPrice());
        holder.image.setOnClickListener(v -> {
            switch (holder.description.getVisibility()) {
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
        });

        Glide.with(context.getApplicationContext())
                .load(foodElements.get(position).getPhoto())
                .transform(new CenterCrop())
                .into(holder.image);

        databaseInstance = new DatabaseInstance(context);
        if (databaseInstance.isFav(foodElements.get(position).getName() + "_" + id)) {
            holder.fav.setImageResource(R.drawable.ic_favorite_black_24dp);
            isFav = true;
        }

        if (databaseInstance.totalQty(foodElements.get(position).getRate()) < 40) {
            holder.add.setEnabled(true);
            databaseInstance.close();
            holder.add.setOnClickListener(view -> {
                databaseInstance = new DatabaseInstance(context);
                databaseInstance.insertIntoCart(foodElements.get(position).getName() + "_" + id, foodElements.get(position).getPhoto(), foodElements.get(position).getPrice(), id, 1);
                //Toast.makeText(context, "Food Added to Cart.", Toast.LENGTH_SHORT).show();
                databaseInstance.close();
                MenuActivity.tv.setText(String.valueOf(databaseInstance.totalQty(foodElements.get(position).getRate())));
            });
        } else {
            databaseInstance.close();
            holder.add.setEnabled(false);
        }
        holder.fav.setOnClickListener(view -> {
            if (!isFav) {
                holder.fav.setImageResource(R.drawable.ic_favorite_black_24dp);
                databaseInstance = new DatabaseInstance(context);
                databaseInstance.insertIntoFav(foodElements.get(position).getName() + "_" + id, foodElements.get(position).getPhoto(), foodElements.get(position).getPrice(), id, 1);
                databaseInstance.close();
                Toast.makeText(context, "Food Added to Favourites.", Toast.LENGTH_SHORT);
            } else {
                databaseInstance = new DatabaseInstance(context);
                databaseInstance.deleteARow(foodElements.get(position).getPhoto(), foodElements.get(position).getRate(), "favour_table");
                databaseInstance.close();
                holder.fav.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            }
            isFav = !isFav;
        });
    }

    @Override
    public int getItemCount() {
        return foodElements.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView food_name;
        private final TextView price;
        private final TextView description;
        private final ImageView image;
        private final ImageButton add;
        private final ImageButton fav;
        private final RelativeLayout bottom;
        private final CardView card;

        public ViewHolder(View view) {
            super(view);
            food_name = view.findViewById(R.id.menuItemName);
            image = view.findViewById(R.id.dishimage);
            price = view.findViewById(R.id.priceofproduct);
            add = view.findViewById(R.id.addtocart);
            fav = view.findViewById(R.id.fav);
            description = view.findViewById(R.id.food_description);
            bottom = view.findViewById(R.id.bottom_section);
            card = view.findViewById(R.id.food_holder);
        }
    }
}
