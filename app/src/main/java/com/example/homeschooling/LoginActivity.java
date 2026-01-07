package com.example.homeschooling;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    // Views - Containers
    private LinearLayout loginContainer, signupContainer;
    private TabLayout tabLayout;

    // Views - Login
    private TextInputEditText edtLoginEmail, edtLoginPassword;
    private Button btnLogin;

    // Views - Signup
    private TextInputEditText edtName, edtSignupEmail, edtSignupPassword;

    // Card Views
    private CardView cardParent, cardTutor;

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

        // Initialize Containers & Tabs
        loginContainer = findViewById(R.id.loginContainer);
        signupContainer = findViewById(R.id.signupContainer);
        tabLayout = findViewById(R.id.tabLayout);

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

        // --- 1. TAB SWITCHING LOGIC ---
        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Sign Up"));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    loginContainer.setVisibility(View.VISIBLE);
                    signupContainer.setVisibility(View.GONE);
                } else {
                    loginContainer.setVisibility(View.GONE);
                    signupContainer.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // --- 2. CARD CLICK LOGIC ---
        cardParent.setOnClickListener(v -> {
            rbParent.setChecked(true);
            rbTutor.setChecked(false);
        });

        cardTutor.setOnClickListener(v -> {
            rbTutor.setChecked(true);
            rbParent.setChecked(false);
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

    private void performLogin(String email, String password) {
        btnLogin.setEnabled(false);
        btnLogin.setText("Logging in...");

        new Handler().postDelayed(() -> {
            btnLogin.setEnabled(true);
            btnLogin.setText("Login");

            if (email.equals(ADMIN_EMAIL) && password.equals(ADMIN_PASSWORD)) {
                Toast.makeText(LoginActivity.this, "Admin Login: Dashboard Coming Soon", Toast.LENGTH_LONG).show();
                return;
            }
            else if (email.equals(PARENT_EMAIL) && password.equals(PARENT_PASSWORD)) {
                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, ParentHomeActivity.class);
                // Naam bheja ja raha hai Parent ko
                intent.putExtra("USER_NAME", "Ali");
                startActivity(intent);
                finish();
            }
            else if (email.equals(TUTOR_EMAIL) && password.equals(TUTOR_PASSWORD)) {
                Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this, TutorHomeActivity.class);
                // Naam bheja ja raha hai Tutor ko
                intent.putExtra("USER_NAME", "Suleman");
                startActivity(intent);
                finish();
            }
            else {
                Toast.makeText(LoginActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
            }

        }, 2000);
    }
}