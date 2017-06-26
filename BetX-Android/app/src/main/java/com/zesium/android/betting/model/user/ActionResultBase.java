package com.zesium.android.betting.model.user;

import java.util.List;

/**
 * Created by Ivan Panic_2 on 6/30/2016.
 */
public class ActionResultBase {
    private boolean IsSuccess;
    private List<BetXValidationResult> ValidationResults;
    private boolean ActionResult;
    private List<String> Messages;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public void setSuccess(boolean success) {
        IsSuccess = success;
    }

    public List<BetXValidationResult> getValidationResults() {
        return ValidationResults;
    }

    public void setValidationResults(List<BetXValidationResult> validationResults) {
        ValidationResults = validationResults;
    }

    public boolean isActionResult() {
        return ActionResult;
    }

    public void setActionResult(boolean actionResult) {
        ActionResult = actionResult;
    }

    public List<String> getMessages() {
        return Messages;
    }

    public void setMessages(List<String> messages) {
        Messages = messages;
    }
}
