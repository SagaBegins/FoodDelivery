package com.example.fooddelivery.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.fooddelivery.Activities.MenuActivity;
import com.example.fooddelivery.Additional.DatabaseInstance;
import com.example.fooddelivery.Fragment.CartFragment;
import com.example.fooddelivery.HelperModal.FoodElement;
import com.example.fooddelivery.R;

import java.util.List;

public class Adapter_Cart extends RecyclerView.Adapter<Adapter_Cart.ViewHolder> {

    DatabaseInstance databaseInstance;
    private final List<FoodElement> foodElements;
    private final Context context;
    CartFragment parent;

    private int id;

    public Adapter_Cart(List<FoodElement> foodElements, CartFragment parent) {
        this.foodElements = foodElements;
        this.parent = parent;
        this.context = parent.getActivity();
        try {
            this.id = foodElements.get(0).getRate();
        } catch (Exception e) {
            e.printStackTrace();
            this.id = 0;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        databaseInstance = new DatabaseInstance(context);

        holder.food_name.setText(foodElements.get(position).getName().split("_")[0]);
        Glide.with(context.getApplicationContext())
                .load(foodElements.get(position).getPhoto()).transform(new CenterCrop(), new RoundedCorners(50))
                .into(holder.image);

        holder.price.setText(foodElements.get(position).getTotalPrice());
        //holder.saved.setText(foodElements.get(position).getSaved());

        if (databaseInstance.totalQty(foodElements.get(position).getRate()) < 40) {
            holder.add.setEnabled(true);
            databaseInstance.close();

            holder.add.setOnClickListener(view -> {
                int qty = foodElements.get(position).getQty() + 1;
                databaseInstance = new DatabaseInstance(context);

                foodElements.get(position).setQty(qty);
                Log.d("name", position + "");

                databaseInstance.updateInRow(holder.food_name.getText() + "_" + foodElements.get(position).getRate(), "cart_table", qty);
                holder.qty.setText("" + foodElements.get(position).getQty());
                try {
                    MenuActivity.tv.setText(String.valueOf(databaseInstance.totalQty(id)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                databaseInstance.close();
                notifyDataSetChanged();
                parent.calculateGrandTotal(id);
                updateTotal(id);
            });
        } else {
            holder.add.setEnabled(false);
            databaseInstance.close();
        }
        holder.sub.setOnClickListener(view -> {
            int qty = foodElements.get(position).getQty() - 1;
            if (qty > 0) {
                foodElements.get(position).setQty(qty);
                databaseInstance = new DatabaseInstance(context);
                databaseInstance.updateInRow(holder.food_name.getText() + "_" + foodElements.get(position).getRate(), "cart_table", qty);
                holder.qty.setText("" + foodElements.get(position).getQty());
                notifyDataSetChanged();
                parent.calculateGrandTotal(id);
                updateTotal(id);
                databaseInstance.close();
                try {
                    MenuActivity.tv.setText(String.valueOf(databaseInstance.totalQty(id)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        holder.delete.setOnClickListener(view -> {
            databaseInstance = new DatabaseInstance(context);
            databaseInstance.deleteARow(foodElements.get(position).getPhoto(), foodElements.get(position).getRate(), "cart_table");
            foodElements.remove(position);
            databaseInstance.close();
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, foodElements.size());
            parent.calculateGrandTotal(id);
            updateTotal(id);
            try {
                MenuActivity.tv.setText(String.valueOf(databaseInstance.totalQty(id)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        holder.qty.setText("" + foodElements.get(position).getQty());
    }

    @Override
    public int getItemCount() {
        return foodElements.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView food_name;
        private final TextView price;
        private final TextView qty;
        private final ImageView image;
        private final ImageButton add;
        private final ImageButton sub;
        private final Button delete;

        public ViewHolder(View itemView) {
            super(itemView);
            food_name = itemView.findViewById(R.id.nameofproduct);
            price = itemView.findViewById(R.id.priceofproduct);
            image = itemView.findViewById(R.id.imagecart);
            add = itemView.findViewById(R.id.add1);
            sub = itemView.findViewById(R.id.sub1);
            qty = itemView.findViewById(R.id.quantity);
            delete = itemView.findViewById(R.id.delete_from_cart);
        }
    }

    private void updateTotal(int restaurantId) {
        double tot = parent.calculateGrandTotal(restaurantId);
        String totalValue = String.format("%.2f", tot);
        SharedPreferences totalPrice;
        totalPrice = parent.getActivity().getSharedPreferences("PRICE_TOTAL", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = totalPrice.edit();
        editor.putString("total", totalValue);
        editor.apply();
    }
}
