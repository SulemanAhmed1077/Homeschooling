package com.example.homeschooling;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ParentHomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    CardView cardRequests, cardAttendance, cardPayment;

    TextView tvUserName, tvUserRole;
    ImageView btnLogout, btnNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_home);

        bottomNav = findViewById(R.id.bottom_nav);
        cardRequests = findViewById(R.id.cardRequests);
        cardAttendance = findViewById(R.id.cardAttendance);
        cardPayment = findViewById(R.id.cardPayment);

        tvUserName = findViewById(R.id.tvUserName);
        tvUserRole = findViewById(R.id.tvUserRole);
        btnLogout = findViewById(R.id.btnLogout);
        btnNotification = findViewById(R.id.btnNotification);

        // --- SET USER DATA ---
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("USER_NAME")) {
            String name = intent.getStringExtra("USER_NAME");
            tvUserName.setText(name);
            tvUserRole.setText("Parent Account");
        }

        cardRequests.setOnClickListener(v -> showMessage("View Requests"));
        cardAttendance.setOnClickListener(v -> showMessage("Mark Attendance"));
        cardPayment.setOnClickListener(v -> showMessage("Payment"));

        btnNotification.setOnClickListener(v -> showMessage("Notifications"));

        btnLogout.setOnClickListener(v -> {
            Intent i = new Intent(ParentHomeActivity.this, LoginActivity.class);
            startActivity(i);
            finish();
        });

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                // Home
            }
            else if (id == R.id.nav_tutors) {
                showMessage("Find Tutors clicked");
            }
            else if (id == R.id.nav_messages) {
                showMessage("Messages clicked");
            }
            else if (id == R.id.nav_attendance) {
                showMessage("Attendance clicked");
            }
            else if (id == R.id.nav_profile) {
                Intent i = new Intent(ParentHomeActivity.this, ProfileActivity.class);
                startActivity(i);
            }
            return true;
        });
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}