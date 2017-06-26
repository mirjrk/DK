package com.zesium.android.betting.model.payment;

import java.util.List;

public class WholeWithdrawData {
    public final BankInfo mBankInfo;
    public final List<Banks> mBanks;
    public final List<Countries> mCountries;

    public WholeWithdrawData(BankInfo bankInfo, List<Banks> banks, List<Countries> countries) {
        mBankInfo = bankInfo;
        mBanks = banks;
        mCountries = countries;
    }
}
