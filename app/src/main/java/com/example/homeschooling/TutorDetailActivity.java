package com.example.homeschooling;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TutorDetailActivity extends AppCompatActivity {

    private TextView tvName, tvPhone, tvCity, tvSubjects, tvClassLevels, tvFee, tvExperience, tvAvailability;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_detail);

        dbHelper = new DatabaseHelper(this);

        tvName = findViewById(R.id.tvDetailName);
        tvPhone = findViewById(R.id.tvDetailPhone);
        tvCity = findViewById(R.id.tvDetailCity);
        tvSubjects = findViewById(R.id.tvDetailSubjects);
        tvClassLevels = findViewById(R.id.tvDetailClassLevels);
        tvFee = findViewById(R.id.tvDetailFee);
        tvExperience = findViewById(R.id.tvDetailExperience);
        tvAvailability = findViewById(R.id.tvDetailAvailability);

        String tutorEmail = getIntent().getStringExtra("TUTOR_EMAIL");

        if (tutorEmail != null && !tutorEmail.isEmpty()) {
            loadTutorDetails(tutorEmail);
        } else {
            Toast.makeText(this, "Error loading tutor details", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void loadTutorDetails(String email) {
        Cursor userCursor = dbHelper.getUserByEmail(email);
        if (userCursor != null && userCursor.moveToFirst()) {
            int userId = userCursor.getInt(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID));
            tvName.setText("Name: " + userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_NAME)));
            tvPhone.setText("Phone: " + userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_PHONE)));
            tvCity.setText("City: " + userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_CITY)));

            userCursor.close();

            Cursor tutorCursor = dbHelper.getTutorProfile(userId);
            if (tutorCursor != null && tutorCursor.moveToFirst()) {
                tvSubjects.setText("Subjects: " + tutorCursor.getString(tutorCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TUTOR_SUBJECTS)));
                tvClassLevels.setText("Classes: " + tutorCursor.getString(tutorCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TUTOR_CLASS_LEVELS)));
                tvFee.setText("Fee: PKR " + tutorCursor.getInt(tutorCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TUTOR_HOURLY_FEE)) + " / hr");

//                String exp = tutorCursor.getString(tutorCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TUTOR_EXPERIENCE));
//                tvExperience.setText("Experience: " + (exp != null ? exp : "N/A"));

                String avail = tutorCursor.getString(tutorCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_TUTOR_AVAILABILITY));
                tvAvailability.setText("Availability: " + (avail != null ? avail : "N/A"));

                tutorCursor.close();
            }
        }
    }
}