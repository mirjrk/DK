package com.zesium.android.betting.model.ticket.create_ticket;

import java.util.List;

/**
 * Created by Ivan Panic_2 on 2/9/2017.
 */

class BetResult {
    private Bet Bet;
    private List<ErrorBet> Errors = null;

    public com.zesium.android.betting.model.ticket.create_ticket.Bet getBet() {
        return Bet;
    }

    public void setBet(com.zesium.android.betting.model.ticket.create_ticket.Bet bet) {
        Bet = bet;
    }

    public List<ErrorBet> getErrors() {
        return Errors;
    }

    public void setErrors(List<ErrorBet> errors) {
        Errors = errors;
    }
}
