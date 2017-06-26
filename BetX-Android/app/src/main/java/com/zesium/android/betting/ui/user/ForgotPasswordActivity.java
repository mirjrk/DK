package com.zesium.android.betting.ui.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.zesium.android.betting.R;
import com.zesium.android.betting.model.forgot_password.ForgottenPasswordAnswerMobileModel;
import com.zesium.android.betting.model.forgot_password.ForgottenPasswordModel;
import com.zesium.android.betting.model.forgot_password.ForgottenPasswordQuestion;
import com.zesium.android.betting.model.user.Question;
import com.zesium.android.betting.utils.AppConstants;
import com.zesium.android.betting.utils.AppLogger;
import com.zesium.android.betting.BetXApplication;
import com.zesium.android.betting.service.NetworkHelper;
import com.zesium.android.betting.utils.Preferences;
import com.zesium.android.betting.model.util.TranslationConstants;
import com.zesium.android.betting.utils.AppUtils;
import com.zesium.android.betting.model.util.WSUtils;
import com.zesium.android.betting.ui.widgets.SFFontEditText;
import com.zesium.android.betting.ui.widgets.SFFontTextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.string.ok;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ForgotPasswordActivity.class.getSimpleName();
    private TextInputLayout emailWrapper;
    private SFFontEditText etAnswer;
    private SFFontEditText etEmail;
    private Button btnResetPassword;
    private TextInputLayout tilSecurityQuestion;
    private Toast mToast;
    private int securityQuestionId;
    private String mCriptedUsername;
    private String username;
    private String msg;
    private String mSecurityQuestionValue;
    private boolean darkTheme;
    private String enterUsernameOrEmailTxt;
    private String enterAnswerTxt;
    private String userNotFoundTxt;
    private String warningTxt;
    private RelativeLayout rlProgressLayout;
    private LinearLayout llHolderLayout;
    private Call<JsonArray> securityQuestionsRequest;
    private Call<ForgottenPasswordQuestion> securityQuestionRequest;
    private Call<JsonObject> sendAnswerRequest;

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
        setTheme(R.style.MyMaterialThemeLight);

        //setting theme for this activity
        darkTheme = Preferences.getTheme(this);
        if (darkTheme) {
            setTheme(R.style.ForgotPasswordDark);
        } else {
            setTheme(R.style.MyMaterialThemeLight);
        }

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                username = extras.getString("username");
                getSecurityQuestionId(username);
            }
        }

        setContentView(R.layout.activity_forgot_password);

        String toolbarTitle;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.FORGOT_PASSWORD)) {
            toolbarTitle = BetXApplication.translationMap.get(TranslationConstants.FORGOT_PASSWORD);
        } else {
            toolbarTitle = getString(R.string.title_forgot_password);
        }

        // Set toolbar for login view
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        if (actionbar != null) {
            // Set back arrow to toolbar
            actionbar.setDisplayHomeAsUpEnabled(true);
            actionbar.setDisplayShowHomeEnabled(true);
            actionbar.setTitle(toolbarTitle);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.SECURITY_QUESTION)) {
            mSecurityQuestionValue = BetXApplication.translationMap.get(TranslationConstants.SECURITY_QUESTION);
        } else {
            mSecurityQuestionValue = getResources().getString(R.string.security_question);
        }


        initialiseFields();

        setTranslationForOtherStringValues();

        fillFieldsWithValues();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            etEmail.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.text_gray), PorterDuff.Mode.SRC_ATOP);
            etAnswer.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.text_gray), PorterDuff.Mode.SRC_ATOP);
        }
        btnResetPassword.setOnClickListener(this);
        etAnswer.setOnClickListener(this);

        // If error is previously set to email edit text, on next focus on thi filed error will be removed
        etEmail.setOnFocusChangeListener((view, b) -> {
            if (b) {
                emailWrapper.setError("");
            }
        });

        etAnswer.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                tilSecurityQuestion.setError("");

                if (!etEmail.getText().toString().equals("")) {
                    if (NetworkHelper.isNetworkAvailable(ForgotPasswordActivity.this)) {
                        getSecurityQuestionId(etEmail.getText().toString());
                    }
                } else {
                    emailWrapper.setError(enterUsernameOrEmailTxt);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        //showing layout
        llHolderLayout.setVisibility(View.VISIBLE);

        //clearing focus after coming back from SmsConfirmationCode screen
        removeFocusAndClearText(etEmail);
        removeFocusAndClearText(etAnswer);
        mSecurityQuestionValue = getResources().getString(R.string.security_question);
        tilSecurityQuestion.setHint(mSecurityQuestionValue);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (!AppConstants.isFromSmsConfirmationScreen) {
                username = extras.getString("username");
                etEmail.setText(username);
                getSecurityQuestionId(username);
            }
        }
        AppConstants.isFromSmsConfirmationScreen = false;
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
            case R.id.btn_reset_password_button:
                if (NetworkHelper.isNetworkAvailable(ForgotPasswordActivity.this)) {
                    hideKeyboard();
                    final String loginName = etEmail.getText().toString();
                    final String answer = etAnswer.getText().toString();

                    if (loginName.equals("")) {
                        emailWrapper.setError(enterUsernameOrEmailTxt);
                    } else {
                        emailWrapper.setError(null);
                    }

                    if (answer.equals("")) {
                        tilSecurityQuestion.setError(enterAnswerTxt);
                    } else {
                        tilSecurityQuestion.setError(null);
                    }
                    if (!loginName.isEmpty() && !answer.isEmpty()) {
                        sendAnswer();
                        llHolderLayout.setVisibility(View.GONE);
                        rlProgressLayout.setVisibility(View.VISIBLE);
                    }

                } else {
                    AppUtils.createToastMessage(ForgotPasswordActivity.this, mToast, getResources().getString(R.string.check_internet));
                }
                break;
            case R.id.et_security_answer:
        }
    }

    private void initialiseFields() {
        // Initialize fields
        rlProgressLayout = (RelativeLayout) findViewById(R.id.rl_progress_layout);
        ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(this, R.color.primary_red),
                android.graphics.PorterDuff.Mode.SRC_IN);
        llHolderLayout = (LinearLayout) findViewById(R.id.ll_parent_layout);
        emailWrapper = (TextInputLayout) findViewById(R.id.email_text_input_layout);
        tilSecurityQuestion = (TextInputLayout) findViewById(R.id.til_security_question);
        etAnswer = (SFFontEditText) findViewById(R.id.et_security_answer);
        etEmail = (SFFontEditText) findViewById(R.id.et_login_name);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password_button);
    }

    private void fillFieldsWithValues() {

        tilSecurityQuestion.setHint(mSecurityQuestionValue);

        String btnResetPasswordText;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.RESET_PASSWORD)) {
            btnResetPasswordText = BetXApplication.translationMap.get(TranslationConstants.RESET_PASSWORD);
        } else {
            btnResetPasswordText = getString(R.string.reset_password);
        }
        btnResetPassword.setText(btnResetPasswordText);

        Typeface typeface = Typeface.createFromAsset(getAssets(), AppConstants.DEFAULT_FONT);
        btnResetPassword.setTypeface(typeface);

        if (username != null) {
            etEmail.setText(username);
        }

        String enterUsernameOrEmail;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.USERNAME_OR_EMAIL)) {
            enterUsernameOrEmail = BetXApplication.translationMap.get(TranslationConstants.USERNAME_OR_EMAIL);
        } else {
            enterUsernameOrEmail = getString(R.string.enter_login_name_hint);
        }
        emailWrapper.setHint(enterUsernameOrEmail);
    }

    /**
     * This methods hides keyboard from phone screen
     */
    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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

    private void createToastMessage(String text) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        mToast.show();
    }

    /**
     * Service for getting id of the security question.
     *
     * @param username String containing username.
     */
    private void getSecurityQuestionId(String username) {
        final ForgottenPasswordModel forgottenPasswordModel = new ForgottenPasswordModel();
        forgottenPasswordModel.setValue(username);
        securityQuestionRequest = NetworkHelper.getBetXService(AppConstants.USER_API).getSecurityQuestion(forgottenPasswordModel);
        securityQuestionRequest.enqueue(new Callback<ForgottenPasswordQuestion>() {


            @Override
            public void onResponse(Call<ForgottenPasswordQuestion> call, Response<ForgottenPasswordQuestion> response) {
                if (response.body() != null) {
                    ForgottenPasswordQuestion forgottenPasswordQuestion = response.body();
                    if (forgottenPasswordQuestion.getActionResult()) {
                        securityQuestionId = forgottenPasswordQuestion.getSecurityQuestionId();
                        mCriptedUsername = forgottenPasswordQuestion.getUsername();
                        AppLogger.d("id", String.valueOf(securityQuestionId));

                        if (NetworkHelper.isNetworkAvailable(ForgotPasswordActivity.this)) {
                            createGetSecurityQuestionsRequest();
                        } else {
                            createToastMessage(getResources().getString(R.string.no_internet_connection_message));
                        }
                    } else {
                        tilSecurityQuestion.setHint(mSecurityQuestionValue);
                        emailWrapper.setError(userNotFoundTxt);
                    }
                }
            }

            @Override
            public void onFailure(Call<ForgottenPasswordQuestion> call, Throwable t) {
                AppLogger.error(TAG, "Exception occured with message: ", t);
                if (!ForgotPasswordActivity.this.isFinishing()) {
                    int message = NetworkHelper.generateErrorMessage(t);
                    AppUtils.createToastMessage(getApplicationContext(), new Toast(getApplicationContext()), getString(message));
                }
            }
        });
    }

    /**
     * Service for getting security question.
     */
    private void createGetSecurityQuestionsRequest() {
        securityQuestionsRequest = NetworkHelper.getBetXService(AppConstants.USER_API).getSecurityQuestions(Preferences.getLanguage(ForgotPasswordActivity.this));
        securityQuestionsRequest.enqueue(new Callback<JsonArray>() {

            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                JsonArray questions = response.body();
                if (questions != null) {
                    for (JsonElement questionObj : questions.getAsJsonArray()) {
                        JsonObject questionData = questionObj.getAsJsonObject();
                        Question question = WSUtils.parseQuestionData(questionData);
                        if (question != null) {
                            if (question.getId() == securityQuestionId) {
                                String questionString = question.getQuestion();
                                tilSecurityQuestion.setHint(questionString);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                AppLogger.error(TAG, "Exception occured with message: ", t);
                if (!ForgotPasswordActivity.this.isFinishing()) {
                    int message = NetworkHelper.generateErrorMessage(t);
                    AppUtils.createToastMessage(getApplicationContext(), new Toast(getApplicationContext()), getString(message));
                }
            }
        });
    }


    /**
     * Service for sending sendAnswerRequest(for security question) and user name.
     */
    private void sendAnswer() {
        ForgottenPasswordAnswerMobileModel forgottenPasswordAnswerMobileModelCall = new ForgottenPasswordAnswerMobileModel();
        final String mEnteredAnswer = etAnswer.getText().toString();
        String userName = etEmail.getText().toString();
        forgottenPasswordAnswerMobileModelCall.setUsername(mCriptedUsername);
        forgottenPasswordAnswerMobileModelCall.setSecurityAnswer(mEnteredAnswer);

        sendAnswerRequest = NetworkHelper.getBetXService(AppConstants.USER_API).sendAnswer(forgottenPasswordAnswerMobileModelCall);
        sendAnswerRequest.enqueue(new Callback<JsonObject>() {

            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.body() != null) {
                    JsonObject responseMessage = response.body();
                    JsonElement isSuccess = responseMessage.get("IsSuccess");
                    JsonElement messages = responseMessage.get("Messages");
                    JsonElement actionResult = responseMessage.get("ActionResult");
                    if (actionResult.toString().equals("false")) {
                        //String userError;
                        msg = String.valueOf(messages);
                        msg = msg.replaceAll("\"", ""); //Message from server has unneeded characters, they are removed here
                        msg = msg.replaceAll("\\[", "").replaceAll("]", "");

                        llHolderLayout.setVisibility(View.VISIBLE);
                        rlProgressLayout.setVisibility(View.GONE);
                        showDialogWithMessage(msg);
                        mCriptedUsername = "";
                    } else if (actionResult.toString().equals("true")) {
                        msg = String.valueOf(messages);
                        msg = msg.replaceAll("\"", ""); //Message from server has unneeded characters, they are removed here
                        msg = msg.replaceAll("\\[", "").replaceAll("]", "");
                        //msg += ".";
                        mCriptedUsername = "";
                        if (msg.isEmpty()) {
                            msg = getResources().getString(R.string.sms_code_info);

                        }

                        AppUtils.createToastMessage(ForgotPasswordActivity.this, mToast, msg);

                        Intent intent = new Intent(ForgotPasswordActivity.this, SmsConfirmationCodeForForgottenPasswordActivity.class);
                        intent.putExtra("user_name", userName);
                        startActivity(intent);
                        rlProgressLayout.setVisibility(View.GONE);
                        AppLogger.d("true", isSuccess.toString());
                    }
                }
            }


            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                AppLogger.error(TAG, "Exception occured with message: ", t);
                if (!ForgotPasswordActivity.this.isFinishing()) {
                    rlProgressLayout.setVisibility(View.GONE);
                    llHolderLayout.setVisibility(View.VISIBLE);
                    int message = NetworkHelper.generateErrorMessage(t);
                    AppUtils.createToastMessage(getApplicationContext(), new Toast(getApplicationContext()), getString(message));
                }
            }
        });
    }

    /**
     * Method for creating alert dialog that pop up when user click on reset password button
     *
     * @param message text to be displayed in dialog
     */
    private void showDialogWithMessage(final String message) {
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_with_message_forgotten_password, null);

        SFFontTextView tvMessage = (SFFontTextView) layout.findViewById(R.id.tv_dialog_message);
        tvMessage.setText(message);

        SFFontTextView tvTitle = (SFFontTextView) layout.findViewById(R.id.tv_dialog_title);
        tvTitle.setText(warningTxt);

        AlertDialog.Builder builder;
        if (darkTheme) {
            builder = new AlertDialog.Builder(this, R.style.DarkBackgroundFilterDialog).setView(layout);
        } else {
            builder = new AlertDialog.Builder(this, R.style.WhiteBackgroundFilterDialog).setView(layout);
        }

        final AlertDialog alertDialog = builder.setPositiveButton(ok,
                (dialog, which) -> {
                    String recoveryMessage1;
                    if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_ACCOUNT_RESET_PASSWORD_SUCCESS_MESSAGE)) {
                        recoveryMessage1 = BetXApplication.translationMap.get(TranslationConstants.MBL_ACCOUNT_RESET_PASSWORD_SUCCESS_MESSAGE);
                    } else {
                        recoveryMessage1 = getResources().getString(R.string.recovery_data_sent);
                    }
                    if (message.equals(recoveryMessage1)) {
                        finish();
                    } else {
                        dialog.dismiss();

                        etEmail.setText("");
                        etAnswer.setText("");
                        etEmail.requestFocus();
                        showSoftKeyboard(etEmail);

                        tilSecurityQuestion.setHint(mSecurityQuestionValue);
                    }
                }).create();


        alertDialog.show();
        alertDialog.setCancelable(true);
        //setting width and height
        alertDialog.getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(
                () -> AppUtils.adjustDialog(getApplicationContext(), alertDialog));
    }

    private void setTranslationForOtherStringValues() {
        if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_ACCOUNT_RESET_PASSWORD_FORM_USERNAME_EMAIL_PLACEHOLDER)) {
            enterUsernameOrEmailTxt = BetXApplication.translationMap.get(TranslationConstants.MBL_ACCOUNT_RESET_PASSWORD_FORM_USERNAME_EMAIL_PLACEHOLDER);
        } else {
            enterUsernameOrEmailTxt = getResources().getString(R.string.enter_username_or_email);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.ENTER_SECRET_ANSWER)) {
            enterAnswerTxt = BetXApplication.translationMap.get(TranslationConstants.ENTER_SECRET_ANSWER);
        } else {
            enterAnswerTxt = getResources().getString(R.string.enter_answer);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.USER_NOT_FOUND)) {
            userNotFoundTxt = BetXApplication.translationMap.get(TranslationConstants.USER_NOT_FOUND);
        } else {
            userNotFoundTxt = getResources().getString(R.string.user_not_found);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.WARNING)) {
            warningTxt = BetXApplication.translationMap.get(TranslationConstants.WARNING);
        } else {
            warningTxt = getResources().getString(R.string.dialog_title_warning);
        }
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
    protected void onDestroy() {
        super.onDestroy();
        if (securityQuestionsRequest != null) {
            securityQuestionsRequest.cancel();
        }
        if (securityQuestionRequest != null) {
            securityQuestionRequest.cancel();
        }
        if (sendAnswerRequest != null) {
            sendAnswerRequest.cancel();
        }
    }
}
