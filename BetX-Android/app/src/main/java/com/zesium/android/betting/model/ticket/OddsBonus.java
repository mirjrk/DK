package com.zesium.android.betting.model.ticket;

/**
 * Created by savic on 28-Sep-16.
 */

public class OddsBonus {
    private BetSlipOddBonusMode OddBonusMode;
    private String OddBonusModeTranslatedName;
    private Double OddMultiplier;

    public BetSlipOddBonusMode getOddBonusMode() {
        return OddBonusMode;
    }

    public void setOddBonusMode(BetSlipOddBonusMode oddBonusMode) {
        OddBonusMode = oddBonusMode;
    }

    public String getOddBonusModeTranslatedName() {
        return OddBonusModeTranslatedName;
    }

    public void setOddBonusModeTranslatedName(String oddBonusModeTranslatedName) {
        OddBonusModeTranslatedName = oddBonusModeTranslatedName;
    }

    public Double getOddMultiplier() {
        return OddMultiplier;
    }

    public void setOddMultiplier(Double oddMultiplier) {
        OddMultiplier = oddMultiplier;
    }
}
