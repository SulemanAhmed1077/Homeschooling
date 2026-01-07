package com.example.homeschooling;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TutorHomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    CardView cardMyStudents, cardMySchedule, cardEarnings;

    TextView tvUserName, tvUserRole;
    ImageView btnLogout, btnNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_home);

        // Init Views
        bottomNav = findViewById(R.id.bottom_nav);
        cardMyStudents = findViewById(R.id.cardMyStudents);
        cardMySchedule = findViewById(R.id.cardMySchedule);
        cardEarnings = findViewById(R.id.cardEarnings);

        tvUserName = findViewById(R.id.tvUserName);
        tvUserRole = findViewById(R.id.tvUserRole);
        btnLogout = findViewById(R.id.btnLogout);
        btnNotification = findViewById(R.id.btnNotification);

        // --- 1. SET USER DATA ---
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("USER_NAME")) {
            String name = intent.getStringExtra("USER_NAME");
            tvUserName.setText("Hello, " + name + "!");
            tvUserRole.setText("Tutor"); // Tutor user hai
        }

        // --- 2. BUTTON CLICKS ---
        cardMyStudents.setOnClickListener(v -> showMessage("View My Students"));
        cardMySchedule.setOnClickListener(v -> showMessage("View My Schedule"));
        cardEarnings.setOnClickListener(v -> showMessage("View My Earnings"));

        btnNotification.setOnClickListener(v -> showMessage("Notifications (You have a new request)"));

        btnLogout.setOnClickListener(v -> {
            Intent i = new Intent(TutorHomeActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        });

        // --- 3. BOTTOM NAV LOGIC ---
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // Home
            }
            else if (id == R.id.nav_tutors) {
                showMessage("Find Students clicked");
            }
            else if (id == R.id.nav_messages) {
                showMessage("Messages clicked");
            }
            else if (id == R.id.nav_attendance) {
                showMessage("Attendance clicked");
            }
            else if (id == R.id.nav_profile) {
                // Profile Activity mein ja sakte hain
                Intent i = new Intent(TutorHomeActivity.this, ProfileActivity.class);
                startActivity(i);
            }
            return true;
        });
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}