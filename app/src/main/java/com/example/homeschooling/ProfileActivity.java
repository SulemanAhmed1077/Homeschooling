package com.example.homeschooling;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileActivity extends AppCompatActivity {

    // Variables
    private TextInputEditText etSubjectName;
    private Button btnAddSubject;
    private ChipGroup chipGroupSubjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);  // Link Profile Activity layout

        // 1. Initialize Views
        etSubjectName = findViewById(R.id.etSubjectName);
        btnAddSubject = findViewById(R.id.btnAddSubject);
        chipGroupSubjects = findViewById(R.id.chipGroupSubjects);

        // 2. Add Subject Button Logic
        btnAddSubject.setOnClickListener(view -> {
            // Get text and remove spaces
            String subjectText = etSubjectName.getText().toString().trim();

            // Check if empty
            if (!TextUtils.isEmpty(subjectText)) {

                // Create New Chip
                Chip chip = new Chip(ProfileActivity.this);
                chip.setText(subjectText);

                // Chip Styling
                chip.setCloseIconVisible(true); // Show 'X' icon
                chip.setCheckable(false);      // Disable checkbox behavior
                chip.setClickable(true);

                // Chip Background Color (White)
                chip.setChipBackgroundColorResource(android.R.color.white);
                // Chip Border/Stroke
                chip.setChipStrokeColorResource(android.R.color.darker_gray);
                chip.setChipStrokeWidth(1);

                // Handle Delete (Click on 'X')
                chip.setOnCloseIconClickListener(view1 -> {
                    chipGroupSubjects.removeView(chip);
                });

                // Add Chip to Group
                chipGroupSubjects.addView(chip);

                // Clear Input
                etSubjectName.setText("");
                etSubjectName.clearFocus();

            } else {
                // Show error if empty
                etSubjectName.setError("Please enter a subject name");
            }
        });
    }
}