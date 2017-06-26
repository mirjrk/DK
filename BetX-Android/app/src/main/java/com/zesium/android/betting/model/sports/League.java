package com.zesium.android.betting.model.sports;

import java.io.Serializable;
import java.util.List;

/**
 * League class that has fields defined by response from server and contains all data for one league.
 * Created by Ivan Panic on 12/17/2015.
 */
public class League implements Serializable {
    private int Id;
    private int MatchesCount;
    private String Name;
    private int CommonLeagueId;
    private boolean HasLeagueTables;
    private int Ordering;
    private int SpecialOrd;
    private List<Match> Matches;
    private List<String> Headers;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getMatchesCount() {
        return MatchesCount;
    }

    public void setMatchesCount(int matchesCount) {
        MatchesCount = matchesCount;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public List<Match> getMatches() {
        return Matches;
    }

    public void setMatches(List<Match> matches) {
        Matches = matches;
    }

    public boolean isHasLeagueTables() {
        return HasLeagueTables;
    }

    public void setHasLeagueTables(boolean hasLeagueTables) {
        HasLeagueTables = hasLeagueTables;
    }

    public List<String> getHeaders() {
        return Headers;
    }

    public void setHeaders(List<String> headers) {
        Headers = headers;
    }

    public int getCommonLeagueId() {
        return CommonLeagueId;
    }

    public void setCommonLeagueId(int commonLeagueId) {
        CommonLeagueId = commonLeagueId;
    }

    public int getOrdering() {
        return Ordering;
    }

    public void setOrdering(int ordering) {
        Ordering = ordering;
    }

    public int getSpecialOrd() {
        return SpecialOrd;
    }

    public void setSpecialOrd(int specialOrd) {
        SpecialOrd = specialOrd;
    }
}
