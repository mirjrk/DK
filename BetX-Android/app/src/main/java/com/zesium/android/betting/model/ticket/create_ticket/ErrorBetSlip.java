package com.zesium.android.betting.model.ticket.create_ticket;

/**
 * Created by Ivan Panic_2 on 2/9/2017.
 */
class ErrorBetSlip {
    private int ErrorType;
    private EBetSlipMessageType MessageType;
    private String TranslatedMessage;
    private String AuthorizationMessage;
    private String CorrectionValue;

    public int getErrorType() {
        return ErrorType;
    }

    public void setErrorType(int errorType) {
        ErrorType = errorType;
    }

    public EBetSlipMessageType getMessageType() {
        return MessageType;
    }

    public void setMessageType(EBetSlipMessageType messageType) {
        MessageType = messageType;
    }

    public String getTranslatedMessage() {
        return TranslatedMessage;
    }

    public void setTranslatedMessage(String translatedMessage) {
        TranslatedMessage = translatedMessage;
    }

    public String getAuthorizationMessage() {
        return AuthorizationMessage;
    }

    public void setAuthorizationMessage(String authorizationMessage) {
        AuthorizationMessage = authorizationMessage;
    }

    public String getCorrectionValue() {
        return CorrectionValue;
    }

    public void setCorrectionValue(String correctionValue) {
        CorrectionValue = correctionValue;
    }
}
