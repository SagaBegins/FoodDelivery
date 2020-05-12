package com.example.food__delivery.Helper;

import com.google.firebase.database.DataSnapshot;

public interface OnDataUpdateListener {
    void onStart();
    void onSuccess(DataSnapshot dataSnapshot);
    void onFailure();
    void onEnd();
}
