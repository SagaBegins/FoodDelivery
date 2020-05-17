package com.example.food__delivery.Helper;


public class PreviousData {
    String name, price, url;
    int rate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setRate(int rate) {this.rate = rate;}

    public int getRate() {return this.rate;}
}
