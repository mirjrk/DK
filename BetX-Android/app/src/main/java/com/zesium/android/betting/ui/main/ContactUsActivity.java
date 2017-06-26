package com.zesium.android.betting.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zesium.android.betting.R;
import com.zesium.android.betting.BetXApplication;
import com.zesium.android.betting.utils.Preferences;
import com.zesium.android.betting.ui.widgets.SFFontTextView;
import com.zesium.android.betting.model.util.TranslationConstants;
import com.zesium.android.betting.utils.AppConstants;

public class ContactUsActivity extends AppCompatActivity implements View.OnClickListener {

    private SFFontTextView workingHoursTitle;
    private String workingHoursTitleTxt;

    private SFFontTextView workingDaysValue;
    private String workingDaysTxt;

    private SFFontTextView workingHoursValue;
    private String workingHoursTxt;

    private SFFontTextView telephoneTitle;
    private String telephoneTitleTxt;

    private SFFontTextView telephoneValue;
    private String telephoneValueTxt;

    private SFFontTextView addressTitle;
    private String addressTitleTxt;

    private SFFontTextView addressValue;
    private String addressValueTxt;

    private SFFontTextView regionValue;
    private String regionValueTxt;

    private SFFontTextView cityValue;
    private String cityValueTxt;

    private SFFontTextView emailTitle;
    private String emailTitleTxt;

    private SFFontTextView emailValue;
    private String emailValueTxt;

    private SFFontTextView versionTitle;
    private String versionTitleTxt;

    private SFFontTextView versionValue;
    private String versionValueTxt;

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

        //setting theme for this activity
        boolean darkTheme = Preferences.getTheme(this);
        if (darkTheme) {
            setTheme(R.style.ContactUsDark);
        } else {
            setTheme(R.style.MyMaterialThemeLight);
        }

        setContentView(R.layout.activity_contact_us);

        initializeContactUsData();

        setTranslationsForOtherStringValues();

        fillFieldsWithValues();

        // Set toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            // Set back arrow to toolbar
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setDisplayShowHomeEnabled(true);

            String title;
            if (BetXApplication.translationMap.containsKey(TranslationConstants.CONTACT_US)) {
                title = BetXApplication.translationMap.get(TranslationConstants.CONTACT_US);
            } else {
                title = getString(R.string.title_contact_us);
            }
            actionbar.setTitle(title);
        }

        ImageView betXLogo = (ImageView) findViewById(R.id.betx_logo_iv);
        if (betXLogo != null) {
            if (darkTheme) {
                betXLogo.setImageResource(R.drawable.ic_betx_logo_white);
            } else {
                betXLogo.setImageResource(R.drawable.ic_betx_logo);
            }
        }

        LinearLayout llTelephone = (LinearLayout) findViewById(R.id.ll_telephone);
        if (llTelephone != null) {
            llTelephone.setOnClickListener(this);
        }

        LinearLayout llEmail = (LinearLayout) findViewById(R.id.ll_email);
        if (llEmail != null) {
            llEmail.setOnClickListener(this);
        }

        fillTitles();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AppConstants.isFromForgottenPassword = true;
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AppConstants.isFromForgottenPassword = true;
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_telephone:
                //Opening dialer on click on phone number
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + getResources().getString(R.string.phone_number_one)));
                startActivity(intent);
                break;
            case R.id.ll_email:
                //Opening email client on click
                final Intent emailLauncher = new Intent(Intent.ACTION_SEND);
                String email = getResources().getString(R.string.betx_email);
                emailLauncher.setType("message/rfc822");
                emailLauncher.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                emailLauncher.putExtra(Intent.EXTRA_SUBJECT, "");
                emailLauncher.putExtra(Intent.EXTRA_TEXT, "");

                startActivity(Intent.createChooser(emailLauncher, ""));
                break;
        }
    }

    private void fillTitles() {
        SFFontTextView tvEmailTitle = (SFFontTextView) findViewById(R.id.tv_email_title);
        SFFontTextView tvAddressTitle = (SFFontTextView) findViewById(R.id.tv_address_title);

        String email;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_EMAIL_LABEL)) {
            email = BetXApplication.translationMap.get(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_EMAIL_LABEL);
        } else {
            email = getString(R.string.email);
        }
        tvEmailTitle.setText(email);

        //String address;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_ADDRESS_LABEL)) {
            email = BetXApplication.translationMap.get(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_ADDRESS_LABEL);
        } else {
            email = getString(R.string.address);
        }
        tvAddressTitle.setText(email);
    }


    private void fillFieldsWithValues() {
        workingHoursTitle.setText(workingHoursTitleTxt);
        workingDaysValue.setText(workingDaysTxt);
        workingHoursValue.setText(workingHoursTxt);
        telephoneTitle.setText(telephoneTitleTxt);
        telephoneValue.setText(telephoneValueTxt);
        addressTitle.setText(addressTitleTxt);
        addressValue.setText(addressValueTxt);
        regionValue.setText(regionValueTxt);
        cityValue.setText(cityValueTxt);
        emailTitle.setText(emailTitleTxt);
        emailValue.setText(emailValueTxt);
        versionTitle.setText(versionTitleTxt);
        versionValue.setText(versionValueTxt);
    }

    private void initializeContactUsData() {
        workingHoursTitle = (SFFontTextView) findViewById(R.id.tv_availability_title);
        workingDaysValue = (SFFontTextView) findViewById(R.id.tv_working_days_value);
        workingHoursValue = (SFFontTextView) findViewById(R.id.tv_working_hours_value);
        telephoneTitle = (SFFontTextView) findViewById(R.id.tv_telephone_title);
        telephoneValue = (SFFontTextView) findViewById(R.id.tv_telephone_number);
        addressTitle = (SFFontTextView) findViewById(R.id.tv_address_title);
        addressValue = (SFFontTextView) findViewById(R.id.tv_address_value);
        regionValue = (SFFontTextView) findViewById(R.id.tv_region_value);
        cityValue = (SFFontTextView) findViewById(R.id.tv_city_value);
        emailTitle = (SFFontTextView) findViewById(R.id.tv_email_title);
        emailValue = (SFFontTextView) findViewById(R.id.tv_email);
        versionTitle = (SFFontTextView) findViewById(R.id.tv_version_name);
        versionValue = (SFFontTextView) findViewById(R.id.tv_version_number);
    }


    /**
     * Method that translates strings depending on chosen language.
     * If string's translation is not available string will get it's value from strings.xml file.
     */
    private void setTranslationsForOtherStringValues() {

        if (BetXApplication.translationMap.containsKey(TranslationConstants.WORKING_HOURS_TITLE)) {
            workingHoursTitleTxt = BetXApplication.translationMap.get(TranslationConstants.WORKING_HOURS_TITLE);
        } else {
            workingHoursTitleTxt = getResources().getString(R.string.working_hours_title);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.WORKING_DAYS_VALUE)) {
            workingDaysTxt = BetXApplication.translationMap.get(TranslationConstants.WORKING_DAYS_VALUE);
        } else {
            workingDaysTxt = getResources().getString(R.string.working_days_value);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.WORKING_HOURS_VALUE)) {
            workingHoursTxt = BetXApplication.translationMap.get(TranslationConstants.WORKING_HOURS_VALUE);
        } else {
            workingHoursTxt = getResources().getString(R.string.working_hours_value);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.PHONE_NUMBER)) {
            telephoneTitleTxt = BetXApplication.translationMap.get(TranslationConstants.PHONE_NUMBER);
        } else {
            telephoneTitleTxt = getResources().getString(R.string.phone_number);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.PHONE_NUMBER_VALUE)) {
            telephoneValueTxt = BetXApplication.translationMap.get(TranslationConstants.PHONE_NUMBER_VALUE);
        } else {
            telephoneValueTxt = getResources().getString(R.string.phone_number_value);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_ADDRESS_CAPTION)) {
            addressTitleTxt = BetXApplication.translationMap.get(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_ADDRESS_CAPTION);
        } else {
            addressTitleTxt = getResources().getString(R.string.address_title);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.JUNIOR)) {
            addressValueTxt = BetXApplication.translationMap.get(TranslationConstants.JUNIOR);
        } else {
            addressValueTxt = getResources().getString(R.string.address_value);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.REGION_VALUE)) {
            regionValueTxt = BetXApplication.translationMap.get(TranslationConstants.REGION_VALUE);
        } else {
            regionValueTxt = getResources().getString(R.string.region_value);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.CITY_VALUE)) {
            cityValueTxt = BetXApplication.translationMap.get(TranslationConstants.CITY_VALUE);
        } else {
            cityValueTxt = getResources().getString(R.string.city_value);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.EMAIL)) {
            emailTitleTxt = BetXApplication.translationMap.get(TranslationConstants.EMAIL);
        } else {
            emailTitleTxt = getResources().getString(R.string.email_title);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.EMAIL_VALUE)) {
            emailValueTxt = BetXApplication.translationMap.get(TranslationConstants.EMAIL_VALUE);
        } else {
            emailValueTxt = getResources().getString(R.string.email_value);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.VERSION)) {
            versionTitleTxt = BetXApplication.translationMap.get(TranslationConstants.VERSION);
        } else {
            versionTitleTxt = getResources().getString(R.string.version_title);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.VERSION_VALUE)) {
            versionValueTxt = BetXApplication.translationMap.get(TranslationConstants.VERSION_VALUE);
        } else {
            versionValueTxt = getResources().getString(R.string.version_value);
        }
    }
}
