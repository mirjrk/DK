package com.zesium.android.betting.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Preferences class store all necessarily data for user, and future use of application.
 * Created by Ivan Panic on 12/17/2015.
 */
public class Preferences {

    private static final String VIEW_PAGER_PAGE = "page";
    private static final String USER_TOKEN = "token";
    private static final String REFRESH_TOKEN = "refresh_token";
    private static final String USER_NAME = "user_name";
    private static final String USER_LOGGED = "user_logged";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String EMAIL = "email";
    private static final String USER_PASSWORD = "user_password";
    private static final String LANGUAGE = "language";
    private static final String LANGUAGE_LONG = "language_long";
    private static final String SELECTED_THEME = "application_theme";
    private static final String TERMINAL_ID = "terminal_id";
    private static final String LAST_CLICKED_NAVIGATION_DRAWER_ITEM = "last_navigation_drawer_item";
    private static final String IS_REGISTRATION_FINISHED = "is_registration_finished";
    // private static final String FILTER_SELECTED = "filter";
    private static final String DARK_THEME = "dark_theme";
    private static final String PIN_NUMBER = "pin_number";
    private static final String IS_LOGGED_WITH_PIN = "is_logged_with_pin";
    private static final String BETTING_MIN_PAY_IN = "min_pay_in";
    private static final String BETTING_SYSTEM_MIN_PAY_IN = "system_min_pay_in";
    private static final String CURRENCY_SIGN = "currency_sign";
    private static final String CURRENCY_ID = "currency_id";
    private static final String IS_BACK_FROM_TICKETS_DETAILS = "is_back_from_ticket_details";
    private static final String PREF_LAST_PAY_IN_VALUE = "last_pay_in_value";

    private static final String PREF_ACCEPT_ANY_ODD_CHANGE = "accept_any_odd_change";
    private static final String PREFF_ACCEPT_ONLY_HIGHER_ODDS = "accept_higher_odds";
    private static final String PREF_TOKEN_EXPIRES_IN = "token_expires_in";

    private static final String ACC_NAME = "account_name";
    private static final String BANK_NAME = "bank_name";
    private static final String BANK_NUMBER = "bank_number";
    private static final String COUNTRY = "country";

    private static final String PREF_VERSION = "shared_pref_version";

    private static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx.getApplicationContext());
    }

    public static void setViewPagerPage(Context ctx, int page) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(VIEW_PAGER_PAGE, page);
        editor.apply();
    }

    public static int getViewPagerPage(Context ctx) {
        return getSharedPreferences(ctx).getInt(VIEW_PAGER_PAGE, 0);
    }

    public static void setSharedPrefVersion(Context ctx, int version) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(PREF_VERSION, version);
        editor.apply();
    }

    public static int getSharedPrefVersion(Context ctx) {
        return getSharedPreferences(ctx).getInt(PREF_VERSION, 0);
    }

    public static void setUserToken(Context ctx, String token) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(USER_TOKEN, token);
        editor.apply();
    }

    public static String getUserToken(Context ctx) {
        return getSharedPreferences(ctx).getString(USER_TOKEN, "");
    }

    public static void setUserName(Context ctx, String userName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(USER_NAME, userName);
        editor.apply();
    }

    public static String getUserName(Context ctx) {
        return getSharedPreferences(ctx).getString(USER_NAME, "");
    }

    public static void setPassword(Context ctx, String password) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(USER_PASSWORD, password);
        editor.apply();
    }

    public static String getPassword(Context ctx) {
        return getSharedPreferences(ctx).getString(USER_PASSWORD, "");
    }

    public static void setApplicationThemeColor(Context ctx, int theme) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(SELECTED_THEME, theme);
        editor.apply();
    }

    public static int getApplicationThemeColor(Context ctx) {
        return getSharedPreferences(ctx).getInt(SELECTED_THEME, 0);
    }

    public static void setLastNavDrawerItem(Context ctx, int navDrawerItem) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putInt(LAST_CLICKED_NAVIGATION_DRAWER_ITEM, navDrawerItem);
        editor.apply();
    }

    public static int getLastNavDrawerItem(Context ctx) {
        return getSharedPreferences(ctx).getInt(LAST_CLICKED_NAVIGATION_DRAWER_ITEM, 0);
    }

    public static void setRefeshToken(Context ctx, String token) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(REFRESH_TOKEN, token);
        editor.apply();
    }

    public static String getRefreshToken(Context ctx) {
        return getSharedPreferences(ctx.getApplicationContext()).getString(REFRESH_TOKEN, "");
    }

    public static void setLanguage(Context ctx, String language) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(LANGUAGE, language);
        editor.apply();
    }

    public static String getLanguage(Context ctx) {
        return getSharedPreferences(ctx).getString(LANGUAGE, "");
    }

    public static void setTerminalId(Context ctx, String terminalId) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(TERMINAL_ID, terminalId);
        editor.apply();
    }

    public static String getTerminalId(Context ctx) {
        return getSharedPreferences(ctx).getString(TERMINAL_ID, "");
    }

    public static void setIsRegistrationFinished(Context ctx, boolean isRegistrationFinished) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(IS_REGISTRATION_FINISHED, isRegistrationFinished);
        editor.apply();
    }

    public static boolean getIsRegistrationFinished(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(IS_REGISTRATION_FINISHED, false);
    }

    public static void setIsUserLogged(Context ctx, boolean isUserLogged) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(USER_LOGGED, isUserLogged);
        editor.apply();
    }

    public static boolean getIsUserLogged(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(USER_LOGGED, false);
    }

    public static void setFirstName(Context ctx, String firstName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(FIRST_NAME, firstName);
        editor.apply();
    }

    public static String getFirstName(Context ctx) {
        return getSharedPreferences(ctx).getString(FIRST_NAME, "");
    }

    public static void setLastName(Context ctx, String lastName) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(LAST_NAME, lastName);
        editor.apply();
    }

    public static String getLastName(Context ctx) {
        return getSharedPreferences(ctx).getString(LAST_NAME, "");
    }

    public static void setEmail(Context ctx, String email) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(EMAIL, email);
        editor.apply();
    }

    public static String getEmail(Context ctx) {
        return getSharedPreferences(ctx).getString(EMAIL, "");
    }

    public static void setTheme(Context ctx, boolean isDarkTheme) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(DARK_THEME, isDarkTheme);
        editor.apply();
    }

    public static boolean getTheme(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(DARK_THEME, false);
    }

    public static void setPinNumber(Context ctx, String pin) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PIN_NUMBER, pin);
        editor.apply();
    }

    public static String getPinNumber(Context ctx) {
        return getSharedPreferences(ctx).getString(PIN_NUMBER, "");
    }

    public static void setIsUserLoggedWithPin(Context ctx, boolean isLoggedWithPin) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(IS_LOGGED_WITH_PIN, isLoggedWithPin);
        editor.apply();
    }

    public static boolean isUserLoggedWithPin(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(IS_LOGGED_WITH_PIN, false);
    }

    public static void setLanguageLong(Context ctx, String language) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(LANGUAGE_LONG, language);
        editor.apply();
    }

    public static String getLanguageLong(Context ctx) {
        return getSharedPreferences(ctx).getString(LANGUAGE_LONG, "");
    }

    public static void setBettingMinPayIn(Context ctx, double minPayIn) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putLong(BETTING_MIN_PAY_IN, Double.doubleToLongBits(minPayIn));
        editor.apply();
    }

    public static double getBettingMinPayIn(Context ctx) {
        return Double.longBitsToDouble(getSharedPreferences(ctx).getLong(BETTING_MIN_PAY_IN, 0));
    }

    public static void setBettingSystemMinPayIn(Context ctx, double minPayIn) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putLong(BETTING_SYSTEM_MIN_PAY_IN, Double.doubleToLongBits(minPayIn));
        editor.apply();
    }

    public static double getBettingSystemMinPayIn(Context ctx) {
        return Double.longBitsToDouble(getSharedPreferences(ctx).getLong(BETTING_SYSTEM_MIN_PAY_IN, 0));
    }


    public static void setCurrencySign(Context ctx, String currencySign) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(CURRENCY_SIGN, currencySign);
        editor.apply();
    }

    public static String getCurrencySign(Context ctx) {
        return getSharedPreferences(ctx).getString(CURRENCY_SIGN, "");
    }

    public static void setCurrencyId(Context ctx, String currencyId) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(CURRENCY_ID, currencyId);
        editor.apply();
    }

    public static String getCurrencyId(Context ctx) {
        return getSharedPreferences(ctx).getString(CURRENCY_ID, "");
    }

    public static double getLastPayInValue(Context ctx) {
        return Double.longBitsToDouble(getSharedPreferences(ctx).getLong(PREF_LAST_PAY_IN_VALUE, 0));
    }

    public static void setLastPayInValue(Context ctx, double lastPayIn) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putLong(PREF_LAST_PAY_IN_VALUE, Double.doubleToLongBits(lastPayIn));
        editor.apply();
    }

    public static void setPrefAcceptAnyOddChange(Context ctx, boolean accept) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREF_ACCEPT_ANY_ODD_CHANGE, accept);
        editor.apply();
    }

    public static boolean getPrefAcceptAnyOddChange(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(PREF_ACCEPT_ANY_ODD_CHANGE, false);
    }

    public static void setPreffAcceptOnlyHigherOdds(Context ctx, boolean accept) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putBoolean(PREFF_ACCEPT_ONLY_HIGHER_ODDS, accept);
        editor.apply();
    }

    public static boolean getPreffAcceptOnlyHigherOdds(Context ctx) {
        return getSharedPreferences(ctx).getBoolean(PREFF_ACCEPT_ONLY_HIGHER_ODDS, false);
    }

    public static long getTokenExpiresIn(Context ctx) {
        return getSharedPreferences(ctx).getLong(PREF_TOKEN_EXPIRES_IN, 0L);
    }

    public static void setTokenExpiresIn(Context ctx, long tokenExpiresIn) {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putLong(PREF_TOKEN_EXPIRES_IN, tokenExpiresIn);
        editor.apply();
    }

    public static void clearAllPrefs(Context ctx) {
        getSharedPreferences(ctx).edit().clear().apply();
    }
}
