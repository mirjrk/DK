package com.zesium.android.betting.model.configuration;

/**
 * Created by Ivan Panic_2 on 6/28/2016.
 */
public class UserSecuritySettingsConfiguration {
    private boolean SecurityQuestionSettingsEnabled;
    private boolean PasswordSettingsEnabled;
    private boolean UserActivityHistoryEnabled;

    public boolean isSecurityQuestionSettingsEnabled() {
        return SecurityQuestionSettingsEnabled;
    }

    public void setSecurityQuestionSettingsEnabled(boolean securityQuestionSettingsEnabled) {
        SecurityQuestionSettingsEnabled = securityQuestionSettingsEnabled;
    }

    public boolean isPasswordSettingsEnabled() {
        return PasswordSettingsEnabled;
    }

    public void setPasswordSettingsEnabled(boolean passwordSettingsEnabled) {
        PasswordSettingsEnabled = passwordSettingsEnabled;
    }

    public boolean isUserActivityHistoryEnabled() {
        return UserActivityHistoryEnabled;
    }

    public void setUserActivityHistoryEnabled(boolean userActivityHistoryEnabled) {
        UserActivityHistoryEnabled = userActivityHistoryEnabled;
    }
}
