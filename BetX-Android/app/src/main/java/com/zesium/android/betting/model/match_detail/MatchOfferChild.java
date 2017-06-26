package com.zesium.android.betting.model.match_detail;

import com.zesium.android.betting.model.GridViewItem;
import com.zesium.android.betting.model.sports.Odd;
import com.zesium.android.betting.model.sports.Offer;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by simikic on 5/18/2017.
 */

class MatchOfferChild implements Serializable {

    private int mItemSpan;
    private int mMatchId;
    private boolean mDisabled;
    private ArrayList<Offer> mGroupedOfferList;
    private ArrayList<Odd> mAllOdds;
    private final ArrayList<GridViewItem> mGridItems;

    private final ArrayList<Odd> mTotalGoals;

    public MatchOfferChild(int matchId) {
        this.mMatchId = matchId;
        this.mGridItems = new ArrayList<>();
        this.mTotalGoals = new ArrayList<>();
    }

    public void setItemSpan(int itemSpan) {
        this.mItemSpan = itemSpan;
    }

    public int getItemSpan() {
        return mItemSpan;
    }

    public int getMatchId() {
        return mMatchId;
    }

    public void setMatchId(int matchId) {
        this.mMatchId = matchId;
    }

    public int getOfferIdByPosition(int position) {
        return mAllOdds.get(position).getOfferId();
    }

    public void setOfferList(ArrayList<Offer> groupedOfferList) {
        this.mGroupedOfferList = groupedOfferList;
    }

    public ArrayList<Offer> getGroupedOfferList() {
        return mGroupedOfferList;
    }

    public void setGroupedOdds(ArrayList<Odd> groupedOdds) {
        this.mAllOdds = groupedOdds;
        for (Odd odd : groupedOdds) {
            GridViewItem item = new GridViewItem();
            if (odd != null) {
                item.setOdd(odd);
                item.setOfferId(odd.getOfferId());
                item.setMatchId(mMatchId);
            }
            mGridItems.add(item);
        }
    }

    public ArrayList<Odd> getGroupedOdds() {
        return mAllOdds;
    }

    public ArrayList<GridViewItem> getGridItems() {
        return mGridItems;
    }

    public Offer getOfferById(int offerId) {
        Offer retOffer = null;
        for (Offer tempOffer : mGroupedOfferList) {
            if (tempOffer.getId() == offerId) {
                retOffer = tempOffer;
                break;
            }
        }

        return retOffer;
    }

    public void setDisabled(boolean disabled) {
        this.mDisabled = disabled;
    }

    public boolean isDisabled() {
        return mDisabled;
    }
}
