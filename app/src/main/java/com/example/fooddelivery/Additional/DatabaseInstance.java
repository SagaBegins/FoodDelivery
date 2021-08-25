package com.example.fooddelivery.Additional;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.fooddelivery.HelperModal.FoodElement;
import com.example.fooddelivery.HelperModal.OrderElements;

import java.util.ArrayList;
import java.util.List;


public class DatabaseInstance extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "FoodDeliveryApp";
    private static final int DATABASE_VERSION = 1;
    private static final String CART_TABLE = "cart_table";
    private static final String FAV_TABLE = "favour_table";
    private static final String PREORDER_TABLE = "order_previous";
    private static final String ORDER_TABLE = "order_cart_one";
    private static final String CREATE_PREORDER = "create table if not exists " + PREORDER_TABLE + "(foodname TEXT ,url TEXT primary key,price TEXT)";
    private static final String CREATE_ORDER = "create table if not exists " + ORDER_TABLE + "(foodname TEXT,url TEXT primary key,price TEXT,rate INTEGER, qty INTEGER DEFAULT 1,name TEXT,address TEXT,transactionID TEXT)";
    private static final String CREATE_CART = "create table if not exists " + CART_TABLE + "(foodname TEXT primary key,url TEXT,price TEXT,rate INTEGER, qty INTEGER DEFAULT 1)";
    private static final String CREATE_FAV = "create table if not exists " + FAV_TABLE + "(foodname TEXT primary key,url TEXT,price TEXT,rate INTEGER,  qty INTEGER DEFAULT 1)";

    Context context;


    public DatabaseInstance(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CART);
        db.execSQL(CREATE_FAV);
        db.execSQL(CREATE_PREORDER);
        db.execSQL(CREATE_ORDER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertIntoCart(String foodname, String url, String price, int rate, int qty) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_CART);

        ContentValues values = new ContentValues();
        values.put("foodname", foodname);
        values.put("url", url);
        values.put("price", price);
        values.put("rate", rate);
        values.put("qty", qty);

        try {
            if (totalQty(rate) < 40) {
                db.insertOrThrow(CART_TABLE, null, values);
                Toast.makeText(context, "Item added in Cart.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Cart is full! Can't order more than 40 items at a time.", Toast.LENGTH_SHORT).show();
            }
        } catch (SQLiteException e) {
            Toast.makeText(context, "Item already exists in Cart.", Toast.LENGTH_SHORT).show();
        }


    }

    public boolean isFav(String foodname) {
        String sqlQuery = "select count(*) from " + FAV_TABLE + " where  foodname= ?";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor i = db.rawQuery(sqlQuery, new String[]{foodname});

        if (i.moveToFirst()) {
            return i.getInt(0) != 0;
        }
        return false;
    }


    public void insertIntoFav(String foodname, String url, String price, int rate, int qty) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_FAV);
        ContentValues values = new ContentValues();
        values.put("foodname", foodname);
        values.put("url", url);
        values.put("price", price);
        values.put("rate", rate);
        values.put("qty", qty);

        try {
            db.insert(FAV_TABLE, null, values);
            Toast.makeText(context, "Item added in Favourites.", Toast.LENGTH_SHORT).show();
        } catch (SQLiteConstraintException e) {
        }

    }


    public int totalQty(int rate) {
        SQLiteDatabase db = this.getWritableDatabase();
        int total = 0;
        String sqlQuery = "select sum(qty) from " + CART_TABLE + " where rate=" + rate;
        Cursor i = db.rawQuery(sqlQuery, null);
        if (i.moveToFirst()) {
            total = i.getInt(0);
        }

        return total;
    }

    public List<FoodElement> getDataFromDB(String cart_table, int restaurantId) {
        List<FoodElement> modelList = new ArrayList<FoodElement>();
        String query = "select * from " + cart_table + " where  rate=" + restaurantId;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_FAV);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                FoodElement food = new FoodElement();
                food.setPhoto(cursor.getString(1));
                food.setName(cursor.getString(0));
                food.setPrice(cursor.getString(2));
                food.setRate(cursor.getInt(3));
                food.setQty(cursor.getInt(4));
                modelList.add(food);
            } while (cursor.moveToNext());
        }

        return modelList;
    }

    public List<FoodElement> getDataFromDB(String cart_table) {
        List<FoodElement> modelList = new ArrayList<FoodElement>();
        String query = "select * from " + cart_table;
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_FAV);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                FoodElement food = new FoodElement();
                food.setPhoto(cursor.getString(1));
                food.setName(cursor.getString(0));
                food.setPrice(cursor.getString(2));
                food.setRate(cursor.getInt(3));
                food.setQty(cursor.getInt(4));
                modelList.add(food);
            } while (cursor.moveToNext());
        }

        return modelList;
    }

    public void deleteCart(int rate, String TABLE) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_FAV);
        db.delete(TABLE, "rate = ?", new String[]{rate + ""});
    }


    public void deleteARow(String url, int rate, String TABLE) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_FAV);
        String query = "DELETE FROM " + TABLE + " WHERE url = ? AND rate = ?";
        db.execSQL(query, new String[]{url, rate + ""});
        //db.delete(TABLE, "url = ? and rate = ?" , new String[] { url, rate+"" });
    }

    public void updateInRow(String name, String table, int qty) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_FAV);
        ContentValues cv = new ContentValues();
        cv.put("qty", qty);
        String query = "UPDATE " + CART_TABLE + " SET qty= ? WHERE foodname = ?";
        db.execSQL(query, new String[]{qty + "", name});
    }

    public void addToOrderTable(String name, String add, String tranxid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_ORDER);
        String query = "select * from " + CART_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                ContentValues values = new ContentValues();
                values.put("foodname", cursor.getString(0));
                values.put("url", cursor.getString(1));
                values.put("price", cursor.getString(2));
                values.put("rate", cursor.getInt(3));
                values.put("qty", cursor.getInt(4));
                values.put("name", name);
                values.put("address", add);
                values.put("transactionID", tranxid);
                try {
                    db.insert(ORDER_TABLE, null, values);
                } catch (SQLiteConstraintException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
    }

    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("DROP TABLE " + CART_TABLE);
            db.execSQL(CREATE_CART);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("", "In delete");
        }
    }

    public void createTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL(CREATE_PREORDER);
        } catch (SQLiteException exe) {
            exe.printStackTrace();
            Log.d("Database Exception", "Error thrown on dividerblack 189 DatabaseEntry.java");
        }

    }

    public List<OrderElements> getDataFromOrder() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sqlQuery = "Select * from " + ORDER_TABLE;
        List<OrderElements> orderElements = new ArrayList<OrderElements>();
        try {
            Cursor cursor = db.rawQuery(sqlQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    OrderElements order = new OrderElements();
                    order.setFoodname(cursor.getString(0));
                    order.setUrl(cursor.getString(1));
                    order.setPrice(cursor.getString(2));
                    order.setRate(cursor.getString(3));
                    order.setQty(cursor.getString(4));
                    order.setName(cursor.getString(5));
                    order.setAddress(cursor.getString(6));
                    order.setTransactionID(cursor.getString(7));
                    orderElements.add(order);
                } while (cursor.moveToNext());
            }
        } catch (SQLiteException exe) {
            exe.printStackTrace();
        }
        return orderElements;
    }

    public void addToPreviousOrder() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(CREATE_ORDER);
        String query = "select * from " + ORDER_TABLE;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                ContentValues values = new ContentValues();
                values.put("foodname", cursor.getString(0));
                values.put("url", cursor.getString(1));
                values.put("price", cursor.getString(2));
                try {
                    db.insert(PREORDER_TABLE, null, values);
                } catch (SQLiteConstraintException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

    }

    public double total(int rate) {
        SQLiteDatabase db = this.getWritableDatabase();
        double total = 0;
        String sqlQuery = "select sum(qty*CAST(price as float)) from " + CART_TABLE + " WHERE rate =" + rate;
        Cursor i = db.rawQuery(sqlQuery, null);
        if (i.moveToFirst()) {
            total = i.getDouble(0);
        }

        return total;
    }

    @Override
    public synchronized void close() {
        super.close();
    }
}