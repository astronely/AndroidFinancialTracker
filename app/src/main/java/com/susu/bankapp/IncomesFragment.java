package com.susu.bankapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import javax.annotation.Nullable;

public class IncomesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_incomes, container, false);
        FloatingActionButton actionButton = view.findViewById(R.id.action_button_income);

        actionButton.setOnClickListener(v -> {
            Log.d("Incomes", "Income added");
            ((MainActivity) requireActivity()).addFragment(R.id.fragment_incomes_layout, new IncomeFragment());
        });
        return view;
    }
}
