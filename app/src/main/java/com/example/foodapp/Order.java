package com.example.foodapp;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Order implements Parcelable {
    private int id;
    private int userId;
    private String orderDate;
    private float total;
    private List<Cart> items;

    public Order(int id, int userId, String orderDate, float total) {
        this.id = id;
        this.userId = userId;
        this.orderDate = orderDate;
        this.total = total;
        this.items = new ArrayList<>();
    }

    protected Order(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        orderDate = in.readString();
        total = in.readFloat();
        items = new ArrayList<>();
        in.readList(items, Cart.class.getClassLoader());
    }

    public static final Creator<Order> CREATOR = new Creator<Order>() {
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(userId);
        dest.writeString(orderDate);
        dest.writeFloat(total);
        dest.writeList(items);
    }

    public int getId() { return id; }
    public int getUserId() { return userId; }
    public String getOrderDate() { return orderDate; }
    public float getTotal() { return total; }
    public List<Cart> getItems() { return items; }
    public void setItems(List<Cart> items) { this.items = items; }
}