package com.zesium.android.betting.model;

import com.zesium.android.betting.model.sports.Odd;

import java.io.Serializable;

/**
 * Created by Ivan Panic_2 on 8/31/2016.
 */
public class GridViewItem implements Serializable {
    private boolean isSelected;
    private int offerId;
    private int matchId;
    private Odd odd;
    private boolean mDisabled;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getOfferId() {
        return offerId;
    }

    public void setOfferId(int offerId) {
        this.offerId = offerId;
    }

    public int getMatchId() {
        return matchId;
    }

    public void setMatchId(int matchId) {
        this.matchId = matchId;
    }

    public Odd getOdd() {
        return odd;
    }

    public void setOdd(Odd odd) {
        this.odd = odd;
    }

    public boolean isDisabled() {
        return mDisabled;
    }

    public void setDisabled(boolean mDisabled) {
        this.mDisabled = mDisabled;
    }

}
