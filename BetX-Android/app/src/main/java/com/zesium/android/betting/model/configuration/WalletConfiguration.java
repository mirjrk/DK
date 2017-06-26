package com.zesium.android.betting.model.configuration;

import java.io.Serializable;

/**
 * Created by Ivan Panic_2 on 2/27/2017.
 */

public class WalletConfiguration implements Serializable {
    private boolean HasDebitInformation;
    private boolean HasVerificationDocument;
    private boolean HasAddressInformations;
    private int VerificationDocumentStatus;
    private double AvailableBalance;
    private double MinPayout;
    private double MaxPayout;
    private boolean IsSuccess;
    private boolean ActionResult;

    public boolean isHasDebitInformation() {
        return HasDebitInformation;
    }

    public void setHasDebitInformation(boolean hasDebitInformation) {
        HasDebitInformation = hasDebitInformation;
    }

    public boolean isHasVerificationDocument() {
        return HasVerificationDocument;
    }

    public void setHasVerificationDocument(boolean hasVerificationDocument) {
        HasVerificationDocument = hasVerificationDocument;
    }

    public boolean isHasAddressInformations() {
        return HasAddressInformations;
    }

    public void setHasAddressInformations(boolean hasAddressInformations) {
        HasAddressInformations = hasAddressInformations;
    }

    public int getVerificationDocumentStatus() {
        return VerificationDocumentStatus;
    }

    public void setVerificationDocumentStatus(int verificationDocumentStatus) {
        VerificationDocumentStatus = verificationDocumentStatus;
    }

    public double getAvailableBalance() {
        return AvailableBalance;
    }

    public void setAvailableBalance(double availableBalance) {
        AvailableBalance = availableBalance;
    }

    public double getMinPayout() {
        return MinPayout;
    }

    public void setMinPayout(double minPayout) {
        MinPayout = minPayout;
    }

    public double getMaxPayout() {
        return MaxPayout;
    }

    public void setMaxPayout(double maxPayout) {
        MaxPayout = maxPayout;
    }

    public boolean isSuccess() {
        return IsSuccess;
    }

    public void setSuccess(boolean success) {
        IsSuccess = success;
    }

    public boolean isActionResult() {
        return ActionResult;
    }

    public void setActionResult(boolean actionResult) {
        ActionResult = actionResult;
    }
}
