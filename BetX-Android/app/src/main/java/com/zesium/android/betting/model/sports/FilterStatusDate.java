package com.zesium.android.betting.model.sports;

/**
 * Created by savic on 31-Aug-16.
 */
public enum FilterStatusDate {
    TODAY(0),
    THREE_DAYS(1),
    WEEK(2),
    COMPLETE(3);

    private final int status;

    FilterStatusDate(int status) {
        this.status = status;
    }

    public int getValue() {
        return status;
    }

}
