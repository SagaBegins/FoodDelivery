package com.example.food__delivery.Fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.viewpager.widget.ViewPager;

import com.example.food__delivery.Fragment.Confirmation;
import com.example.food__delivery.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShippingFragment extends androidx.fragment.app.Fragment {

    EditText firstName, lastName, phoneNo, address, zip, city;
    Button payment;
    SharedPreferences sharedPreferences;
    ViewPager viewPager;
    TabLayout tabLayout;
    TextInputLayout firstNameLayout, phoneNoLayout, addLayout, zipLayout, cityLayout;
    private int restaurantId;
    String cityPattern = "([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)";
    String addressPattern = "^[a-zA-Z0-9.-/s]+$";

    public ShippingFragment(int restaurantId) {
        this.restaurantId = restaurantId;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_shipping, container, false);
        tabLayout = (TabLayout)view.findViewById(R.id.tabs);
        firstName = (EditText)view.findViewById(R.id.firstnameedittext);
        phoneNo = (EditText)view.findViewById(R.id.phonenumberedittext);
        address = (EditText)view.findViewById(R.id.addressedittext);
        zip = (EditText)view.findViewById(R.id.zipedittext);
        city = (EditText)view.findViewById(R.id.cityedittext);


        //uncomment this block for faster testing
       /*firstName.setText("SS");
        phoneNo.setText("99999999999");
        address.setText("sdnajskda");
        zip.setText("123456");
        city.setText("SSS");*/

        firstNameLayout = (TextInputLayout)view.findViewById(R.id.firstnametext);
        addLayout = (TextInputLayout)view.findViewById(R.id.addresstext);
        phoneNoLayout = (TextInputLayout)view.findViewById(R.id.phonenumbertext);
        zipLayout = (TextInputLayout)view.findViewById(R.id.ziptext);
        cityLayout = (TextInputLayout)view.findViewById(R.id.citytext);

        viewPager = (ViewPager)getActivity().findViewById(R.id.container1);

        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        payment = (Button)view.findViewById(R.id.movetopayment);

        firstName.addTextChangedListener(new MyTextWatcher(firstName));
        phoneNo.addTextChangedListener(new MyTextWatcher(phoneNo));
        address.addTextChangedListener(new MyTextWatcher(address));
        zip.addTextChangedListener(new MyTextWatcher(zip));
        city.addTextChangedListener(new MyTextWatcher(city));
        submitForm();
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!firstName.getText().toString().isEmpty()&&!phoneNo.getText().toString().isEmpty()&&!address.getText().toString().isEmpty()&&!zip.getText().toString().isEmpty()&&!city.getText().toString().isEmpty()) {
                    sharedPreferences = getActivity().getSharedPreferences("SHIPPING_ADDRESS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("firstname", firstName.getText().toString());
                    editor.putString("phonenumber", phoneNo.getText().toString());
                    editor.putString("address", address.getText().toString());
                    editor.putString("zip", zip.getText().toString());
                    editor.putString("city", city.getText().toString());
                    editor.commit();
                    Confirmation.textviews(firstName.getText().toString(),address.getText().toString(),city.getText().toString(),zip.getText().toString(),phoneNo.getText().toString());
                    viewPager.setCurrentItem(1);
                }else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("All input fields must be filled to proceed!")
                            .setTitle("Incomplete Data");
                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if(firstName.getText().toString().isEmpty()){
                                firstName.requestFocus();
                            }else if(phoneNo.getText().toString().isEmpty()){
                                phoneNo.requestFocus();
                            }else if(address.getText().toString().isEmpty()){
                                address.requestFocus();
                            }else if(zip.getText().toString().isEmpty()){
                                zip.requestFocus();
                            }else if(city.getText().toString().isEmpty()){
                                city.requestFocus();
                            }
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        return view;
    }
    String namepattern = "^[a-zA-Z\\s]*$";
    String zippattern = "^[1-9][0-9]{5}$";

    class MyTextWatcher implements android.text.TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.firstnameedittext:
                    validateName();
                    break;
                case R.id.phonenumberedittext:
                    validateNumber();
                    break;
                case R.id.addressedittext:
                    validateAddress();
                    break;
                case R.id.zipedittext:
                    validateZip();
                    break;
                case R.id.cityedittext:
                    validateCity();
                    break;
            }
        }
    }

    private void submitForm() {
        if (!validateName()) {
            return;
        }else if (!validateZip()) {
            return;
        }else if (!validateNumber()) {
            return;
        }else if(!validateAddress()){
            return;
        }else if(!validateCity()){
            return;
        }
    }

    private boolean validateName() {
        if (firstName.getText().toString().trim().isEmpty()) {
            firstNameLayout.setError("Name field can't be empty.");
            requestFocus(firstName);
            return false;
        } else if(!(firstName.getText().toString().trim()).matches(namepattern)){
            firstNameLayout.setError("Please enter valid Name.");
            requestFocus(firstName);
            return false;
        }else{
            firstNameLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateZip() {
        if (zip.getText().toString().trim().isEmpty()) {
            zipLayout.setError("Pin Code field can't be empty.");
            requestFocus(zip);
            return false;
        } else if(!(zip.getText().toString().trim()).matches(zippattern)){
            zipLayout.setError("Please enter valid Pin Code.");
            requestFocus(zip);
            return false;
        }else{
            zipLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateNumber() {
        if (phoneNo.getText().toString().trim().isEmpty()) {
            phoneNoLayout.setError("Phone Number field can't be empty.");
            requestFocus(phoneNo);
            return false;
        }else if(phoneNo.getText().toString().trim().length()<10){
            phoneNoLayout.setError("Please enter valid phone number.");
            requestFocus(phoneNo);
            return false;
        }
        else {
            phoneNoLayout.setErrorEnabled(false);
        }

        return true;
    }


    private boolean validateAddress(){
        if (address.getText().toString().trim().isEmpty()) {
            addLayout.setError("Address field can't be empty.");
            requestFocus(address);
            return false;
        } else if(!(address.getText().toString().trim()).matches(addressPattern)){
            addLayout.setError("Please enter valid Address.");
            requestFocus(address);
            return false;
        }else{
            addLayout.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validateCity(){
        if (city.getText().toString().trim().isEmpty()) {
            cityLayout.setError("City field can't be empty.");
            requestFocus(city);
            return false;
        } else if(!(city.getText().toString().trim()).matches(cityPattern)){
            cityLayout.setError("Please enter valid City.");
            requestFocus(city);
            return false;
        }else{
            cityLayout.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


}