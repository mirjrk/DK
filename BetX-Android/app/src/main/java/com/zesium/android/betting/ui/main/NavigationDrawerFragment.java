package com.zesium.android.betting.ui.main;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zesium.android.betting.R;
import com.zesium.android.betting.model.NavigationDrawerItem;
import com.zesium.android.betting.model.UserStatus;
import com.zesium.android.betting.model.user.User;
import com.zesium.android.betting.utils.AppConstants;
import com.zesium.android.betting.utils.AppLogger;
import com.zesium.android.betting.BetXApplication;
import com.zesium.android.betting.service.NetworkHelper;
import com.zesium.android.betting.utils.Preferences;
import com.zesium.android.betting.service.SignalRHelper;
import com.zesium.android.betting.model.util.TranslationConstants;
import com.zesium.android.betting.utils.AppUtils;
import com.zesium.android.betting.ui.user.LoginFragment;
import com.zesium.android.betting.ui.user.LoginWithPinActivity;
import com.zesium.android.betting.ui.user.PersonalInfoActivity;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Navigation drawer fragment creates side menu drawer for whole application.
 * Created by Ivan Panic on 12/16/2015.
 */
public class NavigationDrawerFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = NavigationDrawerFragment.class.getSimpleName();
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View containerView;
    private FragmentDrawerListener drawerListener;
    private NavigationDrawerAdapter mAdapter;
    private TextView tvEmail;
    private TextView tvFirstName;
    private TextView tvLastName;
    private TextView tvUserBalance;
    private RelativeLayout rlInitialsCircle;
    private TextView tvInitials;
    private TextView tvRegisterLogin;
    private boolean isUserDataFilled = false;

    private Call<User> loginUserRequest;
    private List<NavigationDrawerItem> mNavigationDrawerItems;
    private ProgressDialog mProgressDialog;
    private Handler mMainHandler;
    private SignalRHelper mSignalRHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        boolean darkTheme = Preferences.getTheme(getContext());

        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        RecyclerView recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);

        // Initialize widgets in navigation drawer
        RelativeLayout rlMainLayout = (RelativeLayout) layout.findViewById(R.id.main_layout_rl);
        if (darkTheme) {
            rlMainLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.black));
        } else {
            rlMainLayout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        }

        tvEmail = (TextView) layout.findViewById(R.id.tv_email);
        tvFirstName = (TextView) layout.findViewById(R.id.tv_first_name);
        tvLastName = (TextView) layout.findViewById(R.id.tv_last_name);
        tvUserBalance = (TextView) layout.findViewById(R.id.tv_user_balance);
        //tvBalanceCurrency = (TextView) layout.findViewById(R.id.tv_balance_currency);
        tvRegisterLogin = (TextView) layout.findViewById(R.id.tv_register_login);
        rlInitialsCircle = (RelativeLayout) layout.findViewById(R.id.rl_initials_circle);
        tvInitials = (TextView) layout.findViewById(R.id.tv_initials);
        RelativeLayout rlHeader = (RelativeLayout) layout.findViewById(R.id.nav_header_container);

        // Initialize navigation drawer adapter
        mNavigationDrawerItems = new ArrayList<>();
        mAdapter = new NavigationDrawerAdapter(getActivity(), mNavigationDrawerItems);

        initialiseNavigationDrawerItems();

        fillLoginOrRegisterTextView();

        // Initialize list of items in navigation drawer and set listener to it
        recyclerView.setAdapter(mAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(final View view, final int position) {
                new Handler().postDelayed(() -> {
                    drawerListener.onDrawerItemSelected(view, position);
                    mDrawerLayout.closeDrawer(containerView);
                }, 150);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));

        tvRegisterLogin.setOnClickListener(this);
        rlHeader.setOnClickListener(this);

        mMainHandler = new Handler(getActivity().getMainLooper());

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Preferences.getIsUserLogged(getActivity())) {
            if (!isUserDataFilled) {
                tvEmail.setVisibility(View.VISIBLE);
                tvEmail.setText(Preferences.getEmail(getActivity()));
                if (Preferences.getIsUserLogged(getActivity()) && mAdapter.getItemCount() != 9) {
                    tvEmail.setVisibility(View.VISIBLE);
                    tvEmail.setText(Preferences.getEmail(getActivity()));

                    String firstName = Preferences.getFirstName(getActivity());
                    tvFirstName.setVisibility(View.VISIBLE);
                    tvFirstName.setText(firstName);

                    String lastName = Preferences.getLastName(getActivity());
                    tvLastName.setVisibility(View.VISIBLE);
                    tvLastName.setText(lastName);

                    rlInitialsCircle.setVisibility(View.VISIBLE);
                    String initials = "";
                    if (firstName.length() > 0) {
                        initials = firstName.substring(0, 1).toUpperCase() + lastName.substring(0, 1).toUpperCase();
                    }

                    tvInitials.setText(initials);

                    isUserDataFilled = true;

                    tvRegisterLogin.setVisibility(View.GONE);

                    // Add logout item to navigation drawer
                    String logout;
                    if (BetXApplication.translationMap.containsKey(TranslationConstants.LOGOUT)) {
                        logout = BetXApplication.translationMap.get(TranslationConstants.LOGOUT);
                    } else {
                        logout = getString(R.string.logout_dialog_message_logout);
                    }
                    addItemToDrawer(logout, R.drawable.ic_logout);
                } else {
                    tvEmail.setVisibility(View.VISIBLE);
                    tvEmail.setText(Preferences.getEmail(getActivity()));

                    String firstName = Preferences.getFirstName(getActivity());
                    tvFirstName.setVisibility(View.VISIBLE);
                    tvFirstName.setText(firstName);

                    String lastName = Preferences.getLastName(getActivity());
                    tvLastName.setVisibility(View.VISIBLE);
                    tvLastName.setText(lastName);

                    rlInitialsCircle.setVisibility(View.VISIBLE);
                    String initials = "";
                    if (firstName.length() > 0) {
                        initials = firstName.substring(0, 1).toUpperCase() + lastName.substring(0, 1).toUpperCase();
                    }

                    tvInitials.setText(initials);
                }
                mAdapter.notifyItemInserted(8);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // If user is not previously subscribed to update balance results, do it here
        if (!Preferences.getUserToken(getActivity()).equals("")) {
            if (NetworkHelper.isNetworkAvailable(getActivity())) {
                if (mSignalRHelper == null) {
                    startSignalRFromBackgroundThread();
                }
            } else {
                NetworkHelper.showNoConnectionDialog(getActivity());
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (mSignalRHelper != null) {
            AppLogger.d(TAG, "Stop balance signalR");
            mSignalRHelper.stopSignalRInBackground();
            mSignalRHelper = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AppConstants.RESULT_OK) {
            if (requestCode == AppConstants.LOGIN_SCREEN_DATA) {
                // Add logout item to navigation drawer
                String logout;
                if (BetXApplication.translationMap.containsKey(TranslationConstants.LOGOUT)) {
                    logout = BetXApplication.translationMap.get(TranslationConstants.LOGOUT);
                } else {
                    logout = getString(R.string.navigation_drawer_logout);
                }
                addItemToDrawer(logout, R.drawable.ticket_win);
            }
        } else if (resultCode == 99) {
            if (requestCode == AppConstants.LOGIN_WITH_PIN_REQUEST_CODE) {
                // Make sure the request was successful
                logout();
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    /**
     * Adds item to navigation drawer
     *
     * @param title title of navigation drawer item
     * @param icon  icon of navigation drawer item
     */
    private void addItemToDrawer(String title, int icon) {
        NavigationDrawerItem navItem = new NavigationDrawerItem();
        navItem.setTitle(title);
        navItem.setIcon(icon);
        mAdapter.addNewItemToDrawer(navItem);
    }

    private void startSignalRFromBackgroundThread() {
        long expirationTime = 0L;
        try {
            expirationTime = Preferences.getTokenExpiresIn(getActivity());
        } catch (ClassCastException cce) {
            AppLogger.error(TAG, "Exception occurred with message: ", cce);
        }
        long timeInSeconds = System.currentTimeMillis() / 1000;
        if (timeInSeconds < expirationTime) {
            AppLogger.d(TAG, "Token time is ok, proceed with signalR subscribe!");
            String userTokenWithBearer = Preferences.getUserToken(getActivity());
            String tokenForSubscribe = userTokenWithBearer.replace(AppConstants.USER_TOKEN_BEARER, "");

            mSignalRHelper = new SignalRHelper();
            mSignalRHelper.startBalanceUpdatesInBackground(
                    this::liveBalanceUpdate,
                    tokenForSubscribe);
        } else {
            AppLogger.d(TAG, "Token time has expired, get new one!");
            makeLoginRequest();
        }
    }

    private void liveBalanceUpdate(double balance) {
        mMainHandler.post(() -> fillUserDataAndBalance(balance));
    }


    private void makeLoginRequest() {
        // Make login userDetailsRequest to server
        loginUserRequest = NetworkHelper.getBetXService(AppConstants.USER_AUTHORIZATION_API).loginUser(AppConstants.USER_AUTHORIZATION_GRANT_TYPE_VALUE,
                Preferences.getUserName(getActivity()), Preferences.getPassword(getActivity()), AppConstants.USER_AUTHORIZATION_CLIENT_ID_VALUE,
                AppConstants.USER_AUTHORIZATION_TERMINAL_TYPE_VALUE);
        loginUserRequest.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() != null) {
                    User user = response.body();
                    Preferences.setUserToken(getActivity(), user.getAccess_token());
                    Preferences.setRefeshToken(getActivity(), user.getRefresh_token());

                    long timeInSeconds = System.currentTimeMillis() / 1000;
                    long timeWithExpiration = timeInSeconds + Long.valueOf(user.getExpires_in());
                    Preferences.setTokenExpiresIn(getActivity(), timeWithExpiration);

                    if (!Preferences.getUserToken(getActivity()).equals("")) {
                        if (mSignalRHelper == null) {
                            startSignalRFromBackgroundThread();
                        }
                    }

                    cancelProgressDialog();
                } else {
                    cancelProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (getActivity() != null && !getActivity().isFinishing()) {
                    cancelProgressDialog();
                    int message = NetworkHelper.generateErrorMessage(t);
                    AppUtils.createToastMessage(getContext(), new Toast(getContext()), getActivity().getString(message));
                }
            }
        });
        // Show progress dialog
        mProgressDialog = new ProgressDialog(getActivity());
        String message;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_SPRT_TICKET_PLEASE_WAIT)) {
            message = BetXApplication.translationMap.get(TranslationConstants.MBL_SPRT_TICKET_PLEASE_WAIT);
        } else {
            message = getString(R.string.please_wait);
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    public void logout() {
        isUserDataFilled = false;

        // Clear save data about user from preferences
        Preferences.setFirstName(getActivity(), null);
        Preferences.setLastName(getActivity(), null);
        Preferences.setUserName(getActivity(), null);
        Preferences.setPassword(getActivity(), null);
        Preferences.setEmail(getActivity(), null);

        Preferences.setUserToken(getActivity(), null);
        Preferences.setRefeshToken(getActivity(), null);
        Preferences.setTerminalId(getActivity(), null);

        Preferences.setIsUserLogged(getActivity(), false);
        Preferences.setIsUserLoggedWithPin(getActivity(), false);

        // Disconnect from hub which is providing balance changes
        // mHubConnection.disconnect();
        AppLogger.i(NavigationDrawerFragment.class.getSimpleName(), "items " + mNavigationDrawerItems.size());
        // Remove tickets and logout items from navigation drawer
        if (mNavigationDrawerItems.size() > 8) {
            mNavigationDrawerItems.remove(8); //logout
        }
        mAdapter.notifyDataSetChanged();

        tvEmail.setVisibility(View.GONE);
        tvFirstName.setVisibility(View.GONE);
        tvLastName.setVisibility(View.GONE);
        tvUserBalance.setVisibility(View.GONE);
        rlInitialsCircle.setVisibility(View.GONE);
        tvRegisterLogin.setVisibility(View.VISIBLE);

        BetXApplication.sUserStatus = UserStatus.GUEST;

        if (mSignalRHelper != null) {
            mSignalRHelper.stopSignalRInBackground();
            mSignalRHelper = null;
        }
    }

    /**
     * Initializes user data and balance data in navigation drawer.
     *
     * @param balance value received from user notifications
     */
    private void fillUserDataAndBalance(Double balance) {
        NumberFormat nfUS = NumberFormat.getCurrencyInstance(Locale.GERMANY);
        String currency = nfUS.format(balance);
        tvUserBalance.setVisibility(View.VISIBLE);
        tvUserBalance.setText(currency);
    }

    /**
     * Initializes navigation drawer and defines its behavior.
     *
     * @param fragmentId   id of navigation drawer in layout
     * @param drawerLayout layout of drawer
     * @param toolbar      toolbar
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                hideKeyboard();
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                hideKeyboard();
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                hideKeyboard();
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerLayout.post(() -> mDrawerToggle.syncState());
    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_register_login:
                Fragment fragment = new LoginFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.commit();

                // Set the appropriate toolbar title
                ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
                if (actionBar != null) {
                    String login;
                    if (BetXApplication.translationMap.containsKey(TranslationConstants.LOGIN)) {
                        login = BetXApplication.translationMap.get(TranslationConstants.LOGIN);
                    } else {
                        login = getString(R.string.login);
                    }
                    actionBar.setTitle(login);
                }
                mDrawerLayout.closeDrawers();

                break;
            case R.id.nav_header_container:
                checkUserStatus();

                break;
        }
    }

    private void checkUserStatus() {
        switch (BetXApplication.sUserStatus) {
            case LOGGED:
                Intent intent = new Intent(getActivity(), LoginWithPinActivity.class);
                intent.putExtra(AppConstants.IS_FIRST_TIME_LOGIN_WITH_PIN, false);
                intent.putExtra(AppConstants.IS_PERSONAL_INFO_SCREEN, true);
                startActivityForResult(intent, AppConstants.LOGIN_WITH_PIN_REQUEST_CODE);
                break;
            case LOGGED_WITH_PIN:
                Intent personalInfoIntent = new Intent(getActivity(), PersonalInfoActivity.class);
                startActivity(personalInfoIntent);
                break;
            case REGISTERED:
                break;
        }
    }

    private interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    /**
     * Custom touch listener for items in navigation drawer
     */
    private static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private final GestureDetector gestureDetector;
        private final ClickListener clickListener;

        RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    interface FragmentDrawerListener {
        void onDrawerItemSelected(View view, int position);
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    private void cancelProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loginUserRequest != null) {
            loginUserRequest.cancel();
        }

        cancelProgressDialog();
    }

    private void initialiseNavigationDrawerItems() {
        // Initialize initial items to navigation drawer
        String home;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.HOME)) {
            home = BetXApplication.translationMap.get(TranslationConstants.HOME);
        } else {
            home = getString(R.string.navigation_drawer_home);
        }
        addItemToDrawer(home, R.drawable.ic_home);

        // Set live betting item
        String liveBetting;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.LIVE_BETTING)) {
            liveBetting = BetXApplication.translationMap.get(TranslationConstants.LIVE_BETTING);
        } else {
            liveBetting = getString(R.string.navigation_drawer_live_betting);
        }
        addItemToDrawer(liveBetting, R.drawable.ic_icon_live);

        // Set sports betting item
        String sportsBetting;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.SPORTS)) {
            sportsBetting = BetXApplication.translationMap.get(TranslationConstants.SPORTS);
        } else {
            sportsBetting = getString(R.string.navigation_drawer_sport_betting);
        }
        addItemToDrawer(sportsBetting, R.drawable.ic_sports_betting);

        // Set tickets item
        // if (Preferences.getIsUserLogged(getContext())){
        String myTickets;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.MY_TICKETS)) {
            myTickets = BetXApplication.translationMap.get(TranslationConstants.MY_TICKETS);
        } else {
            myTickets = getString(R.string.navigation_drawer_my_tickets);
        }
        addItemToDrawer(myTickets, R.drawable.ic_tickets);
        // }

        // Set bonuses item
        String bonuses;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.BONUSES)) {
            bonuses = BetXApplication.translationMap.get(TranslationConstants.BONUSES);
        } else {
            bonuses = getString(R.string.navigation_drawer_bonuses);
        }
        addItemToDrawer(bonuses, R.drawable.ic_bonuses);

        String paymentText;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.PAYMENT)) {
            paymentText = BetXApplication.translationMap.get(TranslationConstants.PAYMENT);
        } else {
            paymentText = getResources().getString(R.string.payment_title);
        }
        addItemToDrawer(paymentText, R.drawable.ic_transaction);

        // Set contact us item
        String about;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.ABOUT)) {
            about = BetXApplication.translationMap.get(TranslationConstants.ABOUT);
        } else {
            about = getString(R.string.navigation_drawer_about);
        }
        addItemToDrawer(about, R.drawable.ic_icon_info);

        // Set settings item
        String settings;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.SETTINGS)) {
            settings = BetXApplication.translationMap.get(TranslationConstants.SETTINGS);
        } else {
            settings = getString(R.string.navigation_drawer_settings);
        }
        addItemToDrawer(settings, R.drawable.ic_settings);
    }

    private void fillLoginOrRegisterTextView() {
        // Login or register
        StringBuilder loginOrRegister = new StringBuilder();
        if (BetXApplication.translationMap.containsKey(TranslationConstants.PLEASE_LOGIN)) {
            loginOrRegister.append(BetXApplication.translationMap.get(TranslationConstants.PLEASE_LOGIN));
        } else {
            loginOrRegister.append(getString(R.string.please_login));
        }
        loginOrRegister.append(" ");
        if (BetXApplication.translationMap.containsKey(TranslationConstants.OR)) {
            loginOrRegister.append(BetXApplication.translationMap.get(TranslationConstants.OR));
        } else {
            loginOrRegister.append(getString(R.string.or));
        }
        loginOrRegister.append(" ");
        if (BetXApplication.translationMap.containsKey(TranslationConstants.REGISTER)) {
            loginOrRegister.append(BetXApplication.translationMap.get(TranslationConstants.REGISTER));
        } else {
            loginOrRegister.append(getString(R.string.register));
        }
        tvRegisterLogin.setText(loginOrRegister);
    }
}
