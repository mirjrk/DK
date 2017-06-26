package com.zesium.android.betting.model.ticket;

/**
 * Created by Ivan Panic_2 on 6/27/2016.
 */
class TicketResponse {
    private Result Result;
    private int TicketCount;

    public com.zesium.android.betting.model.ticket.Result getResult() {
        return Result;
    }

    public void setResult(com.zesium.android.betting.model.ticket.Result result) {
        Result = result;
    }

    public int getTicketCount() {
        return TicketCount;
    }

    public void setTicketCount(int ticketCount) {
        TicketCount = ticketCount;
    }
}
