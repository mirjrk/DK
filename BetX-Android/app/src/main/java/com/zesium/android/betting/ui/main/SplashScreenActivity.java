package com.zesium.android.betting.ui.main;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.zesium.android.betting.BetXApplication;
import com.zesium.android.betting.R;
import com.zesium.android.betting.model.configuration.ApplicationUserConfiguration;
import com.zesium.android.betting.model.configuration.BettingConfiguration;
import com.zesium.android.betting.model.configuration.GeneralConfiguration;
import com.zesium.android.betting.model.configuration.WholeConfig;
import com.zesium.android.betting.model.translation.TranslationResult;
import com.zesium.android.betting.service.NetworkHelper;
import com.zesium.android.betting.utils.NetworkStateReceiver;
import com.zesium.android.betting.utils.AppConstants;
import com.zesium.android.betting.utils.AppLogger;
import com.zesium.android.betting.utils.Preferences;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * SplashScreenActivity class that represents splash screen of BetX application.
 */
public class SplashScreenActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {
    private static final String TAG = SplashScreenActivity.class.getSimpleName();
    private NetworkStateReceiver networkStateReceiver;
    private Timer mTimer;
    private boolean isPreviousDataRetrieved = false;
    private Date startTime;
    private Observable<WholeConfig> combinedObservableRequest;
    private Subscription mSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Setting status bar color, can't change the Status Bar Color on pre-Lollipop devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.primary_dark_red));
        }

        setContentView(R.layout.activity_splash_screen);
        RelativeLayout mSplashScreenRL = (RelativeLayout) findViewById(R.id.rl_splash);
        boolean darkTheme = Preferences.getTheme(this);
        if (darkTheme) {
            mSplashScreenRL.setBackgroundResource(R.drawable.background_logo_dark);
            setTheme(R.style.SplashThemeDark);
        } else {
            mSplashScreenRL.setBackgroundResource(R.drawable.background_logo_light);
            setTheme(R.style.SplashThemeLight);
        }

        ProgressBar progBar = (ProgressBar) findViewById(R.id.progress);
        if (android.os.Build.VERSION.SDK_INT <= android.os.Build.VERSION_CODES.LOLLIPOP) {
            progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.primary_red),
                    android.graphics.PorterDuff.Mode.SRC_IN);
        }

        String translationLanguage = "en";
        if (Preferences.getLanguage(SplashScreenActivity.this).equals("")){
            Preferences.setLanguage(SplashScreenActivity.this, translationLanguage);
            Preferences.setLanguageLong(SplashScreenActivity.this, "English");
        }

        if (Preferences.getTerminalId(SplashScreenActivity.this).isEmpty()) {
            Preferences.setTerminalId(SplashScreenActivity.this, AppConstants.TICKETS_TERMINAL_ID);
        }

        startTime = new Date();
        getConfiguration();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mTimer = new Timer();
        mTimer.schedule(new NoInternetTimerTask(), 8000);
    }

    private void getConfiguration() {
        if (NetworkHelper.isNetworkAvailable(SplashScreenActivity.this)) {
            Observable<GeneralConfiguration> generalConfigRequest = NetworkHelper.getBetXService(AppConstants.GENERAL_CONFIG_API).getGeneralConfiguration(Preferences.getLanguage(SplashScreenActivity.this))
                    .retry(3)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());

            Observable<BettingConfiguration> bettingConfigRequest = NetworkHelper.getBetXService(AppConstants.BETTING_API).getBettingConfiguration(Preferences.getLanguage(SplashScreenActivity.this),
                    Preferences.getTerminalId(SplashScreenActivity.this))
                    .retry(3)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
            Observable<ApplicationUserConfiguration> configRequest = NetworkHelper.getBetXService(AppConstants.TERMINAL_API).getConfiguration(Preferences.getLanguage(SplashScreenActivity.this),
                    Preferences.getTerminalId(SplashScreenActivity.this))
                    .retry(3)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());


            if (BetXApplication.translationMap.size() == 0) {
                Observable<JsonArray> translationsRequest = NetworkHelper.getBetXService(AppConstants.TERMINAL_API).getTranslation(Preferences.getLanguage(SplashScreenActivity.this))
                        .retry(3)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread());

                combinedObservableRequest = Observable.zip(generalConfigRequest, bettingConfigRequest, configRequest, translationsRequest,
                        (generalConfiguration, bettingConfiguration, applicationUserConfiguration, jsonElements) ->
                                new WholeConfig(generalConfiguration, applicationUserConfiguration, bettingConfiguration, jsonElements));

            } else {
                combinedObservableRequest = Observable.zip(generalConfigRequest, bettingConfigRequest, configRequest,
                        (generalConfiguration, bettingConfiguration, applicationUserConfiguration) ->
                                new WholeConfig(generalConfiguration, applicationUserConfiguration, bettingConfiguration, null));
            }

            mSubscription = combinedObservableRequest.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<WholeConfig>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            new AlertDialog.Builder(SplashScreenActivity.this)
                                    .setTitle("Error")
                                    .setCancelable(false)
                                    .setMessage("Unknown error occurred on server side during retrieving application configuration!")
                                    .setPositiveButton(android.R.string.yes, (dialog, which) -> finish())
                                    .setIcon(R.drawable.ic_ticket_lose)
                                    .show();
                            //AppUtils.createToastMessage(SplashScreenActivity.this, new Toast(SplashScreenActivity.this), "Unknown error occurred on server side!");
                            AppLogger.error(TAG, "Exception occured with message: ", e);
                        }

                        @Override
                        public void onNext(WholeConfig wholeConfig) {
                            BetXApplication betX = (BetXApplication) getApplication();

                            if (BetXApplication.translationMap.size() == 0) {
                                handleTranslations(wholeConfig.mTranslations, betX);
                            }

                            handleBettingConfig(wholeConfig.mBettingConfiguration);

                            handleApplicationConfiguration(wholeConfig.mApplicationUserConfiguration);

                            handleGeneralConfig(wholeConfig.mGeneralConfiguration);

                            Date date = new Date();
                            long period = date.getTime() - startTime.getTime();

                            if (period < AppConstants.SPLASH_SCREEN_TIME) {
                                createTimerForMainScreen(period);
                            } else {
                                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();
                            }
                        }
                    });
        } else {
            // Register receiver to notify when device is connected to internet
            if (networkStateReceiver == null) {
                networkStateReceiver = new NetworkStateReceiver();
                networkStateReceiver.addListener(this);
                this.registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
            }
            NetworkHelper.showNoConnectionDialog(SplashScreenActivity.this);
        }
    }

    private void handleGeneralConfig(GeneralConfiguration generalConfiguration) {
        Preferences.setCurrencyId(SplashScreenActivity.this, generalConfiguration.getCurrencyId());
        Preferences.setCurrencySign(SplashScreenActivity.this, generalConfiguration.getCurrencySign());
    }

    private void handleApplicationConfiguration(ApplicationUserConfiguration applicationUserConfiguration) {
        BetXApplication.setUserConfiguration(applicationUserConfiguration);
    }

    private void handleBettingConfig(BettingConfiguration bettingConfiguration) {
        Preferences.setBettingMinPayIn(SplashScreenActivity.this, bettingConfiguration.getMinPayIn());
        Preferences.setBettingSystemMinPayIn(SplashScreenActivity.this, bettingConfiguration.getSystemMinPayIn());
        if (Preferences.getLastPayInValue(SplashScreenActivity.this) == 0) {
            Preferences.setLastPayInValue(SplashScreenActivity.this, bettingConfiguration.getMinPayIn());
        }
    }

    private void handleTranslations(JsonArray translations, BetXApplication betX) {
        Gson gson = new Gson();
        ArrayList<TranslationResult> translationResults = gson.fromJson(translations, new TypeToken<ArrayList<TranslationResult>>() {
        }.getType());

        Map<String, String> map = new HashMap<>();
        for (TranslationResult translationResult : translationResults) {
            map.put(translationResult.getTk(), translationResult.getT());
        }
        BetXApplication.translationMap = map;

        betX.saveTranslationsData();
    }

    private void createTimerForMainScreen(long period) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                SplashScreenActivity.this.runOnUiThread(() -> {
                    Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
                });
            }
        }, AppConstants.SPLASH_SCREEN_TIME - period);
    }

    @Override
    public void networkAvailable() {
        getConfiguration();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
        }
    }

    @Override
    public void networkUnavailable() {
    }


    private class NoInternetTimerTask extends TimerTask {
        public void run() {
            SplashScreenActivity.this.runOnUiThread(() -> getConfiguration());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (networkStateReceiver != null) {
            unregisterReceiver(networkStateReceiver);
        }

        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
