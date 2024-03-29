package com.example.fooddelivery.Fragment.MainScreenFragment;


import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fooddelivery.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class RateFragment extends androidx.fragment.app.Fragment {

    Button send;
    AlertDialog alertDialog;
    EditText number;

    public RateFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rate, container, false);
        send = view.findViewById(R.id.button8);
        number = view.findViewById(R.id.editText6);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (number.getText().length() == 0 || number.getText().length() != 10) {
                    number.setError("Please provide Correct Mobile Number.");
                } else {
                    HomeFragment fragment = new HomeFragment();
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment).commit();
                }
            }
        });
        return view;
    }
}
