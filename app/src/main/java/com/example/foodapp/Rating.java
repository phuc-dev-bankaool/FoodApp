package com.example.foodapp;

public class Rating {
    private int id;
    private int userId;
    private int foodId;
    private int score;
    private String comment;

    public Rating(int id, int userId, int foodId, int score, String comment) {
        this.id = id;
        this.userId = userId;
        this.foodId = foodId;
        this.score = score;
        this.comment = comment;
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

    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
