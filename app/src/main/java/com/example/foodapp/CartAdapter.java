package com.example.foodapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<Cart> cartItems;
    private OnCartItemListener listener;
    private DatabaseHelper databaseHelper;

    public CartAdapter(Context context, List<Cart> cartItems, OnCartItemListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart_user, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Cart item = cartItems.get(position);
        Food food = databaseHelper.getFoodById(item.getFoodId());

        if (food != null) {
            holder.cartFoodName.setText(food.getName());
            holder.cartFoodPrice.setText(String.format(Locale.getDefault(), "%.0f đ", food.getPrice() * item.getQuantity()));
            if (food.getImageUri() != null && !food.getImageUri().isEmpty()) {
                holder.cartFoodImage.setImageURI(android.net.Uri.parse(food.getImageUri()));
            } else {
                holder.cartFoodImage.setImageResource(R.drawable.default_food_image);
            }
        } else {
            holder.cartFoodName.setText("Unknown Food");
            holder.cartFoodPrice.setText("0 đ");
        }

        holder.cartQuantity.setText(String.valueOf(item.getQuantity()));

        holder.buttonIncrease.setOnClickListener(v -> {
            if (listener != null) {
                listener.onIncreaseQuantity(item);
            }
        });

        holder.buttonDecrease.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDecreaseQuantity(item);
            }
        });

        holder.buttonRemove.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRemoveFromCart(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView cartFoodImage;
        TextView cartFoodName, cartFoodPrice, cartQuantity;
        Button buttonIncrease, buttonDecrease;
        ImageButton buttonRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            cartFoodImage = itemView.findViewById(R.id.cartFoodImage);
            cartFoodName = itemView.findViewById(R.id.cartFoodName);
            cartFoodPrice = itemView.findViewById(R.id.cartFoodPrice);
            cartQuantity = itemView.findViewById(R.id.cartQuantity);
            buttonIncrease = itemView.findViewById(R.id.buttonIncrease);
            buttonDecrease = itemView.findViewById(R.id.buttonDecrease);
            buttonRemove = itemView.findViewById(R.id.buttonRemoveFromCart);
        }
    }

    public interface OnCartItemListener {
        void onIncreaseQuantity(Cart cart);
        void onDecreaseQuantity(Cart cart);
        void onRemoveFromCart(Cart cart);
    }
}