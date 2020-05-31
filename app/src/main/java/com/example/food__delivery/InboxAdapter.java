package com.example.food__delivery;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class InboxAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final List<String> maintitle;
//    private final Integer[] imgid;

    public InboxAdapter(Activity context, List<String> maintitle) {
        super(context, R.layout.inbox_listitem, maintitle);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.maintitle=maintitle;
//        this.imgid=imgid;

    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.inbox_listitem, null,true);

        TextView titleText = (TextView) rowView.findViewById(R.id.title);
//        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        titleText.setText(maintitle.get(position));
//        imageView.setImageResource(imgid[position]);

        return rowView;

    };
}
