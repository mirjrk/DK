package com.zesium.android.betting.model.payment;

/**
 * Created by Ivan Panic_2 on 2/27/2017.
 */

public class MoneyTransactionsRequest {
    private int Limit;
    private int Offset;
    private int YearFilter;
    private int TimeFilter;
    private int TransactionType;
    private String WebUsername;

    public int getLimit() {
        return Limit;
    }

    public void setLimit(int limit) {
        Limit = limit;
    }

    public int getOffset() {
        return Offset;
    }

    public void setOffset(int offset) {
        Offset = offset;
    }

    public int getYearFilter() {
        return YearFilter;
    }

    public void setYearFilter(int yearFilter) {
        YearFilter = yearFilter;
    }

    public int getTimeFilter() {
        return TimeFilter;
    }

    public void setTimeFilter(int timeFilter) {
        TimeFilter = timeFilter;
    }

    public int getTransactionType() {
        return TransactionType;
    }

    public void setTransactionType(int transactionType) {
        TransactionType = transactionType;
    }

    public String getWebUsername() {
        return WebUsername;
    }

    public void setWebUsername(String webUsername) {
        WebUsername = webUsername;
    }
}
