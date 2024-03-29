package com.example.fooddelivery.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

import com.example.fooddelivery.Activities.ChatActivity;
import com.example.fooddelivery.Adapter.InboxAdapter;
import com.example.fooddelivery.HelperModal.ChatModel;
import com.example.fooddelivery.R;
import com.example.fooddelivery.Singleton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InboxFragment extends Fragment {

    public static boolean msgSent = false;
    final String adminemail = "admin@gmail.com";

    FirebaseUser mUser;
    DatabaseReference mRef;

    View view;
    ListView list;

    ArrayList<String> mChats;
    ArrayList<String> mMsg;
    List<Object> uqChatList;
    List<String> uq;
    List<String> maintitle = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inbox, container, false);
        maintitle.add("Admin");
        mChats = new ArrayList<>();
        uqChatList = new ArrayList<>();
        mMsg = new ArrayList<>();
        mUser = Singleton.auth.getCurrentUser();

        if (mUser.getEmail().equals(adminemail)) {
            mRef = Singleton.db.getReference().child("Chat");
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mChats.clear();
                    mMsg.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            ChatModel cm = d.getValue(ChatModel.class);
                            //Getting all the chats that user sent
                            if (cm.getFrom().equals(mUser.getEmail())) {
                                mChats.add(cm.getTo());
                                msgSent = true;
                                mMsg.add(cm.getMsg());
                            }
                            //Getting all the chats sent to the user
                            if (cm.getTo().equals(mUser.getEmail())) {
                                mChats.add(cm.getFrom());
                                msgSent = false;
                                mMsg.add(cm.getMsg());
                            }

                        }
                    }
                    if (mChats.size() > 0)
                        if (!mChats.get(mChats.size() - 1).equals(Singleton.auth.getCurrentUser().getEmail()) && !msgSent) {
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "CHANNEL_ID")
                                    .setSmallIcon(R.drawable.logo1)
                                    .setContentTitle(mChats.get(mChats.size() - 1))
                                    .setContentText("New message received")
                                    .setStyle(new NotificationCompat.BigTextStyle().bigText(mMsg.get(mMsg.size() - 1)))
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    .setAutoCancel(true);

                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getActivity());
                            Log.d("TAG", "onDataChange: Before build");
                            notificationManager.notify(0, builder.build());
                        }
                    msgSent = false;
                    chats();
                    displayInfo();

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            Log.d("myTag", mChats.toString());
        } else {

            Intent i = new Intent(getContext(), ChatActivity.class);
            startActivity(i);
            InboxAdapter adapter = new InboxAdapter(getActivity(), maintitle);
            list = view.findViewById(R.id.list);
            list.setAdapter(adapter);


            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub
                    if (position == 0) {
                        //code specific to first list items
                        Intent i = new Intent(getContext(), ChatActivity.class);
                        startActivity(i);
                    }

                }
            });
        }
        return view;
    }

    // mChats consists of all the user id that user has chatted with, this methods makes the id uniques (removes duplicates)
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void chats() {
        uqChatList = mChats.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private void displayInfo() {
        uq = new ArrayList<>();
        int minus = 0;
        if (uqChatList != null) {
            for (int i = 0; i < uqChatList.size(); i++) {
                String a = uqChatList.get(i).toString();
                if (a.equals(adminemail)) {
                } else {
                    uq.add(a);
                }

            }
        }

        InboxAdapter adapter = new InboxAdapter(getActivity(), uq);
        list = view.findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener((parent, view, position, id) -> {
            // TODO Auto-generated method stub

            int pos = position;
            String fromEmail = uq.get(pos);
            Log.d("From Email", fromEmail);
            Intent intent = new Intent(getContext(), ChatActivity.class);
            intent.putExtra("fromEmail", fromEmail);
            startActivity(intent);
        });

    }
}
