package com.zesium.android.betting.model.ticket.create_ticket;

/**
 * Created by Ivan Panic_2 on 2/9/2017.
 */

class AvailableSystem {
    private Integer TypeFrom;
    private Integer TypeTo;
    private Integer CombinationsCount;
    private double PayIn;
    private double PayInPerCombination;
    private boolean isSelected;

    public Integer getTypeFrom() {
        return TypeFrom;
    }

    public void setTypeFrom(Integer typeFrom) {
        TypeFrom = typeFrom;
    }

    public Integer getTypeTo() {
        return TypeTo;
    }

    public void setTypeTo(Integer typeTo) {
        TypeTo = typeTo;
    }

    public Integer getCombinationsCount() {
        return CombinationsCount;
    }

    public void setCombinationsCount(Integer combinationsCount) {
        CombinationsCount = combinationsCount;
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
