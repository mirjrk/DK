package com.zesium.android.betting.model.forgot_password;

/**
 * Created by savic on 04-Jul-16.
 */

public class ForgottenPasswordAnswerMobileModel {

    private String SecurityAnswer;
    private String Username;

    public String getSecurityAnswer() {
        return SecurityAnswer;
    }

    public void setSecurityAnswer(String securityAnswer) {
        SecurityAnswer = securityAnswer;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }
}