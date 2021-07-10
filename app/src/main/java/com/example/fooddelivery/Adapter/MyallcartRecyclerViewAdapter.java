package com.example.fooddelivery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fooddelivery.HelperModal.OrderList;
import com.example.fooddelivery.R;
import com.example.fooddelivery.Singleton;

import java.util.ArrayList;

public class MyallcartRecyclerViewAdapter extends RecyclerView.Adapter<MyallcartRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<OrderList> mValues;
    private Context context;
    private View view;

    public MyallcartRecyclerViewAdapter(ArrayList<OrderList> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_all_carts, parent, false);
        context = view.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position).restaurantId;

        holder.mIdView.setText(Singleton.restaurantList.get(holder.mItem).restaurantName);
        //Adapter_Cart ac = new Adapter_Cart(mValues.get(position).foodList, context);

       /* while(holder.rv.getChildCount() < mValues.get(position).foodList.size()){
            Log.d("l", holder.rv.getChildCount()+"");};
        /*FragmentManager fm = ((AppCompatActivity) context).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        Fragment cf = new CartFragment(holder.mItem);
        fragmentTransaction.replace(R.id.shop_fragment, cf).commit();*/
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    private void onChange(){
        super.notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public int mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.restaurant_Name);
        }

    }
}
