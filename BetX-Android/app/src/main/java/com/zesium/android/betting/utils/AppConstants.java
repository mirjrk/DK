package com.zesium.android.betting.utils;

/**
 * Created by simikic on 6/23/2017.
 */

public class AppConstants {
    public static final String WITHDRAW_FUNDS = "https://www.betx.sk/";

    public static final String SPORTS_API = "https://www.betx.sk/sport_api/";

    public static final String USER_BALANCE_API = "https://www.betx.sk/notification_api/";

    public static final String USER_AUTHORIZATION_API = "https://authapisk.betx.sk/";

    public static final String TERMINAL_API = "https://terminalapi_sk.betx.sk/";

    public static final String GENERAL_CONFIG_API = "https://www.betx.sk/";

    public static final String BETTING_API = "https://www.betx.sk/betting_api/";

    public static final String USER_API = "https://www.betx.sk/user_api/";

    public static final String WALLET_API = "https://www.betx.sk/wallet_api/";

    public static final String SIGNALR_SPORT_API = "https://www.betx.sk/sport_api/signalr";

    // User authorization constants
    public static final String USER_AUTHORIZATION_GRANT_TYPE_VALUE = "password";

    public static final String USER_REFRESH_TOKEN_GRANT_TYPE_VALUE = "refresh_token";

    public static final String USER_AUTHORIZATION_CLIENT_ID_VALUE = "412d7656a9c74e84a97861c858cab697";

    // Android client id
    public static final String USER_AUTHORIZATION_TERMINAL_TYPE_VALUE = "2";

    public static final String USER_TOKEN_BEARER = "Bearer ";

    // Subscribe to balance notifications constants
    public static final String AUTHORIZATION = "Authorization";

    public static final String USER_ACCOUNT_NOTIFICATION = "accountnotification";

    public static final String USER_ACCOUNT_DATA_UPDATE = "accountDataUpdated";

    // Live betting notification constants
    public static final String NOTIFICATION = "notification";

    public static final String LIVE_UPDATE = "liveUpdated";

    public static final String REGISTER_SPORTS = "RegisterSports";

    public static final String REGISTER_MATCHES = "RegisterMatches";

    public static final String REGISTER_FOR_BET_TYPES = "RegisterForBetTypes";

    public static final String PARAM_SIGNALR_LANGUAGE_QUERY_STRING = "&LanguageId=";

    // Result fragment constants
    public static final String TICKETS_TERMINAL_ID = "2000";

    // Other constants used in project
    public static final String USER_TOKEN = "user_token";

    public static final int LOGIN_SCREEN_DATA = 11;

    public static final int RESULT_OK = -1;

    //Default font
    public static final String DEFAULT_FONT = "fonts/sanfranciscotextregular.TTF";

    // Login fragment constants
    public static final String LOGIN_USER_TOKEN = "login_user_token";

    public static final String LOGIN_USER_NAME = "login_username";

    public static final String LOGIN_PASSWORD = "login_password";

    public static final String LOGIN_REFRESH_TOKEN = "login_refresh_token";

    public static final String LOGIN_TERMINAL_ID = "login_terminal_id";

    public static final String LOGIN_FIRST_NAME = "login_first_name";

    public static final String LOGIN_LAST_NAME = "login_last_name";

    public static final String LOGIN_EMAIL = "login_email";

    public static final String LOGIN_EXPIRES_IN = "expires_in";

    public static final String SHOULD_RETURN_TO_TICKETS = "should_return_to_tickets";

    public static final String IS_TOKEN_EXPIRED = "is_token_expired";

    public static final int TICKETS_FRAGMENT_REQUEST = 5;

    public static final String VIEW_TO_DISPLAY = "view_to_display";

    public static final int DISPLAY_VIEW = 55;

    public static final String LOGIN_FRAGMENT = "login_fragment";

    // PARENTS
    public static final int PARENT_MATCH_DETAILS_DEFAULT = 27;

    public static final String STATISTICS = "Statistics";

    public static final String BANK_LIST = "bank list";

    public static final int LOGIN_WITH_PIN_REQUEST_CODE = 999;

    public static final String IS_FIRST_TIME_LOGIN_WITH_PIN = "is_first_time_login_with_pin";

    public static final String IS_PERSONAL_INFO_SCREEN = "is_personal_info";

    // Detail match offer broadcast
    public static final String BROADCAST_OFFER_ID = "offer_id";

    public static final String BROADCAST_ODD_ID = "odd_id";

    public static final String BROADCAST_TAB_TO_NOTIFY = "who_to_notify";

    public static final String BROADCAST_DETAIL_OFFER = "broadcast_detail_offer";

    public static final String BROADCAST_UPDATE_LIST = "broadcast_update_list";

    public static final String BROADCAST_BANKS_LIST = "broadcast_banks_list";

    public static final String TENNIS = "Tennis";

    public static final String VOLLEYBALL = "Volleyball";

    public static final String ZERO = "0";

    public static final int BACK_FROM_CREATE_TICKET = 586;

    public static final int BACK_FROM_MATCH_DETAILS = 99;

    // Personal info
    public static final String USER_DATA = "user_data";

    public static final String SECURITY_QUESTION_ID = "security_question_id";

    public static final String SECURITY_QUESTION_ANSWER = "security_question_answer";

    public static final int SPLASH_SCREEN_TIME = 2000;

    // Create ticket
    public static final String RETURN_TO_CREATE_TICKET = "return_to_create_ticket";

    public static final String IS_LOGGED_OUT = "is_logged_out";

    // Payment
    public static final String WALLET_CONFIGURATION = "wallet_configuration";

    // Home betting matches limit
    public static final int MATCH_LISTS_SIZE = 10;

    public final static int EIGHT_SECONDS = 8000;

    //total goals
    public static String selected;

    public static final String CODE = "sms_code";

    public static boolean isFromSmsConfirmationScreen;

    public static boolean isFromForgottenPassword; // if true floating view will not be shown
}
