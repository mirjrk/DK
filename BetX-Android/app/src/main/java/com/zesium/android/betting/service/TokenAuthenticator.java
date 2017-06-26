package com.zesium.android.betting.service;

import com.zesium.android.betting.model.user.User;
import com.zesium.android.betting.BetXApplication;
import com.zesium.android.betting.utils.Preferences;
import com.zesium.android.betting.utils.AppConstants;

import java.io.IOException;


import okhttp3.Authenticator;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import retrofit2.Call;

import static com.zesium.android.betting.utils.AppConstants.AUTHORIZATION;

/**
 * Class that automatically retry unauthenticated requests. If return value is null "Token Expired"
 * dialog will be shown to a user and after that he will be redirected to LoginFragment.
 */
class TokenAuthenticator implements Authenticator {


    @Override
    public Request authenticate(Route route, Response response) throws IOException {
        if (responseCount(response) >= 3) {
            return null; // If we've failed 3 times, give up.
        }
        // Refresh your access_token using a synchronous api request
        String refreshToken = Preferences.getRefreshToken(BetXApplication.getApp());

        Call<User> userRequest = NetworkHelper.getBetXService(AppConstants.USER_AUTHORIZATION_API).refreshToken(AppConstants.USER_REFRESH_TOKEN_GRANT_TYPE_VALUE,
                refreshToken, AppConstants.USER_AUTHORIZATION_CLIENT_ID_VALUE,
                AppConstants.USER_AUTHORIZATION_TERMINAL_TYPE_VALUE);
        User user = userRequest.execute().body();
        String userToken = "";

        if (user != null) {
            userToken = AppConstants.USER_TOKEN_BEARER + user.getAccess_token();
            refreshToken = user.getRefresh_token();
            Preferences.setUserToken(BetXApplication.getApp(), userToken);
            Preferences.setRefeshToken(BetXApplication.getApp(), refreshToken);
            Preferences.setTerminalId(BetXApplication.getApp(), user.getTerminal_id());
        }

        // Add new header to rejected request and retry it
        return response.request().newBuilder()
                .header(AUTHORIZATION, userToken)
                .build();
    }

    private int responseCount(Response response) {
        int result = 1;
        while ((response = response.priorResponse()) != null) {
            result++;
        }
        return result;
    }

}