package com.zesium.android.betting.ui.user;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsMessage;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.zesium.android.betting.R;
import com.zesium.android.betting.model.BaseUserConfirmModel;
import com.zesium.android.betting.model.user.ActionResultBase;
import com.zesium.android.betting.model.user.User;
import com.zesium.android.betting.model.user.UserDetails;
import com.zesium.android.betting.utils.AppConstants;
import com.zesium.android.betting.utils.AppLogger;
import com.zesium.android.betting.BetXApplication;
import com.zesium.android.betting.service.NetworkHelper;
import com.zesium.android.betting.utils.Preferences;
import com.zesium.android.betting.model.util.TranslationConstants;
import com.zesium.android.betting.utils.AppUtils;
import com.zesium.android.betting.ui.widgets.SFFontEditText;
import com.zesium.android.betting.ui.widgets.SFFontTextView;

import java.net.HttpURLConnection;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmationCodeActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = ConfirmationCodeActivity.class.getSimpleName();
    private SFFontEditText etConfirmationCode;
    private Button btnConfirm;
    private SFFontTextView tvTimer;
    private SFFontTextView tvResendCode;
    private ProgressDialog mProgressDialog;
    private String mUserName;
    private String mPassword;
    private Timer mTimer;
    private BroadcastReceiver mBroadcastReceiver;
    private Toast mToast;
    private String mMessage;
    private boolean isBroadcastSubscribed;
    private Call<User> loginUserRequest;
    private Call<UserDetails> userDetailsRequest;
    private Call<Boolean> resendConfirmationCodeRequest;
    private Call<ActionResultBase> confirmRegistrationRequest;

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

        setContentView(R.layout.activity_confirmation_code);

        // Set toolbar for tickets
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_SPRT_TICKET_PLEASE_WAIT)) {
            mMessage = BetXApplication.translationMap.get(TranslationConstants.MBL_SPRT_TICKET_PLEASE_WAIT);
        } else {
            mMessage = getString(R.string.please_wait);
        }

        BetXApplication betXApplication = (BetXApplication) getApplication();
        mUserName = betXApplication.getTempUserName();
        mPassword = betXApplication.getTempPassword();

        initialiseLayouts();

        btnConfirm.setOnClickListener(this);
        tvResendCode.setOnClickListener(this);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            etConfirmationCode.getBackground().setColorFilter(ContextCompat.getColor(ConfirmationCodeActivity.this, R.color.gray_line_divider), PorterDuff.Mode.SRC_ATOP);
        }

        BetXApplication betxApplication = (BetXApplication) getApplication();
        betxApplication.creteCountdownTimer(tvTimer, btnConfirm);

        createTimerForResendCodeText();
    }

    /**
     * Create timer that will make resend code functionality visible after 8 seconds.
     */
    private void createTimerForResendCodeText() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> tvResendCode.setVisibility(View.VISIBLE));
            }
        }, AppConstants.EIGHT_SECONDS);
    }

    /**
     * Initialise layouts for this screen.
     */
    private void initialiseLayouts() {
        etConfirmationCode = (SFFontEditText) findViewById(R.id.et_confirmation_code);
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        tvResendCode = (SFFontTextView) findViewById(R.id.tv_send_new_code);
        tvTimer = (SFFontTextView) findViewById(R.id.tv_timer);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                cancelTimer();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                if (etConfirmationCode.getText().toString().length() != 4) {
                    Toast.makeText(ConfirmationCodeActivity.this, "You must enter 4 digit confirmation code!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (NetworkHelper.isNetworkAvailable(ConfirmationCodeActivity.this)) {
                        createUserVerificationRequest(etConfirmationCode.getText().toString());
                    } else {
                        Toast.makeText(ConfirmationCodeActivity.this, getString(R.string.no_internet_connection_message),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.tv_send_new_code:

                BetXApplication app = (BetXApplication) getApplication();
                if (app.getResendCodeCounter() > 2) {
                    Toast.makeText(ConfirmationCodeActivity.this, "You have reached your limit for resending confirmation code!",
                            Toast.LENGTH_LONG).show();
                } else {
                    if (NetworkHelper.isNetworkAvailable(ConfirmationCodeActivity.this)) {
                        app.setResendCodeCounter(app.getResendCodeCounter() + 1);
                        createResendCodeRequest();
                    } else {
                        Toast.makeText(ConfirmationCodeActivity.this, getString(R.string.no_internet_connection_message),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    /**
     * Method createResendCodeRequest creates resend sms code userDetailsRequest.
     */
    private void createResendCodeRequest() {
        resendConfirmationCodeRequest = NetworkHelper.getBetXService(AppConstants.USER_API).resendConfirmationCode(mUserName);
        resendConfirmationCodeRequest.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body() != null) {
                    cancelProgressDialog();
                    if (response.body() != null) {
                        String message;
                        if (response.body()) {
                            message = "Confirmation code is successfully sent to your phone number!";
                        } else {
                            message = "Your account is previously activated or confirmation time has expired!";
                        }
                        Toast.makeText(ConfirmationCodeActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                if (!ConfirmationCodeActivity.this.isFinishing()) {
                    cancelProgressDialog();
                    int message = NetworkHelper.generateErrorMessage(t);
                    AppUtils.createToastMessage(ConfirmationCodeActivity.this, new Toast(ConfirmationCodeActivity.this), getString(message));
                }
                AppLogger.error(TAG, "Exception occurred with message: ", t);
            }
        });
        // Show progress dialog
        mProgressDialog = new ProgressDialog(ConfirmationCodeActivity.this);
        mProgressDialog.setMessage(mMessage);
        mProgressDialog.show();
    }

    /**
     * Cancels progress dialog.
     */
    private void cancelProgressDialog() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * Method initialiseSMSReceiver initializes broadcast receiver which will automatically receive
     * and handle sms verification code.
     */
    private void initialiseSMSReceiver() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(Integer.MAX_VALUE);

        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
                    Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
                    SmsMessage[] msgs;
                    String msg_from;
                    if (bundle != null) {
                        //---retrieve the SMS message received---
                        try {
                            Object[] pdus = (Object[]) bundle.get("pdus");
                            msgs = new SmsMessage[pdus.length];
                            for (int i = 0; i < msgs.length; i++) {
                                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                                msg_from = msgs[i].getOriginatingAddress();

                                if (msg_from.startsWith("BetX")) {
                                    abortBroadcast();
                                    String msgBody = msgs[i].getMessageBody();
                                    etConfirmationCode.setText(msgBody);

                                    createUserVerificationRequest(msgBody);
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            AppLogger.error(TAG, "Exception occured with message: ", e);
                        }
                    }
                }
            }
        };
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    /**
     * Create userDetailsRequest for user verification with code which is received from sms message.
     *
     * @param code String value of 4 digit confirmation code.
     */
    private void createUserVerificationRequest(String code) {
        BaseUserConfirmModel baseUserConfirmModel = new BaseUserConfirmModel();
        baseUserConfirmModel.setActivationCode(code);


        confirmRegistrationRequest = NetworkHelper.getBetXService(AppConstants.USER_API).confirmRegistration("2000", baseUserConfirmModel);
        confirmRegistrationRequest.enqueue(new Callback<ActionResultBase>() {

            @Override
            public void onResponse(Call<ActionResultBase> call, Response<ActionResultBase> response) {
                ActionResultBase actionResultBase = response.body();

                if (actionResultBase != null) {
                    if (actionResultBase.isActionResult()) {
                        Preferences.setIsRegistrationFinished(getApplicationContext(), true);
                        BetXApplication app = (BetXApplication) getApplication();
                        app.cancelTimer();
                        app.setTimeUntilFinish(0);
                        cancelTimer();
                        makeLoginRequest();
                        Toast.makeText(ConfirmationCodeActivity.this, "You have successfully activated your profile", Toast.LENGTH_SHORT).show();
                    } else {
                        String message;
                        if (actionResultBase.getMessages() != null && actionResultBase.getMessages().size() > 0) {
                            message = actionResultBase.getMessages().get(0);
                        } else {
                            message = "Problem occurred during activating your profile. Please try again!";
                        }
                        Toast.makeText(ConfirmationCodeActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(ConfirmationCodeActivity.this, "Unexpected error occurred, please try again!", Toast.LENGTH_SHORT).show();
                }
                cancelProgressDialog();
            }

            @Override
            public void onFailure(Call<ActionResultBase> call, Throwable t) {
                if (!ConfirmationCodeActivity.this.isFinishing()) {
                    cancelProgressDialog();
                    int message = NetworkHelper.generateErrorMessage(t);
                    AppUtils.createToastMessage(ConfirmationCodeActivity.this, new Toast(ConfirmationCodeActivity.this), getString(message));
                }
                AppLogger.error(TAG, "Exception occurred with message: ", t);
            }
        });
        // Show progress dialog
        mProgressDialog = new ProgressDialog(ConfirmationCodeActivity.this);
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
     * Make userDetailsRequest for login user.
     */
    private void makeLoginRequest() {
        loginUserRequest = NetworkHelper.getBetXService(AppConstants.USER_AUTHORIZATION_API).loginUser(AppConstants.USER_AUTHORIZATION_GRANT_TYPE_VALUE,
                mUserName, mPassword, AppConstants.USER_AUTHORIZATION_CLIENT_ID_VALUE,
                AppConstants.USER_AUTHORIZATION_TERMINAL_TYPE_VALUE);
        loginUserRequest.enqueue(new Callback<User>() {

            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.body() != null) {
                    User user = response.body();

                    if (NetworkHelper.isNetworkAvailable(ConfirmationCodeActivity.this)) {
                        createGetProfileRequest(AppConstants.USER_TOKEN_BEARER + user.getAccess_token(),
                                mUserName, mPassword, user.getRefresh_token(), user.getTerminal_id(), user.getExpires_in());
                    } else {
                        cancelProgressDialog();
                        Toast.makeText(ConfirmationCodeActivity.this, getString(R.string.no_internet_connection_message),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    cancelProgressDialog();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (!ConfirmationCodeActivity.this.isFinishing()) {
                    cancelProgressDialog();
                    int message = NetworkHelper.generateErrorMessage(t);
                    AppUtils.createToastMessage(ConfirmationCodeActivity.this, new Toast(ConfirmationCodeActivity.this), getString(message));
                }
                AppLogger.error(TAG, "Exception occurred with message: ", t);
            }
        });
    }

    /**
     * Make userDetailsRequest for getting user profile data.
     *
     * @param userToken    String value of users's token value,
     * @param username     String value of user's username,
     * @param password     String value of user's password,
     * @param refreshToken String value of refresh token data,
     * @param terminalId   String value of token id.
     */
    private void createGetProfileRequest(final String userToken, final String username, final String password,
                                         final String refreshToken, final String terminalId, String expiresIn) {

        userDetailsRequest = NetworkHelper.getBetXService(AppConstants.USER_API).getUserDetails(userToken, terminalId);
        userDetailsRequest.enqueue(new Callback<UserDetails>() {

            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                cancelProgressDialog();

                if (!ConfirmationCodeActivity.this.isFinishing()) {
                    if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        AppUtils.showDialogUnauthorized(ConfirmationCodeActivity.this);
                    } else {
                        UserDetails userDetails = response.body();
                        if (userDetails != null) {
                            cancelTimer();

                            Intent intent = new Intent(ConfirmationCodeActivity.this, LoginWithPinActivity.class);
                            intent.putExtra(AppConstants.LOGIN_USER_TOKEN, userToken);
                            intent.putExtra(AppConstants.LOGIN_USER_NAME, username);
                            intent.putExtra(AppConstants.LOGIN_PASSWORD, password);
                            intent.putExtra(AppConstants.LOGIN_REFRESH_TOKEN, refreshToken);
                            intent.putExtra(AppConstants.LOGIN_TERMINAL_ID, terminalId);
                            intent.putExtra(AppConstants.LOGIN_FIRST_NAME, userDetails.getFirstName());
                            intent.putExtra(AppConstants.LOGIN_LAST_NAME, userDetails.getLastName());
                            intent.putExtra(AppConstants.LOGIN_EMAIL, userDetails.getEmail());
                            intent.putExtra(AppConstants.LOGIN_EXPIRES_IN, expiresIn);
                            intent.putExtra(AppConstants.IS_FIRST_TIME_LOGIN_WITH_PIN, true);
                            intent.putExtra(AppConstants.SHOULD_RETURN_TO_TICKETS, false);
                            startActivityForResult(intent, AppConstants.TICKETS_FRAGMENT_REQUEST);
                        } else {
                            AppUtils.createToastMessage(ConfirmationCodeActivity.this, mToast, "Unable to get profile data");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                if (!ConfirmationCodeActivity.this.isFinishing()) {
                    cancelProgressDialog();
                    int message = NetworkHelper.generateErrorMessage(t);
                    AppUtils.createToastMessage(ConfirmationCodeActivity.this, new Toast(ConfirmationCodeActivity.this), getString(message));
                }
                AppLogger.error(TAG, "Exception occured with message: ", t);
            }
        });
    }

    /**
     * Cancel timer.
     */
    private void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (!isBroadcastSubscribed) {
            initialiseSMSReceiver();
            isBroadcastSubscribed = true;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (isBroadcastSubscribed) {
            try {
                unregisterReceiver(mBroadcastReceiver);
            } catch (IllegalArgumentException e) {
                AppLogger.error(TAG, "Exception occurred with message: ", e);
            }
            cancelTimer();
            isBroadcastSubscribed = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (userDetailsRequest != null) {
            userDetailsRequest.cancel();
        }

        if (resendConfirmationCodeRequest != null) {
            resendConfirmationCodeRequest.cancel();
        }

        if (confirmRegistrationRequest != null) {
            confirmRegistrationRequest.cancel();
        }
        if (loginUserRequest != null) {
            loginUserRequest.cancel();
        }
    }
}
