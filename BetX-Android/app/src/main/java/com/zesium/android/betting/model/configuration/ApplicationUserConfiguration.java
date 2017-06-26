package com.zesium.android.betting.model.configuration;

/**
 * Created by Ivan Panic_2 on 6/28/2016.
 */
public class ApplicationUserConfiguration {
    private boolean TicketHistoryEnabled;
    private boolean OddsRepresentationSettingsEnabled;
    private boolean DepositCreditEnabled;
    private boolean WithdrawCreditEnabled;
    private boolean TransactionHistoryEnabled;
    private boolean ClubEnabled;
    private boolean BonusesEnabled;
    private UserSettingsConfiguration UserSettingsConfiguration;
    private UserSecuritySettingsConfiguration UserSecuritySettingsConfiguration;
    private ShopConfiguration ShopConfiguration;

    public boolean isTicketHistoryEnabled() {
        return TicketHistoryEnabled;
    }

    public void setTicketHistoryEnabled(boolean ticketHistoryEnabled) {
        TicketHistoryEnabled = ticketHistoryEnabled;
    }

    public boolean isOddsRepresentationSettingsEnabled() {
        return OddsRepresentationSettingsEnabled;
    }

    public void setOddsRepresentationSettingsEnabled(boolean oddsRepresentationSettingsEnabled) {
        OddsRepresentationSettingsEnabled = oddsRepresentationSettingsEnabled;
    }

    public boolean isDepositCreditEnabled() {
        return DepositCreditEnabled;
    }

    public void setDepositCreditEnabled(boolean depositCreditEnabled) {
        DepositCreditEnabled = depositCreditEnabled;
    }

    public boolean isWithdrawCreditEnabled() {
        return WithdrawCreditEnabled;
    }

    public void setWithdrawCreditEnabled(boolean withdrawCreditEnabled) {
        WithdrawCreditEnabled = withdrawCreditEnabled;
    }

    public boolean isTransactionHistoryEnabled() {
        return TransactionHistoryEnabled;
    }

    public void setTransactionHistoryEnabled(boolean transactionHistoryEnabled) {
        TransactionHistoryEnabled = transactionHistoryEnabled;
    }

    public boolean isClubEnabled() {
        return ClubEnabled;
    }

    public void setClubEnabled(boolean clubEnabled) {
        ClubEnabled = clubEnabled;
    }

    public boolean isBonusesEnabled() {
        return BonusesEnabled;
    }

    public void setBonusesEnabled(boolean bonusesEnabled) {
        BonusesEnabled = bonusesEnabled;
    }

    public com.zesium.android.betting.model.configuration.UserSettingsConfiguration getUserSettingsConfiguration() {
        return UserSettingsConfiguration;
    }

    public void setUserSettingsConfiguration(com.zesium.android.betting.model.configuration.UserSettingsConfiguration userSettingsConfiguration) {
        UserSettingsConfiguration = userSettingsConfiguration;
    }

    public com.zesium.android.betting.model.configuration.UserSecuritySettingsConfiguration getUserSecuritySettingsConfiguration() {
        return UserSecuritySettingsConfiguration;
    }

    public void setUserSecuritySettingsConfiguration(com.zesium.android.betting.model.configuration.UserSecuritySettingsConfiguration userSecuritySettingsConfiguration) {
        UserSecuritySettingsConfiguration = userSecuritySettingsConfiguration;
    }

    public com.zesium.android.betting.model.configuration.ShopConfiguration getShopConfiguration() {
        return ShopConfiguration;
    }

    public void setShopConfiguration(com.zesium.android.betting.model.configuration.ShopConfiguration shopConfiguration) {
        ShopConfiguration = shopConfiguration;
    }
}
