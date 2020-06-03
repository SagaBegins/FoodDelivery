package com.example.food__delivery.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;


import com.example.food__delivery.Adapter.ChatAdapter;
import com.example.food__delivery.Fragment.InboxFragment;
import com.example.food__delivery.HelperModal.ChatModel;
import com.example.food__delivery.R;
import com.example.food__delivery.Singleton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    FirebaseUser mUser;
    DatabaseReference mRef;
    EditText editMsg;
    Button btnSend;
    ArrayList<ChatModel> mChatData = new ArrayList<>();
    String adminemail = "testadmin@gmail.com";
    String fromEmail;
    ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setTitle("Chat");
        createNotificationChannel();
        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);
        editMsg = (EditText)findViewById(R.id.edt_message);
        btnSend = (Button)findViewById(R.id.btn_send);

        mUser = Singleton.auth.getCurrentUser();

        if(mUser.getEmail().equals(adminemail)){
            Intent i= getIntent();
            Bundle b = i.getExtras();


            if(b!=null)
            {
               fromEmail = (String) b.get("fromEmail");
               displayMsg(fromEmail, adminemail);
            }
        }

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mUser.getEmail().equals(adminemail)){
                    String msg = editMsg.getText().toString();
                    String useremail = "testadmin@gmail.com";
                    String adminemail = fromEmail;

                    if(msg.isEmpty()){
                        //Empty message
                    } else {
                        send(useremail, adminemail, msg);
                        displayMsg(useremail, adminemail);
                        editMsg.setText("");
                    }
                }else {
                    String msg = editMsg.getText().toString();
                    String useremail = mUser.getEmail();
                    String adminemail = "testadmin@gmail.com";

                    if(msg.isEmpty()){
                        //Empty message
                    } else {
                        send(useremail, adminemail, msg);
                        displayMsg(useremail, adminemail);
                        editMsg.setText("");
                    }
                }

            }
        });

        String em = mUser.getEmail();
        String adem = "testadmin@gmail.com";

        if(mUser.getEmail().equals(adem)){

        }else {
            displayMsg(em, adem);
        }


    }

    //Send message
    private void send(String f, final String t, String m){
        DatabaseReference databaseReference = Singleton.db.getReference();

        HashMap<String, Object> hm = new HashMap<>();
        hm.put("from", f);
        hm.put("to", t);
        hm.put("msg", m);
        InboxFragment.msgSent = true;
        databaseReference.child("Chat").push().setValue(hm);

//        final String msgg = m;

//        mRef = Singleton.db.getReference("Users").child(mUser.getUid());
//        mRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                Users user = dataSnapshot.getValue(Users.class);
//                if (check) {
//                    notifyUserWithMessage(t, user.getEmail(), msgg);
//                }
//                check = false;
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
    }

    //Display messages to the user
    private void displayMsg(final String f, final String t){
        mChatData = new ArrayList<>();

        mRef = Singleton.db.getReference().child("Chat");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChatData.clear();
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    ChatModel chatmodel = d.getValue(ChatModel.class);
                    if(chatmodel.getTo().equals(f) && chatmodel.getFrom().equals(t) || chatmodel.getTo().equals(t) && chatmodel.getFrom().equals(f)){
                        mChatData.add(chatmodel);
                    }

                    // Create the adapter to convert the array to views
                    adapter = new ChatAdapter(ChatActivity.this, mChatData);
                    // Attach the adapter to a ListView
                    ListView listView = (ListView) findViewById(R.id.chatlist);
                    listView.setAdapter(adapter);
                    listView.setSelection(mChatData.size()-1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    //Display messages to the user
    private void displayAdminMsg(final String f, final String t){
        mChatData = new ArrayList<>();

        mRef = Singleton.db.getReference().child("Chat");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mChatData.clear();
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    ChatModel chatmodel = d.getValue(ChatModel.class);
                    if(chatmodel.getTo().equals(f) && chatmodel.getFrom().equals(t) || chatmodel.getTo().equals(t) && chatmodel.getFrom().equals(f)){
                        mChatData.add(chatmodel);
                    }

                    // Construct the data source
                    ArrayList<ChatModel> arrayOfUsers = new ArrayList<ChatModel>();
                    // Create the adapter to convert the array to views
                    ChatAdapter adapter = new ChatAdapter(ChatActivity.this, mChatData);
                    // Attach the adapter to a ListView
                    ListView listView = (ListView) findViewById(R.id.chatlist);
                    listView.setAdapter(adapter);
                    listView.setSelection(mChatData.size()-1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
