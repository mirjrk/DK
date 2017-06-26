package com.zesium.android.betting.ui.user;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zesium.android.betting.R;
import com.zesium.android.betting.model.user.ActionResultBase;
import com.zesium.android.betting.model.user.BetXValidationResult;
import com.zesium.android.betting.model.user.City;
import com.zesium.android.betting.model.user.Country;
import com.zesium.android.betting.model.user.Question;
import com.zesium.android.betting.model.user.Region;
import com.zesium.android.betting.model.user.Result;
import com.zesium.android.betting.model.user.UserMobileRegistrationModel;
import com.zesium.android.betting.model.user.UserPreferenceType;
import com.zesium.android.betting.utils.AppConstants;
import com.zesium.android.betting.utils.AppLogger;
import com.zesium.android.betting.BetXApplication;
import com.zesium.android.betting.model.util.DateAndTimeHelper;
import com.zesium.android.betting.service.NetworkHelper;
import com.zesium.android.betting.utils.Preferences;
import com.zesium.android.betting.model.util.TranslationConstants;
import com.zesium.android.betting.utils.AppUtils;
import com.zesium.android.betting.model.util.WSUtils;
import com.zesium.android.betting.ui.widgets.SFFontEditText;
import com.zesium.android.betting.ui.widgets.SFFontTextView;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int PERMISSION_RECEIVE_SMS = 123;
    private static final String TAG = RegistrationActivity.class.getSimpleName();
    private boolean isInitialCallForRequestPermissions = true;

    private Calendar mCalendar;
    private Map<String, Result> mCountriesMap = new HashMap<>();
    private Map<String, Region> mRegionsMap = new HashMap<>();
    private Map<String, City> mCitiesMap = new HashMap<>();
    private final Map<String, Question> mSecurityQuestions = new HashMap<>();

    // Text Input Layouts that shows error messages
    private TextInputLayout tilFirstName;
    private TextInputLayout tilLastName;
    private TextInputLayout tilStreetNameAndNumber;
    private TextInputLayout tilPostCode;
    private TextInputLayout tilUsername;
    private TextInputLayout tilPassword;
    private TextInputLayout tilConfirmPassword;
    private TextInputLayout tilSecurityAnswer;
    private TextInputLayout tilEmail;
    private TextInputLayout tilPhoneNumber;
    private TextInputLayout tilCountry;
    private TextInputLayout tilRegion;
    private TextInputLayout tilCity;
    private TextInputLayout tilSecurityQuestion;

    // Edit text fields that contains values of inserted data
    private SFFontEditText etFirstName;
    private SFFontEditText etLastName;
    private SFFontEditText etStreetNameAndNumber;
    private SFFontEditText etPostCode;
    private SFFontEditText etCountry;
    private SFFontEditText etRegion;
    private SFFontEditText etCity;
    private SFFontEditText etUsername;
    private SFFontEditText etPassword;
    private SFFontEditText etConfirmPassword;
    private SFFontEditText etSecurityQuestion;
    private SFFontEditText etSecurityAnswer;
    private SFFontEditText etEmail;
    private SFFontEditText etPhoneNumber;
    private SFFontTextView tvDateOfBirth;
    private LinearLayout llDateOfBirth;
    private SFFontTextView tvDateOfBirthTitle;
    private SFFontTextView tvPersonalInformation;
    private SFFontTextView tvAddress;
    private SFFontTextView tvAccountSettings;
    private SFFontTextView tvAccountSecurity;
    private SFFontTextView tvContactInformation;

    private SFFontTextView tvTermsAndConditions;
    private SFFontTextView tvStoreData;
    private SFFontTextView tvSendNews;
    private SFFontTextView tvOrderClubCard;

    // Other widgets
    private Button btnRegister;
    private LinearLayout llTermsAndConditions;
    private CheckBox cbTermsAndConditions;
    private LinearLayout llStoreData;
    private CheckBox cbStoreData;
    private LinearLayout llReceiveNews;
    private CheckBox cbReceiveNews;
    private LinearLayout llOrderClubCard;
    private CheckBox cbOrderClubCard;
    private Toast mToast;

    private ProgressDialog mProgressDialog;
    private int textColor;
    private int mPrimaryColor;
    private String mPasswordError;
    private String mConfirmPasswordError;
    private String mUsernameNotAvailableError;
    private String mUsernameError;
    private String mPhoneNumberError;
    private String mConfirmPasswordFocus;
    private String mEmailExample;
    private String mEmailFormatError;
    private String serverNotAvailableTxt;
    private String checkYourInternetTxt;
    private String chooseCountryFirstTxt;
    private String chooseRegionTxt;
    private String fixAboveErrors;
    private String choseSecurityQuestionFirst;
    private String mustSelectUserPreference;
    private String mustAcceptTermsAndUse;
    private String mustEnableRecordingPersonalData;
    private String birthDateIsNotValid;
    private String mustAllowReceiveSmsPremissionsInOrderToCompleteRegistration;
    private String choseSecurityQuestionTxt;
    private Call<Boolean> userFieldAvailabilityRequest;
    private Call<Country> countriesRequest;
    private Call<JsonArray> regionsRequest;
    private Call<JsonArray> citiesRequest;
    private Call<JsonArray> securityQuestionRequest;
    private Call<ActionResultBase> registerRequest;

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
            textColor = ContextCompat.getColor(this, R.color.white);
        } else {
            setTheme(R.style.MyMaterialThemeLight);
            textColor = ContextCompat.getColor(this, R.color.black);
        }
        setContentView(R.layout.activity_registration);

        BetXApplication betxApplication = (BetXApplication) getApplication();
        if (Preferences.getIsRegistrationFinished(RegistrationActivity.this) && betxApplication.getTimeUntilFinish() > 0) {
            Intent intent = new Intent(RegistrationActivity.this, ConfirmationCodeActivity.class);
            startActivity(intent);
        }

        String title;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.REGISTRATION)) {
            title = BetXApplication.translationMap.get(TranslationConstants.REGISTRATION);
        } else {
            title = getString(R.string.title_activity_registration);
        }

        mPrimaryColor = ContextCompat.getColor(this, R.color.primary_red);

        // Set toolbar for tickets
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(title);
        }

        // Initialize initial widgets
        initialiseFields();

        setEditTextBackground();

        setListeners();

        setTranslationForOtherStrignValues();

        fillFieldsWithInitialValues();

        Typeface typeface = Typeface.createFromAsset(getAssets(), AppConstants.DEFAULT_FONT);
        btnRegister.setTypeface(typeface);

        // Initialize calendar for date picker dialogs
        initialiseDatePicker();

        updateLabel(tvDateOfBirth);

        if (NetworkHelper.isNetworkAvailable(RegistrationActivity.this)) {
            createGetCountriesRequest(true);
            createGetSecurityQuestionsRequest(true);
        } else {
            AppUtils.createToastMessage(RegistrationActivity.this, mToast, checkYourInternetTxt);
        }

        // createRegisterRequest(new UserMobileRegistrationModel());
        AppUtils.hideKeyboard(RegistrationActivity.this);

        // If SMS permissions are not granted, registerRequest them-
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(RegistrationActivity.this,
                Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestReceiveSMSPermission(true);
        }
    }

    /**
     * Method that initialises date time picker based on current date and minimum years allowed for
     * gambling which is 18.
     */
    private void initialiseDatePicker() {
        mCalendar = Calendar.getInstance();

        mCalendar.add(Calendar.YEAR, -18);
        mCalendar.add(Calendar.DATE, -1);

        final DatePickerDialog.OnDateSetListener date = (view, year, monthOfYear, dayOfMonth) -> {
            mCalendar.set(Calendar.YEAR, year);
            mCalendar.set(Calendar.MONTH, monthOfYear);
            mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(tvDateOfBirth);
        };

        final DatePickerDialog datePickerDialog = new DatePickerDialog(RegistrationActivity.this, date, mCalendar
                .get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMaxDate(mCalendar.getTimeInMillis());

        llDateOfBirth.setOnClickListener(view -> {
            // Create date picker with calendar from previously set date
            datePickerDialog.show();
        });
    }

    /**
     * Request runtime permissions for receiving SMS messages.
     *
     * @param isInitialCall flag if it's initial call.
     */
    private void requestReceiveSMSPermission(boolean isInitialCall) {
        isInitialCallForRequestPermissions = isInitialCall;
        ActivityCompat.requestPermissions(RegistrationActivity.this,
                new String[]{Manifest.permission.RECEIVE_SMS},
                PERMISSION_RECEIVE_SMS);
    }

    private void fillFieldsWithInitialValues() {
        StringBuilder email = new StringBuilder();
        if (BetXApplication.translationMap.containsKey(TranslationConstants.EMAIL)) {
            email.append(BetXApplication.translationMap.get(TranslationConstants.EMAIL));
        } else {
            email.append(getResources().getString(R.string.email));
        }
        tilEmail.setHint(email);

        String username;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.USERNAME)) {
            username = BetXApplication.translationMap.get(TranslationConstants.USERNAME);
        } else {
            username = getResources().getString(R.string.username);
        }
        tilUsername.setHint(username);

        String phoneNumber;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.PHONE_NUMBER)) {
            phoneNumber = BetXApplication.translationMap.get(TranslationConstants.PHONE_NUMBER);
        } else {
            phoneNumber = getResources().getString(R.string.phone_number);
        }
        tilPhoneNumber.setHint(phoneNumber);

        String firstName;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.FIRST_NAME)) {
            firstName = BetXApplication.translationMap.get(TranslationConstants.FIRST_NAME);
        } else {
            firstName = getResources().getString(R.string.first_name);
        }
        tilFirstName.setHint(firstName);

        String lastName;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.LAST_NAME)) {
            lastName = BetXApplication.translationMap.get(TranslationConstants.LAST_NAME);
        } else {
            lastName = getResources().getString(R.string.last_name);
        }
        tilLastName.setHint(lastName);

        String personalInformation;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.PERSONAL_INFORMATION)) {
            personalInformation = BetXApplication.translationMap.get(TranslationConstants.PERSONAL_INFORMATION);
        } else {
            personalInformation = getResources().getString(R.string.personal_information);
        }
        tvPersonalInformation.setText(personalInformation);

        String dateOfBirth;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_DATE_OF_BIRTH_LABEL)) {
            dateOfBirth = BetXApplication.translationMap.get(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_DATE_OF_BIRTH_LABEL);
        } else {
            dateOfBirth = getResources().getString(R.string.date_of_birth);
        }
        tvDateOfBirthTitle.setText(dateOfBirth);

        String country;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.COUNTRY)) {
            country = BetXApplication.translationMap.get(TranslationConstants.COUNTRY);
        } else {
            country = getResources().getString(R.string.country);
        }
        tilCountry.setHint(country);

        String region;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.REGION)) {
            region = BetXApplication.translationMap.get(TranslationConstants.REGION);
        } else {
            region = getResources().getString(R.string.region);
        }
        tilRegion.setHint(region);

        String city;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.CITY)) {
            city = BetXApplication.translationMap.get(TranslationConstants.CITY);
        } else {
            city = getResources().getString(R.string.city);
        }
        tilCity.setHint(city);

        String postCode;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.ZIP_CODE)) {
            postCode = BetXApplication.translationMap.get(TranslationConstants.ZIP_CODE);
        } else {
            postCode = getResources().getString(R.string.post_code);
        }
        tilPostCode.setHint(postCode);

        String streetNameAndNumber;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_ADDRESS_PLACEHOLDER)) {
            streetNameAndNumber = BetXApplication.translationMap.get(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_ADDRESS_PLACEHOLDER);
        } else {
            streetNameAndNumber = getResources().getString(R.string.street_name_and_number);
        }
        tilStreetNameAndNumber.setHint(streetNameAndNumber);

        String address;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_ADDRESS_CAPTION)) {
            address = BetXApplication.translationMap.get(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_ADDRESS_CAPTION);
        } else {
            address = getResources().getString(R.string.address);
        }
        tvAddress.setText(address);

        String accountSettings;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.ACCOUNT_SETTINGS)) {
            accountSettings = BetXApplication.translationMap.get(TranslationConstants.ACCOUNT_SETTINGS);
        } else {
            accountSettings = getResources().getString(R.string.account_settings);
        }
        tvAccountSettings.setText(accountSettings);

        String password;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.PASSWORD)) {
            password = BetXApplication.translationMap.get(TranslationConstants.PASSWORD);
        } else {
            password = getResources().getString(R.string.password);
        }
        tilPassword.setHint(password);

        String retypePassword;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.RETYPE_PASSWORD)) {
            retypePassword = BetXApplication.translationMap.get(TranslationConstants.RETYPE_PASSWORD);
        } else {
            retypePassword = getResources().getString(R.string.confirm_new_password);
        }
        tilConfirmPassword.setHint(retypePassword);

        String secQuestion;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.CHOOSE_SECURITY_QUESTION)) {
            secQuestion = BetXApplication.translationMap.get(TranslationConstants.CHOOSE_SECURITY_QUESTION);
        } else {
            secQuestion = getResources().getString(R.string.choose_security_question);
        }
        tilSecurityQuestion.setHint(secQuestion);

        String secAnswer;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_SECURITY_ANSWER_LABEL)) {
            secAnswer = BetXApplication.translationMap.get(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_SECURITY_ANSWER_LABEL);
        } else {
            secAnswer = getResources().getString(R.string.security_answer);
        }
        tilSecurityAnswer.setHint(secAnswer);

        String accountSecurity;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.ACCOUNT_SECURITY)) {
            accountSecurity = BetXApplication.translationMap.get(TranslationConstants.ACCOUNT_SECURITY);
        } else {
            accountSecurity = getResources().getString(R.string.account_security);
        }
        tvAccountSecurity.setText(accountSecurity);

        String termsAndConditions;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_TERM_OF_USE_LABEL)) {
            termsAndConditions = BetXApplication.translationMap.get(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_TERM_OF_USE_LABEL);
        } else {
            termsAndConditions = getResources().getString(R.string.terms_and_conditions);
        }
        termsAndConditions += " *";
        tvTermsAndConditions.setText(termsAndConditions);

        String storeData;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_DATA_RECORDING_LABEL)) {
            storeData = BetXApplication.translationMap.get(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_DATA_RECORDING_LABEL);
        } else {
            storeData = getResources().getString(R.string.service_provider_store_data);
        }
        storeData += " *";
        tvStoreData.setText(storeData);

        String sendNews;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_EMAIL_NEWSLETTER_LABEL)) {
            sendNews = BetXApplication.translationMap.get(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_EMAIL_NEWSLETTER_LABEL);
        } else {
            sendNews = getResources().getString(R.string.receive_updates);
        }
        tvSendNews.setText(sendNews);

        String contactInformation;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_CONTACT_INFORMATION_CAPTION)) {
            contactInformation = BetXApplication.translationMap.get(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_CONTACT_INFORMATION_CAPTION);
        } else {
            contactInformation = getResources().getString(R.string.contact_information);
        }
        tvContactInformation.setText(contactInformation);

        String orderClubCard;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.SENDCLUBCARD)) {
            orderClubCard = BetXApplication.translationMap.get(TranslationConstants.SENDCLUBCARD);
        } else {
            orderClubCard = getResources().getString(R.string.order_club_card);
        }
        tvOrderClubCard.setText(orderClubCard);

        if (BetXApplication.translationMap.containsKey(TranslationConstants.VAL_PASSWORD)) {
            mPasswordError = BetXApplication.translationMap.get(TranslationConstants.VAL_PASSWORD);
        } else {
            mPasswordError = getString(R.string.error_password);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.VAL_RETYPE_PASSWORD)) {
            mConfirmPasswordError = BetXApplication.translationMap.get(TranslationConstants.VAL_RETYPE_PASSWORD);
        } else {
            mConfirmPasswordError = getString(R.string.error_confirm_password);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.USERNAME_UNAVAILABLE)) {
            mUsernameNotAvailableError = BetXApplication.translationMap.get(TranslationConstants.USERNAME_UNAVAILABLE);
        } else {
            mUsernameNotAvailableError = getString(R.string.error_username_not_available);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.WRONG_USERNAME_FORMAT)) {
            mUsernameError = BetXApplication.translationMap.get(TranslationConstants.WRONG_USERNAME_FORMAT);
        } else {
            mUsernameError = getString(R.string.error_username);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.WRONG_PHONE_FORMAT)) {
            mPhoneNumberError = BetXApplication.translationMap.get(TranslationConstants.WRONG_PHONE_FORMAT);
        } else {
            mPhoneNumberError = getString(R.string.error_phone_number);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.RETYPE_YOUR_PASSWORD)) {
            mConfirmPasswordFocus = BetXApplication.translationMap.get(TranslationConstants.RETYPE_YOUR_PASSWORD);
        } else {
            mConfirmPasswordFocus = getString(R.string.confirm_new_password);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.EMAIL_EXAMPLE)) {
            mEmailExample = BetXApplication.translationMap.get(TranslationConstants.EMAIL_EXAMPLE);
        } else {
            mEmailExample = getString(R.string.email_example);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.WRONG_EMAIL_FORMAT)) {
            mEmailFormatError = BetXApplication.translationMap.get(TranslationConstants.WRONG_EMAIL_FORMAT);
        } else {
            mEmailFormatError = getString(R.string.error_email_not_valid);
        }

        String btnRegistrationText;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.SUBMIT)) {
            btnRegistrationText = BetXApplication.translationMap.get(TranslationConstants.SUBMIT);
        } else {
            btnRegistrationText = getString(R.string.submit);
        }
        btnRegister.setText(btnRegistrationText);
    }

    private void setEditTextBackground() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            etFirstName.getBackground().setColorFilter(ContextCompat.getColor(RegistrationActivity.this, R.color.gray_line_divider), PorterDuff.Mode.SRC_ATOP);
            etEmail.getBackground().setColorFilter(ContextCompat.getColor(RegistrationActivity.this, R.color.gray_line_divider), PorterDuff.Mode.SRC_ATOP);
            etFirstName.getBackground().setColorFilter(ContextCompat.getColor(RegistrationActivity.this, R.color.gray_line_divider), PorterDuff.Mode.SRC_ATOP);
            etLastName.getBackground().setColorFilter(ContextCompat.getColor(RegistrationActivity.this, R.color.gray_line_divider), PorterDuff.Mode.SRC_ATOP);
            etStreetNameAndNumber.getBackground().setColorFilter(ContextCompat.getColor(RegistrationActivity.this, R.color.gray_line_divider), PorterDuff.Mode.SRC_ATOP);
            etPassword.getBackground().setColorFilter(ContextCompat.getColor(RegistrationActivity.this, R.color.gray_line_divider), PorterDuff.Mode.SRC_ATOP);
            etConfirmPassword.getBackground().setColorFilter(ContextCompat.getColor(RegistrationActivity.this, R.color.gray_line_divider), PorterDuff.Mode.SRC_ATOP);
            etCountry.getBackground().setColorFilter(ContextCompat.getColor(RegistrationActivity.this, R.color.gray_line_divider), PorterDuff.Mode.SRC_ATOP);
            etRegion.getBackground().setColorFilter(ContextCompat.getColor(RegistrationActivity.this, R.color.gray_line_divider), PorterDuff.Mode.SRC_ATOP);
            etCity.getBackground().setColorFilter(ContextCompat.getColor(RegistrationActivity.this, R.color.gray_line_divider), PorterDuff.Mode.SRC_ATOP);
            etPostCode.getBackground().setColorFilter(ContextCompat.getColor(RegistrationActivity.this, R.color.gray_line_divider), PorterDuff.Mode.SRC_ATOP);
            etSecurityQuestion.getBackground().setColorFilter(ContextCompat.getColor(RegistrationActivity.this, R.color.gray_line_divider), PorterDuff.Mode.SRC_ATOP);
            etSecurityAnswer.getBackground().setColorFilter(ContextCompat.getColor(RegistrationActivity.this, R.color.gray_line_divider), PorterDuff.Mode.SRC_ATOP);
            etPhoneNumber.getBackground().setColorFilter(ContextCompat.getColor(RegistrationActivity.this, R.color.gray_line_divider), PorterDuff.Mode.SRC_ATOP);
            etUsername.getBackground().setColorFilter(ContextCompat.getColor(RegistrationActivity.this, R.color.gray_line_divider), PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void setListeners() {
        // Set onFocusChangeListener to verify every field when user change focus
        etEmail.setOnFocusChangeListener(mEmailFocusChangeListener);
        etStreetNameAndNumber.setOnFocusChangeListener(mStreetNameAndNumberFocusChangeListener);
        etPassword.setOnFocusChangeListener(mPasswordFocusChangeListener);
        etConfirmPassword.setOnFocusChangeListener(mConfirmPasswordFocusChangeListener);
        etPhoneNumber.setOnFocusChangeListener(mPhoneNumberFocusChangeListener);
        etSecurityAnswer.setOnFocusChangeListener(mSecurityAnswerFocusChangeListener);
        etPostCode.setOnFocusChangeListener(mPostCodeFocusChangeListener);
        etFirstName.setOnFocusChangeListener(mFirstNameFocusChangeListener);
        etLastName.setOnFocusChangeListener(mLastNameFocusChangeListener);
        etUsername.setOnFocusChangeListener(mUsernameFocusChangeListener);

        // Set onCheckedChangeListeners to find out when to enable register button
        cbTermsAndConditions.setOnCheckedChangeListener(mOnCheckedChangeListener);
        cbStoreData.setOnCheckedChangeListener(mOnCheckedChangeListener);
        cbReceiveNews.setOnCheckedChangeListener(mOnCheckedChangeListener);


        AppUtils.selectCheckBox(llTermsAndConditions, cbTermsAndConditions);
        AppUtils.selectCheckBox(llStoreData, cbStoreData);
        AppUtils.selectCheckBox(llReceiveNews, cbReceiveNews);
        AppUtils.selectCheckBox(llOrderClubCard, cbOrderClubCard);


        // Set text change listeners to find out when to enable register button
        etEmail.addTextChangedListener(mTextWatcher);
        etFirstName.addTextChangedListener(mTextWatcher);
        etLastName.addTextChangedListener(mTextWatcher);
        etStreetNameAndNumber.addTextChangedListener(mTextWatcher);
        etPassword.addTextChangedListener(mTextWatcher);
        etConfirmPassword.addTextChangedListener(mTextWatcher);
        etCountry.addTextChangedListener(mTextWatcher);
        etRegion.addTextChangedListener(mTextWatcher);
        etCity.addTextChangedListener(mTextWatcher);
        etPostCode.addTextChangedListener(mTextWatcher);
        etSecurityQuestion.addTextChangedListener(mTextWatcher);
        etSecurityAnswer.addTextChangedListener(mTextWatcher);
        etPhoneNumber.addTextChangedListener(mTextWatcher);
        etUsername.addTextChangedListener(mTextWatcher);

        // Set On click listener
        etEmail.setOnClickListener(this);
        etFirstName.setOnClickListener(this);
        etLastName.setOnClickListener(this);
        etStreetNameAndNumber.setOnClickListener(this);
        etPassword.setOnClickListener(this);
        etConfirmPassword.setOnClickListener(this);
        etPostCode.setOnClickListener(this);
        etSecurityAnswer.setOnClickListener(this);
        etPhoneNumber.setOnClickListener(this);
        etUsername.setOnClickListener(this);
        etSecurityQuestion.setOnClickListener(this);
        etRegion.setOnClickListener(this);
        etCountry.setOnClickListener(this);
        etCity.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    private void initialiseFields() {
        etEmail = (SFFontEditText) findViewById(R.id.et_email);
        etFirstName = (SFFontEditText) findViewById(R.id.et_first_name);
        etLastName = (SFFontEditText) findViewById(R.id.et_last_name);
        etStreetNameAndNumber = (SFFontEditText) findViewById(R.id.et_street);
        etPassword = (SFFontEditText) findViewById(R.id.et_password);
        etConfirmPassword = (SFFontEditText) findViewById(R.id.et_confirm_password);
        etCountry = (SFFontEditText) findViewById(R.id.et_country);
        etRegion = (SFFontEditText) findViewById(R.id.et_region);
        etCity = (SFFontEditText) findViewById(R.id.et_city);
        etPostCode = (SFFontEditText) findViewById(R.id.et_post_code);
        etSecurityQuestion = (SFFontEditText) findViewById(R.id.et_security_question);
        etSecurityAnswer = (SFFontEditText) findViewById(R.id.et_security_answer);
        etPhoneNumber = (SFFontEditText) findViewById(R.id.et_phone_number);
        etUsername = (SFFontEditText) findViewById(R.id.et_username);
        tvDateOfBirth = (SFFontTextView) findViewById(R.id.tv_date_of_birth);
        llDateOfBirth = (LinearLayout) findViewById(R.id.ll_date_of_birth);
        tvDateOfBirthTitle = (SFFontTextView) findViewById(R.id.tv_date_of_birth_title);
        tvAddress = (SFFontTextView) findViewById(R.id.tv_address_title);
        tvPersonalInformation = (SFFontTextView) findViewById(R.id.tv_personal_information);
        tvAccountSettings = (SFFontTextView) findViewById(R.id.tv_account_settings);
        tvAccountSecurity = (SFFontTextView) findViewById(R.id.tv_account_security);
        tvTermsAndConditions = (SFFontTextView) findViewById(R.id.tv_terms_and_conditions);
        tvStoreData = (SFFontTextView) findViewById(R.id.tv_store_data);
        tvSendNews = (SFFontTextView) findViewById(R.id.tv_email_news);
        tvContactInformation = (SFFontTextView) findViewById(R.id.tv_contact_information);
        tvOrderClubCard = (SFFontTextView) findViewById(R.id.tv_order_club_card);
        SFFontTextView tvRegistrationMessage = (SFFontTextView) findViewById(R.id.tv_registration_message);

        String registrationMessageTxt;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.PLEASE_FILL_ALL_FIELDS_TO_COMPLETE_REGISTRATION)) {
            registrationMessageTxt = BetXApplication.translationMap.get(TranslationConstants.PLEASE_FILL_ALL_FIELDS_TO_COMPLETE_REGISTRATION);
        } else {
            registrationMessageTxt = getResources().getString(R.string.fill_all_fields_to_complete_registration);
        }
        tvRegistrationMessage.setText(registrationMessageTxt);

        tilEmail = (TextInputLayout) findViewById(R.id.til_email);
        tilStreetNameAndNumber = (TextInputLayout) findViewById(R.id.til_street);
        tilPassword = (TextInputLayout) findViewById(R.id.til_password);
        tilConfirmPassword = (TextInputLayout) findViewById(R.id.til_confirm_password);
        tilCountry = (TextInputLayout) findViewById(R.id.til_country);
        tilRegion = (TextInputLayout) findViewById(R.id.til_region);
        tilCity = (TextInputLayout) findViewById(R.id.til_city);
        tilSecurityAnswer = (TextInputLayout) findViewById(R.id.til_security_answer);
        tilSecurityQuestion = (TextInputLayout) findViewById(R.id.til_security_question);
        tilPhoneNumber = (TextInputLayout) findViewById(R.id.til_phone_number);
        tilPostCode = (TextInputLayout) findViewById(R.id.til_post_code);
        tilFirstName = (TextInputLayout) findViewById(R.id.til_first_name);
        tilLastName = (TextInputLayout) findViewById(R.id.til_last_name);
        tilUsername = (TextInputLayout) findViewById(R.id.til_username);

        btnRegister = (Button) findViewById(R.id.btn_register);

        llTermsAndConditions = (LinearLayout) findViewById(R.id.ll_terms_of_conditions);
        cbTermsAndConditions = (CheckBox) findViewById(R.id.cb_terms_and_conditions);

        llReceiveNews = (LinearLayout) findViewById(R.id.ll_receive_updates);
        cbReceiveNews = (CheckBox) findViewById(R.id.cb_receive_updates);

        llStoreData = (LinearLayout) findViewById(R.id.ll_store_data);
        cbStoreData = (CheckBox) findViewById(R.id.cb_store_data);

        llOrderClubCard = (LinearLayout) findViewById(R.id.ll_order_club_card);
        cbOrderClubCard = (CheckBox) findViewById(R.id.cb_order_club_card);
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
     * Method that calls web service for security securityQuestionRequest and returns list of them.
     *
     * @param isInitialCall boolean value to mark if select question dialog should appear
     */
    private void createGetSecurityQuestionsRequest(final boolean isInitialCall) {
        securityQuestionRequest = NetworkHelper.getBetXService(AppConstants.USER_API).getSecurityQuestions(Preferences.getLanguage(RegistrationActivity.this));
        securityQuestionRequest.enqueue(new Callback<JsonArray>() {

            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray questions = response.body();
                if (questions != null) {
                    for (JsonElement questionObj : questions.getAsJsonArray()) {
                        JsonObject questionData = questionObj.getAsJsonObject();
                        Question question = WSUtils.parseQuestionData(questionData);
                        if (question != null) {
                            mSecurityQuestions.put(question.getQuestion(), question);
                        }
                    }
                    if (!isInitialCall && mSecurityQuestions.size() > 0) {
                        selectSecurityQuestionDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                AppLogger.error(TAG, "Error occurred while trying to return a list of security questions", t);
                if (!RegistrationActivity.this.isFinishing()) {
                    int message = NetworkHelper.generateErrorMessage(t);
                    AppUtils.createToastMessage(getApplicationContext(), new Toast(getApplicationContext()), getString(message));
                }
            }
        });
    }

    /**
     * Method that calls web service for citiesRequest in selected region and returns list of them.
     *
     * @param selectedRegion String value which represents previously selected region
     * @param isInitialCall  boolean value to mark if select city dialog should appear
     */
    private void createCityRequest(String selectedRegion, final boolean isInitialCall) {
        Region selectedRegionValue = mRegionsMap.get(selectedRegion);
        citiesRequest = NetworkHelper.getBetXService(AppConstants.USER_API).getCities(String.valueOf(selectedRegionValue.getId()));
        citiesRequest.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray cities = response.body();
                if (mCitiesMap == null) {
                    mCitiesMap = new HashMap<>();
                } else {
                    mCitiesMap.clear();
                }
                if (cities != null) {
                    etCity.setEnabled(true);
                    for (JsonElement cityObj : cities.getAsJsonArray()) {
                        JsonObject cityData = cityObj.getAsJsonObject();
                        City city = WSUtils.parseCityData(cityData);
                        if (city != null) {
                            mCitiesMap.put(city.getName(), city);
                        }
                    }
                    if (!isInitialCall && mCitiesMap.size() > 0) {
                        selectCityDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                AppLogger.error(TAG, "Error occurred while trying to get a list of the cities in the specific region", t);
                if (!RegistrationActivity.this.isFinishing()) {
                    int message = NetworkHelper.generateErrorMessage(t);
                    AppUtils.createToastMessage(getApplicationContext(), new Toast(getApplicationContext()), getString(message));
                }

            }
        });
    }

    /**
     * Method that calls web service for regionsRequest in selected country and returns list of them.
     *
     * @param selectedCountry String value which represents previously selected country
     * @param isInitialCall   boolean value to mark if select region dialog should appear
     */
    private void creteRegionsRequest(String selectedCountry, final boolean isInitialCall) {
        Result selectedCountryResult = mCountriesMap.get(selectedCountry);
        regionsRequest = NetworkHelper.getBetXService(AppConstants.USER_API).getRegions(String.valueOf(selectedCountryResult.getId()));
        regionsRequest.enqueue(new Callback<JsonArray>() {

            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray regions = response.body();
                if (mRegionsMap == null) {
                    mRegionsMap = new HashMap<>();
                } else {
                    mRegionsMap.clear();
                }
                if (regions != null) {
                    etRegion.setEnabled(true);
                    for (JsonElement regionObj : regions.getAsJsonArray()) {
                        JsonObject regionData = regionObj.getAsJsonObject();
                        Region region = WSUtils.parseRegionData(regionData);
                        if (region != null) {
                            mRegionsMap.put(region.getName(), region);
                        }
                    }
                    if (!isInitialCall && mRegionsMap.size() > 0) {
                        selectRegionDialog();
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                AppLogger.error(TAG, "Error occurred while trying to get a list of the regions in the specific country", t);
                if (!RegistrationActivity.this.isFinishing()) {
                    int message = NetworkHelper.generateErrorMessage(t);
                    AppUtils.createToastMessage(getApplicationContext(), new Toast(getApplicationContext()), getString(message));
                }

            }
        });
    }

    /**
     * Method that calls web service for countriesRequest and returns list of them.
     *
     * @param isInitialCall boolean value to mark if select country dialog should appear
     */
    private void createGetCountriesRequest(final boolean isInitialCall) {
        countriesRequest = NetworkHelper.getBetXService(AppConstants.USER_API).getCountries();
        countriesRequest.enqueue(new Callback<Country>() {

            @Override
            public void onResponse(Call<Country> call, Response<Country> response) {
                Country country = response.body();
                if (country != null) {
                    if (country.getResult() != null) {
                        if (mCountriesMap == null) {
                            mCountriesMap = new HashMap<>();
                        }

                        for (Result result : country.getResult()) {
                            mCountriesMap.put(result.getName(), result);
                        }
                        if (!isInitialCall && mCountriesMap.size() > 0) {
                            selectCountryDialog();
                        }
                    }
                } else {
                    AppUtils.createToastMessage(RegistrationActivity.this, mToast, checkYourInternetTxt);
                }
            }

            @Override
            public void onFailure(Call<Country> call, Throwable t) {
                AppLogger.error(TAG, "Error occurred while trying to get countries", t);
                if (!RegistrationActivity.this.isFinishing()) {
                    int message = NetworkHelper.generateErrorMessage(t);
                    AppUtils.createToastMessage(getApplicationContext(), new Toast(getApplicationContext()), getString(message));
                }

            }
        });
    }

    /**
     * Focus listener to check if email is entered in valid format.
     */
    private final View.OnFocusChangeListener mEmailFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (!b) {
                checkEmail();
            } else {
                setErrorTextColor(tilEmail, textColor);
                tilEmail.setError(mEmailExample);
            }
        }
    };

    private void checkEmail() {
        if (!AppUtils.isValidEmail(etEmail.getText())) {
            setErrorTextColor(tilEmail, mPrimaryColor);
            tilEmail.setError(mEmailFormatError);
        } else {
            tilEmail.setError("");
        }
    }

    /**
     * Focus listener to check if street name and number is valid.
     */
    private final View.OnFocusChangeListener mStreetNameAndNumberFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (!b) {
                checkStreetName();
            } else {
                tilStreetNameAndNumber.setError("");
            }
        }
    };

    private void checkStreetName() {
        String streetName = etStreetNameAndNumber.getText().toString();
        String streetNameAndNumber;
        boolean isValid = streetName.matches("^((.){1,}(\\d){1,}(.){0,})$");
        if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_ADDRESS_FORMAT)) {
            streetNameAndNumber = BetXApplication.translationMap.get(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_ADDRESS_FORMAT);
        } else {
            streetNameAndNumber = getString(R.string.error_street_name_and_number);
        }

        if (!isValid) {
            tilStreetNameAndNumber.setError(streetNameAndNumber);
        } else {
            if (streetName.matches("[0-9]+") || streetName.startsWith(" ") || streetName.endsWith(" ")) {
                tilStreetNameAndNumber.setError(streetNameAndNumber);
            } else {
                tilStreetNameAndNumber.setError("");
            }
        }

    }

    /**
     * Focus listener to check if password is entered in valid format. Valid password should contain
     * 6-15 characters including one number and one capital letter
     */
    private final View.OnFocusChangeListener mPasswordFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (!b) {
                checkPassword();
            } else {
                setErrorTextColor(tilPassword, textColor);
                tilPassword.setError(mPasswordError + " ");
            }
        }
    };

    private void checkPassword() {
        String password = etPassword.getText().toString();
        boolean isValid = password.matches("^.*(?=.{6,})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).*$");
        if (!isValid) {
            setErrorTextColor(tilPassword, mPrimaryColor);
            tilPassword.setError(mPasswordError);
        } else {
            tilPassword.setError("");
        }
    }

    /**
     * Focus listener to check if confirm password is same as password.
     */
    private final View.OnFocusChangeListener mConfirmPasswordFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (!b) {
                checkConfirmPassword();
            } else {
                setErrorTextColor(tilConfirmPassword, textColor);
                tilConfirmPassword.setError(mConfirmPasswordFocus);
            }
        }
    };

    private void checkConfirmPassword() {
        if (!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
            setErrorTextColor(tilConfirmPassword, mPrimaryColor);
            tilConfirmPassword.setError(mConfirmPasswordError);
        } else {
            tilConfirmPassword.setError("");
        }
    }

    /**
     * Focus listener to check if password is entered in valid format. Valid password should contain
     * 6-15 characters including one number and one capital letter
     */
    private final View.OnFocusChangeListener mPhoneNumberFocusChangeListener = (view, b) -> {
        if (!b) {
            checkPhoneNumber();
        }
    };

    private void checkPhoneNumber() {
        String phoneNumberText = etPhoneNumber.getText().toString();
        boolean isValid = checkPhoneNumberFormat(phoneNumberText);
        if (!isValid) {
            setErrorTextColor(tilPhoneNumber, mPrimaryColor);
            tilPhoneNumber.setError(mPhoneNumberError);
        } else {
            tilPhoneNumber.setError("");
        }
    }

    private boolean checkPhoneNumberFormat(String phoneNumber) {
        boolean isValid = false;
        if (phoneNumber.startsWith("+") && phoneNumber.length() >= 10 && phoneNumber.length() < 15) {
            isValid = true;
        }
        return isValid;
    }
    
    /**
     * Focus listener to check if question answer is entered in valid format.
     */
    private final View.OnFocusChangeListener mSecurityAnswerFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (!b) {
                String phoneNumberText = etSecurityAnswer.getText().toString();
                if (phoneNumberText.length() > 20) {
                    String securityAnswerError;
                    if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_ACCOUNT_RESET_PASSWORD_FORM_SECURITY_ANSWER_PLACEHOLDER)) {
                        securityAnswerError = BetXApplication.translationMap.get(TranslationConstants.MBL_ACCOUNT_RESET_PASSWORD_FORM_SECURITY_ANSWER_PLACEHOLDER);
                    } else {
                        securityAnswerError = getString(R.string.error_security_answer);
                    }
                    tilSecurityAnswer.setError(securityAnswerError);
                } else {
                    tilSecurityAnswer.setError("");
                }
            } else {
                tilSecurityAnswer.setError("");
            }
        }
    };

    /**
     * Focus listener to check if post code is entered in valid format.
     */
    private final View.OnFocusChangeListener mPostCodeFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (!b) {
                checkPostCode();
            } else {
                tilPostCode.setError("");
            }
        }
    };

    private void checkPostCode() {
        String postCode = etPostCode.getText().toString();
        boolean isValid = postCode.matches("^[a-zA-Z0-9]{5,10}$");
        if (!isValid) {
            tilPostCode.setError(getResources().getString(R.string.error_post_code));
        } else {
            tilPostCode.setError("");
        }
    }

    /**
     * Focus listener to check if first name is entered in valid format.
     */
    private final View.OnFocusChangeListener mFirstNameFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (!b) {
                checkFirstName();
            } else {
                tilFirstName.setError("");
            }
        }
    };

    private void checkFirstName() {
        String firstName = etFirstName.getText().toString();
        boolean isValid = firstName.matches(".*^([ À-ǿa-zA-Z']{2,25})+$.*");
        String firstNameError;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.VAL_FIRST_NAME)) {
            firstNameError = BetXApplication.translationMap.get(TranslationConstants.VAL_FIRST_NAME);
        } else {
            firstNameError = getString(R.string.error_first_name);
        }
        if (!isValid) {
            tilFirstName.setError(firstNameError);
        } else {
            if (firstName.startsWith(" ")) {
                tilFirstName.setError(firstNameError);
            } else {
                tilFirstName.setError("");
            }
        }
    }

    /**
     * Focus listener to check if last name is entered in valid format.
     */
    private final View.OnFocusChangeListener mLastNameFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (!b) {
                checkLastName();
            } else {
                tilLastName.setError("");
            }
        }
    };

    private void checkLastName() {
        String lastName = etLastName.getText().toString();
        boolean isValid = lastName.matches(".*^([ À-ǿa-zA-Z']{2,25})+$.*");
        String lastNameError;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.VAL_LAST_NAME)) {
            lastNameError = BetXApplication.translationMap.get(TranslationConstants.VAL_LAST_NAME);
        } else {
            lastNameError = getString(R.string.error_last_name);
        }
        if (!isValid) {
            tilLastName.setError(lastNameError);
        } else {
            if (lastName.startsWith(" ")) {
                tilLastName.setError(lastNameError);
            } else {
                tilLastName.setError("");
            }

        }
    }

    /**
     * Focus listener to check if Username is entered in valid format.
     */
    private final View.OnFocusChangeListener mUsernameFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (!b) {
                checkUsername();
            } else {
                setErrorTextColor(tilUsername, textColor);
                tilUsername.setError(mUsernameError + " ");
            }
        }
    };

    private void checkUsername() {
        String userName = etUsername.getText().toString();
        boolean isValid = userName.matches(".*^[a-zA-Z0-9]{5,10}$.*");
        if (!isValid) {
            setErrorTextColor(tilUsername, mPrimaryColor);
            tilUsername.setError(mUsernameError);
        } else {
            if (NetworkHelper.isNetworkAvailable(RegistrationActivity.this)) {
                createCheckUserNameAvailabilityRequest();
            } else {
                AppUtils.createToastMessage(RegistrationActivity.this, mToast, checkYourInternetTxt);
            }
            tilUsername.setError("");
        }
    }

    private void createCheckUserNameAvailabilityRequest() {
        userFieldAvailabilityRequest = NetworkHelper.getBetXService(AppConstants.USER_API).userfieldavailability(AppConstants.ZERO, etUsername.getText().toString());
        userFieldAvailabilityRequest.enqueue(new Callback<Boolean>() {

            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                Boolean isAvailable = response.body();
                if (isAvailable != null) {
                    if (!isAvailable) {
                        setErrorTextColor(tilUsername, mPrimaryColor);
                        tilUsername.setError(mUsernameNotAvailableError);
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {

            }
        });
    }

    /**
     * Method updateLabel updates text view when date is picked.
     *
     * @param tempDate text view which will be updated
     */
    private void updateLabel(TextView tempDate) {
        SimpleDateFormat sdf = new SimpleDateFormat(DateAndTimeHelper.REGISTRATION_DATE, Locale.getDefault());
        tempDate.setText(sdf.format(mCalendar.getTime()));
        parseTextToDate();
    }

    private static void setErrorTextColor(TextInputLayout textInputLayout, int color) {
        try {
            Field fErrorView = TextInputLayout.class.getDeclaredField("mErrorView");
            fErrorView.setAccessible(true);
            TextView mErrorView = (TextView) fErrorView.get(textInputLayout);
            Field fCurTextColor = TextView.class.getDeclaredField("mCurTextColor");
            fCurTextColor.setAccessible(true);
            fCurTextColor.set(mErrorView, color);
        } catch (Exception e) {
            AppLogger.error(TAG, "Exception occurred with message: ", e);
        }
    }

    /**
     * Text watcher which is added to all required field to properly enable/disable register button.
     */
    private final TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // check Fields For Empty Values
            checkFieldsForEmptyValues();
        }
    };

    private final CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = (compoundButton, b) -> checkFieldsForEmptyValues();

    /**
     * Method checkFieldsForEmptyValues enables/disables done button depending on whether the required fields are filled.
     */
    private void checkFieldsForEmptyValues() {
        String s1 = etFirstName.getText().toString();
        String s2 = etLastName.getText().toString();
        String s3 = etStreetNameAndNumber.getText().toString();
        String s4 = etPostCode.getText().toString();

        String s5 = etCountry.getText().toString();
        String s6 = etRegion.getText().toString();
        String s7 = etCity.getText().toString();
        String s8 = etUsername.getText().toString();

        String s9 = etPassword.getText().toString();
        String s10 = etConfirmPassword.getText().toString();
        String s11 = etSecurityQuestion.getText().toString();
        String s12 = etSecurityAnswer.getText().toString();

        String s13 = etEmail.getText().toString();
        String s14 = tvDateOfBirth.getText().toString();
        String s15 = etPhoneNumber.getText().toString();

        boolean isTermsChecked = cbTermsAndConditions.isChecked();
        boolean isStoreDataChecked = cbStoreData.isChecked();

        if (s1.equals("") || s2.equals("") || s3.equals("") || s4.equals("")
                || s5.equals("") || s6.equals("") || s7.equals("") || s8.equals("") || s9.equals("")
                || s10.equals("") || s11.equals("") || s12.equals("") || s13.equals("") || s14.equals("")
                || s15.equals("") || !isTermsChecked || !isStoreDataChecked) {
            btnRegister.setEnabled(false);
        } else {
            btnRegister.setEnabled(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.et_country:
                tilSecurityQuestion.setError("");
                if (mCountriesMap.size() > 0) {
                    selectCountryDialog();
                } else {
                    if (NetworkHelper.isNetworkAvailable(RegistrationActivity.this)) {
                        createGetCountriesRequest(false);
                    } else {
                        AppUtils.createToastMessage(RegistrationActivity.this, mToast, checkYourInternetTxt);
                    }
                }
                break;
            case R.id.et_region:
                if (!etCountry.getText().toString().isEmpty()) {
                    tilRegion.setError("");
                    if (mRegionsMap.size() > 0) {
                        selectRegionDialog();
                    } else {
                        if (NetworkHelper.isNetworkAvailable(RegistrationActivity.this)) {
                            Result country = mCountriesMap.get(etCountry.getText().toString());
                            creteRegionsRequest(country.getName(), false);
                        } else {
                            AppUtils.createToastMessage(RegistrationActivity.this, mToast, checkYourInternetTxt);
                        }
                    }
                } else {
                    AppUtils.createToastMessage(RegistrationActivity.this, mToast, chooseCountryFirstTxt);
                }
                break;
            case R.id.et_city:
                if (!etRegion.getText().toString().isEmpty()) {
                    tilCity.setError("");
                    if (mCitiesMap.size() > 0) {
                        selectCityDialog();
                    } else {
                        if (NetworkHelper.isNetworkAvailable(RegistrationActivity.this)) {
                            Region region = mRegionsMap.get(etRegion.getText().toString());
                            createCityRequest(region.getName(), false);
                        } else {
                            AppUtils.createToastMessage(RegistrationActivity.this, mToast, checkYourInternetTxt);
                        }
                    }
                } else {
                    AppUtils.createToastMessage(RegistrationActivity.this, mToast, chooseRegionTxt);
                }
                break;
            case R.id.et_security_question:
                tilSecurityQuestion.setError("");
                if (mSecurityQuestions.size() > 0) {
                    selectSecurityQuestionDialog();
                } else {
                    if (NetworkHelper.isNetworkAvailable(RegistrationActivity.this)) {
                        createGetSecurityQuestionsRequest(false);
                    } else {
                        AppUtils.createToastMessage(RegistrationActivity.this, mToast, checkYourInternetTxt);
                    }
                }
                break;
            case R.id.btn_register:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(RegistrationActivity.this,
                        Manifest.permission.RECEIVE_SMS)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestReceiveSMSPermission(false);
                    AppLogger.i("REGISTRATION", "Everything is not ok, registerRequest permission ");
                } else {
                    AppLogger.i("REGISTRATION", "Everything is ok, call registration ");

                    if (NetworkHelper.isNetworkAvailable(RegistrationActivity.this)) {
                        checkFieldsForErrors();

                        boolean isUsernameValid = true;
                        if (tilUsername.getError() != null) {
                            isUsernameValid = tilUsername.getError().toString().equals("");
                        }

                        boolean isFirstNameValid = true;
                        if (tilFirstName.getError() != null) {
                            isFirstNameValid = tilFirstName.getError().toString().equals("");
                        }

                        boolean isLastNameValid = true;
                        if (tilLastName.getError() != null) {
                            isLastNameValid = tilLastName.getError().toString().equals("");
                        }

                        boolean isEmailValid = true;
                        if (tilEmail.getError() != null) {
                            isEmailValid = tilEmail.getError().toString().equals("");
                        }

                        boolean isSecurityAnswerValid = true;
                        if (tilSecurityAnswer.getError() != null) {
                            isSecurityAnswerValid = tilSecurityAnswer.getError().toString().equals("");
                        }

                        boolean isPasswodValid = true;
                        if (tilPassword.getError() != null) {
                            isPasswodValid = tilPassword.getError().toString().equals("");
                        }

                        boolean isConfirmPasswordValid = true;
                        if (tilConfirmPassword.getError() != null) {
                            isConfirmPasswordValid = tilConfirmPassword.getError().toString().equals("");
                        }

                        boolean isPhoneNumberValid = true;
                        if (tilPhoneNumber.getError() != null) {
                            isPhoneNumberValid = tilPhoneNumber.getError().toString().equals("");
                        }

                        boolean isStreetNameAndNumberValid = true;
                        if (tilStreetNameAndNumber.getError() != null) {
                            isStreetNameAndNumberValid = tilStreetNameAndNumber.getError().toString().equals("");
                        }

                        boolean isPostCodeValid = true;
                        if (tilPostCode.getError() != null) {
                            isPostCodeValid = tilPostCode.getError().toString().equals("");
                        }

                        if (!isFirstNameValid || !isLastNameValid || !isUsernameValid || !isConfirmPasswordValid
                                || !isEmailValid || !isPasswodValid || !isPhoneNumberValid
                                || !isSecurityAnswerValid || !isStreetNameAndNumberValid || !isPostCodeValid) {
                            AppUtils.createToastMessage(RegistrationActivity.this, mToast, fixAboveErrors);
                        } else {
                            UserMobileRegistrationModel model = fillUserData();
                            Preferences.setIsRegistrationFinished(RegistrationActivity.this, true);
                            createRegisterRequest(model);

                            BetXApplication betXApplication = (BetXApplication) getApplication();
                            betXApplication.setTempUserName(etUsername.getText().toString());
                            betXApplication.setTempPassword(etPassword.getText().toString());
                        }
                    } else {
                        AppUtils.createToastMessage(RegistrationActivity.this, mToast, checkYourInternetTxt);
                    }
                }
                break;
            case R.id.et_email:
                tilEmail.setError("");
                break;
            case R.id.et_phone_number:
                tilPhoneNumber.setError("");
                break;
            case R.id.et_post_code:
                tilPostCode.setError("");
                break;
            case R.id.et_first_name:
                tilFirstName.setError("");
                break;
            case R.id.et_last_name:
                tilLastName.setError("");
                break;
            case R.id.et_security_answer:
                if (!etSecurityQuestion.getText().toString().isEmpty()) {
                    tilSecurityAnswer.setError("");
                } else {
                    AppUtils.createToastMessage(RegistrationActivity.this, mToast, choseSecurityQuestionFirst);
                }
                break;
            case R.id.et_street:
                tilStreetNameAndNumber.setError("");
                break;
        }
    }

    private void checkFieldsForErrors() {
        checkFirstName();
        checkLastName();
        checkUsername();
        checkPassword();
        checkConfirmPassword();
        checkPhoneNumber();
        checkEmail();
        checkStreetName();
        checkPostCode();
        checkStreetName();
    }
    
    /**
     * Method that makes registerRequest for registering user.
     *
     * @param model Model object that contains all previously filled data from registration screen.
     */
    private void createRegisterRequest(UserMobileRegistrationModel model) {
        registerRequest = NetworkHelper.getBetXService(AppConstants.USER_API).registerUser(AppConstants.TICKETS_TERMINAL_ID, model.getZipcode(),
                model.getUserPreferences(), cbTermsAndConditions.isChecked(), cbStoreData.isChecked(),
                model.getFirstName(), model.getLastName(), model.getUsername(), model.getBirthdate(), model.getEmail(), model.getEmailConfirm(),
                model.getMobileNumber(), model.getCityId(), model.getRegionId(), model.getCountryId(), model.getAddress(),
                model.getPassword(), model.getPasswordConfirm(), model.getSecurityQuestionId(), model.getSecurityAnswer());
        registerRequest.enqueue(new Callback<ActionResultBase>() {

            @Override
            public void onResponse(Call<ActionResultBase> call, Response<ActionResultBase> response) {
                cancelProgressDialog();

                ActionResultBase result = response.body();
                if (response.code() == 200 && result != null) {
                    if (result.isSuccess()) {
                        Intent intent = new Intent(RegistrationActivity.this, ConfirmationCodeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        if (result.getValidationResults().size() > 0) {
                            for (BetXValidationResult validation : result.getValidationResults()) {
                                switch (validation.getPropertyName()) {
                                    case "Zipcode":
                                        setErrorTextColor(tilPostCode, mPrimaryColor);
                                        tilPostCode.setError(validation.getErrorMessage());
                                        break;
                                    case "UserPreferences":
                                        AppUtils.createToastMessage(RegistrationActivity.this, mToast, mustSelectUserPreference);
                                        break;
                                    case "AcceptTerms":
                                        AppUtils.createToastMessage(RegistrationActivity.this, mToast, mustAcceptTermsAndUse);
                                        break;
                                    case "RecordingPersonalData":
                                        AppUtils.createToastMessage(RegistrationActivity.this, mToast, mustEnableRecordingPersonalData);
                                        break;
                                    case "FirstName":
                                        setErrorTextColor(tilFirstName, mPrimaryColor);
                                        tilFirstName.setError(validation.getErrorMessage());
                                        break;
                                    case "LastName":
                                        setErrorTextColor(tilLastName, mPrimaryColor);
                                        tilLastName.setError(validation.getErrorMessage());
                                        break;
                                    case "Birthdate":
                                        AppUtils.createToastMessage(RegistrationActivity.this, mToast, birthDateIsNotValid);
                                        break;
                                    case "Email":
                                        setErrorTextColor(tilEmail, mPrimaryColor);
                                        tilEmail.setError(validation.getErrorMessage());
                                        break;
                                    case "Address":
                                        setErrorTextColor(tilStreetNameAndNumber, mPrimaryColor);
                                        tilStreetNameAndNumber.setError(validation.getErrorMessage());
                                        break;
                                    case "Password":
                                        setErrorTextColor(tilPassword, mPrimaryColor);
                                        tilPassword.setError(validation.getErrorMessage());
                                        break;
                                    case "PasswordConfirm":
                                        setErrorTextColor(tilConfirmPassword, mPrimaryColor);
                                        tilConfirmPassword.setError(validation.getErrorMessage());
                                        break;
                                    case "SecurityAnswer":
                                        setErrorTextColor(tilSecurityAnswer, mPrimaryColor);
                                        tilSecurityAnswer.setError(validation.getErrorMessage());
                                        break;
                                    case "CityId":
                                        setErrorTextColor(tilCity, mPrimaryColor);
                                        tilCity.setError(validation.getErrorMessage());
                                        break;
                                    case "RegionId":
                                        setErrorTextColor(tilRegion, mPrimaryColor);
                                        tilRegion.setError(validation.getErrorMessage());
                                        break;
                                    case "CountryId":
                                        setErrorTextColor(tilCountry, mPrimaryColor);
                                        tilCountry.setError(validation.getErrorMessage());
                                        break;
                                    case "SecurityQuestionId":
                                        setErrorTextColor(tilSecurityQuestion, mPrimaryColor);
                                        tilSecurityQuestion.setError(validation.getErrorMessage());
                                        break;
                                }
                            }
                            AppUtils.createToastMessage(RegistrationActivity.this, mToast, fixAboveErrors);
                        }
                    }
                } else {
                    AppUtils.createToastMessage(RegistrationActivity.this, mToast, serverNotAvailableTxt);
                }
            }

            @Override
            public void onFailure(Call<ActionResultBase> call, Throwable t) {
                AppLogger.error(TAG, "Error occurred while trying to make register request ", t);
                if (!RegistrationActivity.this.isFinishing()) {
                    cancelProgressDialog();
                    int message = NetworkHelper.generateErrorMessage(t);
                    AppUtils.createToastMessage(getApplicationContext(), new Toast(getApplicationContext()), getString(message));
                }
            }
        });
        // Show progress dialog
        mProgressDialog = new ProgressDialog(RegistrationActivity.this);
        String message;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_SPRT_TICKET_PLEASE_WAIT)) {
            message = BetXApplication.translationMap.get(TranslationConstants.MBL_SPRT_TICKET_PLEASE_WAIT);
        } else {
            message = getString(R.string.title_activity_registration);
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    /**
     * Method fillUserData is getting all filled data and it creates model object which is send in
     * registerRequest for user registration.
     *
     * @return UserMobileRegistrationModel object that contains all necessary data from user registration.
     */
    private UserMobileRegistrationModel fillUserData() {
        UserMobileRegistrationModel model = new UserMobileRegistrationModel();

        model.setUsername(etUsername.getText().toString());
        model.setEmail(etEmail.getText().toString());
        model.setEmailConfirm(etEmail.getText().toString());
        model.setFirstName(etFirstName.getText().toString());
        model.setLastName(etLastName.getText().toString());

        Region region = mRegionsMap.get(etRegion.getText().toString());
        model.setRegionId(region.getId());

        Result country = mCountriesMap.get(etCountry.getText().toString());
        model.setCountryId(country.getId());

        City city = mCitiesMap.get(etCity.getText().toString());
        model.setCityId(city.getId());

        model.setAcceptTerms(cbTermsAndConditions.isChecked());
        model.setZipcode(etPostCode.getText().toString());
        model.setPassword(etPassword.getText().toString());
        model.setPasswordConfirm(etConfirmPassword.getText().toString());
        model.setAddress(etStreetNameAndNumber.getText().toString());
        model.setMobileNumber(etPhoneNumber.getText().toString());
        model.setRecordingPersonalData(cbStoreData.isChecked());

        Question question = mSecurityQuestions.get(etSecurityQuestion.getText().toString());
        model.setSecurityQuestionId(question.getId());
        model.setSecurityAnswer(etSecurityAnswer.getText().toString());

        List<Integer> preferences = new ArrayList<>();
        if (cbReceiveNews.isChecked()) {
            preferences.add(UserPreferenceType.EmailSendNewsLeter.getValue());
        }
        if (cbOrderClubCard.isChecked()) {
            preferences.add(UserPreferenceType.SendClubCard.getValue());
        }
        model.setUserPreferences(preferences);

        String date = parseTextToDate();
        model.setBirthdate(date);

        return model;
    }

    /**
     * Method parseTextToDate parses text from date text view to date object.
     *
     * @return Date object that represents selected data.
     */
    private String parseTextToDate() {
        String date;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DateAndTimeHelper.REGISTRATION_DATE, Locale.getDefault());
            Date tempDate = sdf.parse(tvDateOfBirth.getText().toString());

            DateFormat targetFormat = new SimpleDateFormat(DateAndTimeHelper.REGISTRATION_DATE_FOR_SERVER, Locale.getDefault());
            date = targetFormat.format(tempDate);
        } catch (ParseException pe) {
            throw new IllegalArgumentException();
        }
        return date;
    }

    /**
     * Dialog for selecting security question from list.
     */
    private void selectSecurityQuestionDialog() {
        SFFontTextView tvTitle = new SFFontTextView(RegistrationActivity.this);

        int padding_edges_in_dp = 16;
        final float scale = getResources().getDisplayMetrics().density;
        int padding_in_px = (int) (padding_edges_in_dp * scale + 0.5f);

        tvTitle.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
        tvTitle.setTypeface(Typeface.DEFAULT_BOLD);
        tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
        tvTitle.setText(choseSecurityQuestionTxt);
        AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
        builder.setCustomTitle(tvTitle);

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RegistrationActivity.this,
                android.R.layout.simple_list_item_1);

        final ArrayList<String> filterList = new ArrayList<>();

        for (Question question : mSecurityQuestions.values()) {
            filterList.add(question.getQuestion());
        }
        arrayAdapter.addAll(filterList);
        builder.setAdapter(arrayAdapter, (dialog, which) -> {
            String selectedQuestion = filterList.get(which);
            etSecurityQuestion.setText(selectedQuestion);
            etSecurityAnswer.setEnabled(true);
            etSecurityAnswer.setFocusable(true);
            etSecurityAnswer.setFocusableInTouchMode(true);
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Dialog for selecting city from citiesRequest list.
     */
    private void selectCityDialog() {
        if (mCitiesMap.size() > 0) {

            SFFontTextView tvTitle = new SFFontTextView(RegistrationActivity.this);

            int padding_edges_in_dp = 16;
            final float scale = getResources().getDisplayMetrics().density;
            int padding_in_px = (int) (padding_edges_in_dp * scale + 0.5f);

            tvTitle.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
            tvTitle.setTypeface(Typeface.DEFAULT_BOLD);
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

            StringBuilder stringBuilder = new StringBuilder();
            if (BetXApplication.translationMap.containsKey(TranslationConstants.CHOOSE_CITY)) {
                stringBuilder.append(BetXApplication.translationMap.get(TranslationConstants.CHOOSE_CITY));
            } else {
                stringBuilder.append(getString(R.string.choose_city));
            }
            stringBuilder.append(":");
            tvTitle.setText(stringBuilder);

            AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
            builder.setCustomTitle(tvTitle);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RegistrationActivity.this,
                    android.R.layout.simple_dropdown_item_1line);

            final ArrayList<String> filterList = new ArrayList<>();

            for (City city : mCitiesMap.values()) {
                filterList.add(city.getName());
            }
            arrayAdapter.addAll(filterList);
            builder.setAdapter(arrayAdapter, (dialog, which) -> {
                String selectedCity = filterList.get(which);
                etCity.setText(selectedCity);
                if (mCitiesMap.get(selectedCity).getCityAndZipCode() != null && !mCitiesMap.get(selectedCity).getCityAndZipCode().equals("")) {
                    String postCode = mCitiesMap.get(selectedCity).getCityAndZipCode().replace(" ", "");
                    etPostCode.setText(postCode);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    /**
     * Dialog for selecting country from countriesRequest list.
     */
    private void selectCountryDialog() {
        if (mCountriesMap.size() > 0) {
            SFFontTextView tvTitle = new SFFontTextView(RegistrationActivity.this);

            int padding_edges_in_dp = 16;
            final float scale = getResources().getDisplayMetrics().density;
            int padding_in_px = (int) (padding_edges_in_dp * scale + 0.5f);

            tvTitle.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
            tvTitle.setTypeface(Typeface.DEFAULT_BOLD);
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

            StringBuilder stringBuilder = new StringBuilder();
            if (BetXApplication.translationMap.containsKey(TranslationConstants.CHOOSE_COUNTRY)) {
                stringBuilder.append(BetXApplication.translationMap.get(TranslationConstants.CHOOSE_COUNTRY));
            } else {
                stringBuilder.append(getString(R.string.choose_country));
            }
            stringBuilder.append(":");
            tvTitle.setText(stringBuilder);

            AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
            builder.setCustomTitle(tvTitle);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RegistrationActivity.this,
                    android.R.layout.simple_dropdown_item_1line);

            final ArrayList<String> filterList = new ArrayList<>();

            for (Result result : mCountriesMap.values()) {
                filterList.add(result.getName());
            }

            arrayAdapter.addAll(filterList);
            builder.setAdapter(arrayAdapter, (dialog, which) -> {
                etRegion.setText("");
                etRegion.setEnabled(true);

                etCity.setText("");

                etPostCode.setText("");
                String selectedCountry = filterList.get(which);
                etCountry.setText(selectedCountry);

                if (mCountriesMap.get(selectedCountry).getCountryCallingCode() != null) {
                    etPhoneNumber.setText(mCountriesMap.get(selectedCountry).getCountryCallingCode());
                }

                if (NetworkHelper.isNetworkAvailable(RegistrationActivity.this)) {
                    creteRegionsRequest(selectedCountry, true);
                } else {
                    AppUtils.createToastMessage(RegistrationActivity.this, mToast, checkYourInternetTxt);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    /**
     * Dialog for selecting region from regionsRequest list.
     */
    private void selectRegionDialog() {
        if (mRegionsMap.size() > 0) {
            SFFontTextView tvTitle = new SFFontTextView(RegistrationActivity.this);

            int padding_edges_in_dp = 16;
            final float scale = getResources().getDisplayMetrics().density;
            int padding_in_px = (int) (padding_edges_in_dp * scale + 0.5f);

            tvTitle.setPadding(padding_in_px, padding_in_px, padding_in_px, padding_in_px);
            tvTitle.setTypeface(Typeface.DEFAULT_BOLD);
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

            StringBuilder stringBuilder = new StringBuilder();
            if (BetXApplication.translationMap.containsKey(TranslationConstants.CHOOSE_REGION)) {
                stringBuilder.append(BetXApplication.translationMap.get(TranslationConstants.CHOOSE_REGION));
            } else {
                stringBuilder.append(getString(R.string.choose_region));
            }
            stringBuilder.append(":");
            tvTitle.setText(stringBuilder);

            AlertDialog.Builder builder = new AlertDialog.Builder(RegistrationActivity.this);
            builder.setCustomTitle(tvTitle);

            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(RegistrationActivity.this,
                    android.R.layout.simple_dropdown_item_1line);

            final ArrayList<String> filterList = new ArrayList<>();

            for (Region region : mRegionsMap.values()) {
                filterList.add(region.getName());
            }
            arrayAdapter.addAll(filterList);
            builder.setAdapter(arrayAdapter, (dialog, which) -> {
                etCity.setText("");
                etPostCode.setText("");
                String selectedRegion = filterList.get(which);
                etRegion.setText(selectedRegion);

                if (NetworkHelper.isNetworkAvailable(RegistrationActivity.this)) {
                    createCityRequest(selectedRegion, true);
                } else {
                    AppUtils.createToastMessage(RegistrationActivity.this, mToast, checkYourInternetTxt);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_RECEIVE_SMS: {
                // If registerRequest is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    AppLogger.i(TAG, "Permission granted " + isInitialCallForRequestPermissions);
                } else {
                    AppUtils.createToastMessage(RegistrationActivity.this, mToast, mustAllowReceiveSmsPremissionsInOrderToCompleteRegistration);
                }
                break;
            }
        }
    }

    private void cancelProgressDialog() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * Method that translates strings depending on chosen language.
     * If string's translation is not available string will get it's value from strings.xml file.
     */
    private void setTranslationForOtherStrignValues() {


        if (BetXApplication.translationMap.containsKey(TranslationConstants.SERVER_NOT_AVAILABLE)) {
            serverNotAvailableTxt = BetXApplication.translationMap.get(TranslationConstants.SERVER_NOT_AVAILABLE);
        } else {
            serverNotAvailableTxt = getResources().getString(R.string.server_not_available);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.PLEASE_CHECK_YOUR_INTERNET_CONNECTION)) {
            checkYourInternetTxt = BetXApplication.translationMap.get(TranslationConstants.PLEASE_CHECK_YOUR_INTERNET_CONNECTION);
        } else {
            checkYourInternetTxt = getResources().getString(R.string.check_internet);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.CHOOSE_COUNTRY)) {
            chooseCountryFirstTxt = BetXApplication.translationMap.get(TranslationConstants.CHOOSE_COUNTRY);
        } else {
            chooseCountryFirstTxt = getResources().getString(R.string.choose_country);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.CHOOSE_REGION)) {
            chooseRegionTxt = BetXApplication.translationMap.get(TranslationConstants.CHOOSE_REGION);
        } else {
            chooseRegionTxt = getResources().getString(R.string.choose_region);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_TOASTER_VALIDATION_FAILED_MESSAGE)) {
            fixAboveErrors = BetXApplication.translationMap.get(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_TOASTER_VALIDATION_FAILED_MESSAGE);
        } else {
            fixAboveErrors = getResources().getString(R.string.inserted_info_not_valid);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.SECURITY_INFORMATION)) {
            choseSecurityQuestionFirst = BetXApplication.translationMap.get(TranslationConstants.SECURITY_INFORMATION);
        } else {
            choseSecurityQuestionFirst = getResources().getString(R.string.change_security_question);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.USER_PREFERENCES)) {
            mustSelectUserPreference = BetXApplication.translationMap.get(TranslationConstants.USER_PREFERENCES);
        } else {
            mustSelectUserPreference = getResources().getString(R.string.user_preferences);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.TERMS_AND_CONDITIONS)) {
            mustAcceptTermsAndUse = BetXApplication.translationMap.get(TranslationConstants.TERMS_AND_CONDITIONS);
        } else {
            mustAcceptTermsAndUse = getResources().getString(R.string.terms_and_conditions);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.PLEASE_ACCEPT_RECORDING_DATA)) {
            mustEnableRecordingPersonalData = BetXApplication.translationMap.get(TranslationConstants.PLEASE_ACCEPT_RECORDING_DATA);
        } else {
            mustEnableRecordingPersonalData = getResources().getString(R.string.please_accept_recording_data);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.VAL_INVALID_DATE)) {
            birthDateIsNotValid = BetXApplication.translationMap.get(TranslationConstants.VAL_INVALID_DATE);
        } else {
            birthDateIsNotValid = getResources().getString(R.string.invalid_birthdate);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.SMS_CHARGE)) {
            mustAllowReceiveSmsPremissionsInOrderToCompleteRegistration = BetXApplication.translationMap.get(TranslationConstants.SMS_CHARGE);
        } else {
            mustAllowReceiveSmsPremissionsInOrderToCompleteRegistration = getResources().getString(R.string.receive_sms_permission_disabled);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.SECURITY_QUESTION)) {
            choseSecurityQuestionTxt = BetXApplication.translationMap.get(TranslationConstants.SECURITY_QUESTION);
        } else {
            choseSecurityQuestionTxt = getResources().getString(R.string.change_security_question);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (countriesRequest != null) {
            countriesRequest.cancel();
        }
        if (citiesRequest != null) {
            citiesRequest.cancel();
        }
        if (regionsRequest != null) {
            regionsRequest.cancel();
        }
        if (securityQuestionRequest != null) {
            securityQuestionRequest.cancel();
        }
        if (registerRequest != null) {
            registerRequest.cancel();
        }
        if (userFieldAvailabilityRequest != null) {
            userFieldAvailabilityRequest.cancel();
        }
    }
}
