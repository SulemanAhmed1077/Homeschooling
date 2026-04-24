package com.example.homeschooling;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdminUserAdapter extends RecyclerView.Adapter<AdminUserAdapter.ViewHolder> {

    private Context context;
    private Cursor cursor;
    private OnUserDeleteListener deleteListener;

    public AdminUserAdapter(Context context, Cursor cursor, OnUserDeleteListener listener) {
        this.context = context;
        this.cursor = cursor;
        this.deleteListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_admin_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) return;

        String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
        String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));

        // UI Update
        holder.tvName.setText(name);
        holder.tvEmail.setText(email);


        holder.ivDelete.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteUser(email);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public interface OnUserDeleteListener {
        void onDeleteUser(String email);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvEmail;
        ImageView ivDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            ivDelete = itemView.findViewById(R.id.ivDelete);
        }
    }
}