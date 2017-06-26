package com.zesium.android.betting.ui.payment;

import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.zesium.android.betting.R;
import com.zesium.android.betting.model.payment.PaymentProvider;
import com.zesium.android.betting.ui.widgets.SFFontTextView;
import com.zesium.android.betting.ui.widgets.SendBankData;

import java.util.ArrayList;

/**
 * Created by Ivan Panic_2 on 2/27/2017.
 */

class PaymentProvidersRecyclerAdapter extends RecyclerView.Adapter<PaymentProvidersRecyclerAdapter.DataObjectHolder> {
    private final ArrayList<PaymentProvider> mPaymentProviders;
    private int lastSelected = -1;
    private final OnProviderSelectedListener mOnProviderSelectedListener;
    private AppCompatRadioButton mLastChecked;
    private final SendBankData mSendBankData;

    class DataObjectHolder extends RecyclerView.ViewHolder {
        final SFFontTextView tvBankName;
        final SFFontTextView tvAllowedPayInValue;
        final SFFontTextView tvBankDescription;
        final AppCompatRadioButton rbChecked;
        final RelativeLayout rlPaymentProvider;

        DataObjectHolder(View itemView) {
            super(itemView);
            rlPaymentProvider = (RelativeLayout) itemView.findViewById(R.id.rl_payment_provider);
            tvBankName = (SFFontTextView) itemView.findViewById(R.id.tv_bank_name);
            tvAllowedPayInValue = (SFFontTextView) itemView.findViewById(R.id.tv_allowed_amount_of_money);
            tvBankDescription = (SFFontTextView) itemView.findViewById(R.id.tv_bank_description);
            rbChecked = (AppCompatRadioButton) itemView.findViewById(R.id.rb_selected_bank);
        }
    }

    PaymentProvidersRecyclerAdapter(ArrayList<PaymentProvider> paymentProviders, OnProviderSelectedListener listener, SendBankData sendBankData) {
        mPaymentProviders = paymentProviders;
        mOnProviderSelectedListener = listener;
        mSendBankData = sendBankData;
    }

    @Override
    public PaymentProvidersRecyclerAdapter.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_bank, parent, false);

        return new PaymentProvidersRecyclerAdapter.DataObjectHolder(v);
    }

    @Override
    public void onBindViewHolder(PaymentProvidersRecyclerAdapter.DataObjectHolder holder, int position) {
        PaymentProvider provider = mPaymentProviders.get(position);
        holder.tvBankName.setText(provider.getName());

        int min = (int) provider.getMinDeposit();
        int max = (int) provider.getMaxDeposit();

        String allowedPayIn = "(" + min + " - " + max + ")";
        holder.tvAllowedPayInValue.setText(allowedPayIn);

        if (provider.getDescription() != null && !provider.getDescription().isEmpty()) {
            holder.tvBankDescription.setVisibility(View.VISIBLE);
            holder.tvBankDescription.setText(provider.getDescription());
        } else {
            holder.tvBankDescription.setVisibility(View.GONE);
        }

        holder.rbChecked.setOnCheckedChangeListener(null);
        holder.rbChecked.setChecked(provider.isSelected());


        holder.rlPaymentProvider.setOnClickListener(v -> {
            AppCompatRadioButton clicked = (AppCompatRadioButton) v.findViewById(R.id.rb_selected_bank);
            int clickedPos = holder.getAdapterPosition();
            clicked.setChecked(true);


            if (clicked.isChecked()) {
                if (mLastChecked != null && lastSelected != clickedPos) {
                    mLastChecked.setChecked(false);
                    PaymentProvider lastSelectedProvider = mPaymentProviders.get(lastSelected);
                    lastSelectedProvider.setSelected(false);
                }

                mLastChecked = clicked;
                lastSelected = clickedPos;

                mOnProviderSelectedListener.onProviderSelected(provider);
                mSendBankData.sendBankData(provider.getProviderId());
            } else {
                mLastChecked = null;
            }

            mPaymentProviders.get(clickedPos).setSelected(clicked.isSelected());

        });
    }

    @Override
    public int getItemCount() {
        return mPaymentProviders.size();
    }

    interface OnProviderSelectedListener {
        void onProviderSelected(PaymentProvider paymentProviderProvider);
    }
}
