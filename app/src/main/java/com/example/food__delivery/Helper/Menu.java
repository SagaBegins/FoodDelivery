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

    public boolean containsFood(String food){
        if(vegStartersContains(food))
            return true;
        if(nonVegStartersContains(food))
            return true;
        if(vegMainCourseContains(food))
            return true;
        if(nonVegMainCourseContains(food))
            return true;
        if(riceContains(food))
            return true;
        if(rollsContains(food))
            return true;
        if(sweetsContains(food))
            return true;
        if(breadsContains(food))
            return true;
        if(beveragesContains(food))
            return true;

        return false;
    }

    public boolean vegStartersContains(String food) {
        for(FoodElement f: vegStarters){
            if(f.getName().toLowerCase().contains(food.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public boolean nonVegStartersContains(String food) {
        for(FoodElement f: nonVegStarters){
            if(f.getName().toLowerCase().contains(food.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public boolean vegMainCourseContains(String food) {
        for(FoodElement f: vegMainCourse){
            if(f.getName().toLowerCase().contains(food.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public boolean nonVegMainCourseContains(String food) {
        for(FoodElement f: nonVegMainCourse){
            if(f.getName().toLowerCase().contains(food.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public boolean riceContains(String food) {
        for(FoodElement f: rice){
            if(f.getName().toLowerCase().contains(food.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public boolean rollsContains(String food) {
        for(FoodElement f: rolls){
            if(f.getName().toLowerCase().contains(food.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public boolean sweetsContains(String food) {
        for(FoodElement f: sweets){
            if(f.getName().toLowerCase().contains(food.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public boolean breadsContains(String food) {
        for(FoodElement f: breads){
            if(f.getName().toLowerCase().contains(food.toLowerCase())){
                return true;
            }
        }
        return false;
    }

    public boolean beveragesContains(String food) {
        for(FoodElement f: beverages){
            if(f.getName().toLowerCase().contains(food.toLowerCase())){
                return true;
            }
        }
        return false;
    }
}
