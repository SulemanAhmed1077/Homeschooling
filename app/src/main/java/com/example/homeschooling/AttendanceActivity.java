package com.example.homeschooling;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AttendanceActivity extends AppCompatActivity {

    private EditText etDate;
    private Button btnPresent, btnAbsent;
    private TextView tvChildName;

    private int reqId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);


        etDate = findViewById(R.id.etDate);
        btnPresent = findViewById(R.id.btnPresent);
        btnAbsent = findViewById(R.id.btnAbsent);
        tvChildName = findViewById(R.id.tvChildName);


        reqId = getIntent().getIntExtra("REQ_ID", -1);
        String childName = getIntent().getStringExtra("CHILD_NAME");

        tvChildName.setText("For: " + childName);


        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        etDate.setText(currentDate);


        btnPresent.setOnClickListener(v -> {
            String date = etDate.getText().toString().trim();
            if (!date.isEmpty() && reqId != -1) {


                Toast.makeText(this, "Attendance marked as Present (Prototype)", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Invalid Date", Toast.LENGTH_SHORT).show();
            }
        });


        btnAbsent.setOnClickListener(v -> {
            String date = etDate.getText().toString().trim();
            if (!date.isEmpty() && reqId != -1) {


                Toast.makeText(this, "Attendance marked as Absent (Prototype)", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Invalid Date", Toast.LENGTH_SHORT).show();
            }
        });
    }
}