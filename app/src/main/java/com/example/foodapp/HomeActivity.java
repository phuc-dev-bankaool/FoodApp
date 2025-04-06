package com.example.foodapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView foodRecyclerView;
    private  FoodAdapter foodAdapter;
    private List<Food> foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_user);


        foodRecyclerView = findViewById(R.id.foodRecyclerView);

        foodList = new ArrayList<>();

//        foodList.add(new Food("Pizza", R.drawable.pizza, "Delicious pizza with a variety of toppings"));

        foodAdapter = new FoodAdapter(foodList);



    }
}
