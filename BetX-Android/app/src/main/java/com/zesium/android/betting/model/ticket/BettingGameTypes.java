package com.zesium.android.betting.model.ticket;

/**
 * Created by savic on 28-Sep-16.
 */
public enum BettingGameTypes {
    None(200),
    AnyGameBet(0),
    SportsBet(1),
    VirtualDogsBet(2),
    UKDogsBet(3),
    HorsesBet(4),
    BingoBet(5),
    LottoBet(6),
    AstroWorldBet(7),
    VirtualGamesBet(8),
    Campaign(11),
    VGJackpot(12),
    VirtualSportsBet(13),
    TVVirtualGames(14),
    LiveHorsesBet(15),
    Keno(16),
    GoldenVirtualGames(17),
    JGTerminal(100),
    BingoColorBonus(150),
    BingoNoMatchBonus(151),
    BingoDoubleWinBonus(152),
    BingoJackpot(153);

    private final int id;

    BettingGameTypes(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }
}
