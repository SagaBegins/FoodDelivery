package com.example.food__delivery.Helper;

import java.util.ArrayList;

public class OrderList {
    public ArrayList<FoodElement> foodList = new ArrayList<>();
    public int restaurantId = 0;

    public OrderList(){
    }

    public OrderList(ArrayList<FoodElement> foodElements){
        this.foodList = foodElements;
        try{
            restaurantId = foodElements.get(0).getRate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
