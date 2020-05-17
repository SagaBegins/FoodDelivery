package com.example.food__delivery.Helper;

import java.util.ArrayList;

public class OrderList {
    public ArrayList<FoodElement> foodList = new ArrayList<>();
    private String txnId;
    public String status;
    public String amount;
    public String txnTime;

    public int restaurantId = 0;

    public OrderList(){
    }

    public String getTxnId(){ return this.txnId;}
    public void setTxnId(String txnId) { this.txnId = txnId;}


    public OrderList(ArrayList<FoodElement> foodElements){
        this.foodList = foodElements;
        try{
            restaurantId = foodElements.get(0).getRate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
