package com.zesium.android.betting.model.ticket;

/**
 * Created by Ivan Panic_2 on 6/27/2016.
 */
class TicketHistoryModel {
    private int TimeFilterId;
    private int Year;
    private int Offset;
    private int Limit;
    private int StatusId;
    private int GameId;
    private String Username;

    public int getTimeFilterId() {
        return TimeFilterId;
    }

    public void setTimeFilterId(int timeFilterId) {
        TimeFilterId = timeFilterId;
    }

    public int getYear() {
        return Year;
    }

    public void setYear(int year) {
        Year = year;
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

    public int getStatusId() {
        return StatusId;
    }

    public void setStatusId(int statusId) {
        StatusId = statusId;
    }

    public int getGameId() {
        return GameId;
    }

    public void setGameId(int gameId) {
        GameId = gameId;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
}
