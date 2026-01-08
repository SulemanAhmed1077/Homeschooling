package com.example.homeschooling;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;

public class ParentHomeActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    CardView cardRequests, cardAttendance, cardPayment;
    CardView cardPostRequest; // Post Request wala card

    TextView tvUserName, tvUserRole;
    ImageView btnLogout, btnNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_home);

        // --- IDs DHUNDEIN ---
        bottomNav = findViewById(R.id.bottom_nav);
        cardRequests = findViewById(R.id.cardRequests);
        cardAttendance = findViewById(R.id.cardAttendance);
        cardPayment = findViewById(R.id.cardPayment);
        cardPostRequest = findViewById(R.id.cardPostRequest); // Ye add kiya

        tvUserName = findViewById(R.id.tvUserName);
        tvUserRole = findViewById(R.id.tvUserRole);
        btnLogout = findViewById(R.id.btnLogout);
        btnNotification = findViewById(R.id.btnNotification);

        // --- SET USER DATA ---
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("USER_NAME")) {
            String name = intent.getStringExtra("USER_NAME");

            if (name != null && !name.isEmpty()) {
                tvUserName.setText("Welcome, " + name);
            } else {
                tvUserName.setText("Welcome");
            }
            tvUserRole.setText("Parent Account");
        }

        // --- CLICK LISTENERS ---

        // Post Request Button -> Form Kholega
        cardPostRequest.setOnClickListener(v -> openTuitionForm());

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

    // --- TUITION FORM OPEN KARNE KA METHOD ---
    private void openTuitionForm() {
        BottomSheetDialog bottomSheet = new BottomSheetDialog(ParentHomeActivity.this);

        // Step 2 mein banayi gayi 'tuition_form.xml' load karo
        View sheetView = LayoutInflater.from(ParentHomeActivity.this).inflate(R.layout.tuition_form, null);

        bottomSheet.setContentView(sheetView);
        bottomSheet.show();

        // --- FORM KE ANDAR KE SARE FIELDS LAO ---
        Button btnCreate = sheetView.findViewById(R.id.btnCreate);
        EditText etName = sheetView.findViewById(R.id.etName);
        EditText etClass = sheetView.findViewById(R.id.etClass);
        EditText etSubjects = sheetView.findViewById(R.id.etSubjects);
        EditText etTimings = sheetView.findViewById(R.id.etTimings); // Timings field connect kiya
        EditText etFee = sheetView.findViewById(R.id.etFee);

        // Create Button Par Click Logic
        btnCreate.setOnClickListener(view -> {
            // Data lo inputs se
            String name = etName.getText().toString().trim();
            String className = etClass.getText().toString().trim();
            String subjects = etSubjects.getText().toString().trim();
            String timings = etTimings.getText().toString().trim();
            String fee = etFee.getText().toString().trim();

            // Validation check (Sab bharna hai?)
            if (name.isEmpty() || className.isEmpty() || subjects.isEmpty() || fee.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show();
            } else {
                // Agar sab sahi hai to Message dikhao aur Band kar do
                Toast.makeText(this, "Request Posted!\nFee: ₹" + fee, Toast.LENGTH_SHORT).show();
                bottomSheet.dismiss();
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}