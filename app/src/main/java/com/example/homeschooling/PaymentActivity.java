package com.example.homeschooling;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PaymentActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PaymentAdapter adapter;
    private DatabaseHelper dbHelper;
    private TextView tvEmptyMsg;
    private int parentId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_requests); // Same list layout reuse

        // Title update (kyunke layout reuse ho raha hai)
        TextView header = findViewById(R.id.tvHeaderTitle);
        if (header != null) header.setText("My Payments & Escrow");

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewMyRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get Current Parent ID
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String email = prefs.getString("USER_EMAIL", "");
        Cursor c = dbHelper.getUserByEmail(email);
        if (c != null && c.moveToFirst()) {
            parentId = c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID));
            c.close();
        }

        loadEscrowPayments();
    }

    private void loadEscrowPayments() {
        if (parentId == -1) return;

        Cursor cursor = dbHelper.getEscrowDetailsForParent(parentId);
        if (cursor != null && cursor.getCount() > 0) {
            adapter = new PaymentAdapter(this, cursor, "Parent");
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No active escrow payments found.", Toast.LENGTH_SHORT).show();
        }
    }
}