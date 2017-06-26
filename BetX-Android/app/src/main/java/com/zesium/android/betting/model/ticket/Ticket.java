package com.zesium.android.betting.model.ticket;

import java.io.Serializable;

/**
 * Ticket class that has fields defined by response from server. It contains all basic data for one ticket.
 * Created by Ivan Panic on 12/23/2015.
 */
class Ticket implements Serializable {
    private String EncryptedTicketPin;
    private String TimeCreated;
    private Double PayIn;
    private TicketStatuses Status;
    private String StatusOrigName;
    private String StatusTranslatedName;
    private Double WinAmount;
    private Double PayoutAmount;
    private Double WinPossibleBonusAmount;
    private Double OddsTotal;
    private Double BetAmount;
    private Double WinTotalPossibleAmount;
    private Integer TicketType;
    private String TicketTypeOrigName;
    private String TicketTypeTranslatedName;
    private Integer GameType;
    private Integer ServiceChargePercentage;
    private Double WinMaxPossibleAmount;
    private Object Username;
    private String Id;
    private String CancellationTime;

    public String getTicketId() {
        return TicketId;
    }

    public void setTicketId(String ticketId) {
        TicketId = ticketId;
    }

    private String TicketId;

    public String getEncryptedTicketPin() {
        return EncryptedTicketPin;
    }

    public void setEncryptedTicketPin(String encryptedTicketPin) {
        EncryptedTicketPin = encryptedTicketPin;
    }

    public String getTimeCreated() {
        return TimeCreated;
    }

    public void setTimeCreated(String timeCreated) {
        TimeCreated = timeCreated;
    }

    public Double getPayIn() {
        return PayIn;
    }

    public void setPayIn(Double payIn) {
        PayIn = payIn;
    }

    public TicketStatuses getStatus() {
        return Status;
    }

    public void setStatus(TicketStatuses status) {
        Status = status;
    }

    public String getStatusOrigName() {
        return StatusOrigName;
    }

    public void setStatusOrigName(String statusOrigName) {
        StatusOrigName = statusOrigName;
    }

    public String getStatusTranslatedName() {
        return StatusTranslatedName;
    }

    public void setStatusTranslatedName(String statusTranslatedName) {
        StatusTranslatedName = statusTranslatedName;
    }

    public Double getWinAmount() {
        return WinAmount;
    }

    public void setWinAmount(Double winAmount) {
        WinAmount = winAmount;
    }

    public Double getPayoutAmount() {
        return PayoutAmount;
    }

    public void setPayoutAmount(Double payoutAmount) {
        PayoutAmount = payoutAmount;
    }

    public Double getWinPossibleBonusAmount() {
        return WinPossibleBonusAmount;
    }

    public void setWinPossibleBonusAmount(Double winPossibleBonusAmount) {
        WinPossibleBonusAmount = winPossibleBonusAmount;
    }

    public Double getOddsTotal() {
        return OddsTotal;
    }

    public void setOddsTotal(Double oddsTotal) {
        OddsTotal = oddsTotal;
    }

    public Double getBetAmount() {
        return BetAmount;
    }

    public void setBetAmount(Double betAmount) {
        BetAmount = betAmount;
    }

    public Double getWinTotalPossibleAmount() {
        return WinTotalPossibleAmount;
    }

    public void setWinTotalPossibleAmount(Double winTotalPossibleAmount) {
        WinTotalPossibleAmount = winTotalPossibleAmount;
    }

    public Integer getTicketType() {
        return TicketType;
    }

    public void setTicketType(Integer ticketType) {
        TicketType = ticketType;
    }

    public String getTicketTypeOrigName() {
        return TicketTypeOrigName;
    }

    public void setTicketTypeOrigName(String ticketTypeOrigName) {
        TicketTypeOrigName = ticketTypeOrigName;
    }

    public String getTicketTypeTranslatedName() {
        return TicketTypeTranslatedName;
    }

    public void setTicketTypeTranslatedName(String ticketTypeTranslatedName) {
        TicketTypeTranslatedName = ticketTypeTranslatedName;
    }

    public Integer getGameType() {
        return GameType;
    }

    public void setGameType(Integer gameType) {
        GameType = gameType;
    }

    public Integer getServiceChargePercentage() {
        return ServiceChargePercentage;
    }

    public void setServiceChargePercentage(Integer serviceChargePercentage) {
        ServiceChargePercentage = serviceChargePercentage;
    }

    public Double getWinMaxPossibleAmount() {
        return WinMaxPossibleAmount;
    }

    public void setWinMaxPossibleAmount(Double winMaxPossibleAmount) {
        WinMaxPossibleAmount = winMaxPossibleAmount;
    }

    public Object getUsername() {
        return Username;
    }

    public void setUsername(Object username) {
        Username = username;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCancellationTime() {
        return CancellationTime;
    }

    public void setCancellationTime(String cancellationTime) {
        CancellationTime = cancellationTime;
    }
}
