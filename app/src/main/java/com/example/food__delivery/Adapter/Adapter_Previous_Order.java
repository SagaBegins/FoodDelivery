package com.example.food__delivery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.food__delivery.Helper.PreviousData;
import com.example.food__delivery.R;
import com.example.food__delivery.Testing.DatabaseEntry;

import java.util.List;

public class Adapter_Previous_Order extends RecyclerView.Adapter<Adapter_Previous_Order.ViewHolder> {

    DatabaseEntry databaseEntry;
    List<PreviousData> dataop;
    Context context;

    public Adapter_Previous_Order(List<PreviousData> dataop, Context context) {
        this.dataop = dataop;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_previous_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.priceo.setText(dataop.get(position).getPrice());
        holder.nameo.setText(dataop.get(position).getName());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseEntry = new DatabaseEntry(context);
                databaseEntry.deleteARow(dataop.get(position).getUrl(), dataop.get(position).getRate(),"order_previous");
                dataop.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,dataop.size());
            }
        });
        Glide.with(context.getApplicationContext()).load(dataop.get(position).getUrl()).into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return dataop.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageButton delete;
        TextView nameo, priceo;
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            delete = (ImageButton)itemView.findViewById(R.id.deletebutton);
            imageView = (ImageView)itemView.findViewById(R.id.product_image);
            nameo = (TextView)itemView.findViewById(R.id.nameorderpre);
            priceo = (TextView)itemView.findViewById(R.id.priceorderpre);
        }
    }
}
