package com.zesium.android.betting.ui.payment;


import android.content.Context;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.zesium.android.betting.R;
import com.zesium.android.betting.model.configuration.WalletConfiguration;
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

public class PaymentActivity extends AppCompatActivity implements NetworkStateReceiver.NetworkStateReceiverListener {
    private static final String TAG = PaymentActivity.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private LinearLayout llProgressBar;
    private boolean isConfigurationRetrieved;
    private boolean isPreviousDataRetrieved = false;
    private Toast mToast;
    private NetworkStateReceiver networkStateReceiver;
    private WalletConfiguration mWalletConfiguration;

    private String paymentTitle;
    private String depositFundsTabItem;
    private String withdrawFundsTabItem;
    private String transactionTabItem;
    private Call<WalletConfiguration> walletConfigurationRequest;

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

        // Set theme for this activity
        boolean darkTheme = Preferences.getTheme(this);
        if (darkTheme) {
            setTheme(R.style.DetailMatchOfferDarkTheme);
        } else {
            setTheme(R.style.MyMaterialThemeLight);
        }

        setTranslationsForStringValues();
        setContentView(R.layout.activity_payment);

        // Set toolbar for tickets
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(paymentTitle);
        }

        // Initialise views
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(findViewById(android.R.id.content).getWindowToken(), 0);
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        llProgressBar = (LinearLayout) findViewById(R.id.ll_progress_bar);

        // Spinner which is shown if no data is available to show to user
        ProgressBar pb_spinner = (ProgressBar) findViewById(R.id.pb_spinner);
        if (pb_spinner != null) {
            pb_spinner.getIndeterminateDrawable().setColorFilter(
                    ContextCompat.getColor(this, R.color.primary_red),
                    android.graphics.PorterDuff.Mode.SRC_IN);
        }

        viewPager.setVisibility(View.GONE);
        tabLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isConfigurationRetrieved) {
            if (NetworkHelper.isNetworkAvailable(PaymentActivity.this)) {
                getWalletConfiguration();
            } else {
                // Register receiver to notify when device is connected to internet
                if (networkStateReceiver == null) {
                    networkStateReceiver = new NetworkStateReceiver();
                    networkStateReceiver.addListener(this);
                    registerReceiver(networkStateReceiver, new IntentFilter(android.net.ConnectivityManager.CONNECTIVITY_ACTION));

                    createToastMessage(getString(R.string.check_internet));
                }
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

    private void getWalletConfiguration() {
        walletConfigurationRequest = NetworkHelper.getBetXService(AppConstants.WALLET_API).getWalletConfiguration(Preferences.getUserToken(PaymentActivity.this),
                Preferences.getTerminalId(PaymentActivity.this), Preferences.getLanguage(PaymentActivity.this));
        walletConfigurationRequest.enqueue(new Callback<WalletConfiguration>() {
            @Override
            public void onResponse(Call<WalletConfiguration> call, Response<WalletConfiguration> response) {
                if (!PaymentActivity.this.isFinishing()) {
                    if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        AppUtils.showDialogUnauthorized(PaymentActivity.this);
                    } else {
                        mWalletConfiguration = response.body();
                        isConfigurationRetrieved = true;
                        setupViewPager();
                    }
                }
            }

            @Override
            public void onFailure(Call<WalletConfiguration> call, Throwable t) {
                if (t instanceof JsonSyntaxException) {
                    checkPreviousData();
                } else {
                    if (!PaymentActivity.this.isFinishing()) {
                        int message = NetworkHelper.generateErrorMessage(t);
                        AppUtils.createToastMessage(PaymentActivity.this, new Toast(PaymentActivity.this), getString(message));
                    }
                }
                AppLogger.error(TAG, "Exception occurred with message: ", t);
            }
        });
    }

    /**
     * Check if previous data is retrieved to know when to initialize view.
     */
    private void checkPreviousData() {
        if (isPreviousDataRetrieved) {
            setupViewPager();
        } else {
            isPreviousDataRetrieved = true;
        }
    }

    /**
     * Method setupViewPager initializes view pager and tab layout with items that are added in Adapter.
     */
    private void setupViewPager() {
        isPreviousDataRetrieved = false;
        PaymentActivity.ViewPagerAdapter adapter = new PaymentActivity.ViewPagerAdapter(getSupportFragmentManager());

        // Create detail offer fragment and add it to adapter
        adapter.addFragment(DepositMoneyFragment.newInstance(), depositFundsTabItem);
        adapter.addFragment(WithdrawFundsFragment.newInstance(mWalletConfiguration), withdrawFundsTabItem);
        adapter.addFragment(new TransactionsFragment(), transactionTabItem);

        viewPager.setAdapter(adapter);

        viewPager.setOffscreenPageLimit(3);

        tabLayout.setupWithViewPager(viewPager);

        llProgressBar.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void networkAvailable() {
        if (!isConfigurationRetrieved) {
            getWalletConfiguration();
        }
    }

    @Override
    public void networkUnavailable() {

    }

    private void createToastMessage(String text) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(PaymentActivity.this, text, Toast.LENGTH_SHORT);
        mToast.show();
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

    /**
     * Method that translates strings depending on chosen language.
     * If string's translation is not available string will get it's value from strings.xml file.
     */
    private void setTranslationsForStringValues() {
        if (BetXApplication.translationMap.containsKey(TranslationConstants.PAYMENT)) {
            paymentTitle = BetXApplication.translationMap.get(TranslationConstants.PAYMENT);
        } else {
            paymentTitle = getResources().getString(R.string.payment_title);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.ACCOUNT_PAY_IN)) {
            depositFundsTabItem = BetXApplication.translationMap.get(TranslationConstants.ACCOUNT_PAY_IN);
        } else {
            depositFundsTabItem = getResources().getString(R.string.money_deposit_funds);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.PAYOUT)) {
            withdrawFundsTabItem = BetXApplication.translationMap.get(TranslationConstants.PAYOUT);
        } else {
            withdrawFundsTabItem = getResources().getString(R.string.money_withdraw_funds);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.MONEY_TRANSACTIONS)) {
            transactionTabItem = BetXApplication.translationMap.get(TranslationConstants.MONEY_TRANSACTIONS);
        } else {
            transactionTabItem = getResources().getString(R.string.money_transactions);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (walletConfigurationRequest != null) {
            walletConfigurationRequest.cancel();
        }
    }
}

