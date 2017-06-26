package com.zesium.android.betting.model.user;

/**
 * Created by Ivan Panic_2 on 10/21/2016.
 */
public class BaseUserSecurityModel {
    private int SecurityQuestionId;
    private String SecurityAnswer;

    public int getSecurityQuestionId() {
        return SecurityQuestionId;
    }

    public void setSecurityQuestionId(int securityQuestionId) {
        SecurityQuestionId = securityQuestionId;
    }

    public String getSecurityAnswer() {
        return SecurityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        SecurityAnswer = securityAnswer;
    }
}
