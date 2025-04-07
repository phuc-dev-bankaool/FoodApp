package com.example.foodapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "FoodApp.db";
    private static final int DATABASE_VERSION = 3;


    // Cột của bảng users
    private static final String TABLE_USERS = "users";
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
    // Cột của bảng categories
    private static final String TABLE_CATEGORIES = "categories";
    private static final String COLUMN_CATEGORY_ID = "category_id";
    private static final String COLUMN_CATEGORY_NAME = "category_name";
    // Tạo bảng categories
    private static final String CREATE_TABLE_CATEGORIES =
            "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                    COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CATEGORY_NAME + " TEXT" +
                    ")";
    // Cột của bảng giỏ hàng (cart)
    private static final String TABLE_CART = "cart";
    private static final String COLUMN_CART_ID = "cart_id";
    private static final String COLUMN_CART_USER_ID = "user_id";
    private static final String COLUMN_CART_FOOD_ID = "food_id";
    private static final String COLUMN_CART_QUANTITY = "quantity";
    // Tạo bảng giỏ hàng (cart)
    private static final String CREATE_TABLE_CART =
            "CREATE TABLE " + TABLE_CART + " (" +
                    COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_CART_USER_ID + " INTEGER, " +
                    COLUMN_CART_FOOD_ID + " INTEGER, " +
                    COLUMN_CART_QUANTITY + " INTEGER, " +
                    "FOREIGN KEY(" + COLUMN_CART_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "), " +
                    "FOREIGN KEY(" + COLUMN_CART_FOOD_ID + ") REFERENCES " + TABLE_FOODS + "(" + COLUMN_FOOD_ID + ")" +
                    ")";

    // Cột của bảng đơn hàng (orders)
    private static final String TABLE_ORDERS = "orders";
    private static final String COLUMN_ORDER_ID = "order_id";
    private static final String COLUMN_ORDER_USER_ID = "user_id";  // liên kết với bảng users
    private static final String COLUMN_ORDER_DATE = "order_date";
    private static final String COLUMN_ORDER_TOTAL = "order_total";
    // Tạo bảng orders
    private static final String CREATE_TABLE_ORDERS =
            "CREATE TABLE " + TABLE_ORDERS + " (" +
                    COLUMN_ORDER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_ORDER_USER_ID + " INTEGER, " +
                    COLUMN_ORDER_DATE + " TEXT, " +
                    COLUMN_ORDER_TOTAL + " REAL, " +
                    "FOREIGN KEY(" + COLUMN_ORDER_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + ")" +
                    ")";
    // Bảng Đánh giá (ratings)
    private static final String TABLE_RATINGS = "ratings";
    private static final String COLUMN_RATING_ID = "rating_id";
    private static final String COLUMN_RATING_USER_ID = "user_id";  // liên kết với bảng users
    private static final String COLUMN_RATING_FOOD_ID = "food_id";  // liên kết với bảng foods
    private static final String COLUMN_RATING_SCORE = "rating_score";
    private static final String COLUMN_RATING_COMMENT = "rating_comment";
    // Tạo bảng ratings
    private static final String CREATE_TABLE_RATINGS =
            "CREATE TABLE " + TABLE_RATINGS + " (" +
                    COLUMN_RATING_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_RATING_USER_ID + " INTEGER, " +
                    COLUMN_RATING_FOOD_ID + " INTEGER, " +
                    COLUMN_RATING_SCORE + " INTEGER, " +
                    COLUMN_RATING_COMMENT + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_RATING_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_USER_ID + "), " +
                    "FOREIGN KEY(" + COLUMN_RATING_FOOD_ID + ") REFERENCES " + TABLE_FOODS + "(" + COLUMN_FOOD_ID + ")" +
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
            Log.d("Database", "User found: " + name + " - " + role);
            cursor.close();
            db.close();
            return new User(id, name, userEmail, userPassword, role);
        } else {
            cursor.close();
            db.close();
            Log.d("Database", "No user found for given credentials.");
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
    public List<Food> getAllFoods() {
        List<Food> foodList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FOODS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FOOD_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOOD_NAME));
                int imageResId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FOOD_IMAGE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOOD_DESCRIPTION));
                float price = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_FOOD_PRICE));
                boolean status = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FOOD_STATUS)) == 1;

                foodList.add(new Food(id, name, imageResId, description, price, status));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return foodList;
    }

    public boolean addCategory(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, name);
        long result = db.insert(TABLE_CATEGORIES, null, values);
        db.close();
        return result != -1;
    }
    public boolean addToCart(int userId, int foodId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CART_USER_ID, userId);
        values.put(COLUMN_CART_FOOD_ID, foodId);
        values.put(COLUMN_CART_QUANTITY, quantity);
        long result = db.insert(TABLE_CART, null, values);
        db.close();
        return result != -1;
    }
    public boolean addRating(int userId, int foodId, int score, String comment) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RATING_USER_ID, userId);
        values.put(COLUMN_RATING_FOOD_ID, foodId);
        values.put(COLUMN_RATING_SCORE, score);
        values.put(COLUMN_RATING_COMMENT, comment);
        long result = db.insert(TABLE_RATINGS, null, values);
        db.close();
        return result != -1;
    }
    public boolean placeOrder(int userId, String orderDate, float total) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_USER_ID, userId);
        values.put(COLUMN_ORDER_DATE, orderDate);
        values.put(COLUMN_ORDER_TOTAL, total);
        long result = db.insert(TABLE_ORDERS, null, values);
        db.close();
        return result != -1;
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOODS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATINGS);

            // Sau khi xóa các bảng, tạo lại chúng
            db.execSQL(CREATE_TABLE_USERS);
            db.execSQL(CREATE_TABLE_FOODS);
            db.execSQL(CREATE_TABLE_CATEGORIES);
            db.execSQL(CREATE_TABLE_CART);
            db.execSQL(CREATE_TABLE_ORDERS);
            db.execSQL(CREATE_TABLE_RATINGS);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOODS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
}
