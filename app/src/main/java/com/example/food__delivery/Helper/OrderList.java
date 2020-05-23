package com.example.food__delivery.Helper;

import java.util.ArrayList;

public class OrderList {
    public ArrayList<FoodElement> foodList = new ArrayList<>();
    public String txnId;
    public String status;
    public String amount;
    public String txnTime;
    public int restaurantId = 0;

    public OrderList(){
    }

}
