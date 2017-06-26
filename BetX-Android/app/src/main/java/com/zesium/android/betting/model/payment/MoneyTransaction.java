package com.zesium.android.betting.model.payment;

/**
 * Created by Ivan Panic_2 on 2/27/2017.
 */

public class MoneyTransaction {
    private int Id;
    private String DateTime;
    private String Type;
    private double Amount;
    private double NewCredit;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public double getNewCredit() {
        return NewCredit;
    }

    public void setNewCredit(double newCredit) {
        NewCredit = newCredit;
    }
}