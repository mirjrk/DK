package com.zesium.android.betting.model.user;

/**
 * Created by Ivan Panic_2 on 6/19/2017.
 */

public class AccountChange {
    private double Balance;
    private double CurrentPoints;
    private int UprocessedTicketsCount;
    private int UserMembershipType;

    public double getBalance() {
        return Balance;
    }

    public void setBalance(double balance) {
        Balance = balance;
    }

    public double getCurrentPoints() {
        return CurrentPoints;
    }

    public void setCurrentPoints(double currentPoints) {
        CurrentPoints = currentPoints;
    }

    public int getUprocessedTicketsCount() {
        return UprocessedTicketsCount;
    }

    public void setUprocessedTicketsCount(int uprocessedTicketsCount) {
        UprocessedTicketsCount = uprocessedTicketsCount;
    }

    public int getUserMembershipType() {
        return UserMembershipType;
    }

    public void setUserMembershipType(int userMembershipType) {
        UserMembershipType = userMembershipType;
    }
}