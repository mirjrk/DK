package com.zesium.android.betting.ui.payment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zesium.android.betting.R;
import com.zesium.android.betting.model.payment.DepositCallModel;
import com.zesium.android.betting.model.payment.PaymentProvider;
import com.zesium.android.betting.model.payment.PaymentProvidersResponse;
import com.zesium.android.betting.utils.AppConstants;
import com.zesium.android.betting.utils.AppLogger;
import com.zesium.android.betting.BetXApplication;
import com.zesium.android.betting.service.NetworkHelper;
import com.zesium.android.betting.utils.Preferences;
import com.zesium.android.betting.model.util.TranslationConstants;
import com.zesium.android.betting.utils.AppUtils;
import com.zesium.android.betting.ui.widgets.SFFontEditText;
import com.zesium.android.betting.ui.widgets.SendBankData;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DepositMoneyFragment extends Fragment implements PaymentProvidersRecyclerAdapter.OnProviderSelectedListener, View.OnClickListener, SendBankData {
    private static final String TAG = DepositMoneyFragment.class.getSimpleName();
    private final ArrayList<PaymentProvider> mPaymentProviders = new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private PaymentProvider mSelectedPaymentProvider;
    private SFFontEditText etPaymentValue;
    private LinearLayout rlPayInData;
    private boolean isDataChecked = false;
    private NumberFormat nf;
    private int mProviderId;
    private double enteredAmount;
    private boolean darkTheme;
    private ProgressBar mProgressBar;
    private RelativeLayout rlProgressBar;
    private LinearLayout llBanks;
    private BroadcastReceiver mBroadcastReceiver;
    private IntentFilter intentFilter;
    private boolean isBroadcastRegistered;
    private Call<PaymentProvidersResponse> depositProvidersRequest;
    private Call<DepositCallModel> depositMoneyRequest;

    public DepositMoneyFragment() {
        // Required empty public constructor
    }

    public static DepositMoneyFragment newInstance() {
        DepositMoneyFragment fragment = new DepositMoneyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_deposit_money, container, false);

        darkTheme = Preferences.getTheme(getActivity());

        llBanks = (LinearLayout) rootView.findViewById(R.id.ll_banks_layout);
        llBanks.setVisibility(View.VISIBLE);
        rlProgressBar = (RelativeLayout) rootView.findViewById(R.id.rl_spinner_layout);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar_deposit);
        mProgressBar.getIndeterminateDrawable().setColorFilter(
                ContextCompat.getColor(getContext(), R.color.primary_red),
                android.graphics.PorterDuff.Mode.SRC_IN);

        rlPayInData = (LinearLayout) rootView.findViewById(R.id.rl_pay_in_amount);
        Button btnSendDeposit = (Button) rootView.findViewById(R.id.btn_send_deposit);
        btnSendDeposit.setOnClickListener(this);
        etPaymentValue = (SFFontEditText) rootView.findViewById(R.id.et_pay_in_amount);
        if (Preferences.getTheme(getActivity())) {
            etPaymentValue.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bcg_pay_in_dark));
        } else {
            etPaymentValue.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bcg_pay_in_light));
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_banks);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new PaymentProvidersRecyclerAdapter(mPaymentProviders, this, this);
        recyclerView.setAdapter(mAdapter);

        nf = NumberFormat.getNumberInstance(Locale.GERMAN);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        nf.setGroupingUsed(true);

        getDepositProviders();

        return rootView;
    }

    @Override
    public void onStart() {
        // Initialise broadcast receiver to receive changes from other tab
        if (!isBroadcastRegistered) {
            try {
                initialiseBroadcastReceiver();
                getActivity().registerReceiver(mBroadcastReceiver, intentFilter);
                isBroadcastRegistered = true;
            } catch (Exception e) {
                AppLogger.error(TAG, "Exception occured with message: ", e);
            }
        }
        super.onStart();
    }

    @Override
    public void onResume() {
        llBanks.setVisibility(View.VISIBLE);
        rlProgressBar.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.GONE);
        super.onResume();
    }

    @Override
    public void onStop() {
        if (isBroadcastRegistered) {
            try {
                getActivity().unregisterReceiver(mBroadcastReceiver);
                isBroadcastRegistered = false;
            } catch (Exception e) {
                AppLogger.error(TAG, "Exception occurred with message: ", e);
            }
        }
        super.onStop();
    }

    private boolean checkIfAmountIsValid() {
        boolean isValid = false;
        String currency = Preferences.getCurrencySign(getActivity());
        String payAmount = etPaymentValue.getText().toString();
        if (payAmount.contains(currency)) {
            payAmount = payAmount.replaceAll(currency, "");
        }

        String minTransactionForProvider;
        double minDeposit = mSelectedPaymentProvider.getMinDeposit();
        if (BetXApplication.translationMap.containsKey(TranslationConstants.MIN_TRANSACTION_FOR_THIS_PROVIDER_IS)) {
            minTransactionForProvider = BetXApplication.translationMap.get(TranslationConstants.MIN_TRANSACTION_FOR_THIS_PROVIDER_IS);
        } else {
            minTransactionForProvider = getResources().getString(R.string.min_transaction_for_provider);
        }

        try {
            double payInValue = Double.parseDouble(payAmount);
            double maxDeposit = mSelectedPaymentProvider.getMaxDeposit();

            if (payInValue < minDeposit) {
                payInValue = minDeposit;

                etPaymentValue.setText("" + payInValue + Preferences.getCurrencySign(getActivity()));
                Toast.makeText(getActivity(), minTransactionForProvider + ": " + nf.format(minDeposit) + " " + Preferences.getCurrencySign(getActivity()), Toast.LENGTH_SHORT).show();
                isValid = false;
            } else if (payInValue > maxDeposit) {
                payInValue = maxDeposit;
                String maxTransactionForProvider;
                if (BetXApplication.translationMap.containsKey(TranslationConstants.MAX_TRANSACTION_FOR_THIS_PROVIDER_IS)) {
                    maxTransactionForProvider = BetXApplication.translationMap.get(TranslationConstants.MAX_TRANSACTION_FOR_THIS_PROVIDER_IS);
                } else {
                    maxTransactionForProvider = getResources().getString(R.string.max_transaction_for_provider);
                }
                etPaymentValue.setText("" + payInValue + Preferences.getCurrencySign(getActivity()));
                Toast.makeText(getActivity(), maxTransactionForProvider + ": " + nf.format(maxDeposit) + " " + Preferences.getCurrencySign(getActivity()), Toast.LENGTH_SHORT).show();
                isValid = false;
            } else {
                String payInValueString = payInValue + Preferences.getCurrencySign(getActivity());
                etPaymentValue.setText(payInValueString);
                isValid = true;
            }
        } catch (NumberFormatException e) {
            AppLogger.error(TAG, "Exception occurred with message: ", e);
            Toast.makeText(getActivity(), minTransactionForProvider + ": " + nf.format(minDeposit) + " " + Preferences.getCurrencySign(getActivity()), Toast.LENGTH_SHORT).show();
            etPaymentValue.setText("" + minDeposit + Preferences.getCurrencySign(getActivity()));
        }
        return isValid;
    }

    private void getDepositProviders() {
        depositProvidersRequest = NetworkHelper.getBetXService(AppConstants.WALLET_API).getDepositProviders(Preferences.getUserToken(getActivity()),
                Preferences.getTerminalId(getActivity()), Preferences.getLanguage(getActivity()));
        depositProvidersRequest.enqueue(new Callback<PaymentProvidersResponse>() {
            @Override
            public void onResponse(Call<PaymentProvidersResponse> call, Response<PaymentProvidersResponse> response) {
                if (getActivity() != null && !getActivity().isFinishing() && response.body() != null) {
                    if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        AppUtils.showDialogUnauthorized(getActivity());
                    } else {
                        mPaymentProviders.addAll(response.body().getPaymentProviders());
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<PaymentProvidersResponse> call, Throwable t) {
                AppLogger.error(TAG, "Exception occurred with message: ", t);
                if (getActivity() != null && !getActivity().isFinishing()) {
                    int message = NetworkHelper.generateErrorMessage(t);
                    AppUtils.createToastMessage(getActivity(), new Toast(getActivity()), getString(message));
                }
            }
        });
    }


    @Override
    public void onProviderSelected(PaymentProvider paymentProviderProvider) {
        mSelectedPaymentProvider = paymentProviderProvider;

        etPaymentValue.setText("" + paymentProviderProvider.getMinDeposit() + Preferences.getCurrencySign(getActivity()));
        rlPayInData.setVisibility(View.VISIBLE);

        etPaymentValue.setOnFocusChangeListener((view, b) -> {
            if (b) {
                etPaymentValue.getText().clear();
            } else {
                if (!isDataChecked && !etPaymentValue.getText().toString().isEmpty()) {
                    checkIfAmountIsValid();
                }
                isDataChecked = false;
            }
        });

        etPaymentValue.setOnEditorActionListener((textView, i, keyEvent) -> {
                    if (i == EditorInfo.IME_ACTION_DONE) {
                        hideKeyboard(etPaymentValue);
                        if (!etPaymentValue.getText().toString().isEmpty()) {
                            checkIfAmountIsValid();
                            isDataChecked = true;
                        }
                        etPaymentValue.clearFocus();
                    }
                    return false;
                }
        );
    }

    private void hideKeyboard(SFFontEditText etPayInAmount) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etPayInAmount.getWindowToken(), 0);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send_deposit:
                if (checkIfAmountIsValid()) {
                    if (NetworkHelper.isNetworkAvailable(getActivity())) {
                        llBanks.setVisibility(View.GONE);
                        rlProgressBar.setVisibility(View.VISIBLE);
                        mProgressBar.setVisibility(View.VISIBLE);
                        sendDepositRequest();
                    } else {
                        // Register receiver to notify when device is connected to internet
                        NetworkHelper.showNoConnectionDialog(getActivity());
                    }
                }
                break;
        }
    }

    /**
     * Method that calls service for depositing money.
     */
    private void sendDepositRequest() {
        String payAmount = etPaymentValue.getText().toString();
        if (payAmount.length() > 1) {
            if (payAmount.endsWith(Preferences.getCurrencySign(getActivity()))) {
                payAmount = StringUtils.removeEnd(payAmount, Preferences.getCurrencySign(getActivity()));
                enteredAmount = Double.valueOf(payAmount);
            }
        }
        depositMoneyRequest = NetworkHelper.getBetXService(AppConstants.WALLET_API).depositMoney(Preferences.getLanguage(getActivity()), Preferences.getUserToken(getActivity()),
                Preferences.getTerminalId(getActivity()), mProviderId, enteredAmount, false, 0);

        depositMoneyRequest.enqueue(new Callback<DepositCallModel>() {
            @Override
            public void onResponse(Call<DepositCallModel> call, Response<DepositCallModel> response) {
                if (getActivity() != null && !getActivity().isFinishing()) {
                    if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        AppUtils.showDialogUnauthorized(getActivity());
                    } else {
                        if (response.body() != null) {
                            DepositCallModel depositCallModel = response.body();
                            if (depositCallModel.getRedirectUrl() != null && !depositCallModel.getRedirectUrl().isEmpty()) {
                                rlProgressBar.setVisibility(View.GONE);
                                mProgressBar.setVisibility(View.GONE);
                                Intent intent = new Intent(getActivity(), DepositBankPaymentActivity.class);
                                intent.setData(Uri.parse(depositCallModel.getRedirectUrl()));
                                startActivity(intent);
                            } else if (depositCallModel.getMessages().size() > 0) {
                                for (int i = 0; i < depositCallModel.getMessages().size(); i++) {
                                    String message = depositCallModel.getMessages().get(0);
                                    llBanks.setVisibility(View.VISIBLE);
                                    rlProgressBar.setVisibility(View.GONE);
                                    mProgressBar.setVisibility(View.GONE);
                                    AppUtils.showDialogWithMessage(message, getContext(), darkTheme, true);
                                }
                            }
                        } else if (response.body() == null && response.errorBody() != null) {
                            try {
                                JSONObject jObjError;
                                jObjError = new JSONObject(response.errorBody().string());
                                String errorMessage = jObjError.getString("Message");
                                AppUtils.showDialogWithMessage(errorMessage, getContext(), darkTheme, true);
                            } catch (JSONException e) {
                                AppLogger.error(TAG, "Error occurred while parsing data", e);
                            } catch (IOException e) {
                                AppLogger.error(TAG, getString(R.string.no_internet_connection_message), e);
                            }

                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<DepositCallModel> call, Throwable t) {
                AppUtils.showDialogWithMessage(getResources().getString(R.string.login_server_error), getContext(), darkTheme, true);
                llBanks.setVisibility(View.VISIBLE);
                rlProgressBar.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
                AppLogger.error(TAG, "Exception occurred with message: ", t);
            }
        });
    }

    @Override
    public void sendBankData(int providerId) {
        mProviderId = providerId;
    }

    /**
     * Method initialiseBroadcastReceiver initialises broadcast receiver so that each tab can update its
     * bets depending on bets that are selected/unselected in second tab.
     */
    private void initialiseBroadcastReceiver() {
        intentFilter = new IntentFilter(AppConstants.BROADCAST_BANKS_LIST);
        mBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(AppConstants.BROADCAST_BANKS_LIST)) {
                    try {
                        Bundle extras = intent.getExtras();
                        if (extras != null) {
                            if (extras.getBoolean(AppConstants.BANK_LIST)) {
                                llBanks.setVisibility(View.VISIBLE);
                                rlProgressBar.setVisibility(View.GONE);
                                mProgressBar.setVisibility(View.GONE);
                            }

                        }
                    } catch (Exception e) {
                        AppLogger.error(TAG, "Exception occurred with message: ", e);
                    }
                }
            }
        };
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (depositProvidersRequest != null) {
            depositProvidersRequest.cancel();
        }
        if (depositMoneyRequest != null) {
            depositMoneyRequest.cancel();
        }
    }
}
