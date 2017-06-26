package com.zesium.android.betting.model.ticket;

/**
 * Created by savic on 28-Sep-16.
 */
class TicketSystemShortInfo {
    private int WinNumber;
    private int WinCombinationCount;
    private Double MinPossibleWinAmount;
    private Double MaxPossibleWinAmount;

    public int getWinNumber() {
        return WinNumber;
    }

    public void setWinNumber(int winNumber) {
        WinNumber = winNumber;
    }

    public int getWinCombinationCount() {
        return WinCombinationCount;
    }

    public void setWinCombinationCount(int winCombinationCount) {
        WinCombinationCount = winCombinationCount;
    }

    public Double getMinPossibleWinAmount() {
        return MinPossibleWinAmount;
    }

    public void setMinPossibleWinAmount(Double minPossibleWinAmount) {
        MinPossibleWinAmount = minPossibleWinAmount;
    }

    public Double getMaxPossibleWinAmount() {
        return MaxPossibleWinAmount;
    }

    public void setMaxPossibleWinAmount(Double maxPossibleWinAmount) {
        MaxPossibleWinAmount = maxPossibleWinAmount;
    }
}
