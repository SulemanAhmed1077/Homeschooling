package com.example.homeschooling;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TuitionFormActivity extends AppCompatActivity {

    private EditText etName, etClass, etSubjects, etFee, etDays;
    private Button btnCreate;
    private DatabaseHelper dbHelper;
    private int parentId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tuition_form);

        // Views Initialize karna
        dbHelper = new DatabaseHelper(this);
        etName = findViewById(R.id.etName);
        etClass = findViewById(R.id.etClass);
        etSubjects = findViewById(R.id.etSubjects);
        etFee = findViewById(R.id.etFee);
        etDays = findViewById(R.id.etDays);
        btnCreate = findViewById(R.id.btnCreate);

        // Login kiye hue User ki ID database se nikalna
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        String userEmail = prefs.getString("USER_EMAIL", "");

        Cursor cursor = dbHelper.getUserByEmail(userEmail);
        if (cursor != null && cursor.moveToFirst()) {
            parentId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_USER_ID));
            cursor.close();

            // Parent ki profile se data form mein auto-fill karna
            loadParentData(parentId);
        }

        // Button par click event
        btnCreate.setOnClickListener(v -> saveTuitionRequest());
    }

    private void loadParentData(int pId) {
        Cursor profileCursor = dbHelper.getParentProfile(pId);
        if (profileCursor != null && profileCursor.moveToFirst()) {

            String childName = profileCursor.getString(profileCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PARENT_CHILD_NAME));
            String childClass = profileCursor.getString(profileCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PARENT_CHILD_CLASS));
            String subjects = profileCursor.getString(profileCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PARENT_SUBJECTS));
            int budget = profileCursor.getInt(profileCursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PARENT_BUDGET));

            // Agar data pehle se save hai toh usay TextBoxes mein likh do
            if (childName != null && !childName.isEmpty()) etName.setText(childName);
            if (childClass != null && !childClass.isEmpty()) etClass.setText(childClass);
            if (subjects != null && !subjects.isEmpty()) etSubjects.setText(subjects);
            if (budget > 0) etFee.setText(String.valueOf(budget));

            profileCursor.close();
        }
    }

    private void saveTuitionRequest() {
        String name = etName.getText().toString().trim();
        String className = etClass.getText().toString().trim();
        String subjects = etSubjects.getText().toString().trim();
        String feeStr = etFee.getText().toString().trim();
        String daysStr = etDays.getText().toString().trim();

        // Validation: Koi field khali na ho
        if (name.isEmpty() || className.isEmpty() || subjects.isEmpty() || feeStr.isEmpty() || daysStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int fee = Integer.parseInt(feeStr);
        int days = Integer.parseInt(daysStr);

        // Database mein Save karna
        if (parentId != -1) {
            long result = dbHelper.createTuitionRequest(parentId, name, className, subjects, fee, days);

            if (result != -1) {
                Toast.makeText(this, "Request Posted Successfully!", Toast.LENGTH_LONG).show();
                finish(); // Request save hone ke baad wapas pichli screen par le jaye
            } else {
                Toast.makeText(this, "Error posting request", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Session Expired! Please login again.", Toast.LENGTH_SHORT).show();
        }
    }
}