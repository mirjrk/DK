package com.zesium.android.betting.model.ticket.create_ticket;

/**
 * Created by Ivan Panic_2 on 2/13/2017.
 */

public enum EBetSlipMessageType {
    MinPayin (0),
    MaxPayin(1),
    MaxBets(2),
    BuyOddsMinOdd(3),
    BuyOddsMinBet(4),
    NotEnoughPoints(5),
    JokerBetSlipMissing(6),
    JokerBetSlipNotActive(7),
    JokerBetSlipLevelMissing(8),
    ClubTicketBonusMissing(9),
    PayInOverMaxPayIn(10),
    MinOverMaxPayIn(11),
    PayInUnderMinPayIn(12),
    Authorization(13),
    TicketRejected(14),
    WalletError(15),
    MinSystemRequired(16),
    OccasionBettingDisabled(17),
    MinPayInPerCombination(18),
    ClubBenefitsNotAvailable(19),
    MaxSystemLimit(20),
    JokerBetSlipMinOdds(21),
    MaxWinLimit(22),
    JokerBetSlipMinBets(23);

    private final int id;

    EBetSlipMessageType(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}
