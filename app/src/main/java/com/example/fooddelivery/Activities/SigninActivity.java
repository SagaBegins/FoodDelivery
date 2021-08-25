package com.example.fooddelivery.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fooddelivery.HelperModal.UserModal;
import com.example.fooddelivery.R;
import com.example.fooddelivery.Singleton;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

public class SigninActivity extends AppCompatActivity {

    private boolean isAdmin = false;
    private boolean isSet = false;
    private String facebookname;

    private FirebaseAuth signInauth = Singleton.auth;
    CallbackManager callbackManager;
    DatabaseReference firebaseDatabase = Singleton.db.getReference();

    TextInputEditText emailText, passwordText;
    LoginButton loginButton;
    String eid, password;
    Button login, forgotPass, signup;
    ProgressDialog loading;
    TextInputLayout email, pass;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FacebookSdk.setApplicationId("581033482823166");
        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();
        signInauth = Singleton.auth;

        emailText = findViewById(R.id.email);
        passwordText = findViewById(R.id.password);
        login = findViewById(R.id.button_login);
        forgotPass = findViewById(R.id.forgotpass);
        loginButton = findViewById(R.id.loginButton);
        forgotPass.setPaintFlags(forgotPass.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        signup = findViewById(R.id.signup);
        email = findViewById(R.id.emailinputlayout);
        pass = findViewById(R.id.passwordinputlayout);

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(emailText, InputMethodManager.SHOW_IMPLICIT);

        loading = new ProgressDialog(this);

        eid = emailText.getText().toString();
        password = passwordText.getText().toString();
        emailText.addTextChangedListener(new MyTextWatcher(emailText));
        passwordText.addTextChangedListener(new MyTextWatcher(passwordText));

        signup.setPaintFlags(signup.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), (me, response) -> {
                            JSONObject json = response.getJSONObject();
                            if (response.getError() != null) {
                            } else {
                                try {
                                    url = json.getString("id");
                                    facebookname = json.getString("name");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).executeAsync();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // ...
            }

            @Override
            public void onError(FacebookException error) {
                // ...
            }
        });

        forgotPass.setOnClickListener(view -> {
            startActivity(new Intent(SigninActivity.this, ForgotPasswordActivity.class));
        });

        signup.setOnClickListener(view -> {
            Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
            startActivity(intent);
        });

        login.setOnClickListener(view -> {
            submitForm();

            loading.setMessage("Loggin in Please Wait...");
            loading.show();

            eid = emailText.getText().toString();
            password = passwordText.getText().toString();

            signInauth.signInWithEmailAndPassword(eid, password).addOnCompleteListener(SigninActivity.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        Toast.makeText(SigninActivity.this, "Invalid Email or Password.", Toast.LENGTH_SHORT).show();
                    } else {
                        firebaseDatabase.child("users").child(signInauth.getUid()).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                try {
                                    isAdmin = dataSnapshot.child("admin").getValue(Boolean.class);
                                    Log.d("TAG", "onDataChange: " + isAdmin);
                                } catch (Exception e) {
                                    isAdmin = false;
                                }
                                isSet = true;
                                Intent intent;

                                if (isAdmin) {
                                    intent = new Intent(SigninActivity.this, AdminScreen.class);
                                } else {
                                    intent = new Intent(SigninActivity.this, MainScreen.class);
                                }

                                startActivity(intent);
                                finish();
                                loading.dismiss();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });

                    }

                }
            });
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(final AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());

        signInauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveData();
                            Intent intent = new Intent(SigninActivity.this, MainScreen.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(SigninActivity.this, "Sorry! User with this Email Address already exists.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void saveData() {
        String num = "Facebook user";
        UserModal userModal = new UserModal(facebookname, num, url);
        FirebaseUser user = signInauth.getCurrentUser();

        firebaseDatabase.child("users").child(user.getUid()).setValue(userModal);
    }

    class MyTextWatcher implements TextWatcher {
        private final View view;

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
                case R.id.email:
                    validateEmail();
                    break;
                case R.id.password:
                    validatePassword();
                    break;
            }
        }
    }

    private boolean validateEmail() {
        String emailTrimmed = emailText.getText().toString().trim();

        if (emailTrimmed.isEmpty()) {
            email.setError("Email field can't be empty.");
            requestFocus(emailText);
            return false;
        } else if (!isValidEmail(emailTrimmed)) {
            email.setError("Invalid email.");
            requestFocus(emailText);
            return false;
        } else {
            email.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        String passwordTrimmed = passwordText.getText().toString().trim();
        if (passwordTrimmed.isEmpty()) {
            pass.setError("Password field can't be empty.");
            requestFocus(passwordText);
            return false;
        } else if (passwordTrimmed.length() < 8) {
            pass.setError("Password must be more than 8 characters.");
            requestFocus(passwordText);
            return false;
        } else {
            pass.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void submitForm() {
        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }
    }
}
