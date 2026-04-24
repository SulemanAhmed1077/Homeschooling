package com.example.homeschooling;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private Context context;
    private Cursor cursor;
    private int currentUserId;

    // View Types taake pata chale sent hai ya received
    private static final int VIEW_TYPE_SENT = 1;
    private static final int VIEW_TYPE_RECEIVED = 2;

    public ChatAdapter(Context context, Cursor cursor, int currentUserId) {
        this.context = context;
        this.cursor = cursor;
        this.currentUserId = currentUserId;
    }

    @Override
    public int getItemViewType(int position) {
        cursor.moveToPosition(position);
        int senderId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SENDER_ID));

        // Agar sender main hoon toh SENT layout, warna RECEIVED
        if (senderId == currentUserId) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_SENT) {
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_sent, parent, false);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.item_chat_received, parent, false);
        }
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        cursor.moveToPosition(position);
        String message = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_MESSAGE_TEXT));
        holder.tvMessage.setText(message);
    }

    @Override
    public int getItemCount() {
        return (cursor == null) ? 0 : cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) cursor.close();
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public static class ChatViewHolder extends RecyclerView.ViewHolder {
        TextView tvMessage;
        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMessage = itemView.findViewById(R.id.tvMessage);
        }
    }
}