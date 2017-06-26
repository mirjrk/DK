package com.zesium.android.betting.model.ticket;

/**
 * Created by savic on 28-Sep-16.
 */
public enum TicketBetStatus {
    Unprocessed(0),
    Won(1),
    Lost(2),
    NotCleared(3),
    Canceled(4),
    Boomerang(5);

    private final int id;

    TicketBetStatus(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }

}
