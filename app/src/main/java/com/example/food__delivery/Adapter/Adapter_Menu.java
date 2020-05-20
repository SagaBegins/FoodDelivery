package com.example.food__delivery.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.DrawableUtils;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.DrawableTransformation;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableResource;
import com.bumptech.glide.request.RequestOptions;
import com.example.food__delivery.Activities.AfterMain;
import com.example.food__delivery.Helper.Restaurant;
import com.example.food__delivery.R;
import com.google.android.material.internal.CircularBorderDrawable;

import java.security.MessageDigest;
import java.util.List;

public class Adapter_Menu extends RecyclerView.Adapter<Adapter_Menu.ViewHolder> {

    private List<Restaurant> restaurantList;
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
                .load(restaurantList.get(position).photo).transform(new CenterCrop(), new RoundedCorners(50)).into(holder.image);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Context context = view.getContext();
                Intent intent = new Intent(context, AfterMain.class);
                intent.putExtra("restaurantId", position);
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return restaurantList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView food_name;
        private ImageView image;
        private CardView cardView;
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        public ViewHolder(View view) {
            super(view);
            view.setClipToOutline(true);
            food_name = (TextView) view.findViewById(R.id.textview27);
            image = (ImageView) view.findViewById(R.id.imageViewHero);
            cardView = (CardView)view.findViewById(R.id.card);
        }
    }
}
