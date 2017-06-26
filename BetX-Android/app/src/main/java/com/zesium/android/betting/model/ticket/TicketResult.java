package com.zesium.android.betting.model.ticket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by savic on 28-Sep-16.
 */

public class TicketResult {
    private TicketExtended Ticket;
    private ArrayList<TicketBetBase> Bets;
    private List<TicketSystem> Systems;

    public TicketExtended getTicket() {
        return Ticket;
    }

    public void setTicket(TicketExtended ticket) {
        Ticket = ticket;
    }

    public ArrayList<TicketBetBase> getBets() {
        return Bets;
    }

    public void setBets(ArrayList<TicketBetBase> bets) {
        Bets = bets;
    }

    public List<TicketSystem> getSystems() {
        return Systems;
    }

    public void setSystems(List<TicketSystem> systems) {
        Systems = systems;
    }
}
