package com.example.homeschooling;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RequestsAdapter extends RecyclerView.Adapter<RequestsAdapter.ViewHolder> {

    private Context context;
    private Cursor cursor;
    private DatabaseHelper dbHelper;
    private int currentTutorId;

    public RequestsAdapter(Context context, Cursor cursor, DatabaseHelper dbHelper, int currentTutorId) {
        this.context = context;
        this.cursor = cursor;
        this.dbHelper = dbHelper;
        this.currentTutorId = currentTutorId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) return;

        // 1. Data Fetching
        final int reqId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
        final int parentId = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_REQ_PARENT_ID));
        final String childName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_REQ_CHILD_NAME));
        String className = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_REQ_CLASS));
        String subjects = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_REQ_SUBJECTS));
        int fee = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_REQ_FEE));
        int days = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_REQ_DAYS));
        String status = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_REQ_STATUS));

        String parentName = "Parent";
        int parentNameIndex = cursor.getColumnIndex("parent_name");
        if (parentNameIndex != -1) {
            parentName = cursor.getString(parentNameIndex);
        }
        final String finalParentName = parentName;

        // 2. UI Binding
        holder.tvChildName.setText("Student: " + childName);
        holder.tvClass.setText("Class: " + className + " | Parent: " + parentName);
        holder.tvSubjects.setText("Subjects: " + subjects);
        holder.tvFee.setText("Fee: " + fee + " PKR");
        holder.tvDays.setText(days + " Days/Month");

        // 3. SMART LOGIC: Buttons & Chat Setting

        if ("Pending".equalsIgnoreCase(status)) {
            // Find Students Screen: Sirf Accept dikhao
            holder.btnAccept.setVisibility(View.VISIBLE);
            holder.btnMarkAttendance.setVisibility(View.GONE);
            holder.btnChat.setVisibility(View.GONE); // Pending mein chat nahi hoti

            holder.btnAccept.setOnClickListener(v -> {
                boolean success = dbHelper.acceptTuitionRequest(reqId, currentTutorId);
                if (success) {
                    Toast.makeText(context, "Tuition Accepted!", Toast.LENGTH_SHORT).show();
                    if (context instanceof RequestsListActivity) {
                        ((RequestsListActivity) context).loadRequests();
                    }
                }
            });
        }
        else if ("Accepted".equalsIgnoreCase(status)) {
            // My Students Screen: Accept chhupao, Chat dikhao
            holder.btnAccept.setVisibility(View.GONE);
            holder.btnMarkAttendance.setVisibility(View.GONE);
            holder.btnChat.setVisibility(View.VISIBLE);

            // CHAT BUTTON LOGIC
            holder.btnChat.setOnClickListener(v -> {
                Intent intent = new Intent(context, ChatActivity.class);
                // Receiver Parent hai, isliye Parent ki ID aur Name bhej rahe hain
                intent.putExtra("RECEIVER_ID", parentId);
                intent.putExtra("RECEIVER_NAME", finalParentName);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = newCursor;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvChildName, tvClass, tvSubjects, tvFee, tvDays;
        Button btnAccept, btnMarkAttendance, btnChat; // btnChat add kiya

        public ViewHolder(View itemView) {
            super(itemView);
            tvChildName = itemView.findViewById(R.id.tvChildName);
            tvClass = itemView.findViewById(R.id.tvClass);
            tvSubjects = itemView.findViewById(R.id.tvSubjects);
            tvFee = itemView.findViewById(R.id.tvFee);
            tvDays = itemView.findViewById(R.id.tvDays);
            btnAccept = itemView.findViewById(R.id.btnAcceptRequest);
            btnMarkAttendance = itemView.findViewById(R.id.btnMarkAttendance);

            // Layout mein button ki ID check kar lena 'btnChat' hi ho
            btnChat = itemView.findViewById(R.id.btnChat);
        }
    }
}