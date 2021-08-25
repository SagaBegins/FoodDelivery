package com.example.fooddelivery.HelperModal;

public class Offer {
    // TODO use enums instead
    public static final String vs = "vegstarters";
    public static final String nvs = "nonvegstarters";
    public static final String vm = "vegmaincourse";
    public static final String nvm = "nonvegmaincourse";
    public static final String b = "breads";
    public static final String ro = "rolls";
    public static final String s = "sweets";
    public static final String bev = "beverages";
    public static final String r = "rice";

    public TotalDiscount totalDiscount;

    public float vegstarters = 0.0f;
    public float nonvegstarters = 0.0f;
    public float nonvegmaincourse = 0.0f;
    public float vegmaincourse = 0.0f;
    public float sweets = 0.0f;
    public float breads = 0.0f;
    public float rolls = 0.0f;
    public float rice = 0.0f;
    public float beverages = 0.0f;

    public float vegstartersqty = 1;
    public float nonvegstartersqty = 1;
    public float vegmaincourseqty = 1;
    public float nonvegmaincourseqty = 1;
    public float sweetsqty = 1;
    public float breadsqty = 1;
    public float rollsqty = 1;
    public float riceqty = 1;
    public float beveragesqty = 1;
}
