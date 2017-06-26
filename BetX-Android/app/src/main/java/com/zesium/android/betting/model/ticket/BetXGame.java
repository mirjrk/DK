package com.zesium.android.betting.model.ticket;

/**
 * Created by savic on 28-Sep-16.
 */
public enum BetXGame {
    Terminal(0),
    SportsBetting(1),
    LiveBetting(2),
    Lotto(3),
    DogsRacing(4),
    Bingo(5),
    HorseRacing(6),
    UKDogsBetting(7),
    SuperTickets(8),
    AstroWorld(9),
    VirtualGames(10),
    Campaign(11),
    VGJackpot(12),
    VirtualSports(13),
    TvVirtualGames(14),
    LiveHorses(15),
    Keno(16),
    GoldenRace(17),
    PoolCupon(50),
    CashCollection(51),
    SlotMachine(52),
    ScratchCards(53),
    JGTerminal(100),
    BingoColorBonus(150),
    BingoNoMatchBonus(151),
    BingodoubleWinBonus(152),
    BingoJackpot(153);

    private final int id;

    BetXGame(int id) {
        this.id = id;
    }

    public int getValue() {
        return id;
    }


}
