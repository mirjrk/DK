package com.zesium.android.betting.ui.user;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.zesium.android.betting.R;
import com.zesium.android.betting.model.user.UserPasswordChangeModel;
import com.zesium.android.betting.model.user.UserSecurityResponse;
import com.zesium.android.betting.utils.AppConstants;
import com.zesium.android.betting.utils.AppLogger;
import com.zesium.android.betting.BetXApplication;
import com.zesium.android.betting.service.NetworkHelper;
import com.zesium.android.betting.utils.Preferences;
import com.zesium.android.betting.model.util.TranslationConstants;
import com.zesium.android.betting.utils.AppUtils;
import com.zesium.android.betting.ui.widgets.SFFontEditText;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PasswordFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = PasswordFragment.class.getSimpleName();
    private Button btnChange;
    private ProgressDialog mProgressDialog;

    private TextInputLayout tilOldPassword;
    private TextInputLayout tilNewPassword;
    private TextInputLayout tilConfirmPassword;

    private SFFontEditText etOldPassword;
    private SFFontEditText etNewPassword;
    private SFFontEditText etConfirmPassword;

    private Toast mToast;
    private Call<UserSecurityResponse> changeUserPasswordRequest;

    public PasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_password, container, false);

        initialiseFields(view);
        setListeners();
        fillFieldsWithData();
        return view;
    }

    private void initialiseFields(View view) {
        btnChange = (Button) view.findViewById(R.id.btn_change);

        etOldPassword = (SFFontEditText) view.findViewById(R.id.et_old_password);
        etNewPassword = (SFFontEditText) view.findViewById(R.id.et_new_password);
        etConfirmPassword = (SFFontEditText) view.findViewById(R.id.et_confirm_new_password);

        tilOldPassword = (TextInputLayout) view.findViewById(R.id.til_old_password);
        tilNewPassword = (TextInputLayout) view.findViewById(R.id.til_new_password);
        tilConfirmPassword = (TextInputLayout) view.findViewById(R.id.til_confirm_new_password);

        etOldPassword.setOnFocusChangeListener(mPasswordFocusChangeListener);
        etNewPassword.setOnFocusChangeListener(mNewPasswordFocusChangeListener);
        etConfirmPassword.setOnFocusChangeListener(mConfirmPasswordFocusChangeListener);
    }

    private void setListeners() {
        etOldPassword.addTextChangedListener(mTextWatcher);
        etNewPassword.addTextChangedListener(mTextWatcher);
        etConfirmPassword.addTextChangedListener(mTextWatcher);
        btnChange.setOnClickListener(this);
    }

    private void fillFieldsWithData() {
        String oldPassword;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.OLD_PASSWORD)) {
            oldPassword = BetXApplication.translationMap.get(TranslationConstants.OLD_PASSWORD);
        } else {
            oldPassword = getActivity().getString(R.string.old_password);
        }
        tilOldPassword.setHint(oldPassword);

        String newPassword;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.NEW_PASSWORD)) {
            newPassword = BetXApplication.translationMap.get(TranslationConstants.NEW_PASSWORD);
        } else {
            newPassword = getActivity().getString(R.string.security_answer);
        }
        tilNewPassword.setHint(newPassword);

        String retypePassword;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.RETYPE_PASSWORD)) {
            retypePassword = BetXApplication.translationMap.get(TranslationConstants.RETYPE_PASSWORD);
        } else {
            retypePassword = getActivity().getString(R.string.confirm_new_password);
        }
        tilConfirmPassword.setHint(retypePassword);

        String change;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.CHANGE)) {
            change = BetXApplication.translationMap.get(TranslationConstants.CHANGE);
        } else {
            change = getActivity().getString(R.string.change);
        }
        btnChange.setText(change);
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
                tilOldPassword.setError("");
            }
        }
    };

    private void checkPassword() {
        String password = etOldPassword.getText().toString();
        boolean isValid = password.matches("^.*(?=.{6,})(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).*$");
        if (!isValid) {
            String passwordError;
            if (BetXApplication.translationMap.containsKey(TranslationConstants.VAL_PASSWORD)) {
                passwordError = BetXApplication.translationMap.get(TranslationConstants.VAL_PASSWORD);
            } else {
                passwordError = getString(R.string.error_password);
            }
            tilOldPassword.setError(passwordError);
        } else {
            tilOldPassword.setError("");
        }
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
        String s2 = etOldPassword.getText().toString();
        String s3 = etConfirmPassword.getText().toString();

        if (s1.equals("") || s2.equals("") || s3.equals("")) {
            btnChange.setEnabled(false);
        } else {
            btnChange.setEnabled(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_change:
                if (NetworkHelper.isNetworkAvailable(getActivity())) {
                    verifyFieldsAndCreateRequest();
                } else {
                    AppUtils.createToastMessage(getActivity(), mToast, getResources().getString(R.string.check_internet));
                }
                break;
        }
    }

    private void verifyFieldsAndCreateRequest() {
        checkNewPassword();
        checkPassword();
        checkConfirmPassword();

        boolean isOldPasswordValid = true;
        if (tilOldPassword.getError() != null) {
            isOldPasswordValid = tilOldPassword.getError().toString().equals("");
        }

        boolean isNewPasswordValid = true;
        if (tilNewPassword.getError() != null) {
            isNewPasswordValid = tilNewPassword.getError().toString().equals("");
        }

        boolean isConfirmPasswordValid = true;
        if (tilConfirmPassword.getError() != null) {
            isConfirmPasswordValid = tilConfirmPassword.getError().toString().equals("");
        }

        if (!isOldPasswordValid || !isNewPasswordValid || !isConfirmPasswordValid) {
            AppUtils.createToastMessage(getActivity(), mToast, "Fix above errors");
        } else {
            AppUtils.hideKeyboard(getActivity());
            createUpdatePasswordRequest();
        }
    }

    private void createUpdatePasswordRequest() {

        UserPasswordChangeModel userPasswordChangeModel = new UserPasswordChangeModel();
        userPasswordChangeModel.setOldPassword(etOldPassword.getText().toString());
        userPasswordChangeModel.setNewPassword(etNewPassword.getText().toString());

        changeUserPasswordRequest = NetworkHelper.getBetXService(AppConstants.USER_API).changeUserPassword(Preferences.getUserToken(getActivity()), Preferences.getTerminalId(getActivity()), userPasswordChangeModel);
        changeUserPasswordRequest.enqueue(new Callback<UserSecurityResponse>() {

            @Override
            public void onResponse(Call<UserSecurityResponse> call, Response<UserSecurityResponse> response) {
                if (getActivity() != null && !getActivity().isFinishing()) {
                    cancelProgressDialog();
                    if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        //refreshTokenRequest(getApp());

                        AppUtils.showDialogUnauthorized(getActivity());

                    } else {
                        UserSecurityResponse responseFromServer = response.body();
                        if (responseFromServer != null) {
                            if (responseFromServer.isSuccess()) {
                                String message;
                                if (BetXApplication.translationMap.containsKey(TranslationConstants.PASSWORD_SUCESSFULLY_CHANGED)) {
                                    message = BetXApplication.translationMap.get(TranslationConstants.PASSWORD_SUCESSFULLY_CHANGED);
                                } else {
                                    message = getActivity().getString(R.string.password_changed);
                                }
                                AppUtils.createToastMessage(getActivity(), mToast, message);
                                Preferences.setPassword(getActivity(), etNewPassword.getText().toString());

                                removeFocusAndClearText(etOldPassword);
                                removeFocusAndClearText(etNewPassword);
                                removeFocusAndClearText(etConfirmPassword);

                                removeErrorMessage(tilConfirmPassword);
                                removeErrorMessage(tilOldPassword);
                                removeErrorMessage(tilNewPassword);

                                btnChange.setEnabled(true);
                            } else {
                                if (responseFromServer.getMessages() != null && responseFromServer.getMessages().size() > 0) {
                                    String message = responseFromServer.getMessages().get(0);
                                    AppUtils.createToastMessage(getActivity(), mToast, message);
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UserSecurityResponse> call, Throwable t) {
                AppLogger.error(TAG, "Error occurred while trying update users password", t);
                if (getActivity() != null && !getActivity().isFinishing()) {
                    cancelProgressDialog();
                    int message = NetworkHelper.generateErrorMessage(t);
                    AppUtils.createToastMessage(getActivity(), new Toast(getActivity()), getString(message));
                }
            }
        });

        // Show progress dialog
        mProgressDialog = new ProgressDialog(getActivity());
        String message;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_SPRT_TICKET_PLEASE_WAIT)) {
            message = BetXApplication.translationMap.get(TranslationConstants.MBL_SPRT_TICKET_PLEASE_WAIT);
        } else {
            message = getString(R.string.title_activity_registration);
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    private void removeFocusAndClearText(SFFontEditText editText) {
        editText.getText().clear();
        editText.setFocusableInTouchMode(false);
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(true);
        editText.setFocusable(true);
    }

    private void removeErrorMessage(TextInputLayout textInputLayout) {
        textInputLayout.setError("");
    }

    private void cancelProgressDialog() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (this.isVisible()) {
            if (!isVisibleToUser) {
                tilOldPassword.setError("");
                tilNewPassword.setError("");
                tilConfirmPassword.setError("");

                tilOldPassword.clearFocus();
                tilNewPassword.clearFocus();
                tilConfirmPassword.clearFocus();

                etOldPassword.clearFocus();
                etNewPassword.clearFocus();
                etConfirmPassword.clearFocus();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (changeUserPasswordRequest != null) {
            changeUserPasswordRequest.cancel();
        }
    }
}
