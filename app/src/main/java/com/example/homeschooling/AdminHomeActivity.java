package com.example.homeschooling;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AdminHomeActivity extends AppCompatActivity {

    private RecyclerView rvUsers;
    private RecyclerView rvRequests;
    private DatabaseHelper dbHelper;
    private ImageView btnLogout;
    private TextView tvAdminTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        // Initialize Database Helper
        dbHelper = new DatabaseHelper(this);

        // Initialize Views
        rvUsers = findViewById(R.id.rvUsers);
        rvRequests = findViewById(R.id.rvRequests);
        btnLogout = findViewById(R.id.btnLogout);
        tvAdminTitle = findViewById(R.id.tvAdminTitle);


        tvAdminTitle.setText("Admin Dashboard");


        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        rvRequests.setLayoutManager(new LinearLayoutManager(this));


        btnLogout.setOnClickListener(v -> {

            Intent intent = new Intent(AdminHomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }


    private void loadUsers() {

        Toast.makeText(this, "User list will be available in the final project.", Toast.LENGTH_SHORT).show();
    }


    private void loadRequests() {

        Toast.makeText(this, "Request list will be available in the final project.", Toast.LENGTH_SHORT).show();
    }
}