package com.zesium.android.betting.model.ticket;

/**
 * Created by savic on 28-Sep-16.
 */
public enum BetSlipOddBonusMode {
    NormalTicket(0),
    UseJockerTicket(1),
    BuyJockerTicket(2),
    BuyOdds(3);

    private final int id;

    BetSlipOddBonusMode(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }

}
