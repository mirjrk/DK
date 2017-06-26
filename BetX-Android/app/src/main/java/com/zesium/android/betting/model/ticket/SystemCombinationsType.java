package com.zesium.android.betting.model.ticket;

/**
 * Created by savic on 28-Sep-16.
 */

public enum SystemCombinationsType {
    ShortInfo(0),
    DetailedInfo(1);

    private final int id;

    SystemCombinationsType(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}
