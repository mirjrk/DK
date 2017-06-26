package com.zesium.android.betting.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.zesium.android.betting.R;
import com.zesium.android.betting.model.sports.Match;
import com.zesium.android.betting.model.sports.Offer;
import com.zesium.android.betting.BetXApplication;
import com.zesium.android.betting.model.util.TranslationConstants;
import com.zesium.android.betting.ui.widgets.SFFontTextView;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by simikic on 6/16/2017.
 */
public class BaseActivity extends AppCompatActivity implements IFloatingTicket, View.OnClickListener {
    private static final String TAG = BaseActivity.class.getSimpleName();

    private String matchStr;
    private String matchesStr;
    private String totalOddsStr;

    private View mFloatingView;

    private TextView tvMatchesNumber;
    private TextView tvTotalOdds;
    private TextView tvTotalOddsTxt;
    private NumberFormat nfDot;

    protected void setupFloatingView() {
        mFloatingView = findViewById(R.id.rl_floating_view);

        tvMatchesNumber = (SFFontTextView) findViewById(R.id.tv_matches);
        tvTotalOdds = (SFFontTextView) findViewById(R.id.tv_total_odds);
        tvTotalOddsTxt = (SFFontTextView) findViewById(R.id.tv_total_odds_txt);

        nfDot = NumberFormat.getNumberInstance(Locale.ENGLISH);
        nfDot.setMinimumFractionDigits(2);
        nfDot.setMaximumFractionDigits(2);
        nfDot.setGroupingUsed(true);

        setupTranslationsStrings();
    }

    private void setupTranslationsStrings() {
        if (BetXApplication.translationMap.containsKey(TranslationConstants.MATCH)) {
            matchStr = BetXApplication.translationMap.get(TranslationConstants.MATCH);
        } else {
            matchStr = getResources().getString(R.string.match_floating_view);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_SPRT_MATCHES_OFFER_CAPTION)) {
            matchesStr = BetXApplication.translationMap.get(TranslationConstants.MBL_SPRT_MATCHES_OFFER_CAPTION);
        } else {
            matchesStr = getResources().getString(R.string.matches_floating_view);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.TOTAL_ODDS)) {
            totalOddsStr = BetXApplication.translationMap.get(TranslationConstants.TOTAL_ODDS);
        } else {
            totalOddsStr = getResources().getString(R.string.total_odds);
        }
    }

    @Override
    public void hideFloatingTicket() {
        mFloatingView.setVisibility(View.GONE);
    }

    @Override
    public void showFloatingTicket() {
        mFloatingView.setVisibility(View.VISIBLE);
        mFloatingView.setOnClickListener(this);
    }

    @Override
    public void updateFloatingTicket() {
        String matchesText;
        int matchCounter = 0;
        double totalOdds = 1;

        for (Match match : BetXApplication.getApp().getSelectedBetIds().values()) {
            for (Offer offer : match.getOffers()) {
                matchCounter++;
                totalOdds *= offer.getOdds().get(0).getOdd();
            }
        }

        boolean showTicket = matchCounter > 0;
        if (matchCounter > 1) {
            matchesText = matchCounter + " " + matchesStr;
        } else {
            matchesText = matchCounter + " " + matchStr;
        }

        if (showTicket) {
            tvMatchesNumber.setText(String.valueOf(matchesText));
            String totalOddsString = totalOddsStr + ": ";
            tvTotalOddsTxt.setText(totalOddsString);
            tvTotalOdds.setText(nfDot.format(totalOdds));
            showFloatingTicket();
        } else {
            hideFloatingTicket();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_floating_view:
                Toast.makeText(BaseActivity.this, "Disabled", Toast.LENGTH_SHORT).show();
        }
    }
}
