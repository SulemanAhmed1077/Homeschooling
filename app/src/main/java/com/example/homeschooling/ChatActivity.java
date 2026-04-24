package com.example.homeschooling;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private int senderId, receiverId;
    private String receiverName;
    private EditText etMessage;
    private ImageButton btnSend;
    private RecyclerView rvMessages;
    private ChatAdapter adapter; // Hum agle step mein banayenge

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        dbHelper = new DatabaseHelper(this);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        rvMessages = findViewById(R.id.rvMessages);
        TextView tvTitle = findViewById(R.id.tvChatWith);

        // 1. Data Lena (Intent se ya Session se)
        receiverId = getIntent().getIntExtra("RECEIVER_ID", -1);
        receiverName = getIntent().getStringExtra("RECEIVER_NAME");
        tvTitle.setText("Chat: " + receiverName);

        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        senderId = prefs.getInt("USER_ID", -1);

        // 2. RecyclerView Setup
        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        loadMessages();

        // 3. Send Message Click
        btnSend.setOnClickListener(v -> {
            String text = etMessage.getText().toString().trim();
            if (!text.isEmpty()) {
                long id = dbHelper.sendMessage(senderId, receiverId, text);
                if (id != -1) {
                    etMessage.setText("");
                    loadMessages(); // Refresh chat
                }
            }
        });
    }

    private void loadMessages() {
        Cursor cursor = dbHelper.getChatMessages(senderId, receiverId);
        // Yahan hum adapter ko data bhejenge (Step 3 mein)
        if (adapter == null) {
            adapter = new ChatAdapter(this, cursor, senderId);
            rvMessages.setAdapter(adapter);
        } else {
            adapter.swapCursor(cursor);
        }
        rvMessages.scrollToPosition(adapter.getItemCount() - 1);
    }
}