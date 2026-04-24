package com.example.homeschooling;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ParentHomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private CardView cardRequests, cardAttendance, cardPayment, cardPostRequest, cardFindTutors;
    private TextView tvUserName, tvUserRole, tvActiveTuitions, tvDaysAttended;
    private ImageView btnLogout, btnNotification;

    private DatabaseHelper dbHelper;
    private int parentId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_home);

        // 1. Initialize Database & Views
        dbHelper = new DatabaseHelper(this);

        bottomNav = findViewById(R.id.bottom_nav);
        cardRequests = findViewById(R.id.cardRequests);
        cardAttendance = findViewById(R.id.cardAttendance);
        cardPayment = findViewById(R.id.cardPayment);
        cardPostRequest = findViewById(R.id.cardPostRequest);
        cardFindTutors = findViewById(R.id.cardFindTutors);

        tvUserName = findViewById(R.id.tvUserName);
        tvUserRole = findViewById(R.id.tvUserRole);
        btnLogout = findViewById(R.id.btnLogout);
        btnNotification = findViewById(R.id.btnNotification);

        tvActiveTuitions = findViewById(R.id.tvActiveTuitions);
        tvDaysAttended = findViewById(R.id.tvDaysAttended);

        // 2. Session Management & User Data
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String userEmail = prefs.getString("USER_EMAIL", "");
        String userName = prefs.getString("USER_NAME", "Parent");

        tvUserName.setText("Welcome, " + userName);
        tvUserRole.setText("Parent Account");

        // Fetch Parent ID from Database
        Cursor c = dbHelper.getUserByEmail(userEmail);
        if (c != null && c.moveToFirst()) {
            parentId = c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID));
            c.close();
        }

        // 3. Button Click Listeners (Asli Logic)

        // Post New Tuition Request
        cardPostRequest.setOnClickListener(v -> {
            startActivity(new Intent(ParentHomeActivity.this, TuitionFormActivity.class));
        });

        // Search/Find Tutors
        cardFindTutors.setOnClickListener(v -> {
            startActivity(new Intent(ParentHomeActivity.this, SearchTutorsActivity.class));
        });

        // My Requests (Active Tuitions & Mark Attendance)
        cardRequests.setOnClickListener(v -> {
            startActivity(new Intent(ParentHomeActivity.this, ParentRequestsActivity.class));
        });

        // Attendance tracking shortcut (Seedha wahi screen khulegi)
        cardAttendance.setOnClickListener(v -> {
            startActivity(new Intent(ParentHomeActivity.this, ParentRequestsActivity.class));
        });

        // Payments & Escrow System
        cardPayment.setOnClickListener(v -> {
            startActivity(new Intent(ParentHomeActivity.this, PaymentActivity.class));
        });

        // Logout Logic
        btnLogout.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(ParentHomeActivity.this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        });

        // 4. Bottom Navigation Setup
        bottomNav.setSelectedItemId(R.id.nav_home);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_tutors) {
                startActivity(new Intent(this, SearchTutorsActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            } else if (id == R.id.nav_attendance || id == R.id.nav_messages) {
                Toast.makeText(this, "Messaging feature coming soon!", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        // Dashboard Stats Load
        updateDashboardStats();
    }

    // Har baar screen par wapas aane par stats refresh honge
    @Override
    protected void onResume() {
        super.onResume();
        updateDashboardStats();
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_home);
        }
    }

    private void updateDashboardStats() {
        if (parentId == -1) return;

        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // 1. Count Active Tuitions (Accepted status)
        Cursor activeCursor = db.rawQuery("SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_TUITION_REQUESTS +
                        " WHERE " + DatabaseHelper.COLUMN_REQ_PARENT_ID + "=? AND " + DatabaseHelper.COLUMN_REQ_STATUS + "='Accepted'",
                new String[]{String.valueOf(parentId)});

        if (activeCursor != null && activeCursor.moveToFirst()) {
            tvActiveTuitions.setText(String.valueOf(activeCursor.getInt(0)));
            activeCursor.close();
        }

        // 2. Count Total Present Days Across All Tuitions
        String attendanceQuery = "SELECT COUNT(*) FROM " + DatabaseHelper.TABLE_ATTENDANCE + " a " +
                " JOIN " + DatabaseHelper.TABLE_TUITION_REQUESTS + " r ON a.att_req_id = r." + DatabaseHelper.COLUMN_REQ_ID +
                " WHERE r." + DatabaseHelper.COLUMN_REQ_PARENT_ID + "=? AND a.att_status='Present'";

        Cursor attCursor = db.rawQuery(attendanceQuery, new String[]{String.valueOf(parentId)});

        if (attCursor != null && attCursor.moveToFirst()) {
            tvDaysAttended.setText(String.valueOf(attCursor.getInt(0)));
            attCursor.close();
        }
    }
}