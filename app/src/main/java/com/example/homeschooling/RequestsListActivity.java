package com.example.homeschooling;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RequestsListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RequestsAdapter adapter;
    private DatabaseHelper dbHelper;
    private int tutorId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_list);

        // Views aur Database initialize karna
        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Login kiye hue Tutor ki ID database se nikalna
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String email = prefs.getString("USER_EMAIL", "");

        Cursor c = dbHelper.getUserByEmail(email);
        if (c != null && c.moveToFirst()) {
            tutorId = c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID));
            c.close();
        }

        // List load karna
        loadRequests();
    }

    // Jab bhi screen dobara open ho toh list refresh ho jaye
    @Override
    protected void onResume() {
        super.onResume();
        loadRequests();
    }

    // YE HAI WO FUNCTION JISKA ERROR AA RAHA THA
    public void loadRequests() {
        Cursor cursor = dbHelper.getAvailableRequests();

        if (cursor != null && cursor.getCount() > 0) {
            // Agar requests hain toh list mein show karo
            adapter = new RequestsAdapter(this, cursor, dbHelper, tutorId);
            recyclerView.setAdapter(adapter);
        } else {
            // Agar koi request nahi hai toh khali list show karo aur message do
            Toast.makeText(this, "No new requests available.", Toast.LENGTH_SHORT).show();
            if (adapter != null) {
                adapter.swapCursor(null);
            }
        }
    }
}