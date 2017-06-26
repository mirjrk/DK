package com.zesium.android.betting.model.ticket.create_ticket;

/**
 * Created by Ivan Panic_2 on 2/13/2017.
 */

public enum EBetErrorType {
    Warning(0),
    Error(1),
    Info(2);

    private final int id;

    EBetErrorType(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}
