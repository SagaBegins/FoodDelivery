package com.example.fooddelivery.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.fooddelivery.Activities.MenuActivity;
import com.example.fooddelivery.HelperModal.Restaurant;
import com.example.fooddelivery.R;

import java.util.List;

public class Adapter_Menu extends RecyclerView.Adapter<Adapter_Menu.ViewHolder> {

    private final List<Restaurant> restaurantList;
    Context context;

    public Adapter_Menu(Context context, List<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.food_name.setText(restaurantList.get(position).restaurantName);
        Glide.with(context.getApplicationContext())
                .load(restaurantList.get(position).photo).transform(new CenterCrop())//, new RoundedCorners(50))
                .into(holder.image);

        holder.cardView.setOnClickListener(view -> {
            Context context = view.getContext();
            Intent intent = new Intent(context, MenuActivity.class);
            intent.putExtra("restaurantId", position);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView food_name;
        private final ImageView image;
        private final CardView cardView;

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public ViewHolder(View view) {
            super(view);
            view.setClipToOutline(true);
            food_name = view.findViewById(R.id.restaurantname);
            image = view.findViewById(R.id.restaurantimage);
            cardView = view.findViewById(R.id.card);
        }
    }
}
