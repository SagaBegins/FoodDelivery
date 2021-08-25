package com.example.fooddelivery.Fragment;

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

import com.example.fooddelivery.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShippingFragment extends androidx.fragment.app.Fragment {

    private final int restaurantId;

    EditText firstName, lastName, phoneNo, address, zip, city;
    Button payment;
    SharedPreferences sharedPreferences;
    ViewPager viewPager;
    TabLayout tabLayout;
    TextInputLayout firstNameLayout, phoneNoLayout, addLayout, zipLayout, cityLayout;

    FieldValidator firstNameWatcher;
    FieldValidator phoneNoWatcher;
    FieldValidator addressWatcher;
    FieldValidator zipWatcher;
    FieldValidator cityWatcher;

    String cityPattern = "([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)";
    String addressPattern = "^[a-zA-Z0-9.-/s]+$";
    String namePattern = "^[a-zA-Z\\s]*$";
    String zipPattern = "^[1-9][0-9]{5}$";

    public ShippingFragment(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        View view = inflater.inflate(R.layout.fragment_shipping, container, false);

        // Getting elements from view
        tabLayout = view.findViewById(R.id.tabs);
        firstName = view.findViewById(R.id.firstnameedittext);
        phoneNo = view.findViewById(R.id.phonenumberedittext);
        address = view.findViewById(R.id.addressedittext);
        zip = view.findViewById(R.id.zipedittext);
        city = view.findViewById(R.id.cityedittext);
        viewPager = getActivity().findViewById(R.id.container1);
        payment = view.findViewById(R.id.movetopayment);

        // Layouts
        firstNameLayout = view.findViewById(R.id.firstnametext);
        addLayout = view.findViewById(R.id.addresstext);
        phoneNoLayout = view.findViewById(R.id.phonenumbertext);
        zipLayout = view.findViewById(R.id.ziptext);
        cityLayout = view.findViewById(R.id.citytext);

        // Initializing validators
        firstNameWatcher = new FieldValidator(firstName);
        phoneNoWatcher = new FieldValidator(phoneNo);
        addressWatcher = new FieldValidator(address);
        zipWatcher = new FieldValidator(zip);
        cityWatcher = new FieldValidator(city);

        // Setting up validation
        phoneNo.addTextChangedListener(phoneNoWatcher);
        address.addTextChangedListener(addressWatcher);
        zip.addTextChangedListener(zipWatcher);
        city.addTextChangedListener(cityWatcher);
        firstName.addTextChangedListener(firstNameWatcher);

        // Uncomment this block for faster manual testing
       /*firstName.setText("SS");
        phoneNo.setText("99999999999");
        address.setText("sdnajskda");
        zip.setText("123456");
        city.setText("SSS");*/

        sharedPreferences = getActivity().getSharedPreferences("SHIPPING_ADDRESS", Context.MODE_PRIVATE);

        // Setting the fields with values stored
        firstName.setText(sharedPreferences.getString("firstname", ""));
        phoneNo.setText(sharedPreferences.getString("phonenumber", ""));
        address.setText(sharedPreferences.getString("address", ""));
        zip.setText(sharedPreferences.getString("zip", ""));
        city.setText(sharedPreferences.getString("city", ""));

        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!firstName.getText().toString().isEmpty() && !phoneNo.getText().toString().isEmpty() && !address.getText().toString().isEmpty() && !zip.getText().toString().isEmpty() && !city.getText().toString().isEmpty()) {
                    sharedPreferences = getActivity().getSharedPreferences("SHIPPING_ADDRESS", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("firstname", firstName.getText().toString());
                    editor.putString("phonenumber", phoneNo.getText().toString());
                    editor.putString("address", address.getText().toString());
                    editor.putString("zip", zip.getText().toString());
                    editor.putString("city", city.getText().toString());
                    editor.commit();
                    Confirmation.textViews(firstName.getText().toString(), address.getText().toString(), city.getText().toString(), zip.getText().toString(), phoneNo.getText().toString());
                    viewPager.setCurrentItem(1);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("All input fields must be filled to proceed!")
                            .setTitle("Incomplete Data");
                    builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (firstName.getText().toString().isEmpty()) {
                                firstName.requestFocus();
                            } else if (phoneNo.getText().toString().isEmpty()) {
                                phoneNo.requestFocus();
                            } else if (address.getText().toString().isEmpty()) {
                                address.requestFocus();
                            } else if (zip.getText().toString().isEmpty()) {
                                zip.requestFocus();
                            } else if (city.getText().toString().isEmpty()) {
                                city.requestFocus();
                            }
                        }
                    });

                    submitForm();
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

        return view;
    }

    class FieldValidator implements android.text.TextWatcher {

        private final View view;

        private FieldValidator(View view) {
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
        } else if (!validateZip()) {
            return;
        } else if (!validateNumber()) {
            return;
        } else if (!validateAddress()) {
            return;
        } else if (!validateCity()) {
            return;
        }
    }

    private boolean validateName() {
        if (firstName.getText().toString().trim().isEmpty()) {
            firstNameLayout.setError("Name field can't be empty.");
            requestFocus(firstName);
            return false;
        } else if (!(firstName.getText().toString().trim()).matches(namePattern)) {
            firstNameLayout.setError("Please enter valid Name.");
            requestFocus(firstName);
            return false;
        } else {
            firstNameLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateZip() {
        if (zip.getText().toString().trim().isEmpty()) {
            zipLayout.setError("Pin Code field can't be empty.");
            requestFocus(zip);
            return false;
        } else if (!(zip.getText().toString().trim()).matches(zipPattern)) {
            zipLayout.setError("Please enter valid Pin Code.");
            requestFocus(zip);
            return false;
        } else {
            zipLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateNumber() {
        if (phoneNo.getText().toString().trim().isEmpty()) {
            phoneNoLayout.setError("Phone Number field can't be empty.");
            requestFocus(phoneNo);
            return false;
        } else if (phoneNo.getText().toString().trim().length() < 10) {
            phoneNoLayout.setError("Please enter valid phone number.");
            requestFocus(phoneNo);
            return false;
        } else {
            phoneNoLayout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateAddress() {
        if (address.getText().toString().trim().isEmpty()) {
            addLayout.setError("Address field can't be empty.");
            requestFocus(address);
            return false;
        } else if (!(address.getText().toString().trim()).matches(addressPattern)) {
            addLayout.setError("Please enter valid Address.");
            requestFocus(address);
            return false;
        } else {
            addLayout.setErrorEnabled(false);
        }
        return true;
    }

    private boolean validateCity() {
        if (city.getText().toString().trim().isEmpty()) {
            cityLayout.setError("City field can't be empty.");
            requestFocus(city);
            return false;
        } else if (!(city.getText().toString().trim()).matches(cityPattern)) {
            cityLayout.setError("Please enter valid City.");
            requestFocus(city);
            return false;
        } else {
            cityLayout.setErrorEnabled(false);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        firstName.setText(sharedPreferences.getString("firstname", ""));
        phoneNo.setText(sharedPreferences.getString("phonenumber", ""));
        address.setText(sharedPreferences.getString("address", ""));
        zip.setText(sharedPreferences.getString("zip", ""));
        city.setText(sharedPreferences.getString("city", ""));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        firstName.removeTextChangedListener(firstNameWatcher);
        address.removeTextChangedListener(addressWatcher);
        phoneNo.removeTextChangedListener(phoneNoWatcher);
        zip.removeTextChangedListener(zipWatcher);
        city.removeTextChangedListener(cityWatcher);
    }
}
