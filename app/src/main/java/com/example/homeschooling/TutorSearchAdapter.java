package com.example.homeschooling;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TutorSearchAdapter extends RecyclerView.Adapter<TutorSearchAdapter.TutorViewHolder> {

    private Cursor cursor;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(String tutorEmail, String tutorName);
    }

    public TutorSearchAdapter(Cursor cursor, OnItemClickListener listener) {
        this.cursor = cursor;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TutorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tutor_search, parent, false);
        return new TutorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TutorViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) {
            return;
        }

        int nameIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_NAME);
        String name = (nameIndex != -1) ? cursor.getString(nameIndex) : "N/A";

        int subjectsIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TUTOR_SUBJECTS);
        String subjects = (subjectsIndex != -1) ? cursor.getString(subjectsIndex) : "N/A";

        int feeIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_TUTOR_HOURLY_FEE);
        String fee = (feeIndex != -1) ? cursor.getString(feeIndex) : "0";

        int cityIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_CITY);
        String city = (cityIndex != -1) ? cursor.getString(cityIndex) : "N/A";

        int emailIndex = cursor.getColumnIndex(DatabaseHelper.COLUMN_USER_EMAIL);
        String email = (emailIndex != -1) ? cursor.getString(emailIndex) : "no-email@test.com";

        holder.tvName.setText(name);
        holder.tvSubjects.setText(subjects);
        holder.tvFee.setText("Fee: PKR " + fee + "/hr");
        holder.tvCity.setText("City: " + city);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(email, name);
            }
        });
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

    public static class TutorViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvSubjects, tvFee, tvCity;

        public TutorViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvTutorName);
            tvSubjects = itemView.findViewById(R.id.tvTutorSubjects);
            tvFee = itemView.findViewById(R.id.tvTutorFee);
            tvCity = itemView.findViewById(R.id.tvTutorCity);
        }
    }
}