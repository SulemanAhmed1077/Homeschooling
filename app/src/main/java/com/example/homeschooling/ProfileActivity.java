package com.example.homeschooling;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileActivity extends AppCompatActivity {

    private TextInputLayout tilFullName, tilPhone, tilCity, tilBio;
    private TextInputEditText etFullName, etPhone, etCity, etBio, etSubjectName;
    private ChipGroup chipGroupSubjects;
    private Button btnSaveChanges, btnAddSubject, btnLogout;
    private BottomNavigationView bottomNav;

    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;
    private String currentUserEmail = "guest@test.com";
    private String currentUserRole = "Parent";
    private int currentUserId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tilFullName = findViewById(R.id.tilFullName);
        tilPhone = findViewById(R.id.tilPhone);
        tilCity = findViewById(R.id.tilCity);
        tilBio = findViewById(R.id.tilBio);
        etFullName = findViewById(R.id.etFullName);
        etPhone = findViewById(R.id.etPhone);
        etCity = findViewById(R.id.etCity);
        etBio = findViewById(R.id.etBio);
        etSubjectName = findViewById(R.id.etSubjectName);
        chipGroupSubjects = findViewById(R.id.chipGroupSubjects);
        btnSaveChanges = findViewById(R.id.btnSave);
        btnAddSubject = findViewById(R.id.btnAddSubject);
        btnLogout = findViewById(R.id.btnLogout);
        bottomNav = findViewById(R.id.bottom_nav);

        dbHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);

        currentUserEmail = sharedPreferences.getString("USER_EMAIL", "guest@test.com");
        currentUserRole = sharedPreferences.getString("USER_ROLE", "Parent");

        loadProfileData();

        btnSaveChanges.setOnClickListener(v -> saveProfile());
        btnAddSubject.setOnClickListener(v -> addSubjectFromInput());

        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Toast.makeText(ProfileActivity.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            });
        }

        // --- BOTTOM NAVIGATION LOGIC ---
        if (bottomNav != null) {
            bottomNav.setSelectedItemId(R.id.nav_profile);
            bottomNav.setOnItemSelectedListener(item -> {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    Intent intent;
                    if ("Tutor".equals(currentUserRole)) {
                        intent = new Intent(ProfileActivity.this, TutorHomeActivity.class);
                    } else {
                        intent = new Intent(ProfileActivity.this, ParentHomeActivity.class);
                    }
                    startActivity(intent);
                    finish();
                } else if (id == R.id.nav_tutors) {
                    Intent intent = new Intent(ProfileActivity.this, SearchTutorsActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.nav_messages) {
                    Toast.makeText(this, "Messages clicked", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_attendance) {
                    Toast.makeText(this, "Attendance clicked", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.nav_profile) {
                    // Already on profile screen
                }
                return true;
            });
        }
    }

    private void loadProfileData() {
        android.database.Cursor userCursor = dbHelper.getUserByEmail(currentUserEmail);
        if (userCursor != null && userCursor.moveToFirst()) {
            currentUserId = userCursor.getInt(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID));
            etFullName.setText(userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_NAME)));
            etPhone.setText(userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PHONE)));
            etCity.setText(userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_CITY)));
            userCursor.close();
        }

        if ("Tutor".equals(currentUserRole)) {
            etBio.setHint("Teaching experience...");
            android.database.Cursor tCursor = dbHelper.getTutorProfile(currentUserId);
            if(tCursor != null && tCursor.moveToFirst()) {
                String exp = tCursor.getString(tCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TUTOR_EXPERIENCE));
                String subjects = tCursor.getString(tCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TUTOR_SUBJECTS));
                if(exp != null) etBio.setText(exp);
                if(subjects != null && !subjects.isEmpty()) {
                    String[] subArray = subjects.split(",");
                    for(String s : subArray) {
                        addSubjectChip(s.trim());
                    }
                }
                tCursor.close();
            }
        } else {
            etBio.setHint("Child's Name...");
            android.database.Cursor pCursor = dbHelper.getParentProfile(currentUserId);
            if(pCursor != null && pCursor.moveToFirst()) {
                String childName = pCursor.getString(pCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PARENT_CHILD_NAME));
                String subjects = pCursor.getString(pCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PARENT_SUBJECTS));
                if(childName != null) etBio.setText(childName);
                if(subjects != null && !subjects.isEmpty()) {
                    String[] subArray = subjects.split(",");
                    for(String s : subArray) {
                        addSubjectChip(s.trim());
                    }
                }
                pCursor.close();
            }
        }
    }

    private void saveProfile() {
        String name = etFullName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String city = etCity.getText().toString().trim();
        String bio = etBio.getText().toString().trim();

        if (name.isEmpty()) {
            tilFullName.setError("Name cannot be empty");
            return;
        }
        if (phone.isEmpty()) {
            tilPhone.setError("Phone cannot be empty");
            return;
        }

        dbHelper.updateUser(currentUserId, name, phone, city);

        StringBuilder subjectsBuilder = new StringBuilder();
        for (int i = 0; i < chipGroupSubjects.getChildCount(); i++) {
            Chip chip = (Chip) chipGroupSubjects.getChildAt(i);
            subjectsBuilder.append(chip.getText().toString());
            if (i < chipGroupSubjects.getChildCount() - 1) {
                subjectsBuilder.append(", ");
            }
        }
        String subjects = subjectsBuilder.toString();

        if ("Tutor".equals(currentUserRole)) {
            dbHelper.addOrUpdateTutorProfile(currentUserId, subjects, "", 0, bio, "");
        } else {
            dbHelper.addOrUpdateParentProfile(currentUserId, bio, "", subjects, 0);
        }

        Toast.makeText(this, "Profile Saved Successfully!", Toast.LENGTH_SHORT).show();
    }

    private void addSubjectFromInput() {
        String subjectText = etSubjectName.getText().toString().trim();
        if (!subjectText.isEmpty()) {
            addSubjectChip(subjectText);
            etSubjectName.setText("");
        }
    }

    public void addSubjectChip(String subjectText) {
        if (!subjectText.isEmpty()) {
            Chip chip = new Chip(this);
            chip.setText(subjectText);
            chip.setCloseIconVisible(true);
            chip.setCheckable(false);
            chip.setClickable(true);
            chip.setChipBackgroundColorResource(android.R.color.white);
            chip.setChipStrokeColorResource(android.R.color.darker_gray);
            chip.setChipStrokeWidth(1);

            chip.setOnCloseIconClickListener(v -> {
                chipGroupSubjects.removeView(chip);
            });
            chipGroupSubjects.addView(chip);
        }
    }
}