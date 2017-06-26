package com.zesium.android.betting.model.payment;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by savic on 30-Mar-17.
 */

public class WithdrawModel {
    @SerializedName("IsSuccess")
    @Expose
    private Boolean isSuccess;
    @SerializedName("ValidationResults")
    @Expose
    private List<Object> validationResults = null;
    @SerializedName("ActionResult")
    @Expose
    private Boolean actionResult;
    @SerializedName("Messages")
    @Expose
    private List<String> messages = null;

    public Boolean getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(Boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    public List<Object> getValidationResults() {
        return validationResults;
    }

    public void setValidationResults(List<Object> validationResults) {
        this.validationResults = validationResults;
    }

    public Boolean getActionResult() {
        return actionResult;
    }

    public void setActionResult(Boolean actionResult) {
        this.actionResult = actionResult;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }
}
