package com.zesium.android.betting.model.payment;

import java.util.List;

/**
 * Created by savic on 31-Mar-17.
 */

public class DepositCallModel {

    private String RedirectUrl;
    private String PayToAccountData;
    private Boolean IsSuccess;
    private List<String> ValidationResults;
    private Boolean ActionResult;
    private List<String> Messages;

    public String getRedirectUrl() {
        return RedirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        RedirectUrl = redirectUrl;
    }

    public String getPayToAccountData() {
        return PayToAccountData;
    }

    public void setPayToAccountData(String payToAccountData) {
        PayToAccountData = payToAccountData;
    }

    public Boolean getSuccess() {
        return IsSuccess;
    }

    public void setSuccess(Boolean success) {
        IsSuccess = success;
    }

    public List<String> getValidationResults() {
        return ValidationResults;
    }

    public void setValidationResults(List<String> validationResults) {
        ValidationResults = validationResults;
    }

    public Boolean getActionResult() {
        return ActionResult;
    }

    public void setActionResult(Boolean actionResult) {
        ActionResult = actionResult;
    }

    public List<String> getMessages() {
        return Messages;
    }

    public void setMessages(List<String> messages) {
        Messages = messages;
    }
}
