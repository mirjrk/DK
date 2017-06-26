package com.zesium.android.betting.model.configuration;

/**
 * Created by Ivan Panic_2 on 6/28/2016.
 */
public class UserSettingsConfiguration {
    private boolean PersonalInformationSettingsEnabled;
    private boolean BankAccountSettingsEnabled;
    private boolean UserPreferencesSettingsEnabled;
    private boolean DocumentVerificationSettingsEnabled;

    public boolean isPersonalInformationSettingsEnabled() {
        return PersonalInformationSettingsEnabled;
    }

    public void setPersonalInformationSettingsEnabled(boolean personalInformationSettingsEnabled) {
        PersonalInformationSettingsEnabled = personalInformationSettingsEnabled;
    }

    public boolean isBankAccountSettingsEnabled() {
        return BankAccountSettingsEnabled;
    }

    public void setBankAccountSettingsEnabled(boolean bankAccountSettingsEnabled) {
        BankAccountSettingsEnabled = bankAccountSettingsEnabled;
    }

    public boolean isUserPreferencesSettingsEnabled() {
        return UserPreferencesSettingsEnabled;
    }

    public void setUserPreferencesSettingsEnabled(boolean userPreferencesSettingsEnabled) {
        UserPreferencesSettingsEnabled = userPreferencesSettingsEnabled;
    }

    public boolean isDocumentVerificationSettingsEnabled() {
        return DocumentVerificationSettingsEnabled;
    }

    public void setDocumentVerificationSettingsEnabled(boolean documentVerificationSettingsEnabled) {
        DocumentVerificationSettingsEnabled = documentVerificationSettingsEnabled;
    }
}
