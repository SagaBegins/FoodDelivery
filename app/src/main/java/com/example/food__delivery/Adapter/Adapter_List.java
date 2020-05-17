package com.example.food__delivery.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.food__delivery.Helper.PreviousData;
import com.example.food__delivery.R;

import java.util.ArrayList;


public class Adapter_List extends ArrayAdapter<PreviousData> {

    private ArrayList<PreviousData> dataSet;
    Context mContext;
    TextView txtName;
    TextView txtType;
    ImageView info;

    public Adapter_List(ArrayList<PreviousData> data, Context context) {
        super(context, R.layout.single_list_layout, data);
        this.dataSet = data;
        this.mContext=context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PreviousData dataModel = dataSet.get(position);
         LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.single_list_layout, parent, false);
            txtName = (TextView) convertView.findViewById(R.id.namelist);
            txtType = (TextView) convertView.findViewById(R.id.pricelist);
            info = (ImageView) convertView.findViewById(R.id.imglist);

        txtName.setText(dataModel.getName());
        txtType.setText(dataModel.getPrice());
        Glide.with(getContext()).load(dataModel.getUrl()).into(info);
        return convertView;
    }
}
