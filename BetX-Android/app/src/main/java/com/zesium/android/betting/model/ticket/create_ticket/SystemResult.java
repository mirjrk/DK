package com.zesium.android.betting.model.ticket.create_ticket;

/**
 * Created by Ivan Panic_2 on 2/9/2017.
 */

class SystemResult {
    private int TypeFrom;
    private int TypeTo;
    private Double PayIn;
    private Double BetAmount;
    private Double ServiceChargeAmount;
    private Double MinPayIn;
    private Double MaxPayIn;
    private Double MinOdds;
    private Double MaxOdds;
    private Double TotalOdds;
    private Double PayInPerCombination;
    private Double BetPerCombination;
    private Double MinWin;
    private Double MaxWin;
    private Double TotalWin;
    private Double WinBonus;
    private Double WinTax;
    private Double WinTaxPercent;
    private int CombinationCount;

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

    public Double getServiceChargeAmount() {
        return ServiceChargeAmount;
    }

    public void setServiceChargeAmount(Double serviceChargeAmount) {
        ServiceChargeAmount = serviceChargeAmount;
    }

    public Double getMinPayIn() {
        return MinPayIn;
    }

    public void setMinPayIn(Double minPayIn) {
        MinPayIn = minPayIn;
    }

    public Double getMaxPayIn() {
        return MaxPayIn;
    }

    public void setMaxPayIn(Double maxPayIn) {
        MaxPayIn = maxPayIn;
    }

    public Double getMinOdds() {
        return MinOdds;
    }

    public void setMinOdds(Double minOdds) {
        MinOdds = minOdds;
    }

    public Double getMaxOdds() {
        return MaxOdds;
    }

    public void setMaxOdds(Double maxOdds) {
        MaxOdds = maxOdds;
    }

    public Double getTotalOdds() {
        return TotalOdds;
    }

    public void setTotalOdds(Double totalOdds) {
        TotalOdds = totalOdds;
    }

    public Double getPayInPerCombination() {
        return PayInPerCombination;
    }

    public void setPayInPerCombination(Double payInPerCombination) {
        PayInPerCombination = payInPerCombination;
    }

    public Double getBetPerCombination() {
        return BetPerCombination;
    }

    public void setBetPerCombination(Double betPerCombination) {
        BetPerCombination = betPerCombination;
    }

    public Double getMinWin() {
        return MinWin;
    }

    public void setMinWin(Double minWin) {
        MinWin = minWin;
    }

    public Double getMaxWin() {
        return MaxWin;
    }

    public void setMaxWin(Double maxWin) {
        MaxWin = maxWin;
    }

    public Double getTotalWin() {
        return TotalWin;
    }

    public void setTotalWin(Double totalWin) {
        TotalWin = totalWin;
    }

    public Double getWinBonus() {
        return WinBonus;
    }

    public void setWinBonus(Double winBonus) {
        WinBonus = winBonus;
    }

    public Double getWinTax() {
        return WinTax;
    }

    public void setWinTax(Double winTax) {
        WinTax = winTax;
    }

    public Double getWinTaxPercent() {
        return WinTaxPercent;
    }

    public void setWinTaxPercent(Double winTaxPercent) {
        WinTaxPercent = winTaxPercent;
    }

    public int getCombinationCount() {
        return CombinationCount;
    }

    public void setCombinationCount(int combinationCount) {
        CombinationCount = combinationCount;
    }
}
