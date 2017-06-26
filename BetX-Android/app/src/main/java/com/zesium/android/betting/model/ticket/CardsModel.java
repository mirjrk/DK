package com.zesium.android.betting.model.ticket;

/**
 * Created by savic on 11-Oct-16.
 */
class CardsModel {
    private String MatchId;
    private String TeamNumber;
    private String CardColor;

    public String getMatchId() {
        return MatchId;
    }

    public void setMatchId(String matchId) {
        MatchId = matchId;
    }

    public String getTeamNumber() {
        return TeamNumber;
    }

    public void setTeamNumber(String teamNumber) {
        TeamNumber = teamNumber;
    }

    public String getCardColor() {
        return CardColor;
    }

    public void setCardColor(String cardColor) {
        CardColor = cardColor;
    }
}
