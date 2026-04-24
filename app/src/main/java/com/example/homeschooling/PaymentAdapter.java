package com.example.homeschooling;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PaymentAdapter extends RecyclerView.Adapter<PaymentAdapter.ViewHolder> {

    private Context context;
    private Cursor cursor;
    private String userRole; // "Parent" ya "Tutor"

    public PaymentAdapter(Context context, Cursor cursor, String userRole) {
        this.context = context;
        this.cursor = cursor;
        this.userRole = userRole;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // item_payment.xml layout file use ho rahi hai
        View view = LayoutInflater.from(context).inflate(R.layout.item_payment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) return;

        // Data extraction
        final int escrowId = cursor.getInt(cursor.getColumnIndexOrThrow("escrow_id"));
        String childName = cursor.getString(cursor.getColumnIndexOrThrow("req_child_name"));
        int totalFee = cursor.getInt(cursor.getColumnIndexOrThrow("total_fee"));
        int payableAmount = cursor.getInt(cursor.getColumnIndexOrThrow("payable_amount"));
        String status = cursor.getString(cursor.getColumnIndexOrThrow("escrow_status"));

        holder.tvPayChild.setText("Tuition: " + childName);
        holder.tvPayTotalFee.setText("Monthly Agreement: PKR " + totalFee);
        holder.tvPayPayable.setText("Current Escrow Balance: PKR " + payableAmount);
        holder.tvPayStatus.setText("Status: " + status);

        // Role-based logic
        if ("Tutor".equalsIgnoreCase(userRole)) {
            holder.btnReleasePayment.setVisibility(View.GONE); // Tutor payment release nahi kar sakta
        } else {
            holder.btnReleasePayment.setVisibility(View.VISIBLE);

            // Agar pehle hi release ho chuki hai
            if ("Released".equalsIgnoreCase(status)) {
                holder.btnReleasePayment.setEnabled(false);
                holder.btnReleasePayment.setText("Payment Released");
            }

            holder.btnReleasePayment.setOnClickListener(v -> {
                Toast.makeText(context, "Processing Payment Release...", Toast.LENGTH_SHORT).show();
                // Simulation: Asli project mein yahan payment gateway API aati hai
                Toast.makeText(context, "PKR " + payableAmount + " released to Tutor!", Toast.LENGTH_LONG).show();
                holder.btnReleasePayment.setEnabled(false);
                holder.btnReleasePayment.setText("Done");
            });
        }
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPayChild, tvPayTotalFee, tvPayPayable, tvPayStatus;
        Button btnReleasePayment;

        public ViewHolder(View itemView) {
            super(itemView);
            tvPayChild = itemView.findViewById(R.id.tvPayChild);
            tvPayTotalFee = itemView.findViewById(R.id.tvPayTotalFee);
            tvPayPayable = itemView.findViewById(R.id.tvPayPayable);
            tvPayStatus = itemView.findViewById(R.id.tvPayStatus);
            btnReleasePayment = itemView.findViewById(R.id.btnReleasePayment);
        }
    }
}