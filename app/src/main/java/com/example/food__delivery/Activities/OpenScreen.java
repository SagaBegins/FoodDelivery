package com.example.food__delivery.Activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.food__delivery.Login;
import com.example.food__delivery.R;
import com.example.food__delivery.Signup;

public class OpenScreen extends AppCompatActivity implements View.OnClickListener{
    Button login, signup, skip;
    SQLiteDatabase sqLiteDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_screen);
        login = (Button)findViewById(R.id.button3);
        signup= (Button)findViewById(R.id.button2);
        skip = (Button)findViewById(R.id.button);
        sqLiteDatabase=openOrCreateDatabase("Hunger Hunt", MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("create table if not exists credentials(email text primary key, name text not null, password text not null);");
        login.setOnClickListener(this);
        signup.setOnClickListener(this);
        skip.setOnClickListener(this);

    }
    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.button3:
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);
                break;
            case R.id.button2:
                Intent intent1 = new Intent(this, Signup.class);
                startActivity(intent1);
                break;
            case R.id.button:
                Intent intent2 = new Intent(this,MainScreen.class);
                startActivity(intent2);
        }
    }
}
