package com.zesium.android.betting.model;

/**
 * Created by Ivan Panic_2 on 9/9/2016.
 */
public enum UserStatus {
    GUEST(0),
    LOGGED(1),
    REGISTERED(2),
    LOGGED_WITH_PIN(3);

    private final int id;

    UserStatus(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}
