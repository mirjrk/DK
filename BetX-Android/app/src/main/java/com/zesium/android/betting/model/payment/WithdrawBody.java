package com.zesium.android.betting.model.payment;

/**
 * Created by savic on 30-Mar-17.
 */

public class WithdrawBody {
    private double Amount;
    private String Password;

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
