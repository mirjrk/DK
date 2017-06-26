package com.zesium.android.betting.model.user;

/**
 * Created by Ivan Panic_2 on 7/1/2016.
 */
public class Result {
    private Integer Id;
    private String Name;
    private String CountryCode;
    private String BankForm;
    private String AccForm;
    private Boolean Sepa;
    private Boolean Gemini;
    private Boolean IsEnabled;
    private String CountryCallingCode;
    private Integer TimeStamp;

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(String countryCode) {
        CountryCode = countryCode;
    }

    public String getBankForm() {
        return BankForm;
    }

    public void setBankForm(String bankForm) {
        BankForm = bankForm;
    }

    public String getAccForm() {
        return AccForm;
    }

    public void setAccForm(String accForm) {
        AccForm = accForm;
    }

    public Boolean getSepa() {
        return Sepa;
    }

    public void setSepa(Boolean sepa) {
        Sepa = sepa;
    }

    public Boolean getGemini() {
        return Gemini;
    }

    public void setGemini(Boolean gemini) {
        Gemini = gemini;
    }

    public Boolean getEnabled() {
        return IsEnabled;
    }

    public void setEnabled(Boolean enabled) {
        IsEnabled = enabled;
    }

    public String getCountryCallingCode() {
        return CountryCallingCode;
    }

    public void setCountryCallingCode(String countryCallingCode) {
        CountryCallingCode = countryCallingCode;
    }

    public Integer getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(Integer timeStamp) {
        TimeStamp = timeStamp;
    }
}
