package com.example.homeschooling;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TutorStudentsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RequestsAdapter adapter;
    private DatabaseHelper dbHelper;
    private int tutorId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Hum purani list wali XML hi use kar rahe hain kyunke structure same hai
        setContentView(R.layout.activity_parent_requests);

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewMyRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 1. Session se logged-in Tutor ki details nikalna
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String email = prefs.getString("USER_EMAIL", "");

        Cursor c = dbHelper.getUserByEmail(email);
        if (c != null && c.moveToFirst()) {
            tutorId = c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID));
            c.close();
        }

        // 2. Students ki list load karna
        loadMyStudents();
    }

    // Jab screen par wapas aayen toh list refresh ho jaye
    @Override
    protected void onResume() {
        super.onResume();
        loadMyStudents();
    }

    private void loadMyStudents() {
        if (tutorId != -1) {
            // DatabaseHelper se sirf wo students lana jinhein is tutor ne accept kiya hai
            Cursor cursor = dbHelper.getTutorStudents(tutorId);

            if (cursor != null && cursor.getCount() > 0) {
                // RequestsAdapter ko use kar rahe hain students dikhane ke liye
                adapter = new RequestsAdapter(this, cursor, dbHelper, tutorId);
                recyclerView.setAdapter(adapter);
            } else {
                Toast.makeText(this, "No students accepted yet.", Toast.LENGTH_SHORT).show();
                // Agar list khali hai toh adapter ko refresh kar do
                if (adapter != null) {
                    adapter.swapCursor(null);
                }
            }
        } else {
            Toast.makeText(this, "Error: Tutor ID not found. Please login again.", Toast.LENGTH_SHORT).show();
        }
    }
}