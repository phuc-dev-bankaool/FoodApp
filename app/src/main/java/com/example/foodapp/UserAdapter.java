package com.example.foodapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final Context context;
    private List<User> userList;
    private final OnUserActionListener listener;

    public interface OnUserActionListener {
        void onDelete(User user);
        void onChangeRole(User user);
    }

    public UserAdapter(Context context, List<User> userList, OnUserActionListener listener) {
        this.context = context;
        this.userList = userList;
        this.listener = listener;
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtEmail, txtRole;
        ImageButton btnDelete, btnChangeRole;

        public UserViewHolder(View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.textUserName);
            txtEmail = itemView.findViewById(R.id.textUserEmail);
            txtRole = itemView.findViewById(R.id.textUserRole);
            btnDelete = itemView.findViewById(R.id.buttonDelete);
            btnChangeRole = itemView.findViewById(R.id.buttonRole);
        }
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_row, parent, false);
        return new UserViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull UserAdapter.UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.txtName.setText(user.getName());
        holder.txtEmail.setText(user.getEmail());
        holder.txtRole.setText("Role " + user.getRole());
        // Set behavior for buttons
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(user));
        holder.btnChangeRole.setOnClickListener(v -> listener.onChangeRole(user));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<User> newList) {
        this.userList = newList;
        notifyDataSetChanged();
    }
}
