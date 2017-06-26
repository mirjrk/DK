package com.zesium.android.betting.model.ticket;

/**
 * Created by savic on 11-Oct-16.
 */

public class Statistic {
    private String MatchId;
    private String TeamId;
    private String Corners;

    public String getMatchId() {
        return MatchId;
    }

    public void setMatchId(String matchId) {
        MatchId = matchId;
    }

    public String getTeamId() {
        return TeamId;
    }

    public void setTeamId(String teamId) {
        TeamId = teamId;
    }

    public String getCorners() {
        return Corners;
    }

    public void setCorners(String corners) {
        Corners = corners;
    }
}
