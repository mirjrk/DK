package com.zesium.android.betting.model.ticket;

import java.util.ArrayList;

/**
 * Created by savic on 11-Oct-16.
 */
public class AwayTeamStatsModel {
    private String Name;
    private ArrayList<GoalsModel> Goals;
    private ArrayList<CardsModel> Cards;
    private Statistic Statistic;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public ArrayList<GoalsModel> getGoals() {
        return Goals;
    }

    public void setGoals(ArrayList<GoalsModel> goals) {
        Goals = goals;
    }

    public ArrayList<CardsModel> getCards() {
        return Cards;
    }

    public void setCards(ArrayList<CardsModel> cards) {
        Cards = cards;
    }

    public Statistic getStatistic() {
        return Statistic;
    }

    public void setStatistic(Statistic statistic) {
        Statistic = statistic;
    }
}
