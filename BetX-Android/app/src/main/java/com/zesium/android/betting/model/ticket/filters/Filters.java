package com.zesium.android.betting.model.ticket.filters;

import java.util.ArrayList;

/**
 * Created by Ivan Panic_2 on 6/22/2016.
 */
class Filters {
    private ArrayList<NameValuePair> GameFilters;
    private ArrayList<NameValuePair> StatusFilters;
    private ArrayList<NameValuePair> TimeFilters;

    public ArrayList<NameValuePair> getGameFilters() {
        return GameFilters;
    }

    public void setGameFilters(ArrayList<NameValuePair> gameFilters) {
        GameFilters = gameFilters;
    }

    public ArrayList<NameValuePair> getStatusFilters() {
        return StatusFilters;
    }

    public void setStatusFilters(ArrayList<NameValuePair> statusFilters) {
        StatusFilters = statusFilters;
    }

    public ArrayList<NameValuePair> getTimeFilters() {
        return TimeFilters;
    }

    public void setTimeFilters(ArrayList<NameValuePair> timeFilters) {
        TimeFilters = timeFilters;
    }
}
