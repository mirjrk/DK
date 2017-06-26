package com.zesium.android.betting.model.configuration;

/**
 * Created by Ivan Panic_2 on 1/31/2017.
 */

public class BettingConfiguration {
    private double MinPayIn;
    private double SystemMinPayIn;

    public double getMinPayIn() {
        return MinPayIn;
    }

    public void setMinPayIn(double minPayIn) {
        MinPayIn = minPayIn;
    }

    public double getSystemMinPayIn() {
        return SystemMinPayIn;
    }

    public void setSystemMinPayIn(double systemMinPayIn) {
        SystemMinPayIn = systemMinPayIn;
    }
}
