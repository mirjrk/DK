package com.zesium.android.betting.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.zesium.android.betting.BetXApplication;
import com.zesium.android.betting.R;
import com.zesium.android.betting.model.UserStatus;
import com.zesium.android.betting.model.util.TranslationConstants;
import com.zesium.android.betting.ui.payment.PaymentActivity;
import com.zesium.android.betting.ui.user.LoginFragment;
import com.zesium.android.betting.ui.user.LoginWithPinActivity;
import com.zesium.android.betting.ui.widgets.SFFontTextView;
import com.zesium.android.betting.utils.AppConstants;
import com.zesium.android.betting.utils.AppLogger;
import com.zesium.android.betting.utils.AppUtils;
import com.zesium.android.betting.utils.Preferences;

/**
 * MainActivity class is main class where all necessarily data is initialized and created. It initializes navigation drawer and toolbar.
 * Created by Ivan Panic on 12/16/2015.
 */
public class MainActivity extends BaseActivity implements NavigationDrawerFragment.FragmentDrawerListener {
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private boolean darkTheme;
    private String exitDialogMsgTxt;
    private String logOutDialogMsgTxt;
    private String mLoginTitle;
    private String mSportsBettingTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        //Setting status bar color, can't change the Status Bar Color on pre-Lollipop devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.primary_dark_red));
        }

        darkTheme = Preferences.getTheme(this);
        if (darkTheme) {
            setTheme(R.style.MyMaterialThemeDark);
        } else {
            setTheme(R.style.MyMaterialThemeLight);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupFloatingView();

        // Set toolbar for main view
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Initialise string values from translations
        setTranslation();

        // Initialize navigation drawer
        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mNavigationDrawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);

        // Set listener to navigation drawer
        mNavigationDrawerFragment.setDrawerListener(this);

        Bundle extras = getIntent().getExtras();
        handleExtraValues(extras);
    }

    /**
     * This method handles received extras data so that appropriate fragment can be shown.
     *
     * @param extras extras object that can contain some additional data in intent.
     */
    private void handleExtraValues(Bundle extras) {
        int viewToDisplay = Preferences.getLastNavDrawerItem(MainActivity.this);
        if (extras != null) {
            boolean returnToTicket = extras.getBoolean(AppConstants.IS_TOKEN_EXPIRED);
            boolean isLoggedOut = extras.getBoolean(AppConstants.IS_LOGGED_OUT);
            boolean shouldTicketsBeShown = getIntent().getBooleanExtra(AppConstants.SHOULD_RETURN_TO_TICKETS, false);

            if (returnToTicket) {
                Fragment fragment = new LoginFragment();
                fragmentShow(fragment, mLoginTitle);
                hideFloatingTicket();
                getIntent().removeExtra(AppConstants.IS_TOKEN_EXPIRED);
            } else if (shouldTicketsBeShown) {
                viewToDisplay = 3;
                getIntent().removeExtra(AppConstants.SHOULD_RETURN_TO_TICKETS);
                displayView(viewToDisplay);
            } else if (isLoggedOut) {
                Fragment fragment = new LoginFragment();
                fragmentShow(fragment, mLoginTitle);

                mNavigationDrawerFragment.logout();
                getIntent().removeExtra(AppConstants.IS_LOGGED_OUT);
            }
        } else {
            if (viewToDisplay == 7 || viewToDisplay == 3) {
                hideFloatingTicket();
            }
            displayView(viewToDisplay);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //opening login fragment after changing password
        if (getIntent() != null) {
            openLoginFragment(getIntent());
        }

        setFloatingView();
    }

    /**
     * Method that opens login fragment after changing password.
     *
     * @param intent holding string which fragment should be opened.
     */
    private void openLoginFragment(Intent intent) {
        String openFragment = intent.getStringExtra(AppConstants.LOGIN_FRAGMENT);
        if (openFragment != null && openFragment.equals(AppConstants.LOGIN_FRAGMENT)) {
            Fragment fragment = new LoginFragment();
            fragmentShow(fragment, mLoginTitle);
        }
        hideFloatingTicket();
    }

    /**
     * onBackPressed method defines what happens when user click on back button in application.
     * If user is on home screen it will show exit dialog, and if is on some other screen,
     * user will be returned to home screen.
     */
    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment currentFrag = getSupportFragmentManager().findFragmentById(R.id.container_body);

        if (currentFrag instanceof HomeFragment) {
            showExitDialog();
        } else {
            fragmentManager.popBackStack();
            Fragment fragment = new HomeFragment();
            String title = "";
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment, title);
            fragmentTransaction.commit();
        }

        setFloatingView();
    }

    /**
     * Exit dialog which pop ups when user is about to exit application.
     */
    private void showExitDialog() {
        String ok;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.OK)) {
            ok = BetXApplication.translationMap.get(TranslationConstants.OK);
        } else {
            ok = getString(R.string.dialog_ok);
        }

        String cancel;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.CANCEL)) {
            cancel = BetXApplication.translationMap.get(TranslationConstants.CANCEL);
        } else {
            cancel = getString(R.string.dialog_cancel);
        }

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_exit, null);


        AlertDialog.Builder builder;
        if (darkTheme) {
            builder = new AlertDialog.Builder(this, R.style.DarkBackgroundFilterDialog).setView(layout);
        } else {
            builder = new AlertDialog.Builder(this, R.style.WhiteBackgroundFilterDialog).setView(layout);
        }

        SFFontTextView tvMessage = (SFFontTextView) layout.findViewById(R.id.tv_dialog_text);

        tvMessage.setText(exitDialogMsgTxt);

        final AlertDialog alertDialog = builder.setPositiveButton(ok, (dialog, which) -> moveTaskToBack(true)).setNegativeButton(cancel, (dialog, which) -> {
        }).create();

        alertDialog.show();

        alertDialog.setCancelable(false);
        // Setting width and height
        alertDialog.getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(
                () -> AppUtils.adjustDialog(getApplicationContext(), alertDialog));
    }

    @Override
    public void onDrawerItemSelected(View view, final int position) {

        // Add 200 milliseconds before navigation drawer starts to close to prevent flickering
        new Handler().postDelayed(() -> displayView(position), 300);
    }

    /**
     * Displays the appropriate view by clicked position
     *
     * @param position the position of item in navigation drawer
     */
    private void displayView(int position) {
        Fragment fragment = null;
        String title = "";
        int lastClickedPosition = 0;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = "";
                lastClickedPosition = 0;
                break;
            case 1:
                Toast.makeText(this, "Disabled", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this, "Disabled", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(this, "Disabled", Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(this, "Disabled", Toast.LENGTH_SHORT).show();
                break;
            case 5:
                if (BetXApplication.sUserStatus.equals(UserStatus.LOGGED)) {
                    Intent intent = new Intent(MainActivity.this, LoginWithPinActivity.class);
                    intent.putExtra(AppConstants.IS_FIRST_TIME_LOGIN_WITH_PIN, false);
                    intent.putExtra(AppConstants.IS_PERSONAL_INFO_SCREEN, false);
                    intent.putExtra(AppConstants.VIEW_TO_DISPLAY, 5);
                    startActivityForResult(intent, AppConstants.LOGIN_WITH_PIN_REQUEST_CODE);
                } else if (!Preferences.getUserToken(MainActivity.this).equals("")) {
                    Intent intent = new Intent(MainActivity.this, PaymentActivity.class);
                    startActivity(intent);
                    lastClickedPosition = 0;
                } else {
                    fragment = new LoginFragment();
                    title = mLoginTitle;
                }
                hideFloatingTicket();
                break;
            case 6:
                Toast.makeText(this, "Disabled", Toast.LENGTH_SHORT).show();
                break;
            case 7:
                Toast.makeText(this, "Disabled", Toast.LENGTH_SHORT).show();
                break;
            case 8:
                showLogOutDialog();
                break;
        }
        Preferences.setLastNavDrawerItem(MainActivity.this, lastClickedPosition);

        if (fragment != null) {
            fragmentShow(fragment, title);
        }
    }

    /**
     * This method shows forwarded fragment and sets title on action bar.
     *
     * @param fragment fragment that will be shown.
     * @param title    String value taht will be set as title.
     */
    private void fragmentShow(Fragment fragment, String title) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_body, fragment, title);
        fragmentTransaction.commit();

        // Set the appropriate toolbar title
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }

    /**
     * Shows log out dialog.
     */
    private void showLogOutDialog() {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_logout, null);
        String logOut;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.LOGOUT)) {
            logOut = BetXApplication.translationMap.get(TranslationConstants.LOGOUT);
        } else {
            logOut = getString(R.string.logout_dialog_message_logout);
        }

        SFFontTextView tvTitle = (SFFontTextView) layout.findViewById(R.id.logout_title_textview);
        tvTitle.setText(logOut);

        AlertDialog.Builder builder;
        if (darkTheme) {
            builder = new AlertDialog.Builder(this, R.style.DarkBackgroundFilterDialog).setView(layout);
        } else {
            builder = new AlertDialog.Builder(this, R.style.WhiteBackgroundFilterDialog).setView(layout);
        }

        SFFontTextView tvMessage = (SFFontTextView) layout.findViewById(R.id.tv_dialog_message);

        tvMessage.setText(logOutDialogMsgTxt);

        String cancel;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.CANCEL)) {
            cancel = BetXApplication.translationMap.get(TranslationConstants.CANCEL);
        } else {
            cancel = getString(R.string.dialog_cancel);
        }
        final AlertDialog alertDialog = builder.setPositiveButton(logOut,
                (dialog, which) -> {
                    mNavigationDrawerFragment.logout();
                    displayView(0);
                }).setNegativeButton(cancel, (dialog, which) -> {
        }).create();

        alertDialog.show();
        alertDialog.setCancelable(true);
        //setting width and height
        alertDialog.getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(
                () -> AppUtils.adjustDialog(getApplicationContext(), alertDialog));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case AppConstants.LOGIN_WITH_PIN_REQUEST_CODE:
                int viewToDisplay = 0;
                if (data != null) {
                    viewToDisplay = data.getIntExtra(AppConstants.VIEW_TO_DISPLAY, 0);
                }
                displayView(viewToDisplay);
                break;
            case AppConstants.BACK_FROM_CREATE_TICKET:
                Fragment sportFragment = getSupportFragmentManager().findFragmentByTag(mSportsBettingTitle);
                if (sportFragment != null) {
                    sportFragment.onActivityResult(requestCode, resultCode, data);
                }
                break;
            case AppConstants.BACK_FROM_MATCH_DETAILS:
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(mSportsBettingTitle);
                if (fragment != null) {
                    fragment.onActivityResult(requestCode, resultCode, data);
                } else {
                    AppLogger.i("MAIN ACTIVITY", "***Fragment is null!***");
                }
                break;
        }
    }

    /**
     * Method that show or hide floating view.
     */
    private void setFloatingView() {
        Fragment currentFrag = getSupportFragmentManager().findFragmentById(R.id.container_body);

        if (currentFrag instanceof HomeFragment) {
            updateFloatingTicket();
        } else {
            hideFloatingTicket();
        }
    }

    /**
     * Method that translates strings depending on chosen language.
     * If string's translation is not available string will get it's value from strings.xml file.
     */
    private void setTranslation() {
        if (BetXApplication.translationMap.containsKey(TranslationConstants.EXIT_DIALOG_MSG)) {
            exitDialogMsgTxt = BetXApplication.translationMap.get(TranslationConstants.EXIT_DIALOG_MSG);
        } else {
            exitDialogMsgTxt = getResources().getString(R.string.dialog_exit_text);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.LOGOUT_DIALOG_MSG)) {
            logOutDialogMsgTxt = BetXApplication.translationMap.get(TranslationConstants.LOGOUT_DIALOG_MSG);
        } else {
            logOutDialogMsgTxt = getResources().getString(R.string.logout_dialog_message);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.LOGIN)) {
            mLoginTitle = BetXApplication.translationMap.get(TranslationConstants.LOGIN);
        } else {
            mLoginTitle = getString(R.string.login);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.SPORTS)) {
            mSportsBettingTitle = BetXApplication.translationMap.get(TranslationConstants.SPORTS);
        } else {
            mSportsBettingTitle = getString(R.string.navigation_drawer_sport_betting);
        }
    }
}
