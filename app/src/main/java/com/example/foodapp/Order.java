package com.example.foodapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Order implements Parcelable {
    private int id;
    private int userId;
    private String orderDate;
    private float total;
    protected Order(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        orderDate = in.readString();
        total = in.readFloat();
    }

    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(userId);
        parcel.writeString(orderDate);
        parcel.writeFloat(total);
    }

    @Override
    public int describeContents() {
        return 0;
    }
    public Order(int id, int userId, String orderDate, float total) {
        this.id = id;
        this.userId = userId;
        this.orderDate = orderDate;
        this.total = total;
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

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}
