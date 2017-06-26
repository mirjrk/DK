package com.zesium.android.betting.model.ticket.create_ticket;

/**
 * Created by Ivan Panic_2 on 2/9/2017.
 */

public class Bet {
    private int Game;
    private int BetData;
    private String Pick;
    private double Odd;
    private boolean Fix;
    private String Sbv;

    public int getGame() {
        return Game;
    }

    public void setGame(int game) {
        Game = game;
    }

    public int getBetData() {
        return BetData;
    }

    public void setBetData(int betData) {
        BetData = betData;
    }

    public String getPick() {
        return Pick;
    }

    public void setPick(String pick) {
        Pick = pick;
    }

    public double getOdd() {
        return Odd;
    }

    public void setOdd(double odd) {
        Odd = odd;
    }

    public boolean isFix() {
        return Fix;
    }

    public void setFix(boolean fix) {
        Fix = fix;
    }

    public String getSbv() {
        return Sbv;
    }

    public void setSbv(String sbv) {
        Sbv = sbv;
    }
}
