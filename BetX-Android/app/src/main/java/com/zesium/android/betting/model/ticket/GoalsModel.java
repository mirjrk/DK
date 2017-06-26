package com.zesium.android.betting.model.ticket;

/**
 * Created by savic on 11-Oct-16.
 */

class GoalsModel {
    private String MatchId;
    private String TeamNumber;
    private String HomeTeamScore;
    private String AwayTeamScore;
    private String GoalTime;

    public String getMatchId() {
        return MatchId;
    }

    public void setMatchId(String matchId) {
        MatchId = matchId;
    }

    public String getTeamNumber() {
        return TeamNumber;
    }

    public void setTeamNumber(String teamNumber) {
        TeamNumber = teamNumber;
    }

    public String getHomeTeamScore() {
        return HomeTeamScore;
    }

    public void setHomeTeamScore(String homeTeamScore) {
        HomeTeamScore = homeTeamScore;
    }

    public String getAwayTeamScore() {
        return AwayTeamScore;
    }

    public void setAwayTeamScore(String awayTeamScore) {
        AwayTeamScore = awayTeamScore;
    }

    public String getGoalTime() {
        return GoalTime;
    }

    public void setGoalTime(String goalTime) {
        GoalTime = goalTime;
    }
}
