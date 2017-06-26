package com.zesium.android.betting.ui.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.zesium.android.betting.R;
import com.zesium.android.betting.model.user.User;
import com.zesium.android.betting.model.user.UserDetails;
import com.zesium.android.betting.utils.AppConstants;
import com.zesium.android.betting.utils.AppLogger;
import com.zesium.android.betting.BetXApplication;
import com.zesium.android.betting.service.NetworkHelper;
import com.zesium.android.betting.utils.Preferences;
import com.zesium.android.betting.model.util.TranslationConstants;
import com.zesium.android.betting.utils.AppUtils;
import com.zesium.android.betting.ui.main.ContactUsActivity;
import com.zesium.android.betting.ui.main.IFloatingTicket;
import com.zesium.android.betting.ui.widgets.SFFontEditText;
import com.zesium.android.betting.ui.widgets.SFFontTextView;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = LoginFragment.class.getSimpleName();
    private SFFontEditText etUserName;
    private SFFontEditText etPassword;
    private TextInputLayout tilPassword;
    private TextInputLayout tilUserName;
    private Button btnLogin;
    private Button btnRegister;
    private ProgressDialog mProgressDialog;
    private SFFontTextView tvForgotPassword;
    private SFFontTextView tvContactUs;
    private Toast mToast;
    private boolean shouldReturnTotickets;
    private String checkYourInternetTxt;
    private String enterUsernameTxt;
    private String enterPasswordTxt;
    private String userNotFoundTxt;
    private Call<User> loginUserRequest;
    private Call<UserDetails> userDetailsRequest;
    private IFloatingTicket mCallback;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof IFloatingTicket) {
            mCallback = (IFloatingTicket) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (getArguments() != null) {
            shouldReturnTotickets = getArguments().getBoolean(AppConstants.SHOULD_RETURN_TO_TICKETS);
        }

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        setTranslationForOtherStringValues();

        mCallback.hideFloatingTicket();

        initializeFields(rootView);

        setTranslationForOtherStringValues();

        fillFieldsWithValues();

        setListeners();

        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                tilUserName.setError("");
                tilPassword.setError("");

                Intent intent = new Intent(getActivity(), RegistrationActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_login:
                if (NetworkHelper.isNetworkAvailable(getActivity())) {
                    makeLoginRequest();
                } else {
                    NetworkHelper.showNoConnectionDialog(getActivity());
                }
                break;
            case R.id.tv_forgot_password:
                tilUserName.setError("");
                tilPassword.setError("");

                Intent intentForgotPassword = new Intent(getActivity(), ForgotPasswordActivity.class);
                if (!etUserName.getText().toString().equals("")) {
                    intentForgotPassword.putExtra("username", etUserName.getText().toString());
                    intentForgotPassword.putExtra("login", true);
                }
                startActivity(intentForgotPassword);
                break;
            case R.id.tv_contact_us:
                Intent intentContactUs = new Intent(getActivity(), ContactUsActivity.class);
                startActivity(intentContactUs);
                break;
        }
    }

    private void initializeFields(View rootView) {
        tilUserName = (TextInputLayout) rootView.findViewById(R.id.email_text_input_layout);
        tilPassword = (TextInputLayout) rootView.findViewById(R.id.password_text_input_layout);
        etUserName = (SFFontEditText) rootView.findViewById(R.id.et_username);
        tvForgotPassword = (SFFontTextView) rootView.findViewById(R.id.tv_forgot_password);
        tvContactUs = (SFFontTextView) rootView.findViewById(R.id.tv_contact_us);
        etPassword = (SFFontEditText) rootView.findViewById(R.id.et_password);
        btnLogin = (Button) rootView.findViewById(R.id.btn_login);
        btnRegister = (Button) rootView.findViewById(R.id.btn_register);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            etUserName.getBackground().mutate().setColorFilter(ContextCompat.getColor(getActivity(), R.color.gray_line_divider), PorterDuff.Mode.SRC_ATOP);
            etPassword.getBackground().mutate().setColorFilter(ContextCompat.getColor(getActivity(), R.color.gray_line_divider), PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void fillFieldsWithValues() {
        String userName;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.USERNAME)) {
            userName = BetXApplication.translationMap.get(TranslationConstants.USERNAME);
        } else {
            userName = getResources().getString(R.string.username);
        }
        tilUserName.setHint(userName);

        String password;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.PASSWORD)) {
            password = BetXApplication.translationMap.get(TranslationConstants.PASSWORD);
        } else {
            password = getResources().getString(R.string.password);
        }
        tilPassword.setHint(password);

        String forgotPassword;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.FORGOT_PASSWORD)) {
            forgotPassword = BetXApplication.translationMap.get(TranslationConstants.FORGOT_PASSWORD);
        } else {
            forgotPassword = getResources().getString(R.string.title_forgot_password);
        }
        tvForgotPassword.setText(forgotPassword);

        String contactUs;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.CONTACT_US)) {
            contactUs = BetXApplication.translationMap.get(TranslationConstants.CONTACT_US);
        } else {
            contactUs = getResources().getString(R.string.title_contact_us);
        }
        tvContactUs.setText(contactUs);

        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), AppConstants.DEFAULT_FONT);

        String btnRegistrationText;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.REGISTRATION)) {
            btnRegistrationText = BetXApplication.translationMap.get(TranslationConstants.REGISTRATION);
        } else {
            btnRegistrationText = getString(R.string.title_activity_registration);
        }
        btnRegister.setText(btnRegistrationText);
        btnRegister.setTypeface(typeface);

        String btnLoginText;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.LOGIN)) {
            btnLoginText = BetXApplication.translationMap.get(TranslationConstants.LOGIN);
        } else {
            btnLoginText = getString(R.string.login);
        }
        btnLogin.setText(btnLoginText);
        btnLogin.setTypeface(typeface);
    }

    private void setListeners() {
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);

        tvContactUs.setOnClickListener(this);
        // Set text change listeners to make sure field is not empty
        etUserName.addTextChangedListener(mTextWatcher);
        etPassword.addTextChangedListener(mTextWatcher);
    }

    private void makeLoginRequest() {
        final String username = etUserName.getText().toString();
        final String password = etPassword.getText().toString();

        boolean isUserNameValid = false;

        if (username.equals("")) {
            tilUserName.setError(enterUsernameTxt);
        } else {
            tilUserName.setError("");
            isUserNameValid = true;
        }

        boolean isPasswordValid = false;
        if (password.equals("")) {
            tilPassword.setError(enterPasswordTxt);
        } else {
            tilPassword.setError("");
            isPasswordValid = true;
        }

        if (isPasswordValid && isUserNameValid) {
            //Make login userDetailsRequest to server
            loginUserRequest = NetworkHelper.getBetXService(AppConstants.USER_AUTHORIZATION_API).loginUser(AppConstants.USER_AUTHORIZATION_GRANT_TYPE_VALUE,
                    username, password, AppConstants.USER_AUTHORIZATION_CLIENT_ID_VALUE,
                    AppConstants.USER_AUTHORIZATION_TERMINAL_TYPE_VALUE);
            loginUserRequest.enqueue(new Callback<User>() {

                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    if (response.body() != null) {
                        User user = response.body();
                        if (NetworkHelper.isNetworkAvailable(getActivity())) {
                            createGetProfileRequest(AppConstants.USER_TOKEN_BEARER + user.getAccess_token(),
                                    username, password, user.getRefresh_token(), user.getTerminal_id(), user.getExpires_in());
                        } else {
                            cancelProgressDialog();
                            Toast.makeText(getActivity(), checkYourInternetTxt,
                                    Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        cancelProgressDialog();
                        createLoginFailedDialogue();
                    }

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    AppLogger.error(TAG, "Error occurred while trying to make a login request ", t);
                    if (getActivity() != null && !getActivity().isFinishing()) {
                        cancelProgressDialog();
                        createLoginFailedDialogue();
                    }
                }
            });
            // Show progress dialog
            mProgressDialog = new ProgressDialog(getActivity());
            String message;
            if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_SPRT_TICKET_PLEASE_WAIT)) {
                message = BetXApplication.translationMap.get(TranslationConstants.MBL_SPRT_TICKET_PLEASE_WAIT);
            } else {
                message = getString(R.string.please_wait);
            }
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

    private void createLoginFailedDialogue() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_logout, null);

        String loginFailed;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.LOGINFAILED)) {
            loginFailed = BetXApplication.translationMap.get(TranslationConstants.LOGINFAILED);
        } else {
            loginFailed = getString(R.string.login_failed);
        }

        SFFontTextView tvTitle = (SFFontTextView) layout.findViewById(R.id.logout_title_textview);
        tvTitle.setText(loginFailed);

        AlertDialog.Builder builder;
        if (Preferences.getTheme(getActivity())) {
            builder = new AlertDialog.Builder(getActivity(), R.style.DarkBackgroundFilterDialog).setView(layout);
        } else {
            builder = new AlertDialog.Builder(getActivity(), R.style.WhiteBackgroundFilterDialog).setView(layout);
        }

        String ok;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.LB_OK)) {
            ok = BetXApplication.translationMap.get(TranslationConstants.LB_OK);
        } else {
            ok = getString(R.string.dialog_ok);
        }

        SFFontTextView tvMessage = (SFFontTextView) layout.findViewById(R.id.tv_dialog_message);
        tvMessage.setText(userNotFoundTxt);

        final AlertDialog alertDialog = builder.setPositiveButton(ok,
                (dialog, which) -> {

                }).create();

        alertDialog.show();
        alertDialog.setCancelable(true);
        //setting width and height
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(
                    () -> AppUtils.adjustDialog(getActivity().getApplicationContext(), alertDialog));
        }
    }

    private void createGetProfileRequest(final String userToken, final String username, final String password,
                                         final String refreshToken, final String terminalId, String expiresIn) {

        userDetailsRequest = NetworkHelper.getBetXService(AppConstants.USER_API).getUserDetails(userToken, terminalId);

        userDetailsRequest.enqueue(new Callback<UserDetails>() {

            @Override
            public void onResponse(Call<UserDetails> call, Response<UserDetails> response) {
                if (getActivity() != null && !getActivity().isFinishing()) {
                    cancelProgressDialog();
                    if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        AppUtils.showDialogUnauthorized(getActivity());
                    } else {
                        UserDetails userDetails = response.body();
                        if (userDetails != null) {
                            Intent intent = new Intent(getActivity(), LoginWithPinActivity.class);
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
                            intent.putExtra(AppConstants.SHOULD_RETURN_TO_TICKETS, shouldReturnTotickets);
                            getActivity().startActivityForResult(intent, AppConstants.TICKETS_FRAGMENT_REQUEST);
                        } else {
                            AppUtils.createToastMessage(getActivity(), mToast, "Unable to get profile data");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UserDetails> call, Throwable t) {
                AppLogger.error(TAG, "Exception occurred with message: ", t);
                if (getActivity() != null && !getActivity().isFinishing()) {
                    cancelProgressDialog();
                    int message = NetworkHelper.generateErrorMessage(t);
                    AppUtils.createToastMessage(getActivity(), new Toast(getActivity()), getString(message));
                }
            }
        });

    }

    private final TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            tilUserName.setError("");
            tilPassword.setError("");
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };

    /**
     * Method that translates strings depending on chosen language.
     * If string's translation is not available string will get it's value from strings.xml file.
     */
    private void setTranslationForOtherStringValues() {
        if (BetXApplication.translationMap.containsKey(TranslationConstants.PLEASE_CHECK_YOUR_INTERNET_CONNECTION)) {
            checkYourInternetTxt = BetXApplication.translationMap.get(TranslationConstants.PLEASE_CHECK_YOUR_INTERNET_CONNECTION);
        } else {
            checkYourInternetTxt = getResources().getString(R.string.check_internet);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_ACCOUNT_RESET_PASSWORD_FORM_USERNAME_EMAIL_PLACEHOLDER)) {
            enterUsernameTxt = BetXApplication.translationMap.get(TranslationConstants.MBL_ACCOUNT_RESET_PASSWORD_FORM_USERNAME_EMAIL_PLACEHOLDER);
        } else {
            enterUsernameTxt = getResources().getString(R.string.enter_username_or_email);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_ACCOUNT_PAYOUT_PROVIDER_FORM_PASSWORD_PLACEHOLDER)) {
            enterPasswordTxt = BetXApplication.translationMap.get(TranslationConstants.MBL_ACCOUNT_PAYOUT_PROVIDER_FORM_PASSWORD_PLACEHOLDER);
        } else {
            enterPasswordTxt = getResources().getString(R.string.enter_password);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.LOGIN_WRONG_CREDENTIALS)) {
            userNotFoundTxt = BetXApplication.translationMap.get(TranslationConstants.LOGIN_WRONG_CREDENTIALS);
        } else {
            userNotFoundTxt = getResources().getString(R.string.login_wrong_credentials);
        }
    }

    private void cancelProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (loginUserRequest != null) {
            loginUserRequest.cancel();
        }
        if (userDetailsRequest != null) {
            userDetailsRequest.cancel();
        }
        cancelProgressDialog();
    }
}
