package com.zesium.android.betting.model.ticket;

/**
 * Created by savic on 28-Sep-16.
 */
class TicketSystemCombination {
    private String CombinationDescription;
    private Double StakePerCombination;
    private Double CombinationOdds;
    private Double CombinationWinAmount;

    public String getCombinationDescription() {
        return CombinationDescription;
    }

    public void setCombinationDescription(String combinationDescription) {
        CombinationDescription = combinationDescription;
    }

    public Double getStakePerCombination() {
        return StakePerCombination;
    }

    public void setStakePerCombination(Double stakePerCombination) {
        StakePerCombination = stakePerCombination;
    }

    public Double getCombinationOdds() {
        return CombinationOdds;
    }

    public void setCombinationOdds(Double combinationOdds) {
        CombinationOdds = combinationOdds;
    }

    public Double getCombinationWinAmount() {
        return CombinationWinAmount;
    }

    public void setCombinationWinAmount(Double combinationWinAmount) {
        CombinationWinAmount = combinationWinAmount;
    }
}
