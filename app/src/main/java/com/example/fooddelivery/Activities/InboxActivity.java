package com.example.fooddelivery.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

public class InboxActivity extends AppCompatActivity {

    ListView list;
    FirebaseUser mUser;
    DatabaseReference mRef;
    ArrayList<String> mChats;
    List<Object> uqChatList;
    TextView inb;
    List<String> uq;

    List<String> maintitle = new ArrayList<>();
    String adminemail = "admin@gmail.com";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        maintitle.add("Admin");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setTitle("Inbox");
        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);

        mUser = Singleton.auth.getCurrentUser();
        mChats = new ArrayList<>();
        uqChatList = new ArrayList<>();

        Log.d("Checking email", "onCreate: 62 " + adminemail + " " + mUser.getEmail());
        if (mUser.getEmail().equals(adminemail)) {
            mRef = Singleton.db.getReference("Chat");
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mChats.clear();
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot d : dataSnapshot.getChildren()) {
                            ChatModel cm = d.getValue(ChatModel.class);

                            //Getting all the chats that user sent
                            if (cm.getFrom().equals(mUser.getEmail())) {
                                mChats.add(cm.getTo());
                            }
                            //Getting all the chats sent to the user
                            if (cm.getTo().equals(mUser.getEmail())) {
                                mChats.add(cm.getFrom());
                            }
                        }
                    }
                    chats();
                    displayInfo();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });

            Log.d("myTag", mChats.toString());
        } else {

            Intent i = new Intent(InboxActivity.this, ChatActivity.class);
            startActivity(i);
            InboxAdapter adapter = new InboxAdapter(this, maintitle);
            list = findViewById(R.id.list);
            list.setAdapter(adapter);


            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub
                    if (position == 0) {
                        Intent i = new Intent(InboxActivity.this, ChatActivity.class);
                        startActivity(i);
                    }

                }
            });
        }

    }

    //mChats consists of all the user id that user has chatted with, this methods makes the id uniques (removes duplicates)
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

        InboxAdapter adapter = new InboxAdapter(this, uq);
        list = findViewById(R.id.list);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub

                int pos = position;
                String fromEmail = uq.get(pos);
                Log.d("From Email", fromEmail);
                Intent intent = new Intent(InboxActivity.this, ChatActivity.class);
                intent.putExtra("fromEmail", fromEmail);
                startActivity(intent);
            }
        });

    }
}
