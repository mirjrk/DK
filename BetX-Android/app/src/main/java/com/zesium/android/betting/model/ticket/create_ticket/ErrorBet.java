package com.zesium.android.betting.model.ticket.create_ticket;

/**
 * Created by Ivan Panic_2 on 2/9/2017.
 */
class ErrorBet {
    private EBetErrorType ErrorType;
    private String Error;
    private String TranslatedMessage;
    private String TranslatedDescription;
    private String CorrectionValue;

    public EBetErrorType getErrorType() {
        return ErrorType;
    }

    public void setErrorType(EBetErrorType errorType) {
        ErrorType = errorType;
    }

    public String getError() {
        return Error;
    }

    public void setError(String error) {
        Error = error;
    }

    public String getTranslatedMessage() {
        return TranslatedMessage;
    }

    public void setTranslatedMessage(String translatedMessage) {
        TranslatedMessage = translatedMessage;
    }

    public String getTranslatedDescription() {
        return TranslatedDescription;
    }

    public void setTranslatedDescription(String translatedDescription) {
        TranslatedDescription = translatedDescription;
    }

    public String getCorrectionValue() {
        return CorrectionValue;
    }

    public void setCorrectionValue(String correctionValue) {
        CorrectionValue = correctionValue;
    }
}
