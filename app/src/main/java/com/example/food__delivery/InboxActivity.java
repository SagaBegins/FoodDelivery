package com.example.food__delivery;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InboxActivity extends AppCompatActivity {

    ListView list;
    FirebaseUser mUser;
    String adminemail = "testadmin@gmail.com";
    DatabaseReference mRef;
    ArrayList<String> mChats;
    List<Object> uqChatList;
    TextView inb;
    String[] uq;

    String[] maintitle ={
            "Admin"
    };

    Integer[] imgid={
            R.drawable.logo
    };
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);
        setTitle("Inbox");
//        inb = (TextView)findViewById(R.id.inb);
        mChats = new ArrayList<>();
        uqChatList = new ArrayList<>();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mUser.getEmail().equals(adminemail)){
            mRef = FirebaseDatabase.getInstance().getReference("Chat");
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mChats.clear();
                    if(dataSnapshot.exists()){
                        for(DataSnapshot d : dataSnapshot.getChildren()){
                            ChatModel cm = d.getValue(ChatModel.class);
                            //Getting all the chats that user sent
                            if(cm.getFrom().equals(mUser.getEmail())){
                                mChats.add(cm.getTo());
                            }
                            //Getting all the chats sent to the user
                            if(cm.getTo().equals(mUser.getEmail())){
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
        }else {
            InboxAdapter adapter = new InboxAdapter(this, maintitle);
            list=(ListView)findViewById(R.id.list);
            list.setAdapter(adapter);


            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub
//                    String item = (String) list.getItemAtPosition(position);
//                    Toast.makeText(InboxActivity.this,"You selected : " + item,Toast.LENGTH_SHORT).show();



                    if(position == 0) {
                        //code specific to first list item
//                    Toast.makeText(getApplicationContext(),"Place Your First Option Code",Toast.LENGTH_SHORT).show();

                        Intent i = new Intent(InboxActivity.this, ChatActivity.class);
                        startActivity(i);
                    }

                }
            });
        }

    }

//    //mChats consists of all the user id that user has chatted with, this methods makes the id uniques (removes duplicates)
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void chats(){
        uqChatList = mChats.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private void displayInfo(){
        uq = new String[uqChatList.size()];
        if(uqChatList != null){
            for(int i = 0; i < uqChatList.size(); i++){
                String a = uqChatList.get(i).toString();
                if(a.equals(adminemail)){

                }else {
                    uq[i] = a;
                }

            }
        }

        InboxAdapter adapter = new InboxAdapter(this, uq);
        list=(ListView)findViewById(R.id.list);
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub

                int pos = position;
                String fromEmail = uq[pos];
                Log.d("From Email", fromEmail);
                Intent intent = new Intent(InboxActivity.this, ChatActivity.class);
                intent.putExtra("fromEmail", fromEmail);
                startActivity(intent);


//                if(position == 0) {
//                    //code specific to first list item
////                    Toast.makeText(getApplicationContext(),"Place Your First Option Code",Toast.LENGTH_SHORT).show();
//
//                    Intent i = new Intent(InboxActivity.this, ChatActivity.class);
//                    startActivity(i);
//                }

            }
        });

    }
}
