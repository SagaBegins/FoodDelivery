package com.example.food__delivery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.example.food__delivery.Activities.MainScreen;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;


public class ChatAdapter extends ArrayAdapter<ChatModel> {
    FirebaseAuth auth;
    static int dp = 300;

    public ChatAdapter(@NonNull Context context, ArrayList<ChatModel> mChatData) {
        super(context, 0, mChatData);
        auth = FirebaseAuth.getInstance();
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
        CardView msgCard = (CardView) convertView.findViewById(R.id.msg_card);
        TextView tvName = (TextView) convertView.findViewById(R.id.emailid);
        TextView tvHome = (TextView) convertView.findViewById(R.id.msg);

        if(cm.getFrom().equals(auth.getCurrentUser().getEmail())){
            msgCard.setCardBackgroundColor(Color.GREEN);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.RIGHT;
           tvName.setLayoutParams(params);

           LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            p.width = (int) (dp * getContext().getResources().getDisplayMetrics().density);
            p.gravity = Gravity.RIGHT;
            msgCard.setLayoutParams(p);
        }else{
            msgCard.setCardBackgroundColor(Color.BLUE);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.LEFT;
            tvName.setLayoutParams(params);

            LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            p.width = (int) (dp * getContext().getResources().getDisplayMetrics().density);
            p.gravity = Gravity.LEFT;
            msgCard.setLayoutParams(p);
        }
        // Populate the data into the template view using the data object
        tvName.setText(cm.getFrom());
        tvHome.setText(cm.getMsg());
        // Return the completed view to render on screen
        return convertView;
    }
}
