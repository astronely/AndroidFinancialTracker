package com.susu.bankapp.Model;

public class Data {
    private int sum;
    private String id;
    private String type;
    private String date;

    public Data(String id, int sum, String type, String date) {
        this.id = id;
        this.sum = sum;
        this.type = type;
        this.date = date;
    }

    public Data() {

    }

    public int getSum() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
