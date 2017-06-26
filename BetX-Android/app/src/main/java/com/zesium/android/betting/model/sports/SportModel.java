package com.zesium.android.betting.model.sports;

import java.util.List;

/**
 * Created by Ivan Panic_2 on 7/18/2016.
 */
class SportModel {
    private String DateFrom;
    private String DateTo;
    private List<Integer> SportIds;
    private String OddsFilter;

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

    public List<Integer> getSportIds() {
        return SportIds;
    }

    public void setSportIds(List<Integer> sportIds) {
        SportIds = sportIds;
    }

    public String getOddsFilter() {
        return OddsFilter;
    }

    public void setOddsFilter(String oddsFilter) {
        OddsFilter = oddsFilter;
    }
}
