package com.zesium.android.betting.model.user;

/**
 * Created by Ivan Panic_2 on 6/30/2016.
 */
public class BetXValidationResult {
    private String ErrorMessage;
    private String PropertyName;

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }

    public String getPropertyName() {
        return PropertyName;
    }

    public void setPropertyName(String propertyName) {
        this.PropertyName = propertyName;
    }
}
