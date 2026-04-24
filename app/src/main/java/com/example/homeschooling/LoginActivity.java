package com.example.homeschooling;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private TextView btnSignup;
    private RadioGroup roleGroup;
    private RadioButton rbParent, rbTutor;

    private DatabaseHelper dbHelper;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);
        sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        roleGroup = findViewById(R.id.roleGroup);
        rbParent = findViewById(R.id.rbParent);
        rbTutor = findViewById(R.id.rbTutor);

        roleGroup.check(R.id.rbParent);

        btnLogin.setOnClickListener(v -> performLogin());
        btnSignup.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });
    }

    private void performLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        int selectedRoleId = roleGroup.getCheckedRadioButtonId();
        String selectedRole = (selectedRoleId == R.id.rbParent) ? "Parent" : "Tutor";

        Cursor cursor = dbHelper.checkUser(email, password);
        if (cursor != null && cursor.moveToFirst()) {

            int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_NAME);
            int roleIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_ROLE);

            if (nameIndex == -1 || roleIndex == -1) {
                Toast.makeText(this, "Database error: Columns not found.", Toast.LENGTH_LONG).show();
                cursor.close();
                return;
            }

            String name = cursor.getString(nameIndex);
            String dbRole = cursor.getString(roleIndex);
            cursor.close();

            if (!selectedRole.equals(dbRole)) {
                Toast.makeText(this, "Invalid Role! You are registered as " + dbRole, Toast.LENGTH_LONG).show();
                return;
            }

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("USER_EMAIL", email);
            editor.putString("USER_NAME", name);
            editor.putString("USER_ROLE", dbRole);
            editor.apply();

            Toast.makeText(LoginActivity.this, "Welcome, " + name, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> {
                Intent intent;
                if ("Tutor".equals(dbRole)) {
                    intent = new Intent(LoginActivity.this, TutorHomeActivity.class);
                } else {
                    intent = new Intent(LoginActivity.this, ParentHomeActivity.class);
                }
                intent.putExtra("USER_NAME", name);
                intent.putExtra("USER_ROLE", dbRole);
                startActivity(intent);
                finish();
            }, 500);

        } else {
            if (cursor != null) cursor.close();
            Toast.makeText(this, "Invalid Email or Password", Toast.LENGTH_LONG).show();
        }
    }
}