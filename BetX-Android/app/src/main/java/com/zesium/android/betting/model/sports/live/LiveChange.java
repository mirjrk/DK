package com.zesium.android.betting.model.sports.live;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ivan Panic_2 on 12/28/2016.
 */

public class LiveChange implements Serializable{
    private int SportId;
    private String SportName;
    private String OriginSportName;
    private int CategoryId;
    private String CategoryName;
    private int LeagueId;
    private int  CommonLeagueId;
    private String LeagueName;
    private String HomeTeam;
    private String AwayTeam;
    private com.zesium.android.betting.model.sports.Match Match;
    private int OffersCount;
    private List<String> Headers;

    public int getSportId() {
        return SportId;
    }

    public void setSportId(int sportId) {
        SportId = sportId;
    }

    public String getSportName() {
        return SportName;
    }

    public void setSportName(String sportName) {
        SportName = sportName;
    }

    public String getOriginSportName() {
        return OriginSportName;
    }

    public void setOriginSportName(String originSportName) {
        OriginSportName = originSportName;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public int getLeagueId() {
        return LeagueId;
    }

    public void setLeagueId(int leagueId) {
        LeagueId = leagueId;
    }

    public int getCommonLeagueId() {
        return CommonLeagueId;
    }

    public void setCommonLeagueId(int commonLeagueId) {
        CommonLeagueId = commonLeagueId;
    }

    public String getLeagueName() {
        return LeagueName;
    }

    public void setLeagueName(String leagueName) {
        LeagueName = leagueName;
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

    public com.zesium.android.betting.model.sports.Match getMatch() {
        return Match;
    }

    public void setMatch(com.zesium.android.betting.model.sports.Match match) {
        Match = match;
    }

    public int getOffersCount() {
        return OffersCount;
    }

    public void setOffersCount(int offersCount) {
        OffersCount = offersCount;
    }

    public void setHeaders(List<String> headers) {
        Headers = headers;
    }

    public List<String> getHeaders() {
        return Headers;
    }
}
