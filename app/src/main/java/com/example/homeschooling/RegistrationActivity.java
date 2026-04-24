package com.example.homeschooling;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class RegistrationActivity extends AppCompatActivity {

    private RadioGroup roleGroup;
    private ViewFlipper viewFlipper;
    private Button btnRegister;
    private TextView tabLogin;
    private Button btnUploadCV;
    private TextView tvCvStatus;
    private String selectedCvUri = "";

    private TextInputEditText etRegName, etRegEmail, etRegPassword;

    private TextInputEditText etParentPhone, etParentCity, etChildName, etChildClass, etSubjectsNeeded, etBudget;

    private TextInputEditText etTutorPhone, etTutorCity, etTutorSubjects, etClassLevels, etHourlyFee, etExperience, etAvailability;

    private DatabaseHelper dbHelper;
    private ActivityResultLauncher<Intent> cvPickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        dbHelper = new DatabaseHelper(this);

        initializeViews();

        if (tabLogin != null) {
            tabLogin.setOnClickListener(v -> {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            });
        }

        cvPickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        selectedCvUri = uri.toString();
                        if (tvCvStatus != null) {
                            tvCvStatus.setText("CV Selected Successfully");
                            tvCvStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                        }
                    }
                });

        if (btnUploadCV != null) {
            btnUploadCV.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                cvPickerLauncher.launch(intent);
            });
        }

        roleGroup.check(R.id.rbParent);
        viewFlipper.setDisplayedChild(0);

        roleGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbParent) {
                viewFlipper.setDisplayedChild(0);
            } else if (checkedId == R.id.rbTutor) {
                viewFlipper.setDisplayedChild(1);
            }
        });

        btnRegister.setOnClickListener(v -> performRegistration());
    }

    private void initializeViews() {
        roleGroup = findViewById(R.id.roleGroup);
        viewFlipper = findViewById(R.id.viewFlipper);
        btnRegister = findViewById(R.id.btnRegister);
        tabLogin = findViewById(R.id.tabLogin);

        btnUploadCV = findViewById(R.id.btnUploadCV);
        tvCvStatus = findViewById(R.id.tvCvStatus);

        etRegName = findViewById(R.id.etRegName);
        etRegEmail = findViewById(R.id.etRegEmail);
        etRegPassword = findViewById(R.id.etRegPassword);

        etParentPhone = findViewById(R.id.etParentPhone);
        etParentCity = findViewById(R.id.etParentCity);
        etChildName = findViewById(R.id.etChildName);
        etChildClass = findViewById(R.id.etChildClass);
        etSubjectsNeeded = findViewById(R.id.etSubjectsNeeded);
        etBudget = findViewById(R.id.etBudget);

        etTutorPhone = findViewById(R.id.etTutorPhone);
        etTutorCity = findViewById(R.id.etTutorCity);
        etTutorSubjects = findViewById(R.id.etTutorSubjects);
        etClassLevels = findViewById(R.id.etClassLevels);
        etHourlyFee = findViewById(R.id.etHourlyFee);
        etExperience = findViewById(R.id.etExperience);
        etAvailability = findViewById(R.id.etAvailability);
    }

    private void performRegistration() {
        String name = etRegName != null ? etRegName.getText().toString().trim() : "";
        String email = etRegEmail != null ? etRegEmail.getText().toString().trim() : "";
        String password = etRegPassword != null ? etRegPassword.getText().toString().trim() : "";

        int selectedRoleId = roleGroup.getCheckedRadioButtonId();
        String role = (selectedRoleId == R.id.rbParent) ? "Parent" : "Tutor";

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Name, Email, and Password are required", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
            return;
        }

        String phone = "", city = "";

        if ("Parent".equals(role)) {
            if (etParentPhone != null) phone = etParentPhone.getText().toString().trim();
            if (etParentCity != null) city = etParentCity.getText().toString().trim();
        } else {
            if (etTutorPhone != null) phone = etTutorPhone.getText().toString().trim();
            if (etTutorCity != null) city = etTutorCity.getText().toString().trim();

            if (selectedCvUri.isEmpty() && btnUploadCV != null) {
                Toast.makeText(this, "Please upload your CV (PDF format)", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (phone.isEmpty() || city.isEmpty()) {
            Toast.makeText(this, "Phone and City are required", Toast.LENGTH_SHORT).show();
            return;
        }

        long userId = dbHelper.addUser(name, email, password, phone, city, role);

        if (userId == -1) {
            Toast.makeText(this, "Registration Failed! Email might already exist.", Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(this, "Registration Successful! Please log in.", Toast.LENGTH_LONG).show();

        Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}