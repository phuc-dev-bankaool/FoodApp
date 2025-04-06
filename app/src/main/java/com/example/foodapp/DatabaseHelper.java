package com.example.foodapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "FoodApp.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_USERS = "users";


    // Cột của bảng users
    private static final String COLUMN_USER_ID = "id";
    private static final String COLUMN_USER_NAME = "name";
    private static final String COLUMN_USER_EMAIL = "email";
    private static final String COLUMN_USER_PASSWORD = "password";

    private static final String COLUMN_USER_ROLE = "role";

    // Tạo bảng users
    private static final String CREATE_TABLE_USERS =
            "CREATE TABLE " + TABLE_USERS + " (" +
                    COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_USER_NAME + " TEXT, " +
                    COLUMN_USER_EMAIL + " TEXT UNIQUE, " +
                    COLUMN_USER_PASSWORD + " TEXT, " +
                    COLUMN_USER_ROLE + " TEXT" +
                    ")";

    // Cột của bảng foods
    private static final String TABLE_FOODS = "foods";
    private static final String COLUMN_FOOD_ID = "food_id";
    private static final String COLUMN_FOOD_NAME = "food_name";
    private static final String COLUMN_FOOD_IMAGE = "food_image";
    private static final String COLUMN_FOOD_DESCRIPTION = "food_description";
    private static final String COLUMN_FOOD_PRICE = "food_price";
    private static final String COLUMN_FOOD_STATUS = "food_status";

    // Tạo bảng foods
    private static final String CREATE_TABLE_FOODS =
            "CREATE TABLE " + TABLE_FOODS + " (" +
                    COLUMN_FOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FOOD_NAME + " TEXT, " +
                    COLUMN_FOOD_IMAGE + " INTEGER, " +
                    COLUMN_FOOD_DESCRIPTION + " TEXT, " +
                    COLUMN_FOOD_PRICE + " REAL, " +
                    COLUMN_FOOD_STATUS + " INTEGER" +
                    ")";
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public boolean addUser(String name, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_NAME, name);
        values.put(COLUMN_USER_EMAIL, email);
        values.put(COLUMN_USER_PASSWORD, password);
        values.put(COLUMN_USER_ROLE, email.contains("admin0") ? "admin" : "user");

        long result = db.insert(TABLE_USERS, null, values);
        db.close();
        return result != -1;
    }
    public User getUserRole(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_USER_ID + ", " + COLUMN_USER_NAME + ", " +
                        COLUMN_USER_EMAIL + ", " + COLUMN_USER_PASSWORD + ", " + COLUMN_USER_ROLE +
                        " FROM " + TABLE_USERS +
                        " WHERE " + COLUMN_USER_EMAIL + "=? AND " + COLUMN_USER_PASSWORD + "=?",
                new String[]{email, password});

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String userEmail = cursor.getString(2);
            String userPassword = cursor.getString(3);
            String role = cursor.getString(4);

            cursor.close();
            db.close();
            return new User(id, name, userEmail, userPassword, role);
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }
    public boolean addFood(String name, int imageResId, String description, float price, boolean status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOOD_NAME, name);
        values.put(COLUMN_FOOD_IMAGE, imageResId);
        values.put(COLUMN_FOOD_DESCRIPTION, description);
        values.put(COLUMN_FOOD_PRICE, price);
        values.put(COLUMN_FOOD_STATUS, status ? 1 : 0);
        long result = db.insert(TABLE_FOODS, null, values);
        db.close();
        return result != -1;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_FOODS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOODS);
        onCreate(db);
    }
}
