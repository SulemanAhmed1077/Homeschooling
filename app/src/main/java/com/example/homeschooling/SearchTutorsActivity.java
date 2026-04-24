package com.example.homeschooling;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputEditText;

public class SearchTutorsActivity extends AppCompatActivity implements TutorSearchAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private TutorSearchAdapter adapter;
    private DatabaseHelper dbHelper;
    private TextInputEditText etSubject, etClass, etCity;
    private Button btnSearch;
    private BottomNavigationView bottomNav;

    private SharedPreferences sharedPreferences;
    private String currentUserRole = "Parent";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_tutors);

        dbHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        currentUserRole = sharedPreferences.getString("USER_ROLE", "Parent");

        recyclerView = findViewById(R.id.recyclerViewTutors);
        etSubject = findViewById(R.id.etSearchSubject);
        etClass = findViewById(R.id.etSearchClass);
        etCity = findViewById(R.id.etSearchCity);
        btnSearch = findViewById(R.id.btnSearch);
        bottomNav = findViewById(R.id.bottom_nav);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        btnSearch.setOnClickListener(v -> performSearch());

        performSearch();

        // --- BOTTOM NAVIGATION LOGIC ---
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_tutors);
            bottomNav.setOnItemSelectedListener(item -> {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    Intent intent;
                    if ("Tutor".equals(currentUserRole)) {
                        intent = new Intent(SearchTutorsActivity.this, TutorHomeActivity.class);
                    } else {
                        intent = new Intent(SearchTutorsActivity.this, ParentHomeActivity.class);
                    }
                    startActivity(intent);
                    finish();
                } else if (id == R.id.nav_tutors) {
                    // Already on search screen
                } else if (id == R.id.nav_messages) {
                    Toast.makeText(this, "Messages clicked", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_attendance) {
                    Toast.makeText(this, "Attendance clicked", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_profile) {
                    Intent intent = new Intent(SearchTutorsActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                }
                return true;
            });
        }
    }

    private void performSearch() {
        String subject = etSubject.getText().toString().trim();
        String classLevel = etClass.getText().toString().trim();
        String city = etCity.getText().toString().trim();

        Cursor cursor = dbHelper.searchTutors(subject, classLevel, city);

        if (cursor != null && cursor.getCount() > 0) {
            adapter = new TutorSearchAdapter(cursor, this);
            recyclerView.setAdapter(adapter);
        } else {
            if (adapter != null) {
                adapter.swapCursor(null);
            }
            Toast.makeText(this, "No tutors found matching your criteria.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(String tutorEmail, String tutorName) {
        Intent intent = new Intent(this, TutorDetailActivity.class);
        intent.putExtra("TUTOR_EMAIL", tutorEmail);
        startActivity(intent);
    }
}