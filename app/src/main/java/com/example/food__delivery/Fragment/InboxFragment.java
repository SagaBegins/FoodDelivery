package com.example.food__delivery.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

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


import com.example.food__delivery.ChatActivity;
import com.example.food__delivery.ChatModel;
import com.example.food__delivery.InboxAdapter;
import com.example.food__delivery.R;
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

public class InboxFragment extends Fragment {

    ListView list;
    FirebaseUser mUser;
    final String adminemail = "testadmin@gmail.com";
    DatabaseReference mRef;
    ArrayList<String> mChats;
    List<Object> uqChatList;
    TextView inb;
    List<String> uq;
    View view;

    List<String> maintitle = new ArrayList<>();

    Integer[] imgid={
            R.drawable.logo
    };
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_inbox, container, false);
        maintitle.add("Admin");
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

            Intent i = new Intent(getContext(), ChatActivity.class);
            startActivity(i);
            InboxAdapter adapter = new InboxAdapter(getActivity(), maintitle);
            list=(ListView) view.findViewById(R.id.list);
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

                        Intent i = new Intent(getContext(), ChatActivity.class);
                        startActivity(i);
                    }

                }
            });
        }
        return view;
    }

//    //mChats consists of all the user id that user has chatted with, this methods makes the id uniques (removes duplicates)
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void chats(){
        uqChatList = mChats.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private void displayInfo(){
        uq = new ArrayList<>();
        int minus = 0;
        if(uqChatList != null){
            for(int i = 0; i < uqChatList.size(); i++){
                String a = uqChatList.get(i).toString();
                if(a.equals(adminemail)){
                }else {
                    uq.add(a);
                }

            }
        }

        InboxAdapter adapter = new InboxAdapter(getActivity(), uq);
        list=(ListView) view.findViewById(R.id.list);
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub

                int pos = position;
                String fromEmail = uq.get(pos);
                Log.d("From Email", fromEmail);
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra("fromEmail", fromEmail);
                startActivity(intent);



            }
        });

    }
}
