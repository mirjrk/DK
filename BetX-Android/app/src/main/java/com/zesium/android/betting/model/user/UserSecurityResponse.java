package com.zesium.android.betting.model.user;

import java.util.List;

/**
 * Created by Ivan Panic_2 on 10/21/2016.
 */
public class UserSecurityResponse {
    private boolean IsSuccess;
    private List<String> Messages;

    public boolean isSuccess() {
        return IsSuccess;
    }

    public void setSuccess(boolean success) {
        IsSuccess = success;
    }

    public List<String> getMessages() {
        return Messages;
    }

    public void setMessages(List<String> messages) {
        Messages = messages;
    }
}
