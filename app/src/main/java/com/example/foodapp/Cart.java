package com.example.foodapp;

public class Cart {
    private int id;         // cart_id
    private int userId;     // user_id
    private Integer foodId;
    private int quantity;   // quantity

    public Cart(int id, int userId, Integer foodId, int quantity) {
        this.id = id;
        this.userId = userId;
        this.foodId = foodId;
        this.quantity = quantity;
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

    public Integer getFoodId() { return foodId; }

    public void setFoodId(Integer foodId) { this.foodId = foodId; }
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
