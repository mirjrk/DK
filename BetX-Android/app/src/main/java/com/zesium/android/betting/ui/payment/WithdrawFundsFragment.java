package com.zesium.android.betting.ui.payment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zesium.android.betting.R;
import com.zesium.android.betting.model.configuration.WalletConfiguration;
import com.zesium.android.betting.model.payment.BankInfo;
import com.zesium.android.betting.model.payment.Banks;
import com.zesium.android.betting.model.payment.Countries;
import com.zesium.android.betting.model.payment.WholeWithdrawData;
import com.zesium.android.betting.model.payment.WithdrawBody;
import com.zesium.android.betting.model.payment.WithdrawModel;
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
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class WithdrawFundsFragment extends Fragment implements View.OnFocusChangeListener, TextView.OnEditorActionListener, View.OnClickListener {

    private static final String TAG = WithdrawFundsFragment.class.getSimpleName();
    private SFFontTextView tvAccHolderValue;
    private SFFontTextView tvBankNameValue;
    private SFFontTextView tvBankCountryValue;
    private SFFontTextView tvBankAccNumberValue;
    private SFFontTextView tvWithdrawMinLimitValue;
    private SFFontTextView tvWithdrawMaxLimitValue;
    private SFFontTextView tvWithdrawCreditValue;
    private SFFontEditText etWithdrawAmount;
    private SFFontTextView etPersonalData;
    private SFFontTextView tvAccountHolder;
    private SFFontTextView tvBankData;
    private SFFontTextView tvBankName;
    private SFFontTextView tvCountry;
    private SFFontTextView tvBankAccNumber;
    private SFFontTextView tvWithdrawData;
    private SFFontTextView tvCredit;
    private SFFontTextView tvMinWithdrawLimit;
    private SFFontTextView tvMaxWithdrawLimit;
    private SFFontTextView tvEnterAmountForWithdraw;
    private SFFontTextView tvSecurityConfirmation;
    private Button btnConfirm;
    private TextInputLayout tilWithdrawPassword;
    private SFFontEditText etPassword;

    private WalletConfiguration mWalletConfiguration;

    private NumberFormat nf;
    private boolean isDataChecked = false;
    private String mNoEnoughCredit;
    private String mGreaterThanMax;
    private String mLessThanMin;
    private double mPayInValue;
    private boolean isDarkTheme;
    private Call<WithdrawModel> mWithdrawMoneyRequest;
    private Subscription mSubscription;
    private ProgressDialog mProgressDialog;
    private String mEnterAmountForWithdraw;

    public WithdrawFundsFragment() {
        // Required empty public constructor
    }

    public static WithdrawFundsFragment newInstance(WalletConfiguration walletConfiguration) {
        WithdrawFundsFragment fragment = new WithdrawFundsFragment();
        Bundle args = new Bundle();
        args.putSerializable(AppConstants.WALLET_CONFIGURATION, walletConfiguration);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mWalletConfiguration = (WalletConfiguration) getArguments().getSerializable(AppConstants.WALLET_CONFIGURATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment regarding users bank information
        View myView;
        if (mWalletConfiguration.isHasDebitInformation() && mWalletConfiguration.isHasVerificationDocument() && mWalletConfiguration.isHasAddressInformations()) {
            myView = inflater.inflate(R.layout.fragment_withdraw_funds, container, false);
            initializeWithdrawData(myView);
            initialiseWarningMessages();
            isDarkTheme = Preferences.getTheme(getActivity());

            // Initialise number format for money texts
            nf = NumberFormat.getNumberInstance(Locale.GERMAN);
            nf.setMinimumFractionDigits(2);
            nf.setMaximumFractionDigits(2);
            nf.setGroupingUsed(true);

            getWithdrawInfo();
            setWithdrawDataValues();
            fillFieldsWithValues();

            // Set listeners for amount edit text
            etWithdrawAmount.setOnFocusChangeListener(this);
            etWithdrawAmount.setOnEditorActionListener(this);

            if (Preferences.getTheme(getContext()) && etWithdrawAmount != null) {
                etWithdrawAmount.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bcg_pay_in_dark));
            } else {
                etWithdrawAmount.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bcg_pay_in_light));
            }
        } else {
            myView = inflater.inflate(R.layout.fragment_withdraw_funds_no_bank_data, container, false);

            if (!mWalletConfiguration.isHasDebitInformation()) {
                addLayoutsForAddingDebitInformation(myView);
            }

            if (!mWalletConfiguration.isHasVerificationDocument()) {
                addLayoutsForUsersId(myView);
            }
        }
        return myView;
    }

    private void fillFieldsWithValues() {
        String personalData;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.PERSONAL_DATA)) {
            personalData = BetXApplication.translationMap.get(TranslationConstants.PERSONAL_DATA);
        } else {
            personalData = getResources().getString(R.string.withdraw_funds_personal_data);
        }

        etPersonalData.setText(personalData);

        String accountHolder;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.ACCOUNT_HOLDER)) {
            accountHolder = BetXApplication.translationMap.get(TranslationConstants.ACCOUNT_HOLDER);
        } else {
            accountHolder = getResources().getString(R.string.withdraw_funds_account_holder);
        }
        tvAccountHolder.setText(accountHolder);

        String bankData;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.BANK_DATA)) {
            bankData = BetXApplication.translationMap.get(TranslationConstants.BANK_DATA);
        } else {
            bankData = getResources().getString(R.string.withdraw_funds_bank_data);
        }
        tvBankData.setText(bankData);

        String bankName;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.BANK_IDENTIFIER)) {
            bankName = BetXApplication.translationMap.get(TranslationConstants.BANK_IDENTIFIER);
        } else {
            bankName = getResources().getString(R.string.withdraw_funds_bank_name);
        }
        tvBankName.setText(bankName);


        String bankCountry;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_COUNTRIES_LABEL)) {
            bankCountry = BetXApplication.translationMap.get(TranslationConstants.MBL_ACCOUNT_REGISTER_FORM_COUNTRIES_LABEL);
        } else {
            bankCountry = getString(R.string.withdraw_funds_bank_country);
        }
        tvCountry.setText(bankCountry);


        String bankAccNumber;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.BANK_ACCOUNT_NUMBER)) {
            bankAccNumber = BetXApplication.translationMap.get(TranslationConstants.BANK_ACCOUNT_NUMBER);
        } else {
            bankAccNumber = getString(R.string.withdraw_funds_bank_account_number);
        }
        tvBankAccNumber.setText(bankAccNumber);

        String withdrawData;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.WITHDRAW_DATA)) {
            withdrawData = BetXApplication.translationMap.get(TranslationConstants.WITHDRAW_DATA);
        } else {
            withdrawData = getString(R.string.withdraw_funds_withdraw_data);
        }
        tvWithdrawData.setText(withdrawData);

        String withdraw_credit;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.CURRENT_BALANCE)) {
            withdraw_credit = BetXApplication.translationMap.get(TranslationConstants.CURRENT_BALANCE);
        } else {
            withdraw_credit = getString(R.string.withdraw_funds_credit);
        }
        tvCredit.setText(withdraw_credit);

        String minWithdrawLimit;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.MIN_WITHDRAW_LIMIT)) {
            minWithdrawLimit = BetXApplication.translationMap.get(TranslationConstants.MIN_WITHDRAW_LIMIT);
        } else {
            minWithdrawLimit = getString(R.string.withdraw_funds_min_limit);
        }
        tvMinWithdrawLimit.setText(minWithdrawLimit);

        String maxWithdrawLimit;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.MAX_WITHDRAW_LIMIT)) {
            maxWithdrawLimit = BetXApplication.translationMap.get(TranslationConstants.MAX_WITHDRAW_LIMIT);
        } else {
            maxWithdrawLimit = getString(R.string.withdraw_funds_max_limit);
        }
        tvMaxWithdrawLimit.setText(maxWithdrawLimit);


        if (BetXApplication.translationMap.containsKey(TranslationConstants.WITHDRAW_AMOUNT)) {
            mEnterAmountForWithdraw = BetXApplication.translationMap.get(TranslationConstants.WITHDRAW_AMOUNT);
        } else {
            mEnterAmountForWithdraw = getString(R.string.withdraw_funds_enter_amount);
        }
        tvEnterAmountForWithdraw.setText(mEnterAmountForWithdraw);

        String securityConfirmation;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.SECURITY_CONFIRMATION)) {
            securityConfirmation = BetXApplication.translationMap.get(TranslationConstants.SECURITY_CONFIRMATION);
        } else {
            securityConfirmation = getString(R.string.withdraw_funds_security_confirmation);
        }
        tvSecurityConfirmation.setText(securityConfirmation);

        String enterYourPassword;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.ENTER_PASSWORD)) {
            enterYourPassword = BetXApplication.translationMap.get(TranslationConstants.ENTER_PASSWORD);
        } else {
            enterYourPassword = getString(R.string.withdraw_funds_enter_password);
        }
        tilWithdrawPassword.setHint(enterYourPassword);

        String btnConfirm;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.CONFIRM)) {
            btnConfirm = BetXApplication.translationMap.get(TranslationConstants.CONFIRM);
        } else {
            btnConfirm = getString(R.string.withdraw_funds_confirm_caps);
        }
        this.btnConfirm.setText(btnConfirm);
    }

    /**
     * Method addLayoutsForUsersId adds layout for adding users ID.
     *
     * @param view inflated view when user hasn't got withdraw information.
     */
    private void addLayoutsForUsersId(View view) {
        LinearLayout mLLUploadGovernmentId = (LinearLayout) view.findViewById(R.id.ll_upload_government_id);
        SFFontTextView mTVUploadGovernmentId = (SFFontTextView) view.findViewById(R.id.tv_upload_government_id);
        Button mBtnUploadGovernmentId = (Button) view.findViewById(R.id.btn_upload_government_id);

        String uploadGovernmentIdTvString;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.UPLOAD_GOVERMENT_ISSUE_ID)) {
            uploadGovernmentIdTvString = BetXApplication.translationMap.get(TranslationConstants.UPLOAD_GOVERMENT_ISSUE_ID);
        } else {
            uploadGovernmentIdTvString = getResources().getString(R.string.tv_upload_government_id);
        }
        mTVUploadGovernmentId.setText(uploadGovernmentIdTvString);

        String uploadGovernmentIdBtnString;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.UPLOAD_ID)) {
            uploadGovernmentIdBtnString = BetXApplication.translationMap.get(TranslationConstants.UPLOAD_ID);
        } else {
            uploadGovernmentIdBtnString = getResources().getString(R.string.btn_upload_government_id);
        }
        mBtnUploadGovernmentId.setText(uploadGovernmentIdBtnString);

        mLLUploadGovernmentId.setVisibility(View.VISIBLE);
    }

    /**
     * Method addLayoutsForAddingDebitInformation adds layout for adding debit information.
     *
     * @param view inflated view when user hasn't got withdraw information.
     */
    private void addLayoutsForAddingDebitInformation(View view) {
        LinearLayout mLLAddBankData = (LinearLayout) view.findViewById(R.id.ll_add_bank_data);
        SFFontTextView mTVAddBankData = (SFFontTextView) view.findViewById(R.id.tv_add_bank_data);
        Button mBtnAddBankData = (Button) view.findViewById(R.id.btn_add_bank_data);

        String addBankDataTvString;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.BANK_ACCOUNT_INFO_MISSING)) {
            addBankDataTvString = BetXApplication.translationMap.get(TranslationConstants.BANK_ACCOUNT_INFO_MISSING);
        } else {
            addBankDataTvString = getResources().getString(R.string.tv_add_bank_data);
        }
        mTVAddBankData.setText(addBankDataTvString);

        String addBankBtnString;
        if (BetXApplication.translationMap.containsKey(TranslationConstants.EDIT_BANK_DATA)) {
            addBankBtnString = BetXApplication.translationMap.get(TranslationConstants.EDIT_BANK_DATA);
        } else {
            addBankBtnString = getResources().getString(R.string.btn_add_bank_data);
        }
        mBtnAddBankData.setText(addBankBtnString);

        mLLAddBankData.setVisibility(View.VISIBLE);
    }

    /**
     * Method initializeWithdrawData initialises all view layouts if withdraw data exists.
     *
     * @param view inflated view for withdraw information.
     */
    private void initializeWithdrawData(View view) {
        tvAccHolderValue = (SFFontTextView) view.findViewById(R.id.tv_account_holder_value);
        tvBankNameValue = (SFFontTextView) view.findViewById(R.id.tv_bank_name_value);
        tvBankCountryValue = (SFFontTextView) view.findViewById(R.id.tv_bank_country_value);
        tvBankAccNumberValue = (SFFontTextView) view.findViewById(R.id.tv_bank_account_number_value);
        tvWithdrawCreditValue = (SFFontTextView) view.findViewById(R.id.tv_withdraw_credit_value);
        tvWithdrawMinLimitValue = (SFFontTextView) view.findViewById(R.id.tv_withdraw_min_limit_value);
        tvWithdrawMaxLimitValue = (SFFontTextView) view.findViewById(R.id.tv_withdraw_max_limit_value);

        etWithdrawAmount = (SFFontEditText) view.findViewById(R.id.et_withdraw_amount);

        etWithdrawAmount.setText("0.00" + " " + Preferences.getCurrencySign(getActivity()));

        etPersonalData = (SFFontTextView) view.findViewById(R.id.tv_personal_data);
        tvAccountHolder = (SFFontTextView) view.findViewById(R.id.tv_account_holder);
        tvBankData = (SFFontTextView) view.findViewById(R.id.tv_bank_data);
        tvBankName = (SFFontTextView) view.findViewById(R.id.tv_bank_name);
        tvCountry = (SFFontTextView) view.findViewById(R.id.tv_bank_country);
        tvBankAccNumber = (SFFontTextView) view.findViewById(R.id.tv_bank_account_number);
        tvWithdrawData = (SFFontTextView) view.findViewById(R.id.tv_withdraw_data);
        tvCredit = (SFFontTextView) view.findViewById(R.id.tv_withdraw_credit);
        tvMinWithdrawLimit = (SFFontTextView) view.findViewById(R.id.tv_withdraw_min_limit);
        tvMaxWithdrawLimit = (SFFontTextView) view.findViewById(R.id.tv_withdraw_max_limit);
        tvEnterAmountForWithdraw = (SFFontTextView) view.findViewById(R.id.tv_amount_for_withdraw);
        tvSecurityConfirmation = (SFFontTextView) view.findViewById(R.id.tv_security_confirmation);

        tilWithdrawPassword = (TextInputLayout) view.findViewById(R.id.til_withdraw_password);
        etPassword = (SFFontEditText) view.findViewById(R.id.et_withdraw_password);

        btnConfirm = (Button) view.findViewById(R.id.btn_confirm);
        btnConfirm.setOnClickListener(this);
    }

    /**
     * Method that gets users bank information (bank name, bank country...) by calling BankInfo,
     * Bank and BankCountries services.
     */
    private void getWithdrawInfo() {
        if (NetworkHelper.isNetworkAvailable(getActivity())) {


            rx.Observable<BankInfo> bankInfoRequest = NetworkHelper.getBetXService(AppConstants.WITHDRAW_FUNDS).getBankInfo(Preferences.getLanguage(getActivity()),
                    Preferences.getTerminalId(getActivity()), Preferences.getUserToken(getActivity()))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread());

            rx.Observable<List<Banks>> banksRequest = NetworkHelper.getBetXService(AppConstants.WITHDRAW_FUNDS).getBanks(Preferences.getLanguage(getActivity()),
                    Preferences.getTerminalId(getActivity()), Preferences.getUserToken(getActivity()))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread());

            Observable<List<Countries>> countriesRequest = NetworkHelper.getBetXService(AppConstants.WITHDRAW_FUNDS).getCountries(Preferences.getLanguage(getActivity()),
                    Preferences.getTerminalId(getActivity()), Preferences.getUserToken(getActivity()))
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread());


            Observable<WholeWithdrawData> combinedObservableRequest = rx.Observable.zip(bankInfoRequest, banksRequest, countriesRequest,
                    WholeWithdrawData::new);

            mSubscription = combinedObservableRequest.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<WholeWithdrawData>() {
                        @Override
                        public void onCompleted() {
                            Log.d("WithdrawFundsFragment", "onCompleted: ");
                        }

                        @Override
                        public void onError(Throwable e) {
                            AppLogger.error(TAG, "Error occurred while getting users bank information", e);
                            AppUtils.createToastMessage(getContext(), new Toast(getContext()), "Error occurred while getting users bank information");
                        }

                        @Override
                        public void onNext(WholeWithdrawData wholeWithdrawData) {
                            Log.d("WithdrawFundsFragment", "onNext: ");
                            tvAccHolderValue.setText(wholeWithdrawData.mBankInfo.getAccountName());
                            tvBankAccNumberValue.setText(wholeWithdrawData.mBankInfo.getIban());

                            for (int i = 0; i < wholeWithdrawData.mBanks.size(); i++) {
                                if (wholeWithdrawData.mBankInfo.getBankId().equals(wholeWithdrawData.mBanks.get(i).getId())) {
                                    tvBankNameValue.setText(wholeWithdrawData.mBanks.get(i).getName());
                                }
                            }

                            for (int i = 0; i < wholeWithdrawData.mCountries.size(); i++) {
                                if (wholeWithdrawData.mBankInfo.getBankCountryId().equals(wholeWithdrawData.mCountries.get(i).getId())) {
                                    tvBankCountryValue.setText(wholeWithdrawData.mCountries.get(i).getName());
                                }
                            }
                        }
                    });

        } else {
            // Register receiver to notify when device is connected to internet
            NetworkHelper.showNoConnectionDialog(getActivity());
        }
    }

    /**
     * Method that validates withdraw amount
     */
    private boolean checkIfAmountIsValid() {
        String currency = Preferences.getCurrencySign(getActivity());
        String payAmount = etWithdrawAmount.getText().toString();
        if (payAmount.contains(currency)) {
            payAmount = payAmount.replaceAll(currency, "");
        }

        double minDeposit = mWalletConfiguration.getMinPayout();
        try {
            mPayInValue = Double.parseDouble(payAmount);
            double maxDeposit = mWalletConfiguration.getMaxPayout();
            double credit = mWalletConfiguration.getAvailableBalance();

            String toastMessage;
            if (mPayInValue > credit) {
                if (credit > maxDeposit) {
                    mPayInValue = maxDeposit;
                } else {
                    mPayInValue = credit;
                }
                toastMessage = mNoEnoughCredit;
            } else if (mPayInValue < minDeposit) {
                mPayInValue = minDeposit;
                toastMessage = mLessThanMin;
            } else if (mPayInValue > maxDeposit) {
                mPayInValue = maxDeposit;
                toastMessage = mGreaterThanMax;
            } else {
                etWithdrawAmount.setText("" + nf.format(mPayInValue) + Preferences.getCurrencySign(getActivity()));
                isDataChecked = true;
                return true;
            }

            etWithdrawAmount.setText("" + nf.format(mPayInValue) + Preferences.getCurrencySign(getActivity()));
            Toast.makeText(getActivity(), toastMessage, Toast.LENGTH_SHORT).show();

        } catch (NumberFormatException e) {
            AppLogger.error(TAG, "Exception occured with message: ", e);

            etWithdrawAmount.setText("" + nf.format(minDeposit) + Preferences.getCurrencySign(getActivity()));
            Toast.makeText(getActivity(), mLessThanMin, Toast.LENGTH_SHORT).show();
        }

        return false;

    }

    private void hideKeyboard(SFFontEditText etPayInAmount) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etPayInAmount.getWindowToken(), 0);
    }

    /**
     * Method that initialise warning messages from translations map regarding current application language.
     */
    private void initialiseWarningMessages() {
        if (BetXApplication.translationMap.containsKey(TranslationConstants.THERE_IS_NOT_ENOUGH_CREDIT)) {
            mNoEnoughCredit = BetXApplication.translationMap.get(TranslationConstants.THERE_IS_NOT_ENOUGH_CREDIT);
        } else {
            mNoEnoughCredit = getResources().getString(R.string.no_enough_credit);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.LESS_THAN_MIN_USER_WITHDRAW)) {
            mLessThanMin = BetXApplication.translationMap.get(TranslationConstants.LESS_THAN_MIN_USER_WITHDRAW);
        } else {
            mLessThanMin = getResources().getString(R.string.less_than_min);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.GREATER_THAN_MAX_USER_WITHDRAW)) {
            mGreaterThanMax = BetXApplication.translationMap.get(TranslationConstants.GREATER_THAN_MAX_USER_WITHDRAW);
        } else {
            mGreaterThanMax = getResources().getString(R.string.greater_than_max);
        }
    }


    /**
     * Method for setting up withdraw data
     */
    private void setWithdrawDataValues() {
        if (mWalletConfiguration != null) {
            Double minPayoutDouble = mWalletConfiguration.getMinPayout();
            tvWithdrawMinLimitValue.setText(nf.format(minPayoutDouble) + " " + Preferences.getCurrencySign(getActivity()));

            Double maxPayoutDouble = mWalletConfiguration.getMaxPayout();
            tvWithdrawMaxLimitValue.setText(nf.format(maxPayoutDouble) + " " + Preferences.getCurrencySign(getActivity()));

            Double creditDouble = mWalletConfiguration.getAvailableBalance();
            tvWithdrawCreditValue.setText(nf.format(creditDouble) + " " + Preferences.getCurrencySign(getActivity()));

        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

        switch (v.getId()) {
            case R.id.et_withdraw_amount:
                if (hasFocus) {
                    etWithdrawAmount.getText().clear();
                } else {
                    if (!isDataChecked && !etWithdrawAmount.getText().toString().isEmpty()) {
                        checkIfAmountIsValid();
                    }
                    isDataChecked = false;
                }
                break;
        }

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        switch (v.getId()) {
            case R.id.et_withdraw_amount:
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    hideKeyboard(etWithdrawAmount);
                    if (!etWithdrawAmount.getText().toString().isEmpty()) {
                        checkIfAmountIsValid();
                        isDataChecked = true;
                    }
                    etWithdrawAmount.clearFocus();
                }
                break;
        }
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm:
                if (NetworkHelper.isNetworkAvailable(getActivity())) {
                    boolean passCheck = checkIfPasswordIsValid();
                    if (passCheck) {
                        if (etWithdrawAmount.getText().toString().isEmpty()
                                || etWithdrawAmount.getText().toString().equals("0.00" + " " + Preferences.getCurrencySign(getActivity()))) {
                            Toast.makeText(getActivity(), mEnterAmountForWithdraw, Toast.LENGTH_SHORT).show();
                        } else {
                            sendWithdrawRequest(mPayInValue, etPassword.getText().toString());
                        }
                    }
                } else {
                    NetworkHelper.showNoConnectionDialog(getActivity());
                }
                break;
        }
    }

    /**
     * Method that checks if entered password is true.
     *
     * @return if it's true password is valid, if it's false password is wrong.
     */
    private boolean checkIfPasswordIsValid() {
        final String password = etPassword.getText().toString();
        boolean isPasswordValid = false;
        if (password.equals("")) {
            tilWithdrawPassword.setError(getResources().getString(R.string.login_empty_password));
        } else if (password.equals(Preferences.getPassword(getContext()))) {
            tilWithdrawPassword.setError("");
            isPasswordValid = true;
        } else {
            tilWithdrawPassword.setError(getResources().getString(R.string.withdraw_funds_wrong_password));
            isPasswordValid = false;
        }
        return isPasswordValid;
    }

    /**
     * Method that calls service for withdrawing money.
     *
     * @param amount amount to withdraw,
     * @param pass   users password.
     */
    private void sendWithdrawRequest(double amount, String pass) {
        if (NetworkHelper.isNetworkAvailable(getActivity())) {
            WithdrawBody withdrawBody = new WithdrawBody();
            withdrawBody.setAmount(amount);
            withdrawBody.setPassword(pass);
            /*String token = Preferences.getUserToken(getActivity());
            String terminalId = Preferences.getTerminalId(getActivity());
            String lang = Preferences.getLanguage(getActivity());*/

            mWithdrawMoneyRequest = NetworkHelper.getBetXService(AppConstants.WALLET_API).withdrawMoney(Preferences.getLanguage(getActivity()), Preferences.getUserToken(getActivity()), Preferences.getTerminalId(getActivity()),
                    withdrawBody);

            mWithdrawMoneyRequest.enqueue(new Callback<WithdrawModel>() {
                @Override
                public void onResponse(Call<WithdrawModel> call, Response<WithdrawModel> response) {
                    cancelProgressDialog();
                    btnConfirm.setClickable(true);
                    btnConfirm.setOnClickListener(WithdrawFundsFragment.this);
                    if (getActivity() != null && !getActivity().isFinishing()) {
                        if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                            AppUtils.showDialogUnauthorized(getActivity());
                        } else {
                            if (response.body() != null) {
                                WithdrawModel withdrawModel = response.body();
                                //List<String> messages = withdrawModel.getMessages();

                                if (withdrawModel.getActionResult() && withdrawModel.getMessages().size() == 0) {
                                    AppUtils.showDialogWithMessage(getResources().getString(R.string.withdraw_funds_success), getContext(), isDarkTheme, false);
                                } else if (withdrawModel.getMessages().size() > 0) {
                                    for (int i = 0; i < withdrawModel.getMessages().size(); i++) {
                                        String message = withdrawModel.getMessages().get(i);
                                        if (message.equals(getString(R.string.withdraw_funds_wrong_password))) {

                                            AppUtils.showDialogWithMessage(message, getContext(), isDarkTheme, false);
                                        } /*else if (message.equals("")){
                                    showDialogWithMessage(getResources().getString(R.string.withdraw_funds_success));
                                }*/
                                    }
                                }
                            }
                        }
                    }
                }

                @Override
                public void onFailure(Call<WithdrawModel> call, Throwable t) {
                    AppLogger.error(TAG, "Exception occured with message: ", t);
                    if (getActivity() != null && !getActivity().isFinishing()) {
                        int message = NetworkHelper.generateErrorMessage(t);
                        AppUtils.createToastMessage(getActivity(), new Toast(getActivity()), getString(message));
                        cancelProgressDialog();
                        btnConfirm.setClickable(true);
                        btnConfirm.setOnClickListener(WithdrawFundsFragment.this);
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
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

            btnConfirm.setClickable(false);
            btnConfirm.setOnClickListener(null);
        } else {
            // Register receiver to notify when device is connected to internet
            NetworkHelper.showNoConnectionDialog(getActivity());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        cancelProgressDialog();

        Log.d("WithdrawFundsFragment", "onDestroy: ");
        if (mWithdrawMoneyRequest != null) {
            mWithdrawMoneyRequest.cancel();
        }
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            Log.d("WithdrawFundsFragment", "mSubscription.unsubscribe(): ");
            mSubscription.unsubscribe();
        }
    }

    private void cancelProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
