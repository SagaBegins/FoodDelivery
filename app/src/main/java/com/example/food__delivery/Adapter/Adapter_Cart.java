package com.example.food__delivery.Adapter;

import android.content.Context;
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
import com.example.food__delivery.Testing.DatabaseEntry;
import com.example.food__delivery.Activities.AfterMain;
import com.example.food__delivery.Fragment.CartFragment;
import com.example.food__delivery.Helper.FoodElement;
import com.example.food__delivery.R;

import java.util.List;

public class Adapter_Cart  extends RecyclerView.Adapter<Adapter_Cart.ViewHolder> {

    private List<FoodElement> foodElements;
    Context context;
    DatabaseEntry databaseEntry;
    private int id;
    public Adapter_Cart(List<FoodElement> foodElements, Context context) {
        this.foodElements = foodElements;
        this.context = context;
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
        databaseEntry = new DatabaseEntry(context);
        Glide.with(context.getApplicationContext())
                .load(foodElements.get(position).getPhoto()).transform(new CenterCrop(), new RoundedCorners(50))
                .into(holder.image);
        holder.price.setText(foodElements.get(position).getTotalPrice());
        if(databaseEntry.totalQty(foodElements.get(position).getRate())<40){
            holder.add.setEnabled(true);
            databaseEntry.close();
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = foodElements.get(position).getQty()+1;
                foodElements.get(position).setQty(qty);
                databaseEntry = new DatabaseEntry(context);
                Log.d("name",position+"");
                databaseEntry.updateInRow(holder.food_name.getText()+"_"+foodElements.get(position).getRate(),"cart_table",qty);
                holder.qty.setText(""+foodElements.get(position).getQty());
                AfterMain.tv.setText(String.valueOf(databaseEntry.totalQty(id)));
                databaseEntry.close();
                notifyDataSetChanged();
                CartFragment.calculateGrandTotal(id);
            }
        });
        }else{
            holder.add.setEnabled(false);
            databaseEntry.close();
        }
        holder.sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int qty = foodElements.get(position).getQty()-1;
                if(qty>0) {
                    foodElements.get(position).setQty(qty);
                    databaseEntry = new DatabaseEntry(context);
                    databaseEntry.updateInRow(holder.food_name.getText()+"_"+foodElements.get(position).getRate(), "cart_table", qty);
                    holder.qty.setText("" + foodElements.get(position).getQty());
                    notifyDataSetChanged();
                    CartFragment.calculateGrandTotal(id);
                    databaseEntry.close();
                    AfterMain.tv.setText(String.valueOf(databaseEntry.totalQty(id)));
                }
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseEntry = new DatabaseEntry(context);
                databaseEntry.deleteARow(foodElements.get(position).getPhoto(),foodElements.get(position).getRate(),"cart_table");
                foodElements.remove(position);
                databaseEntry.close();
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,foodElements.size());
                CartFragment.calculateGrandTotal(id);
                AfterMain.tv.setText(String.valueOf(databaseEntry.totalQty(id)));
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
}
