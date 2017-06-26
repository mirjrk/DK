package com.zesium.android.betting.model.forgot_password;

import java.util.List;

/**
 * Created by savic on 04-Jul-16.
 */
public class ForgottenPasswordQuestion {
    private int SecurityQuestionId;
    private String Username;
    private Boolean IsSuccess;
    private List<String> ValidationResults;
    private Boolean ActionResult;
    private List<String> Messages;

    public int getSecurityQuestionId() {
        return SecurityQuestionId;
    }

    public void setSecurityQuestionId(int securityQuestionId) {
        SecurityQuestionId = securityQuestionId;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
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
