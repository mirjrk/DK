package com.zesium.android.betting.model.match_detail;

import android.support.annotation.NonNull;

import com.bignerdranch.expandablerecyclerview.model.ParentListItem;
import com.zesium.android.betting.model.sports.Odd;
import com.zesium.android.betting.model.sports.Offer;
import com.zesium.android.betting.model.sports.PrematchInfo;
import com.zesium.android.betting.utils.AppConstants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by simikic on 5/18/2017.
 */

class MatchOffer implements ParentListItem, Comparable<MatchOffer>, Serializable {

    private String mOfferName;
    private String mBetTypeKey;
    private int mMatchId;
    private int mOfferType;
    private int mOrdering;

    private PrematchInfo mPrematchInfo;
    private boolean mPrematchBoundState;

    private boolean mExpanded;
    private boolean mOfferDisabled;

    private ArrayList<MatchOfferChild> mChildContent;

    public MatchOffer() {
        this.mExpanded = false;
    }

    public MatchOffer(ArrayList<Offer> groupedOfferList, int matchId) {
        this.mOfferType = AppConstants.PARENT_MATCH_DETAILS_DEFAULT;
        this.mExpanded = false;
        this.mMatchId = matchId;
        this.mOfferName = groupedOfferList.get(0).getOriginDescription();
        this.mOrdering = groupedOfferList.get(0).getOrdering();
        this.mBetTypeKey = groupedOfferList.get(0).getBetTypeKey();
        // Modify odd name to include sbv
        for (Offer tempOffer : groupedOfferList) {
            String offerSbv = tempOffer.getSbv();

            for (Odd tempOdd : tempOffer.getOdds()) {
                if (!offerSbv.isEmpty()) {
                    tempOdd.setName(tempOdd.getName() + " (" + offerSbv + ")");
                }
                tempOdd.setOfferId(tempOffer.getId());
                tempOdd.setBetTypeKey(tempOffer.getBetTypeKey());
            }
        }

        Collections.sort(groupedOfferList);

        ArrayList<Odd> groupedOdds = new ArrayList<>();
        for (Offer tempOffer : groupedOfferList) {
            groupedOdds.addAll(tempOffer.getOdds());
        }

        MatchOfferChild childItem = new MatchOfferChild(matchId);
        childItem.setGroupedOdds(groupedOdds);
        childItem.setOfferList(groupedOfferList);

        if (groupedOfferList.get(0).getOdds().size() <= 2) {
            childItem.setItemSpan(2);
        } else {
            childItem.setItemSpan(3);
        }

        mChildContent = new ArrayList<>();
        mChildContent.add(childItem);
    }

    public int getOfferType() {
        return mOfferType;
    }

    public void setOfferType(int offerType) {
        this.mOfferType = offerType;
    }

    public String getOfferName() {
        return mOfferName;
    }

    public void setOfferName(String offerName) {
        this.mOfferName = offerName;
    }

    public PrematchInfo getPrematchInfo() {
        return mPrematchInfo;
    }

    public void setPrematchInfo(PrematchInfo prematchInfo) {
        this.mPrematchInfo = prematchInfo;
    }

    public boolean isPrematchBound() {
        return mPrematchBoundState;
    }

    public void setPrematchBound(boolean boundState) {
        this.mPrematchBoundState = boundState;
    }

    public boolean isOfferDisabled() {
        return mOfferDisabled;
    }

    public void setOfferDisabled(boolean disabled) {
        this.mOfferDisabled = disabled;
        this.getChildItemList().get(0).setDisabled(disabled);
    }

    public boolean isExpanded() {
        return mExpanded;
    }

    public void setExpanded(boolean expanded) {
        this.mExpanded = expanded;
    }

    private int getOrdering() {
        return mOrdering;
    }


    public int getMatchId() {
        return mMatchId;
    }

    public String getBetTypeKey() {
        return mBetTypeKey;
    }

    @Override
    public ArrayList<MatchOfferChild> getChildItemList() {
        return mChildContent;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return mExpanded;
    }

    @Override
    public int compareTo(@NonNull MatchOffer o) {
        if (this.mOrdering == o.getOrdering()) {
            return 0;
        }
        if (this.mOrdering < o.getOrdering()) {
            return -1;
        }
        return 1;
    }
}
