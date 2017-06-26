package com.zesium.android.betting.model.ticket;

/**
 * Created by savic on 28-Sep-16.
 */

class TicketDetailsModel {
    private String EncryptedPin;
    //private int SystemCombinationsType;
    private SystemCombinationsType SystemCombinationsType;

    public String getEncryptedPin() {
        return EncryptedPin;
    }

    public void setEncryptedPin(String encryptedPin) {
        EncryptedPin = encryptedPin;
    }

    public SystemCombinationsType getSystemCombinationsType() {
        return SystemCombinationsType;
    }

    public void setSystemCombinationsType(SystemCombinationsType systemCombinationsType) {
        SystemCombinationsType = systemCombinationsType;
    }
}
