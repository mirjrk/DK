package com.zesium.android.betting.model.ticket.create_ticket;

/**
 * Created by Ivan Panic_2 on 4/11/2017.
 */

public enum EBetSlipType {
    Basic (0),
    System(1);

    private final int id;

    EBetSlipType(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }

}
