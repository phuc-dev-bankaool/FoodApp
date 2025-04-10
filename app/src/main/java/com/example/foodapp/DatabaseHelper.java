package com.example.foodapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "FoodApp.db";
    private static final int DATABASE_VERSION = 4;

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
                    COLUMN_FOOD_IMAGE + " TEXT, " +
                    COLUMN_FOOD_DESCRIPTION + " TEXT, " +
                    COLUMN_FOOD_PRICE + " REAL, " +
                    COLUMN_FOOD_STATUS + " INTEGER" +
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
    private static final String COLUMN_ORDER_USER_ID = "user_id";
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
    private static final String COLUMN_RATING_USER_ID = "user_id";
    private static final String COLUMN_RATING_FOOD_ID = "food_id";
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

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USERS);
        db.execSQL(CREATE_TABLE_FOODS);
        db.execSQL(CREATE_TABLE_CART);
        db.execSQL(CREATE_TABLE_ORDERS);
        db.execSQL(CREATE_TABLE_RATINGS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATINGS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOODS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public void clearDatabase() {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOODS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CART);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_RATINGS);

            // Tạo lại các bảng sau khi xóa
            onCreate(db);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error clearing database: " + e.getMessage());
        } finally {
            db.close();
        }
    }

    // Hàm lấy ngày hiện tại
    public String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }

    public boolean removeOrdersByUserId(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            int rows = db.delete(TABLE_ORDERS, COLUMN_ORDER_USER_ID + "=?", new String[]{String.valueOf(userId)});
            Log.d("DatabaseHelper", "Deleted " + rows + " cart records for userId=" + userId);
            return rows > 0;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error removing cart by userId: " + e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    public boolean removeCartByUserId(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            int rows = db.delete(TABLE_CART, COLUMN_CART_USER_ID + "=?", new String[]{String.valueOf(userId)});
            Log.d("DatabaseHelper", "Deleted " + rows + " cart records for userId=" + userId);
            return rows > 0;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error removing cart by userId: " + e.getMessage());
            return false;
        } finally {
            db.close();
        }
    }

    // Thêm user
    public boolean addUser(String name, String email, String password, String adminCodeInput) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // 1. Check email đã tồn tại
            Cursor cursor = db.query(TABLE_USERS,
                    new String[]{COLUMN_USER_ID},
                    COLUMN_USER_EMAIL + "=?",
                    new String[]{email},
                    null, null, null);
            if (cursor.getCount() > 0) {
                cursor.close();
                db.endTransaction();
                return false;
            }
            cursor.close();

            // 2. Logic phân quyền "đặc biệt"
            final String ADMIN_SECRET_CODE = "#ADMIN2024!";
            final Set<String> WHITELISTED_ADMIN_EMAILS = new HashSet<>(Arrays.asList(
                    "admin@bankapp.com", "boss@company.vn"
            ));

            String role;
            if (WHITELISTED_ADMIN_EMAILS.contains(email) && ADMIN_SECRET_CODE.equals(adminCodeInput)) {
                role = "admin";
            } else {
                role = "user";
            }

            // 3. Insert user
            ContentValues userValues = new ContentValues();
            userValues.put(COLUMN_USER_NAME, name);
            userValues.put(COLUMN_USER_EMAIL, email);
            userValues.put(COLUMN_USER_PASSWORD, password);
            userValues.put(COLUMN_USER_ROLE, role);

            long userId = db.insertOrThrow(TABLE_USERS, null, userValues);
            if (userId == -1) {
                db.endTransaction();
                return false;
            }

            if (!role.equals("admin")) {
                ContentValues cartValues = new ContentValues();
                cartValues.put(COLUMN_CART_USER_ID, userId);
                cartValues.putNull(COLUMN_CART_FOOD_ID);
                cartValues.put(COLUMN_CART_QUANTITY, 0);
                long cartResult = db.insert(TABLE_CART, null, cartValues);
                if (cartResult == -1) {
                    db.endTransaction();
                    return false;
                }

                ContentValues orderValues = new ContentValues();
                orderValues.put(COLUMN_ORDER_USER_ID, userId);
                orderValues.put(COLUMN_ORDER_DATE, getCurrentDate());
                orderValues.put(COLUMN_ORDER_TOTAL, 0.0);
                long orderResult = db.insert(TABLE_ORDERS, null, orderValues);
                if (orderResult == -1) {
                    db.endTransaction();
                    return false;
                }
            }

            db.setTransactionSuccessful();
            Log.d("DatabaseHelper", "User " + name + " (" + role + ") registered successfully.");
            return true;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error adding user: " + e.getMessage());
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public boolean addToCart(int userId, int foodId, int quantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            Cursor cursor = db.query(TABLE_CART,
                    new String[]{COLUMN_CART_ID, COLUMN_CART_QUANTITY},
                    COLUMN_CART_USER_ID + "=? AND " + COLUMN_CART_FOOD_ID + "=?",
                    new String[]{String.valueOf(userId), String.valueOf(foodId)},
                    null, null, null);

            if (cursor.moveToFirst()) {
                int currentQuantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_QUANTITY));
                int newQuantity = currentQuantity + quantity;
                ContentValues values = new ContentValues();
                values.put(COLUMN_CART_QUANTITY, newQuantity);
                int rows = db.update(TABLE_CART, values,
                        COLUMN_CART_USER_ID + "=? AND " + COLUMN_CART_FOOD_ID + "=?",
                        new String[]{String.valueOf(userId), String.valueOf(foodId)});
                cursor.close();
                if (rows > 0) {
                    db.setTransactionSuccessful();
                    Log.d("DatabaseHelper", "Updated cart: foodId=" + foodId + ", newQuantity=" + newQuantity);
                    return true;
                }
            } else {
                cursor.close();
                ContentValues values = new ContentValues();
                values.put(COLUMN_CART_USER_ID, userId);
                values.put(COLUMN_CART_FOOD_ID, foodId);
                values.put(COLUMN_CART_QUANTITY, quantity);
                long result = db.insert(TABLE_CART, null, values);
                if (result != -1) {
                    db.setTransactionSuccessful();
                    Log.d("DatabaseHelper", "Added to cart: foodId=" + foodId + ", quantity=" + quantity);
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error adding to cart: " + e.getMessage());
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public List<Cart> getUserCart(int userId) {
        List<Cart> cartList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CART,
                new String[]{COLUMN_CART_ID, COLUMN_CART_USER_ID, COLUMN_CART_FOOD_ID, COLUMN_CART_QUANTITY},
                COLUMN_CART_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_ID));
                int foodId = cursor.isNull(cursor.getColumnIndexOrThrow(COLUMN_CART_FOOD_ID))
                        ? -1 : cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_FOOD_ID));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_QUANTITY));
                cartList.add(new Cart(id, userId, foodId, quantity));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return cartList;
    }

    public Food getFoodById(int foodId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FOODS,
                new String[]{COLUMN_FOOD_ID, COLUMN_FOOD_NAME, COLUMN_FOOD_IMAGE, COLUMN_FOOD_DESCRIPTION, COLUMN_FOOD_PRICE, COLUMN_FOOD_STATUS},
                COLUMN_FOOD_ID + "=?",
                new String[]{String.valueOf(foodId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FOOD_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOOD_NAME));
            String imageUri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOOD_IMAGE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOOD_DESCRIPTION));
            float price = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_FOOD_PRICE));
            boolean status = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FOOD_STATUS)) == 1;
            cursor.close();
            db.close();
            return new Food(id, name, description, price, status, imageUri);
        }
        cursor.close();
        db.close();
        return null;
    }

    public boolean updateCartQuantity(int cartId, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CART_QUANTITY, newQuantity);
        int rows = db.update(TABLE_CART, values, COLUMN_CART_ID + "=?", new String[]{String.valueOf(cartId)});
        db.close();
        Log.d("DatabaseHelper", "Updated cart quantity: cartId=" + cartId + ", newQuantity=" + newQuantity);
        return rows > 0;
    }

    public boolean removeFromCart(int cartId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_CART, COLUMN_CART_ID + "=?", new String[]{String.valueOf(cartId)});
        db.close();
        Log.d("DatabaseHelper", "Removed from cart: cartId=" + cartId);
        return result > 0;
    }

    // Xóa toàn bộ giỏ hàng của user
    public boolean clearCart(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_CART, COLUMN_CART_USER_ID + "=?", new String[]{String.valueOf(userId)});
        db.close();
        Log.d("DatabaseHelper", "Cleared cart for userId=" + userId);
        return result > 0;
    }

    // Đặt hàng
    public boolean placeOrder(int userId, String orderDate, float total) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_USER_ID, userId);
        values.put(COLUMN_ORDER_DATE, orderDate);
        values.put(COLUMN_ORDER_TOTAL, total);
        long result = db.insert(TABLE_ORDERS, null, values);
        db.close();
        Log.d("DatabaseHelper", "Order placed: userId=" + userId + ", total=" + total);
        return result != -1;
    }

    // Lấy danh sách đơn hàng của user
    public List<Order> getUserOrders(int userId) {
        List<Order> orderList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_ORDERS,
                new String[]{COLUMN_ORDER_ID, COLUMN_ORDER_USER_ID, COLUMN_ORDER_DATE, COLUMN_ORDER_TOTAL},
                COLUMN_ORDER_USER_ID + "=?",
                new String[]{String.valueOf(userId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ORDER_ID));
                String orderDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ORDER_DATE));
                float total = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_ORDER_TOTAL));
                orderList.add(new Order(id, userId, orderDate, total));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return orderList;
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD));
                String role = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ROLE));
                userList.add(new User(id, name, email, password, role));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return userList;
    }

    public boolean deleteUser(int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_USERS, COLUMN_USER_ID + "=?", new String[]{String.valueOf(userId)});
        db.close();
        return result > 0;
    }

    public boolean deleteFood(int foodId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_FOODS, COLUMN_FOOD_ID + "=?", new String[]{String.valueOf(foodId)});
        db.close();
        return result > 0;
    }

    public void updateUserRole(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_USER_ROLE, user.getRole());
            int rows = db.update(TABLE_USERS, values, COLUMN_USER_ID + "=?", new String[]{String.valueOf(user.getId())});
            if (rows == 0) {
                db.endTransaction();
                db.close();
                return;
            }

            String newRole = user.getRole();
            int userId = user.getId();

            if (newRole.equals("user")) {
                Cursor cartCursor = db.query(TABLE_CART,
                        new String[]{COLUMN_CART_ID},
                        COLUMN_CART_USER_ID + "=?",
                        new String[]{String.valueOf(userId)},
                        null, null, null);
                if (cartCursor.getCount() == 0) {
                    ContentValues cartValues = new ContentValues();
                    cartValues.put(COLUMN_CART_USER_ID, userId);
                    cartValues.putNull(COLUMN_CART_FOOD_ID);
                    cartValues.put(COLUMN_CART_QUANTITY, 0);
                    db.insert(TABLE_CART, null, cartValues);
                }
                cartCursor.close();

                Cursor orderCursor = db.query(TABLE_ORDERS,
                        new String[]{COLUMN_ORDER_ID},
                        COLUMN_ORDER_USER_ID + "=?",
                        new String[]{String.valueOf(userId)},
                        null, null, null);
                if (orderCursor.getCount() == 0) {
                    ContentValues orderValues = new ContentValues();
                    orderValues.put(COLUMN_ORDER_USER_ID, userId);
                    orderValues.put(COLUMN_ORDER_DATE, getCurrentDate());
                    orderValues.put(COLUMN_ORDER_TOTAL, 0.0);
                    db.insert(TABLE_ORDERS, null, orderValues);
                }
                orderCursor.close();
            } else if (newRole.equals("admin")) {
                db.delete(TABLE_CART, COLUMN_CART_USER_ID + "=?", new String[]{String.valueOf(userId)});
                db.delete(TABLE_ORDERS, COLUMN_ORDER_USER_ID + "=?", new String[]{String.valueOf(userId)});
            }

            db.setTransactionSuccessful();
            Log.d("DatabaseHelper", "User " + userId + " role updated to " + newRole);
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error updating user role: " + e.getMessage());
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public User getUserRole(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS +
                        " WHERE " + COLUMN_USER_EMAIL + "=? AND " + COLUMN_USER_PASSWORD + "=?",
                new String[]{email, password});

        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_NAME));
            String userEmail = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_EMAIL));
            String userPassword = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_PASSWORD));
            String role = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_ROLE));
            cursor.close();
            db.close();
            return new User(id, name, userEmail, userPassword, role);
        }
        cursor.close();
        db.close();
        return null;
    }

    public long addFood(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOOD_IMAGE, food.getImageUri());
        values.put(COLUMN_FOOD_NAME, food.getName());
        values.put(COLUMN_FOOD_DESCRIPTION, food.getDescription());
        values.put(COLUMN_FOOD_PRICE, food.getPrice());
        values.put(COLUMN_FOOD_STATUS, food.isStatus() ? 1 : 0);

        long result = db.insert(TABLE_FOODS, null, values);
        db.close();
        return result;
    }

    public List<Food> getAllFoods() {
        List<Food> foodList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_FOODS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FOOD_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOOD_NAME));
                String imageUri = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOOD_IMAGE));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FOOD_DESCRIPTION));
                float price = cursor.getFloat(cursor.getColumnIndexOrThrow(COLUMN_FOOD_PRICE));
                boolean status = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_FOOD_STATUS)) == 1;
                foodList.add(new Food(id, name, description, price, status, imageUri));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return foodList;
    }

    public boolean updateFood(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FOOD_NAME, food.getName());
        values.put(COLUMN_FOOD_DESCRIPTION, food.getDescription());
        values.put(COLUMN_FOOD_PRICE, food.getPrice());
        values.put(COLUMN_FOOD_STATUS, food.isStatus() ? 1 : 0);
        int rows = db.update(TABLE_FOODS, values, COLUMN_FOOD_ID + "=?", new String[]{String.valueOf(food.getId())});
        db.close();
        return rows > 0;
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

}