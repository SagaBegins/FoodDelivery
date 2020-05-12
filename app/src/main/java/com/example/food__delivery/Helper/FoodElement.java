package com.example.food__delivery.Helper;

public class FoodElement {

    private String price;
    private String name;
    private String add;
    private String trans;
    private String photo;
    private String foodType;
    int rate;
    int qty;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getAdd() {
        return add;
    }
    public void setAdd(String add) {
        this.add = add;
    }
    public String getTrans() {
        return trans;
    }
    public void setTrans(String trans) {
        this.trans = trans;
    }
    public int getQty() {
        return qty;
    }
    public void setQty(int qty) {
        this.qty = qty;
    }
    public int getRate() {
        return rate;
    }
    public void setRate(int rate) {
        this.rate = rate;
    }
    public FoodElement(String price, String photo, String foodType) {
        this.price = price;
        this.photo = photo;
        this.foodType = foodType;
    }
    public String getPrice() {
        return price;
    }
    public void setPrice(String price) {
        this.price = price;
    }
    public FoodElement(String photo, String foodType) {
        this.photo = photo;
        this.foodType = foodType;
    }
    public FoodElement() {
    }
    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }
    public String getFoodType() {
        return foodType;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }
    public String getPhoto() {
        return photo;
    }
}
