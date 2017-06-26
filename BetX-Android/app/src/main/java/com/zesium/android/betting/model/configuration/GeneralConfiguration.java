package com.zesium.android.betting.model.configuration;


/**
 * Created by Ivan Panic_2 on 1/30/2017.
 */

public class GeneralConfiguration {
    /*private String ClientId;
    private String AnonymousId;
    private String IntegrationId;
    private Object MediaServer;
    private Object PrintUrl;
    private Object KenoApiUrl;
    private String GoldenRaceIntegrationApiUrl;
    private String GoldenRaceInternalIntegrationApiUrl;
    private String LiveTrackerUrl;
    private String MainSliderInterval;
    private String StatisticsClientId;
    private Integer LastMinuteMatchesLimit;
    private Integer MainLastMinuteMatchesLimit;
    private String ErrorTicketRepeatInterval;
    private Integer CompleteSportOfferLimit;
    private Integer SystemCombinationType;
    private Integer TerminalId;
    private Integer DefaultTerminalId;
    private Integer TerminalType;
    private Integer SpecialOrderingLimit;
    private Integer NumberOfSportsInSportMenu;
    private Integer NumberOfSportsInSpecialSportMenu;
    private Integer NumberOfLiveMatches;
    private Integer NumberOfMatchesPerPage;
    private String DateShortPattern;
    private String DateLongPattern;
    private Integer TicketCancelDuration;
    private Integer NumberOfTicketsPerPage;*/
    private String CurrencySign;
    private String CurrencyId;
    /*private Integer CurrencyExchangeRate;
    private String CultureIeftLanguageTag;
    private Boolean IsRepeatTicketEnabled;
    private Boolean IsAccountingEnabled;
    private String OddsRepresentation;
    private Integer OfferBonusPercent;
    private Integer OfferLiveBonusPercent;
    private Integer MinShowingOdds;
    private Integer CurrencyDecimalPlaces;
    private Boolean FractionSbvRepresentation;
    private Boolean LoginDialogEnabled;
    private Integer UploadingDocumentSize;
    private String TerminalDefaultLanguageId;
    private Boolean SportAuthorization;
    private String CompanyName;
    private Boolean WebClubEnabled;
    private List<Integer> WebClubBonusesType = null;
    private List<OddsSegment> OddsSegments = null;
    private Boolean SearchControlSignEnabled;
    private Boolean ThemeChangingEnabled;
    private Boolean GoogleAnalyticsEnabled;
    private String GoogleAnalyticsClient;
    private String BannerImagesPath;
    private Boolean TicketBasicSystemEnabled;
    private Boolean ShowStaticTicketPin;
    private String IntegrationType;*/

    public String getCurrencySign() {
        return CurrencySign;
    }

    public void setCurrencySign(String currencySign) {
        CurrencySign = currencySign;
    }

    public String getCurrencyId() {
        return CurrencyId;
    }

    public void setCurrencyId(String currencyId) {
        CurrencyId = currencyId;
    }
}
