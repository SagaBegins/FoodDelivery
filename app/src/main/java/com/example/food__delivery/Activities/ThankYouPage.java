package com.example.food__delivery.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.food__delivery.R;
import com.example.food__delivery.Additional.DatabaseInstance;

public class ThankYouPage extends AppCompatActivity {

    Button order;
    DatabaseInstance databaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thank_you_page);
        order=(Button)findViewById(R.id.button16);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainScreen.class);
                startActivity(intent);
            }
        });
    }
}
