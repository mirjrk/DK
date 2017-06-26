package com.zesium.android.betting.model.user;

/**
 * Created by Ivan Panic_2 on 10/21/2016.
 */
public class UserPasswordChangeModel {
    private String OldPassword;
    private String NewPassword;

    public String getOldPassword() {
        return OldPassword;
    }

    public void setOldPassword(String oldPassword) {
        OldPassword = oldPassword;
    }

    public String getNewPassword() {
        return NewPassword;
    }

    public void setNewPassword(String newPassword) {
        NewPassword = newPassword;
    }
}
