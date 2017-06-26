package com.zesium.android.betting.model.sports;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Basic offer class that has fields defined by response from server.
 * Created by Ivan Panic on 12/18/2015.
 */
public class Offer implements Serializable, Comparable<Offer> {
    private int Id;
    private Boolean Active;
    private String Description;
    private String OriginDescription;
    private List<Odd> Odds;
    private String BetTypeKey;
    private String Sbv;
    private String SbvText;
    private int indexOfSelectedOdd = -1;
    private int Ordering;
    private List<AllowedBetTypeCombination> AllowedBetTypeCombinations;
    private boolean isFix;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public List<Odd> getOdds() {
        return Odds;
    }

    public void setOdds(List<Odd> odds) {
        Odds = odds;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getBetTypeKey() {
        return BetTypeKey;
    }

    public void setBetTypeKey(String betTypeKey) {
        BetTypeKey = betTypeKey;
    }

    public String getSbv() {
        if (Sbv != null && !Sbv.isEmpty()) {
            return Sbv;
        }
        if (SbvText != null && !SbvText.isEmpty()) {
            return SbvText;
        }
        return "";
    }

    public void setSbv(String sbv) {
        Sbv = sbv;
    }

    public List<AllowedBetTypeCombination> getAllowedBetTypeCombinations() {
        return AllowedBetTypeCombinations;
    }

    public void setAllowedBetTypeCombinations(List<AllowedBetTypeCombination> allowedBetTypeCombinations) {
        AllowedBetTypeCombinations = allowedBetTypeCombinations;
    }

    public int getIndexOfSelectedOdd() {
        return indexOfSelectedOdd;
    }

    public void setIndexOfSelectedOdd(int indexOfSelectedOdd) {
        this.indexOfSelectedOdd = indexOfSelectedOdd;
    }

    public boolean isFix() {
        return isFix;
    }

    public void setFix(boolean fix) {
        isFix = fix;
    }

    public int getOrdering() {
        return Ordering;
    }

    public void setOrdering(int ordering) {
        Ordering = ordering;
    }

    public Boolean isActive() {
        return Active;
    }

    public void setActive(Boolean active) {
        Active = active;
    }

    public String getSbvText() {
        return SbvText;
    }

    public void setSbvText(String sbvText) {
        SbvText = sbvText;
    }

    public String getOriginDescription() {
        return OriginDescription;
    }

    public void setOriginDescription(String originDescription) {
        OriginDescription = originDescription;
    }

    @Override
    public int compareTo(@NonNull Offer o) {
        return this.Odds.get(0).getName().compareTo(o.getOdds().get(0).getName());
    }

    public ArrayList<String> getAllowedBetTypeCombinationsList() {
        ArrayList<String> ret = new ArrayList<>();
        if (AllowedBetTypeCombinations != null) {
            for (AllowedBetTypeCombination temp : AllowedBetTypeCombinations) {
                ret.add(temp.getId());
            }
        }
        return ret;
    }
}
