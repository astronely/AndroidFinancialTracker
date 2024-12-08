package com.susu.bankapp;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

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
import com.susu.bankapp.model.WalletData;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

import javax.annotation.Nullable;

public class IncomesFragment extends Fragment {

    private DatabaseReference incomeDatabase;
    private DatabaseReference walletDatabase;
    private DatePickerDialog datePickerDialog;
    private RecyclerView recyclerView;
    private Date startDate;
    private Date endDate;
    EditText firstDatePicker;
    EditText secondDatePicker;
    FirebaseRecyclerAdapter<Data, MyViewHolder> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_incomes, container, false);
        FloatingActionButton actionButton = view.findViewById(R.id.action_button_income);

        FirebaseAuth auth = FirebaseAuth.getInstance();

        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            Log.d("Incomes", "User exists");
            String uid = user.getUid();
            Log.d("IncomeDatabase Info", uid);
            incomeDatabase = FirebaseDatabase.getInstance().getReference().child(uid).child("IncomeDatabase");
            walletDatabase = FirebaseDatabase.getInstance().getReference().child(uid).child("WalletDatabase");
            Log.d("IncomeDatabase Info", Objects.requireNonNull(incomeDatabase.getKey()));
        }

        actionButton.setOnClickListener(v -> {
            Log.d("Incomes", "Income add menu opened");
            incomeDataInsert();
        });

        firstDatePicker = view.findViewById(R.id.start_date);
        secondDatePicker = view.findViewById(R.id.end_date);

        firstDatePicker.setOnClickListener(v -> {
            showDateRangePickerDialog();
        });

        secondDatePicker.setOnClickListener(v -> {
            showEndDatePickerDialog();
        });

        recyclerView = view.findViewById(R.id.recycler_view_incomes);
        return view;
    }

    public void incomeDataInsert() {
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

            double amount = Double.parseDouble(sum);

            if (TextUtils.isEmpty(type)) {
                edtType.setError("Required field");
                return;
            }

            if (TextUtils.isEmpty(date)) {
                edtDate.setError("Required field");
                return;
            }

            String id = incomeDatabase.push().getKey();
            Data data = new Data(id, amount, type, date);

            if (id != null) {
                incomeDatabase.child(id).setValue(data);
                increaseBalance(amount);
                Log.d("IncomeData Info", sum + " " + type + " " + data.getDate());
                Log.d("IncomeData Info", id);
                Toast.makeText(getActivity(), "Data added", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "ID error", Toast.LENGTH_SHORT).show();
                Log.e("IncomeData Error", "ID is not found");
            }
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
        });

        dialog.show();
    }

    private void increaseBalance(double amountToAdd) {
        walletDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    double currentBalance = snapshot.getValue(Double.class);

                    double updatedBalance = currentBalance + amountToAdd;

                    walletDatabase.setValue(updatedBalance)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Firebase", "Balance updated successfully");
                            })
                            .addOnFailureListener(e -> {
                                Log.e("Firebase", "Failed to update balance", e);
                            });
                } else {
                    walletDatabase.setValue(amountToAdd)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Firebase", "Balance initialized with: " + amountToAdd);
                            })
                            .addOnFailureListener(e -> {
                                Log.e("Firebase", "Failed to initialize balance", e);
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error fetching balance", error.toException());
            }
        });
    }

    private void showDateRangePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog startDatePicker = new DatePickerDialog(requireActivity(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    startDate = new GregorianCalendar(year1, monthOfYear, dayOfMonth).getTime();
                    firstDatePicker.setText(makeDateString(dayOfMonth, monthOfYear+1, year1));
                    showEndDatePickerDialog();
                }, year, month, day);
        startDatePicker.show();
    }

    private void showEndDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog endDatePicker = new DatePickerDialog(requireActivity(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    endDate = new GregorianCalendar(year1, monthOfYear, dayOfMonth).getTime();
                    secondDatePicker.setText(makeDateString(dayOfMonth, monthOfYear+1, year1));
                    Log.d("Date range", startDate + " " + endDate);
                    updateData();
                }, year, month, day);
        endDatePicker.show();
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

    @Override
    public void onStart() {
        super.onStart();

        updateData();
    }

    private void updateData() {
        long startTime = startDate != null ? startDate.getTime() : 0;
        long endTime = endDate != null ? endDate.getTime() : Long.MAX_VALUE;

        FirebaseRecyclerOptions<Data> options = new FirebaseRecyclerOptions.Builder<Data>()
                .setQuery(incomeDatabase.orderByChild("date").startAt(startTime).endAt(endTime), Data.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<Data, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Data model) {
                holder.setType(model.getType());
                holder.setSum(model.getSum());
                holder.setDate(model.getDate());
                Log.d("Model Date", String.valueOf(model.getDate()));
                Log.d("Model Type", model.getType());
                Log.d("Model Sum", String.valueOf(model.getSum()));
            }

            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_transaction_item, parent, false);
                return new MyViewHolder(view);
            }
        };

        recyclerView.setAdapter(adapter);
        Log.d("Recycler Options", options.toString());
        Log.d("Query Debug", incomeDatabase.toString());

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false; // Перемещение не требуется
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getBindingAdapterPosition();
                String key = adapter.getRef(position).getKey();

                incomeDatabase.child(key).removeValue()
                        .addOnSuccessListener(aVoid -> {
                            Log.d("Swipe Delete", "Item deleted successfully.");
                        })
                        .addOnFailureListener(e -> {
                            Log.e("Swipe Delete", "Error deleting item", e);
                        });
                Data data = adapter.getItem(position);
                increaseBalance(-data.getSum());
            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

        adapter.startListening();
        Log.d("Adapter", "Adapter is OK!");
    }


}
