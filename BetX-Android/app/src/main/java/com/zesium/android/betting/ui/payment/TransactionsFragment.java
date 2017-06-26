package com.zesium.android.betting.ui.payment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.zesium.android.betting.R;
import com.zesium.android.betting.model.payment.ActionBaseResultWithObjectResult;
import com.zesium.android.betting.model.payment.MoneyTransaction;
import com.zesium.android.betting.model.payment.MoneyTransactionsRequest;
import com.zesium.android.betting.utils.AppConstants;
import com.zesium.android.betting.utils.AppLogger;
import com.zesium.android.betting.service.NetworkHelper;
import com.zesium.android.betting.utils.Preferences;
import com.zesium.android.betting.utils.AppUtils;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionsFragment extends Fragment {

    private static final String TAG = TransactionsFragment.class.getSimpleName();
    private RecyclerView.Adapter mAdapter;
    private ArrayList<MoneyTransaction> mMoneyTransactions;
    private Call<ActionBaseResultWithObjectResult> getMoneyTransactionRequest;

    public TransactionsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_transactions, container, false);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_banks);
        recyclerView.setLayoutManager(layoutManager);

        mMoneyTransactions = new ArrayList<>();
        mAdapter = new TransactionsRecyclerAdapter(getActivity(), mMoneyTransactions);
        recyclerView.setAdapter(mAdapter);

        getMoneyTransactions();

        return rootView;
    }

    private void getMoneyTransactions() {
        MoneyTransactionsRequest moneyTransactionsRequest = new MoneyTransactionsRequest();
        moneyTransactionsRequest.setLimit(20);
        moneyTransactionsRequest.setOffset(0);
        moneyTransactionsRequest.setTimeFilter(-7);
        moneyTransactionsRequest.setYearFilter(2017);
        moneyTransactionsRequest.setTransactionType(0);
        moneyTransactionsRequest.setWebUsername("");

        getMoneyTransactionRequest = NetworkHelper.getBetXService(AppConstants.WALLET_API).getMoneyTransactions(Preferences.getUserToken(getActivity()),
                Preferences.getTerminalId(getActivity()), Preferences.getLanguage(getActivity()), moneyTransactionsRequest);


        getMoneyTransactionRequest.enqueue(new Callback<ActionBaseResultWithObjectResult>() {

            @Override
            public void onResponse(Call<ActionBaseResultWithObjectResult> call, Response<ActionBaseResultWithObjectResult> response) {
                if (getActivity() != null && !getActivity().isFinishing()) {
                    if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        AppUtils.showDialogUnauthorized(getActivity());
                    } else {
                        if (response.body() != null) {
                            parseTransactions(response.body());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ActionBaseResultWithObjectResult> call, Throwable t) {
                AppLogger.error(TAG, "Exception occurred with message: ", t);
                if (getActivity() != null && !getActivity().isFinishing()) {
                    int message = NetworkHelper.generateErrorMessage(t);
                    AppUtils.createToastMessage(getActivity(), new Toast(getActivity()), getString(message));
                }
            }
        });
    }

    private void parseTransactions(ActionBaseResultWithObjectResult body) {
        Gson gson = new Gson();
        JsonArray transactions = body.getResult().getAsJsonArray(getActivity().getResources().getString(R.string.money_transactions_json));
        if (body.getResult() != null && transactions.size() > 0) {
            for (JsonElement bank : transactions) {
                MoneyTransaction transaction = gson.fromJson(bank, MoneyTransaction.class);
                mMoneyTransactions.add(transaction);
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (getMoneyTransactionRequest != null) {
            getMoneyTransactionRequest.cancel();
        }
    }
}
