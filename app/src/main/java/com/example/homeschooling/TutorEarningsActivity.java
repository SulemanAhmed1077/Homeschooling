package com.example.homeschooling;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class TutorEarningsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PaymentAdapter adapter;
    private DatabaseHelper dbHelper;
    private int tutorId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_requests); // Reusing the same list layout

        dbHelper = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewMyRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Tutor ki ID nikalna
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String email = prefs.getString("USER_EMAIL", "");
        Cursor c = dbHelper.getUserByEmail(email);
        if (c != null && c.moveToFirst()) {
            tutorId = c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID));
            c.close();
        }

        loadEarnings();
    }

    private void loadEarnings() {
        // Tutor ke liye uski earnings nikalne ki query
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT e.*, r." + DatabaseHelper.COLUMN_REQ_CHILD_NAME +
                " FROM " + DatabaseHelper.TABLE_ESCROW_PAYMENTS + " e " +
                " JOIN " + DatabaseHelper.TABLE_TUITION_REQUESTS + " r " +
                " ON e." + DatabaseHelper.COLUMN_ESCROW_REQ_ID + " = r." + DatabaseHelper.COLUMN_REQ_ID +
                " WHERE r." + DatabaseHelper.COLUMN_REQ_TUTOR_ID + " = " + tutorId;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.getCount() > 0) {
            adapter = new PaymentAdapter(this, cursor, "Tutor"); // "Tutor" bheja taake button hide ho jaye
            recyclerView.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No earnings generated yet.", Toast.LENGTH_SHORT).show();
        }
    }
}