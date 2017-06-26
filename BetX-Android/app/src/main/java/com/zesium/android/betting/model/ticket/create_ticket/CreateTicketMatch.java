package com.zesium.android.betting.model.ticket.create_ticket;

import com.zesium.android.betting.model.sports.live.EChangeOddType;
import com.zesium.android.betting.model.ticket.create_ticket.CreateTicketItem;
import com.zesium.android.betting.model.ticket.create_ticket.ErrorBet;

import java.util.List;

/**
 * Created by Ivan Panic_2 on 1/26/2017.
 */

class CreateTicketMatch extends CreateTicketItem {
    private int matchId;
    private int offerId;
    private int oddId;
    private String teamHome;
    private String teamAway;
    private String sbv;
    private String oddName;
    private double oddValue;
    private String description;
    private String oddOriginName;
    private boolean isFix;
    private boolean isLive;
    private String BetTypeKey;
    private List<ErrorBet> errors;
    private EChangeOddType changeOdd;

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public int getOddId() {
        return oddId;
    }

    public void setOddId(int oddId) {
        this.oddId = oddId;
    }

    public String getTeamHome() {
        return teamHome;
    }

    public void setTeamHome(String teamHome) {
        this.teamHome = teamHome;
    }

    public String getTeamAway() {
        return teamAway;
    }

    public void setTeamAway(String teamAway) {
        this.teamAway = teamAway;
    }

    public String getSbv() {
        return sbv;
    }

    public void setSbv(String sbv) {
        this.sbv = sbv;
    }

    public String getOddName() {
        return oddName;
    }

    public void setOddName(String oddName) {
        this.oddName = oddName;
    }

    public double getOddValue() {
        return oddValue;
    }

    public void setOddValue(double oddValue) {
        this.oddValue = oddValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOddOriginName() {
        return oddOriginName;
    }

    public void setOddOriginName(String oddOriginName) {
        this.oddOriginName = oddOriginName;
    }

    public List<ErrorBet> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorBet> errors) {
        this.errors = errors;
    }

    public boolean isFix() {
        return isFix;
    }

    public void setFix(boolean fix) {
        isFix = fix;
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public String getBetTypeKey() {
        return BetTypeKey;
    }

    public void setBetTypeKey(String betTypeKey) {
        BetTypeKey = betTypeKey;
    }

    public EChangeOddType getChangeOdd() {
        return changeOdd;
    }

    public void setChangeOdd(EChangeOddType changeOdd) {
        this.changeOdd = changeOdd;
    }
}
