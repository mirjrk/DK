package com.zesium.android.betting.ui.payment;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zesium.android.betting.R;
import com.zesium.android.betting.utils.Preferences;
import com.zesium.android.betting.utils.AppUtils;

import static com.zesium.android.betting.BetXApplication.getApp;

/**
 * Activity that opens web view for deposit money.
 * Created by savic on 31-Mar-17.
 */

public class DepositBankPaymentActivity extends AppCompatActivity {
    private ProgressBar mProgressBar;
    private RelativeLayout rlProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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

        setContentView(R.layout.activity_deposit_bank_payment);

        // Set toolbar for tickets
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getResources().getString(R.string.fragment_title_bank));
        }
        rlProgressBar = (RelativeLayout)findViewById(R.id.rl_spinner_layout);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar_deposit);
        mProgressBar.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(getApp(), R.color.primary_red),
                android.graphics.PorterDuff.Mode.SRC_IN);

        WebView webView = (WebView) findViewById(R.id.wv_bank_deposit);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(this.getIntent().getDataString());

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                String errorMessage = error.toString();
                AppUtils.createToastMessage(getApp(), new Toast(DepositBankPaymentActivity.this), errorMessage);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                rlProgressBar.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                AppUtils.showBankList(getApp(), true);
                rlProgressBar.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
            }
        });
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
}
