package com.example.homeschooling;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TutorHomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;
    private CardView cardMyStudents, cardEarnings, cardFindStudents, cardMySchedule;
    private TextView tvUserName, tvUserRole, tvTutorStudents, tvTutorEarnings;
    private ImageView btnLogout, btnNotification;

    private DatabaseHelper dbHelper;
    private int tutorId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_home);

        // 1. Initialize Database & Views
        dbHelper = new DatabaseHelper(this);

        bottomNav = findViewById(R.id.bottom_nav);
        cardMyStudents = findViewById(R.id.cardMyStudents);
        cardEarnings = findViewById(R.id.cardEarnings);
        cardFindStudents = findViewById(R.id.cardFindStudents);
        cardMySchedule = findViewById(R.id.cardMySchedule); // Added if exists in XML

        tvUserName = findViewById(R.id.tvUserName);
        tvUserRole = findViewById(R.id.tvUserRole);
        btnLogout = findViewById(R.id.btnLogout);
        btnNotification = findViewById(R.id.btnNotification);

        tvTutorStudents = findViewById(R.id.tvTutorStudents);
        tvTutorEarnings = findViewById(R.id.tvTutorEarnings);

        // 2. Get Tutor Info from Session
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String userEmail = prefs.getString("USER_EMAIL", "");
        String userName = prefs.getString("USER_NAME", "Tutor");

        tvUserName.setText("Hello, " + userName + "!");
        tvUserRole.setText("Tutor Dashboard");

        // Fetch Tutor ID using the helper method
        Cursor c = dbHelper.getUserByEmail(userEmail);
        if (c != null && c.moveToFirst()) {
            tutorId = c.getInt(c.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID));
            c.close();
        }

        // 3. Button Click Listeners (Asli Logic)

        // Find Students (Requests available for everyone)
        cardFindStudents.setOnClickListener(v -> {
            startActivity(new Intent(TutorHomeActivity.this, RequestsListActivity.class));
        });

        // My Students (Accepted only)
        cardMyStudents.setOnClickListener(v -> {
            startActivity(new Intent(TutorHomeActivity.this, TutorStudentsActivity.class));
        });

        // Earnings View
        cardEarnings.setOnClickListener(v -> {
            startActivity(new Intent(TutorHomeActivity.this, TutorEarningsActivity.class));
        });

        // Schedule (Optional feature)
        if (cardMySchedule != null) {
            cardMySchedule.setOnClickListener(v -> {
                Toast.makeText(this, "Schedule feature coming soon!", Toast.LENGTH_SHORT).show();
            });
        }

        // Notifications
        btnNotification.setOnClickListener(v -> {
            Toast.makeText(this, "No new notifications", Toast.LENGTH_SHORT).show();
        });

        // Logout Logic
        btnLogout.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(TutorHomeActivity.this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        });

        // 4. Bottom Navigation Logic
        bottomNav.setSelectedItemId(R.id.nav_home);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_tutors) {
                startActivity(new Intent(this, RequestsListActivity.class));
                return true;
            } else if (id == R.id.nav_profile) {
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            } else if (id == R.id.nav_attendance || id == R.id.nav_messages) {
                Toast.makeText(this, "Feature coming soon!", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        });

        // Initial Stats Load
        updateTutorStats();
    }

    // Har baar screen par wapas aane par stats refresh honge
    @Override
    protected void onResume() {
        super.onResume();
        updateTutorStats();
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_home);
        }
    }

    private void updateTutorStats() {
        if (tutorId == -1) return;

        // Use high-performance helper methods from DatabaseHelper
        int studentCount = dbHelper.getTutorStudentCount(tutorId);
        int totalEarnings = dbHelper.getTutorTotalEarnings(tutorId);

        // UI Update
        if (tvTutorStudents != null) {
            tvTutorStudents.setText(String.valueOf(studentCount));
        }

        if (tvTutorEarnings != null) {
            tvTutorEarnings.setText("PKR " + totalEarnings);
        }
    }
}