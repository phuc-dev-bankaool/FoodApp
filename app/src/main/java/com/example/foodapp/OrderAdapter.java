package com.example.foodapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private Context context;
    private List<Order> orders;
    private DatabaseHelper databaseHelper;

    public OrderAdapter(Context context, List<Order> orders) {
        this.context = context;
        this.orders = orders;
        this.databaseHelper = new DatabaseHelper(context);
    }

    public void updateOrders(List<Order> newOrders) {
        this.orders = newOrders;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_orders, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orders.get(position);
        holder.orderIdText.setText("Order ID: " + order.getId());
        holder.userNameText.setText("User: " + databaseHelper.getUserNameById(order.getUserId()));
        holder.orderDateText.setText("Date: " + order.getOrderDate());
        holder.orderTotalText.setText("Total: " + String.format("%.2f $", order.getTotal()));

        // Display order items
        List<Cart> items = order.getItems();
        if (items.isEmpty()) {
            holder.orderItemsText.setText("Items: None");
        } else {
            StringBuilder itemsText = new StringBuilder("Items:\n");
            for (Cart cart : items) {
                Food food = databaseHelper.getFoodById(cart.getFoodId());
                if (food != null) {
                    itemsText.append("- ").append(food.getName()).append(" (x").append(cart.getQuantity()).append(")\n");
                } else {
                    itemsText.append("- Unknown Food (x").append(cart.getQuantity()).append(")\n");
                }
            }
            holder.orderItemsText.setText(itemsText.toString());
        }
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView orderIdText, userNameText, orderDateText, orderTotalText, orderItemsText;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderIdText = itemView.findViewById(R.id.orderIdText);
            userNameText = itemView.findViewById(R.id.userNameText);
            orderDateText = itemView.findViewById(R.id.orderDateText);
            orderTotalText = itemView.findViewById(R.id.orderTotalText);
            orderItemsText = itemView.findViewById(R.id.orderItemsText);
        }
    }
}