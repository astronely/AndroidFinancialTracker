package com.susu.bankapp;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.susu.bankapp.model.Data;
import com.susu.bankapp.model.MyViewHolder;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<MyViewHolder> {
    private final List<Data> dataList;

    public CustomAdapter(List<Data> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_transaction_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Data data = dataList.get(position);
        holder.setType(data.getType());
        holder.setDate(data.getDate());
        holder.setSum(data.getSum());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }
}