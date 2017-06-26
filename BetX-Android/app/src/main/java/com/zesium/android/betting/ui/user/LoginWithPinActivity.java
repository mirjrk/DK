package com.zesium.android.betting.ui.user;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zesium.android.betting.R;
import com.zesium.android.betting.model.UserStatus;
import com.zesium.android.betting.BetXApplication;
import com.zesium.android.betting.utils.Preferences;
import com.zesium.android.betting.model.util.TranslationConstants;
import com.zesium.android.betting.utils.AppConstants;
import com.zesium.android.betting.utils.AppUtils;
import com.zesium.android.betting.ui.main.MainActivity;
import com.zesium.android.betting.ui.widgets.CustomTextWatcher;
import com.zesium.android.betting.ui.widgets.SFFontEditText;
import com.zesium.android.betting.ui.widgets.SFFontTextView;

import java.util.ArrayList;

public class LoginWithPinActivity extends AppCompatActivity implements View.OnKeyListener, View.OnClickListener {

    private SFFontEditText mPinFirstDigitEditText;
    private SFFontEditText mPinSecondDigitEditText;
    private SFFontEditText mPinThirdDigitEditText;
    private SFFontEditText mPinForthDigitEditText;
    private Button mPinSubmitButton;
    private SFFontTextView mTVLoginAsAnotherUser;
    private SFFontTextView mTVUserId;
    private SFFontTextView userIdTextView;
    private Button btnEditTextClickBlockButton;
    private boolean isDigit1;
    private boolean isDigit2;
    private boolean isDigit3;
    private boolean isDigit4;
    private String username;
    private String userToken;
    private String password;
    private String refreshToken;
    private String terminalId;
    private String firstName;
    private String lastName;
    private String email;
    private ArrayList<String> mPinNumberArray;
    private Toast mToast;
    private boolean isFirstTimeEnterPin;
    private boolean isPersonalInfoScreenNext;
    private boolean shouldReturnToTickets;
    private int deselectedBoxBackground;
    private boolean isShouldReturnToTickets;
    private SFFontTextView mTVEnterPinCode;
    private String enterPinCodeTxt;
    private String loginAsAnotherUserTxt;
    private String usernameTxt;
    private String createPinCodeTxt;
    private String expiresIn;
    private int viewToDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setTranslationsForStringValues();

        //Setting status bar color, can't change the Status Bar Color on pre-Lollipop devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.primary_dark_red));
        }

        //setting theme for this activity
        setThemeForThisActivity();

        setContentView(R.layout.activity_login_with_pin);

        if (getIntent() != null) {
            isShouldReturnToTickets = getIntent().getBooleanExtra(AppConstants.RETURN_TO_CREATE_TICKET, false);
        }

        //initializing layout
        initializeLayout();


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                getDataFromExtras(extras);
            }
        }

        //initializing and setting tool bar
        initializeAndSetToolBar();

        //Setting empty array list
        initializeAndSetEmptyPinArrayList();

        if (isFirstTimeEnterPin) {

            String submit;
            if (BetXApplication.translationMap.containsKey(TranslationConstants.SUBMIT)) {
                submit = BetXApplication.translationMap.get(TranslationConstants.SUBMIT);
            } else {
                submit = getResources().getString(R.string.submit);
            }

            mTVUserId.setText(username);

            Typeface typeface = Typeface.createFromAsset(LoginWithPinActivity.this.getAssets(), AppConstants.DEFAULT_FONT);
            if (mPinSubmitButton != null) {
                mPinSubmitButton.setText(submit);
                mPinSubmitButton.setTypeface(typeface);
            }
        } else {
            if (mTVLoginAsAnotherUser != null) {
                mTVLoginAsAnotherUser.setVisibility(View.VISIBLE);
            }
            if (mPinSubmitButton != null) {
                mPinSubmitButton.setVisibility(View.GONE);
            }
            mTVUserId.setVisibility(View.VISIBLE);
            if (userIdTextView != null) {
                userIdTextView.setVisibility(View.VISIBLE);
                mTVUserId.setText(Preferences.getUserName(getApplicationContext()));
            }

            //Replacing all entered characters to asterisk
            replaceCharactersToAsterisk();
        }

        setLayoutListeners();

        if (Preferences.getTheme(this)) {
            deselectedBoxBackground = ContextCompat.getColor(this, R.color.box_pin_dark);
        } else {
            deselectedBoxBackground = ContextCompat.getColor(this, R.color.box_pin_light);
        }
    }

    /**
     * Method that sets theme for this activity.
     */
    private void setThemeForThisActivity() {
        setTheme(R.style.MyMaterialThemeLight);
        boolean darkTheme = Preferences.getTheme(this);
        if (darkTheme) {
            setTheme(R.style.ContactUsDark);
        } else {
            setTheme(R.style.MyMaterialThemeLight);
        }
    }

    /**
     * Method that replace entered number to asterisk.
     */
    private void replaceCharactersToAsterisk() {
        mPinFirstDigitEditText.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        mPinSecondDigitEditText.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        mPinThirdDigitEditText.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        mPinForthDigitEditText.setTransformationMethod(new AsteriskPasswordTransformationMethod());
    }

    /**
     * Method that initialize toolbar and set title.
     */
    private void initializeAndSetToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            // Set back arrow to toolbar
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setDisplayShowHomeEnabled(true);

            if (!isFirstTimeEnterPin || isPersonalInfoScreenNext) {
                actionbar.setTitle(enterPinCodeTxt);
            } else {
                actionbar.setTitle(createPinCodeTxt);
            }
        }
    }

    /**
     * Method that initialize layout.
     */
    private void initializeLayout() {
        mTVEnterPinCode = (SFFontTextView) findViewById(R.id.enter_pin_code_text_view);
        mTVEnterPinCode.setText(enterPinCodeTxt);
        mPinFirstDigitEditText = (SFFontEditText) findViewById(R.id.et_pin_first);
        mPinFirstDigitEditText.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_dark_red));
        mPinFirstDigitEditText.requestFocus();
        mPinSecondDigitEditText = (SFFontEditText) findViewById(R.id.et_pin_second);
        mPinThirdDigitEditText = (SFFontEditText) findViewById(R.id.et_pin_third);
        mPinForthDigitEditText = (SFFontEditText) findViewById(R.id.et_pin_forth);

        userIdTextView = (SFFontTextView) findViewById(R.id.tv_user_id);
        userIdTextView.setText(usernameTxt);
        mTVUserId = (SFFontTextView) findViewById(R.id.tv_user_id_value);
        mPinSubmitButton = (Button) findViewById(R.id.btn_submit_pin);
        btnEditTextClickBlockButton = (Button) findViewById(R.id.btn_edit_text_block_button);

        mTVLoginAsAnotherUser = (SFFontTextView) findViewById(R.id.tv_login_as_another_user);
        mTVLoginAsAnotherUser.setText(loginAsAnotherUserTxt);

        if (mTVLoginAsAnotherUser != null) {
            mTVLoginAsAnotherUser.setOnClickListener(this);
        }
    }

    /**
     * Method that gets data from Intent extras.
     *
     * @param extras Bundle containing data.
     */
    private void getDataFromExtras(Bundle extras) {
        userToken = extras.getString(AppConstants.LOGIN_USER_TOKEN);
        username = extras.getString(AppConstants.LOGIN_USER_NAME);
        password = extras.getString(AppConstants.LOGIN_PASSWORD);
        refreshToken = extras.getString(AppConstants.LOGIN_REFRESH_TOKEN);
        terminalId = extras.getString(AppConstants.LOGIN_TERMINAL_ID);
        firstName = extras.getString(AppConstants.LOGIN_FIRST_NAME);
        lastName = extras.getString(AppConstants.LOGIN_LAST_NAME);
        email = extras.getString(AppConstants.LOGIN_EMAIL);
        expiresIn = extras.getString(AppConstants.LOGIN_EXPIRES_IN);
        isFirstTimeEnterPin = extras.getBoolean(AppConstants.IS_FIRST_TIME_LOGIN_WITH_PIN, false);
        isPersonalInfoScreenNext = extras.getBoolean(AppConstants.IS_PERSONAL_INFO_SCREEN, false);
        shouldReturnToTickets = extras.getBoolean(AppConstants.SHOULD_RETURN_TO_TICKETS, false);
        viewToDisplay = extras.getInt(AppConstants.VIEW_TO_DISPLAY, 0);
    }

    /**
     * Method that initialize array list and sets all members.
     */
    private void initializeAndSetEmptyPinArrayList() {
        mPinNumberArray = new ArrayList<>(4);
        mPinNumberArray.add(0, "");
        mPinNumberArray.add(1, "");
        mPinNumberArray.add(2, "");
        mPinNumberArray.add(3, "");
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (v.getId()) {
                // Going back to previous field after deleting entered character
                case R.id.et_pin_forth:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {

                        if (((ColorDrawable) mPinForthDigitEditText.getBackground()).getColor()
                                != ContextCompat.getColor(LoginWithPinActivity.this, R.color.primary_dark_red)) {

                            // If mPinForthDigitEditText is not focused then it will gain focus.
                            // This will happen if all pin fields are already filled and user clicks on Delete button on keyboard.
                            setFocusAndBackgroundForPinBox(mPinThirdDigitEditText, mPinForthDigitEditText);
                            mPinNumberArray.set(3, "");
                        } else {

                            // If mPinForthDigitEditText is already focused then focus will be moved to the previous field (mPinThirdDigitEditText)
                            // This will happen when user deletes forth filed.
                            goToPreviousFieldAndHighlightIt(mPinForthDigitEditText, mPinThirdDigitEditText, mPinNumberArray, 3);
                            isDigit3 = false;
                            isDigit4 = false;
                        }
                    }
                    break;
                case R.id.et_pin_third:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        goToPreviousFieldAndHighlightIt(mPinThirdDigitEditText, mPinSecondDigitEditText, mPinNumberArray, 2);
                        isDigit2 = false;
                        isDigit3 = false;
                    }
                    break;
                case R.id.et_pin_second:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        goToPreviousFieldAndHighlightIt(mPinSecondDigitEditText, mPinFirstDigitEditText, mPinNumberArray, 1);
                        isDigit1 = false;
                        isDigit2 = false;
                    }
                    break;
                case R.id.et_pin_first:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        goToPreviousFieldAndHighlightIt(mPinFirstDigitEditText, null, mPinNumberArray, 0);
                        isDigit1 = false;
                    }
                    break;
            }
        }
        return false;
    }

    /**
     * Method that deletes all edit text fields and set all isDigit to false.
     *
     * @param mPinForthDigitEditText  Last(fourth) pin box (edit text) field,
     * @param mPinThirdDigitEditText  third pin box,
     * @param mPinSecondDigitEditText second pin box,
     * @param mPinFirstDigitEditText  first pin box,
     * @param mPinNumberArray         array that holds all entered numbers for pin.
     */
    private void deleteAllEditTextFieldsAndHighlightFirstField(SFFontEditText mPinForthDigitEditText, SFFontEditText mPinThirdDigitEditText,
                                                               SFFontEditText mPinSecondDigitEditText, SFFontEditText mPinFirstDigitEditText,
                                                               ArrayList<String> mPinNumberArray) {
        mPinForthDigitEditText.getText().clear();
        mPinThirdDigitEditText.getText().clear();
        mPinSecondDigitEditText.getText().clear();
        mPinFirstDigitEditText.getText().clear();

        mPinNumberArray.set(3, mPinForthDigitEditText.getText().toString());
        mPinNumberArray.set(2, mPinThirdDigitEditText.getText().toString());
        mPinNumberArray.set(1, mPinSecondDigitEditText.getText().toString());
        mPinNumberArray.set(0, mPinFirstDigitEditText.getText().toString());

        mPinFirstDigitEditText.requestFocus();
        mPinFirstDigitEditText.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_dark_red));
        mPinForthDigitEditText.setBackgroundColor(deselectedBoxBackground);

        isDigit1 = false;
        isDigit2 = false;
        isDigit3 = false;
        isDigit4 = false;
    }

    /**
     * Method that check where should user go after entering pin code.
     *
     * @param pinNumberArray entered pin number saved as a array list.
     */
    private void checkPin(ArrayList<String> pinNumberArray) {
        if (pinNumberArray.toString().equals(Preferences.getPinNumber(this))) {

            hideSoftKeyboard(mPinForthDigitEditText);

            BetXApplication.sUserStatus = UserStatus.LOGGED_WITH_PIN;
            if (isPersonalInfoScreenNext) {
                Intent personalInfoIntent = new Intent(LoginWithPinActivity.this, PersonalInfoActivity.class);
                startActivity(personalInfoIntent);
                finish();
            } else if (viewToDisplay > 0) {
                Intent i = new Intent();
                i.putExtra(AppConstants.VIEW_TO_DISPLAY, viewToDisplay);
                setResult(AppConstants.DISPLAY_VIEW, i);
                finish();
            } else {
                Intent i = new Intent();
                setResult(RESULT_OK, i);
                finish();
            }
        } else {
            if (BetXApplication.loginWithPinAttemptCounter == 5) {
                hideSoftKeyboard(mPinForthDigitEditText);
                openDialogWithLogoutInformation();
            } else {
                createToastMessage(getResources().getString(R.string.incorrect_pin_message));
                deleteAllEditTextFieldsAndHighlightFirstField(mPinForthDigitEditText, mPinThirdDigitEditText,
                        mPinSecondDigitEditText, mPinFirstDigitEditText, mPinNumberArray);
            }
        }
    }

    /**
     * Shows soft keyboard.
     *
     * @param editText EditText which has focus
     */
    private void showSoftKeyboard(SFFontEditText editText) {
        if (editText == null) {
            return;
        }
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(editText.getId(), InputMethodManager.SHOW_FORCED);
    }

    /**
     * Hides soft keyboard.
     *
     * @param editText EditText which has focus
     */
    private void hideSoftKeyboard(EditText editText) {
        if (editText == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * Sets listeners for EditText fields and disabling EditText cursor.
     */
    private void setLayoutListeners() {
        mPinFirstDigitEditText.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) {
                    isDigit1 = TextUtils.isDigitsOnly(mPinFirstDigitEditText.getText());
                    //Switching focus to next field

                    setFocusAndBackgroundForPinBox(mPinFirstDigitEditText, mPinSecondDigitEditText);
                    //Adding number to array list for saving pin to shared preferences
                    mPinNumberArray.set(0, mPinFirstDigitEditText.getText().toString());
                }
            }
        });
        mPinSecondDigitEditText.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) {
                    isDigit2 = TextUtils.isDigitsOnly(mPinSecondDigitEditText.getText());
                    //Switching focus to next field
                    if (mPinFirstDigitEditText.getText().toString().length() == 1) {
                        setFocusAndBackgroundForPinBox(mPinSecondDigitEditText, mPinThirdDigitEditText);
                        mPinNumberArray.set(1, mPinSecondDigitEditText.getText().toString());
                    } else {
                        mPinSecondDigitEditText.getText().clear();
                        setFocusAndHighlightFirstBox();
                    }
                }
            }
        });

        mPinThirdDigitEditText.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) {
                    isDigit3 = TextUtils.isDigitsOnly(mPinThirdDigitEditText.getText());
                    //Switching focus to next field
                    if (mPinSecondDigitEditText.getText().toString().length() == 1) {
                        setFocusAndBackgroundForPinBox(mPinThirdDigitEditText, mPinForthDigitEditText);
                        mPinNumberArray.set(2, mPinThirdDigitEditText.getText().toString());
                    } else {
                        mPinThirdDigitEditText.getText().clear();
                        setFocusAndHighlightFirstBox();
                    }
                }
            }
        });
        mPinForthDigitEditText.addTextChangedListener(new CustomTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) {
                    isDigit4 = TextUtils.isDigitsOnly(mPinForthDigitEditText.getText());
                    //Switching focus to next field
                    if (mPinThirdDigitEditText.getText().toString().length() == 1) {
                        mPinNumberArray.set(3, mPinForthDigitEditText.getText().toString());
                        mPinForthDigitEditText.setBackgroundColor(deselectedBoxBackground);
                    } else {
                        mPinForthDigitEditText.getText().clear();
                        setFocusAndHighlightFirstBox();
                    }

                    if (!isFirstTimeEnterPin && isDigit1 && isDigit2 && isDigit3 && isDigit4) {
                        BetXApplication.loginWithPinAttemptCounter++;
                        checkPin(mPinNumberArray);
                    }
                }
            }
        });

        mPinFirstDigitEditText.setOnKeyListener(this);
        mPinSecondDigitEditText.setOnKeyListener(this);
        mPinThirdDigitEditText.setOnKeyListener(this);
        mPinForthDigitEditText.setOnKeyListener(this);

        btnEditTextClickBlockButton.setOnClickListener(this);
        mPinSubmitButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_submit_pin:
                if (isDigit1 && isDigit2 && isDigit3 && isDigit4 && mPinNumberArray.size() == 4) {
                    saveUserData(userToken, username, password, refreshToken, terminalId, firstName, lastName, email, expiresIn);
                    Preferences.setPinNumber(LoginWithPinActivity.this, String.valueOf(mPinNumberArray));
                    Preferences.setIsUserLoggedWithPin(LoginWithPinActivity.this, true);
                    BetXApplication.sUserStatus = UserStatus.LOGGED_WITH_PIN;
                    if (!isShouldReturnToTickets) {
                        Intent intent = new Intent(LoginWithPinActivity.this, MainActivity.class);
                        if (shouldReturnToTickets) {
                            intent.putExtra(AppConstants.SHOULD_RETURN_TO_TICKETS, true);
                        }
                        startActivity(intent);
                        finish();
                    } else {
                        finish();
                    }
                } else {
                    createToastMessage(getResources().getString(R.string.incorrect_pin_message));
                }
                break;
            case R.id.tv_login_as_another_user:
                creteSwitchUserDialog();
                break;
            case R.id.btn_edit_text_block_button:
                if (!isDigit1) {
                    showSoftKeyboard(mPinFirstDigitEditText);
                } else if (!isDigit2) {
                    showSoftKeyboard(mPinSecondDigitEditText);
                } else if (!isDigit3) {
                    showSoftKeyboard(mPinThirdDigitEditText);
                } else if (!isDigit4) {
                    showSoftKeyboard(mPinForthDigitEditText);
                }
                break;
        }
    }

    /**
     * Method that show window dialog for switching user
     */
    private void creteSwitchUserDialog() {
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

        String logoutMsg;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.LOGOUT_DIALOG_MSG)) {
            logoutMsg = BetXApplication.translationMap.get(TranslationConstants.LOGOUT_DIALOG_MSG);
        } else {
            logoutMsg = getResources().getString(R.string.logout_dialog_message);
        }

        SFFontTextView tvMessage = (SFFontTextView) layout.findViewById(R.id.tv_dialog_message);
        tvMessage.setText(logoutMsg);

        AlertDialog.Builder builder;
        if (Preferences.getTheme(LoginWithPinActivity.this)) {
            builder = new AlertDialog.Builder(this, R.style.DarkBackgroundFilterDialog).setView(layout);
        } else {
            builder = new AlertDialog.Builder(this, R.style.WhiteBackgroundFilterDialog).setView(layout);
        }
        String cancel;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.CANCEL)) {
            cancel = BetXApplication.translationMap.get(TranslationConstants.CANCEL);
        } else {
            cancel = getString(R.string.dialog_cancel);
        }
        final AlertDialog alertDialog = builder.setPositiveButton(logOut,
                (dialog, which) -> {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.putExtra(AppConstants.RETURN_TO_CREATE_TICKET, true);
                    i.putExtra(AppConstants.IS_LOGGED_OUT, true);
                    //setResult(99, i);
                    startActivity(i);
                    finish();

                }).setNegativeButton(cancel, (dialog, which) -> {
        }).create();
        alertDialog.show();
        alertDialog.setCancelable(true);
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(
                    () -> AppUtils.adjustDialog(getApplicationContext(), alertDialog));
        }
    }

    /**
     * Saving user data to shared preferences
     *
     * @param username     String containing username
     * @param password     String containing password
     * @param refreshToken String containing refreshToken
     * @param terminalId   String containing terminalId
     * @param firstName    String containing firstName
     * @param lastName     String containing lastName
     * @param email        String containing email
     * @param expiresIn    String containing time until expiration of token in seconds.
     */
    private void saveUserData(String userToken, String username, String password, String refreshToken, String terminalId,
                              String firstName, String lastName, String email, String expiresIn) {
        Preferences.setUserToken(this, userToken);
        Preferences.setUserName(this, username);
        Preferences.setPassword(this, password);
        Preferences.setRefeshToken(this, refreshToken);
        Preferences.setTerminalId(this, terminalId);
        Preferences.setIsUserLogged(this, true);

        long timeInSeconds = System.currentTimeMillis() / 1000;
        long timeWithExpiration = timeInSeconds + Long.valueOf(expiresIn);
        Preferences.setTokenExpiresIn(this, timeWithExpiration);

        Intent returnIntent = new Intent();
        returnIntent.putExtra(AppConstants.USER_TOKEN, userToken);

        Preferences.setFirstName(this, firstName);
        Preferences.setLastName(this, lastName);
        Preferences.setEmail(this, email);
    }

    /**
     * Replacing entered character to asterisk.
     */
    private class AsteriskPasswordTransformationMethod extends PasswordTransformationMethod {
        @Override
        public CharSequence getTransformation(CharSequence source, View view) {
            return new PasswordCharSequence(source);
        }

        private class PasswordCharSequence implements CharSequence {
            private final CharSequence mSource;

            private PasswordCharSequence(CharSequence source) {
                // Store char sequence
                mSource = source;
            }

            public char charAt(int index) {
                // This is the important part
                return '*';
            }

            public int length() {
                // Return default
                return mSource.length();
            }

            public CharSequence subSequence(int start, int end) {
                // Return default
                return mSource.subSequence(start, end);
            }
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
     * Method createToastMessage is canceling previously created Toast message, if exists and
     * creates new one.
     *
     * @param text String value that represents text that will appear in Toast message.
     */
    private void createToastMessage(String text) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(LoginWithPinActivity.this, text, Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * Method that delete entered character , move cursor to previous field and highlight it.
     *
     * @param digitEditText  EditText field that is going to be deleted,
     * @param previousField  previous EditText field, that is going to be highlight,
     * @param pinNumberArray Array of entered numbers,
     * @param position       position in the array list.
     */
    private void goToPreviousFieldAndHighlightIt(SFFontEditText digitEditText, SFFontEditText previousField,
                                                 ArrayList<String> pinNumberArray, int position) {
        digitEditText.getText().clear();

        if (previousField != null) {
            setFocusAndBackgroundForPinBox(digitEditText, previousField);
            previousField.getText().clear();
        }

        pinNumberArray.set(position, digitEditText.getText().toString());
    }

    /**
     * Method that set focus on previous pin box, highlight it and change color of the other pin pox.
     *
     * @param digitEditText Edit Text(pin box) that was focused before previousField,
     * @param previousField Edit Text (pin box) that is getting focus and highlight.
     */
    private void setFocusAndBackgroundForPinBox(SFFontEditText digitEditText, SFFontEditText previousField) {
        previousField.requestFocus();
        previousField.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_dark_red));
        digitEditText.setBackgroundColor(deselectedBoxBackground);  //setting background color of the previous field; depends from theme
    }

    /**
     * Method that set focus to first pin box, highlight first box and set colors to others pin boxes.
     */
    private void setFocusAndHighlightFirstBox() {
        mPinFirstDigitEditText.requestFocus();
        mPinFirstDigitEditText.setBackgroundColor(ContextCompat.getColor(this, R.color.primary_dark_red));
        mPinSecondDigitEditText.setBackgroundColor(deselectedBoxBackground);
        mPinThirdDigitEditText.setBackgroundColor(deselectedBoxBackground);
        mPinForthDigitEditText.setBackgroundColor(deselectedBoxBackground);
    }

    /**
     * Method that create dialog window with message that user entered 5 times wrong pin number.
     */
    private void openDialogWithLogoutInformation() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setCancelable(false).setMessage(getResources().getString(R.string.pin_login_info_warning_dialog));
        alertDialogBuilder.setPositiveButton(getResources().getString(R.string.dialog_ok), (arg0, arg1) -> {
            arg0.dismiss();
            Intent intentOpenLoginFragment = new Intent(LoginWithPinActivity.this, MainActivity.class);
            intentOpenLoginFragment.putExtra(AppConstants.IS_LOGGED_OUT, true);
            startActivity(intentOpenLoginFragment);
            finish();
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    /**
     * Method that translates strings depending on chosen language.
     * If string's translation is not available string will get it's value from strings.xml file.
     */
    private void setTranslationsForStringValues() {
        if (BetXApplication.translationMap.containsKey(TranslationConstants.ENTER_PIN_CODE)) {
            enterPinCodeTxt = BetXApplication.translationMap.get(TranslationConstants.ENTER_PIN_CODE);
        } else {
            enterPinCodeTxt = getResources().getString(R.string.enter_pin);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.LOGIN_AS_ANOTHER_USER)) {
            loginAsAnotherUserTxt = BetXApplication.translationMap.get(TranslationConstants.LOGIN_AS_ANOTHER_USER);
        } else {
            loginAsAnotherUserTxt = getResources().getString(R.string.login_as_another_user);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.L_USERNAME)) {
            usernameTxt = BetXApplication.translationMap.get(TranslationConstants.L_USERNAME);
        } else {
            usernameTxt = getResources().getString(R.string.username_txt);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.NEW_SECRET_PIN)) {
            createPinCodeTxt = BetXApplication.translationMap.get(TranslationConstants.NEW_SECRET_PIN);
        } else {
            createPinCodeTxt = getResources().getString(R.string.title_activity_create_pin);
        }
    }
}