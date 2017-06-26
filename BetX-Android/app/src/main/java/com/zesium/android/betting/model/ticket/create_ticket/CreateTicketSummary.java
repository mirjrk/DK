package com.zesium.android.betting.model.ticket.create_ticket;

/**
 * Created by Ivan Panic_2 on 1/26/2017.
 */

class CreateTicketSummary extends CreateTicketItem {
    private boolean isDisabled;
    private boolean hasErrors;
    private double possibleWin;
    private double payInValue;
    private double maxPayInValue;
    private double minPayInValue;

    public boolean isDisabled() {
        return isDisabled;
    }

    public void setDisabled(boolean disabled) {
        isDisabled = disabled;
    }

    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public double getPossibleWin() {
        return possibleWin;
    }

    public void setPossibleWin(double possibleWin) {
        this.possibleWin = possibleWin;
    }

    public double getPayInValue() {
        return payInValue;
    }

    public void setPayInValue(double payInValue) {
        this.payInValue = payInValue;
    }

    public double getMaxPayInValue() {
        return maxPayInValue;
    }

    public void setMaxPayInValue(double maxPayInValue) {
        this.maxPayInValue = maxPayInValue;
    }

    public double getMinPayInValue() {
        return minPayInValue;
    }

    public void setMinPayInValue(double minPayInValue) {
        this.minPayInValue = minPayInValue;
    }
}