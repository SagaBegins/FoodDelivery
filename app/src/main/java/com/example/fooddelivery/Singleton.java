package com.example.fooddelivery;

import android.util.Log;

import com.example.fooddelivery.HelperModal.Menu;
import com.example.fooddelivery.HelperModal.Offer;
import com.example.fooddelivery.HelperModal.Restaurant;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Singleton {
    public static ArrayList<Restaurant> restaurantList = new ArrayList<>();
    public static ArrayList<Menu> menuList = new ArrayList<>();
    public static FirebaseAuth auth = FirebaseAuth.getInstance();
    public static FirebaseDatabase db = FirebaseDatabase.getInstance();
    public static Offer offers = new Offer();
}
