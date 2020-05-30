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

import java.util.ArrayList;


public class ChatAdapter extends ArrayAdapter<ChatModel> {

    public ChatAdapter(@NonNull Context context, ArrayList<ChatModel> mChatData) {
        super(context, 0, mChatData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ChatModel cm = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_listitem, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.emailid);
        TextView tvHome = (TextView) convertView.findViewById(R.id.msg);
        // Populate the data into the template view using the data object
        tvName.setText(cm.getFrom());
        tvHome.setText(cm.getMsg());
        // Return the completed view to render on screen
        return convertView;
    }
}
