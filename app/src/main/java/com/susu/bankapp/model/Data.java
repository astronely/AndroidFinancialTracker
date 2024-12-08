package com.susu.bankapp.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

public class Data {
    private double sum;
    private String id;
    private String type;
    private long date;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

    public Data(String id, double sum, String type, String date) {
        try {
            this.id = id;
            this.sum = sum;
            this.type = type;
            this.date = sdf.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public Data() {

    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
