package com.tradeapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tradeapplication.responses.Order;

import java.util.ArrayList;

public class SqliteHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "trade_db";
    private static final String TABLE_ORDERS = "orders";
    private static final String KEY_ID = "id";
    private static final String KEY_TYPE = "type";
    private static final String KEY_RATE = "rate";
    private static final String KEY_QTY = "quantity";

    private static final String CREATE_TABLE_ORDERS = "CREATE TABLE "
            + TABLE_ORDERS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TYPE + " TEXT," + KEY_RATE + " TEXT," + KEY_QTY + " TEXT" + ")";

    private static SqliteHelper sInstance;
    private SQLiteDatabase db;

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static SqliteHelper getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new SqliteHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ORDERS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertOrder(String type, String rate, String qty) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TYPE, type);
        values.put(KEY_RATE, rate);
        values.put(KEY_QTY, qty);
        db.insert(TABLE_ORDERS, null, values);

    }

    public ArrayList<Order> getOrders() {

        ArrayList<Order> ordersList = new ArrayList<>();
        db = this.getReadableDatabase();
        String query = "select * from " + TABLE_ORDERS ;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            ordersList.clear();
            while (cursor.moveToNext()) {
                Order item = new Order();
                item.setId(cursor.getString(cursor.getColumnIndex(KEY_ID)));
                item.setRate(cursor.getString(cursor.getColumnIndex(KEY_RATE)));
                item.setQty(cursor.getString(cursor.getColumnIndex(KEY_QTY)));
                item.setType(cursor.getString(cursor.getColumnIndex(KEY_TYPE)));
                ordersList.add(item);
            }
            cursor.close();
        }
        return ordersList;

    }

}