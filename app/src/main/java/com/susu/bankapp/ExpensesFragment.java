package com.susu.bankapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ExpensesFragment extends Fragment {

    public ExpensesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expenses, container, false);
        FloatingActionButton actionButton = view.findViewById(R.id.action_button_expense);

        actionButton.setOnClickListener(v -> {
            Log.d("Expenses", "Expense added");
            ((MainActivity) requireActivity()).addFragment(R.id.fragment_expenses_layout, new ExpenseFragment());
        });
        return view;
    }
}
