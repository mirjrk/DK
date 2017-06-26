package com.zesium.android.betting.model.ticket;

/**
 * Created by savic on 28-Sep-16.
 */
public enum SuperTicketCombinationsTypes {
    Normal(0),
    Single(1),
    Maxi(2),
    SuperMaxi(3),
    System(4),
    Custom(5);

    private final int id;

    SuperTicketCombinationsTypes(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}
