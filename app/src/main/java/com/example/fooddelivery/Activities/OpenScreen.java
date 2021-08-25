package com.example.fooddelivery.Activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddelivery.R;

public class OpenScreen extends AppCompatActivity implements View.OnClickListener {
    Button login, signup, skip;
    SQLiteDatabase sqLiteDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_screen);

        login = findViewById(R.id.login);
        signup = findViewById(R.id.signup);
        skip = findViewById(R.id.skip);

        sqLiteDatabase = openOrCreateDatabase("Hunger Hunt", MODE_PRIVATE, null);
        sqLiteDatabase.execSQL("create table if not exists credentials(email text primary key, name text not null, password text not null);");
        sqLiteDatabase.close();

        login.setOnClickListener(this);
        signup.setOnClickListener(this);
        skip.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                Intent loginIntent = new Intent(this, SigninActivity.class);
                startActivity(loginIntent);
                break;
            case R.id.signup:
                Intent signUpIntent = new Intent(this, SignupActivity.class);
                startActivity(signUpIntent);
                break;
            case R.id.skip:
                Intent skipIntent = new Intent(this, MainScreen.class);
                startActivity(skipIntent);
        }
    }
}
