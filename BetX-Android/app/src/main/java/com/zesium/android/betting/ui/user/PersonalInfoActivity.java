package com.zesium.android.betting.ui.user;

import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zesium.android.betting.R;
import com.zesium.android.betting.model.user.UserDetails;
import com.zesium.android.betting.utils.AppConstants;
import com.zesium.android.betting.utils.AppLogger;
import com.zesium.android.betting.BetXApplication;
import com.zesium.android.betting.service.NetworkHelper;
import com.zesium.android.betting.utils.Preferences;
import com.zesium.android.betting.model.util.TranslationConstants;
import com.zesium.android.betting.utils.AppUtils;
import com.zesium.android.betting.utils.NetworkStateReceiver;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonalInfoActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {
    private static final String TAG = PersonalInfoActivity.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LinearLayout llProgressBar;
    private NetworkStateReceiver networkStateReceiver;
    private Toast mToast;
    private boolean isProfileDataRetrieved;
    private Call<UserDetails> userDetailsRequest;

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

        //Checking what theme is set
        boolean darkTheme = Preferences.getTheme(this);
        if (darkTheme) {
            setTheme(R.style.RegistrationThemeBackgroundColorLight);
        } else {
            setTheme(R.style.MyMaterialThemeLight);
        }

        setContentView(R.layout.activity_personal_info);

        // Set toolbar for tickets
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        // Initialise views
        llProgressBar = (LinearLayout) findViewById(R.id.ll_progress_bar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.pb_loading);
        if (progressBar != null) {
            progressBar.getIndeterminateDrawable().setColorFilter(
                    ContextCompat.getColor(PersonalInfoActivity.this, R.color.primary_red),
                    android.graphics.PorterDuff.Mode.SRC_IN);
        }

        viewPager.setVisibility(View.GONE);
        tabLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isProfileDataRetrieved) {
            if (NetworkHelper.isNetworkAvailable(PersonalInfoActivity.this)) {
                createGetProfileRequest();
            } else {
                if (networkStateReceiver == null) {
                    networkStateReceiver = new NetworkStateReceiver();
                    networkStateReceiver.addListener(this);
                    registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));
                }
                AppUtils.createToastMessage(PersonalInfoActivity.this, mToast, getResources().getString(R.string.check_internet));
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (networkStateReceiver != null) {
            unregisterReceiver(networkStateReceiver);
            networkStateReceiver = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Method createGetProfileRequest send userDetailsRequest to get details of users profile.
     */
    private void createGetProfileRequest() {

        userDetailsRequest = NetworkHelper.getBetXService(AppConstants.USER_API).getUserDetails(Preferences.getUserToken(PersonalInfoActivity.this),
                Preferences.getTerminalId(PersonalInfoActivity.this));
        userDetailsRequest.enqueue(new Callback<UserDetails>() {

            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                if (!PersonalInfoActivity.this.isFinishing()) {
                    UserDetails userDetails = response.body();
                    if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {

                        AppUtils.showDialogUnauthorized(PersonalInfoActivity.this);

                    } else {
                        if (userDetails != null) {
                            isProfileDataRetrieved = true;
                            setupViewPager(userDetails);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                AppLogger.error(TAG, "Error occurred while trying to get details about users profile", t);
                if (!PersonalInfoActivity.this.isFinishing()) {
                    int message = NetworkHelper.generateErrorMessage(t);
                    AppUtils.createToastMessage(getApplicationContext(), new Toast(getApplicationContext()), getString(message));
                }
            }
        });
    }


    /**
     * Method setupViewPager initializes view pager and tab layout with items that are added in Adapter.
     */
    private void setupViewPager(UserDetails userDetails) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Create detail offer fragment and add it to adapter
        String personal;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_PERSONAL_INFORMATION_CAPTION)) {
            personal = BetXApplication.translationMap.get(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_PERSONAL_INFORMATION_CAPTION);
        } else {
            personal = getString(R.string.personal);
        }

        String password;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.PASSWORD)) {
            password = BetXApplication.translationMap.get(TranslationConstants.PASSWORD);
        } else {
            password = getString(R.string.password);
        }
        String security;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.SECURITY_SETTINGS)) {
            security = BetXApplication.translationMap.get(TranslationConstants.SECURITY_SETTINGS);
        } else {
            security = getString(R.string.security);
        }

        adapter.addFragment(PersonalInfoFragment.newInstance(userDetails), personal);
        adapter.addFragment(SecurityQuestionFragment.newInstance(userDetails.getSecurityQuestionId(), userDetails.getSecurityAnswer()), security);
        adapter.addFragment(new PasswordFragment(), password);

        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.VISIBLE);
        llProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void networkAvailable() {
        if (!isProfileDataRetrieved) {
            createGetProfileRequest();
        }
    }

    @Override
    public void networkUnavailable() {

    }

    /**
     * Class ViewPagerAdapter creates nad initializes view pager adapter. Also it describes it's behaviour.
     */
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userDetailsRequest != null) {
            userDetailsRequest.cancel();
        }
    }
}
