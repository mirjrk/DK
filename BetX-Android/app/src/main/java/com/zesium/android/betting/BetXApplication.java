package com.zesium.android.betting;

import android.app.Application;
import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import com.zesium.android.betting.model.UserStatus;
import com.zesium.android.betting.model.configuration.ApplicationUserConfiguration;
import com.zesium.android.betting.model.sports.Match;
import com.zesium.android.betting.model.sports.Sport;
import com.zesium.android.betting.model.util.BetXDataStore;
import com.zesium.android.betting.utils.AppLogger;
import com.zesium.android.betting.utils.Preferences;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;

/**
 * Class that defines application lifecycle and stores needed data.
 * <p/>
 * Created by Ivan Panic on 12/25/2015.
 */
public class BetXApplication extends Application {

    private static final String TAG = BetXApplication.class.getSimpleName();

    private static BetXApplication sApp;
    private List<Sport> mMostPlayedGames;
    private List<Sport> mSportsOffer;
    private final String translationsFileName = "betx_translations";
    private final String dataFileName = "betx_data";
    private static ApplicationUserConfiguration userConfiguration;
    private static final String FORMAT = "%02d:%02d:%02d";
    private static CountDownTimer timer;
    private long timeUntilFinish = 0;
    private String tempUserName;
    private String tempPassword;
    private LinkedHashMap<Integer, Match> mSelectedBetIds;
    public static UserStatus sUserStatus;
    private BetXDataStore betXDataStore;

    public static Map<String, String> translationMap;
    private int mResendCodeCounter;
    public static int loginWithPinAttemptCounter;

    // This value is set when some sport is selected for live offer and it is used inside live
    // service which updates odds to check if change that is received and its sport id are matched
    // with current tab in live offer
    private int currentLiveSportId;

    private NumberFormat mNumberFormat;

    @Override
    public void onCreate() {
        super.onCreate();

        sApp = this;

        betXDataStore = new BetXDataStore();
        translationMap = new HashMap<>();
        currentLiveSportId = 0;

        loadTranslationsOnStartup();
        loadDataOnStartup();

        mMostPlayedGames = new ArrayList<>();
        mSportsOffer = new ArrayList<>();

        mSelectedBetIds = new LinkedHashMap<>();

        if (Preferences.isUserLoggedWithPin(getApplicationContext())) {
            sUserStatus = UserStatus.LOGGED;
        } else {
            sUserStatus = UserStatus.GUEST;
        }

        mNumberFormat = NumberFormat.getNumberInstance(Locale.getDefault());
        mNumberFormat.setMinimumFractionDigits(2);
        mNumberFormat.setMaximumFractionDigits(2);

        Platform.loadPlatformComponent(new AndroidPlatformComponent());

        int oldSharedPrefVersion = Preferences.getSharedPrefVersion(getApp());

        // If version is changed reset user privileges
        if (BuildConfig.SHARED_PREF_VER > oldSharedPrefVersion) {
            AppLogger.d(TAG, "Shared preference version changed [" + oldSharedPrefVersion + " > " + BuildConfig.SHARED_PREF_VER + "] clear shared pref.");

            Preferences.clearAllPrefs(getApp());
            Preferences.setSharedPrefVersion(getApp(), BuildConfig.SHARED_PREF_VER);
        } else {
            AppLogger.d(TAG, "Shared preference version unchanged.");
        }

    }

    private void saveTranslationsToFile() {
        FileOutputStream fos = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            fos = openFileOutput(translationsFileName, Context.MODE_PRIVATE);
            objectOutputStream = new ObjectOutputStream(fos);
            objectOutputStream.writeObject(translationMap);

        } catch (IOException e) {
            AppLogger.error(TAG, "Exception occured with message: ", e);
        } finally {
            if (objectOutputStream != null) {
                try {
                    objectOutputStream.close();
                } catch (IOException e) {
                    AppLogger.e(TAG, e.toString());
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    AppLogger.e(TAG, e.toString());
                }
            }
        }
    }

    private void loadTranslationsOnStartup() {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = openFileInput(translationsFileName);
            ois = new ObjectInputStream(fis);
            translationMap = (Map<String, String>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            AppLogger.error(TAG, "Exception occured with message: ", e);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    AppLogger.e(TAG, e.toString());
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    AppLogger.e(TAG, e.toString());
                }
            }
        }
    }

    private void loadDataOnStartup() {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = openFileInput(dataFileName);
            ois = new ObjectInputStream(fis);
            betXDataStore = (BetXDataStore) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            AppLogger.error(TAG, "Exception occured with message: ", e);
        } finally {
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    AppLogger.e(TAG, e.toString());
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    AppLogger.e(TAG, e.toString());
                }
            }
        }
    }

    /**
     * Countdown timer that is ticking every second for 5 minutes. It represents time in which user
     * can enter confirmation code received via sms.
     *
     * @param textView TextView value that will be updated with timer.
     */
    public void creteCountdownTimer(final TextView textView, final Button button) {
        if (timeUntilFinish == 0) {
            timeUntilFinish = 5 * 60 * 1000;
        }
        if (timer != null) {
            timer.cancel();
        }
        timer = new CountDownTimer(timeUntilFinish, 1000) {

            public void onTick(long millisUntilFinished) {
                String text = "" + String.format(Locale.getDefault(), FORMAT,
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                textView.setText(text);
                timeUntilFinish = millisUntilFinished;
            }

            public void onFinish() {
                textView.setText(getApplicationContext().getString(R.string.time_is_up));
                button.setEnabled(false);
                timeUntilFinish = 0;
                Preferences.setIsRegistrationFinished(getApplicationContext(), false);
            }
        }.start();
    }

    public void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    public static BetXApplication getApp() {
        return sApp;
    }

    public List<Sport> getMostPlayedGames() {
        return mMostPlayedGames;
    }

    public void saveTranslationsData() {
        saveTranslationsToFile();
    }

    public static ApplicationUserConfiguration getUserConfiguration() {
        return userConfiguration;
    }

    public static void setUserConfiguration(ApplicationUserConfiguration userConfiguration) {
        BetXApplication.userConfiguration = userConfiguration;
    }

    public long getTimeUntilFinish() {
        return timeUntilFinish;
    }

    public void setTimeUntilFinish(long timeUntilFinish) {
        this.timeUntilFinish = timeUntilFinish;
    }

    public String getTempUserName() {
        return tempUserName;
    }

    public void setTempUserName(String tempUserName) {
        this.tempUserName = tempUserName;
    }

    public String getTempPassword() {
        return tempPassword;
    }

    public void setTempPassword(String tempPassword) {
        this.tempPassword = tempPassword;
    }

    public LinkedHashMap<Integer, Match> getSelectedBetIds() {
        return mSelectedBetIds;
    }

    public int getResendCodeCounter() {
        return mResendCodeCounter;
    }

    public void setResendCodeCounter(int resendCodeCounter) {
        mResendCodeCounter = resendCodeCounter;
    }
}
