package com.zesium.android.betting.model.sports;

/**
 * Created by savic on 15-Sep-16.
 */
public enum FilterStatusOdds {
    ONE_THREE(0),
    ONE_FIVE(1),
    ONE_EIGHT(2),
    ALL(3);

    private final int status;

    FilterStatusOdds(int status) {
        this.status = status;
    }

    public int getValue() {
        return status;
    }

}
