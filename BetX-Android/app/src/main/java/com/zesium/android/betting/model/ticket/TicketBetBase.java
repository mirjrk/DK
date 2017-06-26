package com.zesium.android.betting.model.ticket;

/**
 * Created by savic on 28-Sep-16.
 */
class TicketBetBase {
    private String OfferId;
    private String SportOrigName;
    private String HomeTeam;
    private HomeTeamStatsModel HomeTeamStats;
    private AwayTeamStatsModel AwayTeamStats;
    private String AwayTeam;
    private String LeagueTranslatedDescription;
    private String CountryTranslatedDescription;
    private String Score;
    private BetXGame GameType;
    private String GameOrigName;
    private String GameTranslatedName;
    private TicketBetStatus Status;
    private String StatusOrigName;
    private String StatusTranslatedName;
    private String EventTime;
    private Double Odd;
    private String BetCode;
    private String Group;
    private String Pick;
    private String BetTypeTranslatedName;
    private String BetResult;

    public String getOfferId() {
        return OfferId;
    }

    public void setOfferId(String offerId) {
        OfferId = offerId;
    }

    public String getSportOrigName() {
        return SportOrigName;
    }

    public void setSportOrigName(String sportOrigName) {
        SportOrigName = sportOrigName;
    }

    public String getHomeTeam() {
        return HomeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        HomeTeam = homeTeam;
    }

    public HomeTeamStatsModel getHomeTeamStats() {
        return HomeTeamStats;
    }

    public void setHomeTeamStats(HomeTeamStatsModel homeTeamStats) {
        HomeTeamStats = homeTeamStats;
    }

    public AwayTeamStatsModel getAwayTeamStats() {
        return AwayTeamStats;
    }

    public void setAwayTeamStats(AwayTeamStatsModel awayTeamStats) {
        AwayTeamStats = awayTeamStats;
    }

    public String getAwayTeam() {
        return AwayTeam;
    }

    public void setAwayTeam(String awayTeam) {
        AwayTeam = awayTeam;
    }

    public String getLeagueTranslatedDescription() {
        return LeagueTranslatedDescription;
    }

    public void setLeagueTranslatedDescription(String leagueTranslatedDescription) {
        LeagueTranslatedDescription = leagueTranslatedDescription;
    }

    public String getCountryTranslatedDescription() {
        return CountryTranslatedDescription;
    }

    public void setCountryTranslatedDescription(String countryTranslatedDescription) {
        CountryTranslatedDescription = countryTranslatedDescription;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    public BetXGame getGameType() {
        return GameType;
    }

    public void setGameType(BetXGame gameType) {
        GameType = gameType;
    }

    public String getGameOrigName() {
        return GameOrigName;
    }

    public void setGameOrigName(String gameOrigName) {
        GameOrigName = gameOrigName;
    }

    public String getGameTranslatedName() {
        return GameTranslatedName;
    }

    public void setGameTranslatedName(String gameTranslatedName) {
        GameTranslatedName = gameTranslatedName;
    }

    public TicketBetStatus getStatus() {
        return Status;
    }

    public void setStatus(TicketBetStatus status) {
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

    public String getEventTime() {
        return EventTime;
    }

    public void setEventTime(String eventTime) {
        EventTime = eventTime;
    }

    public Double getOdd() {
        return Odd;
    }

    public void setOdd(Double odd) {
        Odd = odd;
    }

    public String getBetCode() {
        return BetCode;
    }

    public void setBetCode(String betCode) {
        BetCode = betCode;
    }

    public String getGroup() {
        return Group;
    }

    public void setGroup(String group) {
        Group = group;
    }

    public String getPick() {
        return Pick;
    }

    public void setPick(String pick) {
        Pick = pick;
    }

    public String getBetTypeTranslatedName() {
        return BetTypeTranslatedName;
    }

    public void setBetTypeTranslatedName(String betTypeTranslatedName) {
        BetTypeTranslatedName = betTypeTranslatedName;
    }

    public String getBetResult() {
        return BetResult;
    }

    public void setBetResult(String betResult) {
        BetResult = betResult;
    }
}
