package com.zesium.android.betting.model.payment;

/**
 * Created by Ivan Panic_2 on 2/27/2017.
 */

import java.util.List;

public class BankInfo {
    private String AccountName;
    private String Iban;
    private String BankPrefix;
    private String AccountNumber;
    private String SwiftOrBic;
    private Integer BankCountryId;
    private Integer BankId;
    private Boolean IsSuccess;
    private List<Object> ValidationResults = null;
    private Boolean ActionResult;
    private List<Object> Messages = null;

    public String getAccountName() {
        return AccountName;
    }

    public void setAccountName(String accountName) {
        AccountName = accountName;
    }

    public String getIban() {
        return Iban;
    }

    public void setIban(String iban) {
        Iban = iban;
    }

    public String getBankPrefix() {
        return BankPrefix;
    }

    public void setBankPrefix(String bankPrefix) {
        BankPrefix = bankPrefix;
    }

    public String getAccountNumber() {
        return AccountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        AccountNumber = accountNumber;
    }

    public String getSwiftOrBic() {
        return SwiftOrBic;
    }

    public void setSwiftOrBic(String swiftOrBic) {
        SwiftOrBic = swiftOrBic;
    }

    public Integer getBankCountryId() {
        return BankCountryId;
    }

    public void setBankCountryId(Integer bankCountryId) {
        BankCountryId = bankCountryId;
    }

    public Integer getBankId() {
        return BankId;
    }

    public void setBankId(Integer bankId) {
        BankId = bankId;
    }

    public Boolean getSuccess() {
        return IsSuccess;
    }

    public void setSuccess(Boolean success) {
        IsSuccess = success;
    }

    public List<Object> getValidationResults() {
        return ValidationResults;
    }

    public void setValidationResults(List<Object> validationResults) {
        ValidationResults = validationResults;
    }

    public Boolean getActionResult() {
        return ActionResult;
    }

    public void setActionResult(Boolean actionResult) {
        ActionResult = actionResult;
    }

    public List<Object> getMessages() {
        return Messages;
    }

    public void setMessages(List<Object> messages) {
        Messages = messages;
    }
}
