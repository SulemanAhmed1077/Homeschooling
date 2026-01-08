package com.example.homeschooling;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    // Views - Containers
    private LinearLayout loginContainer, signupContainer;

    // Views - Login
    private TextInputEditText edtLoginEmail, edtLoginPassword;
    private Button btnLogin;

    // Views - Signup
    private TextInputEditText edtName, edtSignupEmail, edtSignupPassword;

    // Card Views (Changed to MaterialCardView for setStrokeColor support)
    private MaterialCardView cardParent, cardTutor;

    // Radio Buttons
    private RadioButton rbParent, rbTutor;

    private Button btnSignup;

    // --- STATIC CREDENTIALS ---
    private static final String ADMIN_EMAIL = "admin@test.com";
    private static final String ADMIN_PASSWORD = "admin123";

    private static final String PARENT_EMAIL = "parent@test.com";
    private static final String PARENT_PASSWORD = "parent123";

    private static final String TUTOR_EMAIL = "tutor@test.com";
    private static final String TUTOR_PASSWORD = "tutor123";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Containers
        loginContainer = findViewById(R.id.loginContainer);
        signupContainer = findViewById(R.id.signupContainer);

        // Initialize New Toggle Buttons (Replacing TabLayout)
        MaterialButtonToggleGroup toggleGroup = findViewById(R.id.toggleGroup);
        MaterialButton btnTabLogin = findViewById(R.id.btnTabLogin);
        MaterialButton btnTabSignup = findViewById(R.id.btnTabSignup);

        // Initialize Login Views
        edtLoginEmail = findViewById(R.id.edtLoginEmail);
        edtLoginPassword = findViewById(R.id.edtLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);

        // Initialize Signup Views
        edtName = findViewById(R.id.edtName);
        edtSignupEmail = findViewById(R.id.edtSignupEmail);
        edtSignupPassword = findViewById(R.id.edtSignupPassword);

        // Initialize Cards and Radio Buttons
        cardParent = findViewById(R.id.cardParent);
        cardTutor = findViewById(R.id.cardTutor);
        rbParent = findViewById(R.id.rbParent);
        rbTutor = findViewById(R.id.rbTutor);

        btnSignup = findViewById(R.id.btnSignup);

        // --- 1. TOGGLE BUTTON LOGIC (Replacing TabLayout) ---
        // Initial state setup (Login selected by default)
        selectTab(btnTabLogin, btnTabSignup, true);

        // Listener for Login Button
        btnTabLogin.setOnClickListener(v -> {
            selectTab(btnTabLogin, btnTabSignup, true);
            loginContainer.setVisibility(View.VISIBLE);
            signupContainer.setVisibility(View.GONE);
        });

        // Listener for Signup Button
        btnTabSignup.setOnClickListener(v -> {
            selectTab(btnTabLogin, btnTabSignup, false);
            loginContainer.setVisibility(View.GONE);
            signupContainer.setVisibility(View.VISIBLE);
        });

        // --- 2. CARD CLICK LOGIC (Visual Selection) ---
        cardParent.setOnClickListener(v -> {
            rbParent.setChecked(true);
            updateCardSelection(true);
        });

        cardTutor.setOnClickListener(v -> {
            rbTutor.setChecked(true);
            updateCardSelection(false);
        });

        // --- 3. LOGIN BUTTON CLICK ---
        btnLogin.setOnClickListener(v -> {
            String email = edtLoginEmail.getText().toString().trim();
            String password = edtLoginPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email)) {
                edtLoginEmail.setError("Enter Email");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                edtLoginPassword.setError("Enter Password");
                return;
            }

            performLogin(email, password);
        });

        // --- 4. SIGN UP BUTTON CLICK ---
        btnSignup.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String email = edtSignupEmail.getText().toString().trim();
            String password = edtSignupPassword.getText().toString().trim();

            if (TextUtils.isEmpty(name)) {
                edtName.setError("Enter Name");
                return;
            }
            if (TextUtils.isEmpty(email)) {
                edtSignupEmail.setError("Enter Email");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                edtSignupPassword.setError("Enter Password");
                return;
            }

            String role = (rbParent.isChecked()) ? "Parent" : "Tutor";
            Toast.makeText(LoginActivity.this, "Signing up as " + role, Toast.LENGTH_SHORT).show();
        });
    }

    // Helper method to handle Button Toggle styling
    private void selectTab(MaterialButton loginBtn, MaterialButton signupBtn, boolean isLoginSelected) {
        if (isLoginSelected) {
            // Highlight Login Button
            loginBtn.setBackgroundColor(Color.parseColor("#2D3436"));
            loginBtn.setTextColor(Color.WHITE);
            // Reset Signup Button
            signupBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
            signupBtn.setTextColor(Color.parseColor("#636E72"));
        } else {
            // Highlight Signup Button
            signupBtn.setBackgroundColor(Color.parseColor("#2D3436"));
            signupBtn.setTextColor(Color.WHITE);
            // Reset Login Button
            loginBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
            loginBtn.setTextColor(Color.parseColor("#636E72"));
        }
    }

    // Helper method to update Card border color
    private void updateCardSelection(boolean isParentSelected) {
        if (isParentSelected) {
            cardParent.setStrokeColor(Color.parseColor("#2D3436")); // Dark color selected
            cardTutor.setStrokeColor(Color.parseColor("#DFE6E9"));   // Light color unselected
        } else {
            cardTutor.setStrokeColor(Color.parseColor("#2D3436"));   // Dark color selected
            cardParent.setStrokeColor(Color.parseColor("#DFE6E9"));  // Light color unselected
        }
    }

    private void performLogin(String email, String password) {
        btnLogin.setEnabled(false);
        btnLogin.setText("Logging in...");

        new Handler().postDelayed(() -> {
            btnLogin.setEnabled(true);
            btnLogin.setText("Sign In"); // Reset button text

            if (email.equals(ADMIN_EMAIL) && password.equals(ADMIN_PASSWORD)) {
                Toast.makeText(LoginActivity.this, "Admin Login: Dashboard Coming Soon", Toast.LENGTH_LONG).show();
                return;
            }
            else if (email.equals(PARENT_EMAIL) && password.equals(PARENT_PASSWORD)) {
                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                // Parent Login Logic
                Intent intent = new Intent(LoginActivity.this, ParentHomeActivity.class);
                intent.putExtra("USER_NAME", "Ali");
                intent.putExtra("USER_ROLE", "Parent Account");
                startActivity(intent);
                finish();
            }
            else if (email.equals(TUTOR_EMAIL) && password.equals(TUTOR_PASSWORD)) {
                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                // Tutor Login Logic
                Intent intent = new Intent(LoginActivity.this, TutorHomeActivity.class);
                intent.putExtra("USER_NAME", "Suleman");
                intent.putExtra("USER_ROLE", "Tutor Account");
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(LoginActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
            }

        }, 2000);
    }
}