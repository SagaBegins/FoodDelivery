package com.example.fooddelivery.HelperModal;

import java.util.ArrayList;

public class OrderList {
    public ArrayList<FoodElement> foodList = new ArrayList<>();
    public String userID;
    public String userEmail;
    public String txnId;
    public String status;
    public String amount;
    public String address;
    public String txnTime;
    public int restaurantId = 0;

    public OrderList(){
    }

}
