package com.zesium.android.betting.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zesium.android.betting.BetXApplication;
import com.zesium.android.betting.R;
import com.zesium.android.betting.model.best_offer.BestOfferItem;
import com.zesium.android.betting.model.sports.Match;
import com.zesium.android.betting.model.sports.MostPlayedLimitModel;
import com.zesium.android.betting.model.sports.Sport;
import com.zesium.android.betting.model.sports.live.EUpdateType;
import com.zesium.android.betting.model.sports.live.LiveChange;
import com.zesium.android.betting.model.util.SportsBettingHelper;
import com.zesium.android.betting.model.util.TranslationConstants;
import com.zesium.android.betting.service.NetworkHelper;
import com.zesium.android.betting.service.SignalRHelper;
import com.zesium.android.betting.utils.NetworkStateReceiver;
import com.zesium.android.betting.ui.user.LoginFragment;
import com.zesium.android.betting.ui.user.LoginWithPinActivity;
import com.zesium.android.betting.utils.AppConstants;
import com.zesium.android.betting.utils.AppLogger;
import com.zesium.android.betting.utils.AppUtils;
import com.zesium.android.betting.utils.Preferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Home fragment which defines view for representing best offer of matches received from server.
 * Created by Ivan Panic on 12/16/2015.
 */
public class HomeFragment extends Fragment implements View.OnClickListener, NetworkStateReceiver.NetworkStateReceiverListener {
    private static final String TAG = HomeFragment.class.getSimpleName();

    private NetworkStateReceiver networkStateReceiver;
    private boolean isBestOfferCalled;

    private TextView tvTopBarHeader;
    private TextView tvTopBarText;

    private String loginWithPinTxt;
    private String enjoyPlacingBetsTxt;

    private BetXApplication mBetXApplication;

    private LiveOfferAdapter mLiveOfferAdapter;
    private ArrayList<BestOfferItem> mLiveOfferData;
    private String mLiveOfferTitle;

    private RecyclerView.Adapter mBestOfferAdapter;
    private ArrayList<BestOfferItem> mBestOfferData;
    private Handler mHandler;

    private Subscription mBestOfferSubscription;
    private Subscription mLiveOfferSubscription;
    private View mLiveOfferProgress;
    private TextView mNoLiveOfferView;
    private TextView mNoBestOffer;
    private View mBestOfferProgress;
    private SignalRHelper mSignalRHelper;
    private IFloatingTicket mCallback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mBestOfferData = new ArrayList<>();
        mLiveOfferData = new ArrayList<>();

        mBetXApplication = (BetXApplication) getActivity().getApplication();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        initializeHeader(rootView);
        initializeContactUs(rootView);

        initializeBestOfferHolder(rootView);
        initializeLiveOfferHolder(rootView);

        setTranslationForStringValues();
        return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mHandler = new Handler(context.getMainLooper());
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        if (toolbar != null && toolbar.getTitle() != null && !toolbar.getTitle().equals("")) {
            toolbar.setTitle("");
        }

        if (context instanceof IFloatingTicket) {
            mCallback = (IFloatingTicket) context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (NetworkHelper.isNetworkAvailable(getActivity())) {
            // Check if best offer data is previously received from splash screen and initialize list of items with it
            if (mBetXApplication.getMostPlayedGames() == null || mBetXApplication.getMostPlayedGames().size() == 0) {
                // If best offer matches are not received previously, check internet connection and make request for them
                getBestOfferMatches();
            } else {
                List<Sport> sports = mBetXApplication.getMostPlayedGames();

                if (sports != null) {
                    populateList(SportsBettingHelper.parseSportListResponse(mBetXApplication, sports, false), false);
                }
                mBetXApplication.getMostPlayedGames().clear();
            }

            // Fetch data for live matches
            getLiveOfferMatches();
        } else {
            // Register receiver to notify when device is connected to internet
            if (networkStateReceiver == null) {
                networkStateReceiver = new NetworkStateReceiver();
                networkStateReceiver.addListener(this);
                getActivity().registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
                AppUtils.createToastMessage(getActivity(), null, getString(R.string.check_internet));
            }

            mBestOfferAdapter.notifyDataSetChanged();
            mLiveOfferAdapter.notifyDataSetChanged();
        }

        fillHeaderBar();

        if (mBetXApplication.getSelectedBetIds().size() > 0) {
            mCallback.updateFloatingTicket();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (networkStateReceiver != null) {
            getActivity().unregisterReceiver(networkStateReceiver);
            networkStateReceiver = null;
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mSignalRHelper != null) {
            mSignalRHelper.stopSignalRInBackground();
            mSignalRHelper = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstants.LOGIN_WITH_PIN_REQUEST_CODE) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                fillHeaderBar();
            } else if (resultCode == 99) {
                NavigationDrawerFragment navigationDrawerFragment = (NavigationDrawerFragment)
                        getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
                navigationDrawerFragment.logout();

                Fragment fragment = new LoginFragment();
                takeToFragment(fragment, "");
            }
        }
    }

    /**
     * Initialize live offer list (recycler list view). Leave adapter empty until we receive data for it.
     *
     * @param parentView - Use this component to search for all child views.
     */
    private void initializeLiveOfferHolder(View parentView) {
        if (BetXApplication.translationMap.containsKey(TranslationConstants.LIVE_BETTING)) {
            mLiveOfferTitle = BetXApplication.translationMap.get(TranslationConstants.LIVE_BETTING);
        } else {
            mLiveOfferTitle = getResources().getString(R.string.navigation_drawer_live_betting);
        }

        mLiveOfferAdapter = new LiveOfferAdapter(mLiveOfferData, (IFloatingTicket) getActivity());
        LinearLayout liveOffer = (LinearLayout) parentView.findViewById(R.id.ll_live_offer);

        mLiveOfferProgress = liveOffer.findViewById(R.id.rl_empty_view);
        mNoLiveOfferView = (TextView) liveOffer.findViewById(R.id.tv_no_offer);

        String message;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.CURRENTLY_NO_LIVE_MATCHES_IN_PLAY)) {
            message = BetXApplication.translationMap.get(TranslationConstants.CURRENTLY_NO_LIVE_MATCHES_IN_PLAY);
        } else {
            message = getResources().getString(R.string.live_betting_no_matches);
        }

        mNoLiveOfferView.setText(message);
        initializeOfferHolder(liveOffer, mLiveOfferAdapter, mLiveOfferTitle);
    }

    /**
     * Initialize best offer list (recycler list view). Leave adapter empty until we receive data for it.
     *
     * @param parentView - Use this component to search for all child views.
     */
    private void initializeBestOfferHolder(View parentView) {
        String mBestOfferTitle;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.SPORTS)) {
            mBestOfferTitle = BetXApplication.translationMap.get(TranslationConstants.SPORTS);
        } else {
            mBestOfferTitle = getResources().getString(R.string.navigation_drawer_sport_betting);
        }

        mBestOfferAdapter = new BestOfferRecyclerAdapter(getActivity(), mBestOfferData, (IFloatingTicket) getActivity());
        LinearLayout bestOffer = (LinearLayout) parentView.findViewById(R.id.ll_best_offer);

        mBestOfferProgress = bestOffer.findViewById(R.id.rl_empty_view);
        mNoBestOffer = (TextView) bestOffer.findViewById(R.id.tv_no_offer);

        String message;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.THERE_ARE_NO_MATCHES)) {
            message = BetXApplication.translationMap.get(TranslationConstants.THERE_ARE_NO_MATCHES);
        } else {
            message = getContext().getResources().getString(R.string.text_no_matches);
        }

        mNoBestOffer.setText(message);
        initializeOfferHolder(bestOffer, mBestOfferAdapter, mBestOfferTitle);
    }

    /**
     * Initialize offer item components. Leave adapter empty until we receive data for it.
     *
     * @param parentView - Use this component to search for all child views.
     * @param adapter    - RecyclerView adapter that will be used for RV instance.
     * @param title      - Header bar title.
     */
    private void initializeOfferHolder(View parentView, RecyclerView.Adapter adapter, String title) {
        TextView tvOfferTitle = (TextView) parentView.findViewById(R.id.tv_title);
        tvOfferTitle.setText(title);
        tvOfferTitle.setTag(title);
        tvOfferTitle.setOnClickListener(this);

        RecyclerView.LayoutManager llManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        RecyclerView rvOffer = (RecyclerView) parentView.findViewById(R.id.rv_betting_offer);

        RecyclerView.ItemAnimator animator = rvOffer.getItemAnimator();

        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        rvOffer.setHasFixedSize(true);
        rvOffer.setAdapter(adapter);
        rvOffer.setLayoutManager(llManager);
    }

    /**
     * Setup footer view for contact us area.
     *
     * @param parentView - Use this component to search for all child views.
     */
    private void initializeContactUs(View parentView) {
        TextView tvContactUs = (TextView) parentView.findViewById(R.id.tv_contact_us);
        LinearLayout llContactUs = (LinearLayout) parentView.findViewById(R.id.ll_contact_us);

        // Set contact us item
        String contactUs;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.CONTACT_US)) {
            contactUs = BetXApplication.translationMap.get(TranslationConstants.CONTACT_US);
        } else {
            contactUs = getString(R.string.navigation_drawer_about);
        }

        tvContactUs.setText(contactUs);
        llContactUs.setOnClickListener(this);
    }

    /**
     * Setup header view for sign up details.
     *
     * @param parentView - Use this component to search for all child views.
     */
    private void initializeHeader(View parentView) {

        tvTopBarHeader = (TextView) parentView.findViewById(R.id.tv_top_bar_header);
        tvTopBarText = (TextView) parentView.findViewById(R.id.tv_top_bar_text);

        LinearLayout llTopBar = (LinearLayout) parentView.findViewById(R.id.ll_top_bar);
        llTopBar.setOnClickListener(this);
    }

    private void fillHeaderBar() {
        String headerView = tvTopBarHeader.getText().toString();
        switch (BetXApplication.sUserStatus) {
            case LOGGED:
                if (!headerView.equals(loginWithPinTxt)) {
                    tvTopBarHeader.setText(loginWithPinTxt);
                    tvTopBarText.setVisibility(View.GONE);
                }
                break;
            case GUEST:
                String builder = getHeaderText();

                if (!headerView.equals(builder)) {
                    tvTopBarHeader.setText(builder);
                    tvTopBarText.setText(enjoyPlacingBetsTxt);
                }
                break;
            case LOGGED_WITH_PIN:
                if (!headerView.equals(enjoyPlacingBetsTxt)) {
                    tvTopBarHeader.setText(enjoyPlacingBetsTxt);
                    tvTopBarText.setVisibility(View.GONE);
                }
                break;
            case REGISTERED:
                break;
        }
    }

    private String getHeaderText() {
        StringBuilder builder = new StringBuilder();
        if (BetXApplication.translationMap.containsKey(TranslationConstants.USER_LOGIN)) {
            builder.append(BetXApplication.translationMap.get(TranslationConstants.USER_LOGIN));
        } else {
            builder.append(getString(R.string.please_login));
        }
        builder.append(" ");
        if (BetXApplication.translationMap.containsKey(TranslationConstants.OR)) {
            builder.append(BetXApplication.translationMap.get(TranslationConstants.OR));
        } else {
            builder.append(getString(R.string.or));
        }
        builder.append(" ");
        if (BetXApplication.translationMap.containsKey(TranslationConstants.REGISTER)) {
            builder.append(BetXApplication.translationMap.get(TranslationConstants.REGISTER));
        } else {
            builder.append(getString(R.string.register));
        }
        return builder.toString();
    }

    /**
     * Method creates request for getting best offer items
     */
    private void getBestOfferMatches() {
        isBestOfferCalled = true;

        MostPlayedLimitModel mostPlayedLimitModel = new MostPlayedLimitModel();
        mostPlayedLimitModel.setLimit(AppConstants.MATCH_LISTS_SIZE);

        AppLogger.i(TAG, "Get Best offer data");
        Observable<List<Sport>> mBestOfferRequest = NetworkHelper.getBetXService(AppConstants.SPORTS_API).getBestOffer(Preferences.getLanguage(getActivity()), mostPlayedLimitModel);


        mBestOfferSubscription = mBestOfferRequest
                .subscribeOn(Schedulers.io())
                .map(sports -> {
                            ArrayList<BestOfferItem> newData = SportsBettingHelper.parseSportListResponse(mBetXApplication, sports, false);
                            mBestOfferData.clear();
                            mBestOfferData.addAll(newData);
                            return sports.size() == 0;
                        }
                ).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLogger.error(TAG, "Exception occured with message: ", e);
                        if (mBestOfferData.size() == 0) {
                            mNoBestOffer.setVisibility(View.VISIBLE);
                            mBestOfferProgress.setVisibility(View.GONE);
                        }
                        /*if (getActivity() != null && !getActivity().isFinishing()) {
                            int message = NetworkHelper.generateErrorMessage(e);
                            AppUtils.createToastMessage(getActivity(), new Toast(getActivity()), getString(message));
                        }*/
                    }

                    @Override
                    public void onNext(Boolean isEmptyResponse) {
                        if (isEmptyResponse) {
                            mNoBestOffer.setVisibility(View.VISIBLE);
                            mBestOfferProgress.setVisibility(View.GONE);
                        } else {
                            mBestOfferProgress.setVisibility(View.GONE);
                            mNoBestOffer.setVisibility(View.GONE);
                        }
                        mBestOfferAdapter.notifyDataSetChanged();
                    }
                });
    }

    /**
     * Method getLiveOfferMatches send request for all sportsForLiveBettingRequest that are currently available on server.
     */
    private void getLiveOfferMatches() {
        MostPlayedLimitModel mostPlayedLimitModel = new MostPlayedLimitModel();
        mostPlayedLimitModel.setLimit(AppConstants.MATCH_LISTS_SIZE);

        AppLogger.i(TAG, "Get Live offer data");
        Observable<List<Sport>> mLiveOfferRequest = NetworkHelper.getBetXService(AppConstants.SPORTS_API).getSportForLiveBetting(Preferences.getLanguage(getActivity()), mostPlayedLimitModel);
        mLiveOfferSubscription = mLiveOfferRequest
                .subscribeOn(Schedulers.io())
                .map(sports -> {
                    mLiveOfferProgress.post(() -> mLiveOfferProgress.setVisibility(View.GONE));


                    ArrayList<BestOfferItem> newData = SportsBettingHelper.parseSportListResponse(mBetXApplication, sports, false);
                    mLiveOfferData.clear();
                    mLiveOfferData.addAll(newData);
                    HashSet<String> betTypes = new HashSet<>();
                    HashSet<Integer> ids = new HashSet<>();

                    for (BestOfferItem item : newData) {
                        Match match = item.getMatch();
                        if (match.isLive()) {
                            if (match.getBasicOffer() != null) {
                                betTypes.add(match.getBasicOffer().getBetTypeKey());
                            }
                            ids.add(match.getId());
                        }
                    }
                    startSignalR(betTypes, ids);

                    return sports.size() == 0;
                }).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        AppLogger.i(TAG, "getSportForLiveBetting error");

                        if (mLiveOfferData.size() == 0) {
                            mLiveOfferProgress.setVisibility(View.GONE);
                            mNoLiveOfferView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onNext(Boolean isEmptyResponse) {
                        if (isEmptyResponse) {
                            mLiveOfferProgress.setVisibility(View.GONE);
                            mNoLiveOfferView.setVisibility(View.VISIBLE);
                        } else {
                            mNoLiveOfferView.setVisibility(View.GONE);
                            mLiveOfferProgress.setVisibility(View.GONE);
                        }

                        mLiveOfferAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_top_bar:
                String topHeaderText = tvTopBarHeader.getText().toString();

                if (topHeaderText.equals(loginWithPinTxt)) {
                    Intent intent = new Intent(getActivity(), LoginWithPinActivity.class);
                    intent.putExtra(AppConstants.IS_FIRST_TIME_LOGIN_WITH_PIN, false);
                    startActivityForResult(intent, AppConstants.LOGIN_WITH_PIN_REQUEST_CODE);
                } else if (topHeaderText.equals(getActivity().getString(R.string.login_or_register)) || topHeaderText.equals(getHeaderText())) {
                    Fragment fragment = new LoginFragment();
                    takeToFragment(fragment, "");
                }
                break;
            case R.id.ll_contact_us:
                Intent intent = new Intent(getActivity(), ContactUsActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_title:
                Toast.makeText(getActivity(), "Disabled", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void takeToFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment, tag);
        fragmentTransaction.commit();

        // Set the appropriate toolbar title
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            String title;
            if (fragment instanceof LoginFragment) {
                if (BetXApplication.translationMap.containsKey(TranslationConstants.LOGIN)) {
                    title = BetXApplication.translationMap.get(TranslationConstants.LOGIN);
                } else {
                    title = getString(R.string.login);
                }
            } else {
                title = mLiveOfferTitle;
            }
            actionBar.setTitle(title);
        }
    }

    @Override
    public void networkAvailable() {
        if (!isBestOfferCalled) {
            getBestOfferMatches();
        }
        getLiveOfferMatches();
    }

    @Override
    public void networkUnavailable() {
    }

    /**
     * Method that translates strings depending on chosen language.
     * If string's translation is not available string will get it's value from strings.xml file.
     */
    private void setTranslationForStringValues() {
        if (BetXApplication.translationMap.containsKey(TranslationConstants.ENTER_PIN_CODE)) {
            loginWithPinTxt = BetXApplication.translationMap.get(TranslationConstants.ENTER_PIN_CODE);
        } else {
            loginWithPinTxt = getResources().getString(R.string.title_activity_login_with_pin);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.ENJOY_PLACING_BETS)) {
            enjoyPlacingBetsTxt = BetXApplication.translationMap.get(TranslationConstants.ENJOY_PLACING_BETS);
        } else {
            enjoyPlacingBetsTxt = getResources().getString(R.string.enjoy_placing_bets_txt);
        }
    }

    /**
     * Populate list with new data and notify adapter off changes
     *
     * @param newData - list of sports to be added.
     * @param isLive  - Whether or not to update live or best offer list.
     */
    private void populateList(ArrayList<BestOfferItem> newData, boolean isLive) {
        if (isLive) {
            mLiveOfferData.clear();
            mLiveOfferData.addAll(newData);
            mLiveOfferAdapter.notifyDataSetChanged();

            HashSet<String> betTypes = new HashSet<>();
            HashSet<Integer> ids = new HashSet<>();

            for (BestOfferItem item : newData) {
                Match match = item.getMatch();
                if (match.isLive()) {
                    if (match.getBasicOffer() != null) {
                        betTypes.add(match.getBasicOffer().getBetTypeKey());
                    }
                    ids.add(match.getId());
                }
            }
            startSignalR(betTypes, ids);
        } else {
            mBestOfferData.clear();
            mBestOfferData.addAll(newData);
            mBestOfferAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Start live update for best offer list based on their bet type key and ids.
     *
     * @param betTypes - Bet type key filter list,
     * @param ids      - Match or sport id filter list.
     */
    private void startSignalR(HashSet<String> betTypes, HashSet<Integer> ids) {
        mSignalRHelper = new SignalRHelper();
        ArrayList<Integer> matchList = new ArrayList<>();
        matchList.addAll(ids);
        ArrayList<String> betsList = new ArrayList<>();
        betsList.addAll(betTypes);
        mSignalRHelper.startLiveBettingInBackground(
                this::updateMatchData,
                matchList,
                betsList,
                EUpdateType.LIVE_UPDATE_WITH_BET_TYPES);
    }

    /**
     * Callback that is triggered when match has to be updated.
     *
     * @param liveChange - Updated match model.
     */
    private void updateMatchData(LiveChange liveChange) {
        BestOfferItem adapterItem = mLiveOfferAdapter.getItemByMatchID(liveChange.getMatch().getId());
        int itemIndex = mLiveOfferAdapter.getIndexByMatchID(liveChange.getMatch().getId());

        if (liveChange.getHeaders() != null) {
            int columnCount = (liveChange.getHeaders().size() > 2 ? 3 : 2);
            SportsBettingHelper.checkFirstThreeOdds(adapterItem.getMatch(), liveChange.getMatch(), columnCount);
        }

        if (liveChange.getMatch().getOffer() != null && liveChange.getMatch().getOffer().getOdds().size() > 3) {
            for (int i = liveChange.getMatch().getOffer().getOdds().size() - 1; i > 2; i--) {
                liveChange.getMatch().getOffer().getOdds().remove(i);
            }
        }

        adapterItem.setMatch(liveChange.getMatch());
        mHandler.post(() -> mLiveOfferAdapter.notifyItemChanged(itemIndex));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mBestOfferSubscription != null && !mBestOfferSubscription.isUnsubscribed()) {
            mBestOfferSubscription.unsubscribe();
        }

        if (mLiveOfferSubscription != null && !mLiveOfferSubscription.isUnsubscribed()) {
            mLiveOfferSubscription.unsubscribe();
        }
    }
}
