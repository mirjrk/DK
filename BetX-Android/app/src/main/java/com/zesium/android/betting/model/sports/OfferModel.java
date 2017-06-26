package com.zesium.android.betting.model.sports;

import java.util.List;

/**
 * Created by Ivan Panic_2 on 7/18/2016.
 */
class OfferModel {
    private List<Integer> SportIds;
    private List<Integer> CategoryIds;
    private List<Integer> LeagueIds;
    private String DateFrom;
    private String DateTo;
    private int Offset;
    private int Limit;
    private String OddsFilter;

    public List<Integer> getSportIds() {
        return SportIds;
    }

    public void setSportIds(List<Integer> sportIds) {
        SportIds = sportIds;
    }

    public List<Integer> getCategoryIds() {
        return CategoryIds;
    }

    public void setCategoryIds(List<Integer> categoryIds) {
        CategoryIds = categoryIds;
    }

    public List<Integer> getLeagueIds() {
        return LeagueIds;
    }

    public void setLeagueIds(List<Integer> leagueIds) {
        LeagueIds = leagueIds;
    }

    public String getDateFrom() {
        return DateFrom;
    }

    public void setDateFrom(String dateFrom) {
        DateFrom = dateFrom;
    }

    public String getDateTo() {
        return DateTo;
    }

    public void setDateTo(String dateTo) {
        DateTo = dateTo;
    }

    public int getOffset() {
        return Offset;
    }

    public void setOffset(int offset) {
        Offset = offset;
    }

    public int getLimit() {
        return Limit;
    }

    public void setLimit(int limit) {
        Limit = limit;
    }

    public String getOddsFilter() {
        return OddsFilter;
    }

    public void setOddsFilter(String oddsFilter) {
        OddsFilter = oddsFilter;
    }
}
