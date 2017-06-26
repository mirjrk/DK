package com.zesium.android.betting.service;

import android.os.Handler;
import android.os.HandlerThread;

import com.zesium.android.betting.BetXApplication;
import com.zesium.android.betting.model.sports.live.EUpdateType;
import com.zesium.android.betting.model.sports.live.LiveChange;
import com.zesium.android.betting.model.user.AccountChange;
import com.zesium.android.betting.utils.AppConstants;
import com.zesium.android.betting.utils.AppLogger;
import com.zesium.android.betting.utils.Preferences;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import microsoft.aspnet.signalr.client.Credentials;
import microsoft.aspnet.signalr.client.NullLogger;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.transport.ClientTransport;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by simikic on 5/12/2017.
 */

public class SignalRHelper {
    private static final String TAG = SignalRHelper.class.getSimpleName();

    private HubConnection mHubConnection;
    private HubProxy mHubProxy;
    private Subscription mSubscription;

    private HandlerThread mHandlerThread;
    private Handler mHandler;
    private Runnable mStartSignalR;

    public interface ILiveMatchUpdate {
        void onLiveMatchChanged(LiveChange liveChange);
    }

    public interface ILiveBalanceUpdate {
        void onLiveBalanceChanged(double balance);
    }

    public SignalRHelper() {
        mHandlerThread = new HandlerThread(AppConstants.LIVE_UPDATE);
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());
    }

    /**
     * Start signalR instance in background for given filters.
     *
     * @param callback   - Return channel for all updates that are received,
     * @param idFilter   - Match or sport id filter list,
     * @param btkFilter  - Bet type key filter list,
     * @param updateType - Type of signalR instance.
     */
    public void startLiveBettingInBackground(ILiveMatchUpdate callback, ArrayList<Integer> idFilter,
                                             ArrayList<String> btkFilter, EUpdateType updateType) {
        if (idFilter != null && idFilter.isEmpty()) {
            AppLogger.d(TAG, "No active matches, ignore starting signalR");
            return;
        }

        mStartSignalR = () -> mSubscription = Observable
                .just("")
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLogger.e(TAG, "Error occurred during subscribing SignalR on balance updates with message: " + e.toString());
                    }

                    @Override
                    public void onNext(String s) {
                        AppLogger.e(TAG, "Start SignalR in background");
                        String language = Preferences.getLanguage(BetXApplication.getApp());
                        mHubConnection = new HubConnection(AppConstants.SIGNALR_SPORT_API, AppConstants.PARAM_SIGNALR_LANGUAGE_QUERY_STRING + language,
                                false, new NullLogger());

                        // Create proxy for user balance updates
                        mHubProxy = mHubConnection.createHubProxy(AppConstants.NOTIFICATION);
                        ClientTransport clientTransport = new ServerSentEventsTransport(mHubConnection.getLogger());

                        SignalRFuture<Void> awaitConnection = mHubConnection.start(clientTransport);
                        try {
                            awaitConnection.get();
                        } catch (InterruptedException | ExecutionException e) {
                            AppLogger.error(TAG, "Exception occurred with message: ", e);
                        }

                        subscribeForUpdates(idFilter, btkFilter, updateType);

                        mHubConnection.reconnected(() -> {
                            AppLogger.d(TAG, "Reconnected");
                            subscribeForUpdates(idFilter, btkFilter, updateType);
                        });

                        // Subscribe to results of sport updates
                        initialiseLiveBettingCallback(callback, idFilter, updateType);
                    }
                });
        mHandler.postDelayed(mStartSignalR, AppConstants.SPLASH_SCREEN_TIME);
    }

    /**
     * Invoke methods on hub proxy to receive particular results via server push.
     *
     * @param idFilter  - Match or sport id filter list,
     * @param btkFilter - Bet type key filter list.
     */
    private void subscribeForUpdates(ArrayList<Integer> idFilter, ArrayList<String> btkFilter, EUpdateType updateType) {
        AppLogger.d(TAG, "BTK: " + btkFilter);
        AppLogger.d(TAG, "IDS: " + idFilter.toString());

        switch (updateType) {
            case LIVE_UPDATE_WITH_BET_TYPES:
                mHubProxy.invoke(AppConstants.REGISTER_MATCHES, idFilter);
                mHubProxy.invoke(AppConstants.REGISTER_FOR_BET_TYPES, btkFilter);
                break;
            case LIVE_MATCH_DETAILS:
                mHubProxy.invoke(AppConstants.REGISTER_MATCHES, idFilter);
                break;
            case LIVE_SPORT_BETTING:
                mHubProxy.invoke(AppConstants.REGISTER_SPORTS, idFilter);
                break;
        }
    }

    /**
     * Start with data acquisition for data with specific betTypes and idFilter.
     *
     * @param callback   - Return channel for all updates that are received,
     * @param idFilter   - Match or sport id filter list,
     * @param updateType - Type of signalR instance.
     */
    private void initialiseLiveBettingCallback(ILiveMatchUpdate callback, ArrayList<Integer> idFilter, EUpdateType updateType) {
        mHubProxy.on(AppConstants.LIVE_UPDATE, receivedData -> {
            for (LiveChange change : receivedData) {
                switch (updateType) {
                    case LIVE_UPDATE_WITH_BET_TYPES:
                    case LIVE_MATCH_DETAILS:
                        if (idFilter.contains(change.getMatch().getId())) {
                            callback.onLiveMatchChanged(change);
                        }
                        break;
                    case LIVE_SPORT_BETTING:
                        if (idFilter.contains(change.getSportId())) {
                            callback.onLiveMatchChanged(change);
                        }
                        break;
                }
            }
        }, LiveChange[].class);
    }

    /**
     * Start with balance update in background for logged in user.
     *
     * @param callback - Callback that will be fired when new data is received,
     * @param token    - Users token.
     */
    public void startBalanceUpdatesInBackground(ILiveBalanceUpdate callback, String token) {
        mStartSignalR = () -> mSubscription = Observable
                .just("")
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLogger.e(TAG, "Error occurred during subscribing SignalR on balance updates with message: " + e.toString());
                    }

                    @Override
                    public void onNext(String s) {
                        AppLogger.e(TAG, "Start balance updates in background");

                        String language = Preferences.getLanguage(BetXApplication.getApp());
                        Credentials credentials = request -> request.addHeader(AppConstants.AUTHORIZATION, token);

                        // Initialize hub connection for user balance
                        mHubConnection = new HubConnection(AppConstants.USER_BALANCE_API, AppConstants.PARAM_SIGNALR_LANGUAGE_QUERY_STRING + language, true, new NullLogger());
                        mHubConnection.setCredentials(credentials);

                        // Create proxy for user balance updates
                        mHubProxy = mHubConnection.createHubProxy(AppConstants.USER_ACCOUNT_NOTIFICATION);

                        ClientTransport clientTransport = new ServerSentEventsTransport(mHubConnection.getLogger());
                        SignalRFuture<Void> awaitConnection = mHubConnection.start(clientTransport);
                        try {
                            awaitConnection.get();
                        } catch (InterruptedException | ExecutionException e) {
                            AppLogger.error(TAG, "Exception occurred with message: ", e);
                        }

                        initialiseBalanceCallback(callback);
                    }
                });
        mHandler.postDelayed(mStartSignalR, AppConstants.SPLASH_SCREEN_TIME);
    }

    /**
     * @param callback - Callback that will be fired when new data is received.
     */
    private void initialiseBalanceCallback(ILiveBalanceUpdate callback) {
        mHubProxy.on(AppConstants.USER_ACCOUNT_DATA_UPDATE, receivedData -> {
            AppLogger.d(TAG, "Balance received");
            if (receivedData != null) {
                callback.onLiveBalanceChanged(receivedData.getBalance());
            }
        }, AccountChange.class);
    }

    /**
     * Stop signalR instance in background.
     */
    public void stopSignalRInBackground() {
        Observable.just("")
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLogger.e(TAG, "Error occurred during un subscribe SignalR updates with message: " + e.toString());
                    }

                    @Override
                    public void onNext(String s) {
                        AppLogger.e(TAG, "Stop SignalR in background");

                        if (mHandler != null) {
                            mHandler.removeCallbacksAndMessages(null);
                            mHandler = null;
                        }

                        if (mHubConnection != null) {
                            mHubConnection.disconnect();
                            mHubConnection.stop();
                            mHubConnection = null;
                        }

                        if (mHubProxy != null) {
                            mHubProxy.removeSubscription(AppConstants.NOTIFICATION);
                            mHubProxy = null;
                        }

                        if (mSubscription != null) {
                            mSubscription.unsubscribe();
                        }

                        if (mHandlerThread != null) {
                            mHandlerThread.quit();
                            mHandlerThread = null;
                        }
                    }
                });
    }
}
