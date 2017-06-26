package com.zesium.android.betting.model;

import com.zesium.android.betting.model.sports.TeamStatistics;

import java.io.Serializable;
import java.util.List;

/**
 * Created by savic on 24-Aug-16.
 */
class TableOfLeagueSportsBettingModel implements Serializable{

    private List<TeamStatistics> TeamStatistics;
    private String LeagueName;

    public String getLeagueName() {
        return LeagueName;
    }

    public void setLeagueName(String leagueName) {
        LeagueName = leagueName;
    }

    public List<com.zesium.android.betting.model.sports.TeamStatistics> getTeamStatistics() {
        return TeamStatistics;
    }

    public void setTeamStatistics(List<com.zesium.android.betting.model.sports.TeamStatistics> teamStatistics) {
        TeamStatistics = teamStatistics;
    }

    public List<TeamStatistics> getTeamStatisticsList() {
        return TeamStatistics;
    }

    public void setTeamStatisticsList(List<TeamStatistics> teamStatisticsList) {
        this.TeamStatistics = teamStatisticsList;
    }

}






