package com.zesium.android.betting.model.sports;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ivan Panic_2 on 8/23/2016.
 */
public class PrematchInfo implements Serializable{
    private String Description;
    private String HomeTeam;
    private String AwayTeam;
    private String Date;
    private String Season;
    private double HomeTeamPerformance;
    private double AwayTeamPerformance;
    private int LastMeetingsHomeWins;
    private int LastMeetingsAwayWins;
    private int LastMeetingsMatchDraws;
    private List<StatisticMatch> HomeTeamLastMatches;
    private List<StatisticMatch> AwayTeamLastMatches;
    private List<StatisticMatch> LastMeetings;

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getSeason() {
        return Season;
    }

    public void setSeason(String season) {
        Season = season;
    }

    public double getHomeTeamPerformance() {
        return HomeTeamPerformance;
    }

    public void setHomeTeamPerformance(double homeTeamPerformance) {
        HomeTeamPerformance = homeTeamPerformance;
    }

    public double getAwayTeamPerformance() {
        return AwayTeamPerformance;
    }

    public void setAwayTeamPerformance(double awayTeamPerformance) {
        AwayTeamPerformance = awayTeamPerformance;
    }

    public int getLastMeetingsHomeWins() {
        return LastMeetingsHomeWins;
    }

    public void setLastMeetingsHomeWins(int lastMeetingsHomeWins) {
        LastMeetingsHomeWins = lastMeetingsHomeWins;
    }

    public int getLastMeetingsAwayWins() {
        return LastMeetingsAwayWins;
    }

    public void setLastMeetingsAwayWins(int lastMeetingsAwayWins) {
        LastMeetingsAwayWins = lastMeetingsAwayWins;
    }

    public int getLastMeetingsMatchDraws() {
        return LastMeetingsMatchDraws;
    }

    public void setLastMeetingsMatchDraws(int lastMeetingsMatchDraws) {
        LastMeetingsMatchDraws = lastMeetingsMatchDraws;
    }

    public List<StatisticMatch> getHomeTeamLastMatches() {
        return HomeTeamLastMatches;
    }

    public void setHomeTeamLastMatches(List<StatisticMatch> homeTeamLastMatches) {
        HomeTeamLastMatches = homeTeamLastMatches;
    }

    public List<StatisticMatch> getAwayTeamLastMatches() {
        return AwayTeamLastMatches;
    }

    public void setAwayTeamLastMatches(List<StatisticMatch> awayTeamLastMatches) {
        AwayTeamLastMatches = awayTeamLastMatches;
    }

    public List<StatisticMatch> getLastMeetings() {
        return LastMeetings;
    }

    public void setLastMeetings(List<StatisticMatch> lastMeetings) {
        LastMeetings = lastMeetings;
    }
}
