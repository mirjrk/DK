package com.zesium.android.betting.model.ticket;

/**
 * Created by savic on 28-Sep-16.
 */

public class TicketExtended {
    private int TicketId;
    private String  TimeCreated;
    private Double PayIn;
    private Double ServiceChargeAmount;
    private Double ServiceChargePercentage;
    private Double BetAmount;
    private TicketStatuses Status;
    private String StatusOrigName;
    private String StatusTranslatedName;
    private Double WinAmount;
    private Double WinPossibleBonusAmount;
    private Double WinTotalAmount;
    private Double WinMaxPossibleAmount;
    private Double WinMinPossibleAmount;
    private Double WinTotalPossibleAmount;
    private Double WinTaxAmount;
    private Double PayoutAmount;
    private Double OddsTotal;
    private SuperTicketCombinationsTypes TicketType;
    private String TicketTypeOrigName;
    private String TicketTypeTranslatedName;
    private BettingGameTypes GameType;
    private String GameTypeOrigName;
    private String GameTypeTranslatedName;
    private String EncryptedTicketPin;
    private String Username;
    private Double EarlyPayoutAmount;
    private String CancellationTime;
    private boolean PayoutEnabled;
    private String QrUrl;
    private OddsBonus OddsBonus;
    private boolean IsCampaignWinner;

    public int getTicketId() {
        return TicketId;
    }

    public void setTicketId(int ticketId) {
        TicketId = ticketId;
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

    public Double getServiceChargeAmount() {
        return ServiceChargeAmount;
    }

    public void setServiceChargeAmount(Double serviceChargeAmount) {
        ServiceChargeAmount = serviceChargeAmount;
    }

    public Double getServiceChargePercentage() {
        return ServiceChargePercentage;
    }

    public void setServiceChargePercentage(Double serviceChargePercentage) {
        ServiceChargePercentage = serviceChargePercentage;
    }

    public Double getBetAmount() {
        return BetAmount;
    }

    public void setBetAmount(Double betAmount) {
        BetAmount = betAmount;
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

    public Double getWinPossibleBonusAmount() {
        return WinPossibleBonusAmount;
    }

    public void setWinPossibleBonusAmount(Double winPossibleBonusAmount) {
        WinPossibleBonusAmount = winPossibleBonusAmount;
    }

    public Double getWinTotalAmount() {
        return WinTotalAmount;
    }

    public void setWinTotalAmount(Double winTotalAmount) {
        WinTotalAmount = winTotalAmount;
    }

    public Double getWinMaxPossibleAmount() {
        return WinMaxPossibleAmount;
    }

    public void setWinMaxPossibleAmount(Double winMaxPossibleAmount) {
        WinMaxPossibleAmount = winMaxPossibleAmount;
    }

    public Double getWinMinPossibleAmount() {
        return WinMinPossibleAmount;
    }

    public void setWinMinPossibleAmount(Double winMinPossibleAmount) {
        WinMinPossibleAmount = winMinPossibleAmount;
    }

    public Double getWinTotalPossibleAmount() {
        return WinTotalPossibleAmount;
    }

    public void setWinTotalPossibleAmount(Double winTotalPossibleAmount) {
        WinTotalPossibleAmount = winTotalPossibleAmount;
    }

    public Double getWinTaxAmount() {
        return WinTaxAmount;
    }

    public void setWinTaxAmount(Double winTaxAmount) {
        WinTaxAmount = winTaxAmount;
    }

    public Double getPayoutAmount() {
        return PayoutAmount;
    }

    public void setPayoutAmount(Double payoutAmount) {
        PayoutAmount = payoutAmount;
    }

    public Double getOddsTotal() {
        return OddsTotal;
    }

    public void setOddsTotal(Double oddsTotal) {
        OddsTotal = oddsTotal;
    }

    public SuperTicketCombinationsTypes getTicketType() {
        return TicketType;
    }

    public void setTicketType(SuperTicketCombinationsTypes ticketType) {
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

    public BettingGameTypes getGameType() {
        return GameType;
    }

    public void setGameType(BettingGameTypes gameType) {
        GameType = gameType;
    }

    public String getGameTypeOrigName() {
        return GameTypeOrigName;
    }

    public void setGameTypeOrigName(String gameTypeOrigName) {
        GameTypeOrigName = gameTypeOrigName;
    }

    public String getGameTypeTranslatedName() {
        return GameTypeTranslatedName;
    }

    public void setGameTypeTranslatedName(String gameTypeTranslatedName) {
        GameTypeTranslatedName = gameTypeTranslatedName;
    }

    public String getEncryptedTicketPin() {
        return EncryptedTicketPin;
    }

    public void setEncryptedTicketPin(String encryptedTicketPin) {
        EncryptedTicketPin = encryptedTicketPin;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public Double getEarlyPayoutAmount() {
        return EarlyPayoutAmount;
    }

    public void setEarlyPayoutAmount(Double earlyPayoutAmount) {
        EarlyPayoutAmount = earlyPayoutAmount;
    }

    public String getCancellationTime() {
        return CancellationTime;
    }

    public void setCancellationTime(String cancellationTime) {
        CancellationTime = cancellationTime;
    }

    public boolean isPayoutEnabled() {
        return PayoutEnabled;
    }

    public void setPayoutEnabled(boolean payoutEnabled) {
        PayoutEnabled = payoutEnabled;
    }

    public String getQrUrl() {
        return QrUrl;
    }

    public void setQrUrl(String qrUrl) {
        QrUrl = qrUrl;
    }

    public com.zesium.android.betting.model.ticket.OddsBonus getOddsBonus() {
        return OddsBonus;
    }

    public void setOddsBonus(com.zesium.android.betting.model.ticket.OddsBonus oddsBonus) {
        OddsBonus = oddsBonus;
    }

    public boolean isCampaignWinner() {
        return IsCampaignWinner;
    }

    public void setCampaignWinner(boolean campaignWinner) {
        IsCampaignWinner = campaignWinner;
    }
}
