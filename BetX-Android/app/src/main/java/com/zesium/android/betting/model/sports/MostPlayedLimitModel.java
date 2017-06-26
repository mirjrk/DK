package com.zesium.android.betting.model.sports;

/**
 * Created by Ivan Panic_2 on 11/2/2016.
 */

public class MostPlayedLimitModel {
    private int Limit;
    private String SportId;

    public int getLimit() {
        return Limit;
    }

    public void setLimit(int limit) {
        Limit = limit;
    }

    public String getSportId() {
        return SportId;
    }

    public void setSportId(String sportId) {
        SportId = sportId;
    }
}
