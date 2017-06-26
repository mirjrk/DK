package com.zesium.android.betting.model.ticket.create_ticket;

/**
 * Created by Ivan Panic_2 on 2/9/2017.
 */

public class Settings {
    private boolean AcceptAllChanges;
    private boolean AcceptHigherOdds;
    private int OddsChangePercentAccepted;
    private String OddsRepresentation;
    private boolean IsQuickTicket;

    public boolean isAcceptAllChanges() {
        return AcceptAllChanges;
    }

    public void setAcceptAllChanges(boolean acceptAllChanges) {
        AcceptAllChanges = acceptAllChanges;
    }

    public boolean isAcceptHigherOdds() {
        return AcceptHigherOdds;
    }

    public void setAcceptHigherOdds(boolean acceptHigherOdds) {
        AcceptHigherOdds = acceptHigherOdds;
    }

    public int getOddsChangePercentAccepted() {
        return OddsChangePercentAccepted;
    }

    public void setOddsChangePercentAccepted(int oddsChangePercentAccepted) {
        OddsChangePercentAccepted = oddsChangePercentAccepted;
    }

    public String getOddsRepresentation() {
        return OddsRepresentation;
    }

    public void setOddsRepresentation(String oddsRepresentation) {
        OddsRepresentation = oddsRepresentation;
    }

    public boolean isQuickTicket() {
        return IsQuickTicket;
    }

    public void setQuickTicket(boolean quickTicket) {
        IsQuickTicket = quickTicket;
    }
}
