package com.zesium.android.betting.model.ticket;

import java.util.ArrayList;

/**
 * Result class that has fields defined by response from server, and it is main structure when returning tickets for user.
 * Created by Ivan Panic on 12/23/2015.
 */
public class Result {

    private ArrayList<Ticket> Tickets;
    private int TicketCount;

    public ArrayList<Ticket> getTickets() {
        return Tickets;
    }

    public void setTickets(ArrayList<Ticket> tickets) {
        Tickets = tickets;
    }

    public int getTicketCount() {
        return TicketCount;
    }

    public void setTicketCount(int ticketCount) {
        TicketCount = ticketCount;
    }
}
