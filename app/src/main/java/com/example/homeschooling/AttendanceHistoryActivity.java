package com.example.homeschooling;


import android.os.Bundle;

import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AttendanceHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    private String parentName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_requests);

        recyclerView = findViewById(R.id.recyclerViewMyRequests);

        parentName = getIntent().getStringExtra("PARENT_NAME");

        if (parentName == null) parentName = "Guest";

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        showPlaceholderMessage();
    }

    private void showPlaceholderMessage() {

        PlaceholderAdapter adapter = new PlaceholderAdapter("Attendance history will be available in the final project.");
        recyclerView.setAdapter(adapter);
    }


    public static class PlaceholderAdapter extends RecyclerView.Adapter<PlaceholderAdapter.ViewHolder> {
        private String message;

        public PlaceholderAdapter(String message) {
            this.message = message;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            TextView textView = new TextView(parent.getContext());
            textView.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setPadding(32, 32, 32, 32);
            textView.setTextSize(16);
            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            return new ViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.textView.setText(message);
        }

        @Override
        public int getItemCount() {
            return 1;
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public ViewHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView;
            }
        }
    }
}