package com.example.foodapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Cart implements Parcelable {
    private int id;         // cart_id
    private int userId;     // user_id
    private Integer foodId; // Có thể null
    private int quantity;   // quantity

    public Cart(int id, int userId, Integer foodId, int quantity) {
        this.id = id;
        this.userId = userId;
        this.foodId = foodId;
        this.quantity = quantity;
    }

    // Constructor cho Parcelable
    protected Cart(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        // Kiểm tra byte cờ trước khi đọc foodId
        if (in.readByte() == 0) {
            foodId = null;
        } else {
            foodId = in.readInt();
        }
        quantity = in.readInt();
    }

    public static final Creator<Cart> CREATOR = new Creator<Cart>() {
        @Override
        public Cart createFromParcel(Parcel in) {
            return new Cart(in);
        }

        @Override
        public Cart[] newArray(int size) {
            return new Cart[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(userId);
        if (foodId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(foodId);
        }
        dest.writeInt(quantity);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}