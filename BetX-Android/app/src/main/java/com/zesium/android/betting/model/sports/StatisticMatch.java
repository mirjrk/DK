package com.zesium.android.betting.model.sports;

import java.io.Serializable;

/**
 * Created by Ivan Panic_2 on 8/23/2016.
 */
class StatisticMatch implements Serializable{
    private String Description;
    private String HomeTeam;
    private String AwayTeam;
    private String Date;
    private String Result;
    private int Round;
    private int ResultStatus;
    private int TotalGoals;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public int getRound() {
        return Round;
    }

    public void setRound(int round) {
        Round = round;
    }

    public int getResultStatus() {
        return ResultStatus;
    }

    public void setResultStatus(int resultStatus) {
        ResultStatus = resultStatus;
    }

    public int getTotalGoals() {
        return TotalGoals;
    }

    public void setTotalGoals(int totalGoals) {
        TotalGoals = totalGoals;
    }

    public String getHomeTeam() {
        return HomeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        HomeTeam = homeTeam;
    }

    public String getAwayTeam() {
        return AwayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        AwayTeam = awayTeam;
    }
}
