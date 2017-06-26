package com.zesium.android.betting.model.ticket;

/**
 * Created by Ivan Panic_2 on 6/27/2016.
 */
public enum TicketStatuses {
    Unprocessed(0),
    Winning(1),
    Loosing(2),
    Paidout(3),
    Canceled(4),
    NotCleared(5),
    Boomerang(7),
    PayoutTimeExpired(8),
    EarlyWin(9),
    All(10);

    private final int id;

    TicketStatuses(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}
