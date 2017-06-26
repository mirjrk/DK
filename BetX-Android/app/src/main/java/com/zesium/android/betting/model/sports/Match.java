package com.zesium.android.betting.model.sports;

import java.io.Serializable;
import java.util.List;

/**
 * Match class that has fields defined by response from server for one match.
 * Created by Ivan Panic on 12/18/2015.
 */
public class Match implements Serializable {
    private int Id;
    private int CommonLeagueId;
    private int LeagueId;
    private String Description;
    private Offer BasicOffer;
    private String MatchStartTime;
    private String AdditionalMatchData;
    private String TeamHome;
    private String TeamAway;
    private String OffersCount;
    private String OriginSportName;
    private List<Offer> Offers;
    private String LiveMatchTime;
    private String LiveMatchTimeState;
    private String LiveMatchScore;
    private int LiveMatchState;
    private int LiveHomeYellowCards;
    private int LiveAwayYellowCards;
    private int LiveHomeRedCards;
    private int LiveAwayRedCards;
    private int LiveHomeCorners;
    private int LiveAwayCorners;
    private String LiveServer;
    private boolean IsLive;
    private boolean IsLiveMatchAvailable;
    private boolean isBlocked;
    private String LiveSetScore;
    private String LiveGameScore;
    private int LeagueOrd;
    private String LeagueName;
    private int CategoryOrd;
    private String CategoryName;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Offer getOffer() {
        return BasicOffer;
    }

    public void setOffer(Offer offer) {
        BasicOffer = offer;
    }

    public String getMatchStartTime() {
        return MatchStartTime;
    }

    public void setMatchStartTime(String matchStartTime) {
        MatchStartTime = matchStartTime;
    }

    public String getTeamHome() {
        return TeamHome;
    }

    public void setTeamHome(String teamHome) {
        TeamHome = teamHome;
    }

    public String getTeamAway() {
        return TeamAway;
    }

    public void setTeamAway(String teamAway) {
        TeamAway = teamAway;
    }

    public List<com.zesium.android.betting.model.sports.Offer> getOffers() {
        return Offers;
    }

    public void setOffers(List<com.zesium.android.betting.model.sports.Offer> offers) {
        Offers = offers;
    }

    public String getOffersCount() {
        return OffersCount;
    }

    public void setOffersCount(String offersCount) {
        OffersCount = offersCount;
    }

    public Offer getBasicOffer() {
        return BasicOffer;
    }

    public void setBasicOffer(Offer basicOffer) {
        BasicOffer = basicOffer;
    }

    public String getLiveMatchTime() {
        return LiveMatchTime;
    }

    public void setLiveMatchTime(String liveMatchTime) {
        LiveMatchTime = liveMatchTime;
    }

    public String getLiveMatchTimeState() {
        return LiveMatchTimeState;
    }

    public void setLiveMatchTimeState(String liveMatchTimeState) {
        LiveMatchTimeState = liveMatchTimeState;
    }

    public String getLiveMatchScore() {
        return LiveMatchScore;
    }

    public void setLiveMatchScore(String liveMatchScore) {
        LiveMatchScore = liveMatchScore;
    }

    public int getLiveHomeYellowCards() {
        return LiveHomeYellowCards;
    }

    public void setLiveHomeYellowCards(int liveHomeYellowCards) {
        LiveHomeYellowCards = liveHomeYellowCards;
    }

    public int getLiveAwayYellowCards() {
        return LiveAwayYellowCards;
    }

    public void setLiveAwayYellowCards(int liveAwayYellowCards) {
        LiveAwayYellowCards = liveAwayYellowCards;
    }

    public int getLiveHomeRedCards() {
        return LiveHomeRedCards;
    }

    public void setLiveHomeRedCards(int liveHomeRedCards) {
        LiveHomeRedCards = liveHomeRedCards;
    }

    public int getLiveAwayRedCards() {
        return LiveAwayRedCards;
    }

    public void setLiveAwayRedCards(int liveAwayRedCards) {
        LiveAwayRedCards = liveAwayRedCards;
    }

    public boolean isLive() {
        return IsLive;
    }

    public void setLive(boolean live) {
        IsLive = live;
    }

    public boolean isLiveMatchAvailable() {
        return IsLiveMatchAvailable;
    }

    public void setLiveMatchAvailable(boolean liveMatchAvailable) {
        IsLiveMatchAvailable = liveMatchAvailable;
    }

    public String getLiveServer() {
        return LiveServer;
    }

    public void setLiveServer(String liveServer) {
        LiveServer = liveServer;
    }

    public int getLiveMatchState() {
        return LiveMatchState;
    }

    public void setLiveMatchState(int liveMatchState) {
        LiveMatchState = liveMatchState;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public String getLiveSetScore() {
        return LiveSetScore;
    }

    public void setLiveSetScore(String liveSetScore) {
        LiveSetScore = liveSetScore;
    }

    public String getLiveGameScore() {
        return LiveGameScore;
    }

    public void setLiveGameScore(String liveGameScore) {
        LiveGameScore = liveGameScore;
    }

    public int getLiveHomeCorners() {
        return LiveHomeCorners;
    }

    public void setLiveHomeCorners(int liveHomeCorners) {
        LiveHomeCorners = liveHomeCorners;
    }

    public int getLiveAwayCorners() {
        return LiveAwayCorners;
    }

    public void setLiveAwayCorners(int liveAwayCorners) {
        LiveAwayCorners = liveAwayCorners;
    }

    public String getAdditionalMatchData() {
        return AdditionalMatchData;
    }

    public void setAdditionalMatchData(String additionalMatchData) {
        AdditionalMatchData = additionalMatchData;
    }

    public String getOriginSportName() {
        return OriginSportName;
    }

    public void setOriginSportName(String originSportName) {
        OriginSportName = originSportName;
    }

    public int getLeagueOrd() {
        return LeagueOrd;
    }

    public void setLeagueOrd(int leagueOrd) {
        LeagueOrd = leagueOrd;
    }

    public String getLeagueName() {
        return LeagueName;
    }

    public void setLeagueName(String leagueName) {
        LeagueName = leagueName;
    }

    public int getCategoryOrd() {
        return CategoryOrd;
    }

    public void setCategoryOrd(int categoryOrd) {
        CategoryOrd = categoryOrd;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public int getCommonLeagueId() {
        return CommonLeagueId;
    }

    public void setCommonLeagueId(int commonLeagueId) {
        CommonLeagueId = commonLeagueId;
    }

    public int getLeagueId() {
        return LeagueId;
    }

    public void setLeagueId(int leagueId) {
        LeagueId = leagueId;
    }
}
