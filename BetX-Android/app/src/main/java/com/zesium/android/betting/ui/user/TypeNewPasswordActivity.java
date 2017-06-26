package com.zesium.android.betting.ui.user;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zesium.android.betting.R;
import com.zesium.android.betting.model.user.ActionResultBase;
import com.zesium.android.betting.utils.AppConstants;
import com.zesium.android.betting.utils.AppLogger;
import com.zesium.android.betting.BetXApplication;
import com.zesium.android.betting.service.NetworkHelper;
import com.zesium.android.betting.utils.Preferences;
import com.zesium.android.betting.ui.widgets.SFFontEditText;
import com.zesium.android.betting.model.util.TranslationConstants;
import com.zesium.android.betting.utils.AppUtils;
import com.zesium.android.betting.ui.main.MainActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TypeNewPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = TypeNewPasswordActivity.class.getSimpleName();
    private Button btnChange;
    private TextInputLayout tilNewPassword;
    private TextInputLayout tilConfirmPassword;

    private SFFontEditText etNewPassword;
    private SFFontEditText etConfirmPassword;

    private Toast mToast;
    private String smsCode;

    private RelativeLayout rlProgressLayout;
    private LinearLayout llHolderLayout;
    private Call<ActionResultBase> sendNewPasswordRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        //Setting status bar color, can't change the Status Bar Color on pre-Lollipop devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.primary_dark_red));
        }

        setTheme(R.style.MyMaterialThemeLight);

        //Checking what theme is set
        boolean darkTheme = Preferences.getTheme(this);
        if (darkTheme) {
            setTheme(R.style.ConfirmationScreenDark);
        } else {
            setTheme(R.style.MyMaterialThemeLight);
        }
        setContentView(R.layout.activity_type_new_password);

        // Set toolbar for tickets
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_activity_type_new_password);
        }

        Intent intent = getIntent();
        if (intent != null) {
            smsCode = intent.getStringExtra(AppConstants.CODE);
        }


        initialiseFields();
        setListeners();
        fillFieldsWithData();
    }

    @Override
    protected void onResume() {
        super.onResume();

        rlProgressLayout.setVisibility(View.GONE);
        llHolderLayout.setVisibility(View.VISIBLE);
    }

    private void initialiseFields() {
        rlProgressLayout = (RelativeLayout) findViewById(R.id.rl_progress_layout);
        ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(this, R.color.primary_red),
                android.graphics.PorterDuff.Mode.SRC_IN);
        llHolderLayout = (LinearLayout) findViewById(R.id.ll_parent_layout);

        btnChange = (Button) findViewById(R.id.btn_change);

        etNewPassword = (SFFontEditText) findViewById(R.id.et_new_password);
        etConfirmPassword = (SFFontEditText) findViewById(R.id.et_confirm_new_password);

        tilNewPassword = (TextInputLayout) findViewById(R.id.til_new_password);
        tilConfirmPassword = (TextInputLayout) findViewById(R.id.til_confirm_new_password);

        etNewPassword.setOnFocusChangeListener(mNewPasswordFocusChangeListener);
        etConfirmPassword.setOnFocusChangeListener(mConfirmPasswordFocusChangeListener);
    }

    private void setListeners() {
        etNewPassword.addTextChangedListener(mTextWatcher);
        etConfirmPassword.addTextChangedListener(mTextWatcher);
        btnChange.setOnClickListener(this);
    }

    private void fillFieldsWithData() {
        String newPassword;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.NEW_PASSWORD)) {
            newPassword = BetXApplication.translationMap.get(TranslationConstants.NEW_PASSWORD);
        } else {
            newPassword = getString(R.string.security_answer);
        }
        tilNewPassword.setHint(newPassword);

        String retypePassword;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.RETYPE_PASSWORD)) {
            retypePassword = BetXApplication.translationMap.get(TranslationConstants.RETYPE_PASSWORD);
        } else {
            retypePassword = getString(R.string.confirm_new_password);
        }
        tilConfirmPassword.setHint(retypePassword);

        String change;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.CHANGE)) {
            change = BetXApplication.translationMap.get(TranslationConstants.CHANGE);
        } else {
            change = getString(R.string.change);
        }
        btnChange.setText(change);
    }


    /**
     * Focus listener to check if password is entered in valid format. Valid password should contain
     * 6-15 characters including one number and one capital letter
     */
    private final View.OnFocusChangeListener mNewPasswordFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (!b) {
                checkNewPassword();
            } else {
                tilNewPassword.setError("");
            }
        }
    };

    /**
     * Method that checks if entered password is valid.
     */
    private void checkNewPassword() {
        String password = etNewPassword.getText().toString();
        boolean isValid = password.matches("^.*(?=.{6,})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).*$");
        if (!isValid) {
            String passwordError;
            if (BetXApplication.translationMap.containsKey(TranslationConstants.VAL_PASSWORD)) {
                passwordError = BetXApplication.translationMap.get(TranslationConstants.VAL_PASSWORD);
            } else {
                passwordError = getString(R.string.error_password);
            }
            tilNewPassword.setError(passwordError);
        } else {
            tilNewPassword.setError("");
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
                tilConfirmPassword.setError("");
            }
        }
    };

    /**
     * Method that checks if retyped password is valid.
     */
    private void checkConfirmPassword() {
        if (!etNewPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
            String errorPassword;
            if (BetXApplication.translationMap.containsKey(TranslationConstants.VAL_RETYPE_PASSWORD)) {
                errorPassword = BetXApplication.translationMap.get(TranslationConstants.VAL_RETYPE_PASSWORD);
            } else {
                errorPassword = getString(R.string.error_confirm_password);
            }
            tilConfirmPassword.setError(errorPassword);
        } else {
            tilConfirmPassword.setError("");
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


    /**
     * Method checkFieldsForEmptyValues enables/disables done button depending on whether the required fields are filled.
     */
    private void checkFieldsForEmptyValues() {
        String s1 = etNewPassword.getText().toString();
        String s3 = etConfirmPassword.getText().toString();

        if (s1.equals("") && s3.equals("")) {
            btnChange.setEnabled(false);
        } else {
            btnChange.setEnabled(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_change:
                if (NetworkHelper.isNetworkAvailable(this)) {
                    verifyFieldsAndCreateRequest();
                } else {
                    AppUtils.createToastMessage(this, mToast, getResources().getString(R.string.check_internet));
                }
                break;
        }
    }

    /**
     * Method that check if both new password and retyped password are valid. If they are not valid error messages will be shown. If they are valid service is started.
     */
    private void verifyFieldsAndCreateRequest() {
        checkNewPassword();
        checkConfirmPassword();

        boolean isNewPasswordValid = true;
        if (tilNewPassword.getError() != null) {
            isNewPasswordValid = tilNewPassword.getError().toString().equals("");
        }

        boolean isConfirmPasswordValid = true;
        if (tilConfirmPassword.getError() != null) {
            isConfirmPasswordValid = tilConfirmPassword.getError().toString().equals("");
        }

        if (!isNewPasswordValid || !isConfirmPasswordValid) {
            rlProgressLayout.setVisibility(View.GONE);
            llHolderLayout.setVisibility(View.VISIBLE);
            AppUtils.createToastMessage(this, mToast, "Fix above errors");
        } else {
            AppUtils.hideKeyboard(this);
            llHolderLayout.setVisibility(View.GONE);
            rlProgressLayout.setVisibility(View.VISIBLE);
            createUpdatePasswordRequest();
        }
    }

    /**
     * Service that is started if new password is valid.
     */
    private void createUpdatePasswordRequest() {
        sendNewPasswordRequest = NetworkHelper.getBetXService(AppConstants.USER_API).sendNewPassword(Preferences.getTerminalId(TypeNewPasswordActivity.this),
                etNewPassword.getText().toString(), etConfirmPassword.getText().toString(), smsCode);
        sendNewPasswordRequest.enqueue(new Callback<ActionResultBase>() {

            @Override
            public void onResponse(Call<ActionResultBase> call, Response<ActionResultBase> response) {
                if (response.code() == 401) {
                    NetworkHelper.showUnauthorizedDialog(TypeNewPasswordActivity.this);
                } else {
                    ActionResultBase responseFromServer = response.body();
                    if (responseFromServer != null) {
                        if (responseFromServer.isActionResult()) {
                            if (responseFromServer.getMessages() != null && responseFromServer.getMessages().size() > 0) {
                                setToastMessage(responseFromServer, etNewPassword, etConfirmPassword);

                                btnChange.setEnabled(true);

                                Intent intent = new Intent(TypeNewPasswordActivity.this, MainActivity.class);
                                intent.putExtra(AppConstants.LOGIN_FRAGMENT, AppConstants.LOGIN_FRAGMENT);
                                startActivity(intent);
                                rlProgressLayout.setVisibility(View.GONE);
                            }
                        } else {
                            if (responseFromServer.getMessages() != null && responseFromServer.getMessages().size() > 0) {
                                setToastMessage(responseFromServer, etNewPassword, etConfirmPassword);
                                rlProgressLayout.setVisibility(View.GONE);
                                llHolderLayout.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ActionResultBase> call, Throwable t) {
                AppLogger.error(TAG, "Exception occurred with message: ", t);
                if (!TypeNewPasswordActivity.this.isFinishing()) {
                    rlProgressLayout.setVisibility(View.GONE);
                    llHolderLayout.setVisibility(View.VISIBLE);
                    int message = NetworkHelper.generateErrorMessage(t);
                    AppUtils.createToastMessage(getApplicationContext(), new Toast(getApplicationContext()), getString(message));
                }
            }
        });
    }


    /**
     * Method that create toast from message received from server.
     *
     * @param responseFromServer holding response from server.
     * @param etNewPassword      text field for entering new password.
     * @param etConfirmPassword  text field for retyping new password.
     */
    private void setToastMessage(ActionResultBase responseFromServer, SFFontEditText etNewPassword, SFFontEditText etConfirmPassword) {
        String message = responseFromServer.getMessages().get(0);
        AppUtils.createToastMessage(TypeNewPasswordActivity.this, mToast, message);
        removeFocusAndClearText(etNewPassword);
        removeFocusAndClearText(etConfirmPassword);
    }

    /**
     * Method that removes focus and clear text from text input fields.
     *
     * @param editText text field to clear.
     */
    private void removeFocusAndClearText(SFFontEditText editText) {
        editText.getText().clear();
        editText.setFocusableInTouchMode(false);
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(true);
        editText.setFocusable(true);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sendNewPasswordRequest != null) {
            sendNewPasswordRequest.cancel();
        }
    }

}
