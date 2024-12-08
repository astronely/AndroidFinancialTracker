package com.susu.bankapp.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class WalletData {
    private int amount;

    public WalletData(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
