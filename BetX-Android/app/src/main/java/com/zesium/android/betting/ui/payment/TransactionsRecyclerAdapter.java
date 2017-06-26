package com.zesium.android.betting.ui.payment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zesium.android.betting.R;
import com.zesium.android.betting.model.payment.MoneyTransaction;
import com.zesium.android.betting.BetXApplication;
import com.zesium.android.betting.model.util.DateAndTimeHelper;
import com.zesium.android.betting.utils.Preferences;
import com.zesium.android.betting.ui.widgets.SFFontTextView;
import com.zesium.android.betting.model.util.TranslationConstants;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Ivan Panic_2 on 2/27/2017.
 */

class TransactionsRecyclerAdapter extends RecyclerView.Adapter<TransactionsRecyclerAdapter.DataObjectHolder> {
    private final Context mContext;
    private final ArrayList<MoneyTransaction> mTransactions;
    private String mAmount;
    private String mDateAndTime;
    private String mNewBalance;
    private final NumberFormat nf;

    class DataObjectHolder extends RecyclerView.ViewHolder {
        final SFFontTextView tvDate;
        final SFFontTextView tvType;
        final SFFontTextView tvAmount;
        final SFFontTextView tvNewBalance;
        final SFFontTextView tvDateTitle;
        final SFFontTextView tvAmountTitle;
        final SFFontTextView tvNewBalanceTitle;

        DataObjectHolder(View itemView) {
            super(itemView);
            tvDate = (SFFontTextView) itemView.findViewById(R.id.tv_date);
            tvType = (SFFontTextView) itemView.findViewById(R.id.tv_type);
            tvAmount = (SFFontTextView) itemView.findViewById(R.id.tv_amount);
            tvNewBalance = (SFFontTextView) itemView.findViewById(R.id.tv_new_balance);

            tvDateTitle = (SFFontTextView) itemView.findViewById(R.id.tv_date_title);
            tvAmountTitle = (SFFontTextView) itemView.findViewById(R.id.tv_amount_title);
            tvNewBalanceTitle = (SFFontTextView) itemView.findViewById(R.id.tv_new_balance_title);

            tvDateTitle.setText(mDateAndTime);
            tvAmountTitle.setText(mAmount);
            tvNewBalanceTitle.setText(mNewBalance);
        }
    }

    TransactionsRecyclerAdapter(Context context, ArrayList<MoneyTransaction> moneyTransactions) {
        mContext = context;
        mTransactions = moneyTransactions;
        nf = NumberFormat.getNumberInstance(Locale.GERMAN);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        nf.setGroupingUsed(true);
        setTitleTextValues();
    }

    private void setTitleTextValues() {
        if (BetXApplication.translationMap.containsKey(TranslationConstants.AMOUNT)) {
            mAmount = BetXApplication.translationMap.get(TranslationConstants.AMOUNT);
        } else {
            mAmount = mContext.getString(R.string.transactions_amount_title);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.DATE_TIME)) {
            mDateAndTime = BetXApplication.translationMap.get(TranslationConstants.DATE_TIME);
        } else {
            mDateAndTime = mContext.getString(R.string.ticket_date_and_time);
        }

        if (BetXApplication.translationMap.containsKey(TranslationConstants.NEW_BALANCE)) {
            mNewBalance = BetXApplication.translationMap.get(TranslationConstants.NEW_BALANCE);
        } else {
            mNewBalance = mContext.getString(R.string.transactions_new_balance_title);
        }
    }

    @Override
    public TransactionsRecyclerAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_transaction, parent, false);

        return new TransactionsRecyclerAdapter.DataObjectHolder(v);

    }

    @Override
    public void onBindViewHolder(TransactionsRecyclerAdapter.DataObjectHolder holder, int position) {
        MoneyTransaction transaction = mTransactions.get(position);
        holder.tvType.setText(transaction.getType());

        String amount = String.valueOf(nf.format(transaction.getAmount())) + Preferences.getCurrencySign(mContext);
        holder.tvAmount.setText(amount);

        String newBalance = String.valueOf(nf.format(transaction.getNewCredit())) + Preferences.getCurrencySign(mContext);
        holder.tvNewBalance.setText(newBalance);

        String dateAndTime = DateAndTimeHelper.changeDateFormatWithTime(transaction.getDateTime());
        holder.tvDate.setText(dateAndTime);
    }

    @Override
    public int getItemCount() {
        return mTransactions.size();
    }
}
