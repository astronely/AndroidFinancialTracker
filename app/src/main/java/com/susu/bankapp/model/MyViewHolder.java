package com.susu.bankapp.model;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.susu.bankapp.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyViewHolder extends RecyclerView.ViewHolder {
    private final View mView;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setType(String type) {
        TextView typeTextView = mView.findViewById(R.id.expense_type_text);
        typeTextView.setText(type);
    }

    public void setSum(double sum) {
        TextView sumTextView = mView.findViewById(R.id.expense_sum_text);
        String finalSum = String.format(Locale.getDefault(), "%.2f ₽", sum);
        sumTextView.setText(finalSum);
    }

    public void setDate(long date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String formattedDate = sdf.format(new Date(date));
        TextView dateTextView = mView.findViewById(R.id.expense_date_text);
        dateTextView.setText(formattedDate);
    }
}