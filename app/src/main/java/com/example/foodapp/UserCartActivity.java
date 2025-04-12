package com.example.foodapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UserCartActivity extends AppCompatActivity {
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private TextView cartTotalText;
    private Button proceedToOrderButton;
    private DatabaseHelper databaseHelper;
    private User loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_user);

        loggedInUser = getIntent().getParcelableExtra("loggedInUser");
        if (loggedInUser == null || !loggedInUser.getRole().equals("user")) {
            Toast.makeText(this, "Invalid user or not a user role", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        cartRecyclerView = findViewById(R.id.cartRecyclerView);
        cartTotalText = findViewById(R.id.cartTotalText);
        proceedToOrderButton = findViewById(R.id.proceedToOrderButton);

        databaseHelper = new DatabaseHelper(this);
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadCartItems();

        proceedToOrderButton.setOnClickListener(v -> proceedToOrder());
    }

    private void loadCartItems() {
        List<Cart> cartList = databaseHelper.getUserCart(loggedInUser.getId());
        cartAdapter = new CartAdapter(this, cartList, new CartAdapter.OnCartItemListener() {
            @Override
            public void onIncreaseQuantity(Cart cart) {
                databaseHelper.addToCart(loggedInUser.getId(), cart.getFoodId(), 1);
                loadCartItems();
            }

            @Override
            public void onDecreaseQuantity(Cart cart) {
                decreaseQuantity(cart);
                loadCartItems();
            }

            @Override
            public void onRemoveFromCart(Cart cart) {
                databaseHelper.removeFromCart(cart.getId());
                loadCartItems();
            }
        });
        cartRecyclerView.setAdapter(cartAdapter);
        updateTotalPrice(cartList);

        if (cartList.isEmpty()) {
            cartRecyclerView.setVisibility(View.GONE);
            findViewById(R.id.emptyCartView).setVisibility(View.VISIBLE);
            proceedToOrderButton.setEnabled(false);
        } else {
            cartRecyclerView.setVisibility(View.VISIBLE);
            findViewById(R.id.emptyCartView).setVisibility(View.GONE);
            proceedToOrderButton.setEnabled(true);
        }
    }

    private void decreaseQuantity(Cart cart) {
        int newQuantity = cart.getQuantity() - 1;
        if (newQuantity <= 0) {
            databaseHelper.removeFromCart(cart.getId());
        } else {
            databaseHelper.updateCartQuantity(cart.getId(), newQuantity);
        }
    }

    private void updateTotalPrice(List<Cart> cartList) {
        float total = calculateCartTotal(cartList);
        cartTotalText.setText(String.format("Total: %.2f $", total));
    }

    private float calculateCartTotal(List<Cart> cartList) {
        float total = 0;
        for (Cart cart : cartList) {
            if (cart.getFoodId() != null && cart.getFoodId() != -1) {
                Food food = databaseHelper.getFoodById(cart.getFoodId());
                if (food != null) {
                    total += food.getPrice() * cart.getQuantity();
                }
            }
        }
        return total;
    }

    private void proceedToOrder() {
        List<Cart> cartList = databaseHelper.getUserCart(loggedInUser.getId());
        if (cartList.isEmpty()) {
            Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        float total = calculateCartTotal(cartList);

        // Create custom dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Order");

        // Inflate custom layout
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_confirm_order, null);
        builder.setView(dialogView);

        // Setup RecyclerView in dialog
        RecyclerView dialogCartRecyclerView = dialogView.findViewById(R.id.dialogCartRecyclerView);
        dialogCartRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        CartAdapter dialogCartAdapter = new CartAdapter(this, cartList, null);
        dialogCartRecyclerView.setAdapter(dialogCartAdapter);

        // Set total in dialog
        TextView dialogTotalText = dialogView.findViewById(R.id.dialogTotalText);
        dialogTotalText.setText(String.format("Total: %.2f $", total));

        // Set buttons
        builder.setPositiveButton("Yes", (dialog, which) -> {
            String orderDate = databaseHelper.getCurrentDate();
            boolean success = databaseHelper.placeOrder(loggedInUser.getId(), orderDate, total, cartList);
            if (success) {
                databaseHelper.clearCart(loggedInUser.getId());
                Toast.makeText(this, "Order placed successfully", Toast.LENGTH_SHORT).show();

                List<Order> orders = databaseHelper.getUserOrders(loggedInUser.getId());
                if (!orders.isEmpty()) {
                    Order latestOrder = orders.get(orders.size() - 1);
                    Intent intent = new Intent(this, UserOrderActivity.class);
                    intent.putExtra("loggedInUser", loggedInUser);
                    intent.putExtra("order", latestOrder);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "Order placed, but no orders found", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, UserHomeActivity.class);
                    intent.putExtra("loggedInUser", loggedInUser);
                    startActivity(intent);
                    finish();
                }
            } else {
                Toast.makeText(this, "Failed to place order", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.setCancelable(false);
        builder.show();
    }
}