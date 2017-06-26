package com.zesium.android.betting.model.ticket;

import java.util.ArrayList;

/**
 * Bet class that has fields defined by response from server, and contains all basic data for one match in ticket.
 * Created by Ivan Panic on 12/23/2015.
 */
class Bet {
    private String Pick;
    private String EventDescription2;
    private String EventDescription3;
    private String EventTime;
    private ArrayList<Odd> Odds;
    private String Result;
    private String Status;

    public String getPick() {
        return Pick;
    }

    public void setPick(String pick) {
        Pick = pick;
    }

    public String getEventDescription2() {
        return EventDescription2;
    }

    public void setEventDescription2(String eventDescription2) {
        EventDescription2 = eventDescription2;
    }

    public String getEventTime() {
        return EventTime;
    }

    public void setEventTime(String eventTime) {
        EventTime = eventTime;
    }

    public String getEventDescription3() {
        return EventDescription3;
    }

    public void setEventDescription3(String eventDescription3) {
        EventDescription3 = eventDescription3;
    }

    public ArrayList<Odd> getOdds() {
        return Odds;
    }

    public void setOdds(ArrayList<Odd> odds) {
        Odds = odds;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
