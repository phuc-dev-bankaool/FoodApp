package com.example.foodapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    public interface OnFoodActionListener {
        void onEdit(Food food);
        void onDelete(Food food);
        void onAdd(Food food);
    }

    private Context context;
    private List<Food> foodList;
    private OnFoodActionListener listener;
    private boolean isAdmin;
    // adapter cho admin
    public FoodAdapter(Context context, List<Food> foodList, OnFoodActionListener listener) {
        this.context = context;
        this.foodList = foodList;
        this.listener = listener;
        this.isAdmin = true;
    }
    //adapter cho user
    public FoodAdapter(Context context, List<Food> foodListUser) {
        this.context = context;
        this.foodList = foodListUser != null ? foodListUser : new ArrayList<>();
        this.listener = null;
        this.isAdmin = false;
    }
    public static class FoodViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImageView;
        TextView nameTextView, descriptionTextView, priceTextView;
        ImageButton editButton, deleteButton;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImageView = itemView.findViewById(R.id.imageFood);
            nameTextView = itemView.findViewById(R.id.textFoodName);
            descriptionTextView = itemView.findViewById(R.id.textFoodDescription);
            priceTextView = itemView.findViewById(R.id.textFoodPrice);
            editButton = itemView.findViewById(R.id.buttonEdit);
            deleteButton = itemView.findViewById(R.id.buttonDelete);
        }
    }
    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_food_admin, parent, false);
        return new FoodViewHolder(view);
    }

    @SuppressLint("DefaultLocale")
    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food food = foodList.get(position);
        holder.nameTextView.setText(food.getName());
        holder.descriptionTextView.setText(food.getDescription());
        holder.priceTextView.setText(String.format("%.0f VNĐ", food.getPrice()));

        String imageUri = food.getImageUri();
        if (imageUri != null && !imageUri.isEmpty()) {
            holder.foodImageView.setImageURI(Uri.parse(imageUri));
        } else {
            holder.foodImageView.setImageResource(R.drawable.default_food_image);
        }

        if (listener != null) {
            holder.editButton.setVisibility(View.VISIBLE);
            holder.deleteButton.setVisibility(View.VISIBLE);
            holder.editButton.setOnClickListener(v -> listener.onEdit(food));
            holder.deleteButton.setOnClickListener(v -> listener.onDelete(food));
        } else {
            holder.editButton.setVisibility(View.GONE);
            holder.deleteButton.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return foodList != null ? foodList.size() : 0;
    }
    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<Food> newFoodList) {
        this.foodList = newFoodList;
        notifyDataSetChanged();
    }

}