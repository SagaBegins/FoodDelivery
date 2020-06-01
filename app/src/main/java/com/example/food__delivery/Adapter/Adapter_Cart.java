package com.example.food__delivery.Adapter;

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
import com.example.food__delivery.Fragment.CartFragment;
import com.example.food__delivery.Additional.DatabaseInstance;
import com.example.food__delivery.Activities.MenuActivity;
import com.example.food__delivery.HelperModal.FoodElement;
import com.example.food__delivery.R;

import java.util.List;

public class Adapter_Cart  extends RecyclerView.Adapter<Adapter_Cart.ViewHolder> {

    private List<FoodElement> foodElements;
    CartFragment parent;
    private Context context;
    DatabaseInstance databaseInstance;
    private int id;
    public Adapter_Cart(List<FoodElement> foodElements, CartFragment parent) {
        this.foodElements = foodElements;
        this.parent = parent;
        this.context = parent.getActivity();
        try {
            this.id = foodElements.get(0).getRate();
        }catch (Exception e){
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
        holder.food_name.setText(foodElements.get(position).getName().split("_")[0]);
        databaseInstance = new DatabaseInstance(context);
        Glide.with(context.getApplicationContext())
                .load(foodElements.get(position).getPhoto()).transform(new CenterCrop(), new RoundedCorners(50))
                .into(holder.image);
        holder.price.setText(foodElements.get(position).getTotalPrice());
        if(databaseInstance.totalQty(foodElements.get(position).getRate())<40){
            holder.add.setEnabled(true);
            databaseInstance.close();
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = foodElements.get(position).getQty()+1;
                foodElements.get(position).setQty(qty);
                databaseInstance = new DatabaseInstance(context);
                Log.d("name",position+"");
                databaseInstance.updateInRow(holder.food_name.getText()+"_"+foodElements.get(position).getRate(),"cart_table",qty);
                holder.qty.setText(""+foodElements.get(position).getQty());
                try {
                    MenuActivity.tv.setText(String.valueOf(databaseInstance.totalQty(id)));
                }catch(Exception e){
                    e.printStackTrace();
                }
                databaseInstance.close();
                notifyDataSetChanged();
                parent.calculateGrandTotal(id);
                updateTotal(id);
            }
        });
        }else{
            holder.add.setEnabled(false);
            databaseInstance.close();
        }
        holder.sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = foodElements.get(position).getQty()-1;
                if(qty>0) {
                    foodElements.get(position).setQty(qty);
                    databaseInstance = new DatabaseInstance(context);
                    databaseInstance.updateInRow(holder.food_name.getText()+"_"+foodElements.get(position).getRate(), "cart_table", qty);
                    holder.qty.setText("" + foodElements.get(position).getQty());
                    notifyDataSetChanged();
                    parent.calculateGrandTotal(id);
                    updateTotal(id);
                    databaseInstance.close();
                    try {
                        MenuActivity.tv.setText(String.valueOf(databaseInstance.totalQty(id)));
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseInstance = new DatabaseInstance(context);
                databaseInstance.deleteARow(foodElements.get(position).getPhoto(),foodElements.get(position).getRate(),"cart_table");
                foodElements.remove(position);
                databaseInstance.close();
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,foodElements.size());
                parent.calculateGrandTotal(id);
                updateTotal(id);
                try {
                    MenuActivity.tv.setText(String.valueOf(databaseInstance.totalQty(id)));
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });
        holder.qty.setText(""+foodElements.get(position).getQty());
    }
    @Override
    public int getItemCount() {
        return foodElements.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView food_name, price, qty;
        private ImageView image;
        private ImageButton add, sub;
        private Button delete;

        public ViewHolder(View itemView) {
            super(itemView);
            food_name = (TextView) itemView.findViewById(R.id.nameofproduct);
            price = (TextView)itemView.findViewById(R.id.priceofproduct);
            image = (ImageView) itemView.findViewById(R.id.imagecart);
            add = (ImageButton)itemView.findViewById(R.id.add1);
            sub = (ImageButton)itemView.findViewById(R.id.sub1);
            qty = (TextView)itemView.findViewById(R.id.quantity);
            delete = (Button)itemView.findViewById(R.id.delete_from_cart);
        }
    }

    private void updateTotal(int restaurantId){
        double tot = parent.calculateGrandTotal(restaurantId);
        String totalvalue = String.format("%.2f",tot);
        SharedPreferences totalPrice;
        totalPrice = parent.getActivity().getSharedPreferences("PRICE_TOTAL", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = totalPrice.edit();
        editor.putString("total", totalvalue);
        editor.apply();
    }
}
