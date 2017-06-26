package com.zesium.android.betting.model.best_offer;

import com.zesium.android.betting.model.sports.Match;

/**
 * Class that defines all necessarily data for one best offer match.
 * Created by Ivan Panic on 12/16/2015.
 */
public class BestOfferItem {
    private Match mMatch;
    private int mButtonSelected = -1;
    private int mLeagueId;
    private String mLeagueName;
    private boolean disabled;
    private int mLeagueHeaderSize;

    public BestOfferItem() {
    }

    public int getLeagueId() {
        return mLeagueId;
    }

    public void setLeagueId(int leagueId) {
        mLeagueId = leagueId;
    }

    public Match getMatch() {
        return mMatch;
    }

    public void setMatch(Match match) {
        mMatch = match;
    }

    public int getButtonSelected() {
        return mButtonSelected;
    }

    public void setButtonSelected(int buttonSelected) {
        mButtonSelected = buttonSelected;
    }

    public String getLeagueName() {
        return mLeagueName;
    }

    public void setLeagueName(String leagueName) {
        mLeagueName = leagueName;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public int getLeagueHeaderSize() {
        return mLeagueHeaderSize;
    }

    public void setLeagueHeaderSize(int mLeagueHeaderSize) {
        this.mLeagueHeaderSize = mLeagueHeaderSize;
    }
}
