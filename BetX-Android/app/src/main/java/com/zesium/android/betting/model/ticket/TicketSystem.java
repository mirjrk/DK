package com.zesium.android.betting.model.ticket;

import java.util.List;

/**
 * Created by savic on 28-Sep-16.
 */
class TicketSystem {
    private int TypeFrom;
    private int TypeTo;
    private int CombinationsCount;
    private Double PayIn;
    private Double BetAmount;
    private List<TicketSystemCombination> TicketSystemCombinations;
    private List<TicketSystemShortInfo> TicketSystemShortInfos;

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

    public int getCombinationsCount() {
        return CombinationsCount;
    }

    public void setCombinationsCount(int combinationsCount) {
        CombinationsCount = combinationsCount;
    }

    public Double getPayIn() {
        return PayIn;
    }

    public void setPayIn(Double payIn) {
        PayIn = payIn;
    }

    public Double getBetAmount() {
        return BetAmount;
    }

    public void setBetAmount(Double betAmount) {
        BetAmount = betAmount;
    }

    public List<TicketSystemCombination> getTicketSystemCombinations() {
        return TicketSystemCombinations;
    }

    public void setTicketSystemCombinations(List<TicketSystemCombination> ticketSystemCombinations) {
        TicketSystemCombinations = ticketSystemCombinations;
    }

    public List<TicketSystemShortInfo> getTicketSystemShortInfos() {
        return TicketSystemShortInfos;
    }

    public void setTicketSystemShortInfos(List<TicketSystemShortInfo> ticketSystemShortInfos) {
        TicketSystemShortInfos = ticketSystemShortInfos;
    }
}
