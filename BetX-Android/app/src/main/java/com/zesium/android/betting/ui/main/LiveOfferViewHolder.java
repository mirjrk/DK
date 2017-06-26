package com.zesium.android.betting.ui.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zesium.android.betting.BetXApplication;
import com.zesium.android.betting.R;
import com.zesium.android.betting.model.GridViewItem;
import com.zesium.android.betting.model.best_offer.BestOfferItem;
import com.zesium.android.betting.model.sports.Match;
import com.zesium.android.betting.model.sports.Odd;
import com.zesium.android.betting.model.sports.Offer;
import com.zesium.android.betting.model.util.SportsBettingHelper;
import com.zesium.android.betting.model.util.TranslationConstants;
import com.zesium.android.betting.utils.AppConstants;
import com.zesium.android.betting.utils.AppLogger;
import com.zesium.android.betting.utils.AppUtils;

import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;

/**
 * View holder for live offer match.
 * Created by simikic on 5/5/2017.
 */

class LiveOfferViewHolder extends RecyclerView.ViewHolder {
    private static final String TAG = LiveOfferViewHolder.class.getSimpleName();

    private final LinearLayout mMatchHeaderLayout;
    private final GridView mGroupOffers;
    private final ImageView mAwayTeamServe;
    private final ImageView mHomeTeamServe;
    private final TextView mDetailsScore;
    private final TextView mTvTime;
    private final TextView mScore;
    private final TextView mHomeTeam;
    private final TextView mAwayTeam;
    private final Context mContext;
    private final IFloatingTicket mTicketInterface;

    private final String mTennisAdvanced;

    LiveOfferViewHolder(View itemView, IFloatingTicket ticketInterface) {
        super(itemView);

        mContext = itemView.getContext();
        mTicketInterface = ticketInterface;

        mHomeTeam = (TextView) itemView.findViewById(R.id.math_home_team);
        mAwayTeam = (TextView) itemView.findViewById(R.id.math_away_team);
        mScore = (TextView) itemView.findViewById(R.id.tv_score);
        mDetailsScore = (TextView) itemView.findViewById(R.id.tv_detail);
        mTvTime = (TextView) itemView.findViewById(R.id.tv_time);
        mHomeTeamServe = (ImageView) itemView.findViewById(R.id.iv_tennis_serve_home_team);
        mAwayTeamServe = (ImageView) itemView.findViewById(R.id.iv_tennis_serve_away_team);
        mGroupOffers = (GridView) itemView.findViewById(R.id.gv_group_offer);
        mMatchHeaderLayout = (LinearLayout) itemView.findViewById(R.id.ll_match_header_layout);

        if (BetXApplication.translationMap.containsKey(TranslationConstants.ADV)) {
            mTennisAdvanced = BetXApplication.translationMap.get(TranslationConstants.ADV);
        } else {
            mTennisAdvanced = mContext.getString(R.string.live_betting_tennis_adv);
        }
    }

    /**
     * Updated view to represent the contents of the #liveOfferItem.
     *
     * @param liveOfferItem - Model object that contains all details for current match.
     */
    void bind(BestOfferItem liveOfferItem) {
        int ems;
        switch (mContext.getResources().getDisplayMetrics().densityDpi) {
            case DisplayMetrics.DENSITY_LOW:
            case DisplayMetrics.DENSITY_MEDIUM:
            case DisplayMetrics.DENSITY_HIGH:
                ems = 5;
                break;
            case DisplayMetrics.DENSITY_XHIGH:
            case DisplayMetrics.DENSITY_XXHIGH:
            case DisplayMetrics.DENSITY_XXXHIGH:
            default:
                ems = 7;
                break;
        }

        setMatchDetailsClickListener(mMatchHeaderLayout, liveOfferItem);

        //setup item header
        mHomeTeam.setMaxEms(ems);
        mAwayTeam.setMaxEms(ems);
        mHomeTeam.setText(liveOfferItem.getMatch().getTeamHome());
        mAwayTeam.setText(liveOfferItem.getMatch().getTeamAway());
        if (liveOfferItem.getMatch().getLiveMatchScore() != null
                && !liveOfferItem.getMatch().getLiveMatchScore().isEmpty()) {
            mScore.setText(liveOfferItem.getMatch().getLiveMatchScore());
        } else {
            mScore.setText(mContext.getString(R.string.match_vs_value));
        }
        setTennisServeData(liveOfferItem);

        // setup item body
        // Clear text
        mDetailsScore.setText("");
        mTvTime.setText("");
        if (liveOfferItem.getMatch().getOriginSportName().equals((AppConstants.TENNIS))
                || liveOfferItem.getMatch().getOriginSportName().equals(AppConstants.VOLLEYBALL)) {
            if (liveOfferItem.getMatch().getLiveSetScore() != null
                    && !liveOfferItem.getMatch().getLiveSetScore().isEmpty()) {
                String detailsScoreText = liveOfferItem.getMatch().getLiveSetScore();

                if (liveOfferItem.getMatch().getLiveGameScore() != null
                        && !liveOfferItem.getMatch().getLiveGameScore().isEmpty()) {
                    detailsScoreText = "(" + liveOfferItem.getMatch().getLiveSetScore() + ") " + liveOfferItem.getMatch().getLiveGameScore();
                }

                if (liveOfferItem.getMatch().getOriginSportName().equals((AppConstants.TENNIS))
                        && detailsScoreText.contains("50")) {
                    detailsScoreText = detailsScoreText.replace("50", mTennisAdvanced);
                }

                mDetailsScore.setText(detailsScoreText);
            } else if (liveOfferItem.getMatch().getLiveMatchScore() != null && !liveOfferItem.getMatch().getLiveMatchScore().isEmpty()) {
                mDetailsScore.setText(liveOfferItem.getMatch().getLiveMatchScore());
            }
        } else {
            if (liveOfferItem.getMatch().getLiveMatchTimeState() != null
                    && !liveOfferItem.getMatch().getLiveMatchTimeState().isEmpty()) {
                mDetailsScore.setText(liveOfferItem.getMatch().getLiveMatchTimeState());
            }

            if (liveOfferItem.getMatch().getLiveMatchTime() != null
                    && !liveOfferItem.getMatch().getLiveMatchTime().isEmpty()) {
                mTvTime.setText(" - " + liveOfferItem.getMatch().getLiveMatchTime() + "'");
            }
        }

        setOfferLayout(liveOfferItem);
    }

    /**
     * Method setTennisServeData shows tennis icon beside player who is currently serving.
     *
     * @param liveOfferItem - Model object that contains all details for current match.
     */
    private void setTennisServeData(BestOfferItem liveOfferItem) {
        if (liveOfferItem.getMatch().getLiveServer() != null && !liveOfferItem.getMatch().getLiveServer().isEmpty()) {
            switch (liveOfferItem.getMatch().getLiveServer()) {
                case "1":
                    mHomeTeamServe.setVisibility(View.VISIBLE);
                    mAwayTeamServe.setVisibility(View.INVISIBLE);
                    break;
                case "2":
                    mAwayTeamServe.setVisibility(View.VISIBLE);
                    mHomeTeamServe.setVisibility(View.INVISIBLE);
                    break;
                default:
                    mAwayTeamServe.setVisibility(View.GONE);
                    mHomeTeamServe.setVisibility(View.GONE);
                    break;
            }
        } else {
            mAwayTeamServe.setVisibility(View.GONE);
            mHomeTeamServe.setVisibility(View.GONE);
        }
    }

    /**
     * This method initialises offer layout for one match. Offer layout contains grid view with all
     * odds for that offer.
     *
     * @param liveOfferItem - Model object that contains all details for current match.
     */
    private void setOfferLayout(BestOfferItem liveOfferItem) {
        ArrayList<GridViewItem> mGridItems = new ArrayList<>();
        GridViewAdapter mGridViewAdapter = new GridViewAdapter(mContext, mGridItems);
        mGroupOffers.setAdapter(mGridViewAdapter);

        if (liveOfferItem.getMatch().getOffer() != null) {
            if (liveOfferItem.getMatch().getOffer().getOdds().size() <= 2) {
                mGroupOffers.setNumColumns(2);
            } else {
                mGroupOffers.setNumColumns(3);
            }

            for (Odd odd : liveOfferItem.getMatch().getOffer().getOdds()) {
                GridViewItem item = new GridViewItem();
                if (odd != null) {
                    item.setOdd(odd);
                    item.setOfferId(liveOfferItem.getMatch().getOffer().getId());
                    item.setMatchId(liveOfferItem.getMatch().getId());
                }
                mGridItems.add(item);
            }

            mGroupOffers.setOnItemClickListener((adapterView, view, i, l) -> {
                GridViewItem item = (GridViewItem) adapterView.getItemAtPosition(i);
                AppLogger.d(TAG, "Grid view clicked @ " + i);
                setBetSelected(mContext, mGridViewAdapter, item, liveOfferItem);
            });
        } else {
            AppLogger.e(TAG, "Position: " + getAdapterPosition() + " offer count: " + liveOfferItem.getLeagueHeaderSize());
            int itemCount;
            if (liveOfferItem.getLeagueHeaderSize() > 2) {
                itemCount = 3;
            } else {
                itemCount = 2;
            }
            mGroupOffers.setNumColumns(itemCount);
            for (int i = 0; i < itemCount; i++) {
                GridViewItem item = new GridViewItem();
                mGridItems.add(item);
            }
        }
        mGridViewAdapter.notifyDataSetChanged();
    }

    /**
     * Method setBetSelected selects/deselects one bet type when user click on it.
     *
     * @param context         - Application context.
     * @param gridViewAdapter - Adapter of grid view.
     * @param item            - Item from grid view.
     * @param liveOfferItem   - Model object that contains all details for current match.
     */
    private void setBetSelected(Context context, GridViewAdapter gridViewAdapter, final GridViewItem item, BestOfferItem liveOfferItem) {
        if (item.isSelected()) {
            // Remove clicked odd and it's offer from saved clicked items
            SportsBettingHelper.removeOdd(item.getMatchId(), item.getOfferId());

            // Deselect item on view
            item.setSelected(!item.isSelected());

            // If that is only selected odd/offer, remove floating view
            mTicketInterface.updateFloatingTicket();
        } else {
            item.setSelected(!item.isSelected());

            // Deselect previously selected item because only one item in offer can be selected
            SportsBettingHelper.deselectPreviousItem(gridViewAdapter, item.getMatchId(), item.getOfferId());

            // Create clone of the selected match/offer/odd so it can be saved in application class
            Match selectedMatch = SerializationUtils.clone(liveOfferItem.getMatch());
            Offer selectedOffer = SerializationUtils.clone(liveOfferItem.getMatch().getOffer());
            selectedOffer.setIndexOfSelectedOdd(gridViewAdapter.getPosition(item));
            selectedMatch.setOffer(selectedOffer);
            SportsBettingHelper.addOdd(selectedMatch, gridViewAdapter.getPosition(item));

            // Show or hide ticket floating view
            mTicketInterface.updateFloatingTicket();
        }
        gridViewAdapter.notifyDataSetChanged();

        // Send broadcast to refresh other view's
        AppUtils.sendDetailMatchOfferBroadcast(context, item.getOfferId(), item.getOdd().getId(), AppConstants.STATISTICS);
        AppUtils.sendUpdateListBroadcast(context);
    }

    /**
     * Method setMatchDetailsClickListener sets on click listener for every match and leads to detail
     * match offer.
     *
     * @param matchHeaderLayout - Linear layout of match view.
     * @param liveOfferItem     - Model object that contains all details for current match.
     */
    private void setMatchDetailsClickListener(LinearLayout matchHeaderLayout, BestOfferItem liveOfferItem) {
        matchHeaderLayout.setOnClickListener(view -> Toast.makeText(mContext, "Disabled", Toast.LENGTH_SHORT).show());
    }


}