package com.zesium.android.betting.ui.user;

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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zesium.android.betting.R;
import com.zesium.android.betting.model.BaseUserConfirmModel;
import com.zesium.android.betting.model.user.ActionResultBase;
import com.zesium.android.betting.utils.AppConstants;
import com.zesium.android.betting.utils.AppLogger;
import com.zesium.android.betting.BetXApplication;
import com.zesium.android.betting.service.NetworkHelper;
import com.zesium.android.betting.utils.Preferences;
import com.zesium.android.betting.ui.widgets.SFFontEditText;
import com.zesium.android.betting.ui.widgets.SFFontTextView;
import com.zesium.android.betting.utils.AppUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SmsConfirmationCodeForForgottenPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = SmsConfirmationCodeForForgottenPasswordActivity.class.getSimpleName();
    private SFFontEditText mETConfirmationCode;
    private Button mBTNConfirm;
    private SFFontTextView tvResendCode;
    private String mUserName;
    private BroadcastReceiver mBroadcastReceiver;
    private boolean isBroadcastSubscribed;
    private RelativeLayout rlProgressLayout;
    private LinearLayout llHolderLayout;
    private Call<Boolean> resendConfirmationCodeRequest;
    private Call<ActionResultBase> sendReceivedSmsCodeRequest;

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

        setContentView(R.layout.activity_sms_confirmation_code_for_forgotten_password);

        // Set toolbar for tickets
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initialiseLayouts();

        mBTNConfirm.setOnClickListener(this);
        tvResendCode.setOnClickListener(this);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mETConfirmationCode.getBackground().setColorFilter(ContextCompat.getColor(SmsConfirmationCodeForForgottenPasswordActivity.this, R.color.gray_line_divider), PorterDuff.Mode.SRC_ATOP);
        }

        //mService = NetworkHelper.initializeService(AppUtils.USER_API);
    }

    @Override
    protected void onResume() {
        super.onResume();

        llHolderLayout.setVisibility(View.VISIBLE);
        rlProgressLayout.setVisibility(View.GONE);

        removeFocusAndClearText(mETConfirmationCode);
        if (getIntent() != null) {
            mUserName = getIntent().getStringExtra("user_name");

        }

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
                AppLogger.error(TAG, "Exception occured with message: ", e);
            }
            isBroadcastSubscribed = false;
        }
    }


    /**
     * Initialise layouts for this screen.
     */
    private void initialiseLayouts() {
        rlProgressLayout = (RelativeLayout) findViewById(R.id.rl_progress_layout);
        ProgressBar mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mProgressBar.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(this, R.color.primary_red),
                android.graphics.PorterDuff.Mode.SRC_IN);
        llHolderLayout = (LinearLayout) findViewById(R.id.ll_parent_layout);
        mETConfirmationCode = (SFFontEditText) findViewById(R.id.et_confirmation_code);
        mBTNConfirm = (Button) findViewById(R.id.btn_confirm);
        tvResendCode = (SFFontTextView) findViewById(R.id.tv_send_new_code);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                if (mETConfirmationCode.getText().toString().length() != 4) {
                    Toast.makeText(SmsConfirmationCodeForForgottenPasswordActivity.this, "You must enter 4 digit confirmation code!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    if (NetworkHelper.isNetworkAvailable(SmsConfirmationCodeForForgottenPasswordActivity.this)) {
                        createUserVerificationRequest(mETConfirmationCode.getText().toString());
                        llHolderLayout.setVisibility(View.GONE);
                        rlProgressLayout.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(SmsConfirmationCodeForForgottenPasswordActivity.this, getString(R.string.no_internet_connection_message),
                                Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.tv_send_new_code:
                BetXApplication app = (BetXApplication) getApplication();
                if (NetworkHelper.isNetworkAvailable(SmsConfirmationCodeForForgottenPasswordActivity.this)) {
                    app.setResendCodeCounter(app.getResendCodeCounter() + 1);
                    createResendCodeRequest();
                } else {
                    Toast.makeText(SmsConfirmationCodeForForgottenPasswordActivity.this, getString(R.string.no_internet_connection_message),
                            Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }


    /**
     * Create request for user verification with code which is received from sms message.
     *
     * @param code String value of 4 digit confirmation code.
     */
    private void createUserVerificationRequest(String code) {
        BaseUserConfirmModel baseUserConfirmModel = new BaseUserConfirmModel();
        baseUserConfirmModel.setActivationCode(code);

        sendReceivedSmsCodeRequest = NetworkHelper.getBetXService(AppConstants.USER_API).sendReceivedSmsCode("2000", baseUserConfirmModel);

        sendReceivedSmsCodeRequest.enqueue(new Callback<ActionResultBase>() {

            @Override
            public void onResponse(Call<ActionResultBase> call, Response<ActionResultBase> response) {
                //cancelProgressDialog();
                ActionResultBase actionResultBase = response.body();

                if (actionResultBase != null) {
                    if (actionResultBase.isActionResult()) {

                        Intent intent = new Intent(SmsConfirmationCodeForForgottenPasswordActivity.this, TypeNewPasswordActivity.class);
                        intent.putExtra(AppConstants.CODE, code);
                        startActivity(intent);
                        rlProgressLayout.setVisibility(View.GONE);
                    } else {
                        String message;
                        if (actionResultBase.getMessages() != null && actionResultBase.getMessages().size() > 0) {
                            message = actionResultBase.getMessages().get(0);
                        } else {
                            message = "Problem occurred. Please try again!";
                        }
                        llHolderLayout.setVisibility(View.VISIBLE);
                        rlProgressLayout.setVisibility(View.GONE);
                        llHolderLayout.setVisibility(View.VISIBLE);
                        Toast.makeText(SmsConfirmationCodeForForgottenPasswordActivity.this, message, Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(SmsConfirmationCodeForForgottenPasswordActivity.this, "Unexpected error occurred, please try again!", Toast.LENGTH_SHORT).show();
                    rlProgressLayout.setVisibility(View.GONE);
                    llHolderLayout.setVisibility(View.VISIBLE);
                }
            }


            @Override
            public void onFailure(Call<ActionResultBase> call, Throwable t) {
                AppLogger.error(TAG, "Exception occured with message: ", t);
                if (!SmsConfirmationCodeForForgottenPasswordActivity.this.isFinishing()) {
                    int message = NetworkHelper.generateErrorMessage(t);
                    AppUtils.createToastMessage(getApplicationContext(), new Toast(getApplicationContext()), getString(message));
                    rlProgressLayout.setVisibility(View.GONE);
                    llHolderLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    /**
     * Method createResendCodeRequest creates resend sms code request.
     */

    private void createResendCodeRequest() {

        resendConfirmationCodeRequest = NetworkHelper.getBetXService(AppConstants.USER_API).resendConfirmationCode(mUserName); //mUserName
        resendConfirmationCodeRequest.enqueue(new Callback<Boolean>() {

            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.body() != null) {
                    if (response.body() != null) {
                        String message;
                        if (response.body()) {
                            message = "Confirmation code is successfully sent to your phone number!";
                        } else {
                            message = "Something went wrong. Please try again!";

                        }
                        Toast.makeText(SmsConfirmationCodeForForgottenPasswordActivity.this, message, Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                AppLogger.error(TAG, "Exception occurred with message: ", t);
                if (!SmsConfirmationCodeForForgottenPasswordActivity.this.isFinishing()) {
                    int message = NetworkHelper.generateErrorMessage(t);
                    AppUtils.createToastMessage(SmsConfirmationCodeForForgottenPasswordActivity.this, new Toast(SmsConfirmationCodeForForgottenPasswordActivity.this), getString(message));
                }
            }
        });
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
                                    mETConfirmationCode.setText(msgBody);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AppConstants.isFromSmsConfirmationScreen = true;
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        AppConstants.isFromSmsConfirmationScreen = true;
        super.onBackPressed();
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
        if (resendConfirmationCodeRequest != null) {
            resendConfirmationCodeRequest.cancel();
        }
        if (sendReceivedSmsCodeRequest != null) {
            sendReceivedSmsCodeRequest.cancel();
        }
    }
}
