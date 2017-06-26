package com.zesium.android.betting.model.configuration;

import java.util.List;

/**
 * Created by Ivan Panic_2 on 7/18/2016.
 */
public class SportOfferConfiguration {
    private int SpecialSportOrderingLimit;
    private int NumberOfExpandedSportsInMainMenu;
    private int NumberOfSportsInSpecialSportMenu;
    private int NumberOfLiveMatchesOnMainScreen;
    private int SportNumberOfMatchesPerPage;
    private double SportOfferBonusPercent;
    private double LiveOfferBonusPercent;
    private double MinOddsForShowing;
    private boolean SportOfferAuthorizeEnabled;
    private String SpecialSportBrandName;
    private String DateShortPattern;
    private String DateLongPattern;
    private String BetRadarClientId;
    private List<String> OddsFilter;

    public int getSpecialSportOrderingLimit() {
        return SpecialSportOrderingLimit;
    }

    public void setSpecialSportOrderingLimit(int specialSportOrderingLimit) {
        SpecialSportOrderingLimit = specialSportOrderingLimit;
    }

    public int getNumberOfExpandedSportsInMainMenu() {
        return NumberOfExpandedSportsInMainMenu;
    }

    public void setNumberOfExpandedSportsInMainMenu(int numberOfExpandedSportsInMainMenu) {
        NumberOfExpandedSportsInMainMenu = numberOfExpandedSportsInMainMenu;
    }

    public int getNumberOfSportsInSpecialSportMenu() {
        return NumberOfSportsInSpecialSportMenu;
    }

    public void setNumberOfSportsInSpecialSportMenu(int numberOfSportsInSpecialSportMenu) {
        NumberOfSportsInSpecialSportMenu = numberOfSportsInSpecialSportMenu;
    }

    public int getNumberOfLiveMatchesOnMainScreen() {
        return NumberOfLiveMatchesOnMainScreen;
    }

    public void setNumberOfLiveMatchesOnMainScreen(int numberOfLiveMatchesOnMainScreen) {
        NumberOfLiveMatchesOnMainScreen = numberOfLiveMatchesOnMainScreen;
    }

    public int getSportNumberOfMatchesPerPage() {
        return SportNumberOfMatchesPerPage;
    }

    public void setSportNumberOfMatchesPerPage(int sportNumberOfMatchesPerPage) {
        SportNumberOfMatchesPerPage = sportNumberOfMatchesPerPage;
    }

    public double getSportOfferBonusPercent() {
        return SportOfferBonusPercent;
    }

    public void setSportOfferBonusPercent(double sportOfferBonusPercent) {
        SportOfferBonusPercent = sportOfferBonusPercent;
    }

    public double getLiveOfferBonusPercent() {
        return LiveOfferBonusPercent;
    }

    public void setLiveOfferBonusPercent(double liveOfferBonusPercent) {
        LiveOfferBonusPercent = liveOfferBonusPercent;
    }

    public double getMinOddsForShowing() {
        return MinOddsForShowing;
    }

    public void setMinOddsForShowing(double minOddsForShowing) {
        MinOddsForShowing = minOddsForShowing;
    }

    public boolean isSportOfferAuthorizeEnabled() {
        return SportOfferAuthorizeEnabled;
    }

    public void setSportOfferAuthorizeEnabled(boolean sportOfferAuthorizeEnabled) {
        SportOfferAuthorizeEnabled = sportOfferAuthorizeEnabled;
    }

    public String getSpecialSportBrandName() {
        return SpecialSportBrandName;
    }

    public void setSpecialSportBrandName(String specialSportBrandName) {
        SpecialSportBrandName = specialSportBrandName;
    }

    public String getDateShortPattern() {
        return DateShortPattern;
    }

    public void setDateShortPattern(String dateShortPattern) {
        DateShortPattern = dateShortPattern;
    }

    public String getDateLongPattern() {
        return DateLongPattern;
    }

    public void setDateLongPattern(String dateLongPattern) {
        DateLongPattern = dateLongPattern;
    }

    public String getBetRadarClientId() {
        return BetRadarClientId;
    }

    public void setBetRadarClientId(String betRadarClientId) {
        BetRadarClientId = betRadarClientId;
    }

    public List<String> getOddsFilter() {
        return OddsFilter;
    }

    public void setOddsFilter(List<String> oddsFilter) {
        OddsFilter = oddsFilter;
    }
}

