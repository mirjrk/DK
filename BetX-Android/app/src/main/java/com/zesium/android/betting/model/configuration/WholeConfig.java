package com.zesium.android.betting.model.configuration;

import com.google.gson.JsonArray;

/**
 * Created by Ivan Panic_2 on 1/31/2017.
 */

public class WholeConfig {
    public final GeneralConfiguration mGeneralConfiguration;
    public final ApplicationUserConfiguration mApplicationUserConfiguration;
    public final BettingConfiguration mBettingConfiguration;
    public final JsonArray mTranslations;

    public WholeConfig(GeneralConfiguration generalConfiguration, ApplicationUserConfiguration applicationUserConfiguration, BettingConfiguration bettingConfiguration, JsonArray translations) {
        mGeneralConfiguration = generalConfiguration;
        mApplicationUserConfiguration = applicationUserConfiguration;
        mBettingConfiguration = bettingConfiguration;
        mTranslations = translations;
    }
}
