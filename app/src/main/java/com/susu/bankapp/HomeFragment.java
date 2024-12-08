package com.susu.bankapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.susu.bankapp.model.Data;
import com.susu.bankapp.model.MyViewHolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private DatabaseReference incomeDatabase;
    private DatabaseReference expenseDatabase;
    private DatabaseReference walletDatabase;
    private RecyclerView recyclerView;
    private TextView walletBalance;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            Log.d("Home", "User exists");
            String uid = user.getUid();
            Log.d("Home Info", uid);
            incomeDatabase = FirebaseDatabase.getInstance().getReference().child(uid).child("IncomeDatabase");
            expenseDatabase = FirebaseDatabase.getInstance().getReference().child(uid).child("ExpenseDatabase");
            walletDatabase = FirebaseDatabase.getInstance().getReference().child(uid).child("WalletDatabase");
            Log.d("ExpenseDatabase Info", Objects.requireNonNull(expenseDatabase.getKey()));
            Log.d("IncomeDatabase Info", Objects.requireNonNull(incomeDatabase.getKey()));
            Log.d("WalletDatabase Info", Objects.requireNonNull(walletDatabase.getKey()));
        }
        walletBalance = view.findViewById(R.id.wallet_balance_value);
        recyclerView = view.findViewById(R.id.recycler_view_home);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        fetchData();
    }

    private void fetchData() {
        List<Data> combinedData = new ArrayList<>();

        incomeDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Data data = dataSnapshot.getValue(Data.class);
                    if (data != null) {
                        Log.d("IncomeDatabase Data", data.toString());
                        combinedData.add(data);
                    }
                }
                checkIfAllDataLoaded(combinedData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("IncomeDatabase Error", error.getMessage());
            }
        });

        expenseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Data data = dataSnapshot.getValue(Data.class);
                    if (data != null) {
                        Log.d("ExpenseDatabase Data", data.toString());
                        combinedData.add(data);
                    }
                }
                checkIfAllDataLoaded(combinedData);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ExpenseDatabase Error", error.getMessage());
            }
        });

        walletDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Double balance = snapshot.getValue(Double.class);

                    if (balance != null) {
                        walletBalance.setText(String.format(Locale.getDefault(), "%.2f", balance));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error fetching balance", error.toException());
            }
        });
    }

    private void checkIfAllDataLoaded(List<Data> combinedData) {
        if (!combinedData.isEmpty()) {
            combinedData.sort((data1, data2) -> Long.compare(data2.getDate(), data1.getDate()));

            List<Data> limitedData = combinedData.subList(0, Math.min(5, combinedData.size()));

            updateRecyclerView(limitedData);
        }
    }

    private void updateRecyclerView(List<Data> dataList) {
        CustomAdapter adapter = new CustomAdapter(dataList);
        recyclerView.setAdapter(adapter);
    }
}