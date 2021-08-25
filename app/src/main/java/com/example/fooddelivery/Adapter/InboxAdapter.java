package com.example.fooddelivery.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fooddelivery.R;

import java.util.List;

public class InboxAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final List<String> maintitle;

    public InboxAdapter(Activity context, List<String> maintitle) {
        super(context, R.layout.inbox_listitem, maintitle);
        // TODO Auto-generated constructor stub

        this.context = context;
        this.maintitle = maintitle;
    }
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.inbox_listitem, null, true);

        TextView titleText = rowView.findViewById(R.id.title);
        titleText.setText(maintitle.get(position));

        return rowView;

    }

}
