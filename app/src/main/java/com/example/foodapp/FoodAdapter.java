package com.example.foodapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodAdapter {

    private List<Food> foodList;

    public FoodAdapter(List<Food> foodList) {
        this.foodList = foodList;
    }


}
