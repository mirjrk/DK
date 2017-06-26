package com.zesium.android.betting.model.ticket.create_ticket;

import java.util.List;

/**
 * Created by Ivan Panic_2 on 2/9/2017.
 */

class ValidateTicketRequest {
    private int GameType;
    private Settings Settings;
    private List<Bet> Bets;
    private List<System> Systems;
    private int OddBonusMode;
    private int OddBonusId;
    private EBetSlipType BetSlipType;

    public int getGameType() {
        return GameType;
    }

    public void setGameType(int gameType) {
        GameType = gameType;
    }

    public com.zesium.android.betting.model.ticket.create_ticket.Settings getSettings() {
        return Settings;
    }

    public void setSettings(com.zesium.android.betting.model.ticket.create_ticket.Settings settings) {
        Settings = settings;
    }

    public List<Bet> getBet() {
        return Bets;
    }

    public void setBet(List<Bet> bets) {
        Bets = bets;
    }

    public List<Bet> getBets() {
        return Bets;
    }

    public void setBets(List<Bet> bets) {
        Bets = bets;
    }

    public List<System> getSystems() {
        return Systems;
    }

    public void setSystems(List<System> systems) {
        Systems = systems;
    }

    public int getOddBonusMode() {
        return OddBonusMode;
    }

    public void setOddBonusMode(int oddBonusMode) {
        OddBonusMode = oddBonusMode;
    }

    public int getOddBonusId() {
        return OddBonusId;
    }

    public void setOddBonusId(int oddBonusId) {
        OddBonusId = oddBonusId;
    }

    public EBetSlipType getBetSlipType() {
        return BetSlipType;
    }

    public void setBetSlipType(EBetSlipType betSlipType) {
        BetSlipType = betSlipType;
    }
}
