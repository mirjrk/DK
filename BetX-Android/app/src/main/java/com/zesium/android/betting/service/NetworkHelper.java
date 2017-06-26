package com.zesium.android.betting.service;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.zesium.android.betting.BetXApplication;
import com.zesium.android.betting.R;
import com.zesium.android.betting.utils.AppConstants;
import com.zesium.android.betting.utils.AppLogger;
import com.zesium.android.betting.utils.Preferences;
import com.zesium.android.betting.model.util.TranslationConstants;
import com.zesium.android.betting.utils.AppUtils;
import com.zesium.android.betting.ui.widgets.SFFontTextView;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by Ivan Panic_2 on 10/18/2016.
 */
public class NetworkHelper {
    private static final int READ_TIMEOUT = 10;
    private static final int CONNECTION_TIMEOUT = 10;

    private static final int CREATE_TICKET_READ_TIMEOUT = 100;
    private static final int CREATE_TICKET_CONNECTION_TIMEOUT = 100;

    private static final HashMap<String, BetXService> sBetXServices = new HashMap<>();
    private static final String TAG = NetworkHelper.class.getSimpleName();
    private static BetXService sBettingBigTimeout;

    public static boolean isNetworkAvailable(Activity activity) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    public static void showNoConnectionDialog(final Context ctx1) {
        /*final Context ctx = ctx1;
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setCancelable(true);
        builder.setMessage(R.string.no_internet_connection_message);
        builder.setTitle(R.string.no_internet_connection_tittle);
        builder.setPositiveButton(R.string.no_internet_connection_settings, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (ctx instanceof Activity) {
                    ((Activity) ctx).startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 1);
                } else {
                    ctx.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            }
        });

        builder.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
            }
        });
        builder.show();*/

        LayoutInflater inflater = (LayoutInflater) ctx1.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_logout, null);


        android.support.v7.app.AlertDialog.Builder builder;
        if (Preferences.getTheme(ctx1)) {
            builder = new android.support.v7.app.AlertDialog.Builder(ctx1, R.style.DarkBackgroundFilterDialog).setView(layout);
        } else {
            builder = new android.support.v7.app.AlertDialog.Builder(ctx1, R.style.WhiteBackgroundFilterDialog).setView(layout);
        }


        String logOut;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.SETTINGS)) {
            logOut = BetXApplication.translationMap.get(TranslationConstants.SETTINGS);
        } else {
            logOut = ctx1.getResources().getString(R.string.action_settings);
        }

        String cancel;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.CANCEL)) {
            cancel = BetXApplication.translationMap.get(TranslationConstants.CANCEL);
        } else {
            cancel = ctx1.getResources().getString(R.string.dialog_cancel);
        }

        String noConnectionBody;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.PLEASE_CHECK_YOUR_INTERNET_CONNECTION)) {
            noConnectionBody = BetXApplication.translationMap.get(TranslationConstants.PLEASE_CHECK_YOUR_INTERNET_CONNECTION);
        } else {
            noConnectionBody = ctx1.getResources().getString(R.string.no_internet_connection_message);
        }

        SFFontTextView tvBodyMessage = (SFFontTextView) layout.findViewById(R.id.tv_dialog_message);
        tvBodyMessage.setText(noConnectionBody);


        String noConnectionTitle = ctx1.getResources().getString(R.string.no_internet_connection_tittle);
        /*if (BetXApplication.translationMap.containsKey(TranslationConstants.PLEASE_CHECK_YOUR_INTERNET_CONNECTION)) {
            noConnectionTitle = BetXApplication.translationMap.get(TranslationConstants.PLEASE_CHECK_YOUR_INTERNET_CONNECTION);
        } else {
            noConnectionTitle = ctx1.getResources().getString(R.string.no_internet_connection_tittle);
        }*/
        SFFontTextView tvTitle = (SFFontTextView) layout.findViewById(R.id.logout_title_textview);
        tvTitle.setText(noConnectionTitle);


        final android.support.v7.app.AlertDialog alertDialog = builder.setPositiveButton(logOut,
                (dialog, which) -> {
                    if (ctx1 instanceof Activity) {
                        ((Activity) ctx1).startActivityForResult(new Intent(Settings.ACTION_WIFI_SETTINGS), 1);
                    } else {
                        ctx1.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                }).setNegativeButton(cancel, (dialog, which) -> {
                }).create();
        alertDialog.show();
        alertDialog.setCancelable(true);
        //setting width and height
        alertDialog.getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(
                () -> AppUtils.adjustDialog(ctx1.getApplicationContext(), alertDialog));
    }

    /**
     * Initialize service for making request to server.
     *
     * @param apiUrl url address of api
     * @return service object which provides requests.
     */
    private static BetXService initializeBetXService(String apiUrl) {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .authenticator(new TokenAuthenticator())
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(apiUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(new NullOnEmptyConverterFactory())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build();

            return retrofit.create(BetXService.class);
        } catch (Exception e) {
            AppLogger.error(TAG, "Exception occured with message: ", e);
        }
        return null;
    }

    /**
     * Initialize service for create ticket request to server. This request timeout is set to 100
     * seconds because it can go through manual validation.
     *
     * @param apiUrl url address of api
     * @return service object which provides requests.
     */
    private static BetXService initializeServiceForCreatingTicket(String apiUrl) {
        try {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(CREATE_TICKET_CONNECTION_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(CREATE_TICKET_READ_TIMEOUT, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .authenticator(new TokenAuthenticator())
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(apiUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(client)
                    .build();

            return retrofit.create(BetXService.class);
        } catch (Exception e) {
            AppLogger.error(TAG, "Exception occured with message: ", e);
        }
        return null;
    }

    public static void checkResponseForSocketTimeOutException(Activity activity, Throwable t) {
        try {
            SocketException exception = (SocketException) t;
            Log.d("NetworkHelper", "checkResponseForSocketTimeOutException: " + exception);
            String checkInternet;
            if (BetXApplication.translationMap.containsKey(TranslationConstants.PLEASE_CHECK_YOUR_INTERNET_CONNECTION)) {
                checkInternet = BetXApplication.translationMap.get(TranslationConstants.PLEASE_CHECK_YOUR_INTERNET_CONNECTION);
            } else {
                checkInternet = activity.getString(R.string.check_internet);
            }
            Toast.makeText(activity, checkInternet, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            AppLogger.error(TAG, "Exception occured with message: ", e);
        }
    }

    public static void showUnauthorizedDialog(final Context ctx) {
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_logout, null);

        android.support.v7.app.AlertDialog.Builder builder;
        if (Preferences.getTheme(ctx)) {
            builder = new android.support.v7.app.AlertDialog.Builder(ctx, R.style.DarkBackgroundFilterDialog).setView(layout);
        } else {
            builder = new android.support.v7.app.AlertDialog.Builder(ctx, R.style.WhiteBackgroundFilterDialog).setView(layout);
        }

        String cancel;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.CANCEL)) {
            cancel = BetXApplication.translationMap.get(TranslationConstants.CANCEL);
        } else {
            cancel = ctx.getResources().getString(R.string.dialog_cancel);
        }

        String title;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.UNAUTHORIZED)) {
            title = BetXApplication.translationMap.get(TranslationConstants.UNAUTHORIZED);
        } else {
            title = ctx.getResources().getString(R.string.unauthorized);
        }

        SFFontTextView tvTitle = (SFFontTextView) layout.findViewById(R.id.logout_title_textview);
        tvTitle.setText(title);

        String tokenHasExpiredTxt;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.TOKEN_HAS_EXPIRED)) {
            tokenHasExpiredTxt = BetXApplication.translationMap.get(TranslationConstants.TOKEN_HAS_EXPIRED);
        } else {
            tokenHasExpiredTxt = ctx.getResources().getString(R.string.token_expired);
        }

        SFFontTextView tvMessage = (SFFontTextView) layout.findViewById(R.id.tv_dialog_message);
        tvMessage.setText(tokenHasExpiredTxt);

        final android.support.v7.app.AlertDialog alertDialog = builder.setNegativeButton(cancel, (dialog, which) -> {
            if (ctx instanceof Activity) {
                ((Activity) ctx).finish();
            }
        }).setOnCancelListener(dialogInterface -> {
            if (ctx instanceof Activity) {
                ((Activity) ctx).finish();
            }
        }).create();
        alertDialog.show();
        alertDialog.setCancelable(true);
        //setting width and height
        alertDialog.getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(
                () -> AppUtils.adjustDialog(ctx.getApplicationContext(), alertDialog));
    }

    public static BetXService getBetXService(String url) {
        BetXService ret;
        if (sBetXServices.containsKey(url)) {
            ret = sBetXServices.get(url);
        } else {
            ret = initializeBetXService(url);
            sBetXServices.put(url, ret);
        }
        return ret;
    }

    public static BetXService getBetXServiceWithBigTimeout(String url) {
        BetXService ret = null;
        switch (url) {
            case AppConstants.BETTING_API: // value same as AppUtils.CALCULATION_API:
                if (sBettingBigTimeout == null) {
                    sBettingBigTimeout = initializeServiceForCreatingTicket(url);
                }
                ret = sBettingBigTimeout;
                break;
        }
        return ret;
    }

    private static class NullOnEmptyConverterFactory extends Converter.Factory {

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            final Converter<ResponseBody, ?> delegate = retrofit.nextResponseBodyConverter(this, type, annotations);
            return (Converter<ResponseBody, Object>) body -> {
                if (body.contentLength() == 0) return null;
                return delegate.convert(body);
            };
        }
    }

    public static int generateErrorMessage(Throwable t) {
        int error;
        if (t instanceof SocketTimeoutException) {
            error = R.string.request_time_out;
        } else if (t instanceof IOException) {
            error = R.string.no_internet_connection_message;
        } else {
            error = R.string.login_server_error;
        }
        return error;
    }

}
