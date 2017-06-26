package com.zesium.android.betting.model.payment;

import java.util.List;

/**
 * Created by Ivan Panic_2 on 2/27/2017.
 */

public class PaymentProvidersResponse {
    private boolean IsSuccess;
    private List<PaymentProvider> PaymentProviders;
    private boolean ActionResult;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public void setSuccess(boolean success) {
        IsSuccess = success;
    }

    public List<PaymentProvider> getPaymentProviders() {
        return PaymentProviders;
    }

    public void setPaymentProviders(List<PaymentProvider> paymentProviders) {
        PaymentProviders = paymentProviders;
    }

    public boolean isActionResult() {
        return ActionResult;
    }

    public void setActionResult(boolean actionResult) {
        ActionResult = actionResult;
    }
}
