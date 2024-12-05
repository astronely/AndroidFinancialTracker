package com.susu.bankapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.susu.bankapp.Model.Data;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;

public class ExpensesFragment extends Fragment {

    public ExpensesFragment() {
        // Required empty public constructor
    }

    private FirebaseAuth auth;
    private DatabaseReference expenseDatabase;
    private DatePickerDialog datePickerDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);
        FloatingActionButton actionButton = view.findViewById(R.id.action_button_expense);

        auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            Log.d("Expenses", "User exists");
            String uid = user.getUid();
            Log.d("ExpenseDatabase Info", uid);
            expenseDatabase = FirebaseDatabase.getInstance().getReference().child("ExpenseData").child(uid);
            Log.d("ExpenseDatabase Info", Objects.requireNonNull(expenseDatabase.getKey()));
        }

        actionButton.setOnClickListener(v -> {
            Log.d("Expenses", "Expense add menu opened");
            expenseDataInsert();
//            ((MainActivity) requireActivity()).addFragment(R.id.fragment_expenses_layout, new ExpenseFragment());
        });
        return view;
    }

    public void expenseDataInsert() {
        AlertDialog.Builder myDialog = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.custom_input, null);

        myDialog.setView(view);
        AlertDialog dialog = myDialog.create();


        EditText edtSum = view.findViewById(R.id.input_sum);
        EditText edtType = view.findViewById(R.id.input_type);
        EditText edtDate = view.findViewById(R.id.input_date);

        edtDate.setText(getTodaysDate());
        DatePickerDialog.OnDateSetListener dateSetListener = (view1, year, month, dayOfMonth) -> {
            month = month + 1;
            String date = makeDateString(dayOfMonth, month, year);
            edtDate.setText(date);
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(requireActivity(), dateSetListener, year, month, day);
        edtDate.setOnClickListener(v -> {
            openDatePicker(view);
        });

        Button btnAdd = view.findViewById(R.id.input_button_add);
        Button btnCancel = view.findViewById(R.id.input_button_cancel);

        btnAdd.setOnClickListener(v -> {
            String sum = edtSum.getText().toString().trim();
            String type = edtType.getText().toString().trim();
            String date = edtDate.getText().toString().trim();

            if (TextUtils.isEmpty(sum)) {
                edtSum.setError("Required field");
                return;
            }

            int amount = Integer.parseInt(sum);

            if (TextUtils.isEmpty(type)) {
                edtType.setError("Required field");
                return;
            }

            if (TextUtils.isEmpty(date)) {
                edtDate.setError("Required field");
                return;
            }

            String id = expenseDatabase.push().getKey();
            Data data = new Data(id, amount, type, date);
            expenseDatabase.child(id).setValue(data);
            Log.d("ExpenseData Info", sum + " " + type + " " + date);
            Log.d("ExpenseData Info", id);
            Toast.makeText(getActivity(), "Data added", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private String makeDateString(int day, int month, int year) {
        return day + "/" + month + "/" + year;
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month + 1, year);
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
    }
}
