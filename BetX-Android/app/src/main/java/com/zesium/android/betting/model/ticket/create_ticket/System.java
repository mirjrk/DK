package com.zesium.android.betting.model.ticket.create_ticket;

/**
 * Created by Ivan Panic_2 on 2/9/2017.
 */

class System {
    private int TypeFrom;
    private int TypeTo;
    private double PayIn;
    private double PayInPerCombination;

    public int getTypeFrom() {
        return TypeFrom;
    }

    public void setTypeFrom(int typeFrom) {
        TypeFrom = typeFrom;
    }

    public int getTypeTo() {
        return TypeTo;
    }

    public void setTypeTo(int typeTo) {
        TypeTo = typeTo;
    }

    public double getPayIn() {
        return PayIn;
    }

    public void setPayIn(double payIn) {
        PayIn = payIn;
    }

    public double getPayInPerCombination() {
        return PayInPerCombination;
    }

    public void setPayInPerCombination(double payInPerCombination) {
        PayInPerCombination = payInPerCombination;
    }
}
