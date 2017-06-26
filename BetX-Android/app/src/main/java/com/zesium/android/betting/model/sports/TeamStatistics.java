package com.zesium.android.betting.model.sports;

import java.io.Serializable;
import java.util.List;

/**
 * Created by savic on 01-Sep-16.
 */
public class TeamStatistics implements Serializable{
    private String TeamId;
    private String TeamName;
    private String Position;
    private List<StatisticData> StatisticData;

    public String getTeamId() {
        return TeamId;
    }

    public void setTeamId(String teamId) {
        TeamId = teamId;
    }

    public String getTeamName() {
        return TeamName;
    }

    public void setTeamName(String teamName) {
        TeamName = teamName;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public List<com.zesium.android.betting.model.sports.StatisticData> getStatisticData() {
        return StatisticData;
    }

    public void setStatisticData(List<com.zesium.android.betting.model.sports.StatisticData> statisticData) {
        StatisticData = statisticData;
    }
}
