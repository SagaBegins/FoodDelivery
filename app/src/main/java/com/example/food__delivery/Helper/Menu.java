package com.example.food__delivery.Helper;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Menu {
    public ArrayList<FoodElement> vegStarters;
    public ArrayList<FoodElement> nonVegStarters;
    public ArrayList<FoodElement> vegMainCourse;
    public ArrayList<FoodElement> nonVegMainCourse;
    public ArrayList<FoodElement> rice;
    public ArrayList<FoodElement> rolls;
    public ArrayList<FoodElement> sweets;
    public ArrayList<FoodElement> breads;
    public ArrayList<FoodElement> beverages;

    public void setIndex(String index, ArrayList<FoodElement> items) {
        switch (index) {
            case "vegstarters":
                this.vegStarters = new ArrayList<FoodElement>(items);
                break;
            case "nonvegstarters":
                this.nonVegStarters = new ArrayList<FoodElement>(items);
                break;
            case "vegmaincourse":
                this.vegMainCourse = new ArrayList<FoodElement>(items);
                break;
            case "nonvegmaincourse":
                this.nonVegMainCourse = new ArrayList<FoodElement>(items);
                break;
            case "rice":
                this.rice = new ArrayList<FoodElement>(items);
                break;
            case "rolls":
                this.rolls = new ArrayList<FoodElement>(items);
                break;
            case "sweets":
                this.sweets = new ArrayList<FoodElement>(items);
                break;
            case "breads":
                this.breads = new ArrayList<FoodElement>(items);
                break;
            case "beverages":
                this.beverages = new ArrayList<FoodElement>(items);
        }
    }
}
