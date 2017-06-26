package com.zesium.android.betting.model.util;

import com.zesium.android.betting.BetXApplication;
import com.zesium.android.betting.model.best_offer.BestOfferItem;
import com.zesium.android.betting.model.sports.AllowedBetTypeCombination;
import com.zesium.android.betting.model.sports.Category;
import com.zesium.android.betting.model.sports.League;
import com.zesium.android.betting.model.sports.Match;
import com.zesium.android.betting.model.sports.Odd;
import com.zesium.android.betting.model.sports.Offer;
import com.zesium.android.betting.model.sports.Sport;
import com.zesium.android.betting.model.sports.live.EChangeOddType;
import com.zesium.android.betting.ui.main.GridViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class that contains all common methods for sport betting.
 *
 * Created by Ivan Panic_2 on 12/7/2016.
 */

public class SportsBettingHelper {

    /**
     * Method deselectPreviousItem deselects previously selected item from same offer.
     *
     * @param gridViewAdapter adapter which needs to be refreshed after select/deselect changes,
     * @param matchId         integer value of match id,
     * @param offerId         integer value of offer id.
     */
    public static void deselectPreviousItem(GridViewAdapter gridViewAdapter, int matchId, int offerId) {
        if (BetXApplication.getApp().getSelectedBetIds().containsKey(matchId)) {
            Match match = BetXApplication.getApp().getSelectedBetIds().get(matchId);
            for (Offer offer : match.getOffers()) {
                if (offer.getId() == offerId) {
                    gridViewAdapter.getData().get(offer.getIndexOfSelectedOdd()).setSelected(false);
                    break;
                }
            }
        }
    }

    /**
     * Method removeOdd removes clicked odd from array where all selected odds are stored.
     *
     * @param matchId integer value of match id,
     * @param offerId integer value of offer id.
     */
    public static void removeOdd(int matchId, int offerId) {

        Match match1 = BetXApplication.getApp().getSelectedBetIds().get(matchId);

        // Check if wanted match exists in selected offer, this check is added for potential problem
        // when recycler view is refreshing and user clicks on it.
        if (match1 != null) {
            if (match1.getOffers().size() == 1) {
                BetXApplication.getApp().getSelectedBetIds().remove(match1.getId());
            } else {
                for (Offer offer : match1.getOffers()) {
                    if (offer.getId() == offerId) {
                        match1.getOffers().remove(offer);
                        break;
                    }
                }
            }
        }
    }

    /**
     * Method addOdd adds clicked odd in array where all selected odds are stored.
     */
    public static boolean addOdd(Match match, int selectedIndex) {
        boolean isFirstMatchOffer;
        if (BetXApplication.getApp().getSelectedBetIds().containsKey(match.getId())) {
            Match match1 = BetXApplication.getApp().getSelectedBetIds().get(match.getId());

            boolean isFound = false;

            for (Offer offer : match1.getOffers()) {
                if (offer.getId() == match.getOffer().getId()) {
                    isFound = true;
                    offer.setIndexOfSelectedOdd(selectedIndex);
                    offer.getOdds().set(0, match.getOffer().getOdds().get(selectedIndex));
                    break;
                }
            }
            if (!isFound) {
                ArrayList<Odd> odds = new ArrayList<>();
                odds.add(match.getOffer().getOdds().get(selectedIndex));

                match.getOffer().setOdds(odds);
                match.getOffer().setIndexOfSelectedOdd(selectedIndex);
                match.getOffer().setDescription(match.getOffer().getDescription());

                match1.getOffers().add(match.getOffer());
            }
            isFirstMatchOffer = false;
        } else {
            //  Match matchForSaving = SerializationUtils.clone(match);

            ArrayList<Odd> odds = new ArrayList<>();
            odds.add(match.getOffer().getOdds().get(selectedIndex));

            ArrayList<Offer> offers = new ArrayList<>();

            match.getOffer().setOdds(odds);
            match.getOffer().setIndexOfSelectedOdd(selectedIndex);
            match.getOffer().setDescription(match.getOffer().getDescription());

            offers.add(match.getOffer());

            match.setOffers(offers);
            match.setOffer(null);

            BetXApplication.getApp().getSelectedBetIds().put(match.getId(), match);
            isFirstMatchOffer = true;
        }

        return isFirstMatchOffer;
    }

    /**
     * Check if some of odd of this match is previously selected.
     *
     * @param matchId id of the match,
     * @param offerId id of the offer.
     */
    public static int findPreviouslySelectedOdd(int matchId, int offerId) {
        if (BetXApplication.getApp().getSelectedBetIds().containsKey(matchId)) {
            Match match = BetXApplication.getApp().getSelectedBetIds().get(matchId);
            for (Offer offer : match.getOffers()) {
                if (offer.getId() == offerId) {
                    return offer.getIndexOfSelectedOdd();
                }
            }
        }
        return -1;

    }

    /**
     * Method that sets if offer is clickable.
     *
     * @param app                     instance of application class,
     * @param basicOfferVerticalChild parent that holds child,
     * @param matchId                 id of the match,
     * @return boolean, if it's true offer is not clickable.
     */
    private static boolean setEnabledOrDisabled(BetXApplication app, BestOfferItem basicOfferVerticalChild, int matchId) {
        boolean isDisabled = false;
        if (app.getSelectedBetIds().get(matchId) != null) {
            for (Offer offer : app.getSelectedBetIds().get(matchId).getOffers()) {
                if (basicOfferVerticalChild.getMatch().getOffer().getId() == offer.getId()) {
                    isDisabled = false;
                    break;
                } else if (offer.getAllowedBetTypeCombinations() == null || offer.getAllowedBetTypeCombinations().size() == 0) {
                    isDisabled = true;
                    break;
                } else if (offer.getAllowedBetTypeCombinations().size() > 0) {
                    boolean isFound = false;
                    for (AllowedBetTypeCombination combination : offer.getAllowedBetTypeCombinations()) {
                        if (basicOfferVerticalChild.getMatch().getOffer().getBetTypeKey().equals(combination.getId())) {
                            isFound = true;
                            break;
                        }
                    }
                    if (!isFound) {
                        isDisabled = true;
                        break;
                    }
                }
            }
        }
        return isDisabled;
    }

    /**
     * Parses data received from server and adds them to list of best offer items.
     * fragment
     *
     * @param betXApplication - Application instance used for parsing non live list.
     * @param sports          - List to be parsed.
     * @param isLive          - Whether or not response is for live matches.
     * @return - List of matches.
     */
    public static ArrayList<BestOfferItem> parseSportListResponse(BetXApplication betXApplication, List<Sport> sports, boolean isLive) {
        ArrayList<BestOfferItem> retBettingOfferList = new ArrayList<>();
        for (Sport sport : sports) {
            for (Category category : sport.getCategories()) {
                for (League league : category.getLeagues()) {
                    for (Match match : league.getMatches()) {
                        BestOfferItem bestOfferItem = new BestOfferItem();
                        bestOfferItem.setLeagueId(league.getId());
                        bestOfferItem.setLeagueName(league.getName());
                        bestOfferItem.setLeagueHeaderSize(league.getHeaders() == null ? 0 : league.getHeaders().size());
                        bestOfferItem.setMatch(match);
                        if (match.getOffer() != null) {
                            for (int i = match.getOffer().getOdds().size() - 1; i > 2; i--) {
                                match.getOffer().getOdds().remove(i);
                            }
                        }

                        //setting click on basic offer (enabled or disabled)
                        int matchId = bestOfferItem.getMatch().getId();

                        if (!isLive) {
                            if (betXApplication.getSelectedBetIds().get(matchId) != null) {
                                for (Offer offer : betXApplication.getSelectedBetIds().get(matchId).getOffers()) {
                                    if (bestOfferItem.getMatch().getOffer().getId() == offer.getId()) {
                                        break;
                                    } else if (offer.getAllowedBetTypeCombinations() == null || offer.getAllowedBetTypeCombinations().size() == 0) {
                                        break;
                                    } else if (offer.getAllowedBetTypeCombinations().size() > 0) {
                                        boolean isFound = false;
                                        for (AllowedBetTypeCombination combination : offer.getAllowedBetTypeCombinations()) {
                                            if (bestOfferItem.getMatch().getOffer().getBetTypeKey().equals(combination.getId())) {
                                                isFound = true;
                                                break;
                                            }
                                        }
                                        if (!isFound) {
                                            break;
                                        }
                                    }
                                }
                            }
                            bestOfferItem.setDisabled(setEnabledOrDisabled(betXApplication, bestOfferItem, matchId));
                        }
                        retBettingOfferList.add(bestOfferItem);
                    }
                }
            }
        }
        return retBettingOfferList;
    }

    /**
     * Method checkFirstThreeOdds checks which odd has increased or decreased and depending on that
     * odd is colored in green or red for 3 seconds.
     *
     * @param currentMatch     current match data,
     * @param changedMatchData changed match with all data,
     * @param oddsSize         headers size which represents number of odds.
     */
    public static synchronized void checkFirstThreeOdds(Match currentMatch, Match changedMatchData, int oddsSize) {
        if (changedMatchData.getOffer() != null && currentMatch.getOffer() != null) {
            for (int i = 0; i < oddsSize; i++) {
                EChangeOddType changeOddType;
                if (currentMatch.getOffer().getOdds().get(i).getOdd() < changedMatchData.getOffer().getOdds().get(i).getOdd()) {
                    changeOddType = EChangeOddType.INCREASE;
                } else if (currentMatch.getOffer().getOdds().get(i).getOdd() > changedMatchData.getOffer().getOdds().get(i).getOdd()) {
                    changeOddType = EChangeOddType.DECREASE;
                } else {
                    changeOddType = EChangeOddType.NONE;
                }
                changedMatchData.getOffer().getOdds().get(i).setChangeOdd(changeOddType);
            }
        }
    }
}
