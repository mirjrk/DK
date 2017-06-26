package com.zesium.android.betting.model.ticket.create_ticket;

import java.util.List;

/**
 * Created by Ivan Panic_2 on 2/9/2017.
 */

class ValidateTicketResponse {
    private Object EncryptedTicketPin;
    private Double  PayIn;
    private Double  BetAmount;
    private Double  ServiceChargeAmount;
    private Double  ServiceChargePercent;
    private Double  MinPayIn;
    private Double  MaxPayIn;
    private Double  MinOdds;
    private Double  MaxOdds;
    private Double  TotalOdds;
    private Double  MinWin;
    private Double  MaxWin;
    private Double  TotalWin;
    private Double  WinBonus;
    private Double  WinTax;
    private Double  WinTaxPercent;
    private Integer CombinationCount;
    private boolean PayInPerTicketLocked;
    private List<SystemResult> SystemResults = null;
    private List<BetResult> BetResults = null;
    private List<ErrorBetSlip> Errors = null;
    private List<AvailableSystem> AvailableSystems = null;
    private Object PreparedTicketIdentifier;

    public Object getEncryptedTicketPin() {
        return EncryptedTicketPin;
    }

    public void setEncryptedTicketPin(Object encryptedTicketPin) {
        EncryptedTicketPin = encryptedTicketPin;
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

    public Double getServiceChargePercent() {
        return ServiceChargePercent;
    }

    public void setServiceChargePercent(Double serviceChargePercent) {
        ServiceChargePercent = serviceChargePercent;
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

    public Integer getCombinationCount() {
        return CombinationCount;
    }

    public void setCombinationCount(Integer combinationCount) {
        CombinationCount = combinationCount;
    }

    public boolean isPayInPerTicketLocked() {
        return PayInPerTicketLocked;
    }

    public void setPayInPerTicketLocked(boolean payInPerTicketLocked) {
        PayInPerTicketLocked = payInPerTicketLocked;
    }

    public List<SystemResult> getSystemResults() {
        return SystemResults;
    }

    public void setSystemResults(List<SystemResult> systemResults) {
        SystemResults = systemResults;
    }

    public List<BetResult> getBetResults() {
        return BetResults;
    }

    public void setBetResults(List<BetResult> betResults) {
        BetResults = betResults;
    }

    public List<ErrorBetSlip> getErrors() {
        return Errors;
    }

    public void setErrors(List<ErrorBetSlip> errors) {
        Errors = errors;
    }

    public List<AvailableSystem> getAvailableSystems() {
        return AvailableSystems;
    }

    public void setAvailableSystems(List<AvailableSystem> availableSystems) {
        AvailableSystems = availableSystems;
    }

    public Object getPreparedTicketIdentifier() {
        return PreparedTicketIdentifier;
    }

    public void setPreparedTicketIdentifier(Object preparedTicketIdentifier) {
        PreparedTicketIdentifier = preparedTicketIdentifier;
    }
}
